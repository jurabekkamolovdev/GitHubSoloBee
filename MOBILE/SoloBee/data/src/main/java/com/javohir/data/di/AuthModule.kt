package com.javohir.data.di

import com.javohir.data.local.sharedPref.AuthTokenStore
import com.javohir.data.local.sharedPref.SharedPreference
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthTokenStore(impl: SharedPreference): AuthTokenStore
}
