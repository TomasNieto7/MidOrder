package com.desarrollo.myapp.ui.pages.seller

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.desarrollo.myapp.ui.components.NavBar
import com.desarrollo.myapp.ui.components.OrderTopBarBack

@Composable
fun QRSeller(orderId: Int, navController: NavController) {
    Scaffold(
        topBar = { OrderTopBarBack(navController = navController) },
        bottomBar = { NavBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Título centrado
            Text(
                text = "Recoge tu paquete",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .padding(top = 32.dp)
            )

            Text(
                text = "Muestra este código QR al personal del lugar para poder recoger tu paquete.",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 16.sp,
                textAlign = TextAlign.Justify, // Justificación del texto
                modifier = Modifier
                    .fillMaxWidth() // Asegura que el texto ocupe todo el ancho
                    .padding(top = 16.dp)
                    .padding(horizontal = 32.dp) // Espaciado horizontal
            )


            // Cuadro simulado para el QR, centrado
            Box(
                modifier = Modifier
                    .size(300.dp) // Tamaño del cuadro del QR
                    .padding(top = 32.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(16.dp)) // Fondo negro
                    .border(2.dp, Color.White, shape = RoundedCornerShape(16.dp)) // Borde blanco
                    .align(Alignment.CenterHorizontally) // Centrado en el eje horizontal
            ) {
                // Aquí puedes agregar el código QR real si lo tienes
                // Por ejemplo, puedes colocar una imagen o algún contenido simulado
                // Por ahora, es solo un cuadro negro con borde blanco.
            }

            Spacer(modifier = Modifier.weight(1f)) // Empuja el botón hacia abajo

            // Botón en la parte inferior
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9BAF5)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Compartir QR")
            }
        }
    }
}


