package com.study.hometrainingkotlin.view.ExerciseList

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.study.hometrainingkotlin.R
import java.util.*
import kotlin.collections.ArrayList

interface SpriteInterface {
    //자바에서 인터페이스 상수를 사용시 상속되는 각 클래스는 공용 상수가 되어 다른 곳에서도 참조및 재정의가 가능의 문제점 생성
    //https://thdev.tech/kotlin/2020/11/10/kotlin_effective_10/
    //코틀린에서는 디 컴파일시 companion object는 Companion이라는 새로운 static class를 생성하기에 단일 상수로 취급이 됨
    companion object {
        //쓰레드 생성
        var spriteThread:Thread?=null

        var checkStatus:Boolean = true
        var record = -1
        var index = 0
        var spriteIndex =0

        var spriteList:ArrayList<List<String>> = ArrayList()

    }
    var spriteImageItem:ArrayList<String>?
    var spriteImageItem2:ArrayList<String>?
    //핸들러
    var spriteHandler : Handler?
    //나타낼 뷰
    var imageView:ImageView?
    //dialog를 생성할 context
    var activity:Activity



    //핸들러 생성및 제어메소드
    fun createHandler():Handler{
        spriteHandler = object :Handler(Looper.getMainLooper()){
            override fun handleMessage(msg: Message) {
                if (msg.what ==1){
                    rest()
                } else if (msg.what == 2) {
                    //활동이 완료되었을시 실행하는 함수
                    activeComplete()
                } else {
                    //계속 변하는 이미지(1번째 2번째)를 번갈아 띄우기 위한 함수
                    updateThread()
                }
            }
        }
        return spriteHandler as Handler
    }

    //sprite animation에 사용될 2차원 배열 세팅메소드
    fun settingList(){
        if (spriteImageItem!!.size !=0) {
            for (index in spriteIndex..spriteImageItem!!.size-1) {
                //위에서 할당한 배열로된 데이터를 2차원 배열 삽입
                spriteList.add(
                    Arrays.asList(
                        spriteImageItem!!.get(index),
                        spriteImageItem2!!.get(index)
                    )
                )
            }
        }

    }

    //Thread생성 및 실행과 메세지 제어 메소드
    fun threadStart(){
        spriteThread = Thread(){
            if (checkStatus == true){
                while (checkStatus){
                    try {
                        spriteHandler!!.sendMessage(spriteHandler!!.obtainMessage())
                        Thread.sleep(500)
                    }catch (t:Throwable){
                        t.printStackTrace()
                    }
                }
            }else{

                if(record == spriteList.size-1){
                    var finishMessage:Message = spriteHandler!!.obtainMessage()
                    finishMessage.what = 2
                    spriteHandler!!.sendMessage(finishMessage)
                }else{
                    var nextImageMessage:Message = spriteHandler!!.obtainMessage()
                    nextImageMessage.what = 1
                    record++
                    spriteHandler!!.sendMessage(nextImageMessage)
                }

            }
        }
        spriteThread!!.start()
    }

    //Thread상태를 변화시키는 메소드
    fun threadStateChange():Boolean{
        if (checkStatus == true){
            checkStatus=false
        }else {
            checkStatus = true
        }
        return checkStatus
    }

    //쓰레드 초기화 메소드
    fun closeInit(){
        checkStatus = false;
        if (spriteThread == null){
            record =0
        }else{
            spriteThread!!.interrupt()
        }
        record=0
        spriteHandler!!.sendEmptyMessage(0)
        spriteHandler!!.removeMessages(0)
    }

    //쉬는시간 메소드
    private fun rest(){
        imageView!!.setImageResource(R.drawable.rest)
    }

    //운동완료시 호출하는 메소드
    private fun activeComplete(){
        var dialog:AlertDialog.Builder = AlertDialog.Builder(activity!!)
        dialog.setMessage("운동이 끝났습니다")
            .setPositiveButton("확인"){
                    d,w ->
                activity!!.onBackPressed()
            }.create().show()
    }

    //Thread가 읽는 배열의 한 행 데이터를 나누기 연산을 이용하여 반복시키는 메소드
    private fun updateThread(){
        var iteraionColumn:Int = index % 2

        var DBPath = spriteList.get(record).get(iteraionColumn)
        var type ="drawable"
        var packageName:String= activity!!.packageName

        var resId = activity!!.resources.getIdentifier(DBPath,type,packageName)

       when(iteraionColumn){

           0 -> {
               index++
               imageView!!.setImageResource(resId)
           }

           1 -> {
               index++
               imageView!!.setImageResource(resId)
           }

       }
    }



}