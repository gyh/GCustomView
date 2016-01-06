package com.yangyi.app.gcustomview.test3;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by GYH on 2015/12/31.
 */
public class TagFrameView extends FrameLayout {


    private TagView tagView;
    private TagFlickerView tagFlickerView;
    private boolean c = false;
    private Runnable runnable = new TagFrameViewRun(this);

    public TagFrameView(Context context) {
        super(context);
        init(context);
    }

    public TagFrameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    /**
     * 初始化
     * @param context
     */
    private void init(Context context){
        this.tagFlickerView = new TagFlickerView(context);
//        this.tagView = new TagView(context);
//        this.tagFlickerView.resetFileckerAnim(2 * this.tagView.a(), 1000, 0);
//        this.tagView.a(this.b);
        addView(this.tagFlickerView, new FrameLayout.LayoutParams(-1, -1));
//        addView(this.tagView, new FrameLayout.LayoutParams(-1, -1));
    }

    /**
     * 获取Tagview
     * @return
     */
    public TagView getTagView(){
        return tagView;
    }

    /**
     * 移除线程
     */
    public void removeRunable(){
        this.c = false;
        removeCallbacks(this.runnable);
    }

    /**
     *
     */
    public void addRunable(){
        if (this.c){
            return;
        }
        removeCallbacks(this.runnable);
//        this.tagView.c(true);
        postDelayed(this.runnable, 3000L);
    }
}
