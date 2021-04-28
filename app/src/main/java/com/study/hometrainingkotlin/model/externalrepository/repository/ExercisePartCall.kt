package com.study.hometrainingkotlin.model.externalrepository.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.study.hometrainingkotlin.model.externalrepository.utils.ExerciseInterface
import com.study.hometrainingkotlin.model.externalrepository.utils.RetrofitClient
import com.study.hometrainingkotlin.model.externalrepository.vo.ExerciseData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface ExercisePartCall {
    //최후의 방법 각부위별로 LiveData와 ArrayList를 생성후
    //부위의 이름에 따라 if문으로 다른 객체들을 사용하게 하는 방법
    //코드가 더러워질듯
    val requestServer:ExerciseInterface ?
    val TAG :String?
    var exercisePartArray:ArrayList<ExerciseData>?
    //에러 LiveData의 재사용으로 인한 전에 조회한 아이템의 재사용됨
    var exerciseLivePartArray:MutableLiveData<ArrayList<ExerciseData>>?


    //if문 제거 이유 = 서버의 아이템이 변경될 때마다 해당 값을 다시 받아와야함
    //              = 그 전에는 if문 덕분에 call객체가 있을 경우 불러오지 않게 했음으로 안됨
    fun exercisePartCallBack(part:String): MutableLiveData<ArrayList<ExerciseData>>? {
        var exercisePartData: Call<ArrayList<ExerciseData>>? =
            requestServer!!.getExercisePartData(part)
        if (exercisePartData != null) {
            exercisePartData.enqueue(object : Callback<ArrayList<ExerciseData>> {
                override fun onResponse(
                    call: Call<ArrayList<ExerciseData>>,
                    response: Response<ArrayList<ExerciseData>>
                ) {
                    Log.d(TAG, "응답코드" + response.code())
                    //응답을 성공적으로 받았을 시 200번 신호 수신시
                    if (response.isSuccessful) {
                        //서버에서 받은 json데이터 exercisePartArray에 삽입
                        exercisePartArray = response.body()!!
                        exerciseLivePartArray?.value = exercisePartArray
                    }
                }

                override fun onFailure(call: Call<ArrayList<ExerciseData>>, t: Throwable) {
                    Log.d(TAG, "응답코드" + t.message)
                }

            })
            return exerciseLivePartArray
        }else{
            return exerciseLivePartArray
        }
    }


}