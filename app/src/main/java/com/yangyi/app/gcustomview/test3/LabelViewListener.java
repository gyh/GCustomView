package com.yangyi.app.gcustomview.test3;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by GYH on 2016/1/4.
 */
public  interface LabelViewListener {

    int getLayoutLeft();

    int getLayoutTop();

    int getLayoutRight();

    int getLayoutBottom();

    void switchType();

    void showCurrent();

    boolean clickInLabelView(int x,int y);

    boolean moveLabelView();

    boolean clickInCenter(int x,int y);

    void refreshLabelOffset(int xOff,int yOff);

    void refreshLabelCenterPoint(int xC,int yC);

    void setLabelTextList(List<String> textList);

    List<String> getLabelTextList();

    void refreshLabelVisibleArea(int animValue);

    ValueAnimator getLabelShowAnim();

    ValueAnimator getLabelHideAnim();

    int getProitX();
    int getProitY();
}
