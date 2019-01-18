package com.coffe.shentao.httpprocessor.HttpProcessor;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract  class HttpCallBack<Result> implements ICallBack {
    @Override
    public void onSuccess(String result) {
        Gson gson=new Gson();
        Class<?> clz=analysisClassInfo(this);
        Result objResult=(Result)gson.fromJson(result,clz);
        onSuccess(objResult);
    }
    public abstract void onSuccess(Result result);
    public abstract void onFailed(String str);
    public static Class<?> analysisClassInfo(Object object){
        Type genType=object.getClass().getGenericSuperclass();
        Type[] params=((ParameterizedType)genType).getActualTypeArguments();
        return (Class<?>)params[0];
    }
    @Override
    public void onFailure(String e) {
            onFailed(e);
    }
}
