package com.byc.keyboard;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LicensePlateView extends LinearLayout {
    private Context context;
    private TextView[] TextViews;
    private OnFrameTouchListener mTouchListener = new OnFrameTouchListener();
    private static int ITEM_VIEW_COUNT = 8;
    private OnSelect onSelect;
    private static final int[] VIEW_IDS = new int[]{
            R.id.tv_pass1, R.id.tv_pass2, R.id.tv_pass3,
            R.id.tv_pass4, R.id.tv_pass5, R.id.tv_pass6,
            R.id.tv_pass7, R.id.tv_pass8
    };

    public TextView[] getTextViews() {
        return TextViews;
    }

    public String getText() {
        String license = "";
        for (int i = 0; i < TextViews.length; i++) {
            license += TextViews[i].getText().toString().trim();
        }
        return license;
    }


    public LicensePlateView(Context context) {
        this(context, null);
    }

    public LicensePlateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LicensePlateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);

        TextViews = new TextView[8];
        int textsLength = VIEW_IDS.length;
        for (int i = 0; i < textsLength; i++) {
            //textview放进数组中，方便修改操作
            TextViews[i] = addTextView(context, VIEW_IDS[i]);
            TextViews[i].setOnTouchListener(mTouchListener);
            addView(TextViews[i]);
        }
        TextViews[0].requestFocus();//第一个输入框默认设置点中效果
        setTextViewsBackground(0);
    }

    private TextView addTextView(Context context, int id) {
        TextView tv = new TextView(context);
        tv.setId(id);
        tv.setGravity(Gravity.CENTER);
        tv.setMinEms(1);
        tv.setFocusableInTouchMode(true);
        tv.setFocusable(true);
        return tv;
    }

    public void setTextSize(float size) {
        for (TextView tv : TextViews) {
            tv.setTextSize(size);
        }
    }

    public void setTextColor(int color) {
        for (TextView tv : TextViews) {
            tv.setTextColor(color);
        }
    }


    /**
     * 显示输入框的TouchListener
     */
    private class OnFrameTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                tv.setFocusable(true);
                tv.requestFocus();
                String tvString = (String) tv.getText();
                if (TextUtils.isEmpty(tvString)) {
                    return false;
                }
                int viewId = tv.getId();
                for (int i = 0; i < ITEM_VIEW_COUNT; i++) {
                    if (viewId == VIEW_IDS[i]) {
                        TextViews[i].requestFocus();
                        if (onSelect != null) {
                            onSelect.select(i);
                        }
                    }
                }
            }
            return false;
        }
    }

    public void clearFocus() {
        for (int position = 0; position < ITEM_VIEW_COUNT; position++) {
            if (position == 0) {
                TextViews[0].setBackgroundResource(R.drawable.license_plate_first_view_all_gray);
                TextViews[0].clearFocus();
            }
            if (position < ITEM_VIEW_COUNT - 2 && position >= 1) {
                TextViews[position].setBackgroundResource(R.drawable.license_plate_view_right_gray);
                TextViews[position].clearFocus();
            }
            if (position == ITEM_VIEW_COUNT - 2) {
                TextViews[ITEM_VIEW_COUNT - 2].setBackgroundResource(R.drawable.license_plate_mid_view_bg);
                TextViews[ITEM_VIEW_COUNT - 2].clearFocus();
            }
            if (position == ITEM_VIEW_COUNT - 1) {
                TextViews[ITEM_VIEW_COUNT - 1].setBackgroundResource(R.drawable.license_plate_last_view_bg);
                TextViews[ITEM_VIEW_COUNT - 1].clearFocus();
            }

        }

    }

    public void setTextViewsBackground(int position) {
        //第一个框的样式
        if (position == 0) {
            TextViews[0].setBackgroundResource(R.drawable.license_plate_first_view_blue);
        } else {
            TextViews[0].setBackgroundResource(R.drawable.license_plate_first_view_all_gray);
        }
        //从第二个开始，到倒数第二个
        //根据点击选中效果，设置两边的样式
        if (position < ITEM_VIEW_COUNT - 2 && position >= 1) {
            for (int i = 1; i < ITEM_VIEW_COUNT - 2; i++) {
                TextViews[i].setBackgroundResource(R.drawable.license_plate_view_right_gray);
            }
            if (position == 1) {
                TextViews[position - 1].setBackgroundResource(R.drawable.license_plate_first_view_gray);
            } else {
                TextViews[position - 1].setBackgroundResource(R.drawable.license_plate_view_half_gray);
            }
            TextViews[position].setBackgroundResource(R.drawable.license_plate_mid_view_blue);
        } else {
            for (int i = 1; i < ITEM_VIEW_COUNT - 2; i++) {
                TextViews[i].setBackgroundResource(R.drawable.license_plate_view_right_gray);
            }
        }
        //倒数第二个框的样式，根据选中的效果，设置前后两个框的样式
        if (position == ITEM_VIEW_COUNT - 2) {
            TextViews[position].setBackgroundResource(R.drawable.license_plate_mid_view_blue);
            TextViews[position + 1].setBackgroundResource(R.drawable.license_plate_last_view_bg);
            TextViews[position - 1].setBackgroundResource(R.drawable.license_plate_view_half_gray);
        } else {
            TextViews[ITEM_VIEW_COUNT - 2].setBackgroundResource(R.drawable.license_plate_mid_view_bg);
        }
        //最后一个框的样式，根据选中的样式，前面一个样式需要改变
        if (position == ITEM_VIEW_COUNT - 1) {
            TextViews[position].setBackgroundResource(R.drawable.license_plate_last_view_blue);
            TextViews[position - 1].setBackgroundResource(R.drawable.license_plate_view_half_gray);
        } else {
            TextViews[ITEM_VIEW_COUNT - 1].setBackgroundResource(R.drawable.license_plate_last_view_bg);
        }
    }

    public void setOnSelect(OnSelect onSelect) {
        this.onSelect = onSelect;
    }

    interface OnSelect {
        void select(int select);
    }

}
