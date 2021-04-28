package com.study.hometrainingkotlin.view.SelectFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.study.hometrainingkotlin.R
import com.study.hometrainingkotlin.view.exercise.Body
import com.study.hometrainingkotlin.view.exercise.Loins
import com.study.hometrainingkotlin.view.exercise.Lower
import com.study.hometrainingkotlin.view.exercise.Upper

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
        when(v!!.id){
            R.id.IMG_BTN_Upper -> { val upper = Intent(activity,Upper::class.java)
                startActivity(upper)}

            R.id.IMG_BTN_Loins -> { val loins = Intent(activity,Loins::class.java)
                startActivity(loins)}

            R.id.IMG_BTN_Lower -> { val lower =Intent(activity,Lower::class.java)
                startActivity(lower)}

            R.id.IMG_BTN_Body -> { val body =Intent(activity,Body::class.java)
                startActivity(body)}
        }
    }
}