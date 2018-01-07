package com.geezer.networkservice.servicebyflask

import android.util.Log
import com.geezer.crypto.Crypto

/**
 * Created by geezer. on 06/01/2018.
 */
class ServiceByFlaskHelper {
    fun encrypt(message: String): String {
        return Crypto.encrypt(message)
    }

    fun decrypt(cipherData: String): String {
        return Crypto.decrypt(cipherData)
    }

    fun encrypt_aes(message: String, key: String): String {
        Log.e("Crypto", key)
        Log.e("Crypto", message)
        return Crypto.encrypt_aes(message, key)
    }

    fun decrypt_aes(message: String, key: String): String {
        return Crypto.decrypt_aes(message, key)
    }

    fun generateAESKey(): String {
        return "+4h1o/OQxNHkx3SSzmVDWA=="
//        return Crypto.AES.generateKey() ?: ""
    }
}
