package com.coffe.shentao.mvp.model;

import com.coffe.shentao.mvp.R;
import com.coffe.shentao.mvp.bean.Girl;

import java.util.ArrayList;
import java.util.List;
// 统一命名图片 的 类文件 RenameImage
//D:\javaproject\TestOne\src\com\shentao\test\RenameImage.java
public class GirlModelImpl implements IGirlModel {
    @Override
    public void loadGirl(GirlOnLoadListener girlOnLoadListener) {
         //加载数据 初始化数据
        List<Girl>list=new ArrayList<>();
        list.add(new Girl(R.mipmap.fmage1,"迪丽热巴（Dilraba）","1992年6月3日出生于新疆乌鲁木齐市，中国内地影视女演员。"));
        list.add(new Girl(R.mipmap.fmage2,"柳岩（Ada）","1980年11月8日出生于湖南衡阳，中国内地女演员、歌手、主持人。"));
        list.add(new Girl(R.mipmap.fmage3,"沈梦辰","1989年6月13日出生于湖南省湘西吉首市，毕业于湖南大学06级表演系本科，中国内地女主持人、演员、模特。"));
        list.add(new Girl(R.mipmap.fmage4,"景甜（Jing Tian）","1988年7月21日出生于陕西省西安市，毕业于北京电影学院表演系，华语影视女演员。"));
        list.add(new Girl(R.mipmap.fmage5,"赵丽颖","1987年10月16日出生于河北省廊坊市，中国内地影视女演员。"));
        list.add(new Girl(R.mipmap.fmage6,"唐嫣","1983年12月6日身世于上海市，毕业于地方戏剧学院表演系本科班，中国边疆女演员。"));
        list.add(new Girl(R.mipmap.fmage17,"郭碧婷","1984年1月16日诞生于台湾省台北市，具有四分之一美国血缘，华语影视女演员、立体模特。"));
        list.add(new Girl(R.mipmap.fmage8,"刘亦菲","1987年8月25日诞生于湖北省武汉市，华语影视女演员、流行乐歌手毕业于北京影戏院2002级表演系本科班。\n"
        ));
        girlOnLoadListener.onComplete(list);
    }
}
