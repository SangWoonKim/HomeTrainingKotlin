package com.study.hometrainingkotlin.view.SelectFragment

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.study.hometrainingkotlin.R
import com.study.hometrainingkotlin.model.localrepository.room.dao.ExerciseListEntity
import com.study.hometrainingkotlin.view.SelectFragment.dialog.ListCalSumDialog
import com.study.hometrainingkotlin.view.adapter.ExerciseListAdapter
import com.study.hometrainingkotlin.view.adapter.ItemTouchCallback
import com.study.hometrainingkotlin.viewmodel.ExerciseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking

class List : Fragment(), View.OnClickListener {
    //view 객체 정의
    private var list_RecyclerView: RecyclerView? = null
    private var BTN_List_fight: Button? = null
    private var BTN_List_accept: Button? = null
    private var BTN_List_active: Button? = null

    //data또는 뷰모델 정의
    private var exerciseListArray: ArrayList<ExerciseListEntity>? = null

    //    private val exerciseViewModel : ExerciseViewModel by viewModels()
    private val exerciseViewModel: ExerciseViewModel by activityViewModels()
    private var exerciseListAdapter: ExerciseListAdapter? = null

    //아이템 스와이프를 위한 객체
    var swipeCallback = object : ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {

        //사용 안하기 때문에 false반환(위아래 드래그)
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ) = false

        //스와이프 기능
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition //viewHolder를 통해 해당 아이템의 position값 저장
            exerciseViewModel.deleteListItem(exerciseListArray!!.get(position)) //선택한 아이템을 db에서 삭제
            exerciseListArray!!.removeAt(position)    //선택한 아이템을 배열에서 삭제
            exerciseListAdapter!!.notifyDataSetChanged() // 갱신
        }


    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
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


    }

    //LiveData를 갱신받으려면 onActivityCreated에서 호출해야함
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        exerciseViewModel.selectListItem().observe(viewLifecycleOwner, Observer { changeData ->
            //값이 변하는 것을 확인함
            exerciseListArray = changeData as ArrayList<ExerciseListEntity>
            Log.d("changeData", changeData.toString())
            setAdapter()
        })
    }

    //어뎁터 연결 및 갱신시 사용되는 메소드
    private fun setAdapter() {
        //어뎁터 첫 생성시
        if (exerciseListAdapter == null) {
            exerciseListAdapter =
                ExerciseListAdapter(exerciseListArray as ArrayList<ExerciseListEntity>)
            list_RecyclerView!!.layoutManager = LinearLayoutManager(context)
            list_RecyclerView!!.adapter = exerciseListAdapter
        } else {//어뎁터 갱신시
            list_RecyclerView!!.layoutManager = LinearLayoutManager(context)

            exerciseListAdapter!!.updateData(exerciseListArray!!)
            exerciseListAdapter!!.notifyDataSetChanged()
            list_RecyclerView!!.adapter = exerciseListAdapter
        }
//        //itemSwipe 각 아이템별 콜백을 받기위한 객체
//        var itemSwipe = ItemTouchHelper(ItemTouchCallback(exerciseListAdapter!!))
//        //리사이클러뷰와 ItemTouchHelper와 연결
//        itemSwipe.attachToRecyclerView(list_RecyclerView)

        var itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(list_RecyclerView)

    }


//    private fun calNullCheck(): Int {
////        var test:Int
////        runBlocking {
////            test = exerciseViewModel.sumCalListItem()
////        }
////        return test
//
//    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            //나 자신과의 싸움 버튼(그래프)
            R.id.BTN_List_fight -> {

            }

            //계산 및 초기화 버튼(칼로리 계산)
            R.id.BTN_List_accept -> {
                val dialog = ListCalSumDialog()
                fragmentManager?.let { dialog.show(it,"dialog") }
//                val dialogBuilder = context?.let { AlertDialog.Builder(it) }
//
//                dialogBuilder?.setMessage(calNullCheck().toString())
//                    ?.setTitle("칼로리 계산")
//                    ?.setPositiveButton(
//                        "초기화"
//                    ) { dialog, which -> exerciseViewModel.deleteAllListItem() }
//                    ?.setNeutralButton(
//                        "취소"
//                    ) { dialog, which -> }
//                    ?.create()?.show()
            }

            //운동실행 버튼 (애니메이션 실행)
            R.id.BTN_List_active -> {

            }
        }
    }
}