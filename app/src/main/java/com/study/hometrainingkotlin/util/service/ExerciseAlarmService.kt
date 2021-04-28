package com.study.hometrainingkotlin.util.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.study.hometrainingkotlin.R
import com.study.hometrainingkotlin.util.appwidget.ExerciseWidget
import com.study.hometrainingkotlin.util.intentfilter.ExerciseAlarmAction
import com.study.hometrainingkotlin.util.notification.ExerciseAlarmNotification
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class ExerciseAlarmService : Service() {
    /**
     * 서비스 기능구현을 위한 객체 명세
     * */
    private var alarmManager: AlarmManager? = null
    private val exerciseServiceBinder: IBinder? = ExerciseAlarmServiceBinder()
    private var mediaPlayer: MediaPlayer ?= null

    //executor서비스
    private var executorService: ExecutorService? = null
    private lateinit var stopwatch: Runnable
    var second: Int = 0

    companion object {
        var appWidgetBroadCast: ExerciseWidget = ExerciseWidget()
    }

    //notification 객체 생성을 위한 전역 객체
    private var notiBuilder: NotificationCompat.Builder? = null

    //notification 갱신 삭제를 위한 객체
    private var notificationManager: NotificationManager? = null

    //객체 상태 저장
    private var mediaPlayerState:Boolean=false

    /**
     * 바인더를 반환하기위한 내부클래스
     * */

    //이 클래스의 서비스의 바인더를 반환하는 내부클래스 즉 인스턴스를 반환함
    inner class ExerciseAlarmServiceBinder : Binder() {
        fun getBinder(): ExerciseAlarmService? {
            return this@ExerciseAlarmService
        }
    }

    /**
     * 서비스 생명주기에 대한 메소드
     * */
    //서비스 생성시 호출
    override fun onCreate() {
        super.onCreate()
        Log.d("서비스 생성", "create")
        //알람매니저 객체 생성
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        //appwidget에 receiver등록
        registerBroadCast()
        //실행할 쓰레드 1개 생성
        executorService = Executors.newFixedThreadPool(1)

    }


    //바인더 호출 및 반환
    override fun onBind(intent: Intent?): IBinder? {
        return exerciseServiceBinder
    }

    //ExerciseAlarmNotification을 통해 작동되는 메소드(startService호출됨)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("서비스 시작", "onStartCommand Call")

        var getState = intent?.extras?.getString("state")
        //첫번 째 인자가 boolean으로 평가되는 표현식
        //값을 받아 true이면 실행
        // false면 AssertionError를 예외를 발생시키는 예약어
        //근데 여기서 에러가 잡히네 이거...
        assert(getState != null)
        when(getState){
            "alarm_on" ->{
                complete()
            }

            "alarm_off" ->{
                initialize()
            }
            //default
            else ->{
                defaultState()
            }
        }
        //인텐트 남기기
        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroy() {
        Log.d("onDestroy", "서비스 종료")
        super.onDestroy()
        closeService()
    }

    /**
     * 서비스의 기능에 대한 메소드
     * */

    //appWidger을 위한 인텐트필터 암시적으로 등록
    private fun registerBroadCast() {
        val filter = IntentFilter()
        filter.addAction(ExerciseAlarmAction.ALARM_ON)
        filter.addAction(ExerciseAlarmAction.ALARM_OFF)
        registerReceiver(appWidgetBroadCast, filter)
    }

    //해제
    private fun unRegisterBroadCast() {
        unregisterReceiver(appWidgetBroadCast)
    }

    //백그라운드 timer기능 생성 및 쓰레드생성
    //시 분 초 만들기 알고리즘 만들어서 사용
    private fun timerRunning() {

        stopwatch = Runnable {
            while (!Thread.currentThread().isInterrupted) {
                try {
                    TimeUnit.SECONDS.sleep(1)
                    notiBuilder!!.setContentText(second.toString())
                    notificationManager!!.notify(1, notiBuilder!!.build())
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                    break
                }
                second++
                Log.d("timerRunning() Start", second.toString())
            }
        }
    }

    //포그라운드 서비스를 위한 메소드
    @RequiresApi(Build.VERSION_CODES.O)
    private fun foregroundServiceNotificaition() {
        timerRunning()
        createNotiChannel()
        createNoti()
        startForeground(1, notiBuilder!!.build())
    }

    //noti생성을 위한 메소드
    private fun createNoti() {
        notiBuilder = NotificationCompat.Builder(this, "1")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentText(second.toString())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
    }

    //notichannel생성을 위한 메소드
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotiChannel() {
        //채널을 생성
        val name = "ForegroundService Noti Channel!"
        val descriptionText = "포그라운드서비스를 위한 채널"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("1", name, importance).apply {
            description = descriptionText
        }

        //채널을 시스템에 등록
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager!!.createNotificationChannel(channel)
    }


    //알람이 울리면 실행
    private fun complete() {
        mediaPlayer = MediaPlayer.create(this,R.raw.test)
        mediaPlayer!!.start()
        mediaPlayerState=true

    }
    //알람을 멈출때 사용
    private fun initialize(){
        if (mediaPlayerState) {
            mediaPlayer!!.stop()
            mediaPlayer!!.reset()
            mediaPlayer!!.release()
            mediaPlayerState==false
        }
    }
    //초기 상태로 변환
    private fun defaultState() {
        if (mediaPlayerState) {
            initialize()
            mediaPlayer=null
        }
    }

    //onDestroy에서 사용되는 메소드드
    @RequiresApi(Build.VERSION_CODES.O)
    private fun closeService(){
        var intentValue =
            Intent(this, ExerciseAlarmNotification::class.java).putExtra("state", "alarm_off")
        var pendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, intentValue, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager!!.cancel(pendingIntent)
        sendBroadcast(intentValue)
        executorService?.shutdownNow()
        unRegisterBroadCast()
        //알림삭제
        notificationManager?.cancelAll()
        //알림채널삭제
        notificationManager?.deleteNotificationChannel("1")
        executorService = null
    }

    /**
     * bindService를 위한 메소드
     * */

    //알람매니저 설정
    @RequiresApi(Build.VERSION_CODES.O)
    fun setTime(calendar: Calendar) {
        var intentValue =
            Intent(this, ExerciseAlarmNotification::class.java).putExtra("state", "alarm_on")
        var pendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, intentValue, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager!!.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        foregroundServiceNotificaition()
//        timerRunning()
        executorService!!.execute(stopwatch)
//        setNotification(second)

    }


    //알람매니저 리셋
    //알람이 울릴경우
    @RequiresApi(Build.VERSION_CODES.O)
    fun offTime() {
        var intentValue =
            Intent(this, ExerciseAlarmNotification::class.java).putExtra("state", "alarm_off")
        var pendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, intentValue, PendingIntent.FLAG_UPDATE_CURRENT)
        //알람메니저 취소
        alarmManager!!.cancel(pendingIntent)
        //ExerciseAlarmNotification에 "alarm_off"방송
        sendBroadcast(intentValue)
//        stopSelf()
        executorService?.shutdownNow()
        //알림삭제
        notificationManager?.cancelAll()
        //알림채널삭제
        notificationManager?.deleteNotificationChannel("1")
        executorService = null
        //다시 쓰레드 생성
        executorService = Executors.newFixedThreadPool(1)
        //시간 초기화
        second =0
        Log.d("offTime()호출", "offTime")
    }




    //notification값 갱신
    //정기적인 갱신은 15분에 한번씩 가능
    //쓰레드를 늘려 단축 시킬수 있으나 이런 기능에 쓰레드를 할당하는 것은 사치
    //포그라운드 작업을 생각해봐야함
//    private fun setNotification(timer:Int){
//
//        //intent.putString이라 생각하면 편함
//        var timerData = workDataOf("time" to timer)
//        //val notificationWorker = OneTimeWorkRequestBuilder<AlarmNotification>()
//        val constraints = Constraints.Builder()
//            .setRequiresCharging(true)
//            .build()
//        val notificationWorker = PeriodicWorkRequestBuilder<AlarmNotification>(4,TimeUnit.SECONDS)
//            //최소 5초 후에 실행
//            .setInitialDelay(5,TimeUnit.SECONDS)
//            .setInputData(timerData)
//            .setConstraints(constraints)
//            .build()
////        WorkManager.getInstance(applicationContext).enqueueUniqueWork("timer",
////        ExistingWorkPolicy.REPLACE,notificationWorker)
//        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork("timer",ExistingPeriodicWorkPolicy.REPLACE,notificationWorker)
//    }


}