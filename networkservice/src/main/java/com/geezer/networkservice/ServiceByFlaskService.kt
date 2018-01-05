package com.geezer.networkservice

import com.geezer.servicebyflaskmodels.JSONModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by geezer. on 05/01/2018.
 */
class ServiceByFlaskService {
    companion object {
        val SERVICE_BY_FLASK_URL = "http://10.0.2.2:5000"
    }

    interface ServiceByFlask {
        @GET("/api/v1/response_data_format/{data}")
        fun responseDataFormat(@Path("data") data: String): Call<JSONModel<String>>
    }
}