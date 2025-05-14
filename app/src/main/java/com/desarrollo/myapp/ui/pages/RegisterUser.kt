package com.desarrollo.myapp.ui.pages

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.desarrollo.myapp.repository.RegisterRepository
import com.desarrollo.myapp.ui.components.InputLogin
import com.desarrollo.myapp.ui.components.Logo
import kotlinx.coroutines.launch


@Composable
fun RegisterUser(navController: NavController) {
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    var showPasswordError by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val roles = listOf("Vendedor", "Locatario")
    var expanded by remember { mutableStateOf(false) }
    var selectedRol by remember { mutableStateOf("Selecciona un rol") }

    val registerRepository = RegisterRepository()

    // Crear un CoroutineScope para las tareas suspendidas
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp, horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("Crear Cuenta")
        Spacer(modifier = Modifier.height(20.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            InputLogin("Nombre completo", "ejemplo@mail.com", name)
            Spacer(modifier = Modifier.height(16.dp))
            InputLogin("Correo", "ejemplo@mail.com", email)
            Spacer(modifier = Modifier.height(16.dp))
            Column {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(selectedRol)
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    roles.forEach { rol ->
                        DropdownMenuItem(
                            onClick = {
                                selectedRol = rol
                                expanded = false
                            }
                        ) {
                            Text(rol)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            InputLogin("Contraseña", "********", password)
            Spacer(modifier = Modifier.height(16.dp))
            InputLogin("Confirmar Contraseña", "********", confirmPassword)
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (showPasswordError) {
            Text(
                text = "Las contraseñas no coinciden",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(
                onClick = {
                    // Validar las contraseñas
                    if (password.value != confirmPassword.value) {
                        showPasswordError = true
                    } else {
                        showPasswordError = false
                        // Registrar al usuario
                        coroutineScope.launch {
                            val result = registerRepository.registerUser(
                                name.value,
                                email.value,
                                password.value,
                                selectedRol
                            )
                            result.fold(
                                onSuccess = {
                                    // Usuario registrado exitosamente
                                    navController.navigate("login") // O la pantalla deseada
                                },
                                onFailure = {
                                    // Mostrar error
                                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                contentPadding = PaddingValues(
                    start = 0.dp,
                    top = 8.dp,
                    end = 0.dp,
                    bottom = 8.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Registrar", fontSize = 20.sp)
            }
        }
    }
}
