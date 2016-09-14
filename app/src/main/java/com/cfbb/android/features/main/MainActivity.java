package com.cfbb.android.features.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.state.MainFragmentEnum;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.db.user.UserBiz;
import com.cfbb.android.features.account.AccountFragment;
import com.cfbb.android.features.authentication.LoginActivity;
import com.cfbb.android.features.home.HomeFragment;
import com.cfbb.android.features.invest.InvestContentFragment;
import com.cfbb.android.features.invest.InvestFragment;


/**
 * @author MrChang
 *         created  at  2015/12/28.
 * @description main
 */
public class MainActivity extends BaseActivity implements InvestContentFragment.OnProductTypeChangeListener {


    public static final String WITCH_TO_DO = "witch_to_do";
    //充值
    public static final int RECHANGER_GOON = 99901;
    //提现
    public static final int WITHDRAW_GOON = 99902;

    public static final String SHOW_FRAGMENT_INDEX = "show_fragment_index";
    /**
     * 退出间隔时间 单位毫秒
     */
    public static final int EXIT_TIME = 2000;
    /**
     * 底部三个按钮
     */
    private LinearLayout mTabBtnHome;
    private LinearLayout mTabBtnInvest;
    private LinearLayout mTabBtnAccount;
    private ImageView iv_home;
    private TextView tv_home;
    private ImageView iv_invest;
    private TextView tv_invest;
    private ImageView iv_account;
    private TextView tv_acccunt;
    private HomeFragment homeFragment;
    private InvestFragment investFragment;
    private AccountFragment accountFragment;

    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;
    private Intent intent;
    private int index = MainFragmentEnum.HOME.getValue();

    private Bundle bundle;
    private int witch_to_do = -1;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        setBackEnable(false);
        InitailFragments();
    }

    private void InitailFragments() {
        fragmentManager = getSupportFragmentManager();
        initTabBar();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            index = bundle.getInt(SHOW_FRAGMENT_INDEX, MainFragmentEnum.HOME.getValue());
        }
        setTabSelection(index);
    }

    @Override
    public void setUpViews() {

    }

    @Override
    public void setUpLisener() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        homeFragment = null;
        investFragment = null;
        accountFragment = null;
        fragmentManager = null;
    }


    /***
     * 初始化底部导航
     */
    private void initTabBar() {

        iv_home = (ImageView) findViewById(R.id.iv_01);
        tv_home = (TextView) findViewById(R.id.tv_title);

        iv_invest = (ImageView) findViewById(R.id.iv_02);
        tv_invest = (TextView) findViewById(R.id.tv_02);

        iv_account = (ImageView) findViewById(R.id.iv_03);
        tv_acccunt = (TextView) findViewById(R.id.tv_03);

        mTabBtnHome = (LinearLayout) findViewById(R.id.ll_01);
        mTabBtnInvest = (LinearLayout) findViewById(R.id.ll_02);
        mTabBtnAccount = (LinearLayout) findViewById(R.id.ll_03);

        mTabBtnHome.setOnClickListener(this);
        mTabBtnInvest.setOnClickListener(this);
        mTabBtnAccount.setOnClickListener(this);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    WitchToDo();
                    break;
            }
        }
    };

    /**
     * 根据传入的index参数来设置选中的tab页。
     */
    private void setTabSelection(int index) {

        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                // 重置按钮
                resetBtn();
                // 当点击了消息tab时，改变控件的图片和文字颜色
                iv_home.setBackgroundResource(R.mipmap.home_seleced);
                tv_home.setTextColor(getResources().getColor(R.color.txt_red));
                if (homeFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.framecontent, homeFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(homeFragment);

                }
                transaction.commit();
                //Fragment attach to activity need some times
                handler.sendEmptyMessageDelayed(0, 1000);
                break;
            case 1:
                // 重置按钮
                resetBtn();
                // 当点击了消息tab时，改变控件的图片和文字颜色
                iv_invest.setBackgroundResource(R.mipmap.invest_selecteds);
                tv_invest.setTextColor(getResources().getColor(R.color.txt_red));

                if (investFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    investFragment = new InvestFragment();
                    transaction.add(R.id.framecontent, investFragment);

                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(investFragment);
                }
                transaction.commit();
                break;
            case 2:
                // 当点击了动态tab时，改变控件的图片和文字颜色
                if (UserBiz.getInstance(this).CheckLoginState()) {
                    // 重置按钮
                    resetBtn();
                    iv_account.setBackgroundResource(R.mipmap.account_selected);
                    tv_acccunt.setTextColor(getResources().getColor(R.color.txt_red));
                    if (accountFragment == null) {
                        // 如果NewsFragment为空，则创建一个并添加到界面上
                        accountFragment = new AccountFragment();
                        transaction.add(R.id.framecontent, accountFragment);
                    } else {
                        // 如果NewsFragment不为空，则直接将它显示出来
                        transaction.show(accountFragment);
                    }
                    transaction.commit();

                } else {
                    //未登录跳转登录
                    Bundle bundle = new Bundle();
                    bundle.putInt(SHOW_FRAGMENT_INDEX, MainFragmentEnum.ACCOUNT.getValue());
                    JumpCenter.JumpActivity(MainActivity.this, LoginActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, Intent.FLAG_ACTIVITY_CLEAR_TASK, false, false);
                }
                break;
        }

    }


    private void WitchToDo() {
        witch_to_do = bundle.getInt(WITCH_TO_DO);
        switch (witch_to_do) {
            case RECHANGER_GOON:
                homeFragment.doRechargePre();
                break;
            case WITHDRAW_GOON:
                homeFragment.doWithdraw();
                break;
        }
        bundle.clear();
    }


    /**
     * 清除掉所有的选中状态。
     */
    private void resetBtn() {
        iv_home.setBackgroundResource(R.mipmap.home_unselected);
        tv_home.setTextColor(getResources().getColor(R.color.txt_gray));

        iv_invest.setBackgroundResource(R.mipmap.invest_unselecteds);
        tv_invest.setTextColor(getResources().getColor(R.color.txt_gray));

        iv_account.setBackgroundResource(R.mipmap.account_unselected);
        tv_acccunt.setTextColor(getResources().getColor(R.color.txt_gray));
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {

        if (homeFragment != null) {
            if (!homeFragment.isHidden()) {
            }
            transaction.hide(homeFragment);
        }
        if (investFragment != null) {
            if (!investFragment.isHidden()) {
            }
            transaction.hide(investFragment);
        }
        if (accountFragment != null) {
            if (!accountFragment.isHidden()) {
            }
            transaction.hide(accountFragment);
        }
    }


    private long mExitTime = 0;
    private Toast toast;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > EXIT_TIME) {
                toast = Toast.makeText(this, getString(R.string.exit_str), Toast.LENGTH_SHORT);
                toast.show();
                mExitTime = System.currentTimeMillis();
            } else {
                if (null != toast) {
                    toast.cancel();
                }
                finish();
                //System.exit(0);
               // android.os.Process.killProcess(android.os.Process.myPid());
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onUserClick(View v) {
        super.onUserClick(v);
        switch (v.getId()) {
            case R.id.ll_01:
                setTabSelection(0);
                break;
            case R.id.ll_02:
                setTabSelection(1);
                break;
            case R.id.ll_03:
                setTabSelection(2);
                break;
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (intent != null) {
            bundle = getIntent().getExtras();
            index = bundle.getInt(SHOW_FRAGMENT_INDEX, MainFragmentEnum.HOME.getValue());
        }
        setTabSelection(index);
    }

    @Override
    public void onShowProductTypeName(String typeName) {

    }
}
