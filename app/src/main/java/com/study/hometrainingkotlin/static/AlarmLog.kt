package com.study.hometrainingkotlin.static

//운동 설정시간 완료시 true
data class AlarmLog(var alarmState:Boolean) {

    companion object{
        private var alarmLog:AlarmLog ?= null

        fun getAlarmLog():AlarmLog?{
            if (alarmLog==null){
                alarmLog=AlarmLog(false)
            }
            return alarmLog
        }
    }

}