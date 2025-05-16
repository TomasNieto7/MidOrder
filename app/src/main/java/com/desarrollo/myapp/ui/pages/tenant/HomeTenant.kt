package com.desarrollo.myapp.ui.pages.tenant

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.desarrollo.myapp.ui.components.MidOrderTopBar
import com.desarrollo.myapp.ui.components.NavBarTenant
import com.desarrollo.myapp.ui.components.OrderBottomSheet
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


    LaunchedEffect(userId) {
        userId?.let {
            viewModel.checkIfUserHasLocals(it)
            viewModel.checkIfLocalHasOrders(it)
        }
    }

    Scaffold(
        topBar = { MidOrderTopBar() },
        bottomBar = { NavBarTenant(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true },
                containerColor = MaterialTheme.colorScheme.primary,     // Color de fondo
                contentColor = Color.White,             // Color del ícono
                modifier = Modifier.size(74.dp)         // Tamaño del botón (por defecto es 56.dp)
            ) {
                Icon(
                    imageVector = Lucide.ScanQrCode,
                    contentDescription = "Agregar",
                    modifier = Modifier.size(38.dp)     // Tamaño del ícono
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
                            Text("Este usuario tiene orders.") // o lista, etc.
                        }

                        false -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Tu local aún no tiene ninguna orden.",
                                        style = MaterialTheme.typography.bodyLarge,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }

                        null -> {
                            // Puedes dejar esto como un loader simple
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
                        modifier = Modifier
                            .fillMaxSize(),
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
                    // Puedes dejar esto como un loader simple
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
            OrderBottomSheet(
                onDismiss = { showBottomSheet = false },
                onContinue = { /* Lógica al continuar */ }
            )
        }
    }
}

fun getUserId2(context: Context): String? {
    val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    return sharedPref.getString("userId", null)
}


