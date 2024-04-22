package com.ki960213.kidsandseoul.data.network.facility

import com.ki960213.kidsandseoul.data.network.facility.model.NetworkFacility
import com.ki960213.kidsandseoul.data.network.facility.model.NetworkService
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FacilityApi {

    @GET("/facilities")
    suspend fun getFacilities(
        @Query("name") name: String? = null,
        @Query("age") age: Int? = null,
        @Query("administrativeDongId") administrativeDongId: Long? = null,
        @Query("service") service: NetworkService? = null,
        @Query("ids") ids: List<Long>? = null,
    ): List<NetworkFacility>

    @GET("/facilities/{id}")
    suspend fun getFacility(@Path("id") id: Long): NetworkFacility
}
