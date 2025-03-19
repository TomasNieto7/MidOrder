package com.desarrollo.myapp.ui.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.desarrollo.myapp.ui.components.InputLogin
import com.desarrollo.myapp.ui.components.Logo

@Composable
fun LoginPage(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center // Centra horizontal y verticalmente
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Logo()
            Spacer(modifier = Modifier.height(16.dp))
            Inputs()
            Button(onClick = { navController.navigate("homeSeller") }) {
                Text(text = "Iniciar sesión")
            }
        }
    }
}

@Composable
fun Inputs(){
    Column {
        InputLogin("Correo", "ejemplo@mail.com")
        InputLogin("Contraseña", "********")
    }
}