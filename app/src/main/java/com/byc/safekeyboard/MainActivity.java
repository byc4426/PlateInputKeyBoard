package com.byc.safekeyboard;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.byc.keyboard.LicensePlateView;
import com.byc.keyboard.PlateInputKeyBoardDialogUtils;


public class MainActivity extends AppCompatActivity {

    private PlateInputKeyBoardDialogUtils keyBoardDialogUtils;
    private EditText et;
    private TextView[] tvList;
    private LinearLayout linear_pass;
    private LicensePlateView plate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = (EditText) findViewById(R.id.et);
        linear_pass = (LinearLayout) findViewById(R.id.linear_pass);
        plate = findViewById(R.id.plate);
        tvList = new TextView[8];
        tvList[0] = (TextView) findViewById(R.id.tv_pass1);
        tvList[1] = (TextView) findViewById(R.id.tv_pass2);
        tvList[2] = (TextView) findViewById(R.id.tv_pass3);
        tvList[3] = (TextView) findViewById(R.id.tv_pass4);
        tvList[4] = (TextView) findViewById(R.id.tv_pass5);
        tvList[5] = (TextView) findViewById(R.id.tv_pass6);
        tvList[6] = (TextView) findViewById(R.id.tv_pass7);
        tvList[7] = (TextView) findViewById(R.id.tv_pass8);

        keyBoardDialogUtils = new PlateInputKeyBoardDialogUtils(this);
        keyBoardDialogUtils.setOnKeyboardFinishListener(new PlateInputKeyBoardDialogUtils.OnKeyboardFinishListener() {
            @Override
            public void onFinish(String input) {//输入完成按钮监听
                Log.e("input", input);
            }
        });

        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyBoardDialogUtils.show(et);
            }
        });

        linear_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyBoardDialogUtils.show(tvList);
            }
        });

        plate.setTextSize(30);//设置字体大小
        plate.setTextColor(Color.BLUE);//设置字体颜色
        plate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyBoardDialogUtils.show(plate);
            }
        });
        plate.getText();//获取输入的内容
    }
}
