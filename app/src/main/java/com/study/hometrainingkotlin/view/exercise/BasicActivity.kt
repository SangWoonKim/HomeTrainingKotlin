package com.study.hometrainingkotlin.view.exercise

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.study.hometrainingkotlin.R
import com.study.hometrainingkotlin.model.externalrepository.vo.ExerciseData
import com.study.hometrainingkotlin.model.localrepository.room.dao.ExerciseListEntity
import com.study.hometrainingkotlin.view.adapter.ExerciseAdapter
import com.study.hometrainingkotlin.viewmodel.ExerciseViewModel


//각 운동선택 액티비티의 코드량을 줄이기 위한 인터페이스

interface BasicActivity {
    var adapter: ExerciseAdapter?
    val viewModel: ExerciseViewModel?
    var listView: RecyclerView?

    //어뎁터 연결 및 이벤트 정의 메소드
    fun setAdapterAndEvent(recyclerView: RecyclerView?,
                           exercisePartData: ArrayList<ExerciseData>?,
                           context: Context,
                           application: Application) {
        if (adapter == null) {
            adapter = ExerciseAdapter(exercisePartData!!,
                    object : ExerciseAdapter.OnItemClickListener {
                        override fun onItemClick(v: View, position: Int) {
                            Log.d("listPostition", "" + position)
                            val dialog = AlertDialog.Builder(context)
                            val inflater = LayoutInflater.from(context)
                            val view = inflater.inflate(R.layout.dialog_view_exercise_body, null)
                            //리스트뷰에서 선택한 아이템의 인덱스 값 저장
                            val clickItem = exercisePartData!!.get(position)
                            // view를 참조하여 imageView객체 생성
                            val dialog_image = view.findViewById<ImageView>(R.id.image_dialog_body)

                            // 이미지 이름 가져오기(db에 이미지의 이름이 명시되어있음)
                            val dbPath: String = clickItem.E_image
                            // 이미지를 불러올 리소스 파일이 담긴 폴더명 명시
                            val type: String = "drawable"
                            // 패키지 명 반환
                            val packageName: String = application.packageName

                            //(dbPath,type,packageName)을 이용하여 리소스ID값 얻기
                            val imagePath: Int = application.resources.getIdentifier(
                                    dbPath,
                                    type,
                                    packageName
                            )
                            //해당 리소스ID의 이미지를 이용하여 imageView에 set
                            dialog_image.setImageResource(imagePath)

                            //dialog객체에 뷰를 정의
                            dialog.setView(view)
                                    .setNeutralButton("취소", object : DialogInterface.OnClickListener {
                                        override fun onClick(dialog: DialogInterface?, which: Int) {
                                            //아무것도 명시를 하지않으면 밖으로 빠져나옴
                                        }

                                    })
                                    .setPositiveButton("추가", object : DialogInterface.OnClickListener {
                                        override fun onClick(dialog: DialogInterface?, which: Int) {
                                            //클릭한 아이템의 속성을 ExerciseListEntity에 넣어 인스턴스화하여 매개변수로 사용
                                            viewModel!!.insertListItem(
                                                ExerciseListEntity(
                                                    clickItem.E_part,
                                                    clickItem.E_name,
                                                    Integer.parseInt(clickItem.E_setcal),
                                                    clickItem.E_image,
                                                    clickItem.E_imageorg,
                                                    clickItem.E_activeimg,
                                                    clickItem.E_activeimg2
                                                )
                                            )
                                        }

                                    }).create().show()
                        }
                    })
            listView!!.layoutManager = LinearLayoutManager(context)
            listView!!.adapter = adapter
        }else{
            adapter!!.notifyDataSetChanged()
        }
    }
}