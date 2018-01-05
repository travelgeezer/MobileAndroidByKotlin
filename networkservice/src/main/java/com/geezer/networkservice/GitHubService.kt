package com.geezer.networkservice

import com.geezer.githubmodels.Contributor
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by geezer. on 05/01/2018.
 */
class GitHubService {
    companion object {
        val API_URL = "https://api.github.com"
    }

    interface GitHub {
        @GET("/repos/{owner}/{repo}/contributors")
        fun contributors(
                @Path("owner") owner: String,
                @Path("repo") repo: String): Call<List<Contributor>>
    }
}