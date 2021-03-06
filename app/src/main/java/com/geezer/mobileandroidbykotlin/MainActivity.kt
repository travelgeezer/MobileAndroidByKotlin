package com.geezer.mobileandroidbykotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.geezer.middleware.Middleware
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        sample_text.text = stringFromJNI()

        register.setOnClickListener {
            val key = Middleware.Crypto.AES.generateKey() ?: ""
            Middleware.RequestClient
                    .register(name.text.toString().trim(), account.text.toString().trim(), password.text.toString().trim(), key)
                    .subscribe({ sample_text.text = it.toString() }, {
                        val message = Middleware.RequestClient.helper.handleError(it).message
                        sample_text.text = Middleware.Crypto.AES.decrypt(message, key)
                    })
        }

        login.setOnClickListener {
            val key = Middleware.Crypto.AES.generateKey() ?: ""
            Middleware.RequestClient
                    .login(account.text.toString().trim(), password.text.toString().trim(), key)
                    .subscribe({ sample_text.text = "${it.name}, login success!!!"}, {
                        val message = Middleware.RequestClient.helper.handleError(it).message
                        sample_text.text = message
                    })
        }
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
