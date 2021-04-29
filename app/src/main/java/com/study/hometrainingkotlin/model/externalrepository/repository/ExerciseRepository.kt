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
//삭제나 추가같은 것도 가능하나 이 앱에서는 그 기능이 필요하지 않음
//보일러 플레이트 코드를 줄이기 위해 인터페이스나 다른 메소드를 만들어 수정해보았으나
//인터페이스는 데이터의 재사용덕분에 안됨
// 다른메소드는 아예 callback이 안됨 (서버에서는 값을 재대로 전달하고있음)
class ExerciseRepository {

    private val TAG: String = javaClass.simpleName

    companion object {
        private var exerciseUpperArray: ArrayList<ExerciseData>? = null
        private var exerciseUpperLiveArray: MutableLiveData<ArrayList<ExerciseData>> =
            MutableLiveData()
        private var exerciseLowerArray: ArrayList<ExerciseData>? = null
        private var exerciseLowerLiveArray: MutableLiveData<ArrayList<ExerciseData>>? =
            MutableLiveData()
        private var exerciseLoinsArray: ArrayList<ExerciseData>? = null
        private var exerciseLoinsLiveArray: MutableLiveData<ArrayList<ExerciseData>>? =
            MutableLiveData()
        private var exerciseBodyArray: ArrayList<ExerciseData>? = null
        private var exerciseBodyLiveArray: MutableLiveData<ArrayList<ExerciseData>>? =
            MutableLiveData()


        private var exercisePartArray:ArrayList<ExerciseData>?=null
        //레트로핏을 이용한 서버에 요청하기 위한 객체
        private val requestServer =
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

//어째 메소드화 시켜 코드를 줄이면 작동안함 이유가 무엇인지 모르겠음
//    private fun exercisePartCallback(part: String):ArrayList<ExerciseData>? {
//        var requestExercisePartData: Call<ArrayList<ExerciseData>>? =
//            requestServer!!.getExercisePartData(part)
//        if (requestExercisePartData != null){
//            requestExercisePartData.enqueue(object :Callback<ArrayList<ExerciseData>>{
//                override fun onResponse(
//                    call: Call<ArrayList<ExerciseData>>,
//                    response: Response<ArrayList<ExerciseData>>
//                ) {
//                    Log.d(TAG, "응답코드" + response.code())
//                    //응답을 성공적으로 받았을 시 200번 신호 수신시
//                    if (response.isSuccessful) {
//                        //서버에서 받은 json데이터 exercisePartArray에 삽입
//                        exercisePartArray = response.body()!!
//                    }
//                }
//
//                override fun onFailure(call: Call<ArrayList<ExerciseData>>, t: Throwable) {
//                    Log.d(TAG, "응답코드" + t.message)
//                }
//
//            })
//
//        }
//        return exercisePartArray
//    }


    //상체 부위 운동 조회
    fun getExerciseUpper(): MutableLiveData<ArrayList<ExerciseData>> {
        //exericseUpperArray의 객체가 생성되어있지 않은경우
        if (exerciseUpperArray == null) {
            var exerciseUppers: Call<ArrayList<ExerciseData>>? =
                requestServer.getExercisePartData("upper")               //upper부위에 대한 파라미터 정의 (where E_part = upper)
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

    /**
     *데이터 재사용과 생명주기에 의한 빨리 클릭시 화면갱신이 안되는 문제 발생
     * 1안 - 전역변수로 사용중인 객체를 초기화후 재생성한다(이러면 성능하락의 문제가 발생될듯) = 실패
     * 2안 - 전역변수를 각각의 부위별로 추가하여 생성후 사용 LiveData만 한게 아니라 ArrayList를 추가해야함 = 실패
     * 3안 - xml재사용으로 인한 부작용 또는 viewHolder의 부작용... 이건 아닐듯 = 확인결과 아님
     * */

//다리 부위 운동 조회
    fun getExerciseLower(): MutableLiveData<ArrayList<ExerciseData>>? {
        //1안
        //둘다 null이거나 둘중에 하나가 null이거나
//        if ((exercisePartArray == null && exerciseLivePartArray == null) || (exercisePartArray == null || exerciseLivePartArray == null)){
//            exercisePartArray = ArrayList()
//            exerciseLivePartArray = MutableLiveData()
//            return exercisePartCallBack("lower")
//        }else {
//            return exercisePartCallBack("lower")
//        }

        //2안
//        exerciseLowerLiveArray!!.value = exercisePartCallback("lower")
//        return exerciseLowerLiveArray


        //원본코드
        if (exerciseLowerArray == null) {
            var exerciseLowers: Call<ArrayList<ExerciseData>>? =
                requestServer.getExercisePartData("lower")               //upper부위에 대한 파라미터 정의 (where E_part = upper)
            //retrofit의 call 객체가 생성되어있을 경우
            if (exerciseLowers != null) {
                exerciseLowers.enqueue(object : Callback<ArrayList<ExerciseData>> {
                    override fun onResponse(
                        call: Call<ArrayList<ExerciseData>>,
                        response: Response<ArrayList<ExerciseData>>
                    ) {
                        Log.d(TAG, "응답코드" + response.code())
                        //응답을 성공적으로 받았을 시 200번 신호 수신시
                        if (response.isSuccessful) {
                            //json데이터 exerciseUpperArray에 삽입
                            exerciseLowerArray = response.body()!!
                            //exerciseUpperArray의 데이터 exerciseUpperLiveArray에 삽입
                            exerciseLowerLiveArray?.value = exerciseLowerArray
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<ExerciseData>>, t: Throwable) {
                        Log.d(TAG, "응답코드" + t.message)
                    }

                })
            }
            return exerciseLowerLiveArray
        } else {
            return exerciseLowerLiveArray
        }
    }

    //전신 운동 조회
    fun getExerciseBody(): MutableLiveData<ArrayList<ExerciseData>>? {
        //원본코드
        if (exerciseBodyArray == null) {
            var exerciseBodies: Call<ArrayList<ExerciseData>>? =
                requestServer.getExercisePartData("body")               //upper부위에 대한 파라미터 정의 (where E_part = upper)
            //retrofit의 call 객체가 생성되어있을 경우
            if (exerciseBodies != null) {
                exerciseBodies.enqueue(object : Callback<ArrayList<ExerciseData>> {
                    override fun onResponse(
                        call: Call<ArrayList<ExerciseData>>,
                        response: Response<ArrayList<ExerciseData>>
                    ) {
                        Log.d(TAG, "응답코드" + response.code())
                        //응답을 성공적으로 받았을 시 200번 신호 수신시
                        if (response.isSuccessful) {
                            //json데이터 exerciseUpperArray에 삽입
                            exerciseBodyArray = response.body()!!
                            //exerciseUpperArray의 데이터 exerciseUpperLiveArray에 삽입
                            exerciseBodyLiveArray?.value = exerciseBodyArray
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<ExerciseData>>, t: Throwable) {
                        Log.d(TAG, "응답코드" + t.message)
                    }

                })
            }
            return exerciseBodyLiveArray
        } else {
            return exerciseBodyLiveArray
        }
    }


    //허리운동 조회
    fun getExerciseLoins(): MutableLiveData<ArrayList<ExerciseData>>? {
        //원본코드
        if (exerciseLoinsArray == null) {
            var exerciseLoins: Call<ArrayList<ExerciseData>>? =
                requestServer.getExercisePartData("loins")               //upper부위에 대한 파라미터 정의 (where E_part = upper)
            //retrofit의 call 객체가 생성되어있을 경우
            if (exerciseLoins != null) {
                exerciseLoins.enqueue(object : Callback<ArrayList<ExerciseData>> {
                    override fun onResponse(
                        call: Call<ArrayList<ExerciseData>>,
                        response: Response<ArrayList<ExerciseData>>
                    ) {
                        Log.d(TAG, "응답코드" + response.code())
                        //응답을 성공적으로 받았을 시 200번 신호 수신시
                        if (response.isSuccessful) {
                            //json데이터 exerciseUpperArray에 삽입
                            exerciseLoinsArray = response.body()!!
                            //exerciseUpperArray의 데이터 exerciseUpperLiveArray에 삽입
                            exerciseLoinsLiveArray?.value = exerciseLoinsArray
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<ExerciseData>>, t: Throwable) {
                        Log.d(TAG, "응답코드" + t.message)
                    }

                })
            }
            return exerciseLoinsLiveArray
        } else {
            return exerciseLoinsLiveArray
        }
    }

//객체 초기화
//LiveData를 공용으로 사용됨에 따라
//이 문제를 해결하기 위해 객체들을 초기화시킴
//    fun dataReset(){
//        exercisePartArray = null
//        exerciseLivePartArray = null
//    }
}
