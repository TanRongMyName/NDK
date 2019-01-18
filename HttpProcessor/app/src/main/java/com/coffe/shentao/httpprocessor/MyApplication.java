package com.coffe.shentao.httpprocessor;

import android.app.Application;

import com.coffe.shentao.httpprocessor.HttpProcessor.HttpHelper;
import com.coffe.shentao.httpprocessor.HttpProcessor.OkHttpProcessor;
import com.coffe.shentao.httpprocessor.HttpProcessor.RetrofitProcessor;
import com.coffe.shentao.httpprocessor.HttpProcessor.VolleyProcessor;
import com.coffe.shentao.httpprocessor.HttpProcessor.XUtilsProcessor;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //HttpHelper.init(new VolleyProcessor(this));
        //HttpHelper.init(new OkHttpProcessor(this));
        //HttpHelper.init(new XUtilsProcessor(this));
        HttpHelper.init(new RetrofitProcessor(this));
    }
}
