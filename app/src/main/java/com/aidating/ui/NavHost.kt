package com.aidating.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aidating.ui.screens.HomeScreen
import com.aidating.ui.screens.LoginScreen
import com.aidating.ui.screens.ProfileScreen
import com.aidating.ui.screens.SplashScreen

sealed class Route(val route: String) {
    data object Splash : Route("splash")
    data object Login : Route("login")
    data object Home : Route("home")
    data object Profile : Route("profile")
}

@Composable
fun AppNavHost(modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Route.Splash.route, modifier = modifier) {
        composable(Route.Splash.route) { SplashScreen(onFinished = { navController.navigate(Route.Login.route) { popUpTo(Route.Splash.route) { inclusive = true } } }) }
        composable(Route.Login.route) { LoginScreen(onLoggedIn = { navController.navigate(Route.Home.route) { popUpTo(Route.Login.route) { inclusive = true } } }) }
        composable(Route.Home.route) { HomeScreen(onOpenProfile = { navController.navigate(Route.Profile.route) }) }
        composable(Route.Profile.route) { ProfileScreen(onBack = { navController.popBackStack() }) }
    }
}
