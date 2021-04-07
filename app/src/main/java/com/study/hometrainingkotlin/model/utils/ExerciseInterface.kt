package com.study.hometrainingkotlin.model.utils

import com.study.hometrainingkotlin.model.vo.ExerciseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ExerciseInterface {

    //운동부위를 보내 결과값을 반환
    @GET("exercise/index/{E_part}")
    fun getExercisePartData(@Path("E_part") exercisePart:String): Call<ArrayList<ExerciseData>>?
}