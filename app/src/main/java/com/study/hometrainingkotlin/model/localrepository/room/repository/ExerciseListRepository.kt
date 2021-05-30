package com.study.hometrainingkotlin.model.localrepository.room.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.study.hometrainingkotlin.model.localrepository.room.AppDatabase
import com.study.hometrainingkotlin.model.localrepository.room.dao.ExerciseDAO
import com.study.hometrainingkotlin.model.localrepository.room.dao.ExerciseListEntity

class ExerciseListRepository(application: Application) {

    //room의 인스턴스 얻어와 저장
    private val dbInstance = AppDatabase.getInstance(application)

    //인스턴스를 이용하여 늦은 초기화 실행
    // lazy가 안먹음 공부요망
    private val exerciseDAO: ExerciseDAO =
        dbInstance!!.exerciseDAO()



    //운동목록 table 조회(Read, select)
    fun selectList(): LiveData<List<ExerciseListEntity>> {
        return exerciseDAO.exerciseListSelect()
    }

    //운동목록이 참조하는 table에 데이터 삽입(Create,insert)
    fun listInsert(exerciseListEntity: ExerciseListEntity) {
        //백그라운드 스레드에서 삽입 수행
        AppDatabase.writeExecutor.execute(Runnable {
            exerciseDAO.insertExerciseList(exerciseListEntity)
        })
    }

    //운동목록이 참조하는 table의 데이터 삭제(Delete,delete)
    fun deleteListItem(exerciseListEntity: ExerciseListEntity) {
        AppDatabase.writeExecutor.execute(Runnable {
            exerciseDAO.exerciseListDelete(exerciseListEntity)
        })
    }

    //운동목록이 참조하는 table의 데이터 전체 삭제(Delete,delete)
    fun deleteAllListItem() {
        AppDatabase.writeExecutor.execute(Runnable {
            exerciseDAO.exerciseAllListDelete()
        })
    }
}