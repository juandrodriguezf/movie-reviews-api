package com.example.moviereviews.navigation

import android.net.Uri
import com.example.moviereviews.utils.Constants

sealed class Screen(val route: String) {
    data object Home : Screen("home")

    data object ArticleDetail : Screen("article/{articleId}") {
        const val ARG_ARTICLE_ID = "articleId"

        fun createRoute(articleId: String) = "article/${Uri.encode(articleId)}"
    }

    data object MyProfile : Screen("profile/me")

    data object EditProfile : Screen("profile/edit")

    data object UserProfile : Screen("profile/{userId}") {
        const val ARG_USER_ID = "userId"

        fun createRoute(userId: String) = "profile/${Uri.encode(userId)}"
    }

    data object ReviewForm : Screen("review/form?articleId={articleId}&reviewId={reviewId}") {
        const val ARG_ARTICLE_ID = "articleId"
        const val ARG_REVIEW_ID = "reviewId"

        fun createRoute(
            articleId: String,
            reviewId: String = Constants.NO_REVIEW_ID
        ) = "review/form?articleId=${Uri.encode(articleId)}&reviewId=${Uri.encode(reviewId)}"
    }
}
