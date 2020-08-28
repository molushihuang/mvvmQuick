package com.xqd.mylibrary.base;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xqd.mylibrary.R;
import com.xqd.mylibrary.utlis.ActivityUtil;
import com.xqd.mylibrary.utlis.AppUtil;


/**
 * Created by 谢邱东 on 2020/7/31 16:17.
 * NO bug
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected AppCompatActivity mActivity;
    protected LinearLayout llTitleMain;
    protected LinearLayout llTitle;
    private ProgressDialog progressDialog;
    private InputMethodManager imm;
    @Override
    protected void onStart() {
        super.onStart();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setContentView());
        mActivity = this;
        ActivityUtil.Companion.get().addActivity(mActivity);
        llTitleMain = findViewById(R.id.title_main_view);
        llTitle = findViewById(R.id.llTitle);
        initBarView();
        registerBroad();
    }

    protected abstract int setContentView();

    /**
     * 获取状态栏高度
     */
    public void initBarView() {
        if (llTitle != null) {
            llTitle.getLayoutParams().height = AppUtil.getStatusBarHeight(this);
            llTitle.setVisibility(View.VISIBLE);
            try {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) llTitleMain.getLayoutParams();
                layoutParams.height = AppUtil.getStatusBarHeight(this) + getResources().getDimensionPixelSize(R.dimen.app_title_h_new);
                llTitleMain.setLayoutParams(layoutParams);
            } catch (Exception e) {
                e.printStackTrace();

            }

        }
    }


    protected void startActivity(Class<?> gotoActivty) {
        startActivity(new Intent(mActivity, gotoActivty));
    }

    public void shortToast(final String content) {
        if (isRunning()) {
            try {
                Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void showLoad(String string) {
        if (mActivity == null || mActivity.isDestroyed() || mActivity.isFinishing()) {
            return;
        }
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage(string);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    protected void showLoad(String string, boolean cancelable) {
        if (mActivity == null || mActivity.isDestroyed() || mActivity.isFinishing()) {
            return;
        }
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage(string);
        progressDialog.setCancelable(cancelable);
        progressDialog.show();
    }

    protected void showLoadHorizontal() {
        if (mActivity == null || mActivity.isDestroyed() || mActivity.isFinishing()) {
            return;
        }
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = new ProgressDialog(mActivity);
//        progressDialog.setMessage(string);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
    }

    public void dismissload() {
        if (isRunning() && progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void showLoad(DialogBean bean) {
        if (bean.isShow()) {
            showLoad(bean.getMsg());
        } else {
            dismissload();
        }
    }

    protected boolean isRunning() {
        if (mActivity == null || mActivity.isDestroyed() || mActivity.isFinishing()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 隐藏软键盘
     */
    public void saveEditTextAndCloseIMM() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        return;
    }

    /**
     * 打开软键盘
     */
    protected void openEditTextIMM() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 广播部分
     */
    IntentFilter filter;
    private boolean registTag = false;

    //注册广播
    public void registerBroad() {
        String[] actions = filterActions();
        if (actions == null || actions.length == 0) {
            return;
        }

        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        for (String action : actions) {
            filter.addAction(action);
        }
        filter.addCategory(getPackageName());
        registerReceiver(receiver, filter);
        registTag = true;
    }

    //注销广播
    public void unRegister() {
        if (filter != null && registTag) {
            unregisterReceiver(receiver);
            filter = null;
            registTag = false;
        }
    }

    public String[] filterActions() {
        return null;
    }

    public void onReceive(Context context, Intent intent) {

    }

    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            BaseActivity.this.onReceive(context, intent);
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegister();
        ActivityUtil.Companion.get().removeActivity(mActivity);
    }
}
