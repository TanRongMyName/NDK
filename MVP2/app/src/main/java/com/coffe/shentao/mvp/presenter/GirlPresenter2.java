package com.coffe.shentao.mvp.presenter;

import com.coffe.shentao.mvp.bean.Girl;
import com.coffe.shentao.mvp.model.GirlModelImpl;
import com.coffe.shentao.mvp.model.IGirlModel;
import com.coffe.shentao.mvp.view.IGirlView;

import java.util.List;

public class GirlPresenter2 <T extends IGirlView> extends BasePresenter<T> {
    IGirlModel girlModel;
    //绑定activity 生命周期的方法
    public GirlPresenter2() {
        girlModel=new GirlModelImpl();
    }

    public void fetch(){

        //使用弱引用优化的结果
        if(this.girlModel!=null&&viewReference.get()!=null){
            viewReference.get().showLoading("获取数据中....");
            girlModel.loadGirl(new IGirlModel.GirlOnLoadListener() {
                @Override
                public void onComplete(List<Girl> girls) {
                    viewReference.get().showGirs(girls);
                    viewReference.get().showLoading("数据加载完毕...");
                }
            });
        }
    }
}
