package com.desarrollo.myapp.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class LocalRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getLocals(): List<Map<String, Any>> {
        return try {
            val result = db.collection("locals").get().await()
            result.map { document ->
                document.data
            }
        } catch (e: Exception) {
            Log.e("LocalRepository", "Error fetching locals", e)
            emptyList()
        }
    }


    suspend fun getLocalsByOwner(ownerRef: DocumentReference): List<Map<String, Any>> {
        return try {
            val result = db.collection("locals")
                .whereEqualTo("owner", ownerRef)
                .get()
                .await()

            result.map { document ->
                document.data
            }
        } catch (e: Exception) {
            Log.e("LocalRepository", "Error fetching locals by owner", e)
            emptyList()
        }
    }

    fun postLocal(
        context: Context,
        nombre: String,
        categoria: String,
        ubicacion: String,
        espacio: String,
        capacidad: String,
        userId: String,
        imageUris: List<Uri>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()

        val localData = hashMapOf(
            "nombre" to nombre,
            "categoria" to categoria,
            "ubicacion" to ubicacion,
            "espacio" to espacio,
            "capacidad" to capacidad,
            "ref" to userId
        )

        db.collection("locals")
            .add(localData)
            .addOnSuccessListener { documentRef ->
                val localId = documentRef.id
                val imageUrls = mutableListOf<String>()

                val storageRefBase = storage.reference.child("users/$userId/locals/$localId")

                var uploadedCount = 0
                imageUris.forEachIndexed { index, uri ->
                    val imageRef = storageRefBase.child("image_$index.jpg")
                    imageRef.putFile(uri)
                        .continueWithTask { task ->
                            if (!task.isSuccessful) throw task.exception ?: Exception("Error en subida")
                            imageRef.downloadUrl
                        }
                        .addOnSuccessListener { uriDownload ->
                            imageUrls.add(uriDownload.toString())
                            uploadedCount++
                            if (uploadedCount == imageUris.size) {
                                documentRef.update("imagenes", imageUrls)
                                onSuccess()
                            }
                        }
                        .addOnFailureListener { onFailure(it) }
                }
            }
            .addOnFailureListener { onFailure(it) }
    }



}