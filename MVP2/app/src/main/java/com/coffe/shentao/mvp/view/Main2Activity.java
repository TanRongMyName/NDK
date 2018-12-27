package com.coffe.shentao.mvp.view;


import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.coffe.shentao.mvp.MyAdapter;
import com.coffe.shentao.mvp.R;
import com.coffe.shentao.mvp.bean.Girl;
import com.coffe.shentao.mvp.presenter.GirlPresenter;
import com.coffe.shentao.mvp.presenter.GirlPresenter2;

import java.util.List;

public class Main2Activity extends BaseActivity<IGirlView,GirlPresenter2<IGirlView>> implements IGirlView{
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=(ListView)findViewById(R.id.listview);
        girlPresenter.fetch();//baseActivity  中写了 girlpresenter  不能重新定义 不然会出现空指针
    }

    @Override
    protected GirlPresenter2<IGirlView> createPresenter() {
        return new GirlPresenter2<>();
    }

    @Override
    public void showLoading(String content) {
        Toast.makeText(this,content,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showGirs(List<Girl> girls) {
        //--- 这个地方 家在数据同时 将listview he  adapter  连接起来
        MyAdapter adapter=new MyAdapter(girls,this,R.layout.item_layout);
        listView.setAdapter(adapter);
    }
}
