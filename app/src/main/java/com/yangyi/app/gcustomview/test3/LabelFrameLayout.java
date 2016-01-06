package com.yangyi.app.gcustomview.test3;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GYH on 2016/1/4.
 */
public class LabelFrameLayout extends FrameLayout {

    private static final String TAG = "LabelFrameLayout";//tag标签

    private Context context;//上下文

    private List<LabelViewListener> viewListeners = new ArrayList<>();

    private LabelViewListener currentLabel = null;//当前控制的标签

    private boolean isHasOnClick = false;//判断是否点击
    private static final int FRAME_MOVE_OFFSET = 20;

    //记录点击下去的坐标
    private int xDown;
    private int yDown;

//    private int frameHeight = 0;

    private ImageView imageView;

    private LabelFrameOnClickListener frameOnClickListener;

    private ValueAnimator currentAnim;

    private int labelNum = 3;

    public LabelFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        imageView = (ImageView)getChildAt(0);
        super.onFinishInflate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        View topView = getChildAt(0);
//        if (topView != null) {
//            topView.layout(left, top, right, bottom);
//        }
        imageView.layout(left, top, right, bottom);
        Log.i(TAG, "onLayout  left = "+ left + " top = "+ top + " right = "+ right + " bottom =" + bottom);
//        frameHeight = bottom;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                upAction(event);
                break;
            case MotionEvent.ACTION_MOVE:
                moveAction(event);
                break;
            case MotionEvent.ACTION_DOWN:
                downAction(event);
                break;
        }
        return true;
    }

    /**
     * 按下操作
     *
     * @param event
     */
    private void downAction(MotionEvent event) {
        Log.i(TAG, "downAction  x = " + (int) event.getX() + " y = " + (int) event.getY());
        //更新点击标签
        xDown = (int) event.getX();
        yDown = (int) event.getY();
        isHasOnClick = true;
        //判断当前是否有动画，并且是否在运行
        if (currentAnim != null) {
            if (currentAnim.isRunning()) {
                currentAnim.cancel();
                if (currentLabel != null) {
                    //直接完成
                    currentLabel.showCurrent();
                }
            }
        }
        currentLabel = getDownLableView();
        Log.i(TAG, "downAction  currentLabel == " + currentLabel);
    }

    /**
     * 移动操作
     *
     * @param event
     */
    private void moveAction(MotionEvent event) {
        //判断是否移动
        if (isHasOnClick) {
            //如果移动，则用户不是点击
            if (Math.abs(xDown - event.getX()) > FRAME_MOVE_OFFSET || Math.abs(yDown - event.getY()) > FRAME_MOVE_OFFSET) {
                isHasOnClick = false;
            }
        }
        Log.i(TAG, "moveAction  isHasOnClick == " + isHasOnClick);
        if (currentLabel != null) {
            int xOf = (int) event.getX() - xDown;
            int yOf = (int) event.getY() - yDown;
            currentLabel.refreshLabelOffset(xOf, yOf);
            if (!isHasOnClick) {
                //刷新ui
                showLayoutView();
            }
        }
    }

    /**
     * 抬起操作
     *
     * @param event
     */
    private void upAction(MotionEvent event) {
        Log.i(TAG, "upAction  isHasOnClick == " + isHasOnClick + " currentLabel = " + currentLabel);
        if (currentLabel == null && isHasOnClick) {
            //添加最大这是数量
            if (viewListeners.size() >= labelNum) {
                Toast.makeText(context, "最多创建 " + labelNum + " 个", Toast.LENGTH_SHORT).show();
            } else {
                if(frameOnClickListener!=null){
                    frameOnClickListener.onClick((int) event.getX(), (int) event.getY(),currentLabel);
                }
            }
        } else if (currentLabel != null && isHasOnClick) {
            //用户点击到子view
            if (currentLabel.clickInCenter((int) event.getX(), (int) event.getY())) {
                Log.i(TAG, "upAction  用户点击到中心位置");
                //用户点击到中心位置，隐藏动画
                createHideAnmotion();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!currentAnim.isRunning() && currentLabel != null) {
                            currentLabel.switchType();
                            showLayoutView();
                            startShowAnmotion();
                        }
                    }
                }, 600);
            } else {
                //用户每一点击到中心位置
                Log.i(TAG, "upAction  用户点击了标签");
                if(frameOnClickListener!=null){
                    frameOnClickListener.onClick((int) event.getX(), (int) event.getY(),currentLabel);
                }
            }
        } else if (currentLabel != null) {
            //用户点击到标签，移动标签
            int xOf = (int) event.getX() - xDown;
            int yOf = (int) event.getY() - yDown;
            currentLabel.refreshLabelCenterPoint(xOf, yOf);
            Log.i(TAG, "upAction  用户移动了标签");
        }
        isHasOnClick = false;
    }

    /**
     * 展示动画
     */
    private void startShowAnmotion() {
        currentAnim = currentLabel.getLabelShowAnim();
        currentAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currvalu = (int) animation.getAnimatedValue();
                currentLabel.refreshLabelVisibleArea(currvalu);
            }
        });
        currentAnim.start();
    }

    /**
     * 隐藏动画
     */
    private void createHideAnmotion() {
        currentAnim = currentLabel.getLabelHideAnim();
        currentAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int cValues = (int) animation.getAnimatedValue();
                currentLabel.refreshLabelVisibleArea(cValues);

            }
        });
        currentAnim.start();
    }

    /**
     * 获取点击到的子View
     *
     * @return
     */
    private LabelViewListener getDownLableView() {
        LabelViewListener labelViewListener = null;
        for (int i = viewListeners.size() - 1; i >= 0; i--) {
            if (viewListeners.get(i).clickInLabelView(xDown, yDown)) {
                labelViewListener = viewListeners.get(i);
                break;
            }
        }
        return labelViewListener;
    }

    /**
     * 创建subView
     *
     * @param x          创建的x坐标
     * @param y          创建的y坐标
     * @param stringList 创建的标签名称
     * @return
     */
    private void createLabelView(int x, int y, List<String> stringList) {
        Log.i(TAG, "createLabelView  " + "x = " + x + " y = " + y + " stringList = " + stringList);
        if (viewListeners == null) {
            viewListeners = new ArrayList<>();
        }
        LabelViewListener labelViewListener = null;
        //初始化
        switch (stringList.size()) {
            case 1:
                labelViewListener = new OneLabelView(context, stringList.get(0), x, y);
                break;
            case 2:
                labelViewListener = new TwoLabelView(context,stringList,x,y);
                break;
            case 3:
                break;
        }
        viewListeners.add(labelViewListener);
        currentLabel = labelViewListener;
        this.addView((View) labelViewListener);
        showLayoutView();
        startShowAnmotion();
    }

    /**
     * 设置子View的布局
     */
    private void showLayoutView() {
        Log.i(TAG, "showLayoutView left = " + currentLabel.getLayoutLeft() +
                " top = " + currentLabel.getLayoutTop() +
                " right = " + currentLabel.getLayoutRight() +
                " bottom = " + currentLabel.getLayoutBottom());
        ((View) currentLabel).layout(currentLabel.getLayoutLeft(),
                currentLabel.getLayoutTop(),
                currentLabel.getLayoutRight(),
                currentLabel.getLayoutBottom());
        ((View) currentLabel).setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setLabelNum(int labelNum) {
        this.labelNum = labelNum;
    }

    /**
     * 如果view为空则是创建
     * ---如果text为空则不创建
     * 如果view不为空，
     * ---如果text为空则删除
     * ---如果text不为空则更新
     *
     * @param x
     * @param y
     * @param text
     * @param view
     * @return
     */
    public void makeLabelView(int x, int y, List<String> text, LabelViewListener view) {
        //创建
        if (view == null) {
            if (text == null || text.size() == 0) {
                return;
            }
            //添加最大这是数量
            if (viewListeners.size() >= labelNum) {
                Toast.makeText(context, "最多创建 " + labelNum + " 个", Toast.LENGTH_SHORT).show();
            } else {
                createLabelView(x, y, text);
            }
        } else {
            if (text == null || text.size() == 0) {
                removeLabelView((View) view);
                return;
            }
            refreshSubView(text, (View)view);
        }
    }

    /**
     * 更新labelView
     *
     * @param text
     * @param view
     * @return
     */
    private void refreshSubView(List<String> text, View view) {
        if (view instanceof LabelViewListener) {
            LabelViewListener listener = (LabelViewListener) view;
            if (listener.getLabelTextList() != null) {
                if (listener.getLabelTextList().size() == text.size()) {
                    listener.setLabelTextList(text);
                }else {
                    removeLabelView(view);
                    createLabelView(listener.getProitX(),listener.getProitY(),text);
                }
            }
        }
    }

    /**
     * 删除当前的View
     *
     * @param view
     * @return
     */
    private void removeLabelView(View view) {
        this.removeView(view);
        if (view instanceof LabelViewListener) {
            LabelViewListener listener = (LabelViewListener) view;
            viewListeners.remove(listener);
        }
    }

    /**
     * 清除标签
     */
    public void clearLabelView(){
        if(viewListeners == null){
            viewListeners = new ArrayList<>();
        }
        for(int i =0;i<viewListeners.size();i++){
            Log.i(TAG,"clearLabelView  labelView:"+i + " text = " + viewListeners.get(i).getLabelTextList().get(0));
            this.removeView((View)viewListeners.get(i));
        }
        viewListeners.clear();
    }


    /**
     * 设置监听
     *
     * @param frameOnClickListener
     */
    public void setFrameOnClickListener(LabelFrameOnClickListener frameOnClickListener) {
        this.frameOnClickListener = frameOnClickListener;
    }

    interface LabelFrameOnClickListener {
        void onClick(int x, int y, LabelViewListener view);
    }
}
