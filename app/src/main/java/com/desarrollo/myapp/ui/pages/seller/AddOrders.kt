package com.desarrollo.myapp.ui.pages.seller

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.desarrollo.myapp.ui.components.NavBar
import com.desarrollo.myapp.ui.components.OrderTopBarBack
import com.desarrollo.myapp.ui.pages.tenant.getUserId2
import com.desarrollo.myapp.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrders(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    val userId = getUserId2(context)

    var expandedLocations by remember { mutableStateOf(false) }
    var expandedSizes by remember { mutableStateOf(false) }

    val allLocals by viewModel.locals.collectAsState()
    val savedLocalIds by viewModel.savedLocalIds.collectAsState()
    val savedLocals = allLocals.filter { it["id"].toString() in savedLocalIds }

    val sizeLabels = mapOf(
        "25x15" to "Pequeño (25x15cm)",
        "35x25" to "Mediano (35x25cm)",
        "45x35" to "Grande (45x35cm)",
        "60x40" to "Extra Grande (60x40cm)"
    )

    val sizeOptions = sizeLabels.keys.toList()

    val sizeToUnits = mapOf(
        "25x15" to 1,
        "35x25" to 2,
        "45x35" to 3,
        "60x40" to 4
    )

    var selectedLocation by remember { mutableStateOf("") }
    var selectedSize by remember { mutableStateOf("") }
    var recipientName by remember { mutableStateOf("") }

    val selectedLocal = savedLocals.find { it["localName"].toString() == selectedLocation }
    val localId = selectedLocal?.get("id").toString()
    val availableCapacity = (selectedLocal?.get("capacity") as? Number)?.toInt() ?: 0
    val requiredUnits = sizeToUnits[selectedSize] ?: 0
    val fitsInLocation = requiredUnits <= availableCapacity

    LaunchedEffect(userId) {
        userId?.let { viewModel.loadSavedLocals(it) }
    }

    Scaffold(
        topBar = { OrderTopBarBack(navController = navController) },
        bottomBar = { NavBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // --- Location Selector ---
            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outline,
                            MaterialTheme.shapes.medium
                        )
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .clickable { expandedLocations = true },
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = if (selectedLocation.isNotBlank()) selectedLocation else "Selecciona un local",
                        color = if (selectedLocation.isNotBlank()) MaterialTheme.colorScheme.onBackground else Color.Gray
                    )
                }

                DropdownMenu(
                    expanded = expandedLocations,
                    onDismissRequest = { expandedLocations = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    savedLocals.forEach { local ->
                        DropdownMenuItem(
                            text = { Text(local["localName"].toString()) },
                            onClick = {
                                selectedLocation = local["localName"].toString()
                                expandedLocations = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Package Size Selector ---
            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outline,
                            MaterialTheme.shapes.medium
                        )
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .clickable { expandedSizes = true },
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = sizeLabels[selectedSize] ?: "Dimensiones del paquete",
                        color = if (selectedSize.isNotBlank()) MaterialTheme.colorScheme.onBackground else Color.Gray
                    )
                }

                DropdownMenu(
                    expanded = expandedSizes,
                    onDismissRequest = { expandedSizes = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    sizeOptions.forEach { sizeOption ->
                        DropdownMenuItem(
                            text = { Text(sizeLabels[sizeOption] ?: sizeOption) },
                            onClick = {
                                selectedSize = sizeOption
                                expandedSizes = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            if (selectedLocation.isNotBlank() && selectedSize.isNotBlank()) {
                if (!fitsInLocation) {
                    Text(
                        text = "❌ No hay suficiente capacidad disponible en este local.",
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                } else {
                    Text(
                        text = "✅ El paquete cabe en el local seleccionado.",
                        color = Color(0xFF2E7D32),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Recipient Name ---
            OutlinedTextField(
                value = recipientName,
                onValueChange = { recipientName = it },
                label = { Text("Nombre del Destinatario") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- Add Button ---
            Button(
                onClick = {
                    if (localId.isNotBlank() && recipientName.isNotBlank() && selectedSize.isNotBlank()) {
                        viewModel.createOrder(
                            localId = localId,
                            senderId = userId!!,
                            recipientName = recipientName,
                            packageSize = selectedSize,
                            onSuccess = { orderId ->
                                navController.navigate("orderDetail/$orderId/qr")
                            },
                            onFailure = {
                                Toast.makeText(context, "Insufficient space or error", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                },
                enabled = selectedLocation.isNotBlank() && selectedSize.isNotBlank() && fitsInLocation,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF005C9A),
                    contentColor = Color.White
                ),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Agregar Orden", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Cancel Button ---
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF9C4242),
                    contentColor = Color.White
                ),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Cancelar", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
