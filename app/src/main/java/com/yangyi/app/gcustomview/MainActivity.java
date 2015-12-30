package com.yangyi.app.gcustomview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yangyi.app.gcustomview.test1.ScrollViewSampleActivity;
import com.yangyi.app.gcustomview.test2.MyScorllActivity;
import com.yangyi.app.gcustomview.test3.MoveViewActivity;
import com.yangyi.app.gcustomview.test4.PullToZoomActivity;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{


    private String[] strTypes = new String[]{
            "test1 -- 头部渐变 ",
            "test2 -- 滑动头部停留",
            "test3 -- 仿半塘图片添加标签效果",
            "test4 --- https://github.com/matrixxun/PullToZoomInListView这个项目添加注释和优化"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initListView();
    }


    private void initListView(){
        ListView listView = (ListView)findViewById(R.id.listview);
        listView.setAdapter(new MyAdapter(this,this.strTypes));
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                startActivity(new Intent(MainActivity.this, ScrollViewSampleActivity.class));
                break;
            case 1:
                startActivity(new Intent(MainActivity.this, MyScorllActivity.class));
                break;
            case 2:
                startActivity(new Intent(MainActivity.this, MoveViewActivity.class));
                break;
            case 3:
                startActivity(new Intent(MainActivity.this, PullToZoomActivity.class));
                break;
            case 4:
                break;
        }
    }


    class MyAdapter extends BaseAdapter {

        String[] strings;

        LayoutInflater listContainer;

        MyAdapter(Context context,String[] strings){
            this.strings = strings;
            listContainer = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return strings.length;
        }

        @Override
        public Object getItem(int position) {
            return strings[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = null;
            if(convertView==null){
                convertView = listContainer.inflate(R.layout.item_listview,null);
                textView = (TextView)convertView.findViewById(R.id.itemTv);
                convertView.setTag(textView);
            }else {
                textView = (TextView)convertView.getTag();
            }
            textView.setText(strings[position]);
            return convertView;
        }
    }
}