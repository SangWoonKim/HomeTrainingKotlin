package com.study.hometrainingkotlin.view.exercise

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.study.hometrainingkotlin.R
import com.study.hometrainingkotlin.model.vo.ExerciseData
import com.study.hometrainingkotlin.view.exercise.adapter.ExerciseAdapter
import com.study.hometrainingkotlin.viewmodel.ExerciseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class Body : AppCompatActivity() {

    private var recyclerView:RecyclerView ?= null
    private var exerciseAdapter:ExerciseAdapter ?= null
    private var upperList : ArrayList<ExerciseData> ?= null
    //뷰모델 DI 의존성 주입
    private val exerciseViewModel : ExerciseViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.body_activity)
        recyclerView=findViewById(R.id.bodylist)

        exerciseViewModel.getUppers().observe(this,
            { changeData ->   //observing즉 데이터가 바뀌었을시 changeData에 저장됨 (람다식사용)
                upperList = changeData   //adapter를 생성하기 위한 배열요소 upperList에 바뀐데이터 갱신
                setAdapterAndEvent()
//                exerciseAdapter!!.updateData(changeData)
            })
    }

    //어뎁터 연결및 클릭 이벤트 정의 메소드
    private fun setAdapterAndEvent(){
        if (exerciseAdapter == null){
            exerciseAdapter = ExerciseAdapter(upperList!!,object:
                ExerciseAdapter.OnItemClickListener{
                override fun onItemClick(position: Int) {
                    Log.d("listPostition",""+position)
                    //DIALOG기능 삽입구간
                }
            })
            recyclerView!!.layoutManager = LinearLayoutManager(this)
            recyclerView!!.adapter = exerciseAdapter
        }else{
            exerciseAdapter!!.notifyDataSetChanged()
        }
    }
}