package com.coffe.shentao.doubleprogressndk;

public class Watcher {
    static {
        System.loadLibrary("native-lib");
    }
    public native void createWatcher(String userid);

    public native void connectMonitor();
}
