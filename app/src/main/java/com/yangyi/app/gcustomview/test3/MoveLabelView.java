package com.yangyi.app.gcustomview.test3;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GYH on 2015/12/23.
 * 管理功能
 * 显示子视图的功能
 */
public class MoveLabelView extends FrameLayout{

    private static final String TAG = "MyContentView";//tag标签

    private static final int MOVE_THRESHOLD = 20;

    private Context context;//上下文

    private List<LabelView> subViewList = new ArrayList<>();//已经添加的

    private LabelView currentSubView = null;//当前的

    private boolean isHasOnClick = false;

    //记录点击下去的坐标
    private int xDown;
    private int yDown;

    private int bottom = 0;

    private MoveLabelListener moveLabelListener;

    public MoveLabelView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setMoveLabelListener(MoveLabelListener mListener){
        this.moveLabelListener = mListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        this.bottom = bottom;
        Log.v(TAG, "onLayout  bottom = " + bottom);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_UP:
//                Log.v(TAG, "onTouchEvent ACTION_UP " + " X = " + ev.getX() + " Y = " + ev.getY());
                //判断是否在子视图上
                //如果不在子上图，判断是否是点击事件
                //如果在子视图上，判断是否移动，如果没有移动，则切换子视图样式，重新绘制子视图，如果移动了，则不做处理
//                Log.v(TAG, "onTouchEvent ACTION_UP " + " currentSubView = " + currentSubView);
                if(currentSubView != null){
                    //判断是否已经移动过
                    if(isHasOnClick){
                        //判断是否点击到中心位置
                        if(currentSubView.isClickCenterView((int) ev.getX(), (int) ev.getY())){
                            //修改状态
                            startHideAnmotion();
                        }else {
                            //判断是否点击了标签
                            if(currentSubView.isClickLabelView((int) ev.getX(), (int) ev.getY())){
                                moveLabelListener.onClickMoveLable((int) ev.getX(), (int) ev.getY(),currentSubView);
                            }
                        }
                    }else {
                        int xOf = (int) ev.getX() - xDown;
                        int yOf = (int) ev.getY() - yDown;
                        currentSubView.refreshLastPoint(xOf, yOf);
                    }
                }else {
                    if(isHasOnClick){
                        //添加最大这是数量
                        if(subViewList.size()>=3){
                            Toast.makeText(context,"对多 3 个",Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        creatSubView((int) ev.getX(), (int) ev.getY(), "12334455");
//                        moveLabelListener.onClickMoveLable((int) ev.getX(), (int) ev.getY(),null);
                    }
                }
//                Log.v(TAG, "onTouchEvent ACTION_UP " + " isHasOnClick = " + isHasOnClick);
                isHasOnClick = false;
                break;
            case MotionEvent.ACTION_DOWN:
//                Log.v(TAG, "onTouchEvent ACTION_DOWN " + " X = " + ev.getX() + " Y = " + ev.getY());
                //判断是否点击在子视图上面，如果在子视图上面
                isDownSubView((int)ev.getX(),(int)ev.getY());
                xDown = (int)ev.getX();
                yDown = (int)ev.getY();
                isHasOnClick = true;
//                Log.v(TAG, "onTouchEvent ACTION_DOWN " + " isHasOnClick = " +isHasOnClick);
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.v(TAG, "onTouchEvent ACTION_MOVE " + " X = " + ev.getX() + " Y = " + ev.getY());
                //判断是否为点击事件
                if(isHasOnClick){
                    isHasOnClick = !isMove((int) ev.getX(), (int) ev.getY());
                }
                //判断是否是否有子视图，移动子视图
                if(currentSubView!=null){
                    int xOf = (int) ev.getX() - xDown;
                    int yOf = (int) ev.getY() - yDown;
                    currentSubView.refreshOffset(xOf, yOf);
                    //一旦已经移动过就设置为移动了
                    if(currentSubView.isMoveView()){
//                        Log.v(TAG, "onTouchEvent ACTION_MOVE " + " showLayoutView ");
                        showLayoutView();
                    }
                }
//                Log.v(TAG, "onTouchEvent ACTION_MOVE " + " isHasOnClick = " + isHasOnClick);
                break;
        }
        return true;
    }

    /**
     * 判断是否点击在子视图上面
     * @param x
     * @param y
     * @return
     */
    private boolean isDownSubView(int x,int y){
        boolean isDown = false;
        for (int i = subViewList.size()-1;i>=0;i--){
            if(subViewList.get(i).isClickInView(x,y)){
                currentSubView = subViewList.get(i);
                isDown =  true;
                break;
            }
        }
        if(!isDown){
            currentSubView = null;
        }
        Log.v(TAG, "isDownSubView isDown = " + isDown);
        return isDown;
    }

    /**
     * 重新绘制子视图
     */
    private void showLayoutView() {
        Log.v(TAG, "showLayoutView left = " + currentSubView.getViewLeft() +
                " top = " + currentSubView.getViewTop() +
                " right = " + currentSubView.getViewRight() +
                " bottom = " + currentSubView.getViewBottom());
        currentSubView.layout(currentSubView.getViewLeft(),
                currentSubView.getViewTop(),
                currentSubView.getViewRight(),
                currentSubView.getViewBottom());
        currentSubView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, this.bottom));
    }

    /**
     * 判断是否移动了坐标
     * @return
     */
    private boolean isMove(int x,int y){
        if(Math.abs(xDown-x)>MOVE_THRESHOLD || Math.abs(yDown - y)>MOVE_THRESHOLD){
            return true;
        }
        return false;
    }

    /**
     * 如果view为空则是创建
     * ---如果text为空则不创建
     * 如果view不为空，
     * ---如果text为空则删除
     * ---如果text不为空则更新
     * @param x
     * @param y
     * @param text
     * @param view
     * @return
     */
    public boolean makeSubView(int x,int y,String text,View view){
        //创建
        if(view == null){
            if(text==null||"".equals(text)){
                return false;
            }
            //添加最大这是数量
            if(subViewList.size()>=3){
                Toast.makeText(context,"对多 3 个",Toast.LENGTH_SHORT).show();
                return false;
            }
            return creatSubView(x,y,text);
        }else {
            if(text==null||"".equals(text)){
                return removeSubView(view);
            }
            return refreshSubView(text,view);
        }
    }

    /**
     * 创建subView
     * @param x 创建的x坐标
     * @param y 创建的y坐标
     * @param text 创建的标签名称
     * @return
     */
    private boolean creatSubView(int x,int y,String text){
        Log.v(TAG, "createSubView  " + "x = "+x +" y = "+y +" text ="+text);
        if(subViewList == null){
            subViewList = new ArrayList<>();
        }
        //初始化
        LabelView myClickView = new LabelView(context,text,x,y);
        Log.v(TAG, "myClickView  " + "width = " + myClickView.getMeasuredWidth()
                + " height = " + myClickView.getMeasuredHeight());
        subViewList.add(myClickView);
        currentSubView = myClickView;
        this.addView(myClickView);
        showLayoutView();
        startShowAnmotion();
        return true;
    }

    /**
     * 展示动画
     */
    private void startShowAnmotion(){
        ValueAnimator anim ;
        if(currentSubView.currentType == currentSubView.FRIST_TYPE){
            anim = ValueAnimator.ofInt(currentSubView.getWidthView(),0);
        }else {
            anim = ValueAnimator.ofInt(0,currentSubView.getWidthView());
        }
        anim.setDuration(500);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currvalu = (int) animation.getAnimatedValue();
                if (currentSubView.currentType == currentSubView.FRIST_TYPE) {
                    currentSubView.refershArea(currvalu, 0, currentSubView.getWidthView(), currentSubView.getHeightView());
                } else {
                    currentSubView.refershArea(0, 0, currvalu, currentSubView.getHeightView());
                }
            }
        });
        anim.start();
    }

    /**
     * 隐藏动画
     */
    public void startHideAnmotion(){
        final ValueAnimator anim ;
        if(currentSubView.currentType == currentSubView.FRIST_TYPE){
            anim = ValueAnimator.ofInt(0,currentSubView.getWidthView());
        }else {
            anim = ValueAnimator.ofInt(currentSubView.getWidthView(),0);
        }
        anim.setDuration(500);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int cValues = (int) animation.getAnimatedValue();
                //TODO 判断当前的子view是否为空
                if (currentSubView.currentType == currentSubView.FRIST_TYPE) {
                    currentSubView.refershArea(cValues, 0, currentSubView.getWidthView(), currentSubView.getHeightView());
                    if(cValues == currentSubView.getWidthView()){
                        currentSubView.switchType();
                        showLayoutView();
                        startShowAnmotion();
                    }
                } else {
                    currentSubView.refershArea(0, 0, cValues, currentSubView.getHeightView());
                    if(cValues == 0){
                        currentSubView.switchType();
                        showLayoutView();
                        startShowAnmotion();
                    }
                }


            }
        });
        anim.start();
    }

    /**
     * 更新labelView
     * @param text
     * @param view
     * @return
     */
    private boolean refreshSubView(String text,View view){
        if(view instanceof LabelView){
            LabelView labelView = (LabelView)view;
            labelView.setText(text);
            return true;
        }
        return false;
    }

    /**
     * 删除当前的View
     * @param view
     * @return
     */
    public boolean removeSubView(View view){
        this.removeView(view);
        subViewList.remove(view);
        return true;
    }

    interface MoveLabelListener{
        void onClickMoveLable(int x,int y,View view);
    }
}
