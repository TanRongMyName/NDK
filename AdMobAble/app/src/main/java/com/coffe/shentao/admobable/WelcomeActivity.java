package com.coffe.shentao.admobable;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {
    // 声明控件对象
     private TextView textView;
    //声明时间有多少;
    private int count = 5;
    private Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//无标题
        setContentView(R.layout.activity_welcome);
        textView=findViewById(R.id.textView3);
        animation = AnimationUtils.loadAnimation(this, R.anim.animation_text);
        handler.sendEmptyMessageDelayed(0, 1000);
    }
    private int getConut(){
        count--;
        if(count==0){
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        return count;
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            textView.setText(getConut()+"");
            handler.sendEmptyMessageDelayed(0,1000);
            animation.reset();
            textView.startAnimation(animation);
        }
    };
}
