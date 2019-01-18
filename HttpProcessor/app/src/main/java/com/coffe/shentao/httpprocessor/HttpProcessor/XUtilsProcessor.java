package com.coffe.shentao.httpprocessor.HttpProcessor;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.xutils.BuildConfig;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

public class XUtilsProcessor implements IHttpProcess {
    public XUtilsProcessor(Context context){
        x.Ext.init((Application) context);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    }
    @Override
    public void Post(String url, Map<String, Object> params, final ICallBack callbak) {
        RequestParams requestParams = new RequestParams(url);	// 网址(请替换成实际的网址)
        requestParams.addQueryStringParameter("key", "value");	// 参数(请替换成实际的参数与值)
        //使用 for循环将 参数添加到  参数列表中
        setParams(requestParams,params);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onCancelled(CancelledException arg0) {

            }

            // 注意:如果是自己onSuccess回调方法里写了一些导致程序崩溃的代码，也会回调道该方法，因此可以用以下方法区分是网络错误还是其他错误
            // 还有一点，网络超时也会也报成其他错误，还需具体打印出错误内容比较容易跟踪查看
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                if (ex instanceof HttpException) { // 网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                } else { // 其他错误
                }
                callbak.onSuccess(ex.getMessage());
            }

            // 不管成功或者失败最后都会回调该接口
            @Override
            public void onFinished() {

            }

            @Override
            public void onSuccess(String arg0) {
                callbak.onSuccess(arg0);
                Log.v("TanRong----",arg0);
            }
        });

    }

    @Override
    public void Get(String url, Map<String, Object> params, final ICallBack callback) {
        RequestParams requestParams = new RequestParams(url);	// 网址(请替换成实际的网址)
        requestParams.addBodyParameter("key", "value");	// 参数(请替换成实际的参数与值)
        setParams(requestParams,params);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {

            @Override
            public void onCancelled(Callback.CancelledException arg0) {

            }

            // 注意:如果是自己onSuccess回调方法里写了一些导致程序崩溃的代码，也会回调道该方法，因此可以用以下方法区分是网络错误还是其他错误
            // 还有一点，网络超时也会也报成其他错误，还需具体打印出错误内容比较容易跟踪查看
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                if (ex instanceof HttpException) { // 网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    // ...
                } else { // 其他错误
                    // ...
                }
                callback.onSuccess(ex.getMessage());
            }

            // 不管成功或者失败最后都会回调该接口
            @Override
            public void onFinished() {

            }

            @Override
            public void onSuccess(String arg0) {
                callback.onSuccess(arg0);
            }
        });

    }


    public RequestParams setParams(RequestParams requestParams,Map<String,Object> params){
        if(params==null||params.isEmpty()){
            return requestParams;
        }
        for(Map.Entry<String,Object>entry:params.entrySet()){
            requestParams.addParameter(entry.getKey(),entry.getValue().toString());
        }
        return requestParams;
    }
}
