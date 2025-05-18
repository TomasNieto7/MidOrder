package com.desarrollo.myapp.ui.pages.seller

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.composables.icons.lucide.Boxes
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Timer
import com.desarrollo.myapp.ui.components.MidOrderTopBar
import com.desarrollo.myapp.ui.components.NavBar
import com.desarrollo.myapp.ui.components.SearchBarInput
import com.desarrollo.myapp.viewmodel.OrdersViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersSeller(
    navController: NavController,
    viewModel: OrdersViewModel = viewModel()
) {
    val orders by viewModel.orders.collectAsState()
    val context = LocalContext.current
    val userId = getUserId(context)

    LaunchedEffect(userId) {
        userId?.let {
            viewModel.loadOrdersForUser(it)
        }
    }

    Scaffold(
        topBar = { MidOrderTopBar() },
        bottomBar = { NavBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .fillMaxSize()
        ) {
            SearchBarInput()
            if (orders.isEmpty()) {
                Text("No tienes órdenes.")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(orders.size) { i ->
                        val order = orders[i]
                        OrderCard(order = order, onClick = { /* navegar a detalles */ })
                    }
                }
            }


        }
    }
}


// Tarjeta de Orden con función onClick
@Composable
fun OrderCard(order: Map<String, Any>, onClick: () -> Unit) {
    val orderId = order["orderId"]?.toString() ?: ""
    val local = order["local"]?.toString() ?: "Desconocido"
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background, contentColor = MaterialTheme.colorScheme.onSurface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
//                Icon(
//                    Lucide.Timer,
//                    contentDescription = "Tiempo restante",
//                    tint = Color(0xFF974545),
//                    modifier = Modifier.size(40.dp))
//                Spacer(Modifier.width(8.dp))
                Column {
                    Text("Orden  #$orderId", style = MaterialTheme.typography.bodyLarge)
                    Text(local, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
            Icon(
                Lucide.Boxes,
                contentDescription = "Detalles de la orden",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}
