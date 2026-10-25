package com.example.moviereviews.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
}
