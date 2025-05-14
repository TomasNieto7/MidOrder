package com.desarrollo.myapp.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

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
}