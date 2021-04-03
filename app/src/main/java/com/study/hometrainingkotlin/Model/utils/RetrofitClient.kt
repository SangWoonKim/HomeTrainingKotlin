package com.study.hometrainingkotlin.Model.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    object RetrofitClient{
        private const val BASE_URL = "http://10.0.2.2:3000/v1/"
        private var retrofitInstance : Retrofit?=null

        fun getInstance():Retrofit{
            var gson:Gson = GsonBuilder().setLenient().create()
            if (retrofitInstance == null){
                retrofitInstance = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(OkHttpClient().newBuilder().build())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }
            return retrofitInstance!!
        }
    }


}