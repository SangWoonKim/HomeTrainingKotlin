package com.study.hometrainingkotlin.view.SelectFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.study.hometrainingkotlin.R
import com.study.hometrainingkotlin.view.Setting.Theme
import kotlinx.android.synthetic.main.fragment_settings.*

class Settings : Fragment(),View.OnClickListener {

    private var BTN_Setting_Theme:Button?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BTN_Setting_Theme = view.findViewById(R.id.BTN_settings_setting)
        BTN_Setting_Theme!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.BTN_settings_setting -> {
                var fragmentManager: FragmentManager? = activity?.supportFragmentManager
                var fragmentTransaction: FragmentTransaction? = fragmentManager?.beginTransaction()
                fragmentTransaction?.replace(R.id.Main_Frame, Theme())
                fragmentTransaction?.addToBackStack(null)?.commit()
            }
        }
    }
}