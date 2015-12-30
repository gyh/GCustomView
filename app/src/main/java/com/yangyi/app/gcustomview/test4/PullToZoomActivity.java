package com.yangyi.app.gcustomview.test4;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.yangyi.app.gcustomview.R;

/**
 * Created by GYH on 2015/12/28.
 */
public class PullToZoomActivity extends AppCompatActivity{

    PullToZoomListView listView;
    private String[] adapterData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulltozoom);
        listView = (PullToZoomListView)findViewById(R.id.pulllistview);
        adapterData = new String[] { "Activity","Service","Content Provider","Intent","BroadcastReceiver","ADT","Sqlite3","HttpClient","DDMS","Android Studio","Fragment","Loader" };

        listView.setAdapter(new ArrayAdapter<String>(PullToZoomActivity.this,
                android.R.layout.simple_list_item_1, adapterData));
        listView.getHeaderView().setImageResource(R.mipmap.splash);
        listView.getHeaderView().setScaleType(ImageView.ScaleType.CENTER_CROP);
    }
}
