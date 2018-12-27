package com.coffe.shentao.mvp.model;

import com.coffe.shentao.mvp.bean.Girl;

import java.util.List;

/**
 * 用来加载数据
 */
public interface IGirlModel {
    void loadGirl(GirlOnLoadListener girlOnLoadListener);
    //设计一个内部的接口
    interface GirlOnLoadListener{
        void onComplete(List<Girl> girls);
    }
}
