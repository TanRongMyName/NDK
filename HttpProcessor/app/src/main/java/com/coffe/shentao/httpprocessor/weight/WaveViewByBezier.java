package com.coffe.shentao.httpprocessor.weight;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.coffe.shentao.httpprocessor.R;

/**
 * Created by allen on 2016/12/13.
 * <p>
 * 波浪动画
 */

public class WaveViewByBezier extends View {

    /**
     * 屏幕高度
     */
    private int mScreenHeight;
    /**
     * 屏幕宽度
     */
    private int mScreenWidth;

    /**
     * 波浪的画笔
     */
    private Paint mWavePaint;
    /**
     * 一个周期波浪的长度
     */
    private int mWaveLength;

    /**
     * 波浪的路径
     */
    Path mWavePath;

    /**
     * 平移偏移量
     */
    private int mOffset;

    /**
     * 一个屏幕内显示几个周期
     */
    private int mWaveCount;

    /**
     * 振幅
     */
    private int mWaveAmplitude;

    /**
     * 波形的颜色
     */
    private int waveColor = 0xaaFF7E37;

    private static final int SIN = 0;
    private static final int COS = 1;
    private static final int DEFAULT = SIN;

    private int waveType = DEFAULT;


    private ValueAnimator valueAnimator;

    private int bitmapId;
    private Bitmap myBitmap;
    private Region region;
    private int originY=0;

    public WaveViewByBezier(Context context) {
        this(context, null);

    }

    public WaveViewByBezier(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RadarWaveView);
        bitmapId=typedArray.getResourceId(R.styleable.RadarWaveView_boatBitmap,0);//根据图片Id 获取到图片
        typedArray.recycle();

        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inSampleSize=1;
        if(bitmapId>0){
            myBitmap=BitmapFactory.decodeResource(getResources(),bitmapId,options);
            //XFormode 加载成圆形图片
            myBitmap=getCircleBitmap(myBitmap);
        }else{
            myBitmap=BitmapFactory.decodeResource(getResources(),R.drawable.ship2,options);
        }
        init();
    }
    /**
     * 生成透明背景的圆形图片！ 注意要生成透明背景的圆形  图片一定要png类型 不能是JPG 类型
     * @param mboatBitmap
     * @return
     */
    private Bitmap getCircleBitmap(Bitmap mboatBitmap) {
        if(mboatBitmap==null){
            return null;
        }
        Bitmap circleBitmap=Bitmap.createBitmap(mboatBitmap.getWidth(),mboatBitmap.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(circleBitmap);
        final Paint paint=new Paint();
        final Rect rect=new Rect(0 ,0,mboatBitmap.getWidth(),mboatBitmap.getHeight());
        final RectF rectF=new RectF(new Rect(0,0,mboatBitmap.getWidth(),mboatBitmap.getHeight()));
        float roundPx=mboatBitmap.getWidth()>mboatBitmap.getHeight()?mboatBitmap.getHeight()/2:mboatBitmap.getWidth()/2;
        paint.setAntiAlias(true);
        canvas.drawARGB(0,0,0,0);
        paint.setColor(Color.WHITE);
        canvas.drawRoundRect(rectF,roundPx,roundPx,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        final Rect src=new Rect(0,0,mboatBitmap.getWidth(),mboatBitmap.getHeight());
        canvas.drawBitmap(mboatBitmap,src,rect,paint);
        return circleBitmap;
    }

    private void init() {
        mWaveAmplitude = dp2px(50);
        mWaveLength = dp2px(100);
        initPaint();
    }


    /**
     * 初始化画笔
     */
    private void initPaint() {
        mWavePath = new Path();

        mWavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWavePaint.setColor(waveColor);
        mWavePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mWavePaint.setAntiAlias(true);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mScreenHeight = h;
        mScreenWidth = w;

        /**
         * 加上1.5是为了保证至少有两个波形（屏幕外边一个完整的波形，屏幕里边一个完整的波形）
         */
        mWaveCount = (int) Math.round(mScreenWidth / mWaveLength + 1.5);
        if(originY==0){
            originY=getHeight();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        switch (waveType) {
            case SIN:
                drawSinPath(canvas);
                break;
            case COS:
                drawCosPath(canvas);
                break;
        }

        if(region!=null) {

//            Rect bounds = region.getBounds();
//            canvas.drawBitmap(myBitmap, bounds.right - myBitmap.getWidth() / 2, bounds.top - myBitmap.getHeight(), mWavePaint);
            Rect bounds = region.getBounds();
            if(bounds.top>0||bounds.right>0) {
                if(bounds.top<originY) {
                    canvas.drawBitmap(myBitmap, bounds.right - myBitmap.getWidth() / 2, bounds.top - myBitmap.getHeight(), mWavePaint);
                }else{
                    canvas.drawBitmap(myBitmap, bounds.right - myBitmap.getWidth() / 2, bounds.bottom - myBitmap.getHeight(), mWavePaint);
                }
            }else{
                float x=getWidth()/2-myBitmap.getWidth()/2;
                canvas.drawBitmap(myBitmap, x,originY-myBitmap.getHeight(),mWavePaint);
            }
        }

    }

    /**
     * sin函数图像的波形
     *
     * @param canvas
     */
    private void drawSinPath(Canvas canvas) {
        mWavePath.reset();

        mWavePath.moveTo(-mWaveLength + mOffset, mWaveAmplitude);

        // TODO: 2017/6/19   //相信很多人会疑惑为什么控制点的纵坐标是以下值,是根据公式计算出来的,具体计算方法情况文章内容

        for (int i = 0; i < mWaveCount; i++) {

            //第一个控制点的坐标为(-mWaveLength * 3 / 4,-mWaveAmplitude)
            mWavePath.quadTo(-mWaveLength * 3 / 4 + mOffset + i * mWaveLength,
                    -mWaveAmplitude,
                    -mWaveLength / 2 + mOffset + i * mWaveLength,
                    mWaveAmplitude);

            //第二个控制点的坐标为(-mWaveLength / 4,3 * mWaveAmplitude)
            mWavePath.quadTo(-mWaveLength / 4 + mOffset + i * mWaveLength,
                    3 * mWaveAmplitude,
                    mOffset + i * mWaveLength,
                    mWaveAmplitude);
        }

        mWavePath.lineTo(getWidth(), getHeight());
        mWavePath.lineTo(0, getHeight());
        mWavePath.close();

        canvas.drawPath(mWavePath, mWavePaint);

        float x=getWidth()/2;
        region=new Region();
        Region clip=new Region((int)(x-0.1),0,(int)x,getHeight()*2);
        region.setPath(mWavePath,clip);
    }

    /**
     * cos函数图像的波形
     *
     * @param canvas
     */
    private void drawCosPath(Canvas canvas) {
        mWavePath.reset();

        mWavePath.moveTo(-mWaveLength + mOffset, mWaveAmplitude);

        for (int i = 0; i < mWaveCount; i++) {

            //第一个控制点的坐标为(-mWaveLength * 3 / 4,3 * mWaveAmplitude
            mWavePath.quadTo(-mWaveLength * 3 / 4 + mOffset + i * mWaveLength,
                    3 * mWaveAmplitude,
                    -mWaveLength / 2 + mOffset + i * mWaveLength,
                    mWaveAmplitude);

            //第二个控制点的坐标为(-mWaveLength / 4,-mWaveAmplitude)
            mWavePath.quadTo(-mWaveLength / 4 + mOffset + i * mWaveLength,
                    -mWaveAmplitude,
                    mOffset + i * mWaveLength,
                    mWaveAmplitude);
        }

        mWavePath.lineTo(getWidth(), getHeight());
        mWavePath.lineTo(0, getHeight());
        mWavePath.close();

        canvas.drawPath(mWavePath, mWavePaint);
    }


    /**
     * 波形动画
     */
    private void initAnimation() {
        valueAnimator = ValueAnimator.ofInt(0, mWaveLength);
        valueAnimator.setDuration(2000);
        valueAnimator.setStartDelay(300);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffset = (int) animation.getAnimatedValue();
                Log.v("TanRong","mOffset=="+mOffset);
                invalidate();
            }
        });
        valueAnimator.start();
    }

    public void startAnimation() {
        initAnimation();
    }

    public void stopAnimation() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void pauseAnimation() {
        if (valueAnimator != null) {
            valueAnimator.pause();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void resumeAnimation() {
        if (valueAnimator != null) {
            valueAnimator.resume();
        }
    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }
}
