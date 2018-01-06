package com.geezer.crypto

import android.util.Log
import java.security.interfaces.RSAPrivateKey

/**
 * Created by geezer. on 06/01/2018.
 */
class Crypto {
    companion object {
        fun encrypt(message: String): String {
            val encrypt = RSA.encrypt(RSA.DEFAULT_PUBLIC_KEY, message)
            Log.d("Crypto", "encrypt: $encrypt")
            if (!encrypt.isNullOrEmpty()) {//TODO: test
                val hexStr2Str = decrypt(encrypt!!)
                Log.d("Crypto", hexStr2Str)
                return encrypt
            }
            return ""
        }

        //TODO: test
        fun decrypt(cipherData: String): String {
            Log.d("Crypto", "decrypt:")
            val privateKey: RSAPrivateKey = RSA.loadPrivateKey(RSA.DEFAULT_PRIVATE_KEY) as RSAPrivateKey
            return RSA.decrypt(privateKey, cipherData) ?: ""
        }
    }
}
