package com.desarrollo.myapp.ui.pages.tenant

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.desarrollo.myapp.repository.LocalRepository
import com.desarrollo.myapp.ui.components.CardLocationTenant
import com.desarrollo.myapp.ui.components.MidOrderTopBar
import com.desarrollo.myapp.ui.components.NavBarTenant
import com.desarrollo.myapp.ui.components.OrderBottomSheet
import com.desarrollo.myapp.ui.components.SearchBar
import com.desarrollo.myapp.viewmodel.HomeTenantViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyLocalsTenant(navController: NavController) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val userId = getUserId2(context)

    val viewModel: HomeTenantViewModel = viewModel()
    val hasLocals by viewModel.hasLocals

    val locals by viewModel.locals
    val repository = remember { LocalRepository() }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    fun showSnackbar(message: String) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }


    LaunchedEffect(userId) {
        userId?.let {
            viewModel.checkIfUserHasLocals(it)
        }
    }

    val reloadLocals: () -> Unit = {
        userId?.let {
            viewModel.checkIfUserHasLocals(it)
        }
    }

    Scaffold(
        topBar = { MidOrderTopBar() },
        bottomBar = { NavBarTenant(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("registerLocal") },
                containerColor = MaterialTheme.colorScheme.primary,     // Color de fondo
                contentColor = Color.White,             // Color del ícono
                modifier = Modifier.size(74.dp)         // Tamaño del botón (por defecto es 56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
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
            SearchBar()
            Spacer(modifier = Modifier.height(16.dp))
            when (hasLocals) {
                true -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(locals.size) { index ->
                            if (index < locals.size) {
                                val local = locals[index]
                                val images =
                                    (local["pictures"] as? List<*>)?.mapNotNull { it?.toString() }
                                        ?: emptyList()

                                val documentId = local["id"] as? String ?: ""

                                CardLocationTenant(
                                    localName = local["localName"].toString(),
                                    category = local["category"].toString(),
                                    address = local["address"].toString(),
                                    urlImages = images,
                                    documentId = documentId,
                                    localRepository = repository,
                                    userId = userId ?: "",
                                    onDeleted = reloadLocals,
                                    showSnackbar = { message -> showSnackbar(message) }
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }

                false -> {
                    // Mostrar mensaje y botón centrado
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






