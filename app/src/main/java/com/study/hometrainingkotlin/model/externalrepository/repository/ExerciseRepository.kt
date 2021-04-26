package com.study.hometrainingkotlin.model.externalrepository.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.study.hometrainingkotlin.model.externalrepository.utils.ExerciseInterface
import com.study.hometrainingkotlin.model.externalrepository.utils.RetrofitClient
import com.study.hometrainingkotlin.model.externalrepository.vo.ExerciseData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//서버에서 데이터를 받아 저장하는 클래스 Repository
//인터페이스 또는 추상클래스를 이용하여 보일러플레이트 코드 정리해야함
class ExerciseRepository {
    private val TAG: String = javaClass.simpleName

    companion object {
        var exerciseUpperArray: ArrayList<ExerciseData> ?= null
        var exerciseUpperLiveArray: MutableLiveData<ArrayList<ExerciseData>> = MutableLiveData()
        lateinit var exerciseLowerArray: ArrayList<ExerciseData>
        var exerciseLoinsArray: ArrayList<ExerciseData> ?= null
        var exerciseLoinsLiveArray: MutableLiveData<ArrayList<ExerciseData>> = MutableLiveData()
        lateinit var exerciseBodyArray: ArrayList<ExerciseData>
        val responseData =
            RetrofitClient.RetrofitClient.getInstance().create(ExerciseInterface::class.java)

        //singleton 인스턴스
        @JvmStatic
        private var exerciseRepository: ExerciseRepository? = null

        //singleton
        @JvmStatic
        fun getInstance(): ExerciseRepository? {
            if (exerciseRepository == null) {
                exerciseRepository = ExerciseRepository()
            }
            return exerciseRepository
        }
    }

    //상체 부위 운동 조회
    //return MutableLiveData<ArrayList<ExerciseData>>
    fun getExerciseUpper(): MutableLiveData<ArrayList<ExerciseData>> {
        //exericseUpperArray의 객체가 생성되어있지 않은경우
        if (exerciseUpperArray == null) {
            var exerciseUppers: Call<ArrayList<ExerciseData>>? =
                responseData.getExercisePartData("upper")               //upper부위에 대한 파라미터 정의 (where E_part = upper)
            //retrofit의 call 객체가 생성되어있을 경우
            if (exerciseUppers != null) {
                exerciseUppers.enqueue(object : Callback<ArrayList<ExerciseData>> {
                    override fun onResponse(
                        call: Call<ArrayList<ExerciseData>>,
                        response: Response<ArrayList<ExerciseData>>
                    ) {
                        Log.d(TAG, "응답코드" + response.code())
                        //응답을 성공적으로 받았을 시 200번 신호 수신시
                        if (response.isSuccessful) {
                            //json데이터 exerciseUpperArray에 삽입
                            exerciseUpperArray = response.body()!!
                            //exerciseUpperArray의 데이터 exerciseUpperLiveArray에 삽입
                            exerciseUpperLiveArray.value = exerciseUpperArray
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<ExerciseData>>, t: Throwable) {
                        Log.d(TAG, "응답코드" + t.message)
                    }

                })
            }
            return exerciseUpperLiveArray
        } else {
            return exerciseUpperLiveArray
        }
    }

    //다리 부위 운동 조회
    fun getExerciseLower(): ArrayList<ExerciseData> {
        if (exerciseLowerArray == null) {
            var exerciseLowers: Call<ArrayList<ExerciseData>>? =
                responseData.getExercisePartData("lower")
            if (exerciseLowers != null) {
                exerciseLowers.enqueue(object : Callback<ArrayList<ExerciseData>> {
                    override fun onResponse(
                        call: Call<ArrayList<ExerciseData>>,
                        response: Response<ArrayList<ExerciseData>>
                    ) {
                        Log.d(TAG, "응답코드" + response.code())
                        if (response.isSuccessful) {
                            exerciseLowerArray = response.body()!!
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<ExerciseData>>, t: Throwable) {
                        Log.d(TAG, "응답코드" + t.message)
                    }

                })
            }
            return exerciseLowerArray
        }else{
            return exerciseLowerArray
        }
    }

    //전신 운동 조회
    fun getExerciseBody(): ArrayList<ExerciseData> {
        if (exerciseBodyArray == null) {
            var exerciseBodies: Call<ArrayList<ExerciseData>>? =
                responseData.getExercisePartData("body")
            if (exerciseBodies != null) {
                exerciseBodies.enqueue(object : Callback<ArrayList<ExerciseData>> {
                    override fun onResponse(
                        call: Call<ArrayList<ExerciseData>>,
                        response: Response<ArrayList<ExerciseData>>
                    ) {
                        Log.d(TAG, "응답코드" + response.code())
                        if (response.isSuccessful) {
                            exerciseBodyArray = response.body()!!
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<ExerciseData>>, t: Throwable) {
                        Log.d(TAG, "응답코드" + t.message)
                    }

                })
            }
            return exerciseBodyArray
        }else{
            return exerciseBodyArray
        }
    }

    //허리운동 조회
    //if문 제거 이유 = 서버의 아이템이 변경될 때마다 해당 값을 다시 받아와야함
    //              = 그 전에는 if문 덕분에 call객체가 있을 경우 불러오지 않게 했음으로 안됨
   fun getExerciseLoins(): MutableLiveData<ArrayList<ExerciseData>> {
            var exerciseLoins: Call<ArrayList<ExerciseData>>? =
                responseData.getExercisePartData("loins")                   //loins부위에 대한 파라미터 정의 (where E_part = loins)
                exerciseLoins!!.enqueue(object : Callback<ArrayList<ExerciseData>> {
                    override fun onResponse(
                        call: Call<ArrayList<ExerciseData>>,
                        response: Response<ArrayList<ExerciseData>>
                    ) {
                        Log.d(TAG, "응답코드" + response.code())
                        if (response.isSuccessful) {
                            exerciseLoinsArray = response.body()!!
                            exerciseLoinsLiveArray.value = exerciseLoinsArray
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<ExerciseData>>, t: Throwable) {
                        Log.d(TAG, "응답코드" + t.message)
                    }

                })
            return exerciseLoinsLiveArray
    }
}