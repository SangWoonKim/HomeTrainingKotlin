package com.study.hometrainingkotlin.model.utils

import com.study.hometrainingkotlin.model.Login_Data
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import kotlin.collections.HashMap

interface LoginInterface {

    @FormUrlEncoded
    @POST("users/login")
    fun post_Login(@FieldMap login_parameter: HashMap<String,String>): Call<Login_Data>?

    @FormUrlEncoded
    @POST("create")
    fun post_CreateUser(@FieldMap insert_parameter: HashMap<String?, String?>?): Call<Login_Data>?

}