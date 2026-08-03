package com.javohir.solobee.di

import com.javohir.solobee.cashlytics.CrashlyticsLogger
import com.javohir.utils.AppLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.solobee
 * Description: Dependency Injection
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppLogger(
        impl: CrashlyticsLogger
    ): AppLogger {
        return impl
    }

}