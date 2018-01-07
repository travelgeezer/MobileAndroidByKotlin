package com.geezer.networkservice.servicebyflask

import android.util.Log
import com.geezer.crypto.Crypto

/**
 * Created by geezer. on 06/01/2018.
 */
class ServiceByFlaskHelper {
    fun encrypt_rsa(message: String): String {
        return Crypto.RSA.encrypt(message) ?: ""
    }

    fun decrypt_rsa(cipherData: String): String {
        return Crypto.RSA.decrypt(cipherData) ?: ""
    }

    fun encrypt_aes(message: String, key: String): String {
        Log.e("Crypto", key)
        Log.e("Crypto", message)
        return Crypto.AES.encrypt(message, key)
    }

    fun decrypt_aes(message: String, key: String): String {
        return Crypto.AES.decrypt(message, key)
    }

    fun generateAESKey(): String {
        return Crypto.AES.generateKey() ?: ""
    }
}
