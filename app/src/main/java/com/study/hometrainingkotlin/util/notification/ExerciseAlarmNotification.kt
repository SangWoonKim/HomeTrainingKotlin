package com.study.hometrainingkotlin.util.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.study.hometrainingkotlin.R
import com.study.hometrainingkotlin.util.service.ExerciseAlarmService
import com.study.hometrainingkotlin.view.alarm.Alarm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ExerciseAlarmNotification {

    private var exerciseAlarmService: ExerciseAlarmService? = null
    private var notificationManager: NotificationManager? = null


    //수신자(서비스에서 액션값이 전달됨)
    val broadcastReceiver = object : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onReceive(context: Context?, intent: Intent?) {
            var getStatus = intent!!.extras!!.getString("state")

            if (getStatus!! == "alarm_on") {
                createNotification()
            } else if (getStatus!! == "alarm_off") {
                cancelNotificaiton()
            }
        }
    }


    //오레오 이후버전만 허용
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification() {
        var remoteViews: RemoteViews
        var notificationBuilder: NotificationCompat.Builder
        var pendingIntent: PendingIntent
        var activityOpenIntent = Intent(exerciseAlarmService, Alarm::class.java)

        //remoteView클릭시 나타낼 액티비티를 pendingIntent를 이용하여정의
        pendingIntent = PendingIntent.getActivity(exerciseAlarmService, 0, activityOpenIntent, 0)
        remoteViews = RemoteViews(exerciseAlarmService!!.packageName, R.layout.noti_widget)
        notificationManager = exerciseAlarmService!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var channel = NotificationChannel(
            "noti_channel",
            "noti_chaannel",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = "운동설정 시간"
        notificationManager!!.createNotificationChannel(channel)

        notificationBuilder = NotificationCompat.Builder(exerciseAlarmService!!, "noti_channel")
            notificationBuilder
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setCustomContentView(remoteViews)
        var notification : Notification = notificationBuilder.build()
        notification.contentIntent = pendingIntent
        exerciseAlarmService!!.startForeground(0x123,notification)
    }

    private fun cancelNotificaiton(){
        if (notificationManager == null) {
            notificationManager!!.cancel(0x123)
        }
    }

}