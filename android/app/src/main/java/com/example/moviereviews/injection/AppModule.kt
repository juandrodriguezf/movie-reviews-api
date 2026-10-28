package com.example.moviereviews.injection

import com.example.moviereviews.data.datasource.ArticleRemoteDataSource
import com.example.moviereviews.data.datasource.ReviewRemoteDataSource
import com.example.moviereviews.data.datasource.UserRemoteDataSource
import com.example.moviereviews.data.datasource.implementation.FirestoreArticleDataSource
import com.example.moviereviews.data.datasource.implementation.FirestoreReviewDataSource
import com.example.moviereviews.data.datasource.implementation.FirestoreUserDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideArticleRemoteDataSource(
        dataSource: FirestoreArticleDataSource
    ): ArticleRemoteDataSource = dataSource

    @Provides
    @Singleton
    fun provideUserRemoteDataSource(
        dataSource: FirestoreUserDataSource
    ): UserRemoteDataSource = dataSource

    @Provides
    @Singleton
    fun provideReviewRemoteDataSource(
        dataSource: FirestoreReviewDataSource
    ): ReviewRemoteDataSource = dataSource
}
