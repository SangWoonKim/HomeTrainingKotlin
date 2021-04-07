package com.study.hometrainingkotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.study.hometrainingkotlin.model.repository.ExerciseRepository
import com.study.hometrainingkotlin.model.vo.ExerciseData

//뷰모델 (굳이 쓸 필요가 없으나 이번기회에 사용해봄)
//데이터를 조회하는 것 밖에 안하기 때문에 서버에서 값이 달라지지 않는 한 바뀌지 않음
//만약 서버에서 값이 달라지는 것을 알려면 사용하려는 메소드를 재호출해야함(ex)Thread사용)
class ExerciseViewModel: ViewModel() {
    //ExerciseRepository에 접근할 수 있는 인스턴스를 저장
    private val exerciseRepository: ExerciseRepository = ExerciseRepository.getInstance()!!

    //상체 부위 배열데이터를 반환
    fun getUppers() : MutableLiveData<ArrayList<ExerciseData>> {
        return exerciseRepository.getExerciseUpper()
    }

//    fun getLowers() {
//        return upperLiveData.postValue(exerciseRepository.getExerciseLower())
//    }
//
//    fun getBodies() {
//        return upperLiveData.postValue(exerciseRepository.getExerciseBody())
//    }
//
//    fun getLoins() {
//        return upperLiveData.postValue(exerciseRepository.getExerciseLoins())
//    }



}

