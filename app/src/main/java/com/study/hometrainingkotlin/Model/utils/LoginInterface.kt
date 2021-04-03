package com.study.hometrainingkotlin.Model.utils

import com.study.hometrainingkotlin.Model.Login_Data
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.*
import kotlin.collections.HashMap

interface LoginInterface {

    @FormUrlEncoded
    @POST("users/login")
    fun post_Login(@FieldMap insert_parameter: HashMap<String, String>): Call<Login_Data>?

    @FormUrlEncoded
    @POST("create")
    fun post_CreateUser(@FieldMap insert_parameter: HashMap<String?, String?>?): Call<Login_Data>?

}