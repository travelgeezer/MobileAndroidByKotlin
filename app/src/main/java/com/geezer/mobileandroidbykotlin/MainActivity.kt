package com.geezer.mobileandroidbykotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.geezer.mobileandroidbykotlin.service.SimpleService
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        sample_text.text = stringFromJNI()

        Flowable.fromCallable({
            val retrofit = Retrofit.Builder()
                    .baseUrl(SimpleService.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val github = retrofit.create(SimpleService.GitHub::class.java)
            val call = github.contributors("square", "retrofit")
            call.execute().body()
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it?.forEach({
                        Log.d("MainActivity", "login: ${it.login}  contributors: ${it.contributor}")
                    })
                })


        Flowable.fromCallable({
            val retrofit = Retrofit.Builder()
                    .baseUrl(SimpleService.SERVICE_BY_FLASK_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val serviceByFlask = retrofit.create(SimpleService.ServiceByFlask::class.java)
            val call = serviceByFlask.responseDataFormat("call responseDataFormat request")
            call.execute().body()
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    sample_text.text = it?.data
                })
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
