package com.yangyi.app.gcustomview.slidetop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yangyi.app.gcustomview.R;

/**
 * Created by GYH on 2015/12/30.
 */
public class MyScorllActivity extends AppCompatActivity implements MyScrollView.OnScrollListener{

    private static final String TAG = "guoyuehua";

    private EditText search_edit;
    private MyScrollView myScrollView;
    private int searchLayoutTop;

    LinearLayout search01,search02;
    RelativeLayout rlayout;
    View tvtitle ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myscroll);
        //初始化控件
        init();
    }

    private void init() {
        search_edit = (EditText)findViewById(R.id.search_edit);
        myScrollView = (MyScrollView)findViewById(R.id.myScrollView);
        search01 = (LinearLayout)findViewById(R.id.search01);
        search02 = (LinearLayout)findViewById(R.id.search02);
        rlayout = (RelativeLayout)findViewById(R.id.rlayout);
        tvtitle = findViewById(R.id.layoutr2);
        myScrollView.setOnScrollListener(this);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            searchLayoutTop = rlayout.getBottom()-tvtitle.getBottom();//获取searchLayout的顶部位置
            Log.v(TAG, " onWindowFocusChanged -------->>>>>  searchLayoutTop = " + searchLayoutTop);
        }
    }

    //监听滚动Y值变化，通过addView和removeView来实现悬停效果
    @Override
    public void onScroll(int scrollY) {
        Log.v(TAG,"onScroll ------------>>>>>>>>>>   scrollY = "+scrollY);
        Log.v(TAG,"onScroll ------------>>>>>>>>>>   searchLayoutTop = "+searchLayoutTop);
        if(scrollY >= searchLayoutTop){
            Log.v(TAG,"onScroll ------------>>>>>>>>>>   search_edit.getParent() = "+search_edit.getParent());
            if (search_edit.getParent()!=search01) {
                search02.removeView(search_edit);
                search01.addView(search_edit);
            }
        }else{
            Log.v(TAG,"onScroll ------------>>>>>>>>>>   search_edit.getParent() = "+search_edit.getParent());
            if (search_edit.getParent()!=search02) {
                search01.removeView(search_edit);
                search02.addView(search_edit);
            }
        }
    }
}
