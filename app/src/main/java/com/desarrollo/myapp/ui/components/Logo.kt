package com.desarrollo.myapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.PackageCheck

@Composable
fun Logo() {
    Image(
        Lucide.PackageCheck,
        contentDescription = null,
        modifier = Modifier.size(240.dp), // Cambia 48.dp al tamaño que desees)
        colorFilter = ColorFilter.tint(Color(0xFF578FB5))
    )
}