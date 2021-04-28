package com.study.hometrainingkotlin.static

//Login정보 저장
class LoginLog {

    var loginIdLog:String ?= null
    var loginPwLog:String ?= null

    //singleton
    companion object{
        @JvmStatic
        private var INSTANCE:LoginLog ?=null

        @JvmStatic
        fun getInstance():LoginLog? {
            if (INSTANCE ==null){
                INSTANCE = LoginLog()
            }
            return INSTANCE
        }
    }

    //getter
    fun getloginIdLog(): String? {
        return loginIdLog
    }

    fun getloginPwLog(): String? {
        return loginPwLog
    }


    //setter
    fun setloginIdLog(id: String) {
        this.loginIdLog = id
    }

    fun setloginPwLog(pw: String) {
        this.loginPwLog = pw
    }

}