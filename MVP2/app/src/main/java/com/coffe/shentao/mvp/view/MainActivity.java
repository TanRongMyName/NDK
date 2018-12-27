package com.coffe.shentao.mvp.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.coffe.shentao.mvp.MyAdapter;
import com.coffe.shentao.mvp.R;
import com.coffe.shentao.mvp.bean.Girl;
import com.coffe.shentao.mvp.presenter.GirlPresenter;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IGirlView {
    GirlPresenter girlPresenter;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化  listveiw -----
        listView=(ListView)findViewById(R.id.listview);
        girlPresenter=new GirlPresenter();
        girlPresenter.attachView(this);
        girlPresenter.fetch();
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

    @Override
    protected void onDestroy() {
        girlPresenter.detachView();//解绑
        super.onDestroy();
    }
}
