package com.study.hometrainingkotlin.view.kakao

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.study.hometrainingkotlin.R
import com.study.hometrainingkotlin.model.kakao.vo.Documents
import com.study.hometrainingkotlin.viewmodel.ExerciseViewModel
import net.daum.mf.map.api.*

class KakaoMapsMain : AppCompatActivity(),MapView.CurrentLocationEventListener, MapView.POIItemEventListener,
    MapReverseGeoCoder.ReverseGeoCodingResultListener{
    companion object{
        //설정의 gps허용에 대해 수신받는코드
        const val GPS_REQUEST_CODE = 0x234
        const val PERMISSION_REQUEST_CODE = 0x345
    }
    //메니페스트의 권한이 명시된 변수
    private var manifestLocationPermissionCheck = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    private var mapView:MapView?=null
    private var searchResult:ArrayList<Documents>?= null
    private val viewModel:ExerciseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kakao_maps_main_activity)

        mapView = MapView(this)
        var mapViewContainer:ViewGroup = findViewById(R.id.MV_Kakao_Main)
        mapViewContainer.addView(mapView)


    }
    /**
     * 권한에 대한 부분
     * 1.전체적인 권한 확인
     * 2.위치 수신자의 상태 반환
     * 3.기기 설정을 변경하기 위한 다이얼로그 생성
     * 4.기기 설정에서 gps값을 반환받는 메소드
     * 5.앱내의 권한 요청을 반환받는 메소드
     * */


    //1
    //권한 설정및 알림
    @RequiresApi(Build.VERSION_CODES.P)
    fun checkPermission(){
        //GPS모듈이 사용가능한 기기인지 확인
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)){
            //메니페스트 권한 등록 확인
            var locationPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            //앱에서 위치서비스 권한 허용 확인
            if (locationPermission == PackageManager.PERMISSION_GRANTED){
                //현재위치를 얻을 수 있는지 확인
                if (locationReturn()){
                    //트랙킹모드를 이용하여 내 위치에 따라 지도의 중심이 이동됨
                    mapView!!.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading
                }else{
                    gpsDialog()
                }
            }else{ //앱에서 위치서비스 권한 허용이 되어있지 않는 경우
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this, manifestLocationPermissionCheck.get(
                            0
                        )
                    )){
                    Toast.makeText(this, "이 앱을 사용하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                    //앱에서 위치서비스에 대한 권한을 요청하는 코드
                    //(context,arrayof(manifest.permission),requestCode) (context,필요한 권한,반환받을 코드값)
                    ActivityCompat.requestPermissions(
                        this,
                        manifestLocationPermissionCheck,
                        PERMISSION_REQUEST_CODE
                    )
                }else{
                    ActivityCompat.requestPermissions(
                        this,
                        manifestLocationPermissionCheck,
                        PERMISSION_REQUEST_CODE
                    )
                }
            }
        }else{
            Toast.makeText(this, "GPS사용이 불가능한 기기입니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }


    //2
    //GPS나 3g,4g를 이용하여 GPS또는 Network수신자의 상태를 반환
    //현재 위치를 반환할 수 있을경우 true반환
    @RequiresApi(Build.VERSION_CODES.P)
    fun locationReturn() : Boolean{
        var locationManager:LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    //3
    //gps 활성화를 위한 다이얼로그 생성
    fun gpsDialog(){
        var dialog: AlertDialog.Builder = AlertDialog.Builder(this)
        dialog.setTitle("위치 서비스 설정")
        dialog.setMessage("위치 서비스를 설정해야 기능이 활성화됩니다 활성화 하시겠습니까?")
        dialog.setCancelable(true)
        dialog.setPositiveButton("설정"){ d, w ->
            //환경설정에서 gps설정을 지정
            var gpsSetting = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            //(intent,requestCode) (open할 intent, intent에서 결과값을 전달할 key(requestCode설정)
            startActivityForResult(gpsSetting, GPS_REQUEST_CODE)
        }
    }


    //4
    //startActivityForResult()메소드를 이용하여 반환받을 때 동작을 명시하는 메소드
    //액티비티간의 값을 주고받을 때 사용되는 메소드
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GPS_REQUEST_CODE){
            Toast.makeText(this, "GPS가 허용되었습니다", Toast.LENGTH_SHORT).show()
            if (locationReturn()){
                Toast.makeText(this, "GPS가 수신되었습니다", Toast.LENGTH_SHORT).show()
                checkPermission()
                return
            }
        }
    }


    //5
    //퍼미션 즉 앱 내에서 기기에서 받은 권한을 사용하는 것에 대한 반환 메소드
    //앱 권한에 대한 결과를 반환받는 메소드
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //요청코드 확인 및 권한에 대한 정보 확인(배열인 이유는 메니페스트에서 받는 값이 배열이기 때문 즉 size가 같으면 같은 권한)
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.size == manifestLocationPermissionCheck.size){
            var result_check:Boolean = true

            //코틀린의 foreach문은 break가 없어 label을 이용하여 break를 만듬
            run{
                //퍼미션 체크
                grantResults.forEach {
                    if (it != PackageManager.PERMISSION_GRANTED) {
                        result_check = false
                        return@run
                    }
                }
            }

            if (result_check) {
                mapView!!.currentLocationTrackingMode =
                    MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
            }else{
                //거부한 퍼미션이 있을 경우 앱을 사용할 수 없는 이유를 설명하고 앱을 종료

                //거부한 퍼미션이 있을 경우 앱을 사용할 수 없는 이유를 설명하고 앱을 종료
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this, manifestLocationPermissionCheck.get(
                            0
                        )
                    )) {
                    Toast.makeText(this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


    /**
     * 카카오맵 검색및 카카오맵에 대한 부분
     * */

    //rest통신을 통한 반경1km내의 헬스장 마커를 찍는 메소드
    fun nearbySearch(x:Double,y:Double){
        var searchResult = viewModel.getSearchResult(x.toString(),y.toString())
        var circle : MapCircle = MapCircle(
            MapPoint.mapPointWithGeoCoord(x,y),
            1000,
            Color.rgb(255,255,255),
            Color.rgb(255,255,255)
        )
        mapView!!.addCircle(circle)
        searchResult.forEach{
            var marker: MapPOIItem = MapPOIItem()
            marker.mapPoint

            var markerPoint:MapPoint = MapPoint.mapPointWithGeoCoord(it.x.toDouble(),it.y.toDouble())

        }
    }



    /**
     * CurrentLocationEventListener를 재정의한 부분
     * */

    //setCurrentLocationTrackingMode를 통해 단말의 현위치 좌표값을 통보받음
    //단말의 현위치 좌표값을 통보 받는 메소드 (업데이트할 뷰, 현위치, )
    override fun onCurrentLocationUpdate(p0: MapView?, p1: MapPoint?, p2: Float) {
        //위경도 좌표를 이용하여 좌표값으로 mapPoint객체 생성
        // (mapPoint객체는 지도화면 위의 위치와 관련된 작업을 처리할 때 사용)
        var mapPoint:MapPoint.GeoCoordinate =p1!!.mapPointGeoCoord
        Log.i(
            "LocationUpdate!", String.format(
                "MapView onCurrentLocationUpdate (%f,%f) accuracy(%f)",
                mapPoint.latitude,
                mapPoint.longitude,
                p2
            )
        )
        //현재 위치를 geo형식으로 저장
        var currentMapPoint:MapPoint = MapPoint.mapPointWithGeoCoord(
            mapPoint.latitude,
            mapPoint.longitude
        )
        //이 좌표로 지도 중심 이동
        mapView!!.setMapCenterPoint(currentMapPoint, true)
        //전역변수로 현재 좌표 저장
        var mCurrentLat = mapPoint.latitude
        var mCurrentLng = mapPoint.longitude
        Log.i("updateLocation2","현재위치 => $mCurrentLat  $mCurrentLng")
    }

    //기기의 각도값을 통보받는 메소드
    override fun onCurrentLocationDeviceHeadingUpdate(p0: MapView?, p1: Float) {

    }

    //현 위치 갱신 작업이 실패한 경우 호출
    override fun onCurrentLocationUpdateFailed(p0: MapView?) {
        Log.i("updateFailed","updateFailed")
        mapView!!.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading
    }

    //트래킹모드가 사용자에 의해 취소된 경우 호출
    override fun onCurrentLocationUpdateCancelled(p0: MapView?) {
        Log.i("updateCancel","updateCancel")
        mapView!!.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading
    }


    /**
     *POIItemEventListener를 재정의한 부분
     * */

    //사용자가 마커(POI)를 선택시 호출
    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
        TODO("Not yet implemented")
    }

    //deprecated됨 근데 선언이 강제로 되어있어 호출함
    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {

    }

    //마커 클릭시 나타나는 풍선을 클릭시 호출출
    override fun onCalloutBalloonOfPOIItemTouched(
        p0: MapView?,
        p1: MapPOIItem?,
        p2: MapPOIItem.CalloutBalloonButtonType?
    ) {
        TODO("Not yet implemented")
    }

    //마커를 이동시켰을 때 호출
    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {
        TODO("Not yet implemented")
    }


    /**
     *ReverseGeoCodingResultListener를 재정의한 부분
     * */


    //주소를 찾은 경우 호출된다.
    override fun onReverseGeoCoderFoundAddress(p0: MapReverseGeoCoder?, p1: String?) {
        TODO("Not yet implemented")
    }

    //Reverse Geo-Coding 서비스 호출에 실패한 경우 호출된다.
    override fun onReverseGeoCoderFailedToFindAddress(p0: MapReverseGeoCoder?) {
        TODO("Not yet implemented")
    }


}