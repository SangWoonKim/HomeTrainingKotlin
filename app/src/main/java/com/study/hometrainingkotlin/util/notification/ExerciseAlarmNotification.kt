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

    private var context:Context ?= null
    private var exerciseService :Intent ?=null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        this.context = context
        //String값을 전달할 component정의
        exerciseService = Intent(context,ExerciseAlarmService::class.java)
        var getStatus = intent!!.extras!!.getString("state")

        exerciseService?.apply { putExtra("state", getStatus) }
        createNotification()
    }


    //오레오 이후버전만 허용
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification() {
        context!!.startService(exerciseService)
    }

}