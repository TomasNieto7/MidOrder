package com.desarrollo.myapp.repository

import android.content.Context
import com.desarrollo.myapp.model.UserSession
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class LoginRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun login(email: String, password: String): UserSession? {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val user = authResult.user ?: return null

            val docSnapshot = db.collection("users").document(user.uid).get().await()
            val role = docSnapshot.getString("role") ?: return null
            val username = docSnapshot.getString("name") ?: return null

            UserSession(user.uid, role, username)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun logout(context: Context) {
        FirebaseAuth.getInstance().signOut()
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove("userId")
            apply()
        }
    }

    fun getUserName(context: Context): String? {
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("userName", null)
    }

}
