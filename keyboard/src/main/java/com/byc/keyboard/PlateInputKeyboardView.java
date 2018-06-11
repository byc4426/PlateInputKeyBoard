package com.byc.keyboard;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class PlateInputKeyboardView {
    private Activity mContext;
    private View parentView;
    private EditText text;
    private KeyboardView mLetterView;   //字母键盘view
    private KeyboardView mNumberView;   //数字键盘View
    private Keyboard mNumberKeyboard;   // 数字键盘
    private Keyboard mLetterKeyboard;   // 字母键盘
    private Keyboard mSymbolKeyboard;   // 中文键盘

    private boolean isNumber = false;    // 是否数字键盘
    private boolean isSymbol = false;   // 是否是中文
    private View headerView;
    private String[] provinceShort = new String[]{"京", "津", "冀", "鲁", "晋", "蒙", "辽", "吉", "黑", "沪", "苏", "浙", "皖", "闽", "赣", "豫", "鄂", "湘", "粤", "桂", "渝", "川",
            "贵", "云", "藏", "陕", "甘", "青", "琼", "新", "港", "澳", "台", "宁"};
    private TextView[] tvList;
    private EditText mEditText;
    private boolean isTvList;
    private int currentEditText = 0;//默认当前光标在第一个TextView

    public void setEditText(EditText text) {
        mEditText = text;
    }

    public void setTvList(TextView[] tvList) {
        this.tvList = tvList;
    }

    public PlateInputKeyboardView(Activity context, View view) {
        mContext = context;
        parentView = view;

        mNumberKeyboard = new Keyboard(mContext, R.xml.keyboard_numbers);
        mLetterKeyboard = new Keyboard(mContext, R.xml.keyboard_word);
        mSymbolKeyboard = new Keyboard(mContext, R.xml.keyboard_province);
        mNumberView = (KeyboardView) parentView.findViewById(R.id.keyboard_view);
        mLetterView = (KeyboardView) parentView.findViewById(R.id.keyboard_view_2);
        text = (EditText) parentView.findViewById(R.id.text);

        mNumberView.setKeyboard(mNumberKeyboard);
        mNumberView.setEnabled(true);
        mNumberView.setPreviewEnabled(false);
        mNumberView.setOnKeyboardActionListener(listener);
        mLetterView.setKeyboard(mLetterKeyboard);
        mLetterView.setEnabled(true);
        mLetterView.setPreviewEnabled(true);
        mLetterView.setOnKeyboardActionListener(listener);
        headerView = parentView.findViewById(R.id.keyboard_header);

    }

    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {
            if (primaryCode == Keyboard.KEYCODE_SHIFT) {
                mLetterView.setPreviewEnabled(false);
            } else if (primaryCode == Keyboard.KEYCODE_DELETE) {
                mLetterView.setPreviewEnabled(false);
            } else if (primaryCode == 32) {
                mLetterView.setPreviewEnabled(false);
            } else {
                mLetterView.setPreviewEnabled(true);
            }

        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            try {
                if (mEditText == null && tvList == null)
                    return;
                if (isTvList) {
                    if (primaryCode == Keyboard.KEYCODE_CANCEL) {
                        // 隐藏键盘
                        hideKeyboard();
                    } else if (primaryCode == Keyboard.KEYCODE_DELETE || primaryCode == -35) {
                        if (currentEditText > tvList.length - 1) {
                            currentEditText = tvList.length - 1;
                        }
                        if (TextUtils.isEmpty(tvList[currentEditText].getText().toString())) {
                            // 回退键,删除字符
                            currentEditText--;
                        }
                        if (currentEditText < 0) {
                            currentEditText = 0;
                        }
                        tvList[currentEditText].setText("");//将当前tv置为""并currentEditText-1
                        if (currentEditText < 1) {
                            //切换为省份简称键盘
                            showSymbolView();
                        }
                        if (currentEditText < 0) {
                            currentEditText = 0;
                        }
                        setText(tvList);
                        if (plateView != null) {
                            plateView.setTextViewsBackground(currentEditText);
                        }
                    } else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {
                        // 数字与字母键盘互换
                        if (isNumber) {
                            showLetterView();
                            showLetterView2();
                        } else {
                            showNumberView();
                        }
                    } else if (primaryCode == 90001) {
//                  字母与中文切换
                        if (isSymbol) {
                            showLetterView2();
                        } else {
                            showSymbolView();
                        }
                    } else {
                        if (currentEditText == 0) {
                            //如果currentEditText==0代表当前为省份键盘,
                            //currentEditText+1
                            if (isSymbol) {//中文
                                tvList[0].setText(provinceShort[primaryCode]);
                            } else {
                                tvList[0].setText(Character.toString((char) primaryCode));
                            }
                            currentEditText = 1;
                            //切换为字母数字键盘
                            showLetterView2();
                        } else {
//                            //第二位必须大写字母
//                            if (currentEditText == 1 && !Character.toString((char) primaryCode).matches("[A-Z]{1}")) {
//                                return;
//                            }
                            if (currentEditText >= tvList.length - 1) {
                                currentEditText = tvList.length - 1;
                            }
                            if (isSymbol) {//中文
                                tvList[currentEditText].setText(provinceShort[primaryCode]);
                            } else {
                                tvList[currentEditText].setText(Character.toString((char) primaryCode));
                            }
                            currentEditText++;
                        }
                        setText(tvList);
                        if (plateView != null && currentEditText < tvList.length) {
                            plateView.setTextViewsBackground(currentEditText);
                        }
                    }
                } else {
                    Editable editable = mEditText.getText();
                    int start = mEditText.getSelectionStart();
                    if (primaryCode == Keyboard.KEYCODE_CANCEL) {
                        // 隐藏键盘
                        hideKeyboard();
                    } else if (primaryCode == Keyboard.KEYCODE_DELETE || primaryCode == -35) {
                        // 回退键,删除字符
                        if (editable != null && editable.length() > 0) {
                            if (start > 0) {
                                editable.delete(start - 1, start);
                                text.setText(editable.toString());
                            }
                        }
                        if (editable.toString().length() < 1) {
                            showSymbolView();
                        }
                    } else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {
                        // 数字与字母键盘互换
                        if (isNumber) {
                            showLetterView();
                            showLetterView2();
                        } else {
                            showNumberView();
                        }
                    } else if (primaryCode == 90001) {
//                  字母与中文切换
                        if (isSymbol) {
                            showLetterView2();
                        } else {
                            showSymbolView();
                        }
                    } else {
                        // 输入键盘值
                        if (isSymbol) {//中文
                            editable.insert(start, provinceShort[primaryCode]);
                        } else {
                            editable.insert(start, Character.toString((char) primaryCode));
                        }
                        text.setText(editable.toString());
                        if (editable.toString().length() >= 1) {
                            if (isSymbol) {
                                showLetterView2();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };

    //  字母-中文,显示字母
    private void showLetterView2() {
        if (mLetterView != null) {
            headerView.setVisibility(View.VISIBLE);
            mLetterView.setVisibility(View.VISIBLE);
            mNumberView.setVisibility(View.INVISIBLE);
            isSymbol = false;
            isNumber = false;
            mLetterView.setKeyboard(mLetterKeyboard);
        }
    }

    //  字母-中文,显示中文
    private void showSymbolView() {
        try {
            if (mLetterKeyboard != null) {
                headerView.setVisibility(View.VISIBLE);
                mLetterView.setVisibility(View.VISIBLE);
                mNumberView.setVisibility(View.INVISIBLE);
                isSymbol = true;
                mLetterView.setKeyboard(mSymbolKeyboard);
            }
        } catch (Exception e) {
        }
    }

    //  数字-字母,显示字母键盘
    private void showLetterView() {
        try {
            if (mLetterView != null && mNumberView != null) {
                isNumber = false;
                headerView.setVisibility(View.VISIBLE);
                mLetterView.setVisibility(View.VISIBLE);
                mNumberView.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 数字-字母, 显示数字键盘
    private void showNumberView() {
        try {
            if (mLetterView != null && mNumberView != null) {
                isNumber = true;
                headerView.setVisibility(View.VISIBLE);
                mLetterView.setVisibility(View.INVISIBLE);
                mNumberView.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 判断是否是字母
     */
    private boolean isLetter(String str) {
        String wordStr = "abcdefghijklmnopqrstuvwxyz";
        return wordStr.contains(str.toLowerCase());
    }

    public void hideKeyboard() {
        try {
            int visibility = mLetterView.getVisibility();
            if (visibility == View.VISIBLE) {
                headerView.setVisibility(View.GONE);
                mLetterView.setVisibility(View.GONE);
            }
            visibility = mNumberView.getVisibility();
            if (visibility == View.VISIBLE) {
                headerView.setVisibility(View.GONE);
                mNumberView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 显示键盘
     *
     * @param editText
     */
    public void showKeyboard(EditText editText) {
        try {
            isTvList = false;
            this.mEditText = editText;
            int inputText = mEditText.getInputType();
            headerView.setVisibility(View.VISIBLE);
            switch (inputText) {
                case InputType.TYPE_CLASS_NUMBER:
                    showNumberView();
                    break;
                case InputType.TYPE_CLASS_PHONE:
                    showNumberView();
                    break;
                case InputType.TYPE_NUMBER_FLAG_DECIMAL:
                    showNumberView();
                    break;
                default:
                    showSymbolView();
//                    showLetterView();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void setText(TextView[] tvList) {
        String temp = "";
        for (int i = 0; i < tvList.length; i++) {
            temp += tvList[i].getText().toString();
        }
        text.setText(temp);
    }


    public void showKeyboard(TextView[] tvList) {
        isTvList = true;
        this.tvList = tvList;
        for (int i = 0; i < tvList.length; i++) {
            if (TextUtils.isEmpty(tvList[i].getText().toString())) {
                currentEditText = i;
                if (plateView != null) {
                    plateView.setTextViewsBackground(currentEditText);
                }
                break;
            }
        }
        if (currentEditText == 0) {
            showSymbolView();
        } else {
            showLetterView2();
        }

    }

    private LicensePlateView plateView;

    public void showKeyboard(final LicensePlateView plateView) {
        this.plateView = plateView;
        showKeyboard(plateView.getTextViews());
        plateView.setOnSelect(new LicensePlateView.OnSelect() {
            @Override
            public void select(int select) {
                currentEditText = select;
                plateView.setTextViewsBackground(currentEditText);
            }
        });
    }


    public String getInput() {
        return text.getText().toString();
    }
}
