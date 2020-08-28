package com.xqd.mylibrary.base

import androidx.lifecycle.MutableLiveData

/**
 * Created by 谢邱东 on 2020/8/8 14:47.
 * NO bug
 */
class DialogLiveData<T> : MutableLiveData<T>() {

    fun setValue(msg: String) {
        value = DialogBean(true, msg) as T
    }

    fun setValue(isShow: Boolean) {
        value = DialogBean(isShow, "") as T
    }
}