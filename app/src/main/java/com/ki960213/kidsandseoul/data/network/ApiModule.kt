package com.ki960213.kidsandseoul.data.network

import com.ki960213.kidsandseoul.data.network.administrativedong.AdministrativeDongApi
import com.ki960213.kidsandseoul.data.network.facility.FacilityApi
import com.ki960213.kidsandseoul.data.network.fcm.FcmApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun providesAdministrativeDongApi(retrofit: Retrofit): AdministrativeDongApi =
        retrofit.create(AdministrativeDongApi::class.java)

    @Provides
    @Singleton
    fun providesFacilityApi(retrofit: Retrofit): FacilityApi =
        retrofit.create(FacilityApi::class.java)

    @Provides
    @Singleton
    fun providesFcmApi(retrofit: Retrofit): FcmApi =
        retrofit.create(FcmApi::class.java)
}
