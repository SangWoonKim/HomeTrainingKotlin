package com.study.hometrainingkotlin.view.alarm

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
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
    private var serviceState:Boolean=true
    private var serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            exerciseAlarmService =
                (service as ExerciseAlarmService.ExerciseAlarmServiceBinder).getBinder()
            Log.d("서비스 연결", "bindService호출")
            serviceState=true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            exerciseAlarmService = null
            serviceState=false
            Log.d("서비스 연결 해제", "서비스객체 null")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alarm_activity)
        timePicker = findViewById(R.id.timePicker)
        BTN_Alarm_Start = findViewById(R.id.BTN_Alarm_Start)
        BTN_Alarm_Stop = findViewById(R.id.BTN_Alarm_Stop)
        BTN_Alarm_Stop!!.setOnClickListener(this)
        BTN_Alarm_Start!!.setOnClickListener(this)

        calendar = Calendar.getInstance()
        //서비스의 연결상태에 따른 인터페이스

        //서비스 바인딩 onCreate시 바인딩
        //뒤로가기시 꺼짐 이유는 액티비티의 생명주기에 따른 Binder통신이기에
        //액티비티 소멸시 onUnbind호출됨 즉 application 클래스를 생성하여 만들어야함 (앱 생명주기에 따라 삭제되기 때문)
        //아님 브로드캐스트를 쓰던가
       val intent = Intent(this@Alarm,ExerciseAlarmService::class.java)
        bindService(intent.setPackage(this.packageName),
            serviceConnection as ServiceConnection, Context.BIND_AUTO_CREATE)
    }




    //참고로 onBind()는 한번만 호출됨 즉 서비스 종료를 호출하면 Binder객체가 없어짐
    //액티비티를 나갔다 들어오지 않는 이상 Binder객체는 다시 호출이 불가
    //BindService는 생명주기에 맞춰 실행됨으로 버튼으로는 실행이 불가능함
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v!!.id) {
            //알람설정버튼
            R.id.BTN_Alarm_Start -> {
                //설정한 시간 정보 켈린더 객체에 저장
                calendar!!.set(Calendar.HOUR_OF_DAY, timePicker!!.hour)
                calendar!!.set(Calendar.MINUTE, timePicker!!.minute)
//                startService(intent)
                if (!serviceState){
                    bindService(intent, serviceConnection as ServiceConnection, Context.BIND_AUTO_CREATE)
                    exerciseAlarmService!!.setTime(calendar!!)
                }else {
                    exerciseAlarmService!!.setTime(calendar!!)
                }
            }

            //알람삭제버튼
            R.id.BTN_Alarm_Stop -> {

                if (serviceState) {
                    bindService(intent, serviceConnection as ServiceConnection, Context.BIND_AUTO_CREATE)
                    exerciseAlarmService!!.offTime()
//                    unbindService(serviceConnection as ServiceConnection)     //안쓸듯 어차피 액티비티 생명주기에 따라 없어짐
                } else {
                    Toast.makeText(this, "서비스 실행중이 아님", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}