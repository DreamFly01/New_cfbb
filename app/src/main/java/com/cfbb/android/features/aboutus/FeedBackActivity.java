package com.cfbb.android.features.aboutus;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.widget.dialog.YCDialogUtils;

/***
 * 意见反馈
 */
public class FeedBackActivity extends BaseActivity  {

    private TextView tv_back;
    private TextView tv_title;
    private EditText et_content;
    private TextView tv_txtNum;
    private Button btn_submit;

    private YCDialogUtils ycDialogUtils;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_feed_back);
    }

    @Override
    public void setUpViews() {

        ycDialogUtils = new YCDialogUtils(this);
        btn_submit = (Button) findViewById(R.id.btn_ok);
        et_content = (EditText) findViewById(R.id.et_01);
        tv_txtNum = (TextView) findViewById(R.id.tv_03);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_back.setVisibility(View.VISIBLE);
        tv_back.setText(getResources().getString(R.string.account_set_str));
        tv_title.setText(getResources().getString(R.string.feedback_str));
        et_content.addTextChangedListener(mTextWatcher);
        et_content.setSelection(et_content.length());
        setLeftCount();
    }

    @Override
    public void setUpLisener() {
        tv_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onUserClick(View v) {
        super.onUserClick(v);
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.btn_ok:
                if (CheckInput()) {
                    SubmitData();
                }
                break;
        }
    }


    private String content;

    private void SubmitData() {
        //TestResultUtils.getBaseSussefulResult()
        addSubscription(RetrofitClient.SubmitFeedBack(null, content, this, new YCNetSubscriber(this, true) {

            @Override
            public void onYcNext(Object model) {
                //提交成功
                showShortToast(R.string.submit_right);
                finish();
            }
        }));
    }

    private boolean CheckInput() {
        content = et_content.getText().toString().trim();
        if (StrUtil.isEmpty(content)) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getString(R.string.errro_content_can_not_bt_empty_hint), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }
        if (content.length() < 10) {
            ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getString(R.string.erro_min_length_must_larger_than_ten_hint), new YCDialogUtils.MySingleBtnclickLisener() {
                @Override
                public void onBtnClick(View v) {
                    ycDialogUtils.DismissMyDialog();
                }
            }, true);
            return false;
        }
        return true;
    }

    private static final int MAX_COUNT = 200;

    private TextWatcher mTextWatcher = new TextWatcher() {

        private int editStart;
        private int editEnd;

        public void afterTextChanged(Editable s) {
            editStart = et_content.getSelectionStart();
            editEnd = et_content.getSelectionEnd();

            // 先去掉监听器，否则会出现栈溢出
            et_content.removeTextChangedListener(mTextWatcher);

            // 这里只能每次都对整个EditText的内容求长度，不能对删除的单个字符求长度
            // 因为是中英文混合，单个字符而言，calculateLength函数都会返回1
            while (calculateLength(s.toString()) > MAX_COUNT) { // 当输入字符个数超过限制的大小时，进行截断操作
                s.delete(editStart - 1, editEnd);
                editStart--;
                editEnd--;
            }
            et_content.setSelection(editStart);

            // 恢复监听器
            et_content.addTextChangedListener(mTextWatcher);
            if (et_content.getText().toString().length() < 10) {
                btn_submit.setEnabled(false);
            } else {
                btn_submit.setEnabled(true);
            }
            setLeftCount();
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

    };

    private long calculateLength(CharSequence c) {
        return c.length();
    }

    private long getInputCount() {
        return calculateLength(et_content.getText().toString());
    }

    private void setLeftCount() {
        tv_txtNum.setText(getInputCount() + "/" + MAX_COUNT);
    }


    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
