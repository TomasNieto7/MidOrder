package com.desarrollo.myapp.ui.pages.seller

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.desarrollo.myapp.repository.LocalRepository
import com.desarrollo.myapp.ui.components.NavBar
import com.desarrollo.myapp.ui.components.NavBarTenant
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
    var expandedLocals by remember { mutableStateOf(false) }

    val allLocals by viewModel.locals.collectAsState()
    val savedLocalIds by viewModel.savedLocalIds.collectAsState()
    val savedLocals = allLocals.filter { it["id"].toString() in savedLocalIds }

    var expandedSizes by remember { mutableStateOf(false) }
    val sizes =
        listOf(
            "Pequeño (25x15cm)",
            "Mediano (35x25cm)",
            "Grande (45x35cm)",
            "Extra grande (60x40cm)"
        )

    var location by remember { mutableStateOf("") }
    var size by remember { mutableStateOf("Dimensiones del paquete") }
    var nameAddress by remember { mutableStateOf("") }

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
            // --- Selector de Local ---
            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.medium)
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .clickable { expandedLocals = true },
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = if (location.isNotBlank()) location else "Seleccionar local",
                        color = if (location.isNotBlank()) MaterialTheme.colorScheme.onBackground else Color.Gray
                    )
                }

                DropdownMenu(
                    expanded = expandedLocals,
                    onDismissRequest = { expandedLocals = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    savedLocals.forEach { local ->
                        DropdownMenuItem(
                            text = { Text(local["localName"].toString()) },
                            onClick = {
                                location = local["name"].toString()
                                expandedLocals = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Selector de Tamaño ---
            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.medium)
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .clickable { expandedSizes = true },
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = size,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                DropdownMenu(
                    expanded = expandedSizes,
                    onDismissRequest = { expandedSizes = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    sizes.forEach { sizeOption ->
                        DropdownMenuItem(
                            text = { Text(sizeOption) },
                            onClick = {
                                size = sizeOption
                                expandedSizes = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Nombre del Destinatario ---
            OutlinedTextField(
                value = nameAddress,
                onValueChange = { nameAddress = it },
                label = { Text("Nombre del Destinatario") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- Botón Agregar ---
            Button(
                onClick = { /* Acción */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF005C9A),
                    contentColor = Color.White
                ),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Agregar", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Botón Cancelar ---
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






