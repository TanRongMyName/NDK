package com.coffe.shentao.mvp.presenter;

import java.lang.ref.WeakReference;

public class BasePresenter <T>{
    protected WeakReference<T> viewReference;
    //进行绑定
    public void attachView(T view){
        viewReference=new WeakReference<>(view);
    }

    //进行解绑
    public void detachView(){
        viewReference.clear();
    }
}
