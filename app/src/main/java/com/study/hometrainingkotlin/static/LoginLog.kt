package com.study.hometrainingkotlin.static

class LoginLog {

    public var loginIdLog:String
        get() {
            return loginIdLog
        }
        set(LoginIdLog:String) {
            this.loginIdLog = LoginIdLog
        }

    public var loginPwLog:String
        get() {
            return loginPwLog
        }
        set(LoginPwLog:String) {
            this.loginPwLog = LoginPwLog
        }

//싱글톤
    companion object{
        @JvmStatic
        private var INSTANCE:LoginLog ?=null

        @JvmStatic
        fun getInstance():LoginLog? {
            return INSTANCE
        }
    }



}