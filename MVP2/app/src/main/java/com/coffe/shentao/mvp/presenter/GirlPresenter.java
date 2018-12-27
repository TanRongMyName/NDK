package com.coffe.shentao.mvp.presenter;

import android.view.View;

import com.coffe.shentao.mvp.bean.Girl;
import com.coffe.shentao.mvp.model.GirlModelImpl;
import com.coffe.shentao.mvp.model.IGirlModel;
import com.coffe.shentao.mvp.view.IGirlView;

import java.lang.ref.WeakReference;
import java.util.List;
//解决内存泄漏  同时优化MVP 不用 每个 Activity 对应 View Presenter
//绑定activity
public class GirlPresenter<T extends IGirlView> {
    //解决内存泄漏   使用弱引用
    protected WeakReference<T> mViewRef;
    //获取 Activity 的对象   并且实现 数据的model
    //将数据填充完事
    IGirlView girlView;
    IGirlModel girlModel;
    //绑定activity 生命周期的方法
    public GirlPresenter() {
        girlModel=new GirlModelImpl();
    }
    //弱引用使用的方法
//    public GirlPresenter(T girlView) {
//        mViewRef=new WeakReference<>(girlView);
//        girlModel=new GirlModelImpl();
//    }
//    public GirlPresenter(T girlView) {
//        this.girlView = girlView;
//        girlModel=new GirlModelImpl();
//    }
    public void fetch(){
//        if(this.girlModel!=null&&this.girlView!=null){
//            girlView.showLoading("获取数据中....");
//            girlModel.loadGirl(new IGirlModel.GirlOnLoadListener() {
//                @Override
//                public void onComplete(List<Girl> girls) {
//                    girlView.showGirs(girls);
//                    girlView.showLoading("数据加载完毕...");
//                }
//            });
//        }
        //使用弱引用优化的结果
        if(this.girlModel!=null&&this.mViewRef.get()!=null){
            this.mViewRef.get().showLoading("获取数据中....");
            girlModel.loadGirl(new IGirlModel.GirlOnLoadListener() {
                @Override
                public void onComplete(List<Girl> girls) {
                    mViewRef.get().showGirs(girls);
                    mViewRef.get().showLoading("数据加载完毕...");
                }
            });
        }
    }

    //进行绑定
    public void attachView(T view){
        mViewRef=new WeakReference<>(view);
    }

    //进行解绑
    public void detachView(){
        mViewRef.clear();
    }
}
