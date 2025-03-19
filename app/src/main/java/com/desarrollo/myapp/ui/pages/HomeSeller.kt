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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.desarrollo.myapp.ui.components.AddButton
import com.desarrollo.myapp.ui.components.CardLocation
import com.desarrollo.myapp.ui.components.HeaderSeller
import com.desarrollo.myapp.ui.components.NavBar
import com.desarrollo.myapp.ui.components.SearchBar
import com.desarrollo.myapp.ui.theme.MyappTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSeller() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp), // Espacio para el NavBar
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderSeller { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Top
                ) {
                    SearchBar()
                    Spacer(modifier = Modifier.height(8.dp))
                    CardsLocation()
                }
            }
        }

        // Botón flotante (FAB) ubicado antes del NavBar en la jerarquía
        AddButton(
            modifier = Modifier
                .align(Alignment.BottomEnd) // Lo alinea en la parte inferior derecha
                .padding(bottom = 110.dp, end = 22.dp) // Lo sube respecto al NavBar
        )

        // Navbar en la parte inferior
        NavBar(
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}





@Composable
fun CardsLocation() {
    LazyColumn(
        modifier = Modifier.fillMaxSize() // Ocupa todo el espacio disponible
    ) {
        item {
            CardLocation()
            Spacer(modifier = Modifier.height(16.dp)) // Espacio entre elementos
        }
        item {
            CardLocation()
            Spacer(modifier = Modifier.height(16.dp)) // Espacio entre elementos
        }
        item {
            CardLocation()
            Spacer(modifier = Modifier.height(16.dp)) // Espacio entre elementos
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PreviewLoginCard() {
    MyappTheme {
        Surface {
            HomeSeller()
        }
    }
}