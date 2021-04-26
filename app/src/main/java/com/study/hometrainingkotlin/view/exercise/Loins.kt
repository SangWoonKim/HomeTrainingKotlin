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
    override val viewModel: ExerciseViewModel by viewModels()           //viewModel의존성 주입
    override var listView: RecyclerView ?=null
    //클래스 전역변수
    private var loinsList: ArrayList<ExerciseData> ?= null              //listView에 나타낼 데이터 저장



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loins_activity)
        listView= findViewById(R.id.List_Loins)

        //observer등록
        viewModel.getLoins()
                .observe(this,{     //해당 액티비티의 생명주기 등록
                    changeData->           //데이터 변화시 changeData에 데이터가 들어옴 이때 MutableLiveData형식이 아닌 ArrayList형식으로 반환(<>안에 명시한 형식대로 들어옴)
                    loinsList = changeData //해당 데이터를 삽입
                    setAdapterAndEvent(listView,loinsList,this@Loins,application)       //인터페이스 메소드 어뎁터연결 및 아이템 클릭시 alertDialog이벤트가 되어있음
        })
    }

}