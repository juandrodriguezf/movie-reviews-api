package com.example.moviereviews.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.moviereviews.screens.article.ArticleDetailScreen
import com.example.moviereviews.screens.home.HomeScreen
import com.example.moviereviews.screens.profile.ProfileScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onArticleClick = { articleId ->
                    navController.navigate(Screen.ArticleDetail.createRoute(articleId))
                },
                onProfileClick = {
                    navController.navigate(Screen.MyProfile.route)
                }
            )
        }

        composable(
            route = Screen.ArticleDetail.route,
            arguments = listOf(
                navArgument(Screen.ArticleDetail.ARG_ARTICLE_ID) {
                    type = NavType.IntType
                }
            )
        ) {
            ArticleDetailScreen(
                onBack = { navController.popBackStack() },
                onUserClick = { userId ->
                    navController.navigate(Screen.UserProfile.createRoute(userId))
                }
            )
        }

        composable(Screen.MyProfile.route) {
            ProfileScreen(
                onBack = { navController.popBackStack() },
                onArticleClick = { articleId ->
                    navController.navigate(Screen.ArticleDetail.createRoute(articleId))
                }
            )
        }

        composable(
            route = Screen.UserProfile.route,
            arguments = listOf(
                navArgument(Screen.UserProfile.ARG_USER_ID) {
                    type = NavType.IntType
                }
            )
        ) {
            ProfileScreen(
                onBack = { navController.popBackStack() },
                onArticleClick = { articleId ->
                    navController.navigate(Screen.ArticleDetail.createRoute(articleId))
                }
            )
        }
    }
}
