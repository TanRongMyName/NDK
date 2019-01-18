package com.coffe.shentao.httpprocessor.HttpProcessor;

import android.content.Context;
import android.util.Log;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public class RetrofitProcessor implements IHttpProcess {
    public RetrofitProcessor(Context context){

    }

    @Override
    public void Post(String url, Map<String, Object> params, final ICallBack callbak) {
            //URL 有问题 ----常见的是带着参数的请求连接 ---返回数据 直接网络请求 --部分失败
            String param1;
            if(url.endsWith("/")){

            }else{
                param1=url.substring(url.lastIndexOf("/"),url.length());
                url=url.substring(0,url.lastIndexOf("/")+1) ;
                Log.v("TanRong","param1=="+param1+"  url=="+url);
            }
            url="http://oa.massburg.net/#/machine/machineMachine";
            Retrofit retrofit =new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
            GetPhotoInfoInterface request=  retrofit.create(GetPhotoInfoInterface.class);
            Call<String>call=request.postCall();
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.v("TanRong---",response.body());
                    callbak.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.v("TanRong---",t.toString());
                    callbak.onFailure(t.toString());
                }
            });

    }

    @Override
    public void Get(String url, Map<String, Object> params, ICallBack callback) {

    }

    public interface GetPhotoInfoInterface{

        @GET("/")
        Call<String> getCall();
//        总连接一定要以 " / " 结尾
//        String URL_HOME = "http://192.168.0.106/index.php/api/";
        //动态的使用URL 并且不会因为没有使用 / 结尾 导致崩溃
//        @POST
//        @FormUrlEncoded
//        Call<String> postCall(@Url String url, @FieldMap Map<String,Object> map);
//        @FormUrlEncoded
        @POST("0001%2F2250173.json")
        Call<String> postCall();

        //Call<String> postCall(@FieldMap Map<String,Object>params);
    }

}
