package com.study.hometrainingkotlin.model.localrepository.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.study.hometrainingkotlin.model.localrepository.room.dao.ExerciseDAO
import com.study.hometrainingkotlin.model.localrepository.room.dao.ExerciseListEntity


//https://developer.android.com/training/data-storage/room/prepopulate?hl=ko#from-file
//데이터베이스 정의 클래스
//외부(assets)에서 db파일 복사시 version =2로 사용
@Database(entities = [ExerciseListEntity::class],version = 2,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    //쿼리가 담긴 클래스
    abstract fun exerciseDAO() : ExerciseDAO

    //singleton
    companion object{
        @JvmField
        val MIGRATION:Migration = object : Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                TODO("Not yet implemented")
            }

        }
    }
}