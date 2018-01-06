package com.geezer.networkservice

import com.geezer.servicebyflaskmodels.JSONModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by geezer. on 05/01/2018.
 */
class ServiceByFlaskService {

    companion object {
        private val SERVICE_BY_FLASK_URL = "http://10.0.2.2:5000"

        private val retrofit by lazy {
            Retrofit.Builder()
                    .baseUrl(ServiceByFlaskService.SERVICE_BY_FLASK_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        val serviceByFlask: ServiceByFlask by lazy {
            retrofit.create(ServiceByFlaskService.ServiceByFlask::class.java)
        }

    }

    interface ServiceByFlask {
        @GET("/api/v1/response_data_format/{data}")
        fun responseDataFormat(@Path("data") data: String): Call<JSONModel<String>>
    }
}
