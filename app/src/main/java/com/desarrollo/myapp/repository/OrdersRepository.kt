package com.desarrollo.myapp.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.UUID

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
            db.collection("locals").document(localId).update("capacity", currentCapacity - usedUnits).await()

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

            result.map { document ->
                val data = document.data.toMutableMap()
                data["id"] = document.id  // agrega el id explícitamente
                data
            }
        } catch (e: Exception) {
            Log.e("OrdersRepository", "Error fetching orders by user", e)
            emptyList()
        }
    }
}

fun generateUserFriendlyId(length: Int = 8): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    return (1..length)
        .map { chars.random() }
        .joinToString("")
}
