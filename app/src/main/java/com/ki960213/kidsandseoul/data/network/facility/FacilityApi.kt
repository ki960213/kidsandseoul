package com.ki960213.kidsandseoul.data.network.facility

import com.ki960213.domain.facility.model.FacilityOrder
import com.ki960213.kidsandseoul.data.network.facility.model.NetworkFacility
import com.ki960213.kidsandseoul.data.network.facility.model.NetworkFacilityFilterConditions
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FacilityApi {

    @POST("facilities")
    suspend fun getFacilities(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 1000,
        @Query("sort") sort: String? = null,
        @Body filterConditions: NetworkFacilityFilterConditions,
    ): List<NetworkFacility>

    @POST("facilities/count")
    suspend fun getFacilityCount(
        @Body filterConditions: NetworkFacilityFilterConditions,
    ): Long

    @GET("/facilities/{id}")
    suspend fun getFacility(@Path("id") id: Long): NetworkFacility

    @PATCH("/facilities/{id}/addReview")
    suspend fun addReviewOf(@Path("id") facilityId: Long, @Query("starPoint") starPoint: Int)

    @PATCH("/facilities/{id}/deleteReview")
    suspend fun deleteReviewOf(@Path("id") facilityId: Long, @Query("starPoint") starPoint: Int)

    companion object {

        fun createSortParam(facilityOrder: FacilityOrder): String? =
            when (facilityOrder) {
                FacilityOrder.None -> null
                is FacilityOrder.ReviewCount -> "reviewCount,${if (facilityOrder.isDescending) "DESC" else "ASC"}"
                is FacilityOrder.StarPointAvg -> "starPointAvg,${if (facilityOrder.isDescending) "DESC" else "ASC"}"
            }
    }
}
