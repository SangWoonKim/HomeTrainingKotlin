package com.study.hometrainingkotlin.View.exercise.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.study.hometrainingkotlin.BR
import com.study.hometrainingkotlin.Model.vo.ExerciseData
import com.study.hometrainingkotlin.R
import com.study.hometrainingkotlin.databinding.ExerciseItemBinding


class ExerciseAdapter(exerciseArray: ArrayList<ExerciseData>, listener: OnItemClickListener): RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    lateinit var exerciseList : ArrayList<ExerciseData>

    var itemClickListener: OnItemClickListener? = null

    init {
        exerciseList = exerciseArray
        itemClickListener = listener
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
        var exercise : ExerciseData = exerciseList.get(position)
        holder.bind(exercise, itemClickListener!!)
    }

    //선택된 아이템 반환
    fun getItem(position: Int): String? {
        return exerciseList.get(position).E_image
    }

    override fun getItemCount(): Int {
        return exerciseList.size
    }

    //뷰홀더 클래스
    inner class ExerciseViewHolder(itemBinding: ExerciseItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        private var binding : ExerciseItemBinding?=null

        init{
            binding = itemBinding
        }

        fun bind(exercise: ExerciseData, listener: OnItemClickListener){
            //각 아이템에 변수 넣기
            binding?.setVariable(BR.exerciseItem, exercise)
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