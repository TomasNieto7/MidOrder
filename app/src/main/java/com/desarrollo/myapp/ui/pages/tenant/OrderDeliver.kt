package com.desarrollo.myapp.ui.pages.tenant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.desarrollo.myapp.repository.LoginRepository
import com.desarrollo.myapp.ui.components.NavBarTenant
import com.desarrollo.myapp.ui.components.OrderTopBarBack
import com.desarrollo.myapp.viewmodel.OrdersViewModel


@Composable
fun OrderDeliver(
    orderId: String, // este es el documentRef que recibes de la ruta
    navController: NavController,
    ordersViewModel: OrdersViewModel = viewModel()
) {
    val selectedOrder by ordersViewModel.selectedOrder.collectAsState()
    var isLoading by remember { mutableStateOf(true) }
    val loginRepository = LoginRepository()
    val context = LocalContext.current

    LaunchedEffect(orderId) {
        isLoading = true
        ordersViewModel.loadOrderByRef(orderId)
        isLoading = false
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
        bottomBar = { NavBarTenant(navController) }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                val orderFriendlyId = selectedOrder?.get("orderId") as? String ?: "No disponible"
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "ENTREGA ESTE PAQUETE",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                    )
                    Text(
                        text = orderFriendlyId,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = {
                            val deliverPerson = loginRepository.getUserName(context) ?: "Desconocido"
                            ordersViewModel.markOrderAsDelivered(orderId, deliverPerson) { success ->
                                if (success) {
                                    navController.navigate("homeTenant")
                                }
                            }
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
                        Text("ENTREGADO")
                    }
                }
            }
        }
    }
}
