package com.yangyi.app.gcustomview.test4;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

public class PullToZoomListView extends ListView implements AbsListView.OnScrollListener {

    private static final String TAG = "PullToZoomListView";

    private static final int INVALID_VALUE = -1;//重置值

    //自定义加速度动画
    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float interpolator) {
            float f = interpolator - 1.0F;
            return 1.0F + f * (f * (f * (f * f)));
        }
    };
    private int mActivePointerId = INVALID_VALUE;//当前手指的Id
    private FrameLayout mHeaderContainer;//头部
    private int mHeaderHeight;//头部图片的高度
    private ImageView mHeaderImage;//头部图片
    float mLastMotionY = INVALID_VALUE;//最后y坐标
    float mLastScale = INVALID_VALUE;//最后的比例
    float mMaxScale = INVALID_VALUE;//最大的比例
    private OnScrollListener mOnScrollListener;//滑动监听
    private ScalingRunnalable mScalingRunnalable;//动画线程
    private int mScreenHeight;//屏幕高度
    private ImageView mShadow;//阴影遮罩

    public PullToZoomListView(Context paramContext) {
        super(paramContext);
        init(paramContext);
    }

    public PullToZoomListView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init(paramContext);
    }

    public PullToZoomListView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        init(paramContext);
    }

    /**
     * 开启动画
     */
    private void endScraling() {
        if (this.mHeaderContainer.getBottom() >= this.mHeaderHeight) {
            Log.d(TAG, "this.mScalingRunnalable.startAnimation(200L)");
            this.mScalingRunnalable.startAnimation(200L);
        }
    }

    /**
     * 初始化
     * @param paramContext
     */
    private void init(Context paramContext) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) paramContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);

        this.mScreenHeight = metrics.heightPixels;//屏幕高度赋值
        this.mHeaderContainer = new FrameLayout(paramContext);//头部
        this.mHeaderImage = new ImageView(paramContext);//头部图片
        int screenWidth = metrics.widthPixels;//屏幕宽度

        //设置头部View的样式 设置屏幕宽度，最大样式高度为屏幕高度的9/16
        setHeaderViewSize(screenWidth, (int) (9.0F * (screenWidth / 16.0F)));

        this.mShadow = new ImageView(paramContext);//遮罩
        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        this.mShadow.setLayoutParams(layoutParams);//设置遮罩样式
        //头部添加View
        this.mHeaderContainer.addView(this.mHeaderImage);
        this.mHeaderContainer.addView(this.mShadow);
        //添加头部
        addHeaderView(this.mHeaderContainer);
        //初始化返回动画
        this.mScalingRunnalable = new ScalingRunnalable();
        //设置监听
        super.setOnScrollListener(this);
    }

    /**
     * 多点触碰的时候按下,当第0个有手指抬起，再次有手指按下后，将按下的事件的手指指针作为当前手指指针
     *
     * @param motionEvent
     */
    private void onSecondaryPointerUp(MotionEvent motionEvent) {
        Log.d(TAG, "onSecondaryPointerUp motionEvent.getPointerId(0) = " + motionEvent.getPointerId(0));
        Log.d(TAG, "onSecondaryPointerUp this.mActivePointerId = " + this.mActivePointerId);
        if (motionEvent.getPointerId(0) == this.mActivePointerId) {
            this.mLastMotionY = motionEvent.getY(0);
            this.mActivePointerId = motionEvent.getPointerId(0);
        }
        Log.d(TAG, "onSecondaryPointerUp mLastMotionY = " + mLastMotionY);
        Log.d(TAG, "onSecondaryPointerUp mActivePointerId = " + mActivePointerId);
    }

    /**
     * 重置所有数据
     */
    private void reset() {
        this.mActivePointerId = INVALID_VALUE;
        this.mLastMotionY = INVALID_VALUE;
        this.mMaxScale = INVALID_VALUE;
        this.mLastScale = INVALID_VALUE;
    }

    /**
     * 获取顶部布局
     *
     * @return
     */
    public ImageView getHeaderView() {
        return this.mHeaderImage;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
        return super.onInterceptTouchEvent(paramMotionEvent);
    }

    @Override
    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2,
                            int paramInt3, int paramInt4) {
        super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
        if (this.mHeaderHeight == 0) {//设置头部高度
            this.mHeaderHeight = this.mHeaderContainer.getHeight();
        }
        Log.d(TAG, "onLayout mHeaderHeight = " + mHeaderHeight);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.d(TAG, "onScroll");

        float bottomSpacing = this.mHeaderHeight - this.mHeaderContainer.getBottom();
        Log.d(TAG, "onScroll bottomSpacing = " + bottomSpacing);

        if ((bottomSpacing > 0.0F) && (bottomSpacing < this.mHeaderHeight)) {//如果是向上滑动
            int toUpScroll = (int) (0.65D * bottomSpacing);
            this.mHeaderImage.scrollTo(0, -toUpScroll);
            Log.d(TAG, "onScroll 向上滑动  toUpScroll = " + toUpScroll);
        } else if (this.mHeaderImage.getScrollY() != 0) {
            Log.d(TAG, "onScroll this.mHeaderImage.getScrollY() = " + this.mHeaderImage.getScrollY());
            this.mHeaderImage.scrollTo(0, 0);
        }
        if (this.mOnScrollListener != null) {
            this.mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView paramAbsListView, int scrollState) {
        if (this.mOnScrollListener != null) {
            this.mOnScrollListener.onScrollStateChanged(paramAbsListView, scrollState);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_DOWN:
                if (!this.mScalingRunnalable.mIsFinished) {
                    this.mScalingRunnalable.abortAnimation();
                }
                this.mLastMotionY = motionEvent.getY();
                //获取第一个手指指针的ID
                this.mActivePointerId = motionEvent.getPointerId(0);
                this.mMaxScale = (this.mScreenHeight / this.mHeaderHeight);
                this.mLastScale = (this.mHeaderContainer.getBottom() / this.mHeaderHeight);
                Log.d(TAG, "onTouchEvent  ACTION_DOWN mLastMotionY = " + mLastMotionY);
                Log.d(TAG, "onTouchEvent  ACTION_DOWN mActivePointerId = " + mActivePointerId);
                Log.d(TAG, "onTouchEvent  ACTION_DOWN mMaxScale = " + mMaxScale);
                Log.d(TAG, "onTouchEvent  ACTION_DOWN mLastScale = " + mLastScale);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "onTouchEvent ACTION_MOVE mActivePointerId" + mActivePointerId);
                //获取当前id的手机指针
                int pointer = motionEvent.findPointerIndex(this.mActivePointerId);
                //判断指针不为空
                if (pointer == INVALID_VALUE) {
                    Log.e(TAG, "Invalid pointerId=" + this.mActivePointerId + " in onTouchEvent");
                } else {
                    //如果开始没有赋值，则需要赋值
                    if (this.mLastMotionY == INVALID_VALUE) {
                        this.mLastMotionY = motionEvent.getY(pointer);
                    }
                    if (this.mHeaderContainer.getBottom() >= this.mHeaderHeight) {
                        //获取头部样式
                        ViewGroup.LayoutParams headerParams = this.mHeaderContainer.getLayoutParams();

                        float currentScale = ((motionEvent.getY(pointer) - this.mLastMotionY + this.mHeaderContainer.getBottom())
                                / this.mHeaderHeight - this.mLastScale)
                                / 2.0F + this.mLastScale;
                        if ((this.mLastScale <= 1.0D) && (currentScale < this.mLastScale)) {
                            //最后比例小于默认并且当前的比例要小于上次的比例，则修改头部的高度
                            headerParams.height = this.mHeaderHeight;
                            this.mHeaderContainer.setLayoutParams(headerParams);
                            return super.onTouchEvent(motionEvent);
                        } else {
                            //否则，将当前的比例赋值为最后一次的比例
                            this.mLastScale = Math.min(Math.max(currentScale, 1.0F), this.mMaxScale);
                            headerParams.height = ((int) (this.mHeaderHeight * this.mLastScale));
                            //判断修改后的高度小于屏幕的高度
                            if (headerParams.height < this.mScreenHeight) {
                                this.mHeaderContainer.setLayoutParams(headerParams);
                            }
                            //记录最后的y坐标
                            this.mLastMotionY = motionEvent.getY(pointer);
                            return true;
                        }
                    }
                    this.mLastMotionY = motionEvent.getY(pointer);
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouchEvent  ACTION_UP 重置");
                //重置
                reset();
                //当手指起来的时候，结算拉伸，判断是否开启动画
                endScraling();
                break;
            case MotionEvent.ACTION_CANCEL:
                int actionIndex = motionEvent.getActionIndex();//获取当前最上层的指针
                this.mLastMotionY = motionEvent.getY(actionIndex);//获取最后的y坐标
                this.mActivePointerId = motionEvent.getPointerId(actionIndex);//获取最上层指针的手指
                Log.d(TAG, "onTouchEvent  ACTION_CANCEL  actionIndex = " + actionIndex + " mLastMotionY = " + mLastMotionY + " mActivePointerId = " + mActivePointerId);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //当第二个手指按下或者放开触发这个事件
                onSecondaryPointerUp(motionEvent);
                this.mLastMotionY = motionEvent.getY(motionEvent.findPointerIndex(this.mActivePointerId));
                Log.d(TAG, "onTouchEvent_Po  ACTION_POINTER_DOWN mLastMotionY = " + mLastMotionY);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                //当第二个手指按下或者放开
                Log.d(TAG, "onTouchEvent_Po  ACTION_POINTER_UP ");
                break;
        }
        return super.onTouchEvent(motionEvent);
    }

    /**
     * 设置顶部样式
     * @param width
     * @param height
     */
    public void setHeaderViewSize(int width, int height) {
        ViewGroup.LayoutParams layoutParams = this.mHeaderContainer.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LayoutParams(width, height);
        }
        layoutParams.width = width;
        layoutParams.height = height;
        this.mHeaderContainer.setLayoutParams(layoutParams);
        this.mHeaderHeight = height;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }

    public void setShadow(int res) {
        this.mShadow.setBackgroundResource(res);
    }

    class ScalingRunnalable implements Runnable {
        long mDuration;//持续时间
        boolean mIsFinished = true;//是否结束
        float mScale;//比例
        long mStartTime;//开始时间

        ScalingRunnalable() {
        }

        /**
         * 中止动画
         */
        public void abortAnimation() {
            this.mIsFinished = true;
        }

        /**
         * 是否中止
         *
         * @return
         */
        public boolean isFinished() {
            return this.mIsFinished;
        }


        public void run() {
            Log.d(TAG, "ScalingRunnalable mIsFinished = " + this.mIsFinished + " this.mScale = " + this.mScale);
            float currentScale;
            ViewGroup.LayoutParams mHeaderContainerParams;//头部样式
            //判断是否中止和已经滑动超过的默认大小
            if ((!this.mIsFinished) && (this.mScale > 1.0D)) {

                float currentTime = ((float) SystemClock.currentThreadTimeMillis() - (float) this.mStartTime) / (float) this.mDuration;
                currentScale = this.mScale - (this.mScale - 1.0F) * PullToZoomListView.sInterpolator.getInterpolation(currentTime);
                Log.d(TAG, "ScalingRunnalable currentTime = " + currentTime + " currentScale = " + currentScale);

                mHeaderContainerParams = PullToZoomListView.this.mHeaderContainer.getLayoutParams();
                if (currentScale > 1.0F) {
                    Log.d(TAG, "ScalingRunnalable currentScale > 1.0  -- 修改头部高度");
                    mHeaderContainerParams.height = PullToZoomListView.this.mHeaderHeight;
                    mHeaderContainerParams.height = ((int) (currentScale * PullToZoomListView.this.mHeaderHeight));
                    PullToZoomListView.this.mHeaderContainer.setLayoutParams(mHeaderContainerParams);
                    PullToZoomListView.this.post(this);//循环执行
                } else {
                    Log.d(TAG, "ScalingRunnalable currentScale < 1.0  -- 中止");
                    this.mIsFinished = true;
                }
            }
        }

        public void startAnimation(long paramLong) {
            Log.d(TAG, "ScalingRunnalable 开始执行动画");
            this.mStartTime = SystemClock.currentThreadTimeMillis();
            this.mDuration = paramLong;
            this.mScale = ((float) (PullToZoomListView.this.mHeaderContainer.getBottom()) / PullToZoomListView.this.mHeaderHeight);
            this.mIsFinished = false;
            Log.d(TAG, "ScalingRunnalable this.mStartTime = " + this.mStartTime);
            Log.d(TAG, "ScalingRunnalable this.mDuration = " + this.mDuration);
            Log.d(TAG, "ScalingRunnalable this.mScale = " + this.mScale);
            Log.d(TAG, "ScalingRunnalable this.mIsFinished = " + this.mIsFinished);
            PullToZoomListView.this.post(this);
        }
    }
}
