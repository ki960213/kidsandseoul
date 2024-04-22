package com.ki960213.kidsandseoul.data.network.administrativedong

import com.ki960213.kidsandseoul.data.network.administrativedong.model.NetworkAdministrativeDong
import retrofit2.http.GET

interface AdministrativeDongApi {

    @GET("administrative-dongs")
    suspend fun getAdministrativeDongs(): List<NetworkAdministrativeDong>
}
