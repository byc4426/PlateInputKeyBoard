# 自定义车牌输入键盘

*自定义车牌输入键盘

*输入第一个省份后自动切换到数字字母键盘

*删除完后自动切换为省份键盘
![image](https://github.com/byc4426/PlateInputKeyBoard/blob/master/image/000.png)
![image](https://github.com/byc4426/PlateInputKeyBoard/blob/master/image/111.png)
![image](https://github.com/byc4426/PlateInputKeyBoard/blob/master/image/222.png)
![image](https://github.com/byc4426/PlateInputKeyBoard/blob/master/image/333.png)
![image](https://github.com/byc4426/PlateInputKeyBoard/blob/master/image/444.png)



## 实现步骤

在module的gradle
```
    implementation 'com.byc:plateInputKeyboard:1.0.3'
```

#### 方法
 *  使用LicensePlateView 输入
     ```
        keyBoardDialogUtils = new PlateInputKeyBoardDialogUtils(this);
        //设置完成按钮监听
        keyBoardDialogUtils.setOnKeyboardFinishListener(new PlateInputKeyBoardDialogUtils.OnKeyboardFinishListener() {
            @Override
            public void onFinish(String input) {
                Log.e("input",input);
            }
        });
        plate = findViewById(R.id.plate);
        plate.setTextSize(30);//设置字体大小
        plate.setTextColor(Color.BLUE);//设置字体颜色
        plate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyBoardDialogUtils.show(plate);
            }
        });
        plate.getText();//获取输入的内容
      ```
    

*  使用EditText 输入
 ```
        et = (EditText) findViewById(R.id.et);
        et.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 keyBoardDialogUtils.show(et);
              }
         });
  ```
*  使用TextView[] tvList 输入
  
   ```
        linear_pass = (LinearLayout) findViewById(R.id.linear_pass);
        tvList = new TextView[8];
        tvList[0] = (TextView) findViewById(R.id.tv_pass1);
        tvList[1] = (TextView) findViewById(R.id.tv_pass2);
        tvList[2] = (TextView) findViewById(R.id.tv_pass3);
        tvList[3] = (TextView) findViewById(R.id.tv_pass4);
        tvList[4] = (TextView) findViewById(R.id.tv_pass5);
        tvList[5] = (TextView) findViewById(R.id.tv_pass6);
        tvList[6] = (TextView) findViewById(R.id.tv_pass7);
        tvList[7] = (TextView) findViewById(R.id.tv_pass8);
        
        linear_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyBoardDialogUtils.show(tvList);
            }
        });
    ```
    
####  参考
 https://github.com/peiniwan/SafeKeyBoard
