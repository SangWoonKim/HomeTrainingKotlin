package com.study.hometrainingkotlin.view.ExerciseList

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.viewModels
import com.study.hometrainingkotlin.R
import com.study.hometrainingkotlin.viewmodel.ExerciseViewModel

class Active() : AppCompatActivity(),SpriteInterface, View.OnClickListener {
    override var spriteImageItem: ArrayList<String> ?= ArrayList()
    override var spriteImageItem2: ArrayList<String> ?= ArrayList()
    override var spriteHandler: Handler ?= createHandler()
    override var imageView: ImageView ?= null
    override var activity: Activity = this

    private var BTN_Active_Start:Button ?= null
    private var BTN_Active_Exit:Button ?= null
    private val viewModel:ExerciseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.active_activity)
        imageView = findViewById(R.id.IMV_Active)
        BTN_Active_Exit = findViewById(R.id.BTN_Active_Exit)
        BTN_Active_Start = findViewById(R.id.BTN_Active_Start)

        BTN_Active_Start!!.setOnClickListener(this)
        BTN_Active_Exit!!.setOnClickListener(this)
        viewModel.selectListItem().observe(this, {
            //혹시 모를 배열 초기화
            spriteImageItem?.clear()
            spriteImageItem2?.clear()
            //배열의 크기 확인
            if (it!!.size != 0) {
                //배열 삽입
                for (iterate in 0..it.size-1) {
                    spriteImageItem?.add(it.get(iterate).activeImage!!)
                    spriteImageItem2?.add(it.get(iterate).activeImage2!!)
                }
            }
            settingList()
        }
        )


    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.BTN_Active_Start ->{
                threadStateChange()
                BTN_Active_Start!!.text = "운동시작"
                threadStart()
            }

            R.id.BTN_Active_Exit ->{
                BTN_Active_Exit!!.text = "쉬는시간"
                finish()
            }
        }
    }

    override fun onDestroy() {
        closeInit()
        super.onDestroy()
    }
}