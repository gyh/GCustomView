package com.yangyi.app.gcustomview.test1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yangyi.app.gcustomview.R;

/**
 * Created by GYH on 2015/12/22.
 */
public class ScrollViewSampleActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QuickReturnHeaderHelper helper =
                new QuickReturnHeaderHelper(this, R.layout.activity_scrollview,
                R.layout.header);
        View view = helper.createView();
        setContentView(view);
    }
}
