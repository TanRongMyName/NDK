package com.coffe.shentao.doubleprogressndk;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class ProcessService extends Service {
    private String TAG="ProcessService";
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private int i=0;
    @Override
    public void onCreate() {
        super.onCreate();
        Watcher watcher=new Watcher();
        watcher.createWatcher(String.valueOf(Process.myUid()));
        watcher.connectMonitor();
        //用来判断当前的 service 是否被杀死
//        Timer timer=new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                Log.i(TAG,"开启服务 +"+i);
//                i++;
//            }
//        },0,3000);


    }
}
