package com.yangshunfa.pumpvolumeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by yangshunfa on 2017/7/13.
 * tips:
 */

public class PumpVolumeView extends View {

    public static final String BRIGHT_GRAY = "#ff979797";
    public static final String GREEN = "#ff21dc22";
    private Paint mPaint;
    private int mVolume = 100;
    private GestureDetector mDetector;
    private float centerWidth = 0;
    private float centerHeight = 0;
    private float top;
//    private Paint mPaintGray;
    private float mProcessTop = 0;
    private float mDefaultProcess = 100;
    private float mX;
    private float mY;
    private float innerTop;
    private float innerBottom;
    private float mVolumeHeight;
    private String darkGray = "#ff4a4a4a";

    public PumpVolumeView(Context context) {
        super(context);
        init(context);
    }

    public PumpVolumeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PumpVolumeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context) {
//        mPaintGray = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#ff21dc22"));
        mPaint.setTextSize(20);
        mPaint.setTextAlign(Paint.Align.CENTER);

        // 开启一个线程不断递减音量
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true) {
                        Thread.sleep(200);
                        if (mProcessTop < innerBottom) {
                            mProcessTop++;
                            postInvalidate();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        // 最外边深色柱体
        float left = measuredWidth /2 - 20;
        float right = measuredWidth /2 + 20;
        top = measuredHeight / 2;
        float bottom = measuredHeight;
        // 画柱体
        mPaint.setColor(Color.parseColor(darkGray));
        canvas.drawRect(left, top, right, bottom, mPaint);

        // inner rect
        mPaint.setColor(Color.parseColor(BRIGHT_GRAY));
        float innerLeft = left + 10;
        innerTop = top + 30;
        float innerRight = right - 10;
        innerBottom = bottom - 10;

        float volume = (innerBottom - innerTop) / mDefaultProcess;// 每一格音量对于的像素高度
        mVolumeHeight = (innerBottom - mProcessTop)/ volume;// 当前音量高度
        // 绘制音量数字

        canvas.drawText((int)mVolumeHeight + "" , (right - left)/2 + left , top + 20,mPaint);

        // inner background
        canvas.drawRect(innerLeft, innerTop, innerRight, innerBottom, mPaint);
        // 初始的进度
        if (centerHeight == 0 && centerWidth == 0 && mProcessTop == 0) {
            mProcessTop = innerBottom - 50;
            centerWidth = measuredWidth / 2;
            centerHeight = this.top - 50;
        }
        // 绘制绿色的音量进度
        mPaint.setColor(Color.parseColor(GREEN));
        canvas.drawRect(innerLeft, mProcessTop, innerRight, innerBottom, mPaint);

        float handleLeft = centerWidth - 50;
        float handleTop = centerHeight - 10;
        float handleRight = centerWidth + 50;
        float handleBottom = centerHeight;

        mPaint.setColor(Color.parseColor(BRIGHT_GRAY));
        // 横向手柄
        canvas.drawRect(handleLeft, handleTop, handleRight, handleBottom, mPaint);
        // 竖直打气杆
        canvas.drawRect(centerWidth - 5, handleTop, centerWidth +5, this.top, mPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        return mDetector.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                if (y >= top ){
                    // 手指移动距离低于外部柱体最高处
                    centerHeight = top;
                } else {
                    if (y <= 20 ){
                        centerHeight = 20;
                    } else {
                        centerHeight = y;
                    }
                }
                if ((y - mY) > 0 && y <= top){
                    // 手势往下“压”
                    if (mProcessTop >= innerTop) {
                        mProcessTop--;
                    }
                }

                mX = x;
                mY = y;
                invalidate();
                break;
//                return mDetector.onTouchEvent(event);
            default:
//                return mDetector.onTouchEvent(event);
                break;
        }
        return true;
    }
}
