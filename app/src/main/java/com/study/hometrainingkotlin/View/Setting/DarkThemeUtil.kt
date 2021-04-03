package com.study.hometrainingkotlin.View.Setting

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate

class DarkThemeUtil {
    companion object{
        const val LIGHT_MODE:String ="light"
        const val DARK_MODE:String ="dark"
        const val DEFAULT_MODE:String ="default"

        @JvmStatic
        fun applyTheme(color:String){
            when(color){
                LIGHT_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                DARK_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                else -> if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                }
            }
        }


    }
}