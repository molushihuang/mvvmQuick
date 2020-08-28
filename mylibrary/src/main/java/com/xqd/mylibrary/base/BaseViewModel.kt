package com.xqd.mylibrary.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


/**
 * Created by 谢邱东 on 2020/8/4 14:11.
 * NO bug
 */
open class BaseViewModel : ViewModel() {

    /**
     * 管理RxJava请求
     */
    private var compositeDisposable: CompositeDisposable? = null

    var error = MutableLiveData<Any>()
    var errorCode = MutableLiveData<Int>()
    var showDialog = DialogLiveData<DialogBean>()

    /**
     * 添加 rxJava 发出的请求
     */
    protected fun addDisposable(disposable: Disposable?) {
        if (compositeDisposable == null || compositeDisposable!!.isDisposed) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable!!.add(disposable!!)
    }

    fun getError(owner: LifecycleOwner, observer: Observer<Any>) {
        error.observe(owner, observer)
    }

    fun getShowDialog(owner: LifecycleOwner, observer: Observer<DialogBean>) {
        showDialog.observe(owner, observer)
    }

    /**
     * ViewModel销毁同时也取消请求
     */
    override fun onCleared() {
        super.onCleared()
        if (compositeDisposable != null) {
            compositeDisposable!!.dispose()
            compositeDisposable = null
        }

    }
}