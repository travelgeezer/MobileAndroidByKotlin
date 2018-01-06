package com.geezer.middleware.network.github

import com.geezer.githubmodels.Contributor
import com.geezer.networkservice.github.GitHubService
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Callable

/**
 * Created by geezer. on 05/01/2018.
 */

class GithubMiddleware {
    companion object {

        private val github by lazy {
            GitHubService.github
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
