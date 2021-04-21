package com.study.hometrainingkotlin.view.adapter

//실제로 사용되지 않음
//ExerciseListAdapter에 상속되어있음
//스와이프시
interface ItemTouchHelperListener {
    fun onItemSwipe(position:Int)
}