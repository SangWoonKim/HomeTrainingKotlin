package com.study.hometrainingkotlin.Model.utils

import com.study.hometrainingkotlin.Model.Login_Data
import com.study.hometrainingkotlin.Model.vo.ExerciseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ExerciseInterface {

    //운동부위를 보내 결과값을 반환
    @GET("exercise/{E_part}")
    fun getExercisePartData(@Path("E_part") exercisePart:String): Call<ArrayList<ExerciseData>>?
}