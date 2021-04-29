package com.study.hometrainingkotlin.model.localrepository.room.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.study.hometrainingkotlin.model.externalrepository.vo.ExerciseData
import com.study.hometrainingkotlin.model.localrepository.room.AppDatabase
import com.study.hometrainingkotlin.model.localrepository.room.dao.ExerciseDAO
import com.study.hometrainingkotlin.model.localrepository.room.dao.ExerciseListEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ExerciseListRepository(application: Application) {

    //room의 인스턴스 얻어와 저장
    private val dbInstance = AppDatabase.getInstance(application)
    //인스턴스를 이용하여 늦은 초기화 실행
    // lazy가 안먹음 공부요망
    private val exerciseDAO : ExerciseDAO =
            dbInstance!!.exerciseDAO()

//    private var sumCal:Int =0

    //운동목록 table 조회(Read, select)
    fun selectList(): LiveData<List<ExerciseListEntity>> {
        return exerciseDAO.exerciseListSelect()
    }

    //운동목록이 참조하는 table에 데이터 삽입(Create,insert)
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

    //운동목록이 참조하는 table의 데이터 삭제(Delete,delete)
    fun deleteListItem(exerciseListEntity: ExerciseListEntity){
        AppDatabase.writeExecutor.execute(Runnable {
            exerciseDAO.exerciseListDelete(exerciseListEntity)
        })
    }

    //운동목록이 참조하는 table의 데이터 전체 삭제(Delete,delete)
    fun deleteAllListItem(){
        AppDatabase.writeExecutor.execute(Runnable {
            exerciseDAO.exerciseAllListDelete()
        })
    }

    //운동목록에 있는 아이템들의 칼로리를 조회하여 총값 계산
    fun sumCalListItem():LiveData<Int>{
//        runBlocking {         //{}안의 로직이 수행되기 전까지 빠져나가지 못함
//            GlobalScope.launch {                  //IO쓰레드에서 실행
//                sumCal = exerciseDAO.exerciseListCalSum()
//            }
//        }
           return exerciseDAO.exerciseListCalSum()
    }


}