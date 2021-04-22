package com.study.hometrainingkotlin.view.exercise

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.study.hometrainingkotlin.R
import com.study.hometrainingkotlin.model.externalrepository.vo.ExerciseData
import com.study.hometrainingkotlin.view.adapter.ExerciseAdapter
import com.study.hometrainingkotlin.viewmodel.ExerciseViewModel

class Loins: AppCompatActivity(),BasicActivity {

    //interface 전역변수
    override var adapter: ExerciseAdapter ?= null
    //viewModel의존성 주입
    override val viewModel: ExerciseViewModel by viewModels()
    override var listView: RecyclerView ?=null

    //Loins전역변수
    private var exerciseAdapter : ExerciseAdapter ?= null
    private var list_Loins: RecyclerView ? =null
    private var loinsList: ArrayList<ExerciseData> ?= null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loins_activity)
//        list_Loins = findViewById(R.id.List_Loins)
        listView= findViewById(R.id.List_Loins)


        viewModel.getLoins().observe(this,{
            changeData->
            loinsList = changeData
            setAdapterAndEvent(listView,loinsList,this@Loins,application)
        })
    }

}