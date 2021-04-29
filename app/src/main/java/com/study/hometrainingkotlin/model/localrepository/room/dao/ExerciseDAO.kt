package com.study.hometrainingkotlin.model.localrepository.room.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow

//쿼리문 명세 클래스
//실제로 db에 접근하는 용도
@Dao
interface ExerciseDAO {
    //운동추가버튼 누를때 사용
    //onConflict insert시 옵션 설정 REPLACE == INSERT OR REPLACE INTO
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExerciseList(vararg exerciseListEntity: ExerciseListEntity)


    //운동목록 조회시 사용
    @Query("SELECT * FROM exerciseresult ")
    fun exerciseListSelect(): LiveData<List<ExerciseListEntity>>

    //운동목록에서 swipe시 아이템 삭제시 사용
    @Delete
    fun exerciseListDelete(vararg exerciseListEntity: ExerciseListEntity)

    //운동목록에서 참조하는 테이블 데이터 전체 삭제
    @Query("DELETE FROM exerciseresult")
    fun exerciseAllListDelete()

    //운동목록에 있는 아이템들의 칼로리값을 모두 더한 후 조회
//    @Query("SELECT sum(cal) from exerciseresult")
//    suspend fun exerciseListCalSum():Int

    @Query("SELECT sum(cal) from exerciseresult")
    fun exerciseListCalSum():LiveData<Int>
}