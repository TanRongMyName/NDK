package com.coffe.shentao.httpprocessor.HttpProcessor;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.Map;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
//okhttp 有线程异步的问题
public class OkHttpProcessor implements IHttpProcess {
    private static final String TAG="OKHttpProcessor";
    private OkHttpClient mOkHttpClient;
    private Context context;
    private Handler myhandelr;
    public OkHttpProcessor(Context context){
        mOkHttpClient=new OkHttpClient();
        myhandelr=new Handler();
    }

    @Override
    public void Post(String url, Map<String, Object> params, final ICallBack callbak) {
        RequestBody requestBody=appendBody(params);
        Request request=new Request.Builder().url(url).post(requestBody).header("User-Agent","a").build();
        Log.v(TAG,"TAG------------Post");
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                myhandelr.post(new Runnable() {
                    @Override
                    public void run() {
                        callbak.onFailure(e.toString());
                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // Log.v(TAG,"TAG------------"+response.isSuccessful()+"  response.body().string()"+response.body().string());
                // Okhttp 返回结果 只能 判断调用一次 第二次调用 会没有返回数据
                if (response.isSuccessful()) {
                    final String result = response.body().string();
                    myhandelr.post(new Runnable() {
                        @Override
                        public void run() {
                            callbak.onSuccess(result);
                        }
                    });

                } else {
                    myhandelr.post(new Runnable() {
                        @Override
                        public void run() {
                            callbak.onFailure(response.body().toString());
                        }
                    });
                }
            }
        });
    }



    @Override
    public void Get(String url, Map<String, Object> params, ICallBack callback) {

    }

    private RequestBody appendBody(Map<String,Object> params){
        FormBody.Builder body=new FormBody.Builder();
        if(params==null||params.isEmpty()){
            return body.build();
        }
        for(Map.Entry<String,Object>entry:params.entrySet()){
            body.add(entry.getKey(),entry.getValue().toString());
        }
        return body.build();
    }
}

