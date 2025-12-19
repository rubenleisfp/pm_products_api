package com.fp.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fp.Screen
import com.fp.ui.frontpage.FrontPageScreen
import com.fp.ui.store.StoreScreen
import com.fp.ui.store.StoreViewModel

/**
 * Sets up the navigation graph for the application.
 *
 * This composable function is responsible for creating a `NavController`
 * and configuring a `NavHost` with all the possible navigation routes. It defines
 * the entry points for different screens like [FrontPageScreen] and [StoreScreen],
 * and handles the navigation logic between them.
 *
 * The `startDestination` is set to the [FrontPageScreen].
 */
@Composable
fun NavigationStack() {
    // Crea un NavController para administrar la navegación de la app
    val navController = rememberNavController()
    // Configura NavHost para definir las rutas de navegación y la ruta de inicio
    NavHost(navController = navController, startDestination = Screen.FrontPageScreen.route) {
        // Define una composable para la pantalla principal
        composable(route = Screen.FrontPageScreen.route) {
            // MainScreen es el punto de partida de la navegación
            FrontPageScreen(navController = navController)
        }
        // Define una composable para la pantalla de detalles con un argumento opcional "text"
        composable(
            route = Screen.StoreScreen.route
        ) {
            // Pasa el argumento "text" a la pantalla de detalles
            val storeViewModel: StoreViewModel =
                viewModel(factory = StoreViewModel.Factory)
            storeViewModel.loadProducts()
            StoreScreen(navController = navController, storeViewModel)
        }
    }

}