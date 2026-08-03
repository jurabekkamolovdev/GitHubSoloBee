package com.javohir.data.di

/**
 * Created by: Javohir Oromov macOS
 * Project: SoloBee
 * Package: com.javohir.data.di
 * Description: ApiModule: Hilt `@Module` — dependency injection ulanishlari.
 */

import com.javohir.data.remote.ActivitiesApiService
import com.javohir.data.remote.AvatarsApiService
import com.javohir.data.remote.CategoriesApiService
import com.javohir.data.remote.DeleteApiService
import com.javohir.data.remote.LoginApiService
import com.javohir.data.remote.ProfileApiService
import com.javohir.data.remote.ProgressApiService
import com.javohir.data.remote.RefreshApiService
import com.javohir.data.remote.RegisterApiService
import com.javohir.data.remote.TopicsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideLoginApiService(retrofit: Retrofit): LoginApiService {
        return retrofit.create(LoginApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileApiService(retrofit: Retrofit): ProfileApiService {
        return retrofit.create(ProfileApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCategoriesApiService(retrofit: Retrofit): CategoriesApiService {
        return retrofit.create(CategoriesApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTopicsApiService(retrofit: Retrofit): TopicsApiService {
        return retrofit.create(TopicsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideActivitiesApiService(retrofit: Retrofit): ActivitiesApiService {
        return retrofit.create(ActivitiesApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideProgressApiService(retrofit: Retrofit): ProgressApiService {
        return retrofit.create(ProgressApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRefreshApiService(@Named("refresh_retrofit") retrofit: Retrofit): RefreshApiService {
        return retrofit.create(RefreshApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDeleteApiService(retrofit: Retrofit): DeleteApiService{
        return retrofit.create(DeleteApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAvatarsApiService(retrofit: Retrofit): AvatarsApiService{
        return retrofit.create(AvatarsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRegisterApiService(retrofit: Retrofit): RegisterApiService{
        return retrofit.create(RegisterApiService::class.java)
    }


}
