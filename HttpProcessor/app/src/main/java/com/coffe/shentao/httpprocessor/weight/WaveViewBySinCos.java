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
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.coffe.shentao.httpprocessor.R;

/**
 * Created by allen on 2017/6/20.
 * /**
 * y=A*sin(ωx+φ)+k
 * <p>
 * A—振幅越大，波形在y轴上最大与最小值的差值越大
 * ω—角速度， 控制正弦周期(单位角度内震动的次数)
 * φ—初相，反映在坐标系上则为图像的左右移动。这里通过不断改变φ,达到波浪移动效果
 * k—偏距，反映在坐标系上则为图像的上移或下移。
 */
public class WaveViewBySinCos extends View {
    private Context mContext;
    /**
     * 振幅
     */
    private int A;
    /**
     * 偏距
     */
    private int K;

    /**
     * 波形的颜色
     */
    private int waveColor = 0xaaFF7E37;

    /**
     * 初相
     */
    private float φ;

    /**
     * 波形移动的速度
     */
    private float waveSpeed = 3f;

    /**
     * 角速度
     */
    private double ω;

    /**
     * 开始位置相差多少个周期
     */
    private double startPeriod;

    /**
     * 是否直接开启波形
     */
    private boolean waveStart;
    private Path path;
    private Paint paint;

    private static final int SIN = 0;
    private static final int COS = 1;

    private int waveType;

    private static final int TOP = 0;
    private static final int BOTTOM = 1;

    private int waveFillType;
    private int boatBitmap;
    private Bitmap mboatBitmap;
    private ValueAnimator valueAnimator;

    private Region region;
    public WaveViewBySinCos(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        getAttr(attrs);
        K=A;
        initPaint();

        initAnimation();
    }

    private void getAttr(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.RadarWaveView);

        waveType = typedArray.getInt(R.styleable.RadarWaveView_waveType, SIN);
        waveFillType = typedArray.getInt(R.styleable.RadarWaveView_waveFillType, BOTTOM);
        A = typedArray.getDimensionPixelOffset(R.styleable.RadarWaveView_waveAmplitude, dp2px(10));
        waveColor = typedArray.getColor(R.styleable.RadarWaveView_waveColor, waveColor);
        waveSpeed = typedArray.getFloat(R.styleable.RadarWaveView_waveSpeed, waveSpeed);
        startPeriod = typedArray.getFloat(R.styleable.RadarWaveView_waveStartPeriod, 0);
        waveStart = typedArray.getBoolean(R.styleable.RadarWaveView_waveStart, false);

        boatBitmap=typedArray.getResourceId(R.styleable.RadarWaveView_boatBitmap,0);//根据图片Id 获取到图片
                                                                                               //涨水
        typedArray.recycle();

        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inSampleSize=1;
        if(boatBitmap>0){
            mboatBitmap=BitmapFactory.decodeResource(getResources(),boatBitmap,options);
            //XFormode 加载成圆形图片
            mboatBitmap=getCircleBitmap(mboatBitmap);
        }else{
            mboatBitmap=BitmapFactory.decodeResource(getResources(),R.drawable.ship1,options);
        }
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

    private void initPaint() {
        path = new Path();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);//填充完整
        paint.setColor(waveColor);
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
    int orationY=0;
    private void initAnimation() {
        valueAnimator = ValueAnimator.ofInt(0, getWidth());
        valueAnimator.setDuration(1000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                /**
                 * 刷新页面调取onDraw方法，通过变更φ 达到移动效果
                 */
                invalidate();
            }
        });
        if (waveStart) {
            valueAnimator.start();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ω = 2 * Math.PI / getWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        switch (waveType) {
            case SIN:
                drawSin(canvas);
                break;
            case COS:
                drawCos(canvas);
                break;
        }

        if(region!=null) {
            Rect bounds = region.getBounds();
            canvas.drawBitmap(mboatBitmap, bounds.right-mboatBitmap.getWidth()/2, bounds.top-mboatBitmap.getHeight(), paint);
        }

    }


    /**
     * 根据cos函数绘制波形
     *
     * @param canvas
     */
    private void drawCos(Canvas canvas) {

        switch (waveFillType) {
            case TOP:
                fillTop(canvas);
                break;
            case BOTTOM:
                fillBottom(canvas);
                break;
        }
    }

    /**
     * 根据sin函数绘制波形
     *
     * @param canvas
     */
    private void drawSin(Canvas canvas) {

        switch (waveFillType) {
            case TOP:
                fillTop(canvas);
                break;
            case BOTTOM:
                fillBottom(canvas);
                break;
        }

    }

    /**
     * 填充波浪上面部分
     */
    private void fillTop(Canvas canvas) {

        φ -= waveSpeed / 100;
        float y;

        path.reset();
        path.moveTo(0, getHeight());

        for (float x = 0; x <= getWidth(); x += 20) {
            y = (float) (A * Math.sin(ω * x + φ + Math.PI * startPeriod) + K);
            path.lineTo(x, getHeight() - y);
        }

        path.lineTo(getWidth(), 0);
        path.lineTo(0, 0);
        path.close();

        canvas.drawPath(path, paint);

    }

    /**
     * 填充波浪下面部分
     */
    private void fillBottom(Canvas canvas) {

        φ -= waveSpeed / 100;
        float y;

        path.reset();
        path.moveTo(0, 0);

        for (float x = 0; x <= getWidth(); x += 20) {
            y = (float) (A * Math.sin(ω * x + φ + Math.PI * startPeriod) + K);
            path.lineTo(x, y+mboatBitmap.getHeight());//带上了船的高度 这样 船移动到上边的时候不会被遮住
        }

        //填充矩形
        path.lineTo(getWidth(), getHeight());
        path.lineTo(0, getHeight());
        path.close();

        canvas.drawPath(path, paint);
        float x=getWidth()/2;
        region=new Region();
        Region clip=new Region((int)(x-0.1),0,(int)x,getHeight()*2);
        region.setPath(path,clip);

    }


    public void startAnimation() {
        if (valueAnimator != null) {
            valueAnimator.start();
        }
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


}
