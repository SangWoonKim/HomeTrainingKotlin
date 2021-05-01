package com.study.hometrainingkotlin.model.localrepository.room.vo

import androidx.room.Entity
import androidx.room.PrimaryKey

//SQLite DB에서 사용
//DB의 Table들을 정의한 파일
//exercisemyself테이블 정의
//조회 및 삽입시 사용
@Entity(tableName = "exercisesumcal")
class ExerciseSumCalEntity (val Sc_cal:Int?,
                            val Sc_date:String?,
                            @PrimaryKey(autoGenerate = true )val Sc_id:Int? = null

)