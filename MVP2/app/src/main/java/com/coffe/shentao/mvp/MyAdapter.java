package com.coffe.shentao.mvp;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.coffe.shentao.mvp.bean.Girl;

import java.util.List;


public class MyAdapter extends BaseAdapter {

    private List<Girl> data;
    private Context context;
    private LayoutInflater inflater;
    private int layoutId;
    public MyAdapter(List<Girl>data,Context context,int layoutId){
        this.data=data;
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.layoutId=layoutId;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(layoutId, parent, false); //加载布局
            holder = new ViewHolder();
            holder.titleTv = (TextView) convertView.findViewById(R.id.title);
            holder.descTv = (TextView) convertView.findViewById(R.id.dirsc);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {   //else里面说明，convertView已经被复用了，说明convertView中已经设置过tag了，即holder
            holder = (ViewHolder) convertView.getTag();
        }

        Girl bean = data.get(position);
        holder.titleTv.setText(bean.getTitle());
        holder.descTv.setText(bean.getDisctri());
        holder.imageView.setImageResource(bean.getDrawableid());

        return convertView;
    }

    //这个ViewHolder只能服务于当前这个特定的adapter，因为ViewHolder里会指定item的控件，不同的ListView，item可能不同，所以ViewHolder写成一个私有的类
    private class ViewHolder {
        TextView titleTv;
        TextView descTv;
        ImageView imageView;
    }


}
