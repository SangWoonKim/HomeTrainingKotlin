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
import com.study.hometrainingkotlin.viewmodel.ExerciseViewModel

class ExerciseListAdapter(exerciseListArray:ArrayList<ExerciseListEntity>):
    RecyclerView.Adapter<ExerciseListAdapter.ExerciseListViewHolder>(),
    ItemTouchHelperListener     //아이템 스와이프시 필요한 인터페이스
                                //사용하려고 했으나 뷰모델을 사용하기에는 뷰모델의 인자를 가져다와서 쓰는 것은 더 긴 코드를 작성할 것 같아 사용하지 않음
{

    var exerciseListList:ArrayList<ExerciseListEntity>? = exerciseListArray

    //직접 데이터를 갱신하는 메소드
    fun updateData(exerciseListChangeData:ArrayList<ExerciseListEntity>){
        exerciseListList= exerciseListChangeData
    }

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

    //사용되지 않음
    override fun onItemSwipe(position: Int) {
       //roomdb에서 데이터 삭제 메소드 구현
        exerciseListList!!.removeAt(position)
        notifyDataSetChanged()
    }
}