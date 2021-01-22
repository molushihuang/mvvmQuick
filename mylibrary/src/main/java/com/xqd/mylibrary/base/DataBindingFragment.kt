package com.xqd.mylibrary.base

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 * Created by 谢邱东 on 2020/8/28 10:13.
 * NO bug
 */
 abstract class DataBindingFragment<DB : ViewDataBinding> : Fragment() {

    protected lateinit var dataBinding: DB
    protected lateinit var mActivity: AppCompatActivity
    protected var first = true
    private var progressDialog: ProgressDialog? = null
    private var registTag = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, setContentView(), container, false)
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mActivity = (activity as AppCompatActivity?)!!
        if (first) {
            first = false
            dataBinding.lifecycleOwner = this
            registerBroad()
            getViewModel()
            initView()
            initData()
        }
    }

    protected abstract fun setContentView(): Int

    open fun getViewModel() {}

    /**
     * 初始化视图
     */
    open fun initView(){}

    /**
     * 初始化数据
     */
    open fun initData(){}

    fun shortToast(content: String?) {
        try {
            if(content.isNullOrEmpty()){
                Toast.makeText(mActivity, content, Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected open fun startActivity(gotoActivity: Class<*>?) {
        startActivity(Intent(mActivity, gotoActivity))
    }

    protected open fun showLoad(string: String?) {
        showLoad(string, false)
    }

    protected open fun showLoad(string: String?, cancelable: Boolean) {
        if (mActivity == null || mActivity.isDestroyed || mActivity.isFinishing) {
            return
        }
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
        progressDialog = ProgressDialog(mActivity)
        progressDialog!!.setMessage(string)
        progressDialog!!.setCancelable(cancelable)
        progressDialog!!.show()
    }

    protected open fun changeLoad(string: String?) {
        if (mActivity == null || mActivity.isDestroyed || mActivity.isFinishing) {
            return
        }
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.setMessage(string)
        } else {
            progressDialog = ProgressDialog(mActivity)
            progressDialog!!.setMessage(string)
            progressDialog!!.setCancelable(false)
            progressDialog!!.show()
        }
    }

    protected open fun showLoadHorizontal() {
        if (mActivity == null || mActivity.isDestroyed || mActivity.isFinishing) {
            return
        }
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
        progressDialog = ProgressDialog(mActivity)
        //        progressDialog.setMessage(string);
        progressDialog!!.setCancelable(false)
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog!!.show()
    }

    open fun dismissload() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
    }

    open fun showLoad(bean: DialogBean) {
        if (bean.isShow) {
            showLoad(bean.msg)
        } else {
            dismissload()
        }
    }

    /**
     * 广播部分
     */
    var filter: IntentFilter? = null

    var receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            this@DataBindingFragment.onReceive(context, intent)
        }
    }

    open fun filterActions(): Array<String>? {
        return null
    }

    open fun onReceive(context: Context?, intent: Intent?) {}

    open fun registerBroad() {
        if (mActivity != null) {
            val actions = filterActions()
            if (actions == null || actions.size == 0) {
                return
            }
            filter = IntentFilter()
            filter!!.addCategory(mActivity.packageName)
            for (action in actions) {
                filter!!.addAction(action)
            }
            mActivity.registerReceiver(receiver, filter)
            registTag = true
        }
    }


    open fun unRegister() {
        if (mActivity != null && filter != null && registTag) {
            mActivity.unregisterReceiver(receiver)
            registTag = false
            filter = null
        }
    }

    override fun onDestroy() {
        unRegister()
        super.onDestroy()
    }

}