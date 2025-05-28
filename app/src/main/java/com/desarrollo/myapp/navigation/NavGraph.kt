package com.desarrollo.myapp.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.desarrollo.myapp.ui.pages.seller.HomeSeller
import com.desarrollo.myapp.ui.pages.seller.LocationsSeller
import com.desarrollo.myapp.ui.pages.LoginPage
import com.desarrollo.myapp.ui.pages.seller.OrderDetails
import com.desarrollo.myapp.ui.pages.seller.OrdersSeller
import com.desarrollo.myapp.ui.pages.seller.QRSeller
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.desarrollo.myapp.ui.pages.tenant.HomeTenant
import com.desarrollo.myapp.ui.pages.tenant.MyLocalsTenant
import com.desarrollo.myapp.ui.pages.tenant.RegisterLocal
import com.desarrollo.myapp.ui.pages.RegisterUser
import com.desarrollo.myapp.ui.pages.seller.AddOrders
import com.desarrollo.myapp.ui.pages.tenant.OrderDeliver
import com.desarrollo.myapp.ui.pages.tenant.OrderDetailsTenant
import com.desarrollo.myapp.ui.pages.tenant.ScanQR
import com.desarrollo.myapp.viewmodel.HomeViewModel
import com.desarrollo.myapp.viewmodel.OrdersViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets


@ExperimentalMaterial3Api
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "login") {
        composable("login") { LoginPage(navController) }
        composable("homeSeller") {
            val homeViewModel: HomeViewModel = viewModel()
            HomeSeller(navController = navController, viewModel = homeViewModel)
        }
        composable("ordersSeller") {
            val ordersViewModel: OrdersViewModel = viewModel()
            OrdersSeller(navController, ordersViewModel) }
        composable("locationSeller") {
            val homeViewModel: HomeViewModel = viewModel()
            LocationsSeller(navController, homeViewModel)
        }
        composable("addOrders") {
            val homeViewModel: HomeViewModel = viewModel()
            AddOrders(navController, homeViewModel)
        }

        composable("orderDetail/{orderId}") { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            OrderDetails(navController = navController, orderId = orderId)
        }

        // Ruta para la subpágina dentro de "orderDetail" (por ejemplo, ver QR)
        composable("orderDetail/{orderId}/qr") { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: return@composable
            QRSeller(orderId = orderId, navController = navController)
        }
        composable("homeTenant") {
            HomeTenant(navController = navController)
        }
        composable("myLocalsTenant") {
            MyLocalsTenant(navController = navController)
        }

        composable("registerLocal") {
            RegisterLocal(navController = navController)
        }
        composable("registerUser") {
            RegisterUser(navController = navController)
        }
        composable("scanQR") {
            ScanQR(navController = navController)
        }
        composable(
            route = "orderDeliver/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedOrderId = backStackEntry.arguments?.getString("orderId") ?: ""
            val orderId = URLDecoder.decode(encodedOrderId, StandardCharsets.UTF_8.toString())
            OrderDeliver(orderId = orderId, navController = navController)
        }

        composable("orderDetailTenant/{orderId}") { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            OrderDetailsTenant(navController = navController, orderId = orderId)
        }

    }
}

