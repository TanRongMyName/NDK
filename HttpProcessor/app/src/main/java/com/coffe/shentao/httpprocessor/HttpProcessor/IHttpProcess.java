package com.coffe.shentao.httpprocessor.HttpProcessor;

import java.util.Map;

public interface IHttpProcess {
    //网络访问： post get del update put
    void Post(String url, Map<String,Object> params, ICallBack callbak);
    void Get(String url,Map<String,Object>params,ICallBack callback);
}
