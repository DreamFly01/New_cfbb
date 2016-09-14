package com.cfbb.android.commom.textwatch;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.cfbb.android.commom.utils.others.StrUtil;

/**
 * @author MrChang45
 * @time 2016/7/25
 * @desc 输入银行卡号输入 四个一空
 */
public class TextWatchForBankNumber implements TextWatcher {

    private String mAfterStr = "";
    private String mChangeStr = "";
    private int mIndex = 0;
    private String mBeforeStr = "";
    private boolean mChangeIndex = true;
    //接收银行卡号输入EditText
    private EditText mEtPerpose;

    public TextWatchForBankNumber(EditText mEtPerpose) {
        this.mEtPerpose = mEtPerpose;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        mBeforeStr = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before,
                              int count) {
        mAfterStr = s.toString();
        if (mChangeIndex)
            mIndex = mEtPerpose.getSelectionStart();
    }

    @Override
    public void afterTextChanged(Editable s) {

        if (StrUtil.isEmpty(s.toString()) || mBeforeStr.equals(mAfterStr)) {
            mChangeIndex = true;
            return;
        }
        mChangeIndex = false;
        //先去掉全部空格，方便接下来处理
        char c[] = s.toString().replace(" ", "").toCharArray();
        mChangeStr = "";
        //4个一划分
        for (int i = 0; i < c.length; i++) {
            mChangeStr = mChangeStr + c[i] + ((i + 1) % 4 == 0 && i + 1 != c.length ? " " : "");
        }
        if (mAfterStr.length() > mBeforeStr.length()) {
            if (mChangeStr.length() == mIndex + 1) {
                mIndex = mChangeStr.length() - mAfterStr.length() + mIndex;
            }
            if (mIndex % 5 == 0 && mChangeStr.length() > mIndex + 1) {
                mIndex++;
            }
        } else if (mAfterStr.length() < mBeforeStr.length()) {
            if ((mIndex + 1) % 5 == 0 && mIndex > 0 && mChangeStr.length() > mIndex + 1) {
            } else {
                mIndex = mChangeStr.length() - mAfterStr.length() + mIndex;
                if (mAfterStr.length() % 5 == 0 && mChangeStr.length() > mIndex + 1) {
                    mIndex++;
                }
            }
        }

        mEtPerpose.setText(mChangeStr);
        mEtPerpose.setSelection(mChangeStr.length());

    }

}

