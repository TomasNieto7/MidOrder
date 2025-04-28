package com.desarrollo.myapp.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.desarrollo.myapp.ui.pages.HomeSeller
import com.desarrollo.myapp.ui.pages.LocationsSeller
import com.desarrollo.myapp.ui.pages.LoginPage
import com.desarrollo.myapp.ui.pages.OrderDetails
import com.desarrollo.myapp.ui.pages.OrdersSeller
import com.desarrollo.myapp.ui.pages.QRSeller
import androidx.lifecycle.viewmodel.compose.viewModel
import com.desarrollo.myapp.viewmodel.HomeViewModel


@ExperimentalMaterial3Api
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "login") {
        composable("login") { LoginPage(navController) }
        composable("homeSeller") {
            val homeViewModel: HomeViewModel = viewModel()
            HomeSeller(navController = navController, viewModel = homeViewModel)
        }
        composable("ordersSeller") { OrdersSeller(navController) }
        composable("locationSeller") { LocationsSeller(navController) }

        // Ruta para la subpágina de detalles de la orden
        composable("orderDetail/{orderId}") { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId")?.toInt() ?: return@composable
            OrderDetails(orderId = orderId, navController = navController)
        }

        // Ruta para la subpágina dentro de "orderDetail" (por ejemplo, ver QR)
        composable("orderDetail/{orderId}/qr") { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId")?.toInt() ?: return@composable
            QRSeller(orderId = orderId, navController = navController)
        }
    }
}

