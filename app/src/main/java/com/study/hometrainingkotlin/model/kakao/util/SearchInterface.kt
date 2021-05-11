package com.study.hometrainingkotlin.model.kakao.util

import com.study.hometrainingkotlin.model.kakao.vo.Documents
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SearchInterface {

    //카카오맵 서버에 reqeust를 하기위한 메소드
    @GET("/v2/local/search/keyword.json")
    fun searchKeyword(
        //여기서 에러.. 왜? postman은 잘 받더만 여기는 왜?
        @Header("Authorization") restApiKey: String,
        @Query("query") keyWord:String,
        @Query("x") x:String,
        @Query("y") y:String,
        @Query("radius") radius:Int,
        @Query("size") size:Int,
    ):Call<Documents>
}