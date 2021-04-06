package com.study.hometrainingkotlin.Model.vo

//운동에 대한 정보 저장 클래스
data class ExerciseData(
    val E_part:String,
    val E_name:String,
    val E_setcal:String,
    val E_image:String,
    val E_imageorg:String,
    val E_activeimg:String,
    val E_activeimg2:String,
) {
}