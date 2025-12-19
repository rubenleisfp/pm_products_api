package com.fp.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fp.Screen
import com.fp.Screen.FrontPageScreen.route
import com.fp.ui.frontpage.FrontPageScreen
import com.fp.ui.store.StoreScreen
import com.fp.ui.store.StoreViewModel

/**
 * Created by Your name on 19/12/2025.
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