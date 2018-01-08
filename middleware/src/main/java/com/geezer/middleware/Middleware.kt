package com.geezer.middleware

import com.geezer.middleware.crypto.CryptoMiddleware
import com.geezer.middleware.network.servicebyflask.ServiceByFlaskMiddleware

/**
 * Created by geezer. on 08/01/2018.
 */
class Middleware {
    companion object {
        val RequestClient by lazy {
            ServiceByFlaskMiddleware()
        }

        val Crypto by lazy {
            CryptoMiddleware()
        }
    }
}