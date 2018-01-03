package com.geezer.mobileandroidbykotlin.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


/**
 * Created by geezer. on 03/01/2018.
 */
class SimpleService {

    companion object {
        val API_URL = "https://api.github.com"
    }


    data class Contributor(val login: String, val contributor: Int)


    interface GitHub {
        @GET("/repos/{owner}/{repo}/contributors")
        fun contributors(
                @Path("owner") owner: String,
                @Path("repo") repo: String): Call<List<Contributor>>
    }
}