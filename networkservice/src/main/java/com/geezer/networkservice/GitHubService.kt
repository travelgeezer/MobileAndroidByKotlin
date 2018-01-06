package com.geezer.networkservice

import com.geezer.githubmodels.Contributor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by geezer. on 05/01/2018.
 */
class GitHubService {
    companion object {
        private val API_URL = "https://api.github.com"

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                    .baseUrl(GitHubService.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        val github: GitHub by lazy {
            retrofit.create(GitHubService.GitHub::class.java)
        }
    }

    interface GitHub {
        @GET("/repos/{owner}/{repo}/contributors")
        fun contributors(
                @Path("owner") owner: String,
                @Path("repo") repo: String): Call<List<Contributor>>
    }
}