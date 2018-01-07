package com.geezer.networkservice.servicebyflask

import com.geezer.servicebyflaskmodels.JSONModel
import com.geezer.servicebyflaskmodels.UserModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

/**
 * Created by geezer. on 05/01/2018.
 */
class ServiceByFlaskService {

    companion object {
        private val SERVICE_BY_FLASK_URL = "http://10.0.2.2:5000"

        private val retrofit by lazy {
            Retrofit.Builder()
                    .baseUrl(SERVICE_BY_FLASK_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        val serviceByFlask: ServiceByFlask by lazy {
            retrofit.create(ServiceByFlask::class.java)
        }

        val serviceByFlaskHelper: ServiceByFlaskHelper by lazy {
            ServiceByFlaskHelper()
        }

    }

    interface ServiceByFlask {
        @GET("/api/v1/response_data_format/{data}")
        fun responseDataFormat(@Path("data") data: String): Call<JSONModel<String>>

        @Headers("Content-Type: application/json")
        @POST("/api/v1/test_rsa/")
        fun testRsa(@Body bod: Map<String, String>): Call<JSONModel<String>>

        @Headers("Content-Type: application/json")
        @POST("/api/v1/register")
        fun register(@Body body: Map<String, String>): Call<JSONModel<UserModel>>
    }
}
