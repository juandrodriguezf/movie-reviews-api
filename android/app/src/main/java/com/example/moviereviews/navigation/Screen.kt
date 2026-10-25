package com.example.moviereviews.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")

    data object ArticleDetail : Screen("article/{articleId}") {
        const val ARG_ARTICLE_ID = "articleId"

        fun createRoute(articleId: Int) = "article/$articleId"
    }

    data object MyProfile : Screen("profile/me")

    data object UserProfile : Screen("profile/{userId}") {
        const val ARG_USER_ID = "userId"

        fun createRoute(userId: Int) = "profile/$userId"
    }

    data object ReviewForm : Screen("review/form?articleId={articleId}&reviewId={reviewId}") {
        const val ARG_ARTICLE_ID = "articleId"
        const val ARG_REVIEW_ID = "reviewId"

        fun createRoute(articleId: Int, reviewId: Int = -1) =
            "review/form?articleId=$articleId&reviewId=$reviewId"
    }
}
