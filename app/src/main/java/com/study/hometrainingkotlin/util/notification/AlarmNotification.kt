package com.study.hometrainingkotlin.util.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.study.hometrainingkotlin.R
import com.study.hometrainingkotlin.util.service.ExerciseAlarmService

class AlarmNotification(context: Context,workerParam: WorkerParameters): Worker(context, workerParam) {


    //백그라운드에서 호출
    //실행되는 부분
    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        val timerCount = inputData.getInt("time",0)
        sendNotification(timerCount)
        return Result.success()
    }



    //노티피케이션 생성
    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(timer:Int){
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel("noti_channel","stopWatch",NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(applicationContext,"noti_channel")
            .setContentTitle("exampleTimer(처음써보는 worker)")
            .setContentText(timer.toString())
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        notificationManager.notify(0x123,notification)

    }
}