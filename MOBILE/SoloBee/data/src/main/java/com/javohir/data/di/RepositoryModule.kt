package com.javohir.data.di

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.di
 * Description: RepositoryModule: Hilt `@Module` — dependency injection ulanishlari.
 */

import com.javohir.data.repository.ActivitiesRepositoryImpl
import com.javohir.data.repository.AvatarsRepositoryImpl
import com.javohir.data.repository.DeleteRepositoryImpl
import com.javohir.data.repository.HomeRepositoryImpl
import com.javohir.data.repository.LoginRepositoryImpl
import com.javohir.data.repository.ProgressRepositoryImpl
import com.javohir.data.repository.RegisterRepositoryImpl
import com.javohir.data.repository.SessionRepositoryImpl
import com.javohir.data.repository.SplashRepositoryImpl
import com.javohir.data.repository.TopicsRepositoryImpl
import com.javohir.domain.repository.ActivitiesRepository
import com.javohir.domain.repository.AvatarsRepository
import com.javohir.domain.repository.DeleteRepository
import com.javohir.domain.repository.HomeRepository
import com.javohir.domain.repository.LoginRepository
import com.javohir.domain.repository.ProgressRepository
import com.javohir.domain.repository.RegisterRepository
import com.javohir.domain.repository.SessionRepository
import com.javohir.domain.repository.SplashRepository
import com.javohir.domain.repository.TopicsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLoginRepository(impl: LoginRepositoryImpl): LoginRepository

    @Binds
    @Singleton
    abstract fun bindHomeRepository(impl: HomeRepositoryImpl): HomeRepository

    @Binds
    @Singleton
    abstract fun bindSplashRepository(impl: SplashRepositoryImpl): SplashRepository

    @Binds
    @Singleton
    abstract fun bindSessionRepository(impl: SessionRepositoryImpl): SessionRepository

    @Binds
    @Singleton
    abstract fun bindTopicRepository(impl: TopicsRepositoryImpl): TopicsRepository

    @Binds
    @Singleton
    abstract fun bindActivitiesRepository(impl: ActivitiesRepositoryImpl): ActivitiesRepository

    @Binds
    @Singleton
    abstract fun bindProgressRepository(impl: ProgressRepositoryImpl): ProgressRepository

    @Binds
    @Singleton
    abstract fun bindDeleteRepository(impl: DeleteRepositoryImpl): DeleteRepository

    @Binds
    @Singleton
    abstract fun bindRegisterRepository(impl: RegisterRepositoryImpl): RegisterRepository

    @Binds
    @Singleton
    abstract fun bindAvatarsRepository(impl: AvatarsRepositoryImpl): AvatarsRepository
}
