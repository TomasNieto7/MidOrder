package com.desarrollo.myapp.ui.pages

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.desarrollo.myapp.repository.LocalRepository
import com.desarrollo.myapp.ui.components.NavBarTenant
import com.desarrollo.myapp.ui.components.OrderTopBarBack
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items


@Composable
fun RegisterLocal(navController: NavController) {

    val context = LocalContext.current
    val localRepo = LocalRepository()
    val userId = getUserId2(context)
    val imageUris = remember { mutableStateListOf<Uri>() }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.size in 1..5) {
            imageUris.clear()
            imageUris.addAll(uris)
        } else {
            Toast.makeText(context, "Selecciona entre 1 y 5 imágenes", Toast.LENGTH_SHORT).show()
        }
    }


    Scaffold(
        topBar = { OrderTopBarBack(navController = navController) },
        bottomBar = { NavBarTenant(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding) // <- padding del Scaffold
                .padding(16.dp)   // <- tu padding personalizado
                .verticalScroll(rememberScrollState())
        ) {
            var nombre by remember { mutableStateOf("") }
            var categoria by remember { mutableStateOf("") }
            var ubicacion by remember { mutableStateOf("") }
            var espacio by remember { mutableStateOf("") }
            var capacidad by remember { mutableStateOf("") }

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del Local") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Categoría") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = ubicacion,
                onValueChange = { ubicacion = it },
                label = { Text("Ubicación") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = espacio,
                onValueChange = { espacio = it },
                label = { Text("Espacio") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = capacidad,
                onValueChange = { capacidad = it },
                label = { Text("Capacidad") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Subir Imágenes (1-5)", style = MaterialTheme.typography.labelMedium)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color(0xFFCCE5FF), RoundedCornerShape(8.dp)) // Un celeste suave
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (imageUris.isNotEmpty()) {
                    LazyRow {
                        items(imageUris) { uri -> // ✅ uri es el valor real, no un índice
                            Image(
                                painter = rememberAsyncImagePainter(uri),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(4.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                } else {
                    Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Upload")
                }
            }


            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C4242))
                ) {
                    Text("Cancelar")
                }
                Button(
                    onClick = {
                        if (userId != null && imageUris.size in 1..5) {
                            localRepo.postLocal(
                                context = context,
                                nombre = nombre,
                                categoria = categoria,
                                ubicacion = ubicacion,
                                espacio = espacio,
                                capacidad = capacidad,
                                userId = userId, // ✅ ya no es nullable
                                imageUris = imageUris.toList(),
                                onSuccess = {
                                    Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack() // Regresa a la pantalla anterior
                                },
                                onFailure = {
                                    Toast.makeText(context, "Error al registrar", Toast.LENGTH_SHORT).show()
                                }
                            )
                        } else {
                            Toast.makeText(context, "Faltan datos o imágenes", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005C9A))
                ) {
                    Text("Agregar")
                }
            }
        }
    }
}






