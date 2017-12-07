package com.doing.flat.coffee.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.doing.flat.coffee.R;

/**
 * @author Zheng Haibo
 * @date 2015年3月27日 下午5:20:51
 * @web http://github.com/nuptboyzhb
 * @Description: 水波效果
 */
public class WaterWaveView extends View implements Runnable {

    private static final int PEROID = 16;// 绘制周期
    private static final int X_STEP = 15;// X轴的量化步长-越小曲线月平滑，但是计算量越大

    private Path firstWavePath;
    private Path secondWavePath;

    private Paint firstPaint;
    private Paint secondPaint;

    private int firstColor = 0xff128cd2;
    private int secondColor = 0xff0b547e;

    private int width;// view的高度
    private int height;// view的宽度

    private float moveWave = 0.0f;// 波形移动
    private float omega;// 波形的周期
    private double waveHeight;// 波形的幅度
    private float moveSpeed;// 波形的移动速度
    private float heightOffset = 100;
//    private float heightOffset = 180;

    private boolean isOnDraw = true;
    private Rect mMaskSrcRect, mMaskDestRect;
    private Bitmap mMaskBitmap;
    private Paint mBitmapPaint;
    private PorterDuffXfermode mPorterDuffXfermode;

    private boolean type = true;
    private boolean state = true;

    public WaterWaveView(Context context) {
        super(context);
        initBitmap();
    }

    public WaterWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBitmap();
        init(context, attrs);
    }

    public WaterWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBitmap();
        init(context, attrs);
    }

    public void setType(boolean type) {
        this.type = type;
    }

    /**
     * 获取自定义View的设置
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        new Thread(this).start();// 启动绘制线程
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(getSecondWavePath(), getSecondLayerPaint());// 绘制第二层
        canvas.drawPath(getFristWavePath(), getFristLayerPaint());// 绘制第一层

        if (mBitmapPaint == null) {
            mBitmapPaint = new Paint();
            // 防抖动
            mBitmapPaint.setDither(true);
            // 开启图像过滤
            mBitmapPaint.setFilterBitmap(true);
            // 设置图像的混合模式
            mBitmapPaint.setXfermode(mPorterDuffXfermode);
        }
//        // 绘制遮罩圆
//        canvas.drawBitmap(mMaskBitmap, mMaskSrcRect, mMaskDestRect,
//                mBitmapPaint);
    }

    // 初始化bitmap
    private void initBitmap() {
        mMaskBitmap = ((BitmapDrawable) getResources().getDrawable(
                R.drawable.circle_500))
                .getBitmap();
        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int maskWidth = mMaskBitmap.getWidth();
        int maskHeight = mMaskBitmap.getHeight();
        mMaskSrcRect = new Rect(0, 0, maskWidth, maskHeight);
        mMaskDestRect = new Rect(0, 0, w, h);
    }

    /**
     * 使用路径描绘绘制的区域
     *
     * @return
     */
    private Path getSecondWavePath() {
        // 绘制区域2的路径
        if (secondWavePath == null) {
            secondWavePath = new Path();
        }
        secondWavePath.reset();
        secondWavePath.moveTo(0, height);// 移动到左下角的点
        for (float x = 0; x <= width; x += X_STEP) {
            float y = (float) (waveHeight
                    * Math.cos(omega * x + moveWave * 1.7f) + waveHeight)
                    + heightOffset;
            secondWavePath.lineTo(x, y);
        }
        secondWavePath.lineTo(width, 0);
        secondWavePath.lineTo(width, height);
        return secondWavePath;
    }

    /**
     * 根据画笔的颜色，创建画笔
     *
     * @return
     */
    private Paint getSecondLayerPaint() {
        if (this.secondPaint == null) {
            secondPaint = new Paint();
        }
        secondPaint.setColor(secondColor);
        secondPaint.setStyle(Paint.Style.FILL);
        secondPaint.setAntiAlias(true);
        return secondPaint;
    }

    /**
     * 根据画笔的颜色，创建画笔
     *
     * @return
     */
    private Paint getFristLayerPaint() {
        // TODO 获取第一层的Paint，主要是颜色
        if (firstPaint == null) {
            firstPaint = new Paint();
        }
        firstPaint.setColor(firstColor);
        firstPaint.setStyle(Paint.Style.FILL);
        firstPaint.setAntiAlias(true);
        return firstPaint;
    }

    /**
     * 使用路径描绘绘制的区域
     *
     * @return
     */
    private Path getFristWavePath() {
        // 绘制区域1的路径
        if (firstWavePath == null) {
            firstWavePath = new Path();
        }
        firstWavePath.reset();
        firstWavePath.moveTo(0, height);// 移动到左下角的点
        for (float x = 0; x <= width; x += X_STEP) {
            float y = (float) (waveHeight * Math.sin(omega * x + moveWave) + waveHeight)
                    + heightOffset;
            firstWavePath.lineTo(x, y);
        }
        firstWavePath.lineTo(width, 0);
        firstWavePath.lineTo(width, height);
        return firstWavePath;
    }

    @Override
    public void run() {
        while (isOnDraw) {
            long startTime = System.currentTimeMillis();
            moveWave += moveSpeed;
            postInvalidate();
            long time = System.currentTimeMillis() - startTime;
            if (time < PEROID) {
                try {
                    Thread.sleep(PEROID - time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        initWaveParam();
        super.onWindowFocusChanged(hasWindowFocus);
    }

    private void initWaveParam() {
//        this.width = getWidth();
//        this.height = getHeight();
        this.width = getHeight();
        this.height = getWidth();
        waveHeight = height / 30;
//        omega = (float) (3.5f * Math.PI / width);
        omega = (float) (3.5f * Math.PI / width);
//        moveSpeed = 0.15f;
        moveSpeed = 0;
    }

    @Override
    protected void onDetachedFromWindow() {
        isOnDraw = false;
        super.onDetachedFromWindow();
    }

    /**
     * 设置omega
     *
     * @param omega
     */
    public void setOmega(float omega) {
        this.omega = omega;
    }

    /**
     * 设置波形的高度
     *
     * @param waveHeight
     */
    public void setWaveHeight(double waveHeight) {
        this.waveHeight = waveHeight;
    }

    /**
     * 设置波形的移动速度
     *
     * @param moveSpeed
     */
    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    /**
     * 设置第一层颜色
     *
     * @param firstColor
     */
    public void setFirstColor(int firstColor) {
        this.firstColor = firstColor;
    }

    /**
     * 设置第二层颜色
     *
     * @param secondColor
     */
    public void setSecondColor(int secondColor) {
        this.secondColor = secondColor;
    }

    /**
     * 参考值63
     *
     * @param progress
     */
    public void setMoveSpeedByProgress(int progress) {
        if (progress >= 0 && progress <= 100) {
            moveSpeed = progress * 0.003f;
        }
    }

    /**
     * 参考值18
     *
     * @param progress
     */
    public void setOmegaByProgress(int progress) {
        if (progress >= 0 && progress <= 100) {
            omega = progress * (float) (0.1f * Math.PI / width);
        }
    }

    /**
     * 参考范围0-10
     *
     * @param progress
     */
    public void setWaveHeightByProgress(int progress) {
        if (progress >= 0 && progress <= 100) {
            waveHeight = progress * height * 0.005f;
        }
    }

    public void setHeightOffsetByProgress(int progress) {
        if (progress >= 0 && progress <= 200) {
            heightOffset = progress * height * 0.005f;
        }
    }

}
