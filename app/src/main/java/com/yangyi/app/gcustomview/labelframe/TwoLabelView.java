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
//    private int textFirstWidth = 0;
//    private int textSecondWidth = 0;
//    private int textHeight = 0;
//    //斜线的高度
//    private static final int slashHeight = 70;
//    private static final int slashWidth = 30;
//    //直线的长度
//    private static final int straightLine = 50;

    private LabelTextView labelTextViewFirst;
    private LabelTextView labelTextViewSecond;
    private LabelLineView labelLineViewFirst;
    private LabelLineView labelLineViewSecond;

    //原点图片
    private BitmapDrawable bmpDraw = (BitmapDrawable) getResources().getDrawable(R.mipmap.kkkk);
    private Bitmap originPic = bmpDraw.getBitmap();

    public TwoLabelView(Context context, List<String> text, int x, int y) {
        super(context);
        setWillNotDraw(false);
        this.xPrevious = x;
        this.yPrevious = y;

        this.labelTextList.addAll(text);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        originPic = Utils.drawableToBitmap(new LabelRoundDot(originPic));
        labelLineViewFirst = new LabelLineView(LabelLineView.FRIST_TYPE);
        labelLineViewSecond = new LabelLineView(LabelLineView.SECOND_TYPE);
        labelTextViewFirst = new LabelTextView(labelTextList.get(0),LabelTextView.FRIST_TYPE);
        labelTextViewSecond = new LabelTextView(labelTextList.get(1),LabelTextView.FRIST_TYPE);
        initWidthHeight();
    }

    /**
     * 初始化
     */
    private void initWidthHeight() {
        switch (currentType) {
            case FRIST_MODEL:
                //找出最长的字符串
                labelViewWidth = (labelTextViewFirst.getIntrinsicWidth()>labelTextViewSecond.getIntrinsicWidth()?
                        labelTextViewFirst.getIntrinsicWidth():labelTextViewSecond.getIntrinsicWidth())
                        +labelLineViewFirst.getIntrinsicWidth()+originPic.getWidth();
                labelViewHeight = labelTextViewFirst.getIntrinsicHeight()+labelLineViewFirst.getIntrinsicHeight()*2;
                break;
            case SECOND_MODEL:
                //找出最长的字符串
                labelViewWidth = (labelTextViewFirst.getIntrinsicWidth()>labelTextViewSecond.getIntrinsicWidth()?
                        labelTextViewFirst.getIntrinsicWidth():labelTextViewSecond.getIntrinsicWidth())
                        +labelLineViewFirst.getIntrinsicWidth()+originPic.getWidth();
                labelViewHeight = labelTextViewFirst.getIntrinsicHeight()+labelLineViewFirst.getIntrinsicHeight()*2;
                break;
            case THIRD_MODEL:
                labelViewWidth = labelTextViewFirst.getIntrinsicWidth() + labelLineViewFirst .getIntrinsicWidth()
                        + labelTextViewSecond.getIntrinsicWidth() + labelLineViewSecond.getIntrinsicWidth()+originPic.getWidth();
                labelViewHeight = labelTextViewFirst.getIntrinsicHeight()+labelLineViewFirst.getIntrinsicHeight()*2;
                break;
            case FOURTH_MODEL:
                labelViewWidth = labelTextViewFirst.getIntrinsicWidth() + labelLineViewFirst .getIntrinsicWidth()
                        + labelTextViewSecond.getIntrinsicWidth() + labelLineViewSecond.getIntrinsicWidth()+originPic.getWidth();
                labelViewHeight = labelTextViewFirst.getIntrinsicHeight()+labelLineViewFirst.getIntrinsicHeight()*2;
                break;
        }
        Log.i(TAG, "initWidthHeight labelViewWidth = " + labelViewWidth + " labelViewHeight = " + labelViewHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, "--  onDraw -------");
//        Rect bg = new Rect(0, 0, labelViewWidth, labelViewHeight);
//        mPaint.setColor(Color.parseColor("#ffffff"));
//        canvas.drawRect(bg, mPaint);

        canvas.save();
        canvas.clipRect(areaLeft, areaTop, areaRight, areaBottom);
        if (currentType == FRIST_MODEL) {
            onDrawFirst(canvas);
        } else if (currentType == SECOND_MODEL) {
            onDrawSecond(canvas);
        } else if (currentType == THIRD_MODEL) {
            onDrawThird(canvas);
        } else if (currentType == FOURTH_MODEL) {
            onDrawFourth(canvas);
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

        //上面的文字
        canvas.drawBitmap(Utils.drawableToBitmap(labelTextViewFirst),
                labelViewWidth-originPic.getWidth()-labelLineViewFirst.getIntrinsicWidth()-labelTextViewFirst.getIntrinsicWidth(),
                0,mPaint);
        //上面的线条
        canvas.drawBitmap(Utils.drawableToBitmap(labelLineViewFirst),
                labelViewWidth-originPic.getWidth()-labelLineViewFirst.getIntrinsicWidth(),
                labelTextViewFirst.getIntrinsicHeight()/2,
                mPaint);
        //下面的文字
        canvas.drawBitmap(Utils.drawableToBitmap(labelTextViewSecond),
                labelViewWidth - originPic.getWidth() - labelLineViewSecond.getIntrinsicWidth() - labelTextViewSecond.getIntrinsicWidth(),
                labelViewHeight - labelTextViewSecond.getIntrinsicHeight(),
                mPaint);
        //下面的线条
        canvas.drawBitmap(Utils.drawableToBitmap(labelLineViewSecond),
                labelViewWidth - originPic.getWidth() - labelLineViewSecond.getIntrinsicWidth(),
                labelTextViewFirst.getIntrinsicHeight() / 2 + labelLineViewFirst.getIntrinsicHeight(),
                mPaint);
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

        //上面条线
        canvas.drawBitmap(Utils.drawableToBitmap(labelLineViewFirst),
                originPic.getWidth(),
                labelTextViewFirst.getIntrinsicHeight()/2,
                mPaint);
        //上面文字
        canvas.drawBitmap(Utils.drawableToBitmap(labelTextViewFirst),
               originPic.getWidth()+labelLineViewFirst.getIntrinsicWidth(),
                0,
                mPaint);
        //下面线条
        canvas.drawBitmap(Utils.drawableToBitmap(labelLineViewSecond),
                originPic.getWidth(),
                labelTextViewFirst.getIntrinsicHeight()/2+labelLineViewFirst.getIntrinsicHeight(),
                mPaint);
        //下面文字
        canvas.drawBitmap(Utils.drawableToBitmap(labelTextViewSecond),
                originPic.getWidth()+labelLineViewSecond.getIntrinsicWidth(),
                labelViewHeight - labelTextViewSecond.getIntrinsicHeight(),
                mPaint);

        //画原点
        int xBmp = 0;
        int yBmp = (labelViewHeight-originPic.getWidth())/2;
        canvas.drawBitmap(originPic, xBmp, yBmp, null);
        Log.i(TAG, "onDrawSecond xBmp = " + xBmp + " yBmp = " + yBmp);
    }

    /**
     * 第三种绘制
     */
    private void onDrawThird(Canvas canvas){
        Log.i(TAG, "onDrawSecond ---------");

        //左边的文字
        canvas.drawBitmap(Utils.drawableToBitmap(labelTextViewFirst),
                0,
                0,
                mPaint);

        //上面条线
        canvas.drawBitmap(Utils.drawableToBitmap(labelLineViewFirst),
                labelTextViewFirst.getIntrinsicWidth(),
                labelTextViewFirst.getIntrinsicHeight()/2,
                mPaint);

        //画原点
        int xBmp = labelTextViewFirst.getIntrinsicWidth()+labelLineViewFirst.getIntrinsicWidth();
        int yBmp = (labelViewHeight-originPic.getHeight())/2;
        canvas.drawBitmap(originPic, xBmp, yBmp, null);
        Log.i(TAG, "onDrawSecond xBmp = " + xBmp + " yBmp = " + yBmp);

        //下面线条
        canvas.drawBitmap(Utils.drawableToBitmap(labelLineViewSecond),
                labelTextViewFirst.getIntrinsicWidth()+labelLineViewFirst.getIntrinsicWidth()+originPic.getWidth(),
                labelViewHeight/2,
                mPaint);
        //下面文字
        canvas.drawBitmap(Utils.drawableToBitmap(labelTextViewSecond),
                labelViewWidth - labelTextViewSecond.getIntrinsicWidth(),
                labelViewHeight - labelTextViewSecond.getIntrinsicHeight(),
                mPaint);


    }
    /**
     * 第四种绘制
     */
    private void onDrawFourth(Canvas canvas){
        Log.i(TAG, "onDrawFourth ---------");
        //左边文字
        canvas.drawBitmap(Utils.drawableToBitmap(labelTextViewFirst),
                0,
                labelViewHeight-labelTextViewFirst.getIntrinsicHeight(),
                mPaint);

        //左边条线
        canvas.drawBitmap(Utils.drawableToBitmap(labelLineViewFirst),
                labelTextViewFirst.getIntrinsicWidth(),
                labelViewHeight-labelTextViewFirst.getIntrinsicHeight()/2-labelLineViewFirst.getIntrinsicHeight(),
                mPaint);
        //画原点
        int xBmp = labelTextViewFirst.getIntrinsicWidth()+labelLineViewFirst.getIntrinsicWidth();
        int yBmp = (labelViewHeight-originPic.getWidth())/2;
        canvas.drawBitmap(originPic, xBmp, yBmp, null);
        Log.i(TAG, "onDrawSecond xBmp = " + xBmp + " yBmp = " + yBmp);

        //右边线条
        canvas.drawBitmap(Utils.drawableToBitmap(labelLineViewSecond),
                labelTextViewFirst.getIntrinsicWidth()+labelLineViewFirst.getIntrinsicWidth()+originPic.getWidth(),
                labelTextViewSecond.getIntrinsicHeight()/2,
                mPaint);
        //右边文字
        canvas.drawBitmap(Utils.drawableToBitmap(labelTextViewSecond),
                labelViewWidth-labelTextViewSecond.getIntrinsicWidth(),
                0,
                mPaint);


    }

    @Override
    public int getLayoutLeft() {
        if (currentType == FRIST_MODEL) {
            return xPrevious + xOffset - labelViewWidth + originPic.getWidth()/2;
        } else if (currentType == SECOND_MODEL) {
            return xPrevious + xOffset - originPic.getWidth()/2;
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
            return xPrevious + xOffset + +originPic.getWidth()/2;
        } else if (currentType == SECOND_MODEL) {
            return xPrevious + xOffset + labelViewWidth - originPic.getWidth()/2;
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
            labelTextViewFirst.setCurrentType(LabelTextView.SECOND_TYPE);
            labelTextViewSecond.setCurrentType(LabelTextView.SECOND_TYPE);
            labelLineViewFirst.setCurrentType(LabelLineView.THIRD_TYPE);
            labelLineViewSecond.setCurrentType(LabelLineView.FOURTH_TYPE);
        } else if (currentType == SECOND_MODEL) {
            currentType = THIRD_MODEL;
            labelTextViewFirst.setCurrentType(LabelTextView.FRIST_TYPE);
            labelTextViewSecond.setCurrentType(LabelTextView.SECOND_TYPE);
            labelLineViewFirst.setCurrentType(LabelLineView.FRIST_TYPE);
            labelLineViewSecond.setCurrentType(LabelLineView.FOURTH_TYPE);
        } else if (currentType == THIRD_MODEL) {
            currentType = FOURTH_MODEL;
            labelTextViewFirst.setCurrentType(LabelTextView.FRIST_TYPE);
            labelTextViewSecond.setCurrentType(LabelTextView.SECOND_TYPE);
            labelLineViewFirst.setCurrentType(LabelLineView.SECOND_TYPE);
            labelLineViewSecond.setCurrentType(LabelLineView.THIRD_TYPE);
        } else if (currentType == FOURTH_MODEL) {
            currentType = FRIST_MODEL;
            labelTextViewFirst.setCurrentType(LabelTextView.FRIST_TYPE);
            labelTextViewSecond.setCurrentType(LabelTextView.FRIST_TYPE);
            labelLineViewFirst.setCurrentType(LabelLineView.FRIST_TYPE);
            labelLineViewSecond.setCurrentType(LabelLineView.SECOND_TYPE);
        }
        initWidthHeight();
        invalidate();
    }

    @Override
    public void showCurrent() {
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
            this.labelTextList.clear();
            this.labelTextList.addAll(textList);
            labelTextViewFirst.setLabelText(textList.get(0));
            labelTextViewSecond.setLabelText(textList.get(1));
            initWidthHeight();
            showCurrent();
        }

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
            areaLeft = labelViewWidth/2-animValue;
            areaTop = 0;
            areaRight = labelViewWidth/2 + animValue;
            areaBottom = labelViewHeight;
        } else if (currentType == FOURTH_MODEL) {
            areaLeft = labelViewWidth/2-animValue;
            areaTop = 0;
            areaRight = labelViewWidth/2 + animValue;
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
