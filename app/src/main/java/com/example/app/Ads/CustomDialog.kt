package com.example.app
import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager


class  CustomDialog private constructor() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var dialogAAFontsUtility: CustomDialog? = null
        var dialog: Dialog? = null
            private set
        @SuppressLint("StaticFieldLeak")
        private var context: Activity? = null
        fun getInstance(con: Activity?): CustomDialog? {
            context = con
            return if (dialogAAFontsUtility == null) {
                dialogAAFontsUtility = CustomDialog()
                dialogAAFontsUtility
            } else {
                dialogAAFontsUtility
            }
        }
    }
    fun setContentView(view: View?, isCancelable: Boolean,widthPercent:Float=0.85f): CustomDialog? {
        context?.let {
            dialog = Dialog(it)
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(view!!)
            dialog?.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.setCancelable(isCancelable)
            val displayMetrics = DisplayMetrics()
            it.windowManager.defaultDisplay.getMetrics(displayMetrics)
            val height = displayMetrics.heightPixels
            val width = displayMetrics.widthPixels
            /* dialog!!.window!!.attributes.windowAnimations = R.style.dialogueStyle*/
         dialog?.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
            return dialogAAFontsUtility
        }
        return null
    }

    fun showDialog(): Dialog? {
        dialog?.let {
            if(!it.isShowing)
                dialog?.show()
        }
        return dialog
    }

    fun dismissDialog() {
        dialog?.let {
            if (context?.isFinishing==false && it.isShowing) {
                it.dismiss()
            }
        }
    }
}