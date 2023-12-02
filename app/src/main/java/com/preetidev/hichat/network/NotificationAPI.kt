package com.preetidev.hichat.network


import com.preetidev.hichat.entity.PushNotification
import com.preetidev.hichat.notifications.Constants.Companion.CONTENT_TYPE
import com.preetidev.hichat.notifications.Constants.Companion.SERVER_KEY
import retrofit2.Response
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationAPI{
    @Headers("Authorization:key=$SERVER_KEY","Content_Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>

}