package com.coffe.shentao.httpprocessor.HttpProcessor;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpHelper implements IHttpProcess{
     private Map<String,Object> mParams;
     private static HttpHelper _instance;
     private HttpHelper(){
         mParams=new HashMap<String,Object>();
     }

     public static HttpHelper obtain(){
         synchronized (HttpHelper.class){
             if(_instance ==null){
                 _instance=new HttpHelper();
             }
         }
         return _instance;
     }
     private static IHttpProcess mIHttpProcessor=null;
     public static void init(IHttpProcess httpProcess){
         mIHttpProcessor=httpProcess;
     }

    @Override
    public void Post(String url, Map<String, Object> params, ICallBack callbak) {
         //参数的拼接
        final String finalUrl=appendParams(url,params);
        mIHttpProcessor.Post(url,params,callbak);
    }

    @Override
    public void Get(String url, Map<String, Object> params, ICallBack callback) {
        final String finalUrl=appendParams(url,params);
        mIHttpProcessor.Get(url,params,callback);
    }

public static String appendParams(String url, Map<String, Object> params){
         if(params==null||params.isEmpty()){
             return url;
         }
         StringBuffer urlBuilder=new StringBuffer(url);
         if(urlBuilder.indexOf("?")<=0){
             urlBuilder.append("?");
         }else{
             if(!urlBuilder.toString().endsWith("?")){
                 urlBuilder.append("&");
             }
         }
         for(Map.Entry<String,Object>entry:params.entrySet()){
             urlBuilder.append(entry.getKey()).append("=").append(encode(entry.getValue().toString()));
         }
         return urlBuilder.toString();
}


    private static String encode(String str){
        try {
            return URLEncoder.encode(str,"utf-8");
        } catch (UnsupportedEncodingException e) {
            Log.e("参数转码异常",e.toString());
            throw new RuntimeException(e);
        }
    }
}
