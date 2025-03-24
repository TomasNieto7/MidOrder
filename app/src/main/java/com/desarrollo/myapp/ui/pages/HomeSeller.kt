package com.desarrollo.myapp.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Bell
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.User
import com.desarrollo.myapp.ui.components.AddButton
import com.desarrollo.myapp.ui.components.CardLocation
import com.desarrollo.myapp.ui.components.HeaderSeller
import com.desarrollo.myapp.ui.components.NavBar
import com.desarrollo.myapp.ui.components.OrderBottomSheet
import com.desarrollo.myapp.ui.components.SearchBar
import com.desarrollo.myapp.ui.theme.MyappTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MidOrderTopBar() {
    TopAppBar(
        title = { Text("MidOrder") },
        actions = {
            IconButton(onClick = { /* Acción de notificación */ }) {
                Icon(Lucide.Bell, contentDescription = "Notificaciones")
            }
            IconButton(onClick = { /* Acción de usuario */ }) {
                Icon(Lucide.User, contentDescription = "Perfil")
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSeller(navController: NavController) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { MidOrderTopBar() },
        bottomBar = { NavBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .padding(horizontal = 16.dp)
        ) {
            SearchBar()
            Spacer(modifier = Modifier.height(16.dp))
            CardsLocation()
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet (
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            OrderBottomSheet (
                onDismiss = { showBottomSheet = false },
                onContinue = { /* Lógica al continuar */ }
            )
        }
    }
}







@Composable
fun CardsLocation() {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            CardLocation()
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            CardLocation()
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            CardLocation()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
