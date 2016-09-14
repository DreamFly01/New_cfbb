package com.cfbb.android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.cfbb.android.R;
import com.cfbb.android.commom.utils.others.ArithUtil;

/**
 * 带进度指示的进度圆形，线程安全的View，可直接在线程中更新进度
 *
 * @author MrChang45
 */
public class YCRoundProgress extends View {
    /**
     * 画笔对象的引用
     */
    private Paint paint;

    /**
     * 圆环的颜色
     */
    private int roundColor;

    /**
     * 圆环进度的颜色
     */
    private int roundProgressColor;

    /**
     * 中间进度百分比的字符串的颜色
     */
    private int textColor;

    /**
     * 中间进度百分比的字符串的字体
     */
    private float textSize;

    /**
     * 圆环的宽度
     */
    private float roundWidth;

    /**
     * 最大进度
     */
    private double max;

    /**
     * 当前进度
     */
    private double progress;

    // 后缀的文字
    private String suffixStr = "%";
    private String showMsg;
    private double setProgressInt = 0;

    private Handler mHanlder = new Handler() {
        public void handleMessage(Message msg) {
            if ((progress <= setProgressInt) && progress != 0) {
                progress++;
                postInvalidate();
                if (progress < setProgressInt) {
                    mHanlder.sendEmptyMessageDelayed(1, 10);
                }
            }
        }

        ;
    };

    public YCRoundProgress(Context context) {
        this(context, null);
    }

    public YCRoundProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YCRoundProgress(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        paint = new Paint();

        // 获取自定义属性和默认值
        roundColor = getResources()
                .getColor(R.color.txt_gray8);
        roundProgressColor = getResources()
                .getColor(R.color.blue);
        textColor = getResources().getColor(R.color.blue);
        textSize =getResources().getDimensionPixelSize(R.dimen.size_12);
        roundWidth =getResources().getDimensionPixelSize(R.dimen.ycprograss_roundWidth);
        max = 100;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * 画最外层的大圆环
         */
        int centre = getWidth() / 2; // 获取圆心的x坐标
        int radius = (int) (centre - roundWidth / 2); // 圆环的半径
        paint.setColor(roundColor); // 设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); // 设置空心
        paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
        paint.setAntiAlias(true); // 消除锯齿
        canvas.drawCircle(centre, centre, radius, paint); // 画出圆环



        /**
         * 画进度百分比
         */
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.DEFAULT); // 设置字体

        double percent = ArithUtil.mul(ArithUtil.div(progress,max,4),100);
       // L.e("xxx","progress="+progress+",max"+max+"((progress / max) * 100)"+((progress / max) * 100)+",percent="+percent);
        if (!suffixStr.equals("")) {
            showMsg = percent + suffixStr;
        }

        float textWidth = paint.measureText(showMsg); // 测量字体宽度，我们需要根据字体的宽度设置在圆环中间

        canvas.drawText(showMsg, centre - textWidth / 2, centre + textSize
                / 2, paint); // 画出进度百分比

        /**
         * 画圆弧 ，画圆环的进度
         */

        // 设置进度是实心还是空心
        paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
        paint.setColor(roundProgressColor); // 设置进度的颜色
        RectF oval = new RectF(centre - radius, centre - radius, centre
                + radius, centre + radius); // 用于定义的圆弧的形状和大小的界限
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(oval, -90, (float) (360 * progress / max), false,
                paint); // 根据进度画圆弧

    }

    public synchronized double getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     *
     * @param max
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * 获取进度.需要同步
     *
     * @return
     */
    public synchronized double getProgress() {
        return progress;
    }

    /***
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步 刷新界面调用postInvalidate()能在非UI线程刷新
     *
     * @param progress 进度
     */
    public synchronized void setProgress(double progress) {

        suffixStr = "%";

        if (progress < 0 || progress > 100) {
            throw new IllegalArgumentException(
                    "progress not less than 0 and must lower 100; progress = " + progress);
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            setProgressInt = (progress / 100);
            this.progress = progress;
            postInvalidate();
            mHanlder.sendEmptyMessageDelayed(1, 10);
        }

    }

    public int getCricleColor() {
        return roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }
}
