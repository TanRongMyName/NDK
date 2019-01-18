package com.coffe.shentao.httpprocessor;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AccelerateInterpolator;

import com.coffe.shentao.httpprocessor.weight.WaveView;
//https://www.jianshu.com/p/cba46422de67
//Android水波纹特效的简单实现
public class WaveViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave_view);
        WaveView mWaveView=findViewById(R.id.wave_view);
        mWaveView.setDuration(5000);
        mWaveView.setStyle(Paint.Style.STROKE);
        mWaveView.setSpeed(400);
        mWaveView.setColor(Color.parseColor("#ff0000"));
        mWaveView.setInterpolator(new AccelerateInterpolator(1.2f));
        mWaveView.start();
    }
}
