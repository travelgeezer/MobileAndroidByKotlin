package com.geezer.middleware.network

import com.geezer.githubmodels.Contributor
import com.geezer.networkservice.GitHubService
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Callable

/**
 * Created by geezer. on 05/01/2018.
 */

class GithubMiddleware {
    companion object {
        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                    .baseUrl(GitHubService.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        private val github: GitHubService.GitHub by lazy {
            retrofit.create(GitHubService.GitHub::class.java)
        }

        fun contributor(owner: String, repo: String): Flowable<List<Contributor>?> {
            return rx(Callable {
                val call = github.contributors(owner, repo)
                call.execute().body()
            })
        }

        fun <T> rx(callable: Callable<T>): Flowable<T> {
            return Flowable.fromCallable(callable)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }

    }
}