package com.study.hometrainingkotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.study.hometrainingkotlin.Model.repository.ExerciseRepository
import com.study.hometrainingkotlin.Model.vo.ExerciseData

class ExerciseViewModel(): ViewModel() {
    private val exerciseRepository: ExerciseRepository = ExerciseRepository.getInstance()!!

//    private var upperLiveData: MutableLiveData<ArrayList<ExerciseData>> = MutableLiveData()

//    private val upperLiveData: MutableLiveData<java.util.ArrayList<ExerciseData>> = MutableLiveData<java.util.ArrayList<ExerciseData>>()
    private var upperLiveData: MutableLiveData<ArrayList<ExerciseData>> = MutableLiveData()
//    by lazy{
//        .also { getUppers() }
//    }


    fun getUppers() : MutableLiveData<ArrayList<ExerciseData>> {
//        return upperLiveData.value = exerciseRepository.getExerciseBody()
        return exerciseRepository.getExerciseBody().also { upperLiveData }
//        return repository.getUserDTO().also { users = it }
    }

    fun getLowers() {
        return upperLiveData.postValue(exerciseRepository.getExerciseLower())
    }

    fun getBodies() {
        return upperLiveData.postValue(exerciseRepository.getExerciseBody())
    }

    fun getLoins() {
        return upperLiveData.postValue(exerciseRepository.getExerciseLoins())
    }



}

