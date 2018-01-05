package com.geezer.middleware.network.servicebyflask

/**
 * Created by geezer. on 05/01/2018.
 */
class ResultException(val httpError: HttpError) : Exception()
