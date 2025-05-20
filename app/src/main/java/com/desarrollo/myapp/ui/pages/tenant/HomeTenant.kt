package com.desarrollo.myapp.ui.pages.tenant

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.ScanQrCode
import com.composables.icons.lucide.Boxes
import com.desarrollo.myapp.repository.LoginRepository
import com.desarrollo.myapp.ui.components.MidOrderTopBar
import com.desarrollo.myapp.ui.components.NavBarTenant
import com.desarrollo.myapp.ui.components.SearchBarInput
import com.desarrollo.myapp.viewmodel.HomeTenantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTenant(navController: NavController) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val userId = getUserId2(context)

    val viewModel: HomeTenantViewModel = viewModel()
    val hasLocals by viewModel.hasLocals
    val hasOrders by viewModel.hasOrders
    val orders by viewModel.orders
    val loginRepository = LoginRepository()

    LaunchedEffect(userId) {
        userId?.let {
            viewModel.checkIfUserHasLocals(it)
        }
    }

    Scaffold(
        topBar = {
            MidOrderTopBar(
                onMiCuentaClick = { /* navegar a la pantalla de mi cuenta */ },
                onCerrarSesionClick = {
                    loginRepository.logout(context)
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                }
            )
        },
        bottomBar = { NavBarTenant(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("scanQR") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.size(74.dp)
            ) {
                Icon(
                    imageVector = Lucide.ScanQrCode,
                    contentDescription = "Agregar",
                    modifier = Modifier.size(38.dp)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            SearchBarInput()
            Spacer(modifier = Modifier.height(16.dp))
            when (hasLocals) {
                true -> {
                    when (hasOrders) {
                        true -> {
                            Column {
                                orders.forEach { order ->
                                    OrderCard(order = order, onClick = {
                                        // Navegar a detalle de la orden, ejemplo:
                                        navController.navigate("orderDetail/${order["orderId"]}")
                                    })
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }

                        false -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Tu local aún no tiene ninguna orden.",
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        null -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }

                false -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "No tienes ningún local registrado",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    navController.navigate("registerLocal")
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Registrar local")
                            }
                        }
                    }
                }

                null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            // OrderBottomSheet composable que ya tienes definido
            // OrderBottomSheet(onDismiss = { showBottomSheet = false }, onContinue = { /*...*/ })
        }
    }
}

fun getUserId2(context: Context): String? {
    val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    return sharedPref.getString("userId", null)
}


// Tarjeta de Orden con función onClick
@Composable
fun OrderCard(order: Map<String, Any>, onClick: () -> Unit) {
    val orderId = order["orderId"]?.toString() ?: ""
    val localName = order["localName"]?.toString() ?: "Desconocido"
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Orden  #$orderId", style = MaterialTheme.typography.bodyLarge)
                    Text(localName, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
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
