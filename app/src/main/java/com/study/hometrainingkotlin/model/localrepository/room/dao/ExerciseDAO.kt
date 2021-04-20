package com.study.hometrainingkotlin.model.localrepository.room.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import androidx.sqlite.db.SupportSQLiteQuery

//쿼리문 명세 클래스
//실제로 db에 접근하는 용도
@Dao
interface ExerciseDAO {
    //운동추가버튼 누를때 사용
    //onConflict insert시 옵션 설정 REPLACE == INSERT OR REPLACE INTO
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExerciseList(vararg exerciseListEntity: ExerciseListEntity)


//    //운동목록 조회시 사용
    @Query("SELECT * FROM exerciseresult ")
    fun exerciseListSelect(): LiveData<List<ExerciseListEntity>>
}