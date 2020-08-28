package com.xqd.mylibrary.base

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer

/**
 * Created by 谢邱东 on 2020/8/28 10:36.
 * NO bug
 */
abstract class ModelBindingFragment<VM : BaseViewModel, DB : ViewDataBinding> : DataBindingFragment<DB>() {

    protected lateinit var viewModel: VM

    override fun getViewModel() {
        viewModel = initViewModel()

        viewModel.getShowDialog(this,
            Observer { dialogBean -> showLoad(dialogBean) })
        viewModel.getError(this,
            Observer { o -> shortToast(o.toString()) })
    }

    protected abstract fun initViewModel(): VM

}