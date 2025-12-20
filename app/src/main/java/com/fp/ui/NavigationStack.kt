package com.fp.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fp.Screen
import com.fp.data.FavoriteProduct
import com.fp.ui.favorite.FavoriteViewModel
import com.fp.ui.favorite.FavoritesScreen
import com.fp.ui.frontpage.FrontPageScreen
import com.fp.ui.store.StoreScreen
import com.fp.ui.store.StoreViewModel

/**
 * Composable function that sets up the navigation graph for the application.
 *
 * This function uses Jetpack Compose Navigation to define the different screens (routes)
 * and the Composables that should be displayed for each route. It initializes the
 * `NavController` and the necessary `ViewModel`s, and configures the `NavHost`
 * with the application's navigation flow.
 *
 * The defined routes are:
 * - `FrontPageScreen`: The initial screen of the app.
 * - `StoreScreen`: Displays the list of products.
 * - `FavoriteScreen`: Displays the user's favorite products.
 */
@Composable
fun NavigationStack() {

    // Crea un NavController para administrar la navegación de la app
    val navController = rememberNavController()
    // Recuperamos los viewModel para mostrarlos por pantalla
    val storeViewModel: StoreViewModel =
        viewModel(factory = StoreViewModel.Factory)

    val favoriteViewModel: FavoriteViewModel =
        viewModel(factory = FavoriteViewModel.Factory)


    // Configura NavHost para definir las rutas de navegación y la ruta de inicio
    NavHost(navController = navController, startDestination = Screen.FrontPageScreen.route) {

        // Define una composable para la pantalla principal
        composable(route = Screen.FrontPageScreen.route) {
            // MainScreen es el punto de partida de la navegación
            FrontPageScreen(navController = navController)
        }
        // Define una composable para la pantalla de productos
        composable(
            route = Screen.StoreScreen.route
        ) {
            storeViewModel.loadProducts()
            StoreScreen(navController = navController, storeViewModel, favoriteViewModel)
        }
        //Define una composable para la pantalla de favoritos
        composable(
          route =Screen.FavoriteScreen.route
        ) {
            FavoritesScreen(navController = navController, favoriteViewModel)
        }
    }

}