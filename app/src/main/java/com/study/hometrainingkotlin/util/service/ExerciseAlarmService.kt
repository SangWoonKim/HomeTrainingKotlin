package com.study.hometrainingkotlin.util.service

import android.app.AlarmManager
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.work.*
import com.study.hometrainingkotlin.util.appwidget.ExerciseWidget
import com.study.hometrainingkotlin.util.intentfilter.ExerciseAlarmAction
import com.study.hometrainingkotlin.util.notification.AlarmNotification
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class ExerciseAlarmService : Service() {

    private var alarmManager:AlarmManager ?= null
    private val exerciseServiceBinder: IBinder ?= ExerciseAlarmServiceBinder()
    private var mediaPlayer : MediaPlayer ?= null
    //executor서비스
    private var executorService : ExecutorService ?=null
    private lateinit var stopwatch:Runnable
    var second:Int =0
//        private set
    companion object{
        var appWidgetBroadCast: ExerciseWidget = ExerciseWidget()
    }

    //서비스 생성시 호출
    override fun onCreate() {
        super.onCreate()
        Log.d("서비스 생성","create")
        //알람매니저 객체 생성
        var alarmManager:AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        //appwidget에 receiver등록
        registerBroadCast()
        //실행할 쓰레드 1개 생성
        executorService = Executors.newFixedThreadPool(1)

    }

    //백그라운드 timer기능 생성 및 쓰레드생성
    private fun timerRunning(){

        stopwatch = Runnable {
            while (!Thread.currentThread().isInterrupted){
                try {
                    TimeUnit.SECONDS.sleep(1)
                }catch (e:InterruptedException){
                    Thread.currentThread().interrupt()
                    break
                }
                second++
                Log.d("timerRunning() Start",second.toString())
            }
        }

    }


    //바인더 호출 및 반환
    override fun onBind(intent: Intent?): IBinder? {
        return exerciseServiceBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("서비스 시작","onStartCommand Call")
        if (intent != null){
            val action = intent.action
            if (ExerciseAlarmAction.SET_ALARM == action){
                setAlarm()
            }else if (ExerciseAlarmAction.RUNNING == action){
                running()
            }else if (ExerciseAlarmAction.CLOSE == action){
                close()
            }else if (ExerciseAlarmAction.COMPLETE_EXERCISE == action){
                complete()
            }
        }
        //인텐트 남기기
        return START_REDELIVER_INTENT
    }

    //appWidger을 위한 인텐트필터 암시적으로 등록
    private fun registerBroadCast(){
        val filter = IntentFilter()
        filter.addAction(ExerciseAlarmAction.CLOSE)
        filter.addAction(ExerciseAlarmAction.COMPLETE_EXERCISE)
        filter.addAction(ExerciseAlarmAction.RUNNING)
        filter.addAction(ExerciseAlarmAction.SET_ALARM)
        registerReceiver(appWidgetBroadCast,filter)
    }

    //해제
    private fun unRegisterBroadCast(){
        unregisterReceiver(appWidgetBroadCast)
    }

    //정지
    fun complete(){
        //play
    }
    //시작
    fun setAlarm(){
        //threadStart

    }
    //실행중
    fun running(){
        //handler를 이용한 화면 표시
    }
    //초기
    fun close(){
        //stop
    }

    //알람매니저 설정
    fun setTime(calendar: Calendar){
//        var intentValue =Intent(this,ExerciseAlarmNotification::class.java).putExtra("state","alarm_on")
//        var pendingIntent: PendingIntent = PendingIntent.getBroadcast(this,0,intentValue,PendingIntent.FLAG_UPDATE_CURRENT)
//        alarmManager!!.set(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,pendingIntent)
        timerRunning()
        executorService!!.execute(stopwatch)
        setNotification(second)
    }

    //알람매니저 리셋
    fun offTime(){
//        var intentValue =Intent(this,ExerciseAlarmNotification::class.java).putExtra("state","alarm_off")
//        var pendingIntent: PendingIntent = PendingIntent.getBroadcast(this,0,intentValue,PendingIntent.FLAG_UPDATE_CURRENT)
//        alarmManager!!.cancel(pendingIntent)
        stopSelf()
    }

    override fun onDestroy() {
        Log.d("onDestroy","서비스 종료")
        super.onDestroy()
        //다른메소드 찾기
        //shutdown은 쓰레드 자체를 삭제 즉 쓰레드는 남기고 runnable을 없애야함
        executorService!!.shutdownNow()
        unRegisterBroadCast()
    }

    //notification값 갱신
    //정기적인 갱신은 15분에 한번씩 가능
    //쓰레드를 늘려 단축 시킬수 있으나 이런 기능에 쓰레드를 할당하는 것은 사치
    //포그라운드 작업을 생각해봐야함
    private fun setNotification(timer:Int){

        //intent.putString이라 생각하면 편함
        var timerData = workDataOf("time" to timer)
        //val notificationWorker = OneTimeWorkRequestBuilder<AlarmNotification>()
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .build()
        val notificationWorker = PeriodicWorkRequestBuilder<AlarmNotification>(4,TimeUnit.SECONDS)
            //최소 5초 후에 실행
            .setInitialDelay(5,TimeUnit.SECONDS)
            .setInputData(timerData)
            .setConstraints(constraints)
            .build()
//        WorkManager.getInstance(applicationContext).enqueueUniqueWork("timer",
//        ExistingWorkPolicy.REPLACE,notificationWorker)
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork("timer",ExistingPeriodicWorkPolicy.REPLACE,notificationWorker)
    }

    //그 해결책으로 나온 것
    //배터리 소모량 극심함
    //쩔수 없음 그래도 이거 사용해야할듯
    override fun startForegroundService(service: Intent?): ComponentName? {
        return super.startForegroundService(service)
    }

    inner class ExerciseAlarmServiceBinder : Binder() {
        fun getBinder(): ExerciseAlarmService? {
            return this@ExerciseAlarmService
        }
    }
}