package com.coffe.shentao.httpprocessor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.coffe.shentao.httpprocessor.HttpProcessor.HttpCallBack;
import com.coffe.shentao.httpprocessor.HttpProcessor.HttpHelper;
import com.coffe.shentao.httpprocessor.HttpProcessor.ICallBack;
import com.coffe.shentao.httpprocessor.bean.PhotoSetInfo;
import com.coffe.shentao.httpprocessor.weight.WaveViewByBezier;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private static final String TAG="MainActivity";
    private String url="http://c.3g.163.com/photo/api/set/0001%2F2250173.json";
    private Map<String,Object> params=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.textView);
    }
    public void IntentRequest(View view){
        //网络请求
        HttpHelper.obtain().Post(url, params, new HttpCallBack<PhotoSetInfo>() {
            @Override
            public void onSuccess(PhotoSetInfo result) {
                StringBuffer sb =new StringBuffer();
                if(result!=null){
                    sb.append("来源: ")
                            .append(result.getSource())
                            .append("\r\n")
                            .append("Tag:")
                            .append(result.getSettag())
                            .append("\r\n")
                            .append("天气描述:")
                            .append(result.getDesc());
                }
                textView.setText(sb.toString());
            }

            @Override
            public void onFailed(String str) {
                Toast.makeText(MainActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
            }
        }
    );
    }

    public void OnClickWight(View view){
        Intent intent=null;
        switch(view.getId()){
            case R.id.button2:
                intent=new Intent(MainActivity.this,WaveViewBySinCosActivity.class);
                break;
            case R.id.button3:
                intent=new Intent(MainActivity.this,WaveViewByBezierActivity.class);
                break;
            case R.id.button4:
                intent=new Intent(MainActivity.this,WaveViewActivity.class);
                break;
            default:
                break;
        }
        if(intent!=null){
            startActivity(intent);
            intent=null;
        }else{
            Toast.makeText(this,"没有任务",Toast.LENGTH_SHORT).show();
        }

    }



}
