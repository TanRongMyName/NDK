package com.coffe.shentao.httpprocessor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.coffe.shentao.httpprocessor.weight.WaveViewByBezier;

public class WaveViewByBezierActivity extends AppCompatActivity {
    private WaveViewByBezier waveViewByBezier;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave_view_by_bezier);

        waveViewByBezier = (WaveViewByBezier) findViewById(R.id.wave_bezier);

        waveViewByBezier.startAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        waveViewByBezier.pauseAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        waveViewByBezier.resumeAnimation();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        waveViewByBezier.stopAnimation();
    }
}
