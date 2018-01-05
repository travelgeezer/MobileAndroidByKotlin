package com.geezer.middleware.network.servicebyflask

import com.geezer.networkservice.ServiceByFlaskService
import com.geezer.servicebyflaskmodels.JSONModel
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.util.concurrent.Callable

/**
 * Created by geezer. on 05/01/2018.
 */

class ServiceByFlaskMiddleware {


    companion object {
        private val TAG = "ServiceByFlask"

        private val serviceByFlask by lazy {
            ServiceByFlaskService.serviceByFlask
        }

        fun responseDataFormat(data: String): Flowable<String> {
            return rx(Callable {
                val call = serviceByFlask.responseDataFormat(data)
                call.execute()
            })
        }


        private fun <T> rx(callable: Callable<Response<JSONModel<T>>>): Flowable<T> {
            return Flowable.fromCallable(callable)
                    .map { ServiceByFlaskMiddlewareHelper.filterResponse(it) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }
}
