package com.study.hometrainingkotlin.view.exercise.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.study.hometrainingkotlin.BR
import com.study.hometrainingkotlin.R
import com.study.hometrainingkotlin.databinding.ExerciseItemBinding
import com.study.hometrainingkotlin.model.vo.ExerciseData


class ExerciseAdapter(exerciseArray: ArrayList<ExerciseData>, listener: OnItemClickListener): RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    var exerciseList : ArrayList<ExerciseData>?=exerciseArray

    var itemClickListener: OnItemClickListener? = null

    init {
        itemClickListener = listener
    }

    //데이터 갱신시 호출되는 메소드 즉 배열의 값이 외부에서 달라졌을시 어뎁터에 적용하기 위한 메소드
    fun updateData(exercise: ArrayList<ExerciseData>){
        this.exerciseList = exercise
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        return ExerciseViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.exercise_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        var exercise : ExerciseData = exerciseList!!.get(position)
        holder.bind(exercise, itemClickListener!!)
    }

    //선택된 아이템이미지 반환
    fun getItem(position: Int): String? {
        return exerciseList?.get(position)?.E_image
    }

    override fun getItemCount(): Int {
        return exerciseList!!.size
    }

    //뷰홀더 클래스
    inner class ExerciseViewHolder(itemBinding: ExerciseItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        private var binding : ExerciseItemBinding = itemBinding

        fun bind(exercise: ExerciseData, listener: OnItemClickListener){
            //각 아이템에 변수 넣기
            binding?.setVariable(BR.exerciseitem, exercise)
            //클릭리스너 등록
            itemView.setOnClickListener {
                var currentPosition =  adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION){
                    if (itemClickListener != null){
                        itemClickListener!!.onItemClick(currentPosition)
                    }
                }
            }
        }
    }



    //아이템 클릭 인터페이스 등록
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}