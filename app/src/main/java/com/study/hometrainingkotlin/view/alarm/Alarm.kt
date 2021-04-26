package com.study.hometrainingkotlin.view.alarm

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import com.study.hometrainingkotlin.R
import com.study.hometrainingkotlin.util.service.ExerciseAlarmService
import kotlinx.android.synthetic.main.alarm_activity.*
import java.util.*

class Alarm : AppCompatActivity(), View.OnClickListener {

    private var BTN_Alarm_Start: Button? = null
    private var BTN_Alarm_Stop: Button? = null
    private var timePicker: TimePicker? = null
    private var calendar: Calendar? = null
    private var exerciseAlarmService: ExerciseAlarmService? = null
    private var serviceConnection: ServiceConnection ?=null
    //    var context:Context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alarm_activity)
        timePicker = findViewById(R.id.timePicker)
        BTN_Alarm_Start = findViewById(R.id.BTN_Alarm_Start)
        BTN_Alarm_Stop = findViewById(R.id.BTN_Alarm_Stop)
        BTN_Alarm_Stop!!.setOnClickListener(this)
        BTN_Alarm_Start!!.setOnClickListener(this)

        calendar = Calendar.getInstance()
        serviceConnection = object : ServiceConnection {
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
        val intent = Intent(this@Alarm,ExerciseAlarmService::class.java)
        bindService(intent.setPackage(this.packageName),
            serviceConnection as ServiceConnection, Context.BIND_AUTO_CREATE)
    }


    //서비스의 연결상태에 따른 인터페이스


    override fun onClick(v: View?) {
        when (v!!.id) {
            //알람설정버튼
            R.id.BTN_Alarm_Start -> {
                //설정한 시간 정보 켈린더 객체에 저장
                calendar!!.set(Calendar.HOUR_OF_DAY, timePicker!!.hour)
                calendar!!.set(Calendar.MINUTE, timePicker!!.minute)


//                startService(intent)
                exerciseAlarmService!!.setTime(calendar!!)
            }

            //알람삭제버튼
            R.id.BTN_Alarm_Stop -> {

                if (exerciseAlarmService != null) {
                    bindService(intent, serviceConnection as ServiceConnection, Context.BIND_AUTO_CREATE)
                    exerciseAlarmService!!.offTime()
                    unbindService(serviceConnection as ServiceConnection)
                } else {
                    Toast.makeText(this, "서비스 실행중이 아님", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}