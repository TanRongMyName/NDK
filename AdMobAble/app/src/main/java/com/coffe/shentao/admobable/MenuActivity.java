package com.coffe.shentao.admobable;

import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import fcd.awa.tgs.AdManager;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{
    public Button fanstion,ad,countad,withoutcountad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);




        fanstion=findViewById(R.id.button3);
        ad=findViewById(R.id.button4);
        countad=findViewById(R.id.button5);
        withoutcountad=findViewById(R.id.button6);

        fanstion.setOnClickListener(this);
        ad.setOnClickListener(this);
        countad.setOnClickListener(this);
        withoutcountad.setOnClickListener(this);


    }
    public void  JumpAd(View view){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);

    }
    public void JumpWelcome(View view){
        Intent intent=new Intent(this,WelcomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Intent  intent=null;
        switch(v.getId()){
            case R.id.button3:
                intent=new Intent(this,YoumiFansStationActivity.class);
                break;
            case R.id.button4:
                Log.v("TanRong","android 广告");
                break;
            case R.id.button5:
                Log.v("TanRong","Android 积分广告SDK");
                break;
            case R.id.button6:
                Log.v("TanRong","Android 无积分广告");
                break;
               default:
                break;
        }
        if(intent!=null) {
            startActivity(intent);
            intent=null;
        }

    }
}
