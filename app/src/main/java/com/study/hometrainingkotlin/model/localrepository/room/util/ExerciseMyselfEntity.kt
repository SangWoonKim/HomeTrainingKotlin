package com.study.hometrainingkotlin.model.localrepository.room.util

import androidx.room.Entity
import androidx.room.PrimaryKey

//SQLite DB에서 사용
//DB의 Table들을 정의한 파일
//exercisemyself테이블 정의
//조회 및 삽입시 사용
@Entity(tableName = "exercisemyself")
data class ExerciseMyselfEntity(val My_date:String?,
                                val My_part:String?,
                                val My_name:String?,
                                val My_cal:Int?,
                                @PrimaryKey(autoGenerate = true) val My_id:Int?=null
)