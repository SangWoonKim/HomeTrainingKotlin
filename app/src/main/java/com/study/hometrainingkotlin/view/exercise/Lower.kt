package com.study.hometrainingkotlin.view.exercise

import android.os.Bundle
import androidx.activity.viewModels
import com.study.hometrainingkotlin.viewmodel.ExerciseViewModel

class Lower() : AbstractBasicActivity() {

//    private val exerciseViewModel: ExerciseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.viewModel!!.getLowers()?.observe(this,
            {
                changeData->
                super.listItem = changeData
                setAdapterAndEvent(super.listView,super.listItem,this@Lower,application)
            })
    }
}