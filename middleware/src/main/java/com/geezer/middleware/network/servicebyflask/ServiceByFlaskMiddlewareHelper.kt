package com.geezer.middleware.network.servicebyflask

import com.geezer.console.Console
import com.geezer.servicebyflaskmodels.JSONModel
import com.google.gson.JsonSyntaxException
import retrofit2.Response
import java.net.SocketTimeoutException

/**
 * Created by geezer. on 05/01/2018.
 */
class ServiceByFlaskMiddlewareHelper {
    companion object {
        private const val TAG = "ServiceByFlaskMiddlewareHelper"
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
