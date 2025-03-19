package com.desarrollo.myapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    var query by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        SearchBar(
            query = query,
            onQueryChange = { query = it },
            onSearch = { /* Acción de búsqueda */ },
            active = false,
            onActiveChange = { /* Manejar el estado activo */ },
            placeholder = { Text("Buscar...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp) // Ajusta la altura según tus necesidades
        ) {
            // Contenido de resultados de búsqueda
        }
    }
}