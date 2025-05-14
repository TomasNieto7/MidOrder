package com.desarrollo.myapp.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class RegisterRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = Firebase.firestore

    // Función para registrar un usuario
    suspend fun registerUser(name: String, email: String, password: String, role: String): Result<String> {
        return try {
            // Crear el usuario en Firebase Authentication
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()

            // Obtener el UID del usuario
            val userId = authResult.user?.uid ?: throw Exception("Error al obtener UID del usuario")

            // Almacenar los detalles adicionales del usuario en Firestore
            val userData = hashMapOf(
                "name" to name,
                "email" to email,
                "role" to role
            )

            // Guardar en Firestore en una colección de "users"
            db.collection("users").document(userId).set(userData).await()

            // Si todo va bien, devolvemos un resultado exitoso
            Result.success("Usuario registrado exitosamente")
        } catch (e: Exception) {
            // Si ocurre algún error, devolvemos el error
            Result.failure(e)
        }
    }
}
