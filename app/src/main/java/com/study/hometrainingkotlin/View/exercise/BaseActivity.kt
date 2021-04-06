package com.study.hometrainingkotlin.View.exercise

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.study.hometrainingkotlin.Model.vo.ExerciseData
import com.study.hometrainingkotlin.R
import com.study.hometrainingkotlin.View.exercise.adapter.ExerciseAdapter
import com.study.hometrainingkotlin.viewmodel.ExerciseViewModel

abstract class BaseActivity<T: ViewDataBinding, V:ViewModel>(private val viewModelClass: Class<V>): AppCompatActivity() {
    protected lateinit var binding:T
    protected abstract var viewModel:V
    protected abstract val layoutResId : Int



    var adapter : ExerciseAdapter  ?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //초기화된 layoutResId로 databinding객체 생성
        binding = DataBindingUtil.setContentView(this,layoutResId)
        //liveData를 사용하기 위해 사용
        binding.lifecycleOwner = this@BaseActivity
//        viewModel = ViewModelProvider(this).get(viewModelClass)
        viewModel = ExerciseViewModel by viewModels()
        viewModel.
    }

    fun setUpRecyclerView(){
        if (adapter == null){
            adapter = ExerciseAdapter(exerciseDataList!!,object : ExerciseAdapter.OnItemClickListener{
                override fun onItemClick(position: Int) {
                    Toast.makeText(this@BaseActivity,position,Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}