package com.ki960213.kidsandseoul.data.network.fcm

import retrofit2.http.DELETE
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface FcmApi {

    @PATCH("/fcm-tokens")
    suspend fun saveToken(@Query("userId") userId: String, @Query("token") fcmToken: String)

    @DELETE("/fcm-tokens")
    suspend fun deleteToken(@Query("userId") userId: String, @Query("token") fcmToken: String)

    @POST("/notifications")
    suspend fun send(
        @Query("userIds") userIds: List<String>,
        @Query("message") message: String,
    )
}
