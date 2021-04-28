package com.study.hometrainingkotlin.util.application

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import com.study.hometrainingkotlin.util.service.ExerciseAlarmService
import java.util.*

class ServiceInterface(context: Context) {


    private var exerciseAlarmService: ExerciseAlarmService? = null

    //IBinder에 대한 유무를 확인하는 인터페이스 같음
    private var serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            exerciseAlarmService =
                (service as ExerciseAlarmService.ExerciseAlarmServiceBinder).getBinder()
            Log.d("서비스 연결", "bindService호출")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            exerciseAlarmService = null
            Log.d("서비스 연결 해제", "서비스객체 null")
        }
    }

    init {
        val intent = Intent(context,ExerciseAlarmService::class.java).setPackage(context.packageName)
        context.bindService(intent,serviceConnection,Context.BIND_AUTO_CREATE)
    }

    //서비스의 setTime호출
    @RequiresApi(Build.VERSION_CODES.O)
    fun setTime(calendar: Calendar){
        if (exerciseAlarmService!= null){
            exerciseAlarmService?.setTime(calendar)
        }
    }

    //서비스의 offTime호출
    @RequiresApi(Build.VERSION_CODES.O)
    fun offTime(){
        if (exerciseAlarmService!= null){
            exerciseAlarmService?.offTime()
        }
    }
}