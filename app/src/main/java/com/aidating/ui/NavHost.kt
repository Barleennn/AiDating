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
import com.aidating.ui.screens.ErrorScreen
import com.aidating.ui.screens.onboarding.AboutScreen
import com.aidating.ui.screens.onboarding.GenderScreen
import com.aidating.ui.screens.onboarding.LanguagesScreen
import com.aidating.ui.screens.onboarding.NameScreen
import com.aidating.ui.screens.onboarding.PhotosScreen

sealed class Route(val route: String) {
    data object Splash : Route("splash")
    data object Login : Route("login")
    data object Home : Route("home")
    data object Profile : Route("profile")
    // Onboarding
    data object Name : Route("onboarding/name")
    data object Gender : Route("onboarding/gender")
    data object About : Route("onboarding/about")
    data object Languages : Route("onboarding/languages")
    data object Photos : Route("onboarding/photos")
    // Error
    data object Error : Route("error")
}

@Composable
fun AppNavHost(modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Route.Splash.route, modifier = modifier) {
        composable(Route.Splash.route) { SplashScreen(onFinished = { navController.navigate(Route.Login.route) { popUpTo(Route.Splash.route) { inclusive = true } } }) }
        composable(Route.Login.route) { LoginScreen(onLoggedIn = { navController.navigate(Route.Name.route) { popUpTo(Route.Login.route) { inclusive = true } } }) }

        // Onboarding flow
        composable(Route.Name.route) {
            NameScreen(progress = 0.2f) { _ -> navController.navigate(Route.Gender.route) }
        }
        composable(Route.Gender.route) {
            GenderScreen(progress = 0.4f) { _ -> navController.navigate(Route.About.route) }
        }
        composable(Route.About.route) {
            AboutScreen(progress = 0.6f) { _ -> navController.navigate(Route.Languages.route) }
        }
        composable(Route.Languages.route) {
            LanguagesScreen(progress = 0.8f) { _ -> navController.navigate(Route.Photos.route) }
        }
        composable(Route.Photos.route) {
            PhotosScreen(progress = 1.0f) { navController.navigate(Route.Home.route) { popUpTo(Route.Login.route) { inclusive = true } } }
        }

        composable(Route.Home.route) { HomeScreen(onOpenProfile = { navController.navigate(Route.Profile.route) }) }
        composable(Route.Profile.route) { ProfileScreen(onBack = { navController.popBackStack() }) }
        composable(Route.Error.route) { ErrorScreen(message = "Не удалось загрузить данные") { navController.popBackStack() } }
    }
}
