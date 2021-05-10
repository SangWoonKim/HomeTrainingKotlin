package com.study.hometrainingkotlin.model.kakao.repository


import android.util.Log
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
        private var searchResultArray :ArrayList<Documents> ?= null

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

    //카카오맵 서버에서 근처 헬스장 정보를 가져오기위한 메소드
    fun getGymSearchLocation(x:String,y:String): ArrayList<Documents>{
        var searchRequestCallback: Call<ArrayList<Documents>> = requestServer.searchKeyword(R.string.kakao_rest_api_key.toString(),"근처헬스장",x,y,1000,15)
        if (searchRequestCallback != null){
            searchRequestCallback.enqueue(object :Callback<ArrayList<Documents>>{
                override fun onResponse(
                    call: Call<ArrayList<Documents>>,
                    response: Response<ArrayList<Documents>>
                ) {
                   Log.i("카카오맵 서버응답코드",""+response.code())
                    if (response.isSuccessful){
                        searchResultArray = response.body()
                    }
                }

                override fun onFailure(call: Call<ArrayList<Documents>>, t: Throwable) {
                    Log.i("카카오맵 에러메시지",""+t.message)
                }
            })
        }else{
            return searchResultArray!!
        }
        return searchResultArray!!
    }
}