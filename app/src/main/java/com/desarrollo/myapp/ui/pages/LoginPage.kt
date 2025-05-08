package com.desarrollo.myapp.ui.pages

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
    Log.d("login", "${emailState}, ${passwordState}")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Logo()
            Spacer(modifier = Modifier.height(16.dp))
            Inputs(emailState, passwordState)
            Button(onClick = {
                scope.launch {
                    val userSession = loginRepository.login(emailState.value, passwordState.value)
                    if (userSession != null) {
                        saveUserId(context, userSession.userId)
                        when (userSession.role.lowercase()) {
                            "seller" -> navController.navigate("homeSeller")
                            "tenant" -> navController.navigate("homeTenant")
                            else -> Toast.makeText(context, "Rol desconocido", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }
                }
            }) {
                Text(text = "Iniciar sesión")
            }


        }
    }
}

@Composable
fun Inputs(emailState: MutableState<String>, passwordState: MutableState<String>) {
    Column {
        InputLogin("Correo", "ejemplo@mail.com", emailState)
        InputLogin("Contraseña", "********", passwordState)
    }
}

fun saveUserId(context: Context, userId: String) {
    val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    with (sharedPref.edit()) {
        putString("userId", userId)
        apply()
    }
}
