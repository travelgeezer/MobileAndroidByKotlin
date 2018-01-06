package com.geezer.mobileandroidbykotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.geezer.middleware.network.servicebyflask.ServiceByFlaskMiddleware
import com.geezer.middleware.network.servicebyflask.ServiceByFlaskMiddlewareHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        sample_text.text = stringFromJNI()

        register.setOnClickListener {
            ServiceByFlaskMiddleware
                    .register(name.text.toString().trim(), account.text.toString().trim(), password.text.toString().trim())
                    .subscribe({ sample_text.text = it.toString() }, { sample_text.text = ServiceByFlaskMiddlewareHelper.handleError(it).toString() })
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
