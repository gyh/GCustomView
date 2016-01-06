package com.yangyi.app.gcustomview.test3;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.yangyi.app.gcustomview.R;
import com.yangyi.app.gcustomview.Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GYH on 2016/1/5.
 */
public class FrameLabelActivity extends AppCompatActivity{

    Button btnselect;
    LabelFrameLayout layout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_framelabel);
        layout2 = (LabelFrameLayout)findViewById(R.id.labelframe);
        layout2.setFrameOnClickListener(new LabelFrameLayout.LabelFrameOnClickListener() {
            @Override
            public void onClick(int x, int y, LabelViewListener view) {
                mySmallDialog(x,y,view);
            }
        });
        btnselect = (Button)findViewById(R.id.btnselect);
        btnselect.setVisibility(View.VISIBLE);
        btnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout2.clearLabelView();
                Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
                intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
                intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
                startActivityForResult(intent, 1);
            }
        });

        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = Utils.getViewBitmap(layout2,layout2.getWidth(),layout2.getHeight());
                try {
                    Utils.saveBitmap(bitmap,System.currentTimeMillis()+".png");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Log.e("uri", uri.toString());
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                ImageView imageView = (ImageView) findViewById(R.id.imgeview);
                /* 将Bitmap设定到ImageView */
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(),e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*自定义对话框*/
    private void mySmallDialog(final int x, final int y, final LabelViewListener view){
        final AlertDialog.Builder  mySmallDialog = new AlertDialog.Builder(this);
        final View smallLayout = LayoutInflater.from(this).inflate(R.layout.layout_small_dialog,null);
        final EditText editText = (EditText)smallLayout.findViewById(R.id.small_edit);
        final EditText editText2 = (EditText)smallLayout.findViewById(R.id.small_edit2);
        if(view != null){
            List<String> stringList = view.getLabelTextList();
            if(stringList != null && stringList.size() ==1){
                editText.setText(stringList.get(0));
                editText.setSelection(stringList.get(0).length());
            }else if(stringList != null && stringList.size() ==2){
                editText.setText(stringList.get(0));
                editText.setSelection(stringList.get(0).length());
                editText2.setText(stringList.get(1));
                editText2.setSelection(stringList.get(1).length());
            }
        }
        mySmallDialog.setTitle("标签 ");
        mySmallDialog.setView(smallLayout);
        mySmallDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<String> strings = new ArrayList<String>();
                String str1 = editText.getText().toString().trim();
                String str2 = editText2.getText().toString().trim();

                if (!"".equals(str1)) {
                    strings.add(str1);
                }
                if (!"".equals(str2)) {
                    strings.add(str2);
                }
                layout2.makeLabelView(x, y, strings, view);
            }
        });
        mySmallDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mySmallDialog.show();
            }
        }, 300);
    }
}
