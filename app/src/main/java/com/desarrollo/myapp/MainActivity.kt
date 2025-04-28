package com.desarrollo.myapp

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.desarrollo.myapp.navigation.NavGraph
import com.desarrollo.myapp.ui.theme.MyappTheme
import com.google.firebase.firestore.FirebaseFirestore

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: ArrayAdapter<String>
    private val TAG = "localsMain"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "Este es un mensaje de prueba")

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance()

// Realizar la consulta a Firestore
        db.collection("locals")
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    Log.d("MainActivity", "No hay documentos en la colección 'locals'.")
                } else {
                    for (document in result) {
                        Log.d("MainActivity", "${document.id} => ${document.data}")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("MainActivity", "Error obteniendo documentos.", exception)
            }


// Mensaje de prueba después de la consulta
        Log.d("MainActivity", "hola")

        setContent {
            MyappTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavGraph(navController)
                }
            }
        }
    }
}
