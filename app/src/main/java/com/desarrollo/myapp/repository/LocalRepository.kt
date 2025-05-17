package com.desarrollo.myapp.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

class LocalRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getLocals(): List<Map<String, Any>> {
        return try {
            val result = db.collection("locals").get().await()
            result.map { document ->
                val data = document.data.toMutableMap()
                data["id"] = document.id  // 👈 Agrega el ID del documento
                data
            }
        } catch (e: Exception) {
            Log.e("LocalRepository", "Error fetching locals", e)
            emptyList()
        }
    }


    suspend fun getLocalsByOwner(userId: String): List<Map<String, Any>> {
        return try {
            val snapshot = db.collection("locals")
                .whereEqualTo("owner", userId)
                .get()
                .await()

            snapshot.documents.map { doc ->
                val data = doc.data ?: emptyMap<String, Any>()
                // Agregamos el documentId en el mapa con la clave "id"
                data + ("id" to doc.id)
            }
        } catch (e: Exception) {
            // Aquí podrías hacer logging o manejo de errores más avanzado si quieres
            emptyList()
        }
    }

    fun postLocal(
        context: Context,
        nombre: String,
        categoria: String,
        ubicacion: String,
        capacidad: Int,
        userId: String,
        imageUris: List<Uri>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val TAG = "PostLocal"
        val db = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()

        val localData = hashMapOf(
            "localName" to nombre,
            "category" to categoria,
            "address" to ubicacion,
            "capacity" to capacidad,
            "owner" to userId
        )

        db.collection("locals")
            .add(localData)
            .addOnSuccessListener { documentRef ->
                val localId = documentRef.id
                val imageUrls = mutableListOf<String>()
                val uploadedRefs = mutableListOf<StorageReference>()
                val storageRefBase = storage.reference.child("users/$userId/locals/$localId")

                var completedCount = 0
                var failed = false

                fun handleFailure(reason: Exception) {
                    Log.e(TAG, "Fallo: ${reason.message}", reason)
                    uploadedRefs.forEach { ref ->
                        ref.delete().addOnSuccessListener {
                            Log.d(TAG, "Imagen eliminada: ${ref.path}")
                        }.addOnFailureListener {
                            Log.e(TAG, "Error al eliminar imagen: ${ref.path}", it)
                        }
                    }

                    documentRef.delete()
                        .addOnSuccessListener {
                            Log.d(TAG, "Documento eliminado tras error")
                            onFailure(reason)
                        }
                        .addOnFailureListener {
                            Log.e(TAG, "Error al eliminar documento tras fallo", it)
                            onFailure(reason)
                        }
                }

                fun checkCompletion() {
                    if (completedCount == imageUris.size) {
                        if (failed) {
                            handleFailure(Exception("Una o más imágenes fallaron"))
                        } else {
                            documentRef.update("pictures", imageUrls)
                                .addOnSuccessListener {
                                    Log.d(TAG, "Documento actualizado con URLs")
                                    onSuccess()
                                }
                                .addOnFailureListener {
                                    Log.e(TAG, "Error al actualizar documento", it)
                                    handleFailure(it)
                                }
                        }
                    }
                }

                imageUris.forEachIndexed { index, uri ->
                    val imageRef = storageRefBase.child("image_$index.jpg")
                    Log.d(TAG, "Subiendo imagen $index a ${imageRef.path}")
                    imageRef.putFile(uri)
                        .continueWithTask { task ->
                            if (!task.isSuccessful) {
                                Log.e(TAG, "Error subiendo imagen $index", task.exception)
                                throw task.exception ?: Exception("Error al subir imagen")
                            }
                            imageRef.downloadUrl
                        }
                        .addOnSuccessListener { downloadUrl ->
                            Log.d(TAG, "Imagen $index subida correctamente")
                            imageUrls.add(downloadUrl.toString())
                            uploadedRefs.add(imageRef)
                            completedCount++
                            checkCompletion()
                        }
                        .addOnFailureListener {
                            Log.e(TAG, "Fallo subiendo imagen $index", it)
                            failed = true
                            completedCount++
                            checkCompletion()
                        }
                }
            }
            .addOnFailureListener {
                Log.e(TAG, "Error al crear documento Firestore", it)
                onFailure(it)
            }
    }

    fun deleteLocal(
        documentId: String,
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()
        val documentRef = db.collection("locals").document(documentId)

        documentRef.delete()
            .addOnSuccessListener {
                val storageRef = storage.reference.child("users/$userId/locals/$documentId")

                storageRef.listAll()
                    .addOnSuccessListener { listResult ->
                        val deleteTasks = listResult.items.map { it.delete() }

                        if (deleteTasks.isEmpty()) {
                            onSuccess()
                        } else {
                            Tasks.whenAll(deleteTasks)
                                .addOnSuccessListener {
                                    onSuccess()
                                }
                                .addOnFailureListener { e ->
                                    onFailure(e)
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.w("Storage", "No se pudieron listar imágenes: ${e.message}")
                        onSuccess()
                    }
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun saveLocalReference(
        userId: String,
        localId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val localRef = db.collection("locals").document(localId)
        val data = mapOf(
            "localRef" to localRef,
            "savedAt" to FieldValue.serverTimestamp()
        )

        db.collection("users")
            .document(userId)
            .collection("savedLocals")
            .document(localId)
            .set(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    suspend fun getSavedLocals(userId: String): List<String> {
        return try {
            val snapshot = db.collection("users")
                .document(userId)
                .collection("savedLocals")
                .get()
                .await()

            snapshot.documents.map { it.id }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun saveLocalForUser(userId: String, localId: String) {
        db.collection("users")
            .document(userId)
            .collection("savedLocals")
            .document(localId)
            .set(mapOf("saved" to true)) // puedes guardar más info si quieres
            .await()
    }

    suspend fun removeLocalFromUser(userId: String, localId: String) {
        db.collection("users")
            .document(userId)
            .collection("savedLocals")
            .document(localId)
            .delete()
            .await()
    }


}