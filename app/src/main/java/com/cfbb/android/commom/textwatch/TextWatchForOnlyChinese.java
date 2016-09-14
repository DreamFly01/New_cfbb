package com.cfbb.android.commom.textwatch;

import android.text.Editable;
import android.text.TextWatcher;

import com.cfbb.android.commom.utils.others.StrUtil;

/**
 * @author MrChang45
 * @time 2016/7/26
 * @desc 只允许输入汉字
 */
public class TextWatchForOnlyChinese implements TextWatcher {

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

        if(!StrUtil.isEmpty(mAfterStr)) {
            if (!StrUtil.isChinese(mAfterStr.substring(mAfterStr.length() - 1, mAfterStr.length()))) {
                s.delete(mAfterStr.length() - 1, mAfterStr.length());
            }
        }
    }
}
