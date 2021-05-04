package com.study.hometrainingkotlin.util.application

import android.app.Application
import android.content.SharedPreferences
import com.study.hometrainingkotlin.util.Theme.ThemeUtil

class ServiceApplication: Application() {
    //login과 alarmLog의 전역변수를 여기에 넣어서 사용가능 하나 일단은
    //따로 구현 후 정리 및 리팩토링하기


    //singleton
    companion object{
        private var serviceApplicationInstance: ServiceApplication ?= null

        fun getInstance(): ServiceApplication? {
            return serviceApplicationInstance
        }
    }

    //서비스에 접근이 가능한 클래스
    private var serviceInterface: ServiceInterface ?=null

    fun getServiceInterface(): ServiceInterface?
    {
        return serviceInterface
    }

    //Application클래스는 앱실행과 동시에 생성됨
    //Application클래스의 객체는 앱의 생명주기와 관련되어있음으로
    //앱이 죽기 전까지 계속 살아있음 즉 홈화면으로 나가도 통신이 가능함
    override fun onCreate() {
        super.onCreate()
        //이 클래스가 메모리에 올라감과 동시에 객체가 생성됨
        serviceApplicationInstance = this
        //객체 생성
        serviceInterface = ServiceInterface(applicationContext)
        //preference파일 객체화
        var theme:SharedPreferences = getSharedPreferences("com.study.hometrainingkotlin.prefs", MODE_PRIVATE)
        //preference파일에서 themeSelect로 된 키값을 가져옴
        var themeValue: String? = theme.getString("themeSelect","dark")
        //해당 값을 파라미터로 보내어 테마 변경
        ThemeUtil.applyTheme(themeValue!!)
    }
}