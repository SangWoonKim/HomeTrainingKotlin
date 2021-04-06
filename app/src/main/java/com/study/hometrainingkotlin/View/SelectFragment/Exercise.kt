package com.study.hometrainingkotlin.View.SelectFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.study.hometrainingkotlin.R

class Exercise: Fragment(),View.OnClickListener {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return  inflater.inflate(R.layout.fragment_exercise, container, false
        )
    }

    //onCreateView후 호출 실질적인 뷰의 초기화 시작
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val IMG_BTN_Upper = view.findViewById<View>(R.id.IMG_BTN_Upper) as ImageButton
        val IMG_BTN_Lower = view.findViewById<View>(R.id.IMG_BTN_Lower) as ImageButton
        val IMG_BTN_Body = view.findViewById<View>(R.id.IMG_BTN_Body) as ImageButton
        val IMG_BTN_Loins = view.findViewById<View>(R.id.IMG_BTN_Loins) as ImageButton
        IMG_BTN_Upper.setOnClickListener(this)
        IMG_BTN_Lower.setOnClickListener(this)
        IMG_BTN_Body.setOnClickListener(this)
        IMG_BTN_Loins.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}