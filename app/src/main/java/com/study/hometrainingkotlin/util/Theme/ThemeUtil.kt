package com.study.hometrainingkotlin.util.Theme

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate

class ThemeUtil {
    companion object{
        const val LIGHT ="light"
        const val DARK ="dark"
        const val DEFAULTTHEME ="default"

        //파라미터에 따라 실제로 테마가 변경되는 부분
        //액티비티나 회면을 새로고침함
        @JvmStatic
        fun applyTheme(color:String){
            when(color){
                LIGHT->{ AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)}
                DARK->{AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)}
                DEFAULTTHEME->{
                    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                        //다크모드 활성화 했을 경우 나이트모드 설정
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    }else{
                        //절전모드 활성화 했을 경우 나이트 모드 설정
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                    }
                }
            }
        }

    }
}