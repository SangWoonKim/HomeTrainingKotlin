package com.study.hometrainingkotlin.model.localrepository.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.study.hometrainingkotlin.model.localrepository.room.dao.ExerciseDAO
import com.study.hometrainingkotlin.model.localrepository.room.dao.ExerciseListEntity
import com.study.hometrainingkotlin.model.localrepository.room.util.ExerciseMyselfEntity
import com.study.hometrainingkotlin.model.localrepository.room.util.ExerciseSumCalEntity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


//https://developer.android.com/training/data-storage/room/prepopulate?hl=ko#from-file
//데이터베이스 정의 클래스
//외부(assets)에서 db파일 복사시 version =2로 사용
@Database(entities = [ExerciseListEntity::class, ExerciseMyselfEntity::class, ExerciseSumCalEntity::class] ,version = 2,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    //쿼리가 담긴 클래스
    abstract fun exerciseDAO() : ExerciseDAO

    //singleton(하나의 데이터베이스를 여러인스턴스가 동시에 접근하는 것을 방지하기 위함)
    companion object{
        //https://developer.android.com/codelabs/android-room-with-a-view#7
        // 쓰기를 위한 Executor 백그라운드 작업에 필요한 쓰레드객체
        val writeExecutor : ExecutorService = Executors.newFixedThreadPool(4)
        private var INSTANCE: AppDatabase? = null

        //db에 접근할 객체
        fun getInstance(context: Context):AppDatabase?{
             return INSTANCE ?: synchronized(AppDatabase::class){
                 //room을 이용하여 이 클래스에 명시된 정보를 갖고 객체를 생성
                INSTANCE?: Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java,
                    "HomeTrainingKotlin.db")
                    //.fallbackToDestructiveMigration() // 기존 데이터를 버리고 다음 버전으로 넘어감
                    .addMigrations(MIGRATION)
                    //.allowMainThreadQueries() //메인스레드에서도 쿼리를 처리하는 것을 허용 하지만 권장되지는 않음
                    .build()
                    .also { INSTANCE = it }
             }
        }

        @JvmField
        val MIGRATION:Migration = object : Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                //처음 마이그레이션할 때 즉 칼럼이나 테이블이 변경이 안되었을 경우 빈칸으로 놓음

//                database.execSQL("CREATE TABLE exercisemyself (My_date TEXT, My_part TEXT, My_name TEXT, My_cal INTEGER, PRIMARY KEY(My_date))")
            }

        }
    }
}