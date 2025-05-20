package com.desarrollo.myapp.ui.pages.tenant

import android.Manifest
import android.annotation.SuppressLint
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.concurrent.Executors

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScanQR(navController: NavController) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    if (cameraPermissionState.status.isGranted) {
        QRScannerView { qrValue ->
            // Codifica el valor antes de navegar
            val encodedQrValue = URLEncoder.encode(qrValue, StandardCharsets.UTF_8.toString())
            navController.navigate("orderDeliver/$encodedQrValue")
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Se requiere permiso de cámara para escanear QR.")
        }
    }
}

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun QRScannerView(
    onQRCodeScanned: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }
    val coroutineScope = rememberCoroutineScope()
    var scanned by remember { mutableStateOf(false) }

    AndroidView(
        factory = { previewView },
        modifier = Modifier.fillMaxSize()
    )

    DisposableEffect(Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        val executor = Executors.newSingleThreadExecutor()

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            val scanner = BarcodeScanning.getClient()

            imageAnalysis.setAnalyzer(executor, { imageProxy ->
                val mediaImage = imageProxy.image
                if (mediaImage != null && !scanned) {
                    val image =
                        InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

                    scanner.process(image)
                        .addOnSuccessListener { barcodes ->
                            for (barcode in barcodes) {
                                barcode.rawValue?.let { qrCode ->
                                    scanned = true

                                    // Espera 500ms antes de llamar a onQRCodeScanned para evitar error cámara
                                    coroutineScope.launch {
                                        delay(500)
                                        onQRCodeScanned(qrCode)
                                    }
                                }
                            }
                        }
                        .addOnFailureListener {
                            // Manejar errores si se desea
                        }
                        .addOnCompleteListener {
                            imageProxy.close()
                        }
                } else {
                    imageProxy.close()
                }
            })

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner, cameraSelector, preview, imageAnalysis
            )
        }, ContextCompat.getMainExecutor(context))

        onDispose {
            cameraProviderFuture.get().unbindAll()
        }
    }

    QRScannerOverlay()
}

@Composable
fun QRScannerOverlay() {
    Box(modifier = Modifier.fillMaxSize()) {
        val transparentRectSize = 250.dp
        val overlayColor = Color.Black.copy(alpha = 0.6f)

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(overlayColor)
        )

        Box(
            modifier = Modifier
                .size(transparentRectSize)
                .align(Alignment.Center)
                .background(Color.Transparent)
        )

        Box(
            modifier = Modifier
                .size(transparentRectSize)
                .align(Alignment.Center)
                .border(2.dp, Color.White)
        )
    }
}
