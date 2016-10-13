package com.cfbb.android.commom.baseview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.cfbb.android.R;
import com.cfbb.android.app.MyApplication;
import com.cfbb.android.commom.config.Const;
import com.cfbb.android.commom.utils.base.PhoneUtils;
import com.cfbb.android.commom.utils.others.SPUtils;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.db.user.UserBiz;
import com.cfbb.android.features.gesture.GestureVerifyActivity;
import com.cfbb.android.features.slidingFinishView.SwipeBackFragment;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.APIService;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.BaseResultBean;
import com.cfbb.android.protocol.bean.UnsupportedBankCardBean;
import com.cfbb.android.widget.dialog.YCDialogUtils;

import java.util.Calendar;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/***
 * @author MrChang
 *         create at 2015-12-08
 * @description 公共activity基类
 */
public abstract class BaseActivity extends SwipeBackFragment implements View.OnClickListener {

    protected Intent mIntent;

    //管理异步处理与Activity生命周期,避免出现内存泄漏
    private CompositeSubscription mCompositeSubscription;

    public BaseActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(savedInstanceState);
        setUpViews();
        setUpLisener();
        getDataOnCreate();
    }


    private YCDialogUtils ycDialogUtils;
    private String msg = null;

    @Override
    protected void onStart() {
        super.onStart();

        msg = (String) SPUtils.get(BaseActivity.this, Const.SAFE_UPDATE_MSG, "");
        IsExsitUnSupportBankCard();

    }

    @Override
    protected void onResume() {
        super.onResume();
        PhoneUtils.isEmulator(this);
        CheckGestureState();

    }

    private void IsExsitUnSupportBankCard() {

        if (UserBiz.getInstance(this).CheckLoginState() && !msg.equals("-1")) {

            BaseResultBean<UnsupportedBankCardBean> bankCardBeanBaseResultBean = new BaseResultBean<>();
            bankCardBeanBaseResultBean.code = APIService.OK_CODE;
            UnsupportedBankCardBean unsupportedBankCardBean = new UnsupportedBankCardBean();
            unsupportedBankCardBean.content = "sssss";
            bankCardBeanBaseResultBean.data = unsupportedBankCardBean;

            RetrofitClient.IsExsitUnSupportBankCard(null, this, new YCNetSubscriber<UnsupportedBankCardBean>(this) {
                @Override
                public void onYcNext(UnsupportedBankCardBean model) {

                    if (ycDialogUtils != null) {
                        ycDialogUtils.DismissMyDialog();
                    }
                    if (!StrUtil.isEmpty(model.content)) {
                        ycDialogUtils = new YCDialogUtils(BaseActivity.this);
                        ycDialogUtils.showunBindBankDialog(model.content, new YCDialogUtils.MySingleBtnclickLisener() {

                            @Override
                            public void onBtnClick(View v) {
                                UnBindBankCard();
                            }

                        }, false);
                    } else {
                        SPUtils.put(BaseActivity.this, Const.SAFE_UPDATE_MSG, "-1");
                    }
                }

            });
        }

    }

    private void UnBindBankCard() {
        ycDialogUtils.DismissMyDialog();
        RetrofitClient.UnBundlingNoSupportBank(null, this, new YCNetSubscriber(this, true) {

            @Override
            public void onYCError(APIException e) {

                ycDialogUtils.showSingleDialog(getString(R.string.dialog_kindly_title), getString(R.string.unbindFalied), new YCDialogUtils.MySingleBtnclickLisener() {
                    @Override
                    public void onBtnClick(View v) {
                        ycDialogUtils.DismissMyDialog();
                    }
                }, true);
            }

            @Override
            public void onYcNext(Object model) {
                SPUtils.put(BaseActivity.this, Const.SAFE_UPDATE_MSG, "-1");
                // JumpCenter.JumpActivity(BaseActivity.this, MainActivity.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);

    }

    protected void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }

    /***
     * 是否隐藏键盘
     *
     * @param v
     * @param event
     * @return
     */
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private Long preShowDates = 0L;

    public void CheckGestureState() {

        // 检查是否登录状态，检查是否需要弹出手势密码
        if (UserBiz.getInstance(this).CheckLoginState()) {

            //检测是否打开了手势密码
            if (UserBiz.getInstance(this).Is_Open_Gesture()) {

                //检测是否有可以弹出手势密码
                if (MyApplication.getInstance().isGestureCanShow()) {
                    //如果上次显示时间 跟这次时间间隔超过一分钟就显示，否则不显示
                    preShowDates = Long.valueOf(SPUtils.get(this, Const.GESTURE_SHOW_TIME, Long.valueOf("0")).toString());
                    if ((System.currentTimeMillis() - preShowDates) > 60000) {
                        //条件同时满足，弹出手势密码界面
                        mIntent = new Intent(this, GestureVerifyActivity.class);
                        startActivity(mIntent);
                    } else {
                        MyApplication.getInstance().setGestureCanShow(false);
                    }

                }
            }
        }
    }

    /**
     * 长时间显示Toast提示(来自String)
     *
     * @param message
     */
    public void showLongToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间显示Toast提示(来自res)
     *
     * @param resId
     */
    public void showLongToast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_LONG).show();
    }

    /**
     * 短暂显示Toast提示(来自res)
     *
     * @param resId
     */
    protected void showShortToast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
    }

    /**
     * 短暂显示Toast提示(来自String)
     *
     * @param text
     */
    protected void showShortToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * setContentView
     *
     * @param savedInstanceState
     */
    protected abstract void initContentView(Bundle savedInstanceState);

    /***
     * 初始化试图
     */
    public abstract void setUpViews();

    /***
     * 注册事件
     */
    public abstract void setUpLisener();


    /**
     * 加载数据
     */
    public void getDataOnCreate() {

    }

    /**
     * 用户点击事件响应
     * 注：如果某个页面想屏蔽防重复点击，只需子类实现 onClick，注意在子类里面不能调用super.onclick
     * 如果只想屏蔽某个点击事件，只需针对该view单独new 一个View.onClicklisner
     *
     * @param v
     */
    public void onUserClick(View v) {

    }

    //两次点击最小间隔时间
    public static final int MIN_CLICK_DELAY_TIME = 500;
    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        //防止重复点击
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onUserClick(v);
        }
    }
}
