package com.desarrollo.myapp.ui.pages.tenant

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.desarrollo.myapp.ui.components.MidOrderTopBar
import com.desarrollo.myapp.ui.components.NavBarTenant
import com.desarrollo.myapp.ui.components.SearchBar
import com.desarrollo.myapp.viewmodel.HomeTenantViewModel

@Composable
fun MyLocalsTenant(navController: NavController) {

    val context = LocalContext.current
    val userId = getUserId2(context)

    val viewModel: HomeTenantViewModel = viewModel()
    val hasLocals by viewModel.hasLocals

    LaunchedEffect(userId) {
        userId?.let {
            viewModel.checkIfUserHasLocals(it)
        }
    }

    Scaffold(
        topBar = { MidOrderTopBar() },
        bottomBar = { NavBarTenant(navController) }
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
                    // Aquí mantienes tu UI normal cuando sí hay locales
                    Text("Este usuario tiene locales.") // o lista, etc.
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
                            Button(onClick = {
                                navController.navigate("registerLocal")
                            }) {
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
}






