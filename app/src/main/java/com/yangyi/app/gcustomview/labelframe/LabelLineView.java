package com.yangyi.app.gcustomview.labelframe;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by GYH on 2016/1/7.
 */
public class LabelLineView extends Drawable {

    private static final String TAG = "LabelLineView";

    public static final int FRIST_TYPE = 0;
    public static final int SECOND_TYPE = 1;
    public static final int THIRD_TYPE = 2;
    public static final int FOURTH_TYPE = 3;

    private int currentType = FRIST_TYPE;

    //斜线的高度
    private static final int slashHeight = 70;
    private static final int slashWidth = 30;
    //直线的长度
    private static final int straightLine = 50;

    private int labelWidth;
    private int labelHeight;

    private Paint mPaint;

    public LabelLineView (int type){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.parseColor("#99000000"));
        labelHeight = slashHeight;
        labelWidth = slashWidth + straightLine;
        this.currentType = type;
        Log.i(TAG, "onDrawFirst  labelHeight = " + labelHeight + " labelWidth = " + labelWidth);
    }


    @Override
    public void draw(Canvas canvas) {
        if(currentType == FRIST_TYPE){
            drawFrist(canvas);
        }else if(currentType == SECOND_TYPE){
            drawSecond(canvas);
        }else if(currentType == THIRD_TYPE){
            drawThird(canvas);
        }else if(currentType == FOURTH_TYPE){
            drawFourth(canvas);
        }
    }

    /**
     *
     * @param canvas
     */
    private void drawFrist(Canvas canvas){
        //画第一条直线
        int lineSx = 0;
        int lineSy = 0;
        int lineEx = straightLine;
        int lineEy = 0;
        canvas.drawLine(lineSx, lineSy, lineEx, lineEy, mPaint);// 画线

        lineSx = straightLine;
        lineSy = 0;
        lineEx = labelWidth;
        lineEy = labelHeight;
        canvas.drawLine(lineSx, lineSy, lineEx, lineEy, mPaint);// 画线
    }

    /**
     *
     * @param canvas
     */
    private void drawSecond(Canvas canvas){
        //画第一条直线
        int lineSx = 0;
        int lineSy = labelHeight;
        int lineEx = straightLine;
        int lineEy = labelHeight;
        canvas.drawLine(lineSx, lineSy, lineEx, lineEy, mPaint);// 画线

        lineSx = straightLine;
        lineSy = labelHeight;
        lineEx = labelWidth;
        lineEy = 0;
        canvas.drawLine(lineSx, lineSy, lineEx, lineEy, mPaint);// 画线
    }

    private void drawThird(Canvas canvas){
        int lineSx = 0;
        int lineSy = labelHeight;
        int lineEx = slashWidth;
        int lineEy = 0;
        canvas.drawLine(lineSx, lineSy, lineEx, lineEy, mPaint);// 画线

        lineSx = slashWidth;
        lineSy = 0;
        lineEx = labelWidth;
        lineEy = 0;
        canvas.drawLine(lineSx, lineSy, lineEx, lineEy, mPaint);// 画线
    }

    private void drawFourth(Canvas canvas){
        //画第一条直线
        int lineSx = 0;
        int lineSy = 0;
        int lineEx = slashWidth;
        int lineEy = slashHeight;
        canvas.drawLine(lineSx, lineSy, lineEx, lineEy, mPaint);// 画线

        lineSx = slashWidth;
        lineSy = slashHeight;
        lineEx = labelWidth;
        lineEy = labelHeight;
        canvas.drawLine(lineSx, lineSy, lineEx, lineEy, mPaint);// 画线
    }

    public void setCurrentType(int currentType) {
        this.currentType = currentType;
    }

    @Override
    public int getIntrinsicWidth() {
        return labelWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return labelHeight;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
