package com.study.hometrainingkotlin.view.Splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.study.hometrainingkotlin.view.Login.Login

class initSplashView: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try{
            Thread.sleep(3000)
        }catch (e:InterruptedException){
            e.printStackTrace()
        }
        startActivity(Intent(this,Login::class.java))
        finish()
    }
}