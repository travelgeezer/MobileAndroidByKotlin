package com.geezer.middleware.network

import com.geezer.networkservice.ServiceByFlaskService
import com.geezer.servicebyflaskmodels.JSONModel
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Callable

/**
 * Created by geezer. on 05/01/2018.
 */

class ServiceByFlaskMiddleware {
    companion object {
        private val retrofit by lazy {
            Retrofit.Builder()
                    .baseUrl(ServiceByFlaskService.SERVICE_BY_FLASK_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        private val serviceByFlask by lazy {
            retrofit.create(ServiceByFlaskService.ServiceByFlask::class.java)
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