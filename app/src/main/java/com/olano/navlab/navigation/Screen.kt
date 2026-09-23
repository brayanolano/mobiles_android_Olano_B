package com.olano.navlab.navigation
sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object List : Screen("list_screen")
    object Profile : Screen("profile_screen")
    object Detail : Screen("detail_screen/{itemId}") {
        fun createRoute(itemId: Int) = "detail_screen/$itemId"
    }
}