package com.study.hometrainingkotlin.model.localrepository.room.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.study.hometrainingkotlin.model.localrepository.room.AppDatabase
import com.study.hometrainingkotlin.model.localrepository.room.dao.ExerciseDAO
import com.study.hometrainingkotlin.model.localrepository.room.vo.ExerciseSumCalEntity

class ExerciseSumCalRepository(application: Application) {
    private val dbInstance = AppDatabase.getInstance(application)

    //인스턴스를 이용하여 늦은 초기화 실행
    // lazy가 안먹음 공부요망
    private val exerciseDAO: ExerciseDAO =
        dbInstance!!.exerciseDAO()

    //다른 스레드를 이용하여 삽입실행
    fun sumCalInsert(exerciseSumCalEntity: ExerciseSumCalEntity) {
        AppDatabase.writeExecutor.execute {
            exerciseDAO.insertExerciseSumCal(exerciseSumCalEntity)
        }
    }
    //운동목록에 있는 아이템들의 칼로리를 조회하여 총값 계산
    fun sumCalListItem(): LiveData<Int> {
        return exerciseDAO.exerciseListCalSum()
    }

    //나 자신과의 싸움에서 barChart에 표시할 때 사용되는 메소드
    fun selectSumCal(): LiveData<List<ExerciseSumCalEntity>> {
        return exerciseDAO.exerciseSumCalSelect()
    }
}