package com.desarrollo.myapp.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class LoginRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun login(email: String, password: String): String? {
        return try {
            val querySnapshot = db.collection("users")
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents[0]
                document.getString("role")  // Devuelve el role ("seller" o "tenant")
            } else {
                null  // No encontró usuario
            }
        } catch (e: Exception) {
            null
        }
    }
}

