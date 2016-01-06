package com.yangyi.app.gcustomview.test3;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.yangyi.app.gcustomview.R;
import com.yangyi.app.gcustomview.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GYH on 2016/1/4.
 */
public class TwoLabelView extends View implements LabelViewListener {

    private static final String TAG = "TwoLabelView";

    //存在两种模式切换
    private static final int FRIST_MODEL = 0;
    private static final int SECOND_MODEL = 1;
    private static final int THIRD_MODEL = 2;
    private static final int FOURTH_MODEL = 3;
    //移动偏移量阀值
    private static final int MOVE_OFFSET = 20;
    //中心点击半径
    private static final int CENTER_RADIUS = 60;
    private static final int VIEW_PADDING = 20;
    //当前模式
    public int currentType = FRIST_MODEL;

    private int labelViewHeight = 0;//子视图的高
    private int labelViewWidth = 0;//子视图的宽

    private int xPrevious;//子视图上一个x坐标
    private int yPrevious;//子视图上一个y坐标

    //偏移量
    private int xOffset;
    private int yOffset;
    //内容
    private List<String> labelTextList = new ArrayList<>();

    //可见区域变化
    private int areaLeft = 0;
    private int areaTop = 0;
    private int areaRight = 0;
    private int areaBottom = 0;

    private Paint mPaint;
    //记录两个字符串的视图长度
    private int textFirstWidth = 0;
    private int textSecondWidth = 0;
    private int textHeight = 0;
    //斜线的高度
    private static final int slashHeight = 50;
    private static final int slashWidth = 50;
    //直线的长度
    private static final int straightLine = 50;

    //原点图片
    private BitmapDrawable bmpDraw = (BitmapDrawable) getResources().getDrawable(R.mipmap.kkkk);
    private Bitmap originPic = bmpDraw.getBitmap();

    public TwoLabelView(Context context, List<String> text, int x, int y) {
        super(context);
        setWillNotDraw(false);
        this.xPrevious = x;
        this.yPrevious = y;
        this.labelTextList.addAll(text);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {

        originPic = Utils.drawableToBitmap(new LabelTextView(originPic));

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Rect mBounds = new Rect();
        mPaint.setTextSize(20);
        //获取第一个和第二个字符串的长度
        mPaint.getTextBounds(labelTextList.get(0), 0, labelTextList.get(0).length(), mBounds);
        textFirstWidth = mBounds.width();
        mPaint.getTextBounds(labelTextList.get(1), 0, labelTextList.get(1).length(), mBounds);
        textSecondWidth = mBounds.width();
        //设置高度
        textHeight = mBounds.height() + VIEW_PADDING;
        labelViewHeight = mBounds.height() + slashHeight * 2 + VIEW_PADDING;
        switch (currentType) {
            case FRIST_MODEL:
                //找出最长的字符串
                int strBigLen = textFirstWidth > textSecondWidth ? textFirstWidth : textSecondWidth;
                labelViewWidth = strBigLen + straightLine + slashWidth + originPic.getWidth() + textHeight * 3 / 2;
                break;
            case SECOND_MODEL:
                //找出最长的字符串
                int strBigLen2 = textFirstWidth > textSecondWidth ? textFirstWidth : textSecondWidth;
                labelViewWidth = strBigLen2 + straightLine + slashWidth + originPic.getWidth() + textHeight * 3 / 2;
                break;
            case THIRD_MODEL:
                break;
            case FOURTH_MODEL:
                break;
        }

        Log.i(TAG, "initView textFirstWidth = " + textFirstWidth + " textSecondWidth = " + textSecondWidth + " textHeight = " + textHeight);
        Log.i(TAG, "initView labelViewWidth = " + labelViewWidth + " labelViewHeight = " + labelViewHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, "--  onDraw -------");
        canvas.save();
        canvas.clipRect(areaLeft, areaTop, areaRight, areaBottom);
        if (currentType == FRIST_MODEL) {
            onDrawFirst(canvas);
        } else if (currentType == SECOND_MODEL) {
            onDrawSecond(canvas);
        } else if (currentType == THIRD_MODEL) {

        } else if (currentType == FOURTH_MODEL) {

        }
        canvas.restore();
    }

    /**
     * 绘制第一模式
     *
     * @param canvas
     */
    private void onDrawFirst(Canvas canvas) {
        Log.i(TAG, "onDrawFirst ---------");
//        Rect bg = new Rect(0, 0, labelViewWidth, labelViewHeight);
//        mPaint.setColor(Color.parseColor("#ffffff"));
//        canvas.drawRect(bg, mPaint);

        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.parseColor("#99000000"));
        //画第一个半圆
        int ovalLeft = labelViewWidth - textHeight / 2 - textFirstWidth - textHeight - straightLine - slashWidth - originPic.getWidth();
        int ovalTop = 0;
        int ovalRight = labelViewWidth - textFirstWidth - textHeight / 2 - straightLine - slashWidth - originPic.getWidth();
        int ovalBottom = textHeight;
        Log.i(TAG, "onDrawFirst ------ ovalLeft = " + ovalLeft + " ovalTop = " + ovalTop + " ovalRight = " + ovalRight + " ovalBottom = " + ovalBottom);
        RectF oval = new RectF(ovalLeft, ovalTop, ovalRight, ovalBottom);
        canvas.drawArc(oval, 270, -180, true, mPaint);

        //画第二个半圆
        int oval2Left = labelViewWidth - textHeight / 2 - textSecondWidth - textHeight - straightLine - slashWidth - originPic.getWidth();
        int oval2Top = labelViewHeight - textHeight;
        int oval2Right = labelViewWidth - textSecondWidth - textHeight / 2 - straightLine - slashWidth - originPic.getWidth();
        int oval2Bottom = labelViewHeight;
        Log.i(TAG, "onDrawFirst ------ oval2Left = " + oval2Left + " oval2Top = " + oval2Top + " oval2Right = " + oval2Right + " oval2Bottom = " + oval2Bottom);
        RectF oval2 = new RectF(oval2Left, oval2Top, oval2Right, oval2Bottom);
        canvas.drawArc(oval2, 270, -180, true, mPaint);

        //画第一个矩形
        int targetRectLeft = labelViewWidth - textFirstWidth - textHeight - straightLine - slashWidth - originPic.getWidth();
        int targetRectTop = 0;
        int targetRectRight = labelViewWidth - textHeight - straightLine - slashWidth - originPic.getWidth();
        int targetRectBottom = textHeight;
        Log.i(TAG, "onDrawFirst ------ targetRectLeft = " + targetRectLeft + " targetRectTop = " + targetRectTop + " targetRectRight = " + targetRectRight + " targetRectBottom = " + targetRectBottom);
        Rect targetRect = new Rect(targetRectLeft, targetRectTop, targetRectRight, targetRectBottom);
        canvas.drawRect(targetRect, mPaint);
        //画第二个矩形
        int targetRect2Left = labelViewWidth - textSecondWidth - textHeight - straightLine - slashWidth - originPic.getWidth();
        int targetRect2Top = labelViewHeight - textHeight;
        int targetRect2Right = labelViewWidth - textHeight - straightLine - slashWidth - originPic.getWidth();
        int targetRect2Bottom = labelViewHeight;
        Log.i(TAG, "onDrawFirst ------ targetRect2Left = " + targetRect2Left + " targetRect2Top = " + targetRect2Top + " targetRect2Right = " + targetRect2Right + " targetRect2Bottom = " + targetRect2Bottom);
        Rect targetRect2 = new Rect(targetRect2Left, targetRect2Top, targetRect2Right, targetRect2Bottom);
        canvas.drawRect(targetRect2, mPaint);
        //画第一个三角形
        Path path = new Path();
        path.moveTo(labelViewWidth - textHeight - straightLine - slashWidth - originPic.getWidth(), 0);// 此点为多边形的起点
        path.lineTo(labelViewWidth - straightLine - slashWidth - originPic.getWidth(), textHeight / 2);
        path.lineTo(labelViewWidth - textHeight - straightLine - slashWidth - originPic.getWidth(), textHeight);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, mPaint);
        //画第二个三角形
        Path path2 = new Path();
        path2.moveTo(labelViewWidth - textHeight - straightLine - slashWidth - originPic.getWidth(), labelViewHeight - textHeight);// 此点为多边形的起点
        path2.lineTo(labelViewWidth - straightLine - slashWidth - originPic.getWidth(), labelViewHeight - textHeight / 2);
        path2.lineTo(labelViewWidth - textHeight - straightLine - slashWidth - originPic.getWidth(), labelViewHeight);
        path2.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path2, mPaint);

        //画第一条直线
        int lineSx = labelViewWidth - straightLine - slashWidth - originPic.getWidth() - 4;
        int lintSy = textHeight / 2;
        int lintEx = labelViewWidth - slashWidth - originPic.getWidth();
        int lintEy = textHeight / 2;
        canvas.drawLine(lineSx, lintSy, lintEx, lintEy, mPaint);// 画线

        //画第二条直线
        lineSx = labelViewWidth - straightLine - slashWidth - originPic.getWidth() - 4;
        lintSy = labelViewHeight - textHeight / 2;
        lintEx = labelViewWidth - slashWidth - originPic.getWidth();
        lintEy = labelViewHeight - textHeight / 2;
        canvas.drawLine(lineSx, lintSy, lintEx, lintEy, mPaint);// 画线


        //画第一条斜线
        lineSx = labelViewWidth - slashWidth - originPic.getWidth();
        lintSy = textHeight / 2;
        lintEx = labelViewWidth - originPic.getWidth()/2;
        lintEy = labelViewHeight / 2;
        canvas.drawLine(lineSx, lintSy, lintEx, lintEy, mPaint);// 画线


        //画第二条斜线
        lineSx = labelViewWidth - slashWidth - originPic.getWidth();
        lintSy = labelViewHeight - textHeight / 2;
        lintEx = labelViewWidth - originPic.getWidth()/2;
        lintEy = labelViewHeight / 2;
        canvas.drawLine(lineSx, lintSy, lintEx, lintEy, mPaint);// 画线

        //写第一个文字
        mPaint.setColor(Color.parseColor("#ffffff"));
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(labelTextList.get(0), targetRect.centerX(), baseline, mPaint);
        //写第二个文字
        Paint.FontMetricsInt fontMetrics2 = mPaint.getFontMetricsInt();
        int baseline2 = (targetRect2.bottom + targetRect2.top - fontMetrics2.bottom - fontMetrics2.top) / 2;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(labelTextList.get(1), targetRect2.centerX(), baseline2, mPaint);


        //画原点
        int xBmp = labelViewWidth - originPic.getWidth();
        int yBmp = (labelViewHeight - originPic.getWidth()) / 2;
        canvas.drawBitmap(originPic, xBmp, yBmp, null);
        Log.i(TAG, "onDrawFirst xBmp = " + xBmp + " yBmp = " + yBmp);
    }

    /**
     * 绘制第二模式
     *
     * @param canvas
     */
    private void onDrawSecond(Canvas canvas) {
        Log.i(TAG, "onDrawSecond ---------");
//        Rect bg = new Rect(0, 0, labelViewWidth, labelViewHeight);
//        mPaint.setColor(Color.parseColor("#ffffff"));
//        canvas.drawRect(bg, mPaint);

        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.parseColor("#99000000"));
        //画第一个半圆
        int ovalLeft = originPic.getWidth() + slashWidth + straightLine + textHeight + textFirstWidth-textHeight/2;
        int ovalTop = 0;
        int ovalRight = originPic.getWidth() + slashWidth + straightLine + textHeight + textFirstWidth+textHeight/2;
        int ovalBottom = textHeight;
        Log.i(TAG, "onDrawSecond ------ ovalLeft = " + ovalLeft + " ovalTop = " + ovalTop + " ovalRight = " + ovalRight + " ovalBottom = " + ovalBottom);
        RectF oval = new RectF(ovalLeft, ovalTop, ovalRight, ovalBottom);
        canvas.drawArc(oval, 90, -180, true, mPaint);

        //画第二个半圆
        int oval2Left = originPic.getWidth() + slashWidth + straightLine + textHeight + textSecondWidth - textHeight/2;
        int oval2Top = labelViewHeight - textHeight;
        int oval2Right = originPic.getWidth() + slashWidth + straightLine + textHeight + textSecondWidth + textHeight/2;
        int oval2Bottom = labelViewHeight;
        Log.i(TAG, "onDrawSecond ------ oval2Left = " + oval2Left + " oval2Top = " + oval2Top + " oval2Right = " + oval2Right + " oval2Bottom = " + oval2Bottom);
        RectF oval2 = new RectF(oval2Left, oval2Top, oval2Right, oval2Bottom);
        canvas.drawArc(oval2, 90, -180, true, mPaint);

        //画第一个矩形
        int targetRectLeft = originPic.getWidth() + slashWidth + straightLine + textHeight;
        int targetRectTop = 0;
        int targetRectRight = originPic.getWidth() + slashWidth + straightLine + textHeight + textFirstWidth;
        int targetRectBottom = textHeight;
        Log.i(TAG, "onDrawSecond ------ targetRectLeft = " + targetRectLeft + " targetRectTop = " + targetRectTop + " targetRectRight = " + targetRectRight + " targetRectBottom = " + targetRectBottom);
        Rect targetRect = new Rect(targetRectLeft, targetRectTop, targetRectRight, targetRectBottom);
        canvas.drawRect(targetRect, mPaint);
        //画第二个矩形
        int targetRect2Left = originPic.getWidth() + slashWidth + straightLine + textHeight;
        int targetRect2Top = labelViewHeight - textHeight;
        int targetRect2Right = originPic.getWidth() + slashWidth + straightLine + textHeight + textSecondWidth;
        int targetRect2Bottom = labelViewHeight;
        Log.i(TAG, "onDrawSecond ------ targetRect2Left = " + targetRect2Left + " targetRect2Top = " + targetRect2Top + " targetRect2Right = " + targetRect2Right + " targetRect2Bottom = " + targetRect2Bottom);
        Rect targetRect2 = new Rect(targetRect2Left, targetRect2Top, targetRect2Right, targetRect2Bottom);
        canvas.drawRect(targetRect2, mPaint);
        //画第一个三角形
        Path path = new Path();
        path.moveTo(originPic.getWidth() + slashWidth + straightLine + textHeight, 0);// 此点为多边形的起点
        path.lineTo(originPic.getWidth() + slashWidth + straightLine, textHeight / 2);
        path.lineTo(originPic.getWidth() + slashWidth + straightLine + textHeight, textHeight);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, mPaint);
        //画第二个三角形
        Path path2 = new Path();
        path2.moveTo(originPic.getWidth() + slashWidth + straightLine + textHeight, labelViewHeight - textHeight);// 此点为多边形的起点
        path2.lineTo(originPic.getWidth() + slashWidth + straightLine , labelViewHeight - textHeight / 2);
        path2.lineTo(originPic.getWidth() + slashWidth + straightLine + textHeight, labelViewHeight);
        path2.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path2, mPaint);

        //画第一条直线
        int lineSx = originPic.getWidth() + slashWidth;
        int lintSy = textHeight / 2;
        int lintEx = originPic.getWidth() + slashWidth + straightLine+4;
        int lintEy = textHeight / 2;
        canvas.drawLine(lineSx, lintSy, lintEx, lintEy, mPaint);// 画线

        //画第二条直线
        lineSx = originPic.getWidth() + slashWidth;
        lintSy = labelViewHeight - textHeight / 2;
        lintEx = originPic.getWidth() + slashWidth + straightLine+4;
        lintEy = labelViewHeight - textHeight / 2;
        canvas.drawLine(lineSx, lintSy, lintEx, lintEy, mPaint);// 画线


        //画第一条斜线
        lineSx = originPic.getWidth()/2;
        lintSy = labelViewHeight / 2;
        lintEx = originPic.getWidth() + slashWidth;
        lintEy = textHeight / 2;
        canvas.drawLine(lineSx, lintSy, lintEx, lintEy, mPaint);// 画线


        //画第二条斜线
        lineSx = originPic.getWidth()/2;
        lintSy = labelViewHeight / 2;
        lintEx = originPic.getWidth() + slashWidth;
        lintEy = labelViewHeight - textHeight / 2;
        canvas.drawLine(lineSx, lintSy, lintEx, lintEy, mPaint);// 画线

        //写第一个文字
        mPaint.setColor(Color.parseColor("#ffffff"));
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(labelTextList.get(0), targetRect.centerX(), baseline, mPaint);
        //写第二个文字
        Paint.FontMetricsInt fontMetrics2 = mPaint.getFontMetricsInt();
        int baseline2 = (targetRect2.bottom + targetRect2.top - fontMetrics2.bottom - fontMetrics2.top) / 2;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(labelTextList.get(1), targetRect2.centerX(), baseline2, mPaint);


        //画原点
        int xBmp = 0;
        int yBmp = (labelViewHeight-originPic.getWidth())/2;
        canvas.drawBitmap(originPic, xBmp, yBmp, null);
        Log.i(TAG, "onDrawSecond xBmp = " + xBmp + " yBmp = " + yBmp);
    }


    @Override
    public int getLayoutLeft() {
        if (currentType == FRIST_MODEL) {
            return xPrevious + xOffset - labelViewWidth;
        } else if (currentType == SECOND_MODEL) {
            return xPrevious + xOffset;
        } else if (currentType == THIRD_MODEL) {
            return xPrevious + xOffset - labelViewWidth / 2;
        } else if (currentType == FOURTH_MODEL) {
            return xPrevious + xOffset - labelViewWidth / 2;
        }
        return 0;
    }

    @Override
    public int getLayoutTop() {
        return yPrevious + yOffset - labelViewHeight / 2;
    }

    @Override
    public int getLayoutRight() {
        if (currentType == FRIST_MODEL) {
            return xPrevious + xOffset;
        } else if (currentType == SECOND_MODEL) {
            return xPrevious + xOffset + labelViewWidth;
        } else if (currentType == THIRD_MODEL) {
            return xPrevious + xOffset + labelViewWidth / 2;
        } else if (currentType == FOURTH_MODEL) {
            return xPrevious + xOffset + labelViewWidth / 2;
        }
        return 0;
    }

    @Override
    public int getLayoutBottom() {
        return yPrevious + yOffset + labelViewHeight / 2;
    }

    @Override
    public void switchType() {
        if (currentType == FRIST_MODEL) {
            currentType = SECOND_MODEL;
        } else if (currentType == SECOND_MODEL) {
//            currentType = THIRD_MODEL;
            currentType = FRIST_MODEL;
        } else if (currentType == THIRD_MODEL) {
            currentType = FOURTH_MODEL;
        } else if (currentType == FOURTH_MODEL) {
            currentType = FRIST_MODEL;
        }
        initView();
        invalidate();
    }

    @Override
    public void showCurrent() {
//        if (currentType == FRIST_MODEL) {
//            areaLeft = 0;
//            areaTop = 0;
//            areaRight = labelViewWidth;
//            areaBottom = labelViewHeight;
//        } else if (currentType == SECOND_MODEL) {
//            areaLeft = 0;
//            areaTop = 0;
//            areaRight = labelViewWidth;
//            areaBottom = labelViewHeight;
//        }else if(currentType == THIRD_MODEL){
//
//        }else if(currentType == FOURTH_MODEL){
//
//        }
        areaLeft = 0;
        areaTop = 0;
        areaRight = labelViewWidth;
        areaBottom = labelViewHeight;
        invalidate();
    }

    @Override
    public boolean clickInLabelView(int x, int y) {
        if (y > yPrevious - labelViewHeight / 2 && y < yPrevious + labelViewHeight / 2) {
            if (currentType == FRIST_MODEL) {
                if (x > xPrevious - labelViewWidth && x < xPrevious) {
                    return true;
                } else {
                    return false;
                }
            } else if (currentType == SECOND_MODEL) {
                if (x > xPrevious && x < xPrevious + labelViewWidth) {
                    return true;
                } else {
                    return false;
                }
            } else if (currentType == THIRD_MODEL) {
                if (x > xPrevious - labelViewWidth / 2 && x < xPrevious + labelViewWidth / 2) {
                    return true;
                } else {
                    return false;
                }
            } else if (currentType == FOURTH_MODEL) {
                if (x > xPrevious - labelViewWidth / 2 && x < xPrevious + labelViewWidth / 2) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public boolean moveLabelView() {
        if (Math.abs(xOffset) > MOVE_OFFSET || Math.abs(yOffset) > MOVE_OFFSET) {
            return true;
        }
        return false;
    }

    @Override
    public boolean clickInCenter(int x, int y) {
        if (Math.abs(xPrevious - x) < CENTER_RADIUS && Math.abs(yPrevious - y) < CENTER_RADIUS) {
            return true;
        }
        return false;
    }

    @Override
    public void refreshLabelOffset(int xOff, int yOff) {
        this.xOffset = xOff;
        this.yOffset = yOff;
    }

    @Override
    public void refreshLabelCenterPoint(int xOff, int yOff) {
        this.xPrevious = this.xPrevious + xOff;
        this.yPrevious = this.yPrevious + yOff;
        this.xOffset = 0;
        this.yOffset = 0;
    }

    @Override
    public void setLabelTextList(List<String> textList) {
        if (textList != null && textList.size() == 2) {
            this.labelTextList.addAll(textList);
        }
        initView();
        invalidate();
    }

    @Override
    public List<String> getLabelTextList() {
        return labelTextList;
    }

    @Override
    public void refreshLabelVisibleArea(int animValue) {
        if (currentType == FRIST_MODEL) {
            areaLeft = animValue;
            areaTop = 0;
            areaRight = labelViewWidth;
            areaBottom = labelViewHeight;
        } else if (currentType == SECOND_MODEL) {
            areaLeft = 0;
            areaTop = 0;
            areaRight = animValue;
            areaBottom = labelViewHeight;
        } else if (currentType == THIRD_MODEL) {
            areaLeft = animValue;
            areaTop = 0;
            areaRight = animValue;
            areaBottom = labelViewHeight;
        } else if (currentType == FOURTH_MODEL) {
            areaLeft = animValue;
            areaTop = 0;
            areaRight = animValue;
            areaBottom = labelViewHeight;
        }
        invalidate();
    }

    @Override
    public ValueAnimator getLabelShowAnim() {
        ValueAnimator anim = null;
        if (currentType == FRIST_MODEL) {
            anim = ValueAnimator.ofInt(labelViewWidth, 0);
        } else if (currentType == SECOND_MODEL) {
            anim = ValueAnimator.ofInt(0, labelViewWidth);
        } else if (currentType == THIRD_MODEL) {
            anim = ValueAnimator.ofInt(0, labelViewWidth / 2);
        } else if (currentType == FOURTH_MODEL) {
            anim = ValueAnimator.ofInt(0, labelViewWidth / 2);
        }
        if (anim == null) {
            return null;
        }
        anim.setDuration(500);
        return anim;
    }

    @Override
    public ValueAnimator getLabelHideAnim() {
        ValueAnimator anim = null;
        if (currentType == FRIST_MODEL) {
            anim = ValueAnimator.ofInt(0, labelViewWidth);
        } else if (currentType == SECOND_MODEL) {
            anim = ValueAnimator.ofInt(labelViewWidth, 0);
        } else if (currentType == THIRD_MODEL) {
            anim = ValueAnimator.ofInt(labelViewWidth / 2, 0);
        } else if (currentType == FOURTH_MODEL) {
            anim = ValueAnimator.ofInt(labelViewWidth / 2, 0);
        }
        if (anim == null) {
            return null;
        }
        anim.setDuration(500);
        return anim;
    }

    @Override
    public int getProitX() {
        return xPrevious;
    }

    @Override
    public int getProitY() {
        return yPrevious;
    }
}
