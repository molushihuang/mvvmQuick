package com.xqd.mylibrary.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;

/**
 * Created by 谢邱东 on 2020/8/8 15:18.
 * NO bug
 */

public abstract class ModelBindingActivity<VM extends BaseViewModel, DB extends ViewDataBinding> extends DataBindingActivity<DB> {

    protected VM viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = initViewModel();
        initObserve();
    }

    /**
     * 初始化ViewModel
     */
    protected abstract VM initViewModel();

    private void initObserve() {
        if (viewModel != null) {
            viewModel.getShowDialog(this, new Observer<DialogBean>() {
                @Override
                public void onChanged(DialogBean dialogBean) {
                    showLoad(dialogBean);
                }
            });

            viewModel.getError(this, new Observer<Object>() {
                @Override
                public void onChanged(Object o) {
                    if(o!=null){
                        shortToast(o.toString());
                    }
                }
            });
        }
    }

}
