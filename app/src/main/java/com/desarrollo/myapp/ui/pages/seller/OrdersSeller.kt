package com.desarrollo.myapp.ui.pages.seller

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.composables.icons.lucide.Boxes
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Timer
import com.desarrollo.myapp.ui.components.MidOrderTopBar
import com.desarrollo.myapp.ui.components.NavBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersSeller(navController: NavController) {
    // Lista de órdenes
    val orders = listOf(
        OrderDetail(1234, "Local 1", "20 cm x 20 cm", "Tomas Alberto", "--/--/--", "10/03/2025"),
        OrderDetail(1232, "Local 2", "15 cm x 15 cm", "María López", "01/04/2025", "15/04/2025"),
        OrderDetail(1233, "Local 3", "30 cm x 30 cm", "Carlos Rivera", "05/05/2025", "20/05/2025")
    )

    Scaffold(
        topBar = { MidOrderTopBar() },
        bottomBar = { NavBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Mis órdenes",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(orders.size) { order ->
                    OrderCard(orders[order]) {
                        // Navegar a la subpágina de detalles con el ID de la orden
                        navController.navigate("orderDetail/${orders[order].id}")
                    }
                }
            }
            Button(
                onClick = { /* Acción para mostrar QR */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9BAF5)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Imprimir etiquetas")
            }
        }
    }
}



// Tarjeta de Orden con función onClick
@Composable
fun OrderCard(order: OrderDetail, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }, // Detecta clics en la tarjeta
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFF002D55)) // Borde azul como en la imagen
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Lucide.Timer, contentDescription = "Tiempo restante")
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Orden  #${order.id}", style = MaterialTheme.typography.bodyLarge)
                    Text(order.local, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
            Icon(Lucide.Boxes, contentDescription = "Detalles de la orden")
        }
    }
}

// Modelo de datos de la orden
data class OrderDetail(
    val id: Int,
    val local: String,
    val packageSize: String,
    val sender: String,
    val pickupDate: String,
    val deliveryDate: String
)
