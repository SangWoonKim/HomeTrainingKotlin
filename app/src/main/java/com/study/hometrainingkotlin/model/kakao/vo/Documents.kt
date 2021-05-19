package com.study.hometrainingkotlin.model.kakao.vo

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Documents(
    var documents: @RawValue ArrayList<Document>
):Parcelable

@Parcelize
data class Document(
    val address_name: String,
    val category_group_code: String,
    val category_group_name: String,
    val category_name: String,
    val distance: String,
    val id: String,
    val phone: String,
    val place_name: String,
    val place_url: String,
    val road_address_name: String,
    val x: Double,
    val y: Double
):Parcelable