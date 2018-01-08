package com.geezer.middleware.network.servicebyflask

import com.geezer.console.Console
import com.geezer.middleware.Middleware
import com.geezer.networkservice.servicebyflask.ServiceByFlaskService
import com.geezer.servicebyflaskmodels.JSONModel
import com.geezer.servicebyflaskmodels.UserModel
import com.google.gson.JsonSyntaxException
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.net.SocketTimeoutException

/**
 * Created by geezer. on 05/01/2018.
 */

class ServiceByFlaskMiddleware {

    companion object {
        private val TAG = "ServiceByFlask"

        private val serviceByFlask by lazy {
            ServiceByFlaskService.serviceByFlask
        }

    }

    val helper by lazy {
        Helper()
    }


    fun responseDataFormat(data: String): Flowable<String> {
        return rx({ serviceByFlask.responseDataFormat(data).execute() })
    }

    fun testRsaPublicKeyDecode(key: String, message: String): Flowable<String> {
        val map = hashMapOf<String, String>()
        map.put("key", Middleware.Crypto.RSA.encrypt(key) ?: ""/*.encrypt_rsa(key)*/)
        val value = Middleware.Crypto.AES.encrypt(message, key)
        map.put("message", value)
        return rx({ serviceByFlask.testRsa(map).execute() })
    }

    fun register(name: String, account: String, password: String, key: String): Flowable<UserModel> {
        val hashMap = HashMap<String, String>()
        hashMap.put("key", Middleware.Crypto.RSA.encrypt(key) ?: "")
        hashMap.put("name", name)
        hashMap.put("account", account)
        hashMap.put("password", Middleware.Crypto.AES.encrypt(password, key) ?: "")
        return rx({ serviceByFlask.register(hashMap).execute() })
    }


    private fun <T> rx(callable: () -> Response<JSONModel<T>>): Flowable<T> {
        return Flowable.fromCallable(callable)
                .map { helper.filterResponse(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    class Helper {
        private val TAG = "ServiceByFlaskMiddlewareHelper"
        /**
         * handle custom error code
         */
        private fun <T> responseErrorHandle(model: JSONModel<T>): Exception {
            Console.error(TAG, "responseErrorHandle( code = ${model.code} )")
            return when (model.code) {
                HttpError.PARAMS_ERROR.code -> {
                    val httpError = HttpError.PARAMS_ERROR
                    httpError.message = model.info
                    ResultException(httpError)
                }
                else -> ResultException(HttpError.UNKNOWN_ERROR)
            }
        }

        /**
         * handle service error code
         */
        private fun httpErrorHandle(code: Int): Exception {
            Console.error(TAG, "httpErrorHandle( code = $code )")
            return when (code) {
                HttpError.SERVER_ERROR.code -> ResultException(HttpError.SERVER_ERROR)
                HttpError.TIME_ERROR.code -> ResultException(HttpError.TIME_ERROR)
                HttpError.SERVER_UNAVAILABLE.code -> ResultException(HttpError.SERVER_UNAVAILABLE)
                HttpError.BAD_REQUEST.code -> ResultException(HttpError.BAD_REQUEST)
                HttpError.NOT_FOUND.code -> ResultException(HttpError.NOT_FOUND)
                else -> ResultException(HttpError.UNKNOWN_ERROR)
            }
        }

        private fun <T> filterJSONModel(model: JSONModel<T>): T {
            Console.log(TAG, "model: $model")
            if (model.isSuccessful) {
                return model.data
            }
            throw responseErrorHandle(model)
        }

        fun <T> filterResponse(response: Response<JSONModel<T>>): T {
            Console.log(TAG, "response.isSuccessful: ${response.isSuccessful} \n response.code: ${response.code()}}")
            if (response.isSuccessful) {
                return filterJSONModel(response.body()!!)
            }
            throw httpErrorHandle(response.code())
        }

        fun handleError(error: Throwable): HttpError {
            return when (error) {

                is ResultException -> error.httpError

                is JsonSyntaxException -> {
                    val parseError = HttpError.PARSE_ERROR
                    parseError.message = error.message ?: ""
                    parseError
                }

                is SocketTimeoutException -> {
                    val timeOut = HttpError.TIME_ERROR
                    timeOut.message = error.message ?: ""
                    timeOut
                }

                else -> HttpError.UNKNOWN_ERROR
            }
        }
    }

}
