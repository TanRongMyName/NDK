package com.coffe.shentao.mvp.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.coffe.shentao.mvp.presenter.BasePresenter;

public abstract class BaseActivity <V,T extends BasePresenter<V>> extends Activity {
    //表示层的 引用
    public T girlPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        girlPresenter=createPresenter();
        girlPresenter.attachView((V) this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        girlPresenter.detachView();
    }
    //异常退出的时候 --解绑
    //提供给子类 填充 presenter
    protected abstract T createPresenter();
}
