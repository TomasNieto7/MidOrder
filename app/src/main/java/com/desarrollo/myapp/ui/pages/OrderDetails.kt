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
import androidx.compose.material.icons.filled.ShoppingCart
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
import com.composables.icons.lucide.Boxes
import com.desarrollo.myapp.ui.components.NavBar
import com.desarrollo.myapp.ui.components.OrderTopBarBack

@Composable
fun OrderDetails(orderId: Int, navController: NavController) {
    // Lista de pedidos simulados
    val orders = listOf(
        OrderDetail(1234, "Local 1", "20 cm x 20 cm", "Tomas Alberto", "--/--/--", "10/03/2025"),
        OrderDetail(1232, "Local 2", "15 cm x 15 cm", "María López", "01/04/2025", "15/04/2025"),
        OrderDetail(1233, "Local 3", "30 cm x 30 cm", "Carlos Rivera", "05/05/2025", "20/05/2025")
    )
    val order = orders.firstOrNull { it.id == orderId } ?: return

    Scaffold(
        topBar = { OrderTopBarBack(navController = navController) },
        bottomBar = { NavBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize() // Esto hace que el Column ocupe todo el espacio disponible
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp) // Usar verticalArrangement aquí
            ) {
                // Mostrar la información de la orden
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Orden  #${order.id}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        fontSize = 28.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Lucide.Boxes,
                        contentDescription = "Logo",
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Detalles", fontWeight = FontWeight.Bold)
                Text(text = order.local)
                Text(text = "ID del local: #${order.id}")
                Text(text = "Paquete: ${order.packageSize}")
                Text(text = "Remitente: ${order.sender}")
                Text(text = "Fecha de recolección: ${order.pickupDate}")
                Text(text = "Fecha de entrega: ${order.deliveryDate}")
            }

            // Spacer para empujar los botones hacia abajo
            Spacer(modifier = Modifier.weight(1f)) // Esto empuja los botones hacia abajo

            // Agregamos los botones en la parte inferior
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
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
                    onClick = { navController.navigate("orderDetail/${order.id}/qr") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9BAF5)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver QR")
                }
            }
        }
    }
}






