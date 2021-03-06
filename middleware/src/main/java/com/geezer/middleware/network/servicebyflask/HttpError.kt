package com.geezer.middleware.network.servicebyflask

/**
 * Created by geezer. on 05/01/2018.
 */
enum class HttpError(val code: Int, val description: String) {
    NO_CONNECTION_ERROR(-1, "no connection error"),
    NETWORK_ERROR(-2, "network error"),
    PARSE_ERROR(-3, "parse error"),
    UNKNOWN_ERROR(-4, "unknown error"),

    SERVER_ERROR(500, "server error"),
    TIME_ERROR(504, "time out"),
    SERVER_UNAVAILABLE(503, " Service Unavailable"),
    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not Found"),


    PARAMS_ERROR(2, "Params error");

    var message: String = ""

    override fun toString(): String {
        return "HttpError(code=$code, description='$description', message='$message')"
    }


}
