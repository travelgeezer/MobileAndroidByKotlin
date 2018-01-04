package com.geezer.mobileandroidbykotlin.models

import java.io.Serializable

/**
 * Created by geezer. on 04/01/2018.
 */
data class BaseModel<T>(val code: Int, val  data: T, val info: String): Serializable