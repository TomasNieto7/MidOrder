package com.desarrollo.myapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddButton(modifier: Modifier = Modifier) {
    FloatingActionButton(
        onClick = { /* Acción del FAB */ },
        modifier = modifier
            .size(70.dp) // Ajusta el tamaño del FAB si es necesario
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Agregar",
            modifier = Modifier.size(30.dp) // Ajusta el tamaño del ícono si es necesario
        )
    }
}