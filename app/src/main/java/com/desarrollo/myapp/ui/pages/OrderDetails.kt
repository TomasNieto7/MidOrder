package com.desarrollo.myapp.ui.pages

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Box
import com.composables.icons.lucide.Lucide
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.desarrollo.myapp.ui.components.NavBar

@Composable
fun OrderDetails(orderId: Int, navController: NavController) {
    // Aquí recuperamos la orden correspondiente, puedes usar un ViewModel o un repositorio
    val orders = listOf(
        OrderDetail(1234, "Local 1", "20 cm x 20 cm", "Tomas Alberto", "--/--/--", "10/03/2025"),
        OrderDetail(1232, "Local 2", "15 cm x 15 cm", "María López", "01/04/2025", "15/04/2025"),
        OrderDetail(1233, "Local 3", "30 cm x 30 cm", "Carlos Rivera", "05/05/2025", "20/05/2025")
    )
    val order = orders.firstOrNull { it.id == orderId } ?: return

    Scaffold (
        topBar = { MidOrderTopBar() }, // Reutilizamos el mismo TopBar
        bottomBar = { NavBar(navController) } // Reutilizamos el mismo NavBar
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Orden  #${order.id}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Detalles", fontWeight = FontWeight.Bold)
            Text(text = order.local)
            Text(text = "ID del local: #${order.id}")
            Text(text = "Paquete: ${order.packageSize}")
            Text(text = "Remitente: ${order.sender}")
            Text(text = "Fecha de recolección: ${order.pickupDate}")
            Text(text = "Fecha de entrega: ${order.deliveryDate}")

            Spacer(modifier = Modifier.height(32.dp))

            // Botones
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { /* Acción para Ver guía */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9BAF5)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver guía")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { /* Acción para mostrar QR */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9BAF5)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("QR")
                }
            }
        }
    }
}


