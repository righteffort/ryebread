package org.righteffort.ryebread.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.righteffort.ryebread.ui.screens.HomeScreen
import org.righteffort.ryebread.ui.screens.PlayerScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

// Defines the navigation routes for the application
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Player : Screen("player/{streamUrl}") {
        fun createRoute(streamUrl: String): String {
            // URL-encode the stream URL to safely pass it as a navigation argument
            val encodedUrl = URLEncoder.encode(streamUrl, StandardCharsets.UTF_8.toString())
            return "player/$encodedUrl"
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(onPlayUrl = { url ->
                navController.navigate(Screen.Player.createRoute(url))
            })
        }
        composable(
            route = Screen.Player.route,
            arguments = listOf(navArgument("streamUrl") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedUrl = backStackEntry.arguments?.getString("streamUrl") ?: ""
            val streamUrl = URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString())
            PlayerScreen(streamUrl = streamUrl)
        }
    }
}
