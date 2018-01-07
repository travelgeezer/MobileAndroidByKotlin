package com.geezer.crypto

/**
 * Created by geezer. on 06/01/2018.
 */
class Crypto {
    companion object {
        val AES by lazy {
            AES()
        }

        val RSA by lazy {
            RSA()
        }
    }
}
