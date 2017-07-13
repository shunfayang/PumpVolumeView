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

    private Paint mPaint;
    private int mVolume = 100;
    private GestureDetector mDetector;
    private float centerWidth = 0;
    private float centerHeight = 0;
    private float top;
    private Paint mPaintGray;
    private float mProcessTop = 0;
    private float mDefaultProcess = 100;
    private float mX;
    private float mY;
    private float innerTop;
    private float innerBottom;
    private float mVolumeHeight;

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
        mPaintGray = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintGray.setColor(Color.parseColor("#ff4a4a4a"));
        mPaint.setColor(Color.parseColor("#ff21dc22"));
        mDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                float v = e2.getY() - e1.getY();
                Log.d("moose", "" + v);
                if (v > 50){
                    // 递增
                    mProcessTop ++;
                    return true;
                }
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
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
        float left = measuredWidth /2 - 20;
        float right = measuredWidth /2 + 20;
        top = measuredHeight / 2;
        float bottom = measuredHeight;
        // 柱体
        mPaintGray.setColor(Color.parseColor("#ff4a4a4a"));
        canvas.drawRect(left, top, right, bottom, mPaintGray);

        // inner rect
        mPaintGray.setColor(Color.parseColor("#ff979797"));
        float innerLeft = left + 10;
        innerTop = top + 30;
        float innerRight = right - 10;
        innerBottom = bottom - 10;

        float volume = (innerBottom - innerTop) / mDefaultProcess;// 每一格音量对于的像素高度
        mVolumeHeight = (innerBottom - mProcessTop)/ volume;// 当前音量高度
        // 绘制音量数字

//        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mPaintGray.setTextSize(20);
        mPaintGray.setTextAlign(Paint.Align.CENTER);
        canvas.drawText((int)mVolumeHeight + "" , (right - left)/2 + left , top + 20,mPaintGray);

        // inner background
        canvas.drawRect(innerLeft, innerTop, innerRight, innerBottom, mPaintGray);
//        mPaint.setColor(Color.parseColor("#ff21dc22"));
//        mProcessTop = innerTop + 50;
        // 绘制默认手柄
        if (centerHeight == 0 && centerWidth == 0 && mProcessTop == 0) {
            mProcessTop = innerBottom - 10;
            centerWidth = measuredWidth / 2;
            centerHeight = this.top - 50;
        }
        canvas.drawRect(innerLeft, mProcessTop, innerRight, innerBottom, mPaint);

        float handleLeft = centerWidth - 50;
        float handleTop = centerHeight - 10;
        float handleRight = centerWidth + 50;
        float handleBottom = centerHeight;
        canvas.drawRect(handleLeft, handleTop, handleRight, handleBottom, mPaintGray);
        canvas.drawRect(centerWidth - 5, handleTop, centerWidth +5, this.top, mPaintGray);

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
                    centerHeight = top;
                } else {
                    if (y <= 20 ){
                        centerHeight = 20;
                    } else {
                        centerHeight = y;
                    }
                }
                if ((y - mY) > 0 && y <= top){
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
