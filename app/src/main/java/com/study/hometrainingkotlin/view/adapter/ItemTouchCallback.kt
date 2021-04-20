package com.study.hometrainingkotlin.view.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class ItemTouchCallback(listener: ItemTouchHelperListener): ItemTouchHelper.Callback() {

    private var listener:ItemTouchHelperListener?= null

    init {
        this.listener = listener
    }
    //return drag위치와 swipe위치를 입력하여 현재 position을 int로 반환
    //(drag는 사용하지 않음으로 0,swipe_flag)
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        var swipe_flag = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(0,swipe_flag)
    }

    //drag는 사용하지 않음으로 false
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    )=false

    //리사이클러뷰의 뷰홀더와 움직일 방향을 입력받는 메소드
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //선택한 아이템의 position값을 전달
        listener!!.onItemSwipe(viewHolder.adapterPosition)
    }
}