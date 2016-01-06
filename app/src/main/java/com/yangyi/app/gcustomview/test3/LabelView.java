package com.yangyi.app.gcustomview.test3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;

import com.yangyi.app.gcustomview.R;

/**
 * Created by GYH on 2015/12/24.
 */
public class LabelView extends View {

    private static final String TAG = "MyClickView";

    public static final int FRIST_TYPE = 0;
    public static final int SECOND_TYPE = 1;

    private static final int MOVE_THRESHOLD = 20;

    private static final int CENTER_RADIUS = 60;

    public int currentType = FRIST_TYPE;

    private int heightView = 300;//子视图的高

    private int widthView = 300;//子视图的宽

    private int xPrevious;//子视图上一个x坐标

    private int yPrevious;//子视图上一个y坐标

    //偏移量
    private int xOffset;
    private int yOffset;
    //内容
    private String text;

    private int textColor;

    private int bgColor;

    private Paint mPaint;

    private Rect mBounds;
    //可见区域变化
    private int areaLeft = 0;
    private int areaTop = 0;
    private int areaRight = 0;
    private int areaBottom = 0;

    //购物车图片
    BitmapDrawable bmpDraw = (BitmapDrawable) getResources().getDrawable(R.mipmap.icon_origin);
    Bitmap bmp = bmpDraw.getBitmap();

    public LabelView(Context context, String text, int x, int y) {
        super(context);
        this.text = text;
        xPrevious = x;
        yPrevious = y;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBounds = new Rect();
        this.currentType = FRIST_TYPE;
        bgColor = getResources().getColor(R.color.colorAccent);
        textColor = Color.parseColor("#111111");
        initBind();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        float textWidth = mBounds.width();
        float textHeight = mBounds.height();
        if(currentType == FRIST_TYPE){
            canvas.clipRect(areaLeft, areaTop, areaRight, areaBottom);
            mPaint.setColor(bgColor);
            canvas.drawRect(0, 0, widthView - bmp.getWidth(), heightView, mPaint);
            mPaint.setColor(textColor);
            canvas.drawText(text, (widthView - bmp.getWidth()) / 2 - textWidth / 2,
                    heightView / 2 + textHeight / 2, mPaint);
            Log.v(TAG, "onDraw drawText x = " +((widthView - bmp.getWidth()) / 2 - textWidth / 2)
                    + "y = " + (heightView / 2 - textHeight / 2));
            canvas.drawBitmap(bmp, widthView-bmp.getWidth(), (heightView-bmp.getWidth())/2, mPaint);
            Log.v(TAG, "onDraw  bmp  x = " + (widthView-bmp.getWidth())
                    + " y = " + ((heightView - bmp.getWidth()) / 2));
        } else {
            canvas.clipRect(areaLeft, areaTop, areaRight, areaBottom);
            mPaint.setColor(bgColor);
            canvas.drawRect(bmp.getWidth(), 0, widthView, heightView, mPaint);
            mPaint.setColor(textColor);
            canvas.drawBitmap(bmp, 0, (heightView - bmp.getWidth()) / 2, mPaint);
            Log.v(TAG, "onDraw  bmp.getWidth()/2 = " + bmp.getWidth() / 2
                    + " bmp.getHeight()/2 = " + bmp.getHeight() / 2);
            canvas.drawText(text, (widthView + bmp.getWidth()) / 2 - textWidth / 2,
                    heightView / 2 + textHeight / 2, mPaint);
            Log.v(TAG, "onDraw getWidth() / 2 - textWidth / 2 = " +(getWidth() / 2 - textWidth / 2)
                    + " getHeight() / 2 + textHeight / 2 = " + (getHeight() / 2 + textHeight / 2));
        }
        Log.v(TAG, "onDraw  currentType = " + currentType);
        canvas.restore();
    }

    /**
     * 初始化高宽
     */
    private void initBind() {
        mPaint.setTextSize(50);
        mPaint.getTextBounds(text, 0, text.length(), mBounds);
        widthView = mBounds.width() + bmp.getWidth()+20;
        heightView = mBounds.height() + 20;
        Log.v(TAG, "initBind widthView = " + widthView + " heightView = " + heightView);
    }

    /**
     * 切换视图类型
     */
    public void switchType() {
        Log.v(TAG,"switchType");
        if (currentType == FRIST_TYPE) {
            currentType = SECOND_TYPE;
            bgColor = getResources().getColor(R.color.colorPrimary);
            textColor = Color.parseColor("#FFFFFF");
        } else {
            currentType = FRIST_TYPE;
            bgColor = getResources().getColor(R.color.colorAccent);
            textColor = Color.parseColor("#111111");
        }
        invalidate();
    }

    /**
     * 判断点击的坐标是否在子视图上
     *
     * @param x
     * @param y
     * @return
     */
    public boolean isClickInView(int x, int y) {
        if(y>yPrevious-heightView/2 && y < yPrevious + heightView/2){
            if(currentType == FRIST_TYPE){
                if(x>xPrevious-widthView && x<xPrevious){
                    return true;
                }else {
                    return false;
                }
            }else {
                if(x>xPrevious && x<xPrevious+widthView ){
                    return true;
                }else {
                    return false;
                }
            }
        }else {
            return false;
        }
    }

    /**
     * 判断是否移动了坐标
     *
     * @return
     */
    public boolean isMoveView() {
        if (Math.abs(xOffset) > MOVE_THRESHOLD || Math.abs(yOffset) > MOVE_THRESHOLD) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否点击中了中心坐标
     *
     * @return
     */
    public boolean isClickCenterView(int x, int y) {
        if (Math.abs(xPrevious - x) < CENTER_RADIUS && Math.abs(yPrevious - y) < CENTER_RADIUS) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否点击的标签
     * @param x
     * @param y
     * @return
     */
    public boolean isClickLabelView(int x,int y){
        if(y>yPrevious-heightView/2 && y < yPrevious + heightView/2){
            if(currentType == FRIST_TYPE){
                if(x>xPrevious-widthView && x<xPrevious-bmp.getWidth()){
                    return true;
                }else {
                    return false;
                }
            }else {
                if(x>xPrevious+bmp.getWidth() && x<xPrevious+widthView ){
                    return true;
                }else {
                    return false;
                }
            }
        }else {
            return false;
        }
    }

    /**
     * 更新
     *
     * @param xOffset
     * @param yOffset
     */
    public void refreshOffset(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    /**
     * 更新最后的坐标
     *
     * @param xOff
     * @param yOff
     */
    public void refreshLastPoint(int xOff, int yOff) {
        this.xPrevious = this.xPrevious + xOff;
        this.yPrevious = this.yPrevious + yOff;
        this.xOffset = 0;
        this.yOffset = 0;
    }


    public void setText(String text) {
        this.text = text;
        initBind();
        invalidate();
    }

    public String getText() {
        return text == null ? "" : text;
    }

    public int getViewLeft() {
        if(currentType == FRIST_TYPE){
            return xPrevious + xOffset - widthView;
        }else {
            return xPrevious + xOffset;
        }
    }

    public int getViewTop() {
        return yPrevious + yOffset - heightView / 2;
    }

    public int getViewRight() {
        if(currentType == FRIST_TYPE){
            return xPrevious + xOffset;
        }else {
            return xPrevious + xOffset + widthView;
        }
    }

    public int getViewBottom() {
        return yPrevious + yOffset + heightView / 2;
    }

    /**
     * 更新可见区域
     * @param areaLeft
     * @param areaTop
     * @param areaRight
     * @param areaBottom
     */
    public void refershArea(int areaLeft,int areaTop,int areaRight, int areaBottom){
        this.areaLeft = areaLeft;
        this.areaTop = areaTop;
        this.areaRight = areaRight;
        this.areaBottom = areaBottom;
        invalidate();
    }



    public int getHeightView() {
        return heightView;
    }

    public int getWidthView() {
        return widthView;
    }
}
