package com.fp

/**
 * Represents a sealed class for defining navigation routes within the application.
 * Each object inheriting from [Screen] corresponds to a specific screen and holds its unique route string.
 *
 * @param route The unique identifier for the navigation route.
 */
sealed class Screen(val route: String) {
    object FrontPageScreen: Screen("front_page_screen")
    object StoreScreen: Screen("store_screen")
    object FavoriteScreen: Screen("favorite_screen")
}