package com.study.hometrainingkotlin.view.exercise

import android.os.Bundle

class Upper: AbstractBasicActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.viewModel!!.getUppers()?.observe(this,
            {
                    changeData->
                super.listItem = changeData
                setAdapterAndEvent(super.listView,super.listItem,this@Upper,application)
            })
    }

}