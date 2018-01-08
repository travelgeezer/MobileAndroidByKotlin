package com.geezer.middleware.crypto

import com.geezer.crypto.Crypto

/**
 * Created by geezer. on 08/01/2018.
 */
class CryptoMiddleware {

    val RSA by lazy {
        Crypto.RSA
    }

    val AES by lazy {
        Crypto.AES
    }
}