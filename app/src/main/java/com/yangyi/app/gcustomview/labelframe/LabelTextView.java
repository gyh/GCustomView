package com.yangyi.app.gcustomview.labelframe;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by GYH on 2016/1/6.
 */
public class LabelTextView extends Drawable{

    private static final String TAG = "LabelTextView";

    public static final int FRIST_TYPE = 0;
    public static final int SECOND_TYPE = 1;

    private int currentType = FRIST_TYPE;

    private static final int VIEW_PADDING = 25;
    private static final int TEXT_SIZE = 20;

    private String labelText;
    private int labelTextWidth;
    private int labelTextHeight;

    private Paint mPaint;

    public LabelTextView(String labelText, int textType){
        this.labelText = labelText;
        this.currentType = textType;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(TEXT_SIZE);
        initLabel();
    }

    private void initLabel(){
        Rect mBounds = new Rect();
        mPaint.getTextBounds(this.labelText, 0, this.labelText.length(), mBounds);
        this.labelTextHeight = mBounds.height() + VIEW_PADDING;
        this.labelTextWidth = labelTextHeight/2 +(mBounds.width()<6? 6:mBounds.width()) + VIEW_PADDING;
        Log.i(TAG, "initLabel labelTextHeight = " + labelTextHeight + " labelTextWidth = " + labelTextWidth);
    }

    @Override
    public int getIntrinsicWidth(){
        return labelTextWidth;
    }
    @Override
    public int getIntrinsicHeight(){
        return labelTextHeight;
    }



    public String getLabelText() {
        return labelText;
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
        initLabel();
    }

    @Override
    public void draw(Canvas canvas) {
        if(currentType == FRIST_TYPE){
            drawFrist(canvas);
        }else if(currentType == SECOND_TYPE){
            drawSecond(canvas);
        }
    }

    private void drawFrist(Canvas canvas){
        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.parseColor("#99000000"));
        //画半圆
        RectF oval = new RectF(0, 0, labelTextHeight, labelTextHeight);
        canvas.drawArc(oval, 270, -180, true, mPaint);

        Rect targetRect = new Rect(labelTextHeight/2, 0, labelTextWidth - VIEW_PADDING, labelTextHeight);
        canvas.drawRect(targetRect, mPaint);
        //画三角形
        Path path = new Path();
        path.moveTo(labelTextWidth - VIEW_PADDING, 0);// 此点为多边形的起点
        path.lineTo(labelTextWidth, labelTextHeight/2);
        path.lineTo(labelTextWidth - VIEW_PADDING, labelTextHeight);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, mPaint);
        //写文字
        mPaint.setColor(Color.parseColor("#ffffff"));
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(labelText, targetRect.centerX(), baseline, mPaint);
    }

    private void drawSecond(Canvas canvas){
        Log.i(TAG, "onDrawSecond ---------");
        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.parseColor("#99000000"));
        //画半圆
        RectF oval = new RectF(labelTextWidth - labelTextHeight, 0, labelTextWidth, labelTextHeight);
        canvas.drawArc(oval,90, -180, true, mPaint);

        //画矩形
        Rect targetRect = new Rect(VIEW_PADDING, 0, labelTextWidth-labelTextHeight/2, labelTextHeight);
        canvas.drawRect(targetRect, mPaint);
        //画三角形
        Path path = new Path();
        path.moveTo(VIEW_PADDING, 0);// 此点为多边形的起点
        path.lineTo(0, labelTextHeight/2);
        path.lineTo(VIEW_PADDING, labelTextHeight);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, mPaint);
        //写文字
        mPaint.setColor(Color.parseColor("#ffffff"));
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(labelText, targetRect.centerX(), baseline, mPaint);
    }

    public void setCurrentType(int currentType) {
        this.currentType = currentType;
    }

    @Override
    public void setAlpha(int alpha)
    {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf)
    {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity()
    {
        return PixelFormat.TRANSLUCENT;
    }
}
