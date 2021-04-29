package com.study.hometrainingkotlin.view.SelectFragment.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.study.hometrainingkotlin.viewmodel.ExerciseViewModel

class ListCalSumDialog : DialogFragment() {
    private var builder:AlertDialog.Builder?=null
    private var calText:Int =0
    private val viewModel: ExerciseViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            // Use the Builder class for convenient dialog construction
            builder = AlertDialog.Builder(it)
            builder!!.setMessage(calText.toString())
                .setTitle("칼로리 계산")
                .setPositiveButton("초기화")
                { dialog, id ->

                }
                .setNegativeButton("취소")
                { dialog, id ->

                }
            builder!!.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData(viewModel)

    }

    private fun observeData(viewModel: ExerciseViewModel) {
        viewModel.sumCalListItem().observe(viewLifecycleOwner, {
                changeData -> calText = changeData
            //갱신할 뷰가 없다.. 커스텀으로 만들어서 invaildate를 넣어야하나.... 흠...
            view?.invalidate()
        })
    }
}
