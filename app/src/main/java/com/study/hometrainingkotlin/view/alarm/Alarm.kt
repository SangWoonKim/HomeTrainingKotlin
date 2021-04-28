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
import com.study.hometrainingkotlin.util.application.ServiceApplication
import com.study.hometrainingkotlin.util.service.ExerciseAlarmService
import kotlinx.android.synthetic.main.alarm_activity.*
import java.util.*

//알람 액티비티
class Alarm : AppCompatActivity(), View.OnClickListener {

    private var BTN_Alarm_Start: Button? = null
    private var BTN_Alarm_Stop: Button? = null
    private var timePicker: TimePicker? = null
    private var calendar: Calendar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alarm_activity)
        timePicker = findViewById(R.id.timePicker)
        BTN_Alarm_Start = findViewById(R.id.BTN_Alarm_Start)
        BTN_Alarm_Stop = findViewById(R.id.BTN_Alarm_Stop)
        BTN_Alarm_Stop!!.setOnClickListener(this)
        BTN_Alarm_Start!!.setOnClickListener(this)
        //캘린더 인스턴스 얻어옴
        calendar = Calendar.getInstance()
    }




    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v!!.id) {
            //알람설정버튼
            R.id.BTN_Alarm_Start -> {
                //설정한 시간 정보 켈린더 객체에 저장
                calendar!!.set(Calendar.HOUR_OF_DAY, timePicker!!.hour)
                calendar!!.set(Calendar.MINUTE, timePicker!!.minute)
                //application객체의 인스턴스를 얻어와 서비스와 통신
                ServiceApplication.getInstance()!!.getServiceInterface()!!.setTime(calendar!!)
            }

            //알람삭제버튼
            R.id.BTN_Alarm_Stop -> {
                //application객체의 인스턴스를 얻어와 서비스와 통신
                ServiceApplication.getInstance()!!.getServiceInterface()!!.offTime()
            }

        }
    }
}