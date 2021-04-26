package com.study.hometrainingkotlin.view.SelectFragment

import android.content.Intent
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.study.hometrainingkotlin.R
import com.study.hometrainingkotlin.view.alarm.Alarm
import java.lang.Exception

//주석처리된 곳은 Thread를 이용하여 message객체를 handler에 보내는 방식의 구현   (좋은 방법)
//현재는 handler를 이용하여 계속 UIThread에 message를 보내 화면갱신이 일어나게함 (좋은 방법이 아님),(코드 수를 줄이기 위함 그리고 메인스레드에 부담이 덜 가는것 같아 사용중)

class Timer : Fragment(), View.OnClickListener {
    //View정의
    private var stopWatch : TextView?= null
    private var record : TextView?= null
    private var BTN_Timer_Start : Button ?= null
    private var BTN_Timer_Record : Button ?= null
    private var BTN_Timer_Alarm : Button ?= null

    //상태값 정의
    companion object{
        //상태값에 따른 상수
        val INIT:Int = 0; //처음
        val RUNNING:Int = 1; //실행중
        val PAUSE:Int = 2; //정지

        //상태을 저장하는 변수
        var status:Int = INIT
        var threadState:Boolean = false
    }

    //기록할 때 순서 체크를 위한 변수
    private var count = 1

    //타이머 시간 값을 저장할 변수
    private var baseTime: Long = 0          //시작버튼 클릭 후 경과시간 저장
    private var pauseTime: Long = 0         //정지버튼 클릭 후 경과시간 저장

    //핸들러 선언(MemoryWeak 최적화)
    private var mainHandler = object : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
//            stopWatch!!.text = msg.obj.toString()
            stopWatch!!.text = getTime()
            this.sendEmptyMessage(0)
        }
    }

    //핸들러에 메시지를 보내기 위한 스레드
//    private var updateWatchThread = Thread {
//        while (true) {
//            if (threadState) {
//                try {
//                    var message: Message = Message.obtain()     //빈메시지 객체를 반환 받음
//                    message.obj = getTime()                     //메시지에 데이터 삽입
//                    message.what = 0
//                    mainHandler.sendMessage(message)            //핸들러에 메시지 보냄
//                } catch (e: InterruptedException) {
//                    e.printStackTrace()
//                }
//            }else if (!threadState){
//                try {
//                    Thread.yield()                              //스레드 정지
//                }catch (e:Exception){
//                    e.printStackTrace()
//                }
//            }
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stopWatch = view.findViewById(R.id.TV_Timer_StopWatch)
        record = view.findViewById(R.id.TV_Timer_Record)
        BTN_Timer_Start = view.findViewById(R.id.BTN_Timer_Start)
        BTN_Timer_Alarm = view.findViewById(R.id.BTN_Timer_Alarm)
        BTN_Timer_Record = view.findViewById(R.id.BTN_Timer_Record)
        //리스너 등록
        BTN_Timer_Start!!.setOnClickListener(this)
        BTN_Timer_Alarm!!.setOnClickListener(this)
        BTN_Timer_Record!!.setOnClickListener(this)
//        updateWatchThread.start()

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.BTN_Timer_Start -> {
                startStopWatch()
            }
            R.id.BTN_Timer_Alarm -> {
                val alarm : Intent = Intent(activity,Alarm::class.java)
                startActivity(alarm)
            }
            R.id.BTN_Timer_Record -> {
                recordStopWatch()
            }
        }
    }

    //시작버튼 누를시 작돋되는 메소드
    //핸들러를 통해
    private fun startStopWatch(){
        when(status){
            INIT -> { //초기 시작시
                baseTime = SystemClock.elapsedRealtime()         //elapsedRealtime() 기기가 부팅된 시점부터 현재까지의 시간간격을 ms단위로 출력하는 메소드(여기서는 클릭후 부터 시간을 반환)
                mainHandler.sendEmptyMessage(0)            //핸들러에서 메시지를 보냄(0이라는 메시지 제목) 즉 화면갱신 시작
                BTN_Timer_Start!!.text = "멈춤"                  //시작버튼 누를시 텍스트 변경
                BTN_Timer_Record!!.text = "기록"
                BTN_Timer_Record!!.isEnabled = true
//                threadState = true                             //쓰레드 시작상태 변경
                status = RUNNING                                 //상태 변경(실행중)
            }

            RUNNING -> { // 실행중일시
                mainHandler.removeMessages(0)              //핸들러에 메시지 삭제(0이라는 메시지 제목) 즉 화면갱신 중단
                pauseTime = SystemClock.elapsedRealtime()
                BTN_Timer_Start!!.text = "다시시작"
                BTN_Timer_Record!!.text = "초기화"
//                threadState = false                            //쓰레드 시작상태 변경
//                updateWatchThread
                status = PAUSE                                   //상태 변경(정지)
            }

            PAUSE -> {  //정지상태일시
                var reStart = SystemClock.elapsedRealtime()
                baseTime += (reStart - pauseTime)
                mainHandler.sendEmptyMessage(0)
//                threadState = true                             //쓰레드 시작상태 변경
                BTN_Timer_Start!!.text = "멈춤"
                BTN_Timer_Record!!.text = "기록"
                status = RUNNING                                 //상태 변경 (시작)
            }

        }
    }

    //'기록'버튼 클릭시 이벤트 메소드
    //기록하는 메소드
    private fun recordStopWatch(){
        when(status){
            RUNNING -> {  //타이머 실행중일시
                var timeList = record!!.text.toString()
                timeList += String.format("%2d.%s\n", count, getTime())
                record!!.text = timeList    //메소드 기록
                count++                     //카운트 값 증가
            }

            //PAUSE 상태에서 '기록'버튼은 '초기화'기능을 수행해야하기에
            //모든 값을 초기화 한 후 상태값도 초기화 함
            PAUSE -> {   //스탑워치 정지중일시
                BTN_Timer_Start!!.text = "시작"
                BTN_Timer_Record!!.text = "기록"

                stopWatch!!.text = "00:00:00"
                record!!.text = ""

                baseTime = 0
                pauseTime = 0
                count = 1
                status = INIT
            }
        }
    }

    //경과시간을 얻는 메소드
    //핸들러에 경과 시간을 반환
    private fun getTime():String{
        var nowTime = SystemClock.elapsedRealtime()
        var overTime = nowTime-baseTime     //함수가 실행된 이후 경과시간

        var m = overTime/1000/60
        var s = (overTime/1000)%60
        var ms = overTime%1000

        var recTime = String.format("%02d:%02d:%03d", m, s, ms)
        return recTime
    }
}