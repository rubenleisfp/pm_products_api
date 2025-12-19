package com.fp

/**
 * Created by Your name on 19/12/2025.
 */
sealed class Screen(val route: String) {
    //Definimos primero las rutas de las pantallas
    object FrontPageScreen: Screen("front_page_screen")
    object StoreScreen: Screen("store_screen")
}