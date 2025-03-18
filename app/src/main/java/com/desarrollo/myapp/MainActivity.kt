package com.desarrollo.myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.desarrollo.myapp.ui.theme.MyappTheme
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.PackageCheck
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyappTheme {
                Surface {
                    LoginPage()
                }
            }
        }
    }
}


@Composable
fun Logo() {
    Image(
        Lucide.PackageCheck,
        contentDescription = null,
        modifier = Modifier.size(240.dp), // Cambia 48.dp al tamaño que desees)
        colorFilter = ColorFilter.tint(Color(0xFF578FB5))
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputLogin(label: String, placeholder: String) {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        //isError = text.isEmpty(), // Ejemplo de estado de error
        modifier = Modifier.padding(10.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF578FB5),
            unfocusedBorderColor = Color(0xFF578FB5),
            cursorColor = Color.Black,
            focusedLabelColor = Color(0xFF00528A),
            unfocusedLabelColor = Color.Black,
            errorBorderColor = Color.Red,
            errorLabelColor = Color.Red
        ),
    )
}

@Composable
fun Inputs(){
    Column {
        InputLogin("Correo", "ejemplo@mail.com")
        InputLogin("Contraseña", "********")
    }
}

@Composable
fun LoginPage() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center // Centra horizontal y verticalmente
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Logo()
            Spacer(modifier = Modifier.height(16.dp))
            Inputs()
            Button(onClick = { /* Acción del botón */ }) {
                Text(text = "Haz clic aquí")
            }
        }
    }
}

@Preview
@Composable
fun PreviewLoginCard() {
    MyappTheme {
        Surface {
            LoginPage()
        }
    }
}