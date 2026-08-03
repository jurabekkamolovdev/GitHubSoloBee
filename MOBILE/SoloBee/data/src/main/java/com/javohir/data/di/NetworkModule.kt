package com.javohir.data.di

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.di
 * Description: NetworkModule: Hilt `@Module` — dependency injection ulanishlari.
 */

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.javohir.data.BuildConfig
import com.javohir.data.remote.AuthInterceptor
import com.javohir.data.remote.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private fun loggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator,
    ): OkHttpClient {
        val chuckerInterceptor = ChuckerInterceptor.Builder(context).build()

        return OkHttpClient.Builder()
            .addInterceptor(chuckerInterceptor)
            .addInterceptor(authInterceptor)
            .authenticator(tokenAuthenticator)
            .addInterceptor(loggingInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    @Named("refresh_okhttp")
    fun provideRefreshOkHttpClient(
        @ApplicationContext context: Context,
    ): OkHttpClient {
        val chuckerInterceptor = ChuckerInterceptor.Builder(context).build()

        return OkHttpClient.Builder()
            .addInterceptor(chuckerInterceptor)
            .addInterceptor(loggingInterceptor())
            .build()
    }

    @Provides
    @Singleton
    @Named("refresh_retrofit")
    fun provideRefreshRetrofit(@Named("refresh_okhttp") client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}
