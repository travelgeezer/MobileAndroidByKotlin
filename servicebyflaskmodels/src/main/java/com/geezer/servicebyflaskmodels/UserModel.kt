package com.geezer.servicebyflaskmodels

import java.io.Serializable

/**
 * Created by geezer. on 06/01/2018.
 */
data class UserModel(val name: String, val account: String) : Serializable {
    override fun toString(): String {
        return "UserModel(name='$name', account='$account')"
    }
}