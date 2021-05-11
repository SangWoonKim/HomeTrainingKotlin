package com.study.hometrainingkotlin.view.kakao

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.study.hometrainingkotlin.R
import com.study.hometrainingkotlin.model.kakao.vo.Documents

class KakaoDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kakao_detail_activity)
        var getData: Intent = intent
        var detailData:Documents = intent.getParcelableExtra("detailSearch")
        Log.d("detailData", detailData.documents.get(0).place_name)
    }
}