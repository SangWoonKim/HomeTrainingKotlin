package com.study.hometrainingkotlin.model.localrepository.room.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.study.hometrainingkotlin.model.localrepository.room.AppDatabase
import com.study.hometrainingkotlin.model.localrepository.room.dao.ExerciseDAO
import com.study.hometrainingkotlin.model.localrepository.room.vo.ExerciseMyselfEntity
import com.study.hometrainingkotlin.model.localrepository.room.vo.ExerciseSumCalEntity

class ExerciseMyselfRepository(application: Application) {
    //room의 인스턴스 얻어와 저장
    private val dbInstance = AppDatabase.getInstance(application)

    //인스턴스를 이용하여 늦은 초기화 실행
    // lazy가 안먹음 공부요망
    private val exerciseDAO: ExerciseDAO =
        dbInstance!!.exerciseDAO()

    //exercisemyself테이블에 아이템 삽입 메소드
    fun listInsert(exerciseMyselfEntity: ArrayList<ExerciseMyselfEntity>){
        AppDatabase.writeExecutor.execute{
            exerciseDAO.insertExerciseMyselfList(exerciseMyselfEntity)
        }
    }

    fun selectMyselfDetail(): LiveData<List<ExerciseMyselfEntity>> {
        return exerciseDAO.exerciseMyselfDetailSelect()
    }

}