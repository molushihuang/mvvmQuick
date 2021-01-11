package com.xqd.mylibrary.utlis

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

object ToastUtil {

    private var sToast: Toast? = null

    fun init(context: Context) {
        if (sToast == null) {
            sToast = Toast.makeText(context, "", Toast.LENGTH_SHORT)
        }
    }

    fun showToast(@StringRes resId: Int) {
        show(resId, Toast.LENGTH_SHORT)
    }

    fun showToast(`object`: Any?) {
        show(`object`, Toast.LENGTH_SHORT)
    }

    fun showToastLong(@StringRes resId: Int) {
        show(resId, Toast.LENGTH_LONG)
    }

    fun showToastLong(`object`: Any?) {
        show(`object`, Toast.LENGTH_LONG)
    }



    private fun show(@StringRes resId: Int, length: Int) {
        check()
        sToast!!.setText(resId)
        sToast!!.duration = length
        sToast!!.show()
    }

    private fun show(`object`: Any?, length: Int) {
        check()
        if (`object` != null) {
            sToast!!.setText(`object`.toString())
            sToast!!.duration = length
            sToast!!.show()
        }
    }

    private fun check() {
        if (sToast == null) {
            throw IllegalStateException("you must call ToastUtil.init(context) first")
        }
    }


}


