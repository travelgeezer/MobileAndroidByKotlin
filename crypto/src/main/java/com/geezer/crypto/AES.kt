package com.geezer.crypto

import android.util.Base64
import android.util.Log
import java.io.UnsupportedEncodingException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Created by geezer. on 07/01/2018.
 */
class AES {
    companion object {
        const val ALGORITHM = "AES/CBC/PKCS5Padding"
        private val iv = "fedcba9876543210"            // TODO: Dummy iv (CHANGE IT!)
        private val ivspec: IvParameterSpec = IvParameterSpec(iv.toByteArray())

    }

    @Throws(NoSuchPaddingException::class, NoSuchAlgorithmException::class, InvalidKeyException::class, BadPaddingException::class, IllegalBlockSizeException::class)
    fun decrypt(input: String, key: String): String {
        val skey = SecretKeySpec(key.toByteArray(), "AES")
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, skey, ivspec)
        val output = cipher.doFinal(Base64.decode(input, Base64.DEFAULT))
        return String(output)
    }

    @Throws(NoSuchPaddingException::class, NoSuchAlgorithmException::class, InvalidKeyException::class, UnsupportedEncodingException::class, BadPaddingException::class, IllegalBlockSizeException::class)
    fun encrypt(input: String, key: String): String {
        val skey = SecretKeySpec(key.toByteArray(), "AES")
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, skey, ivspec)
        val crypted = cipher.doFinal(input.toByteArray())
        return Base64.encodeToString(crypted, Base64.DEFAULT)
    }


    fun generateKey(): String? {
        try {
            val keyGen = KeyGenerator.getInstance("AES")
            keyGen.init(128)
            return Base64.encodeToString(keyGen.generateKey().encoded, Base64.NO_WRAP)
        } catch (e: NoSuchAlgorithmException) {
            Log.e("security", "[encryption] SymetricCiphers: Generate key failure for AES ::: $e")
            return null
        }
    }
}
