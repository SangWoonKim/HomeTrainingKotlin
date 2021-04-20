package com.study.hometrainingkotlin.view.SelectFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.study.hometrainingkotlin.R
import com.study.hometrainingkotlin.model.localrepository.room.dao.ExerciseListEntity
import com.study.hometrainingkotlin.view.adapter.ExerciseListAdapter
import com.study.hometrainingkotlin.viewmodel.ExerciseViewModel

class List : Fragment(),View.OnClickListener {
    private var list_RecyclerView : RecyclerView ?= null
    private var BTN_List_fight: Button ?= null
    private var BTN_List_accept: Button ?= null
    private var BTN_List_active: Button ?= null

    private var exerciseListArray : ArrayList<ExerciseListEntity>?=null
    private val exerciseViewModel : ExerciseViewModel by viewModels()
    private var exerciseListAdapter: ExerciseListAdapter ?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list_RecyclerView = view.findViewById<RecyclerView>(R.id.List_Exerciselist)
        BTN_List_fight = view.findViewById<Button>(R.id.BTN_List_fight)
        BTN_List_accept = view.findViewById<Button>(R.id.BTN_List_accept)
        BTN_List_active = view.findViewById<Button>(R.id.BTN_List_active)

        BTN_List_fight!!.setOnClickListener(this)
        BTN_List_accept!!.setOnClickListener(this)
        BTN_List_active!!.setOnClickListener(this)

        exerciseViewModel.selectListItem().observe(viewLifecycleOwner,{
            changeData ->
            exerciseListArray = changeData as ArrayList<ExerciseListEntity>
            setAdapter()
        })
    }

    private fun setAdapter(){
        if(exerciseListAdapter == null){
            exerciseListAdapter = ExerciseListAdapter(exerciseListArray as ArrayList<ExerciseListEntity>)
            list_RecyclerView!!.layoutManager = LinearLayoutManager(context)
            list_RecyclerView!!.adapter = exerciseListAdapter
        }else{
            exerciseListAdapter!!.notifyDataSetChanged()
        }
    }


    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}