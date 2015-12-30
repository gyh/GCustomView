package com.yangyi.app.gcustomview.test3;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import com.yangyi.app.gcustomview.R;

/**
 * Created by GYH on 2015/12/23.
 */
public class MoveViewActivity extends AppCompatActivity implements MoveLabelView.MoveLabelListener{


    MoveLabelView moveLabelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moveview);
        moveLabelView = (MoveLabelView)findViewById(R.id.movelable);
        moveLabelView.setMoveLabelListener(this);
    }

    /*自定义对话框*/
    private void mySmallDialog(final int x, final int y, final View view){
        AlertDialog.Builder  mySmallDialog = new AlertDialog.Builder(this);
        final View smallLayout = LayoutInflater.from(this).inflate(R.layout.layout_small_dialog,null);
        final EditText editText = (EditText)smallLayout.findViewById(R.id.small_edit);
        if(view != null){
            LabelView labelView = (LabelView)view;
            editText.setText(labelView.getText());
            editText.setSelection(labelView.getText().length());
        }
        mySmallDialog.setTitle("标签 ");
        mySmallDialog.setView(smallLayout);
        mySmallDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveLabelView.makeSubView(x,y,editText.getText().toString(),view);
            }
        });
        mySmallDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        mySmallDialog.show();
    }

    @Override
    public void onClickMoveLable(int x,int y,View view) {
        mySmallDialog(x,y,view);
    }
}
