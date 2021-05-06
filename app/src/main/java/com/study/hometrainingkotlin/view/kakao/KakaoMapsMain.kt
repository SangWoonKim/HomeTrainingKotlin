package com.study.hometrainingkotlin.view.kakao

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.study.hometrainingkotlin.R
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class KakaoMapsMain : AppCompatActivity(){
    private var mapView:MapView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kakao_maps_main_activity)

        mapView = MapView(this)
        var mapViewContainer:ViewGroup = findViewById(R.id.MV_Kakao_Main)
        mapViewContainer.addView(mapView)
        //트랙킹모드를 이용하여 내 위치에 따라 지도의 중심이 이동됨
        mapView!!.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading

    }

    //권한 설정및 알림
    fun checkPermission(){
        //GPS모듈이 사용가능한 기기인지 확인
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)){
            //메니페스트 권한 등록 확인
            var locationPermission = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
            if (locationPermission == PackageManager.PERMISSION_GRANTED){

            }else{ //권한 등록이 되어있지 않는 경우

            }
        }else{

        }
    }
}