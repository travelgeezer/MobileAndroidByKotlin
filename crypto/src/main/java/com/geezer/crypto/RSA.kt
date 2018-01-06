package com.geezer.crypto

import android.util.Base64
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.security.*
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import kotlin.experimental.and

/**
 * Created by geezer. on 06/01/2018.
 */

class RSA {
    companion object {

        private val HEX_CHAR = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

        private const val ALGORITHM = "RSA/ECB/PKCS1Padding"
        const val DEFAULT_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw2PYFXNTii7LqORg0GcJ\n" +
                "o6O3ZdJzR20E1C1cm3t9mapzNi+XCEZO0nywlYJ344DWiRXMUTVkiT8N6HPfzfKA\n" +
                "5v8zvZlWrUliZZ2tj14rAfoIM/4LqYtszfMHcT32sdZk272wQj2NWrf8G65c4pVQ\n" +
                "SYKPaoXy9oahZOstCu8UeG86yv+o5ZcVjb6ZmLh/lsCNVd1rROvueJBIPcYJlUfE\n" +
                "j6QKazooYLMAZsSmP98znb795aeS5zQC806QNj5HORDhM+NqzQTyuobR+RTbso/j\n" +
                "L0cswtl04CvtoMC9LI8r51KXCjXYKmxuMU7yARgmgjPI29qYSpKqc4k9GVvdFREq\n" +
                "5QIDAQAB"

        const val DEFAULT_PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDDY9gVc1OKLsuo\n" +
                "5GDQZwmjo7dl0nNHbQTULVybe32ZqnM2L5cIRk7SfLCVgnfjgNaJFcxRNWSJPw3o\n" +
                "c9/N8oDm/zO9mVatSWJlna2PXisB+ggz/gupi2zN8wdxPfax1mTbvbBCPY1at/wb\n" +
                "rlzilVBJgo9qhfL2hqFk6y0K7xR4bzrK/6jllxWNvpmYuH+WwI1V3WtE6+54kEg9\n" +
                "xgmVR8SPpAprOihgswBmxKY/3zOdvv3lp5LnNALzTpA2Pkc5EOEz42rNBPK6htH5\n" +
                "FNuyj+MvRyzC2XTgK+2gwL0sjyvnUpcKNdgqbG4xTvIBGCaCM8jb2phKkqpziT0Z\n" +
                "W90VESrlAgMBAAECggEASPsfOAvmlUObYI9FNjJ2YEADfa3Yz0ICPrpez9iVdnM7\n" +
                "K28lK7sPMAHQ91dPLpPuDjAtK9wUnEsIB95YJxXAXgfOrK/8tyck8K2vqVBUIh3h\n" +
                "OIJYmtBwKJDxI7QwVAavj4BPuL5C8TKtoS1EloGNBpgUt4Ym1Tw+RnKtVVstuzmx\n" +
                "Gh1KQsbywUP54mBAAopBlTPzAstG5u8ksO440dHK+Sp+XTV4mbWoHDd+PSighyyI\n" +
                "xut9Sv3xviRMK9eIQXfg6N1oBAMl31Sv2e/o2kHdlnq35uddRi+2mREFrFwg5m8K\n" +
                "a95M5xhKpmJjWD6pmDowdZcygXel8n8Zmw1ek11vIQKBgQDxmY1IUVpDNJ8M6quW\n" +
                "bybJQjkrMdChBY/g5EyX1UxIN0ifk1PAarazr+hFtPcJrV+Z61oNwOV/AH7Q4MrU\n" +
                "xfaPg4iEqRj/B/WM/RZGu0Cop2DWLcU/euQg6acz06gvTiAtGBdm6dUXrhuQ4C+A\n" +
                "OnQdjBtoW/pJcipTq0sDdVIVvQKBgQDPCTNVNeedgH1gaw4xa9it/BhfgOFzzodu\n" +
                "HXJbQSRbh07yKWviANE5q/UVoCNOiIumKUxLV/K1ObSbExvfCNE8K0ucWD22nj3o\n" +
                "RKX2pua8ByjGBrD1+Q/SD8F2Yg4YwXtGnqrdFHzsgPOsJbAgQ0u/BD569fdqmAGo\n" +
                "Q9DkqPBYSQKBgQDjtoCtatASi+RlPkIf9f+urdZIWhlzy1RC+asfaJEPd9vhW/ES\n" +
                "aQJu9huMp/Y3jsuOvX8Re4BTZdohcd57EWNE/QqNTvwaVBwimOCIJjHZRHlHJbjG\n" +
                "DkQ6qT9I3/OjMpWtKwnTnA2W1ilxMXxT9cQ/nTCv+hE7bHlshwSa69dEWQKBgQC5\n" +
                "13xopLZ/vvLMNczLuA8MPhpyv/GgTmdrBKu+Fj9YmaDX5y6b9DfbGOwquVB4EkGo\n" +
                "qbF+gDexTVc9TG3cSsHmzMZWVS8wWeMgN17yhObhKVwERcVbRnJZESOL7IDS3/qU\n" +
                "baDaSqXIdghFIp9ylnXLxTeAkIHDASebryQ33R3BYQKBgQDbT0c3x2hXFdOQH65z\n" +
                "GGFROgWYq4sohf2tY/q221xwJtIZ5zA5J0IWSv0YRvtRW2ypNVqtFEbP4Tg9JSf8\n" +
                "lDAM79iY5PbQi1WfLhsrSrehKhPLU5c+H1iUREIjoy4aDPwLd3YbzfJzIxHXpRpF\n" +
                "eSil/yDHxrjlnw3j5/+OQ0ihEw=="

        fun genKeyPair() {
            var keyPairGen: KeyPairGenerator? = null
            try {
                keyPairGen = KeyPairGenerator.getInstance("RSA")
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
            keyPairGen?.initialize(1024, SecureRandom())
            val keyPair = keyPairGen?.generateKeyPair()
            val privateKey = keyPair?.private
            val publicKey = keyPair?.public
        }

        /**
         * Load public key from InputString
         */
        @Throws(Exception::class)
        fun loadPublicKey(inputStream: InputStream) {
            try {
                val br = BufferedReader(InputStreamReader(inputStream))
                var readLine: String? = br.readLine()
                val sb = StringBuilder()
                while (readLine != null) {
                    if (readLine!![0] == '-') {
                        readLine = br.readLine()
                        continue
                    } else {
                        sb.append(readLine)
                        sb.append('\r')
                    }
                    readLine = br.readLine()
                }
                loadPublicKey(sb.toString())
            } catch (e: IOException) {
                throw  Exception("Public key data flow reads errors")
            } catch (e: NullPointerException) {
                throw Exception("The public key input stream is empty")
            }
        }

        /**
         * Load public key from string
         */
        @Throws(Exception::class)
        fun loadPublicKey(publicKeyStr: String): PublicKey {
            try {
                val buffer = Base64.decode(publicKeyStr, Base64.DEFAULT)
                val keyFactory = KeyFactory.getInstance("RSA")
                val keySpec = X509EncodedKeySpec(buffer)
                return keyFactory.generatePublic(keySpec)
            } catch (e: NoSuchAlgorithmException) {
                throw Exception("No algorithm was found")
            } catch (e: InvalidKeySpecException) {
                throw Exception("The public key of illegal")
            } catch (e: NullPointerException) {
                throw Exception("The public key data is empty")
            }
        }

        /**
         * Load private key from InputStream
         */
        @Throws(Exception::class)
        fun loadPrivateKey(inputStream: InputStream) {
            try {
                val br = BufferedReader(InputStreamReader(inputStream))
                var readLine: String? = br.readLine()
                val sb = StringBuilder()
                while (readLine != null) {
                    if (readLine!![0] == '-') {
                        readLine = br.readLine()
                        continue
                    } else {
                        sb.append(readLine)
                        sb.append('\r')
                    }
                    readLine = br.readLine()
                }
                loadPrivateKey(sb.toString())
            } catch (e: IOException) {
                throw  Exception("Public key data flow reads errors")
            } catch (e: NullPointerException) {
                throw Exception("The public key input stream is empty")
            }
        }

        /**
         * Load private key from String
         */
        @Throws(Exception::class)
        fun loadPrivateKey(privateKeyStr: String): PrivateKey {
            try {
                val buffer = Base64.decode(privateKeyStr, Base64.DEFAULT)
                val keySpec = PKCS8EncodedKeySpec(buffer)
                val keyFactory = KeyFactory.getInstance("RSA")
                return keyFactory.generatePrivate(keySpec)
            } catch (e: NoSuchAlgorithmException) {
                throw Exception("No algorithm was found")
            } catch (e: InvalidKeySpecException) {
                throw Exception("The public key of illegal")
            } catch (e: NullPointerException) {
                throw Exception("The public key data is empty")
            }
        }

        @Throws(Exception::class)
        fun encrypt(publicKeyStr: String, data: String): String? {
            try {
                val buffer = Base64.decode(publicKeyStr, Base64.DEFAULT)
                val keyFactory = KeyFactory.getInstance("RSA")
                val keySpec = X509EncodedKeySpec(buffer)
                val publicKey = keyFactory.generatePublic(keySpec) as RSAPublicKey
                val cipher = Cipher.getInstance(ALGORITHM)
                cipher.init(Cipher.ENCRYPT_MODE, publicKey)
                val output = cipher.doFinal(data.toByteArray())
                return Base64.encodeToString(output, Base64.NO_WRAP)
            } catch (e: NoSuchAlgorithmException) {
                throw Exception("No algorithm was found")
            } catch (e: NoSuchPaddingException) {
                e.printStackTrace()
                return null
            } catch (e: InvalidKeyException) {
                throw Exception("Encryption public key is illegal, please check")
            } catch (e: IllegalBlockSizeException) {
                throw Exception("Illegal length")
            } catch (e: BadPaddingException) {
                throw Exception("The plaintext data has been damaged")
            }
        }

        /**
         * Encryption process
         */
        @Throws(Exception::class)
        fun encrypt(publicKey: RSAPublicKey?, plainTextData: ByteArray): ByteArray? {
            if (publicKey == null) {
                throw Exception("Encryption public key is empty, please set")
            }
            try {
                val cipher = Cipher.getInstance(ALGORITHM)
                cipher.init(Cipher.ENCRYPT_MODE, publicKey)
                val output = cipher.doFinal(plainTextData)
                return output
            } catch (e: NoSuchAlgorithmException) {
                throw Exception("No algorithm was found")
            } catch (e: NoSuchPaddingException) {
                e.printStackTrace()
                return null
            } catch (e: InvalidKeyException) {
                throw Exception("Encryption public key is illegal, please check")
            } catch (e: IllegalBlockSizeException) {
                throw Exception("Illegal length")
            } catch (e: BadPaddingException) {
                throw Exception("The plaintext data has been damaged")
            }
        }

        /**
         * Decryption process
         */
        @Throws(Exception::class)
        fun decrypt(privateKey: RSAPrivateKey, cipherData: String): String? {
            if (privateKey == null) {
                throw Exception("Decryption private key is empty, please set")
            }
            try {
                val cipher = Cipher.getInstance(ALGORITHM)
                cipher.init(Cipher.DECRYPT_MODE, privateKey)
                val buffer = Base64.decode(cipherData.toByteArray(), Base64.NO_WRAP)
                val output = cipher.doFinal(buffer)
                val hexStr = byteArrayToString(output)
                return hexStr2Str(hexStr)
            } catch (e: NoSuchAlgorithmException) {
                throw Exception("No algorithm was found")
            } catch (e: NoSuchPaddingException) {
                e.printStackTrace()
                return null
            } catch (e: InvalidKeyException) {
                throw Exception("Encryption public key is illegal, please check")
            } catch (e: IllegalBlockSizeException) {
                throw Exception("Illegal length")
            } catch (e: BadPaddingException) {
                throw Exception("The plaintext data has been damaged")
            }
        }

        private fun byteArrayToString(data: ByteArray): String {
            val stringBuilder = StringBuilder()
            for (i in data.indices) {
                stringBuilder.append(HEX_CHAR[(data[i] and (0xf0).toByte()).toInt().ushr(4)])
                stringBuilder.append(HEX_CHAR[(data[i] and (0x0f).toByte()).toInt()])
            }
            return stringBuilder.toString()
        }

        private fun hexStr2Str(hexStr: String): String {
            val hexs = hexStr.toCharArray()
            val bytes = ByteArray(hexStr.length / 2)
            var n: Int
            for (i in bytes.indices) {
                n = HEX_CHAR.indexOf(hexs[2 * i]) * 16
                n += HEX_CHAR.indexOf(hexs[2 * i + 1])
                bytes[i] = (n and 0xff).toByte()
            }
            return String(bytes)
        }

    }

}
