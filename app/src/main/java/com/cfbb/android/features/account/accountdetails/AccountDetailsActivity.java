package com.cfbb.android.features.account.accountdetails;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.state.RechargeStateEnum;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.features.account.AddBankActivity;
import com.cfbb.android.features.account.withdrawAndrecharge.RechargeActivity;
import com.cfbb.android.features.account.withdrawAndrecharge.RechargeRightActivity;
import com.cfbb.android.features.account.withdrawAndrecharge.WithDrawActivity;
import com.cfbb.android.features.authentication.RealNameAuthenticationActivity;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.APIService;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.RechargeInfoBean;
import com.cfbb.android.protocol.bean.WithDrawInfoBean;
import com.cfbb.android.widget.dialog.YCDialogUtils;

import java.util.ArrayList;

/***
 * 账户明细
 */
public class AccountDetailsActivity extends BaseActivity {

    private int currentPage = 0;// 当前显示位置

    private TextView tv_account_info;
    private TextView tv_invest_info;
    private TextView tv_loan_info;
    private ViewPager viewPager;
    private ImageView iv_cursor;
    private TextView tv_back;
    private TextView tv_title;

    private Button btn_withdraw;
    private Button btn_recharge;
    private YCDialogUtils ycDialogUtils;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_account_details);
    }

    @Override
    public void getDataOnCreate() {
        InitialViewPager();
        InitImageView();
    }

    public void setUpViews() {

        ycDialogUtils = new YCDialogUtils(this);
        btn_recharge = (Button) findViewById(R.id.btn_ok);
        btn_withdraw = (Button) findViewById(R.id.btn_withdraw);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_back.setText(getResources().getString(R.string.nav_account));
        tv_back.setVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.account_details_str));
        tv_account_info = (TextView) findViewById(R.id.tv_02);
        tv_invest_info = (TextView) findViewById(R.id.tv_03);
        tv_loan_info = (TextView) findViewById(R.id.tv_04);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        iv_cursor = (ImageView) findViewById(R.id.cursor);
    }

    public void setUpLisener() {
        btn_recharge.setOnClickListener(this);
        btn_withdraw.setOnClickListener(this);
        tv_back.setOnClickListener(this);
        tv_account_info.setOnClickListener(this);
        tv_invest_info.setOnClickListener(this);
        tv_loan_info.setOnClickListener(this);
    }


    @Override
    public void onUserClick(View v) {
        super.onUserClick(v);
        switch (v.getId()) {
            //充值
            case R.id.btn_ok:
                doRechargePre();
                break;
            //提现
            case R.id.btn_withdraw:
                addSubscription(RetrofitClient.GetWithdrawInfo(null, this, new YCNetSubscriber<WithDrawInfoBean>(this, true) {


                    @Override
                    public void onYcNext(WithDrawInfoBean model) {
                        //获取提现初始化信息成功
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(WithDrawActivity.WITHDRAW_RIGHT_TURN_TO_ACTIVITY_CLASS, AccountDetailsActivity.class);
                        bundle.putString(WithDrawActivity.SHOW_BACK_TXT, getResources().getString(R.string.account_details_str));
                        bundle.putParcelable(WithDrawActivity.WITHDRAW_INFO, model);
                        JumpCenter.JumpActivity(AccountDetailsActivity.this, WithDrawActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
                    }
                }));
                break;
            case R.id.tv_back:
                finish();
                break;
            //账户详情
            case R.id.tv_02:
                viewPager.setCurrentItem(0);
                break;
            //投资详情
            case R.id.tv_03:
                viewPager.setCurrentItem(1);
                break;
            //借款详情
            case R.id.tv_04:
                viewPager.setCurrentItem(2);
                break;
        }
    }

    private void doRechargePre() {

        addSubscription(RetrofitClient.GetRechargeInitalInfo(null, AccountDetailsActivity.this, new YCNetSubscriber<RechargeInfoBean>(AccountDetailsActivity.this, true) {

            @Override
            public void onYcNext(RechargeInfoBean model) {
                accountInfoFragment.setInit(true);
                if (model.rechargeState == RechargeStateEnum.FIRST_RECHARGE.getValue()) {

                    ycDialogUtils.showBindBankDialog(getString(R.string.bind_bankcrad_hint), new YCDialogUtils.MyTwoBtnclickLisener() {

                        @Override
                        public void onSecondBtnClick(View v) {

                            ycDialogUtils.DismissMyDialog();
                            Bundle bundle = new Bundle();
                            bundle.putString(AddBankActivity.BACK_TXT, getString(R.string.account_details_str));
                            bundle.putSerializable(AddBankActivity.ADDBANK_RIGHT_TURN_TO_ACTIVITY_CLASS, RechargeActivity.class);
                            JumpCenter.JumpActivity(AccountDetailsActivity.this, AddBankActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);

                        }

                        @Override
                        public void onFirstBtnClick(View v) {
                            ycDialogUtils.DismissMyDialog();
                        }

                    }, true);

                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString(RechargeActivity.SHOW_BACK_TXT, getResources().getString(R.string.account_details_str));
                    bundle.putParcelable(RechargeActivity.RECHARGEINFO_DATA, model);
                    bundle.putSerializable(RechargeRightActivity.RECHARGE_RIGHT_TURN_TO_ACTIVITY_CLASS, AccountDetailsActivity.class);
                    JumpCenter.JumpActivity(AccountDetailsActivity.this, RechargeActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);

                }


            }

            @Override
            public void onYCError(APIException e) {
                if (e.code == APIService.NO_AUTHENTICATION_CODE) {

                    ycDialogUtils.showAuthenticationDialog(e.getMessage(), new YCDialogUtils.MyTwoBtnclickLisener() {
                        @Override
                        public void onSecondBtnClick(View v) {
                            //ok
                            ycDialogUtils.DismissMyDialog();
                            Bundle bundle = new Bundle();
                            bundle.putString(RealNameAuthenticationActivity.SHOW_BACK_TXT, getResources().getString(R.string.account_details_str));
                            bundle.putSerializable(RealNameAuthenticationActivity.NEXT_ACTIVITY_CLASS, RechargeActivity.class);
                            JumpCenter.JumpActivity(AccountDetailsActivity.this, RealNameAuthenticationActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);

                        }

                        @Override
                        public void onFirstBtnClick(View v) {
                            ycDialogUtils.DismissMyDialog();
                        }

                    }, true);

                } else {
                    ycDialogUtils.showSingleDialog(AccountDetailsActivity.this.getResources().getString(R.string.dialog_title), e.msg, new YCDialogUtils.MySingleBtnclickLisener() {
                        @Override
                        public void onBtnClick(View v) {
                            ycDialogUtils.DismissMyDialog();
                        }
                    }, true);
                }
            }
        }));
    }

    private ArrayList<Fragment> list;
    private MyViewPagerAdaptor mAdaptor;
    private AccountInfoFragment accountInfoFragment;

    /**
     * 初始化ViewPager控件
     */
    private void InitialViewPager() {
        accountInfoFragment = new AccountInfoFragment();
        InvestInfoFragment investInfoFragment = new InvestInfoFragment();
        LoanInfoFragment loanInfoFragment = new LoanInfoFragment();

        list = new ArrayList<>();
        list.add(accountInfoFragment);
        list.add(investInfoFragment);
        list.add(loanInfoFragment);

        mAdaptor = new MyViewPagerAdaptor(getSupportFragmentManager());
        viewPager.setAdapter(mAdaptor);
        // 设置初始显示位置
        viewPager.setCurrentItem(0);
        // 设置页面切换监听事件
        viewPager.setOnPageChangeListener(new MyPageChangeLisenr());
        viewPager.setOffscreenPageLimit(3);

    }


    /**
     * fragmenpager适配器
     *
     * @author yucha_000
     */
    public class MyViewPagerAdaptor extends FragmentPagerAdapter {

        public MyViewPagerAdaptor(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int arg0) {
            return list.get(arg0);
        }

        @Override
        public int getCount() {
            return list.size();
        }

    }

    private float screenW1_3;
    private LinearLayout.LayoutParams lp;

    public class MyPageChangeLisenr implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        // arg0 :往右滑动，是当前页面，全部滑动完毕是那个页面序号，往左滑动是下一个页面序号
        // arg1:当前页面偏移的百分比
        // arg2:当前页面偏移的像素位置
        @Override
        public void onPageScrolled(int position, float positionOffset, int arg2) {

            // 核心代码
            lp.leftMargin = (int) (screenW1_3 * positionOffset + position
                    * screenW1_3);
            iv_cursor.setLayoutParams(lp);

        }

        @Override
        public void onPageSelected(int arg0) {

            ResetTextViewTitle();
            currentPage = arg0;
            switch (currentPage) {
                case 0:
                    tv_account_info.setTextColor(getResources().getColor(R.color.txt_red));
                    break;
                case 1:
                    tv_invest_info.setTextColor(getResources().getColor(R.color.txt_red));
                    break;
                case 2:
                    tv_loan_info.setTextColor(getResources().getColor(R.color.txt_red));
                    break;
                default:
                    break;
            }
        }

    }

    private void ResetTextViewTitle() {
        tv_account_info.setTextColor(getResources().getColor(R.color.txt_3));
        tv_invest_info.setTextColor(getResources().getColor(R.color.txt_3));
        tv_loan_info.setTextColor(getResources().getColor(R.color.txt_3));
    }

    private int screenW;
    private int bmpW;// 动画图片宽度

    /**
     * 初始化动画
     */
    private void InitImageView() {
        iv_cursor = (ImageView) findViewById(R.id.cursor);
        bmpW = BitmapFactory.decodeResource(getResources(), R.mipmap.diver_red)
                .getWidth();// 获取图片宽度
        // Andorid.util 包下的DisplayMetrics 类提供了一种关于显示的通用信息，如显示大小，分辨率和字体。
        // 为了获取DisplayMetrics 成员，首先初始化一个对象如下：
        // DisplayMetrics metrics ＝ new DisplayMetrics();
        // getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // 注：构造函数DisplayMetrics 不需要传递任何参数；调用getWindowManager() 之后，
        // 会取得现有Activity 的Handle ，此时，getDefaultDisplay()
        // 方法将取得的宽高维度存放于DisplayMetrics 对象中，
        // 而取得的宽高维度是以像素为单位(Pixel) ，“像素”所指的是“绝对像素”而非“相对像素”。
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenW = dm.widthPixels;
        screenW1_3 = screenW / 3;
        lp = (android.widget.LinearLayout.LayoutParams) iv_cursor
                .getLayoutParams();
        lp.width = screenW / 3;
        iv_cursor.setLayoutParams(lp);
        tv_account_info.setTextColor(getResources().getColor(R.color.txt_red));

    }


}
