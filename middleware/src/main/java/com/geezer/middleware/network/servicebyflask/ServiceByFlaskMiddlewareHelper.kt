package com.geezer.middleware.network.servicebyflask

import com.geezer.servicebyflaskmodels.JSONModel
import com.google.gson.JsonSyntaxException
import retrofit2.Response
import java.net.SocketTimeoutException

/**
 * Created by geezer. on 05/01/2018.
 */
class ServiceByFlaskMiddlewareHelper {
    companion object {

        private fun resultErrorHandle(code: Int?): Exception {
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
            if (model.isSuccessful) {
                return model.data
            }
            throw resultErrorHandle(model.code)
        }

        fun <T> filterResponse(response: Response<JSONModel<T>>): T {
            if (response.isSuccessful) {
                return filterJSONModel(response.body()!!)
            }
            throw ServiceByFlaskMiddlewareHelper.resultErrorHandle(response.code())
        }

        fun handleError(error: Throwable): HttpError {
            return when (error) {

                is SocketTimeoutException -> {
                    val timeOut = HttpError.TIME_ERROR
                    timeOut.message = error.message ?: ""
                    timeOut
                }

                is JsonSyntaxException -> {
                    val parseError = HttpError.PARSE_ERROR
                    parseError.message = error.message ?: ""
                    parseError
                }

                is NullPointerException -> HttpError.NOT_FOUND

                is ResultException -> error.httpError

                else -> HttpError.UNKNOWN_ERROR
            }
        }
    }
}
