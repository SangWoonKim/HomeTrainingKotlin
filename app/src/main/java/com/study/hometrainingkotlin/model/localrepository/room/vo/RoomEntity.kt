package com.study.hometrainingkotlin.model.localrepository.room.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

//SQLite DB에서 사용
//DB의 Table들을 정의한 파일
//exerciseresult테이블 정의
//조회 및 삽입시 사용
@Entity(tableName = "exerciseresult")

data class ExerciseListEntity(val part: String,         //부위
                              val name:String,          //이름
                              val cal:Int,           //칼로리
                              val image:String,         //리스트뷰 이미지
                              val imageOrg:String,      //dialog이미지
                              val activeImage:String,   //스프라이트 이미지 1
                              val activeImage2:String,   //스프라이트 이미지 2
                              //자동증가 기능 autoGenerate = true
                              @PrimaryKey(autoGenerate = true) val id:Int? = null
                              )