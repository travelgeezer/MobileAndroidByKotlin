package com.geezer.mobileandroidbykotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.geezer.middleware.network.github.GithubMiddleware
import com.geezer.middleware.network.servicebyflask.ServiceByFlaskMiddlewareHelper
import com.geezer.middleware.network.servicebyflask.ServiceByFlaskMiddleware
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        sample_text.text = stringFromJNI()


        GithubMiddleware
                .contributor("square", "retrofit")
                .subscribe {
                    it?.forEach {
                        Log.d("MainActivity", "login: ${it.login}  contributors: ${it.contributor}")
                    }
                }

        sample_text.setOnClickListener {
            ServiceByFlaskMiddleware
                    .responseDataFormat("call SImpleServiceMiddleware")
                    .subscribe({ sample_text.text = it },
                            {
                                it.printStackTrace()
                                val handleError = ServiceByFlaskMiddlewareHelper.handleError(it)
                                sample_text.text = "handleError.code: ${handleError.code}\n handleError.description: ${handleError.description} \n handleError.message: ${handleError.message}\n"
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
