package com.study.hometrainingkotlin.View.exercise

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.study.hometrainingkotlin.Model.vo.ExerciseData
import com.study.hometrainingkotlin.R
import com.study.hometrainingkotlin.databinding.BodyActivityBinding
import com.study.hometrainingkotlin.viewmodel.ExerciseViewModel

class Body() : BaseActivity<BodyActivityBinding,ExerciseViewModel>(ExerciseViewModel::class.java) {
    override val layoutResId: Int = R.layout.body_activity
    override var viewModel: ExerciseViewModel = ExerciseViewModel()

    var item: ExerciseViewModel = ViewModelProvider(this).get(ExerciseViewModel::class.java)



}