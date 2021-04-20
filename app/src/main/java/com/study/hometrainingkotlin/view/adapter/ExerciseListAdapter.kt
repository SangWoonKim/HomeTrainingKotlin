package com.study.hometrainingkotlin.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.study.hometrainingkotlin.BR
import com.study.hometrainingkotlin.R
import com.study.hometrainingkotlin.databinding.ExerciselistItemBinding
import com.study.hometrainingkotlin.model.localrepository.room.dao.ExerciseListEntity

class ExerciseListAdapter(exerciseListArray:ArrayList<ExerciseListEntity>): RecyclerView.Adapter<ExerciseListAdapter.ExerciseListViewHolder>(),ItemTouchHelperListener {

    var exerciseListList:ArrayList<ExerciseListEntity>? = exerciseListArray


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseListViewHolder {
        return ExerciseListViewHolder(
               DataBindingUtil.inflate(
               LayoutInflater.from(parent.context),
               R.layout.exerciselist_item,
               parent,
               false
               )
        )
    }

    override fun onBindViewHolder(holder: ExerciseListViewHolder, position: Int) {
        var exerciseListItem : ExerciseListEntity = exerciseListList!!.get(position)
        holder.bind(exerciseListItem)
    }

    override fun getItemCount(): Int {
        return exerciseListList!!.size
    }

    inner class ExerciseListViewHolder(itemBinding:ExerciselistItemBinding) :RecyclerView.ViewHolder(itemBinding.root){
        private var binding : ExerciselistItemBinding = itemBinding

        fun bind(exerciseListitem: ExerciseListEntity){
            //이미지로 이름으로 셋팅해야함 layout에서
            binding.setVariable(BR.exerciseListItem,exerciseListitem)
        }
    }

    override fun onItemSwipe(position: Int) {
       //roomdb에서 데이터 삭제 메소드 구현
        exerciseListList!!.removeAt(position)
        notifyDataSetChanged()
    }
}