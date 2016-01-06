package com.yangyi.app.gcustomview.test3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.List;


/**
 * Created by GYH on 2015/12/31.
 */
public class TagFlickerView extends View implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener ,TagFilckerListener{

    private Paint paint;//画笔
    private ValueAnimator valueAnimator;//动画效果
    private int circleRadius;//画圆的半径
    private int d;
    private float anpla;//透明的
    private boolean staterunning;//判断当前动画是否在运行
    private boolean g = true;
    private List<TagFlickerViewHelp> tagFlickerViewHelps;

    public TagFlickerView(Context context) {
        super(context);
        init();
    }

    public TagFlickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 绘制圆
     *
     * @param canvas
     * @param tagFlickerViewHelp
     */
    private void toDrawCircle(Canvas canvas, TagFlickerViewHelp tagFlickerViewHelp) {
        canvas.drawCircle(tagFlickerViewHelp.xCircle, tagFlickerViewHelp.yCircle, this.circleRadius, this.paint);
    }

    /**
     * 开启动画
     *
     * @param isRun
     */
    private void startAnimator(boolean isRun) {
        if (isRun) {
            this.staterunning = true;
        }
        if (!isAnimatorRunning()) {
            this.circleRadius = 0;
            this.valueAnimator.start();
        }
    }

    /**
     * 结算动画
     *
     * @param isStop
     */
    private void stopAnimator(boolean isStop) {
        if (isStop) {
            this.staterunning = false;
        }
        if (isAnimatorRunning()) {
            this.valueAnimator.cancel();
            invalidate();
        }
    }


    /**
     * 初始化
     */
    private void init() {
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setColor(-1);
        this.paint.setStyle(Paint.Style.FILL);
        this.valueAnimator = new ValueAnimator();
        this.valueAnimator.setInterpolator(new DecelerateInterpolator());
        this.valueAnimator.addUpdateListener(this);
        this.valueAnimator.addListener(this);
        this.valueAnimator.setRepeatCount(-1);
        this.valueAnimator.setRepeatMode(1);
    }

    /**
     * 判断动画是否在运行
     *
     * @return
     */
    private boolean isAnimatorRunning() {
        return (this.valueAnimator != null && this.valueAnimator.isRunning());
    }

    private String getTagName() {
        return getClass().getSimpleName();
    }

    /**
     * 开启动画
     */
    public void startFilckerAnim() {
        Log.v(getTagName(), "start filcker anim is runnin=" + this.valueAnimator.isRunning() + ", " +
                "is started=" + this.valueAnimator.isStarted() + ", staterunning=" + this.staterunning);
        startAnimator(true);
    }


    /**
     * 重置动画
     *
     * @param anmimatorVal
     * @param animatorDuration
     * @param i3
     */
    public void resetFileckerAnim(int anmimatorVal, int animatorDuration, int i3) {
        stopFilckerAnim();
        this.anpla = (255.0F / anmimatorVal);
        this.valueAnimator.setIntValues(new int[]{1, anmimatorVal});
        this.valueAnimator.setDuration(animatorDuration);
    }

    /**
     * 停止动画
     */
    public void stopFilckerAnim() {
        Log.v(getTagName(), "stop filcker anim is runnin=" + this.valueAnimator.isRunning() + ", " +
                "is started=" + this.valueAnimator.isStarted() + ", staterunning=" + this.staterunning);
        stopAnimator(true);
    }


    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        int animatedValue = (Integer) animation.getAnimatedValue();
        if (getWindowVisibility() == VISIBLE) {
            if (this.circleRadius != animatedValue) {
                this.circleRadius = animatedValue;
                int alpha = 240 - (int) (this.circleRadius * this.anpla);
                if (alpha < 0)
                    alpha = 0;
                this.paint.setAlpha(alpha);
                invalidate();
            }
            return;
        }
        stopAnimator(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(tagFlickerViewHelps != null){
            for(int i =0;i<tagFlickerViewHelps.size();i++){
                toDrawCircle(canvas,tagFlickerViewHelps.get(i));
            }
        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if(visibility == VISIBLE && this.staterunning){
            startFilckerAnim();
        }
        if(visibility == GONE){
            stopAnimator(false);
        }

    }

    @Override
    public void startAnimation(List<TagFlickerViewHelp> tagFlickerViewHelps) {

    }

    @Override
    public void resetAnimation(List<TagFlickerViewHelp> tagFlickerViewHelps) {
        this.tagFlickerViewHelps = tagFlickerViewHelps;
        stopFilckerAnim();
        invalidate();
        startFilckerAnim();
    }

    @Override
    public void stopAnimation(List<TagFlickerViewHelp> tagFlickerViewHelps) {
        stopFilckerAnim();
    }
}
