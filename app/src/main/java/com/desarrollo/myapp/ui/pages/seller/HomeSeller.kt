package com.desarrollo.myapp.ui.pages.seller

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import com.desarrollo.myapp.ui.components.CardLocation
import com.desarrollo.myapp.ui.components.MidOrderTopBar
import com.desarrollo.myapp.ui.components.NavBar
import com.desarrollo.myapp.ui.components.OrderBottomSheet
import com.desarrollo.myapp.ui.components.SearchBarInput
import com.desarrollo.myapp.viewmodel.HomeViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSeller(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val locals by viewModel.locals.collectAsState()
    val savedLocalIds by viewModel.savedLocalIds.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val userId = getUserId(context)

    LaunchedEffect(userId) {
        userId?.let { viewModel.loadSavedLocals(it) }
    }

    Scaffold(
        topBar = { MidOrderTopBar() },
        bottomBar = { NavBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("addOrders") },
                containerColor = MaterialTheme.colorScheme.primary,     // Color de fondo
                contentColor = Color.White,             // Color del ícono
                modifier = Modifier.size(74.dp)         // Tamaño del botón (por defecto es 56.dp)
            ) {
                Icon(
                    imageVector = Lucide.Plus,
                    contentDescription = "Agregar",
                    modifier = Modifier.size(38.dp)     // Tamaño del ícono
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .fillMaxSize()
        ) {
            SearchBarInput()
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Divider(modifier = Modifier.height(1.dp))
                }
                items(locals.size) { index ->
                    val local = locals[index]
                    val images = (local["pictures"] as? List<*>)?.mapNotNull { it?.toString() }
                        ?: emptyList()
                    val localId = local["id"]?.toString() ?: return@items
                    val isSaved = savedLocalIds.contains(localId)

                    CardLocation(
                        localName = local["localName"].toString(),
                        category = local["category"].toString(),
                        address = local["address"].toString(),
                        urlImages = images,
                        isAdded = isSaved,
                        onToggle = {
                            viewModel.toggleLocalSave(userId!!, localId)
                        }
                    )


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

fun getUserId(context: Context): String? {
    val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    return sharedPref.getString("userId", null)
}


