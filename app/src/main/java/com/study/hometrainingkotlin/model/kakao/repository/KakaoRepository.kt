package com.study.hometrainingkotlin.model.kakao.repository


import android.content.Context
import android.util.Log
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.lifecycle.MutableLiveData
import com.study.hometrainingkotlin.R
import com.study.hometrainingkotlin.model.kakao.util.RetrofitClientKakaoMaps
import com.study.hometrainingkotlin.model.kakao.util.SearchInterface
import com.study.hometrainingkotlin.model.kakao.vo.Documents
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KakaoRepository {

    companion object{

        private var searchResultLiveData:MutableLiveData<ArrayList<Documents>> = MutableLiveData()
        private var searchResultArray :Documents ?= Documents(ArrayList())

        //레트로핏을 이용한 서버에 요청하기 위한 객체
        private val requestServer = RetrofitClientKakaoMaps.RetrofitKakaoClient.getInstance().create(SearchInterface::class.java)

        //singleton
        @JvmStatic
        var kakaoRepository : KakaoRepository ?=null

        @JvmStatic
        fun getInstance() : KakaoRepository?{
            if (kakaoRepository == null){
                kakaoRepository = KakaoRepository()
            }
            return kakaoRepository
        }
    }

    //카카오맵 서버에서 근처 헬스장 정보를 가져오기위한 메소드(실패)
    //방식을 바꿔야할듯 private으로 바꾼후 하기
    //속지말자... x에 y y에 x가 들어가야함...
    private fun getGymSearchLocation(x:String,y:String){
        val searchRequestCallback: Call<Documents> = requestServer.searchKeyword(
            "KakaoAK 8ed3c141cc239582f4074c40f8faf784",
            "근처헬스장",
            y,
            x,
            1000,
            15)
        if (searchRequestCallback != null){
            //에러 부분 왜?
            searchRequestCallback.enqueue(object :Callback<Documents>{
                override fun onResponse(
                    call: Call<Documents>,
                    response: Response<Documents>
                ) {
                    Log.d("Test", "Raw: ${response.raw()}")
                    Log.d("Test", "Body: ${response.body()}")
                    Log.i("카카오맵 서버응답코드",""+response.code())
                    searchResultArray?.documents = response.body()?.documents!!
                }

                override fun onFailure(call: Call<Documents>, t: Throwable) {
                    Log.i("카카오맵 에러메시지",""+t.message)
                }
            })
        }
        searchResultArray!!.documents!!.forEach{
            Log.d("TestItem",it.place_name)
        }
    }

    //
    fun getGymSearchResult(x:String, y: String): Documents?{
        getGymSearchLocation(x,y)
        return searchResultArray
    }


    //test코드(성공)
     fun getGymSearchLocation2(){
        var searchRequestCallback: Call<Documents> = requestServer.searchKeyword(
            "KakaoAK 8ed3c141cc239582f4074c40f8faf784",
            "근처헬스장",
            "37.38023273704522",
            "127.23256409811758",
            1000,
            15)
        if (searchRequestCallback != null){
            searchRequestCallback.enqueue(object :Callback<Documents>{
                override fun onResponse(
                    call: Call<Documents>,
                    response: Response<Documents>
                ) {
                    Log.d("Test", "Raw: ${response.raw()}")
                    Log.d("Test", "Body: ${response.body()}")
                    Log.i("카카오맵 서버응답코드",""+response.code())

                }

                override fun onFailure(call: Call<Documents>, t: Throwable) {
                    Log.i("카카오맵 에러메시지",""+t.message)
                }
            })
        }
     }


}