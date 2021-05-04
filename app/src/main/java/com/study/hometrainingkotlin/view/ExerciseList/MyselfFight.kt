package com.study.hometrainingkotlin.view.ExerciseList

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.study.hometrainingkotlin.R
import com.study.hometrainingkotlin.model.localrepository.room.vo.ExerciseMyselfEntity
import com.study.hometrainingkotlin.model.localrepository.room.vo.ExerciseSumCalEntity
import com.study.hometrainingkotlin.view.adapter.ExerciseMyselfAdapter
import com.study.hometrainingkotlin.viewmodel.ExerciseViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MyselfFight : AppCompatActivity() {
    //객체와 뷰에대한 변수
    private val viewModel: ExerciseViewModel by viewModels()
    private var list_Myself: RecyclerView? = null
    private var exerciseMyselfAdapter: ExerciseMyselfAdapter? = null

    //MPAndroidChart에서 사용하는 변수
    private var chart_Myself: BarChart? = null          //view객체
    private var barEntries = ArrayList<BarEntry>()      //BarEntry = BarChart에 표시될 데이터를 저장

    //db에서 받은 값 저장 List
    private var detailMyselfList: ArrayList<ExerciseMyselfEntity>? = null
    //날짜 형식으로 받은 데이터를 저장하는 List
    private var optionalMyselfList: ArrayList<ExerciseMyselfEntity> = ArrayList()
    //현재 날짜를 오늘날짜로 초기화
    private var date: String = SimpleDateFormat("yyMMdd").format(Date())
    private var exerciseSumCalList: ArrayList<ExerciseSumCalEntity>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.myself_fight_activity)
        list_Myself = findViewById(R.id.List_Myself)
        chart_Myself = findViewById(R.id.Chart_Myself)


        //BarChart를 위한 부분
        viewModel.getSumCal().observe(this) { changeData ->
            exerciseSumCalList = changeData as ArrayList<ExerciseSumCalEntity>
            for (i in 0..changeData.size - 1) {

                //db에서 조회한 데이터들을 배열에 넣은 후 형변환하여 (x,y)좌표형식으로 넣음
                //삽입하는 메소드에서 date의 패턴을 바꿔서 넣어야함
                //Join한 값을 넣는 변수 추가해야함
                barEntries.add(
                    BarEntry(
                        changeData.get(i).Sc_date!!.toFloat(),
                        changeData.get(i).Sc_cal!!.toFloat()
                    )
                )
            }
            barDataSetting()
        }
        //RecyclerView를 위한 부분
        //문제점 발생 LiveData형식 즉 Mutable이 아니기 때문에 다른값이 들어가지 못함...
        //1안 cursor로 받는다 즉 ExerciseDAo
        viewModel.getMyselfDetail().observe(this) { changeData ->
            detailMyselfList = changeData as ArrayList<ExerciseMyselfEntity>
        }
    }

    private fun xAxsisLabelCreate(): ArrayList<String> {
        var label: ArrayList<String> = ArrayList()
        for (i in 0..exerciseSumCalList!!.size - 1) {
            label.add(exerciseSumCalList!!.get(i).Sc_cal.toString())
        }
        return label
    }

    //BarChart만들때 사용되는 메소드
    private fun barDataSetting() {
        val barDataSet = BarDataSet(barEntries, "나 자신과의 싸움 그래프")     //막대의 두께, 색, 테두리등 기능 설정
        val barData = BarData(barDataSet)           //막대에 보여질 데이터 구성


        var xAxis: XAxis = chart_Myself?.xAxis!!
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxsisLabelCreate())

        barData.barWidth = 1.0f                       //막대 너비 설정

        chart_Myself?.data = barData
        chart_Myself?.animateXY(0, 100)                  //chart가 그려질 때 애니메이션

        //최대 그래프가 포이는 개수 설정
//        chart_Myself?.setMaxVisibleValueCount(7)
        //두손가락으로 줌인 줌아웃하는 기능 설정
//        chart_Myself?.setPinchZoom(true)

        //뷰 갱신
        chart_Myself?.invalidate()

        //바 클릭시 이벤트 등록
        chart_Myself?.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (e != null) {
                    //x좌표의 값 전달(x좌표의 값은 날짜임)
                    date = e.x.toInt().toString()
                    //optionalMyselfList에 아이템 삭제
                    if (optionalMyselfList.size>0){
                        optionalMyselfList.clear()
                    }
                    //optionalMyselfList에 아이템 삽입
                    for (i in 0 until detailMyselfList!!.size){
                        //My_date의 칼럼값이 date의 값과 같을 경우에만 추가
                        if (detailMyselfList!!.get(i).My_date == date){
                            optionalMyselfList?.add(detailMyselfList!!.get(i))
                        }
                    }
                    setMyselfAdapter()
                }
            }

            override fun onNothingSelected() {
                
            }
        })
    }

    //어뎁터 연결및 갱신 메소드
    private fun setMyselfAdapter() {

        if (exerciseMyselfAdapter == null) {
            exerciseMyselfAdapter = ExerciseMyselfAdapter(optionalMyselfList!!)
            list_Myself!!.layoutManager = LinearLayoutManager(this)
            list_Myself!!.adapter = exerciseMyselfAdapter
        } else {
            exerciseMyselfAdapter!!.notifyDataSetChanged()
        }
    }

}