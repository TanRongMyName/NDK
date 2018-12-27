package com.coffe.shentao.mvp.view;

import com.coffe.shentao.mvp.bean.Girl;

import java.util.List;

/**
 * 定义所有的UI逻辑
 */
public interface IGirlView {
     void  showLoading(String content);//加载进度条
     void showGirs(List<Girl> girls);
}
