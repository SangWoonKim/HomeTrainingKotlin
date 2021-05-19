package com.study.hometrainingkotlin.view.kakao

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.study.hometrainingkotlin.R
import com.study.hometrainingkotlin.model.kakao.vo.Documents

class KakaoDetailActivity : AppCompatActivity() {
    private var TV_Kakao_Detail_Name:TextView?=null
    private var TV_Kakao_Detail_Address:TextView?=null
    private var TV_Kakao_Detail_Homepage:TextView?=null
    private var TV_Kakao_Detail_Number:TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kakao_detail_activity)
        var getData: Intent = intent
        var detailData:Documents = intent.getParcelableExtra("detailSearch")

        Log.d("detailData", detailData.documents.get(0).place_name)

        TV_Kakao_Detail_Name = findViewById<View>(R.id.TV_Kakao_Detail_Name) as TextView
        TV_Kakao_Detail_Address = findViewById<View>(R.id.TV_Kakao_Detail_Address) as TextView
        TV_Kakao_Detail_Homepage = findViewById<View>(R.id.TV_Kakao_Detail_Homepage) as TextView
        TV_Kakao_Detail_Number = findViewById<View>(R.id.TV_Kakao_Detail_Number) as TextView

        TV_Kakao_Detail_Name!!.text = detailData.documents.get(0).place_name
        TV_Kakao_Detail_Address!!.text = detailData.documents.get(0).address_name
        TV_Kakao_Detail_Homepage!!.text =detailData.documents.get(0).place_url
        TV_Kakao_Detail_Number!!.text =detailData.documents.get(0).phone
    }
}