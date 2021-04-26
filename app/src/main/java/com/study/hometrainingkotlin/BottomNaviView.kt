package com.study.hometrainingkotlin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.study.hometrainingkotlin.view.SelectFragment.*
import com.study.hometrainingkotlin.view.SelectFragment.List

class BottomNaviView : AppCompatActivity() {
    //뒤로가기 버튼 시 사용 첫번째 뒤로가기 버튼 클릭시 현재시간 저장
    private var backPressedTime: Long = 0

    //바텀네비게이션뷰 객체 정의
    private var navigationview: BottomNavigationView ?= null
    //프레그먼트 제어 정의
    private var fragmentManager: FragmentManager ?= null
    private var fragmentTransaction: FragmentTransaction ?=null

    //프레그먼트 객체 정의
    private var exercise : Exercise ?= null
    private var timer : Timer ?= null
    private var food : Food ?= null
    private var list : List ?= null
    private var settings : Settings ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //정의된 객체 할당
        exercise = Exercise()
        timer = Timer()
        food = Food()
        list = List()
        settings = Settings()

        setFragment(0) //초기화면 설정

        navigationview = findViewById(R.id.bottomnaviview)
        //itemClickListener
        navigationview?.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.select_exercise -> setFragment(0)
                R.id.select_timer -> setFragment(1)
                R.id.select_food -> setFragment(2)
                R.id.select_exerciselist -> setFragment(3)
                R.id.select_settings-> setFragment(4)
            }
            true
        })
    }

    //프레그먼트 교체 (FrameLayout에 각기 다른화면 띄우기)
    private fun setFragment(page:Int){
        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager!!.beginTransaction()

        when(page){
            0 -> {
                fragmentTransaction!!.replace(R.id.Main_Frame, exercise!!,"exercise") //Main_Frame에 exercise(Fragment상속) 객체 표시 및 스택tag이름지정
                    .addToBackStack("exercise") //프레그먼트 이동시 스택에 기록("스택이름 태그")
            }
            1 -> {
                fragmentTransaction!!.replace(R.id.Main_Frame, timer!!,"timer")
                    .addToBackStack("timer")
            }
            2 -> {
                fragmentTransaction!!.replace(R.id.Main_Frame, food!!,"food")
                    .addToBackStack("food")
            }
            3 -> {
                fragmentTransaction!!.replace(R.id.Main_Frame, list!!,"list")
                    .addToBackStack("list")
            }
            4 -> {
                fragmentTransaction!!.replace(R.id.Main_Frame, settings!!,"settings")
                    .addToBackStack("settings")
            }
        }
        fragmentTransaction!!.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE) //애니메이션 설정
        fragmentTransaction!!.commit() //적용
    }

    //뒤로가기 버튼 클릭시 바텀네비게이션의 아이템 focus처리 메소드
    private fun backButtonUpdateIcon(navigationView: BottomNavigationView){
        //.findFragmentByTag("Tag") Tag이름의 태그가 있을 시 프레그먼트 저장
        val stackTag1:Fragment? = supportFragmentManager.findFragmentByTag("exercise")
        val stackTag2:Fragment? = supportFragmentManager.findFragmentByTag("timer")
        val stackTag3:Fragment? = supportFragmentManager.findFragmentByTag("food")
        val stackTag4:Fragment? = supportFragmentManager.findFragmentByTag("list")
        val stackTag5:Fragment? = supportFragmentManager.findFragmentByTag("settings")

        //프래그먼트가 존재하거나 현재 표시중일 시 네비게이션 버튼 focus 표시
        if(stackTag1 != null && stackTag1.isVisible) {navigationView.menu.findItem(R.id.select_exercise).isChecked = true }
        if(stackTag2 != null && stackTag2.isVisible) {navigationView.menu.findItem(R.id.select_timer).isChecked = true }
        if(stackTag3 != null && stackTag3.isVisible) {navigationView.menu.findItem(R.id.select_food).isChecked = true }
        if(stackTag4 != null && stackTag4.isVisible) {navigationView.menu.findItem(R.id.select_exerciselist).isChecked = true }
        if(stackTag5 != null && stackTag5.isVisible) {navigationView.menu.findItem(R.id.select_settings).isChecked = true }
    }

    //뒤로가기 키 누를시 이벤트 정의
    override fun onBackPressed() {

        //마지막 프레그먼트 스택일경우 (스택이 없을 경우는 0으로 처리하면 됨
        if (supportFragmentManager.backStackEntryCount ==1){
            var currentTime:Long = System.currentTimeMillis()           //뒤로가기 버튼 클릭시 부터 시간을 측정
            var intervalTime:Long = currentTime - backPressedTime       //처음 누른 후 두번째 백버튼 클릭한 시간 간격을 저장
            if (intervalTime in 0..2000){                               //두번째 백버튼을 클릭한 시간차가 2000ms 이내 일 경우
//                finishAffinity()                                          //모든 액티비티 닫음(부모액티비티도 없앰)
                finish()                                                //현재 액티비티 닫음(이전 액티비티 스택이 있을경우 이전액티비티가 보여짐)
            }else{                                                      //처음 백버튼을 누르거나 시간차가 2000ms 초과 일 경우
                backPressedTime = currentTime                           //현재 측정된 시간을 backPressedTime에 저장
                Toast.makeText(this,"한 번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show()
                return
            }
        }else{
            super.onBackPressed()
            //뒤로가기 아이콘 포커스 처리
            val itemFocus = findViewById<View>(R.id.bottomnaviview) as BottomNavigationView
            backButtonUpdateIcon(itemFocus)
        }

    }
}
