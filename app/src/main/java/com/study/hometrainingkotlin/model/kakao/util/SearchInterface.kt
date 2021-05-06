package com.study.hometrainingkotlin.model.kakao.util

import com.study.hometrainingkotlin.model.kakao.vo.Documents
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SearchInterface {
    @GET("/v2/local/search/keyword.json")
    fun searchKeyword(
        @Header("authorization") restApi:String,
        @Query("query") keyWord:String,
        @Query("x") x:String,
        @Query("y") y:String,
        @Query("radius") radius:Int,
        @Query("size") size:Int,
    ):Call<Documents>
}