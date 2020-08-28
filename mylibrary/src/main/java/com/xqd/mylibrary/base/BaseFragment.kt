package com.xqd.mylibrary.base

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.xqd.mylibrary.R
import com.xqd.mylibrary.utlis.AppUtil


/**
 * Created by 谢邱东 on 2020/7/30 17:29.
 * NO bug
 */
open class BaseFragment : Fragment() {
    private var registTag = false
    protected var first = true
    protected lateinit var mActivity: AppCompatActivity
    protected var llTitle: LinearLayout? = null
    protected var mView: View? = null
    private var progressDialog: ProgressDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActivity = (activity as AppCompatActivity?)!!
        llTitle = mView!!.findViewById(R.id.llTitle)
        registerBroad()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mActivity = (activity as AppCompatActivity?)!!
        //        initBarView()
    }

    open fun initBarView() {
        if (llTitle != null) {
            llTitle!!.layoutParams.height = AppUtil.getStatusBarHeight(mActivity)
            llTitle!!.visibility = View.VISIBLE
        }
    }

    /**
     * Short toast.
     *
     * @param content
     */
    fun shortToast(content: String?) {
        try {
            Toast.makeText(mActivity, content, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected open fun startActivity(gotoActivity: Class<*>?) {
        startActivity(Intent(mActivity, gotoActivity))
    }


    protected open fun showLoad(string: String?) {
        if (mActivity == null || mActivity.isDestroyed || mActivity.isFinishing) {
            return
        }
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
        progressDialog = ProgressDialog(mActivity)
        progressDialog!!.setMessage(string)
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
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
            this@BaseFragment.onReceive(context, intent)
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