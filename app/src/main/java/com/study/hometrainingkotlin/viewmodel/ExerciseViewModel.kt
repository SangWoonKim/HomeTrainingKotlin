package com.study.hometrainingkotlin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.study.hometrainingkotlin.model.externalrepository.repository.ExerciseRepository
import com.study.hometrainingkotlin.model.externalrepository.vo.ExerciseData
import com.study.hometrainingkotlin.model.localrepository.room.AppDatabase
import com.study.hometrainingkotlin.model.localrepository.room.dao.ExerciseDAO
import com.study.hometrainingkotlin.model.localrepository.room.dao.ExerciseListEntity
import com.study.hometrainingkotlin.model.localrepository.room.repository.ExerciseListRepository

//뷰모델 (굳이 쓸 필요가 없으나 이번기회에 사용해봄)
//데이터를 조회하는 것 밖에 안하기 때문에 서버에서 값이 달라지지 않는 한 바뀌지 않음
//만약 서버에서 값이 달라지는 것을 알려면 사용하려는 메소드를 재호출해야함(ex)Thread사용)
class ExerciseViewModel(application: Application): AndroidViewModel(application) {
    //ExerciseRepository에 접근할 수 있는 인스턴스를 저장
    private val exerciseRepository: ExerciseRepository = ExerciseRepository.getInstance()!!

    //
    private val exerciseListRepository: ExerciseListRepository by lazy {
        ExerciseListRepository(application)
    }

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

    // 운동목록 item 삽입메소드
    fun insertListItem(exerciseListEntity:ExerciseListEntity){
        return exerciseListRepository.listInsert(exerciseListEntity)
    }


}

