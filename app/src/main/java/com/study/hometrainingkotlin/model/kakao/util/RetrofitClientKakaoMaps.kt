package com.study.hometrainingkotlin.model.kakao.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClientKakaoMaps {
    object RetrofitKakaoClient{
        private const val BASE_URL = "https://dapi.kakao.com/"
        private var retrofitInstance : Retrofit?=null

        fun getInstance(): Retrofit {
            var gson: Gson = GsonBuilder().setLenient().create()

            //타임아웃 시간 설정
            var okHttpClient = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build()

            if (retrofitInstance == null){
                retrofitInstance = Retrofit.Builder()
                    //서버 URL
                    .baseUrl(BASE_URL)
                    //타임아웃 시간 적용
                    .client(okHttpClient)
                    //데이터 파싱
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    //객체 정보 반환
                    .build()
            }
            return retrofitInstance!!
        }
    }
}