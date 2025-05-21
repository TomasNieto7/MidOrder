package com.desarrollo.myapp.ui.pages.seller

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.desarrollo.myapp.repository.LoginRepository
import com.desarrollo.myapp.repository.QRRepository
import com.desarrollo.myapp.ui.components.NavBar
import com.desarrollo.myapp.ui.components.OrderTopBarBack
import java.io.File
import java.io.FileOutputStream

@Composable
fun QRSeller(orderId: String, navController: NavController) {
    val context = LocalContext.current
    val qrRepo = remember { QRRepository() }
    val qrBitmap = remember(orderId) { qrRepo.generateQRCodeBitmap(orderId) }
    val loginRepository = LoginRepository()

    Scaffold(
        topBar = {
            OrderTopBarBack(navController = navController,
                onMiCuentaClick = { /* navegar a la pantalla de mi cuenta */ },
                onCerrarSesionClick = {
                    loginRepository.logout(context)
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                })
        },
        bottomBar = { NavBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Recoge tu paquete",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .padding(top = 32.dp)
            )

            Text(
                text = "Muestra este código QR al personal del lugar para poder recoger tu paquete.",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Image(
                bitmap = qrBitmap.asImageBitmap(),
                contentDescription = "Código QR",
                modifier = Modifier
                    .size(300.dp)
                    .align(Alignment.CenterHorizontally)
                    .border(2.dp, Color.Black, shape = RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { shareImage(qrBitmap, context) }, // Aquí el share directo
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Compartir QR")
            }
        }
    }
}

fun shareImage(bitmap: Bitmap, context: Context) {
    try {
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()
        val file = File(cachePath, "qr_image.png")
        FileOutputStream(file).use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }

        val contentUri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, contentUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(shareIntent, "Compartir QR"))
    } catch (e: Exception) {
        e.printStackTrace()
        // Aquí puedes mostrar un Toast si quieres
    }
}



