package com.geezer.servicebyflaskmodels

import java.io.Serializable

/**
 * Created by geezer. on 04/01/2018.
 */
class JSONModel<out T>(val code: Int, val data: T, val info: String) : Serializable {

    val isSuccessful
        get() = code == 200

    override fun toString(): String {
        return "JSONModel(code=$code, data=$data, info='$info')"
    }
}
