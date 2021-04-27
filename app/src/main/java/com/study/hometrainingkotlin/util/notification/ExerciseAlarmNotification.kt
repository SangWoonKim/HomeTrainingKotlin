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


class ExerciseAlarmNotification: BroadcastReceiver() {

    private var exerciseAlarmService: ExerciseAlarmService? = null
    private var notificationManager: NotificationManager? = null
    private var context:Context ?= null
    private var exerciseService = Intent(context,ExerciseAlarmService::class.java)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        this.context = context
        var getStatus = intent!!.extras!!.getString("state")

        if (getStatus!! == "alarm_on") {
            createNotification()
        } else if (getStatus!! == "alarm_off") {
            cancelNotificaiton()
        }
    }


    //오레오 이후버전만 허용
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification() {
        context!!.startForegroundService(exerciseService)
    }

    private fun cancelNotificaiton(){
        context!!.stopService(exerciseService)
    }



}