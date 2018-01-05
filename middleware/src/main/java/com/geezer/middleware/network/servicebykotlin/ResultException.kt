package com.geezer.middleware.network.servicebykotlin

/**
 * Created by geezer. on 05/01/2018.
 */
class ResultException(val httpError: HttpError) : Exception()
