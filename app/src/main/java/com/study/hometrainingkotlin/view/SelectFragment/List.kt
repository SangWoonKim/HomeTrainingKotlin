package com.study.hometrainingkotlin.view.SelectFragment


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.study.hometrainingkotlin.R
import com.study.hometrainingkotlin.model.localrepository.room.dao.ExerciseListEntity
import com.study.hometrainingkotlin.model.localrepository.room.vo.ExerciseMyselfEntity
import com.study.hometrainingkotlin.model.localrepository.room.vo.ExerciseSumCalEntity
import com.study.hometrainingkotlin.view.ExerciseList.MyselfFight
import com.study.hometrainingkotlin.view.adapter.ExerciseListAdapter
import com.study.hometrainingkotlin.viewmodel.ExerciseViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class List : Fragment(), View.OnClickListener {
    //view 객체 정의
    private var list_RecyclerView: RecyclerView? = null
    private var BTN_List_fight: Button? = null
    private var BTN_List_accept: Button? = null
    private var BTN_List_active: Button? = null

    //data또는 뷰모델 정의
    private var exerciseListArray: ArrayList<ExerciseListEntity>? = null
    private val exerciseViewModel: ExerciseViewModel by activityViewModels()

    //운동목록에 사용되는 adapter
    private var exerciseListAdapter: ExerciseListAdapter? = null

    //다이얼로그에 사용되는 변수및 빌더
    private var dialogBuilder: AlertDialog.Builder ?= null
    private var cal:Int?=null
    private var currentDate :String ?=null
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

/**
 * 안드로이드 생명주기 공간
 * */
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
        //운동목록에 사용됨
        exerciseViewModel.selectListItem().observe(viewLifecycleOwner, Observer { changeData ->
            //값이 변하는 것을 확인함
            exerciseListArray = changeData as ArrayList<ExerciseListEntity>
            Log.d("changeData", changeData.toString())
            setAdapter()
        })
        //칼로리 계산에서 사용됨
        exerciseViewModel.sumCalListItem().observe(viewLifecycleOwner, { changeData ->
            if (changeData ==null){
                cal=0
            }else {
                cal = changeData
                Log.d("changeDataCal", changeData.toString())
            }
        })
    }

    /**
     * 어뎁터 연결 및 갱신시 사용되는 메소드
     * 다이얼로그 생성시 사용되는 메소드
     * 클릭시 사용되는 메소드
     * */
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


    //다이얼로그 생성
    private fun buildDialog(){
        dialogBuilder = context?.let { AlertDialog.Builder(it) }

        dialogBuilder?.setMessage(cal.toString())
            ?.setTitle("칼로리 계산")
            ?.setPositiveButton(
                "추가"
            ) { dialog, which ->
                if (exerciseListArray?.size!=0) {
                    //운동목록의 아이템들을 exerciseMyself테이블에 삽입
                    exerciseViewModel.insertMyself(insertMyselfData())
                    //운동목록의 아이템들의 칼로리 총합과 날짜 삽입
                    exerciseViewModel.insertSumCal(ExerciseSumCalEntity(cal,
                    currentDate))
                }else{
                    Toast.makeText(activity,"",Toast.LENGTH_LONG).show()
                }
            }
            ?.setNeutralButton(
                "초기화"
            ) { dialog, which -> exerciseViewModel.deleteAllListItem() }
            ?.create()?.show()
    }

    //exercisemyself테이블에 운동목록에 있는 데이터 삽입 메소드
    private fun insertMyselfData():ArrayList<ExerciseMyselfEntity>{
        var insertData:ArrayList<ExerciseMyselfEntity> =ArrayList()
        var dateFormat = SimpleDateFormat("yyMMdd")
        var date = Date()
        currentDate = dateFormat.format(date)
        for (i in 0..exerciseListArray!!.size!!-1) {
            insertData.add(ExerciseMyselfEntity(currentDate,
                exerciseListArray!!.get(i).part,
                exerciseListArray!!.get(i).name,
                exerciseListArray!!.get(i).cal)
            )
        }
        return insertData
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            //나 자신과의 싸움 버튼(그래프)
            R.id.BTN_List_fight -> {
                val myselfActivity =Intent(activity,MyselfFight::class.java)
                startActivity(myselfActivity)
            }

            //계산 및 초기화 버튼(칼로리 계산)
            R.id.BTN_List_accept -> {
                buildDialog()
            }

            //운동실행 버튼 (애니메이션 실행)
            R.id.BTN_List_active -> {

            }
        }
    }
}