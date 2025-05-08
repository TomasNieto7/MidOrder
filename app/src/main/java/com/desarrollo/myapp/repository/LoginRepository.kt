package com.desarrollo.myapp.repository

import com.desarrollo.myapp.model.UserSession
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class LoginRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun login(email: String, password: String): UserSession? {
        return try {
            val querySnapshot = db.collection("users")
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents[0]
                val role = document.getString("role")
                val userId = document.id

                if (role != null) {
                    UserSession(userId, role)
                } else {
                    null
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
