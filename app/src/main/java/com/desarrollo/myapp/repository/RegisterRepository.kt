import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayInputStream

class RegisterRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = Firebase.firestore
    private val storage = FirebaseStorage.getInstance()

    suspend fun registerUser(
        name: String,
        email: String,
        password: String,
        role: String
    ): Result<String> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: throw Exception("Error al obtener UID del usuario")

            val userData = hashMapOf(
                "name" to name,
                "email" to email,
                "role" to role
            )

            // Guardar los datos en Firestore
            db.collection("users").document(userId).set(userData).await()

            // Si el rol es Locatario, crear carpeta en Firebase Storage
            if (role.lowercase() == "locatario") {
                createLocalsFolder(userId)
            }

            Result.success("Usuario registrado exitosamente")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun createLocalsFolder(userId: String) {
        // Firebase Storage no tiene carpetas, así que subimos un archivo vacío
        val dummyContent = ByteArrayInputStream(ByteArray(0))
        val storageRef = storage.reference.child("users/$userId/locals/.nomedia")

        storageRef.putStream(dummyContent).await()
    }
}
