package com.study.hometrainingkotlin.model.localrepository.room.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.study.hometrainingkotlin.model.externalrepository.vo.ExerciseData
import com.study.hometrainingkotlin.model.localrepository.room.AppDatabase
import com.study.hometrainingkotlin.model.localrepository.room.dao.ExerciseDAO
import com.study.hometrainingkotlin.model.localrepository.room.dao.ExerciseListEntity

class ExerciseListRepository(application: Application) {

    //room의 인스턴스 얻어와 저장
    private val dbInstance = AppDatabase.getInstance(application)
    //인스턴스를 이용하여 늦은 초기화 실행
    // lazy가 안먹음 공부요망
    private val exerciseDAO : ExerciseDAO =
            dbInstance!!.exerciseDAO()


    //운동목록 table 조회
    fun selectList(): LiveData<List<ExerciseListEntity>> {
        return exerciseDAO.exerciseListSelect()
    }

    fun listInsert(exerciseListEntity : ExerciseListEntity){
        //람다식 사용하기 아직 안함
//        AppDatabase.writeExecutor.execute(() -> {
//            exerciseDAO.insertExerciseList(exerciseListEntity)
//        })
        //백그라운드 스레드에서 삽입 수행
        AppDatabase.writeExecutor.execute(Runnable {
            exerciseDAO.insertExerciseList(exerciseListEntity)
        })
    }
}