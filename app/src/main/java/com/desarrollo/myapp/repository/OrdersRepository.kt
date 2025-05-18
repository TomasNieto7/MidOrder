package com.desarrollo.myapp.repository

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import java.util.Date

class OrdersRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getOrdersByLocal(localID: String): List<Map<String, Any>> {
        return try {
            val result = db.collection("orders")
                .whereEqualTo("local", localID)
                .get()
                .await()

            result.map { document ->
                document.data
            }
        } catch (e: Exception) {
            Log.e("OrdersRepository", "Error fetching locals by owner", e)
            emptyList()
        }
    }

    suspend fun createOrderAndUpdateCapacity(
        localId: String,
        senderId: String,
        recipientName: String,
        packageSize: String
    ): String? {
        return try {
            val localDoc = db.collection("locals").document(localId).get().await()
            val currentCapacity = localDoc.getDouble("capacity") ?: return null

            val sizeToUnits = mapOf(
                "15x15" to 1,
                "25x15" to 2,
                "35x25" to 3,
                "45x35" to 4,
                "60x40" to 5
            )
            val usedUnits = sizeToUnits[packageSize] ?: return null

            if (usedUnits > currentCapacity) return null

            val orderId = generateUserFriendlyId()
            val newOrder = hashMapOf(
                "orderId" to orderId,
                "sender" to "/users/$senderId",
                "address" to recipientName,
                "size" to packageSize,
                "quantity" to 1,
                "local" to localId,
                "location" to "/locals/$localId",
                "sent" to Date(),
                "localArrived" to Date(),
                "delivered" to Date()
            )

            db.collection("orders").add(newOrder).await()
            db.collection("locals").document(localId)
                .update("capacity", currentCapacity - usedUnits).await()

            orderId
        } catch (e: Exception) {
            Log.e("OrdersRepository", "Error creando orden", e)
            null
        }
    }

    suspend fun getOrdersByUser(userId: String): List<Map<String, Any>> {
        return try {
            val userPath = "/users/$userId"
            val result = db.collection("orders")
                .whereEqualTo("sender", userPath)
                .get()
                .await()

            result.mapNotNull { document ->
                val data = document.data.toMutableMap()
                data["id"] = document.id

                // Obtener el ID del local
                val localId = data["local"] as? String ?: return@mapNotNull null

                // Buscar el documento del local para obtener su nombre
                val localSnapshot = db.collection("locals").document(localId).get().await()
                val localName = localSnapshot.getString("localName") ?: "Local sin nombre"

                // Agregar el nombre del local al mapa
                data["localName"] = localName

                data
            }
        } catch (e: Exception) {
            Log.e("OrdersRepository", "Error fetching orders by user", e)
            emptyList()
        }
    }

    suspend fun getOrderByRef(orderRef: String): Map<String, Any>? {
        return try {
            val doc = Firebase.firestore
                .collection("orders")
                .document(orderRef)
                .get()
                .await()

            val data = doc.data?.toMutableMap() ?: return null
            data["id"] = doc.id

            // Obtener el ID del local
            val localId = data["local"] as? String ?: return null

            // Buscar el documento del local
            val localSnapshot = Firebase.firestore
                .collection("locals")
                .document(localId)
                .get()
                .await()

            val localName = localSnapshot.getString("localName") ?: "Local sin nombre"
            data["localName"] = localName

            data
        } catch (e: Exception) {
            Log.e("OrdersRepository", "Error fetching order by ref", e)
            null
        }
    }
}

fun generateUserFriendlyId(length: Int = 8): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    return (1..length)
        .map { chars.random() }
        .joinToString("")
}
