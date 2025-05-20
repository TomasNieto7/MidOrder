package com.desarrollo.myapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.LogOut
import com.composables.icons.lucide.UserRound

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MidOrderTopBar(
    onMiCuentaClick: () -> Unit = {},
    onCerrarSesionClick: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text("MidOrder") },
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(Lucide.User, contentDescription = "Perfil")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                // ITEM: Mi cuenta
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = Lucide.UserRound,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    text = {
                        Text(
                            text = "Mi cuenta",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    onClick = {
                        expanded = false
                        onMiCuentaClick()
                    }
                )

                // ITEM: Cerrar sesión
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = Lucide.LogOut,
                            contentDescription = null,
                            tint = Color(0xFF974545),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    text = {
                        Text(
                            text = "Cerrar sesión",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF974545)
                        )
                    },
                    onClick = {
                        expanded = false
                        onCerrarSesionClick()
                    }
                )
            }

        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
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
                    contentDescription = "Regresar",
                    tint = Color.White
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
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,         // Color de fondo
            titleContentColor = Color.White,            // Color del texto del título
            actionIconContentColor = Color.White        // Color de los íconos de acción
        )
    )
}


