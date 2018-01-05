package com.geezer.middleware.network

import com.geezer.networkservice.ServiceByFlaskService
import com.geezer.servicebyflaskmodels.JSONModel
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Callable

/**
 * Created by geezer. on 05/01/2018.
 */

class ServiceByFlaskMiddleware {
    companion object {

        private val serviceByFlask by lazy {
            ServiceByFlaskService.serviceByFlask
        }

        fun responseDataFormat(data: String): Flowable<String> {
            return rx(Callable {
                val call = serviceByFlask.responseDataFormat(data)
                call.execute().body()
            })
        }

        class ResultException(message: String) : Exception(message)

        private fun <T> filterResultCode(model: JSONModel<T>?): T {
            return when (model?.code) {
                ServiceByFlaskService.ResultCode.SUCCESS.code -> model.data
                else -> throw ResultException(model?.info ?: "Unexpected response: $model")
            }
        }

        private fun <T> rx(callable: Callable<JSONModel<T>?>): Flowable<T> {
            return Flowable.fromCallable(callable)
                    .map { filterResultCode(it) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

        }
    }
}