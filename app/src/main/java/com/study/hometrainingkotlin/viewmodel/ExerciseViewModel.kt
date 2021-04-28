package com.study.hometrainingkotlin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
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

    //운동선택 상체 부위 배열데이터를 반환
    fun getUppers() : MutableLiveData<ArrayList<ExerciseData>> {
        return exerciseRepository.getExerciseUpper()
    }

    fun getLowers(): MutableLiveData<ArrayList<ExerciseData>>? {
        return exerciseRepository.getExerciseLower()
    }

    fun getBodies(): MutableLiveData<ArrayList<ExerciseData>>? {
        return exerciseRepository.getExerciseBody()
    }

    fun getLoins() : MutableLiveData<ArrayList<ExerciseData>>? {
        return exerciseRepository.getExerciseLoins()
    }

    //exerciseReposititory의 LiveData를 공용으로 사용하게 됨으로 써
    //아이템이 재사용됨 즉 서버에서 값을 받기 전까지 같은 아이템이 출력됨으로
    //객체를 reset시켜야해서 사용됨
//    fun exerciseResetGetData(){
//        return exerciseRepository.dataReset()
//    }

    //운동목록 조회 메소드
    fun selectListItem(): LiveData<List<ExerciseListEntity>> {
        return exerciseListRepository.selectList()
    }

    // 운동목록 item 삽입메소드
    fun insertListItem(exerciseListEntity:ExerciseListEntity){
        return exerciseListRepository.listInsert(exerciseListEntity)
    }

    // 운동목록 item 삭제메소드
    fun deleteListItem(exerciseListEntity: ExerciseListEntity){
        return exerciseListRepository.deleteListItem(exerciseListEntity)
    }


}

