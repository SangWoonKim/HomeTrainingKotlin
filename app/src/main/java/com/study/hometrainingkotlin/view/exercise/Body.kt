package com.study.hometrainingkotlin.view.exercise

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.study.hometrainingkotlin.R
import com.study.hometrainingkotlin.model.externalrepository.vo.ExerciseData
import com.study.hometrainingkotlin.model.localrepository.room.dao.ExerciseListEntity
import com.study.hometrainingkotlin.view.exercise.adapter.ExerciseAdapter
import com.study.hometrainingkotlin.viewmodel.ExerciseViewModel

class Body : AppCompatActivity() {

    private var recyclerView:RecyclerView ?= null
    private var exerciseAdapter:ExerciseAdapter ?= null
    private var upperList : ArrayList<ExerciseData> ?= null
    //뷰모델 DI 의존성 주입
    private val exerciseViewModel : ExerciseViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.body_activity)
        recyclerView=findViewById(R.id.bodylist)

        exerciseViewModel.getUppers().observe(this,
            { changeData ->   //observing즉 데이터가 바뀌었을시 changeData에 저장됨 (람다식사용)
                upperList = changeData   //adapter를 생성하기 위한 배열요소 upperList에 바뀐데이터 갱신
                setAdapterAndEvent()
//                exerciseAdapter!!.updateData(changeData)
            })
    }

    //어뎁터 연결및 클릭 이벤트 정의 메소드
    private fun setAdapterAndEvent(){
        if (exerciseAdapter == null){
            exerciseAdapter = ExerciseAdapter(upperList!!, object :
                    ExerciseAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    Log.d("listPostition", "" + position)
                    //다이얼로그 객체 생성
                    val dialog = AlertDialog.Builder(this@Body)
                    //화면에 표시할 inflater생성
                    val inflater = LayoutInflater.from(this@Body)
                    // view 생성
                    val view = inflater.inflate(R.layout.dialog_view_exercise_body, null)
                    //리스트뷰에서 선택한 아이템의 인덱스 값 저장
                    val clickItem = upperList!!.get(position)
                    // view를 참조하여 imageView객체 생성
                    val dialog_image = view.findViewById<ImageView>(R.id.image_dialog_body)

                    // 이미지 이름 가져오기(db에 이미지의 이름이 명시되어있음)
                    val dbPath: String = clickItem.E_image
                    // 이미지를 불러올 리소스 파일이 담긴 폴더명 명시
                    val type: String = "drawable"
                    // 패키지 명 반환
                    val packageName: String = application.packageName

                    //(dbPath,type,packageName)을 이용하여 리소스ID값 얻기
                    val imagePath: Int = application.resources.getIdentifier(dbPath, type, packageName)
                    //해당 리소스ID의 이미지를 이용하여 imageView에 set
                    dialog_image.setImageResource(imagePath)

                    //dialog객체에 뷰를 정의
                    dialog.setView(view)
                            .setNeutralButton("취소", object : DialogInterface.OnClickListener{
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    //아무것도 명시를 하지않으면 밖으로 빠져나옴
                                }

                            })
                            .setPositiveButton("추가",object : DialogInterface.OnClickListener{
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    //클릭한 아이템의 속성을 ExerciseListEntity에 넣어 인스턴스화하여 매개변수로 사용
                                    exerciseViewModel.insertListItem(ExerciseListEntity(
                                            clickItem.E_part,
                                            clickItem.E_name,
                                            Integer.parseInt(clickItem.E_setcal),
                                            clickItem.E_image,
                                            clickItem.E_imageorg,
                                            clickItem.E_activeimg,
                                            clickItem.E_activeimg2
                                    ))
                                }

                            })
                }
            })
            recyclerView!!.layoutManager = LinearLayoutManager(this)
            recyclerView!!.adapter = exerciseAdapter
        }else{
            exerciseAdapter!!.notifyDataSetChanged()
        }
    }
}