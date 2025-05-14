package com.desarrollo.myapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.composables.icons.lucide.ChevronLeft
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.Lucide
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@Composable
fun CardLocation(
    localName: String,
    category: String,
    address: String,
    urlImages: List<String>,
    isAdded: Boolean
) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    // Estado para saber si las imágenes están cargando
    val isLoading = remember { mutableStateOf(true) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            if (urlImages.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    // PAGER
                    HorizontalPager(
                        count = urlImages.size,
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxSize()
                    ) { page ->
                        AsyncImage(
                            model = urlImages[page],
                            contentDescription = "Imagen del lugar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                            onLoading = {
                                // Mientras la imagen carga, establecer isLoading en true
                                isLoading.value = true
                            },
                            onSuccess = {
                                // Cuando la imagen se carga, establecer isLoading en false
                                isLoading.value = false
                            }
                        )
                    }

                    // Muestra el loading shimmer mientras carga
                    if (isLoading.value) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Gray.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            // Puedes usar un CircularProgressIndicator o un shimmer effect
                            CircularProgressIndicator(
                                modifier = Modifier.size(40.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    // Flechas de navegación
                    if (urlImages.size > 1) {
                        // Flecha derecha
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(8.dp)
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.2f))
                                .clickable {
                                    coroutineScope.launch {
                                        val next =
                                            (pagerState.currentPage + 1).coerceAtMost(urlImages.lastIndex)
                                        pagerState.animateScrollToPage(next)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Lucide.ChevronRight,
                                contentDescription = "Siguiente",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        // Flecha izquierda
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(8.dp)
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.2f))
                                .clickable {
                                    coroutineScope.launch {
                                        val prev = (pagerState.currentPage - 1).coerceAtLeast(0)
                                        pagerState.animateScrollToPage(prev)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Lucide.ChevronLeft,
                                contentDescription = "Anterior",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = localName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = category,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = address,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { /* Acción */ },
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isAdded) MaterialTheme.colorScheme.primary else Color(
                            0xFF974545
                        )
                    )
                ) {
                    Text(if (isAdded) "Agregar" else "Eliminar")
                }
            }
        }
    }
}

@Composable
fun CardLocationTenant(
    localName: String,
    category: String,
    address: String,
    urlImages: List<String>,
    isAdded: Boolean
) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    // Estado para saber si las imágenes están cargando
    val isLoading = remember { mutableStateOf(true) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            if (urlImages.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    // PAGER
                    HorizontalPager(
                        count = urlImages.size,
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxSize()
                    ) { page ->
                        AsyncImage(
                            model = urlImages[page],
                            contentDescription = "Imagen del lugar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                            onLoading = {
                                isLoading.value = true
                            },
                            onSuccess = {
                                isLoading.value = false
                            }
                        )
                    }

                    // Muestra el loading shimmer mientras carga
                    if (isLoading.value) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Gray.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(40.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    // Flechas de navegación
                    if (urlImages.size > 1) {
                        // Flecha derecha
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(8.dp)
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.2f))
                                .clickable {
                                    coroutineScope.launch {
                                        val next =
                                            (pagerState.currentPage + 1).coerceAtMost(urlImages.lastIndex)
                                        pagerState.animateScrollToPage(next)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Lucide.ChevronRight,
                                contentDescription = "Siguiente",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        // Flecha izquierda
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(8.dp)
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.2f))
                                .clickable {
                                    coroutineScope.launch {
                                        val prev = (pagerState.currentPage - 1).coerceAtLeast(0)
                                        pagerState.animateScrollToPage(prev)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Lucide.ChevronLeft,
                                contentDescription = "Anterior",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    // FAB arriba a la izquierda - al final del Box para que esté visible encima
                    FloatingActionButton(
                        onClick = { showBottomSheet = true },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .size(70.dp),
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White,
                        elevation = FloatingActionButtonDefaults.elevation(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Agregar",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = localName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = category,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = address,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { /* Acción */ },
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isAdded)
                            MaterialTheme.colorScheme.primary
                        else
                            Color(0xFF974545)
                    )
                ) {
                    Text("Eliminar")
                }
            }
        }
    }
}



