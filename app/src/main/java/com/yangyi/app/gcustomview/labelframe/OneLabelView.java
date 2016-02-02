package com.yangyi.app.gcustomview.labelframe;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;

import com.yangyi.app.gcustomview.R;
import com.yangyi.app.gcustomview.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GYH on 2016/1/4.
 */
public class OneLabelView extends View implements LabelViewListener {

    private static final String TAG = "OneLabelView";

    //存在两种模式切换
    private static final int FRIST_MODEL = 0;
    private static final int SECOND_MODEL = 1;
    //移动偏移量阀值
    private static final int MOVE_OFFSET = 20;
    //中心点击半径
    private static final int CENTER_RADIUS = 60;
//    private static final int VIEW_PADDING = 20;
    //当前模式
    public int currentType = FRIST_MODEL;

    private int labelViewHeight = 0;//子视图的高
    private int labelViewWidth = 0;//子视图的宽

    private int xPrevious;//子视图上一个x坐标
    private int yPrevious;//子视图上一个y坐标

    //偏移量
    private int xOffset;
    private int yOffset;
    //可见区域变化
    private int areaLeft = 0;
    private int areaTop = 0;
    private int areaRight = 0;
    private int areaBottom = 0;

    private Paint mPaint;
    private LabelTextView labelTextView;

    //原点图片
    private BitmapDrawable bmpDraw = (BitmapDrawable) getResources().getDrawable(R.mipmap.kkkk);
    private Bitmap originPic = bmpDraw.getBitmap();

    public OneLabelView(Context context, String text, int x, int y) {
        super(context);
        setWillNotDraw(false);
        this.xPrevious = x;
        this.yPrevious = y;
        labelTextView = new LabelTextView(text, LabelTextView.FRIST_TYPE);
        originPic = Utils.drawableToBitmap(new LabelRoundDot(originPic));
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        initWidthHeight();
    }
    /**
     * 初始化
     */
    private void initWidthHeight() {
        labelViewHeight =labelTextView.getIntrinsicHeight();
        labelViewWidth = labelTextView.getIntrinsicWidth() + originPic.getWidth();
        Log.i(TAG, "initWidthHeight labelViewWidth = " + labelViewWidth + " labelViewHeight = " + labelViewHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Log.i(TAG, "--  onDraw -------");
//        Rect bg = new Rect(0, 0, labelViewWidth, labelViewHeight);
//        mPaint.setColor(Color.parseColor("#ffffff"));
//        canvas.drawRect(bg, mPaint);
        canvas.save();
        canvas.clipRect(areaLeft, areaTop, areaRight, areaBottom);
        if (currentType == FRIST_MODEL) {
            onDrawFirst(canvas);
        } else if (currentType == SECOND_MODEL) {
            onDrawSecond(canvas);
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
        canvas.drawBitmap(Utils.drawableToBitmap(labelTextView),4,0,mPaint);
        //画原点
        int xBmp = labelViewWidth-originPic.getWidth();
        int yBmp = (labelViewHeight-originPic.getWidth())/2;
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
        canvas.drawBitmap(Utils.drawableToBitmap(labelTextView), originPic.getWidth()-4, 0, mPaint);
        //画原点
        int xBmp = 0;
        int yBmp = (labelViewHeight-originPic.getWidth())/2;
        canvas.drawBitmap(originPic, xBmp, yBmp, null);
        Log.i(TAG, "onDrawFirst xBmp = " + xBmp + " yBmp = " + yBmp);
    }


    @Override
    public int getLayoutLeft() {
        if (currentType == FRIST_MODEL) {
            return xPrevious + xOffset - labelViewWidth+originPic.getWidth()/2;
        } else if (currentType == SECOND_MODEL) {
            return xPrevious + xOffset - originPic.getWidth()/2;
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
            return xPrevious + xOffset+originPic.getWidth()/2;
        } else if (currentType == SECOND_MODEL) {
            return xPrevious + xOffset + labelViewWidth -originPic.getWidth()/2;
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
            labelTextView.setCurrentType(LabelTextView.SECOND_TYPE);
        } else if (currentType == SECOND_MODEL) {
            currentType = FRIST_MODEL;
            labelTextView.setCurrentType(LabelTextView.FRIST_TYPE);
        }
        invalidate();
    }

    @Override
    public void showCurrent() {
        if (currentType == FRIST_MODEL) {
            areaLeft = 0;
            areaTop = 0;
            areaRight = labelViewWidth;
            areaBottom = labelViewHeight;
        } else if (currentType == SECOND_MODEL) {
            areaLeft = 0;
            areaTop = 0;
            areaRight = labelViewWidth;
            areaBottom = labelViewHeight;
        }
        invalidate();
    }

    @Override
    public boolean clickInLabelView(int x, int y) {
        if (y > yPrevious - labelViewHeight / 2 && y < yPrevious + labelViewHeight / 2) {
            if (currentType == FRIST_MODEL) {
                if (x > xPrevious - labelViewWidth +originPic.getWidth()/2 && x < xPrevious +originPic.getWidth()/2) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (x > xPrevious -originPic.getWidth()/2&& x < xPrevious + labelViewWidth - originPic.getWidth()/2) {
                    return true;
                } else {
                    return false;
                }
            }
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
        if (textList != null && textList.size() > 0) {
            labelTextView.setLabelText(textList.get(0));
            initWidthHeight();
            showCurrent();
        }
    }

    @Override
    public List<String> getLabelTextList() {
        List<String> strings = new ArrayList<>();
        strings.add(labelTextView.getLabelText());
        return strings;
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
        return yPrevious
                ;
    }
}
