package com.geezer.networkservice.servicebyflask

import com.geezer.crypto.Crypto

/**
 * Created by geezer. on 06/01/2018.
 */
class ServiceByFlaskHelper {
    fun encrypt(message: String): String {
        return Crypto.encrypto(message)
    }

    fun decrypt(ct: String): String {
        return Crypto.decrypto(ct)
    }
}