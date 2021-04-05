package com.study.hometrainingkotlin.static

class LoginLog {

//     var loginIdLog:String
//        get() {
//            return loginIdLog
//        }
//        set(LoginIdLog: String) {
//            this.loginIdLog = LoginIdLog
//        }
//
//     var loginPwLog:String
//        get() {
//            return loginPwLog
//        }
//        set(LoginPwLog: String) {
//            this.loginPwLog = LoginPwLog
//        }

//싱글톤

    var loginIdLog:String ?= null

    var loginPwLog:String ?= null

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