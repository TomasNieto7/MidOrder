package com.desarrollo.myapp.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.desarrollo.myapp.ui.pages.HomeSeller
import com.desarrollo.myapp.ui.pages.LoginPage

@ExperimentalMaterial3Api
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "login") {
        composable("login") { LoginPage(navController) }
        composable("homeSeller") { HomeSeller() }
    }
}