package com.example.moviereviews.injection

import com.example.moviereviews.data.datasource.ArticleRemoteDataSource
import com.example.moviereviews.data.datasource.ReviewRemoteDataSource
import com.example.moviereviews.data.datasource.UserRemoteDataSource
import com.example.moviereviews.data.datasource.implementation.RetrofitArticleDataSource
import com.example.moviereviews.data.datasource.implementation.RetrofitReviewDataSource
import com.example.moviereviews.data.datasource.implementation.RetrofitUserDataSource
import com.example.moviereviews.data.datasource.services.MovieReviewsService
import com.example.moviereviews.utils.Constants
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()

    @Provides
    @Singleton
    fun provideMovieReviewsService(retrofit: Retrofit): MovieReviewsService =
        retrofit.create(MovieReviewsService::class.java)

    @Provides
    @Singleton
    fun provideArticleRemoteDataSource(service: MovieReviewsService): ArticleRemoteDataSource =
        RetrofitArticleDataSource(service)

    @Provides
    @Singleton
    fun provideUserRemoteDataSource(service: MovieReviewsService): UserRemoteDataSource =
        RetrofitUserDataSource(service)

    @Provides
    @Singleton
    fun provideReviewRemoteDataSource(service: MovieReviewsService): ReviewRemoteDataSource =
        RetrofitReviewDataSource(service)
}
