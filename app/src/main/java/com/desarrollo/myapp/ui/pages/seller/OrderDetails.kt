package com.desarrollo.myapp.ui.pages.seller

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.composables.icons.lucide.Boxes
import com.composables.icons.lucide.Lucide
import com.desarrollo.myapp.repository.LoginRepository
import com.desarrollo.myapp.ui.components.NavBar
import com.desarrollo.myapp.ui.components.OrderTopBarBack
import com.desarrollo.myapp.viewmodel.OrdersViewModel


@Composable
fun OrderDetails(
    navController: NavController,
    orderId: String,
    viewModel: OrdersViewModel = viewModel()
) {
    val order by viewModel.selectedOrder.collectAsState()
    val loginRepository = LoginRepository()
    val context = LocalContext.current

    LaunchedEffect(orderId) {
        viewModel.loadOrderByRef(orderId)
    }

    Scaffold(
        topBar = {
            OrderTopBarBack(navController = navController,
                onMiCuentaClick = { /* navegar a la pantalla de mi cuenta */ },
                onCerrarSesionClick = {
                    loginRepository.logout(context)
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                })
        },
        bottomBar = { NavBar(navController) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Scrollable content
            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 16.dp)
                    .padding(bottom = 140.dp) // espacio para los botones
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                if (order != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Orden  #${order?.get("orderId")}",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Icon(
                            Lucide.Boxes,
                            contentDescription = "Detalles de la orden",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "Detalles:",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Local: ${order?.get("localName")}")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Tamaño: ${order?.get("size")}")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Destinatario: ${order?.get("address")}")
                } else {
                    Text("Cargando orden...")
                }
            }

            // Fixed Bottom Buttons
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 32.dp, vertical = 8.dp)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Button(
                    onClick = {
                        // Acción para "Ver guía"
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    ),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Ver guía", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        navController.navigate("orderDetail/${orderId}/qr")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    ),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("QR", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}