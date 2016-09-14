package com.cfbb.android.commom.textwatch;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * @author MrChang45
 * @time 2016/7/26
 * @desc 不允许输入空格
 */
public class TextWatchForNoBlank implements TextWatcher {

    private String mAfterStr = "";

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mAfterStr = s.toString();
    }

    @Override
    public void afterTextChanged(Editable s) {

        if (mAfterStr.contains(" ")) {
            s.delete(mAfterStr.length() - 1, mAfterStr.length());
        }
    }
}
