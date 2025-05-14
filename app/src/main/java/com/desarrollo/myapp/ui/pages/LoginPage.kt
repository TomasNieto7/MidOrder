package com.desarrollo.myapp.ui.pages

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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

            // Inputs ocupando el ancho máximo
            Inputs(emailState, passwordState)

            Spacer(modifier = Modifier.height(20.dp))

            // Fila con botón y texto, ajustando al tamaño de los Inputs
            Column(
                modifier = Modifier
                    .fillMaxWidth() // Esto hace que el Row tenga el mismo ancho que los inputs
            ) {
                // Botón de inicio de sesión con el mismo tamaño que los Inputs
                Button(
                    onClick = {
                        scope.launch {
                            val userSession =
                                loginRepository.login(emailState.value, passwordState.value)
                            if (userSession != null) {
                                saveUserId(context, userSession.userId)
                                when (userSession.role.lowercase()) {
                                    "seller" -> navController.navigate("homeSeller")
                                    "tenant" -> navController.navigate("homeTenant")
                                    else -> Toast.makeText(
                                        context,
                                        "Rol desconocido",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Correo o contraseña incorrectos",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .fillMaxWidth() // El botón ocupa el mismo ancho que los inputs
                        .height(50.dp), // Altura personalizada si es necesario
                    contentPadding = PaddingValues(
                        start = 0.dp,
                        top = 8.dp,
                        end = 0.dp,
                        bottom = 8.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary      // Color del texto/íconos
                    )
                ) {
                    Text("Iniciar sesión", fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.height(16.dp)) // Espacio entre el texto y el botón

                // Texto para redirigir a registro
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
                    Spacer(modifier = Modifier.height(1.dp)) // 👈 espaciado fino
                    Text(
                        text = "Regístrate",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 20.sp,
                        modifier = Modifier.clickable { navController.navigate("registerUser") }
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
