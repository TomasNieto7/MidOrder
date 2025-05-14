package com.desarrollo.myapp.ui.pages

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.desarrollo.myapp.repository.LoginRepository
import com.desarrollo.myapp.ui.components.InputLogin
import com.desarrollo.myapp.ui.components.Logo
import kotlinx.coroutines.launch

@Composable
fun LoginPage(navController: NavController) {
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val context = LocalContext.current
    val loginRepository = remember { LoginRepository() }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp, horizontal = 40.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Logo()
            Spacer(modifier = Modifier.height(20.dp))
            Inputs(emailState, passwordState)
            Spacer(modifier = Modifier.height(20.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        scope.launch {
                            val email = emailState.value.trim()
                            val password = passwordState.value

                            // Validación simple
                            if (email.isEmpty() || password.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    "Por favor, completa todos los campos",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@launch
                            }

                            val result = loginRepository.login(email, password)

                            if (result != null) {
                                saveUserId(context, result.userId)
                                when (result.role.lowercase()) {
                                    "vendedor" -> navController.navigate("homeSeller")
                                    "locatario" -> navController.navigate("homeTenant")
                                    else -> Toast.makeText(
                                        context,
                                        "Rol desconocido",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Correo o contraseña incorrectos o problema de red",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Iniciar sesión", fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "¿No estás registrado?",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(
                        text = "Regístrate",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 20.sp,
                        modifier = Modifier.clickable {
                            navController.navigate("registerUser")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Inputs(emailState: MutableState<String>, passwordState: MutableState<String>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        InputLogin("Correo", "ejemplo@mail.com", emailState)
        Spacer(modifier = Modifier.height(16.dp))
        InputLogin("Contraseña", "********", passwordState)
    }
}

fun saveUserId(context: Context, userId: String) {
    val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        putString("userId", userId)
        apply()
    }
}
