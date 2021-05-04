package com.study.hometrainingkotlin.model.localrepository.room.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

import androidx.sqlite.db.SupportSQLiteQuery
import com.study.hometrainingkotlin.model.localrepository.room.vo.ExerciseMyselfEntity
import com.study.hometrainingkotlin.model.localrepository.room.vo.ExerciseSumCalEntity
import kotlinx.coroutines.flow.Flow

//쿼리문 명세 클래스
//실제로 db에 접근하는 용도
@Dao
interface ExerciseDAO {
    //운동선택에서 추가버튼 누를때 사용
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
    @Query("SELECT sum(cal) FROM exerciseresult")
    fun exerciseListCalSum():LiveData<Int>

    //운동목록의 다이얼로그 버튼에서 추가버튼 클릭시 사용
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertExerciseMyselfList(exerciseMyselfEntity: List<ExerciseMyselfEntity>)

    //exerciseSumCal테이블을 조회할 때 사용
    @Query("SELECT * FROM exercisesumcal")
    fun exerciseSumCalSelect():LiveData<List<ExerciseSumCalEntity>>

    //나자신과의 싸움에서 BarChart 클릭시 사용
    @Query("SELECT * FROM exercisemyself")
    fun exerciseMyselfDetailSelect():LiveData<List<ExerciseMyselfEntity>>

    //운동목록에서 나자신과의 싸움 추가버튼 클릭시 사용
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExerciseSumCal(vararg exerciseSumCalEntity: ExerciseSumCalEntity)
}