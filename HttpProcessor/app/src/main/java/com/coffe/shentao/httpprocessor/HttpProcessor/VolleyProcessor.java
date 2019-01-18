package com.coffe.shentao.httpprocessor.HttpProcessor;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

public class VolleyProcessor implements IHttpProcess{
    private static RequestQueue mQueue=null;

    public VolleyProcessor(Context context){
        mQueue= Volley.newRequestQueue(context);
    }
    @Override
    public void Post(String url, Map<String, Object> params, final ICallBack callbak) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callbak.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callbak.onFailure(error.toString());
            }
        });
        mQueue.add(stringRequest);
    }

    @Override
    public void Get(String url, Map<String, Object> params, final ICallBack callback) {
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        });
        mQueue.add(stringRequest);
    }
}
