package com.geezer.middleware.network.servicebykotlin

import com.geezer.networkservice.ServiceByFlaskService
import com.geezer.servicebyflaskmodels.JSONModel
import com.google.gson.JsonSyntaxException

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

        fun handleError(error: Throwable): HttpError {
            return when (error) {
                is ResultException -> error.httpError

                is JsonSyntaxException -> {
                    val parseError = HttpError.PARSE_ERROR
                    parseError.message = error.message ?: ""
                    parseError
                }

                is NullPointerException -> HttpError.NOT_FOUND

                else -> HttpError.UNKNOWN_ERROR
            }
        }

        fun <T> filterResultCode(model: JSONModel<T>?): T {
            return when (model?.code) {
                ServiceByFlaskService.ResultCode.SUCCESS.code -> model.data
                else -> throw resultErrorHandle(model?.code)
            }
        }
    }
}
