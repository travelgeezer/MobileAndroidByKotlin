package com.geezer.console

import android.util.Log


/**
 * Created by geezer. on 06/01/2018.
 */

class Console {
    companion object {

        const val TAG = "Console"

        fun log(tag: String = TAG, message: String = "") {
            if (BuildConfig.DEBUG) {
                Log.v(tag, message)
            }
        }

        fun info(tag: String = TAG, message: String = "") {
            if (BuildConfig.DEBUG) {
                Log.i(tag, message)
            }
        }

        private fun debug(tag: String = TAG, message: String = "") {
            if (BuildConfig.DEBUG) {
                Log.d(tag, message)
            }
        }

        fun warn(tag: String = TAG, message: String = "") {
            if (BuildConfig.DEBUG) {
                Log.w(tag, message)
            }
        }

        fun error(tag: String = TAG, message: String = "") {
            if (BuildConfig.DEBUG) {
                Log.e(tag, message)
            }
        }
    }
}