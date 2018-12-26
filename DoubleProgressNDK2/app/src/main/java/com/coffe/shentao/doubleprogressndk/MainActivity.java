package com.coffe.shentao.doubleprogressndk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * android NDK 双进程 监听服务socket APP 何时被杀死
 * liunx socket 长连接--柱塞式 read 方法
 * socket 守护的是关键服务 --server 在后台不可见
 */
public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=new Intent(this,ProcessService.class);
        startService(intent);
    }
    //ps   查看进程


}
