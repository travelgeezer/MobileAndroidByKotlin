package com.geezer.middleware

import com.geezer.githubmodels.Contributor
import com.geezer.servicebyflaskmodels.JSONModel
import com.geezer.networkservice.SimpleService
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Callable

/**
 * Created by geezer. on 04/01/2018.
 */
class SimpleServiceMiddleware {

    companion object {

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                    .baseUrl(SimpleService.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        private val github: SimpleService.GitHub by lazy {
            retrofit.create(SimpleService.GitHub::class.java)
        }


        private val retrofit2 by lazy {
            Retrofit.Builder()
                    .baseUrl(SimpleService.SERVICE_BY_FLASK_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        private val serviceByFlask by lazy {
            retrofit2.create(SimpleService.ServiceByFlask::class.java)
        }


        fun contributor(owner: String, repo: String): Flowable<List<Contributor>?> {
            return rx(Callable {
                val call = github.contributors(owner, repo)
                call.execute().body()
            })
        }

        fun responseDataFormat(data: String): Flowable<JSONModel<String>?> {
            return rx(Callable {
                val call = serviceByFlask.responseDataFormat(data)
                call.execute().body()
            })
        }


        private fun <T> rx(callable: Callable<T>): Flowable<T> {
            return Flowable.fromCallable(callable)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

}
