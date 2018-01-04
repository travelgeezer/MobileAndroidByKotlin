package com.geezer.mobileandroidbykotlin.service

import com.geezer.mobileandroidbykotlin.models.BaseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


/**
 * Created by geezer. on 03/01/2018.
 */
class SimpleService {

    companion object {
        val API_URL = "https://api.github.com"
        val SERVICE_BY_FLASK_URL = "http://10.0.2.2:5000"
    }


    data class Contributor(val login: String, val contributor: Int)


    interface GitHub {
        @GET("/repos/{owner}/{repo}/contributors")
        fun contributors(
                @Path("owner") owner: String,
                @Path("repo") repo: String): Call<List<Contributor>>
    }

    interface ServiceByFlask {
        @GET("/api/v1/response_data_format/{data}")
        fun responseDataFormat(@Path("data") data: String): Call<BaseModel<String>>
    }
}
