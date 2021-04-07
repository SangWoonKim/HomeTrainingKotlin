package com.study.hometrainingkotlin.model.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.study.hometrainingkotlin.model.utils.ExerciseInterface
import com.study.hometrainingkotlin.model.utils.RetrofitClient
import com.study.hometrainingkotlin.model.vo.ExerciseData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//서버에서 데이터를 받아 저장하는 클래스 Repository

class ExerciseRepository {
    private val TAG: String = javaClass.simpleName

    companion object {
        var exerciseUpperArray: ArrayList<ExerciseData> ?= null
        var exerciseUpperLiveArray: MutableLiveData<ArrayList<ExerciseData>> = MutableLiveData()
        lateinit var exerciseLowerArray: ArrayList<ExerciseData>
        lateinit var exerciseLoinsArray: ArrayList<ExerciseData>
        lateinit var exerciseBodyArray: ArrayList<ExerciseData>
        val responseData =
            RetrofitClient.RetrofitClient.getInstance().create(ExerciseInterface::class.java)

        @JvmStatic
        private var exerciseRepository: ExerciseRepository? = null

        @JvmStatic
        fun getInstance(): ExerciseRepository? {
            if (exerciseRepository == null) {
                exerciseRepository = ExerciseRepository()
            }
            return exerciseRepository
        }
    }

    //상체 부위 운동 조회
    fun getExerciseUpper(): MutableLiveData<ArrayList<ExerciseData>> {
        if (exerciseUpperArray == null) {
            var exerciseUppers: Call<ArrayList<ExerciseData>>? =
                responseData.getExercisePartData("upper")
            if (exerciseUppers != null) {
                exerciseUppers.enqueue(object : Callback<ArrayList<ExerciseData>> {
                    override fun onResponse(
                        call: Call<ArrayList<ExerciseData>>,
                        response: Response<ArrayList<ExerciseData>>
                    ) {
                        Log.d(TAG, "응답코드" + response.code())
                        if (response.isSuccessful) {
                            exerciseUpperArray = response.body()!!
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
    fun getExerciseLoins(): ArrayList<ExerciseData> {
        if (exerciseLoinsArray == null) {
            var exerciseLoins: Call<ArrayList<ExerciseData>>? =
                responseData.getExercisePartData("loinsr")
            if (exerciseLoins != null) {
                exerciseLoins.enqueue(object : Callback<ArrayList<ExerciseData>> {
                    override fun onResponse(
                        call: Call<ArrayList<ExerciseData>>,
                        response: Response<ArrayList<ExerciseData>>
                    ) {
                        Log.d(TAG, "응답코드" + response.code())
                        if (response.isSuccessful) {
                            exerciseLoinsArray = response.body()!!
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<ExerciseData>>, t: Throwable) {
                        Log.d(TAG, "응답코드" + t.message)
                    }

                })
            }
            return exerciseLoinsArray
        }else{
            return exerciseLoinsArray
        }
    }
}