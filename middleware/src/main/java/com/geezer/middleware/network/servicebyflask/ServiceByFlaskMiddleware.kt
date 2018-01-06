package com.geezer.middleware.network.servicebyflask

import com.geezer.networkservice.servicebyflask.ServiceByFlaskService
import com.geezer.servicebyflaskmodels.JSONModel
import com.geezer.servicebyflaskmodels.UserModel
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

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
            return rx({ serviceByFlask.responseDataFormat(data).execute() })
        }

        fun register(name: String, account: String, password: String): Flowable<UserModel> {
            val hashMap = HashMap<String, String>()
            hashMap.put("name", name)
            hashMap.put("account", account)
            hashMap.put("password", password)
            return rx({ serviceByFlask.register(hashMap).execute() })
        }


        private fun <T> rx(callable: () -> Response<JSONModel<T>>): Flowable<T> {
            return Flowable.fromCallable(callable)
                    .map { ServiceByFlaskMiddlewareHelper.filterResponse(it) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }
}
