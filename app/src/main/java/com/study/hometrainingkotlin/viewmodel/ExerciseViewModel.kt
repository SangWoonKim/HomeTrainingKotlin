package com.study.hometrainingkotlin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.study.hometrainingkotlin.model.externalrepository.repository.ExerciseRepository
import com.study.hometrainingkotlin.model.externalrepository.vo.ExerciseData
import com.study.hometrainingkotlin.model.kakao.repository.KakaoRepository
import com.study.hometrainingkotlin.model.kakao.vo.Documents
import com.study.hometrainingkotlin.model.localrepository.room.dao.ExerciseListEntity
import com.study.hometrainingkotlin.model.localrepository.room.repository.ExerciseListRepository
import com.study.hometrainingkotlin.model.localrepository.room.repository.ExerciseMyselfRepository
import com.study.hometrainingkotlin.model.localrepository.room.repository.ExerciseSumCalRepository
import com.study.hometrainingkotlin.model.localrepository.room.util.ExerciseMyselfEntity
import com.study.hometrainingkotlin.model.localrepository.room.util.ExerciseSumCalEntity

//뷰모델 (굳이 쓸 필요가 없으나 이번기회에 사용해봄)
//데이터를 조회하는 것 밖에 안하기 때문에 서버에서 값이 달라지지 않는 한 바뀌지 않음
//만약 서버에서 값이 달라지는 것을 알려면 사용하려는 메소드를 재호출해야함(ex)Thread사용)
class ExerciseViewModel(application: Application): AndroidViewModel(application) {
    //ExerciseRepository에 접근할 수 있는 인스턴스를 저장
    private val exerciseRepository: ExerciseRepository = ExerciseRepository.getInstance()!!

    //
    private val exerciseListRepository: ExerciseListRepository by lazy {
        ExerciseListRepository(application)
    }
    private val exerciseMyselfRepository:ExerciseMyselfRepository by lazy {
        ExerciseMyselfRepository(application)
    }
    private val exerciseSumCalRepository:ExerciseSumCalRepository by lazy {
        ExerciseSumCalRepository(application)
    }
    private val kakaoRepository:KakaoRepository = KakaoRepository.getInstance()!!

    /**
     * 운동선택에서 사용되는 메소드
     * */
    //Upper
    fun getUppers() : MutableLiveData<ArrayList<ExerciseData>> {
        return exerciseRepository.getExerciseUpper()
    }
    //Lower
    fun getLowers(): MutableLiveData<ArrayList<ExerciseData>>? {
        return exerciseRepository.getExerciseLower()
    }
    //Body
    fun getBodies(): MutableLiveData<ArrayList<ExerciseData>>? {
        return exerciseRepository.getExerciseBody()
    }
    //Loins
    fun getLoins() : MutableLiveData<ArrayList<ExerciseData>>? {
        return exerciseRepository.getExerciseLoins()
    }


    /**
     * 운동목록에서 사용되는 메소드
     * */
    //운동목록 item조회메소드
    fun selectListItem(): LiveData<List<ExerciseListEntity>> {
        return exerciseListRepository.selectList()
    }

    // 운동목록 item 삽입메소드
    fun insertListItem(exerciseListEntity:ExerciseListEntity){
        return exerciseListRepository.listInsert(exerciseListEntity)
    }

    // 운동목록 item 삭제메소드
    fun deleteListItem(exerciseListEntity: ExerciseListEntity){
        return exerciseListRepository.deleteListItem(exerciseListEntity)
    }

    // 운동목록 item 전체 삭제메소드
    fun deleteAllListItem(){
        return exerciseListRepository.deleteAllListItem()
    }

    //운동목록에 있는 item들의 총 칼로리소모량을 출력하는 메소드
    fun sumCalListItem():LiveData<Int>{
        return exerciseSumCalRepository.sumCalListItem()
    }

    //운동목록에 있는 item리스트들의 정보 일부를 exerciseMyself에 삽입하는 메소드
    fun insertMyself(exerciseMyselfEntity: ArrayList<ExerciseMyselfEntity>){
        return exerciseMyselfRepository.listInsert(exerciseMyselfEntity)
    }
    //운동목록에 있는 item들의 총칼로리 값과 오늘의 날짜를 exercisesumcal테이블에 삽입하는 메소드
    fun insertSumCal(exerciseSumCalEntity:ExerciseSumCalEntity) {
        return exerciseSumCalRepository.sumCalInsert(exerciseSumCalEntity)
    }


    /**
     * 나 자신과의 싸움에서 사용되는 메소드
     * */
    //나자신과의 싸움에서 join하여 출력되는 메소드
    fun getSumCal():LiveData<List<ExerciseSumCalEntity>>{
        return exerciseSumCalRepository.selectSumCal()
    }

    //나 자신과의 싸움에서 Bar클릭시 호출되는 메소드
    fun getMyselfDetail():LiveData<List<ExerciseMyselfEntity>>{
        return exerciseMyselfRepository.selectMyselfDetail()
    }


    /**
     * 카카오맵에서 사용되는 메소드
     * */

    fun getSearchResult(x:String,y:String):Documents?{
        return kakaoRepository.getGymSearchResult(x,y)
    }

}

