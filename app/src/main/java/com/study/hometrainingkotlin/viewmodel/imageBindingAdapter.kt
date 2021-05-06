package com.study.hometrainingkotlin.viewmodel

import android.widget.ImageView
import androidx.databinding.BindingAdapter

object imageBindingAdapter {

    @BindingAdapter("imageResource")
    @JvmStatic
    fun ImageView.setImageResource(resource:String){
        var resid = context.resources.getIdentifier(resource,"drawable",context.packageName)
        this.setImageResource(resid)
    }
}