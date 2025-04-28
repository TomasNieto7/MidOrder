package com.desarrollo.myapp.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
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


}