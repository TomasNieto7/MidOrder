package com.desarrollo.myapp.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.desarrollo.myapp.ui.components.NavBar
import com.desarrollo.myapp.ui.components.OrderBottomSheet
import com.desarrollo.myapp.ui.components.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsSeller(navController: NavController) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { MidOrderTopBar() },
        bottomBar = { NavBar(navController) },
    ) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .padding(horizontal = 16.dp)
        ) {
            Text("Mis Ubicaciones")
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