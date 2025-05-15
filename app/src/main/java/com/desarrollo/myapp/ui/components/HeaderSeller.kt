package com.desarrollo.myapp.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.composables.icons.lucide.ArrowLeft
import com.composables.icons.lucide.Bell
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.User
import androidx.compose.material3.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MidOrderTopBar() {
    TopAppBar(
        title = { Text("MidOrder") },
        actions = {
            IconButton(onClick = { /* Acción de notificación */ }) {
                Icon(Lucide.Bell, contentDescription = "Notificaciones")
            }
            IconButton(onClick = { /* Acción de usuario */ }) {
                Icon(Lucide.User, contentDescription = "Perfil")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,         // Color de fondo
            titleContentColor = Color.White,            // Color del texto del título
            actionIconContentColor = Color.White        // Color de los íconos de acción
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTopBarBack(navController: NavController) {
    TopAppBar(
        title = { Text("MidOrder") },
        navigationIcon = {
            // Flecha de regreso
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Lucide.ArrowLeft, // Flecha hacia atrás
                    contentDescription = "Regresar"
                )
            }
        },
        actions = {
            IconButton(onClick = { /* Acción de notificación */ }) {
                Icon(Lucide.Bell, contentDescription = "Notificaciones")
            }
            IconButton(onClick = { /* Acción de usuario */ }) {
                Icon(Lucide.User, contentDescription = "Perfil")
            }
        }
    )
}


