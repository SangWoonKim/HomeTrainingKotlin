package com.study.hometrainingkotlin.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.study.hometrainingkotlin.BR
import com.study.hometrainingkotlin.R
import com.study.hometrainingkotlin.databinding.ExerciseItemBinding
import com.study.hometrainingkotlin.databinding.ListviewMyselfBinding
import com.study.hometrainingkotlin.model.localrepository.room.vo.ExerciseMyselfEntity

class ExerciseMyselfAdapter(exerciseMyselfArray: ArrayList<ExerciseMyselfEntity>) :
    RecyclerView.Adapter<ExerciseMyselfAdapter.ExerciseMyselfViewHolder>() {

    private var exerciseMyselfList : ArrayList<ExerciseMyselfEntity> ?= exerciseMyselfArray

    //뷰홀더 클래스
    inner class ExerciseMyselfViewHolder(itemBinding:ListviewMyselfBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        private var binding : ListviewMyselfBinding = itemBinding

        fun bind(exerciseMyselfItem : ExerciseMyselfEntity){
            binding.setVariable(BR.exercisemyselfitem,exerciseMyselfItem)
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExerciseMyselfAdapter.ExerciseMyselfViewHolder {
        return ExerciseMyselfViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.listview_myself,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ExerciseMyselfAdapter.ExerciseMyselfViewHolder,
        position: Int
    ) {
        var exerciseMyselfItem : ExerciseMyselfEntity = exerciseMyselfList!!.get(position)
        holder.bind(exerciseMyselfItem)
    }

    override fun getItemCount(): Int {
        return exerciseMyselfList!!.size
    }
}