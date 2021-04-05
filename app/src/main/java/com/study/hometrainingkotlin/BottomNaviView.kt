package com.study.hometrainingkotlin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.study.hometrainingkotlin.View.SelectFragment.*
import com.study.hometrainingkotlin.View.SelectFragment.List

class BottomNaviView : AppCompatActivity() {
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

    override fun onBackPressed() {
        var backKeyDownTime:Long = 0
        //뒤로가기 키를 처음 눌렀거나,액션을 취한 후 500ms이후에 뒤로가기키를 누를 경우
        if (System.currentTimeMillis() > backKeyDownTime + 500){
            backKeyDownTime = System.currentTimeMillis()
            Toast.makeText(this,"한 번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show()
            return
        }
        // fragmentStack이 존재하지 않을 경우
        else if (fragmentManager!!.backStackEntryCount == 0){
            finishAffinity()
            System.runFinalization()
            System.exit(0)
        }

        //뒤로가기 키를 액션을 취한 후 500ms이내에 뒤로가기를 누를경우
        if (System.currentTimeMillis() <= backKeyDownTime +500){
            this.moveTaskToBack(true) //현재 실행 앱을 백그라운드로 이동
            this.finish()//앱 종료
            android.os.Process.killProcess(android.os.Process.myPid())//앱 프로세스 종료
        }

        super.onBackPressed()
        val itemFocus = findViewById<View>(R.id.bottomnaviview) as BottomNavigationView
        backButtonUpdateIcon(itemFocus)
    }
}
