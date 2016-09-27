package com.cfbb.android.features.account;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseFragment;
import com.cfbb.android.commom.config.Const;
import com.cfbb.android.commom.state.AccountTurnWhereEnum;
import com.cfbb.android.commom.state.IsHidenMoneyShowEnum;
import com.cfbb.android.commom.state.LoanReleasedStateEnum;
import com.cfbb.android.commom.state.MainFragmentEnum;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.commom.utils.base.PhoneUtils;
import com.cfbb.android.commom.utils.image.ImageWithGlideUtils;
import com.cfbb.android.commom.utils.others.SPUtils;
import com.cfbb.android.db.user.UserBiz;
import com.cfbb.android.features.account.accountdetails.AccountDetailsActivity;
import com.cfbb.android.features.account.myinvest.MyInvestActivity;
import com.cfbb.android.features.account.releaseLoan.AddLoanActivity;
import com.cfbb.android.features.account.withdrawAndrecharge.RechargeActivity;
import com.cfbb.android.features.authentication.RealNameAuthenticationActivity;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.APIService;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.AccountInfoBean;
import com.cfbb.android.protocol.bean.MyBankInfoBean;
import com.cfbb.android.protocol.bean.RechargeInfoBean;
import com.cfbb.android.widget.PullDownView;
import com.cfbb.android.widget.YCLoadingBg;
import com.cfbb.android.widget.dialog.YCDialogUtils;

/**
 * 我的财富
 */
public class AccountFragment extends BaseFragment implements View.OnClickListener {

    private static final int IS_CHANGE_USER_PHOTO = 1001;
    private AccountInfoBean accountInfoBean;
    private Boolean is_open_eye = true;

    private ImageView iv_potoh;
    private TextView tv_userName;
    private TextView tv_recharge_money;
    private TextView tv_total_aring;
    private TextView tv_acccount_total_money;
    private TextView tv_already_lean_moeny;
    private TextView tv_is_show;
    private TextView tv_custom_tel;
    private TextView tv_working_time;
    private ImageView iv_open_eye;
    private RelativeLayout rl_account_details;
    private RelativeLayout rl_my_invest;
    private RelativeLayout rl_trade_details;
    private RelativeLayout rl_release_loan;
    private RelativeLayout rl_my_bankcard_list;
    private RelativeLayout rl_my_red_paper;
    private TextView tv_red_pokage_num;
    private YCLoadingBg ycLoadingBg;
    private RelativeLayout rl_gift;
    private TextView tv_gift;
    private PullDownView pullDowmView;
    private ListView listView;
    private YCDialogUtils ycDialogUtils;

    public AccountFragment() {

    }

    @Override
    public int initContentView() {
        return R.layout.fragment_account;
    }

    @Override
    public void setUpLisener() {

        tv_custom_tel.setOnClickListener(this);
        rl_my_red_paper.setOnClickListener(this);
        rl_my_bankcard_list.setOnClickListener(this);
        rl_account_details.setOnClickListener(this);
        rl_trade_details.setOnClickListener(this);
        rl_my_invest.setOnClickListener(this);
        iv_open_eye.setOnClickListener(this);
        iv_potoh.setOnClickListener(this);
        tv_recharge_money.setOnClickListener(this);
        rl_release_loan.setOnClickListener(this);
        rl_gift.setOnClickListener(this);
        pullDowmView.setUpdateHandle(new PullDownView.UpdateHandle() {
            @Override
            public void onUpdate() {
                getDataOnActivityCreated();
            }
        });

    }

    private View viewHeader;

    @Override
    public void setUpViews(View view) {

        ycDialogUtils = new YCDialogUtils(getActivity());
        pullDowmView = (PullDownView) view.findViewById(R.id.pullDownView);
        listView = (ListView) view.findViewById(R.id.listView);
        //头部view
        viewHeader = LayoutInflater.from(getActivity()).inflate(R.layout.listview_account_item, null);
        listView.setHeaderDividersEnabled(true);
        listView.addHeaderView(viewHeader);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return null;
            }
        });

        ycLoadingBg = (YCLoadingBg) view.findViewById(R.id.ycLoadingBg);

        iv_potoh = (ImageView) viewHeader.findViewById(R.id.iv_01);
        tv_userName = (TextView) viewHeader.findViewById(R.id.tv_title);
        tv_recharge_money = (TextView) viewHeader.findViewById(R.id.tv_02);
        tv_total_aring = (TextView) viewHeader.findViewById(R.id.tv_03);
        tv_acccount_total_money = (TextView) viewHeader.findViewById(R.id.tv_04);
        tv_already_lean_moeny = (TextView) viewHeader.findViewById(R.id.tv_05);
        tv_is_show = (TextView) viewHeader.findViewById(R.id.tv_06);
        tv_custom_tel = (TextView) viewHeader.findViewById(R.id.tv_07);
        tv_working_time = (TextView) viewHeader.findViewById(R.id.tv_08);
        tv_red_pokage_num = (TextView) viewHeader.findViewById(R.id.tv_back);
        iv_open_eye = (ImageView) viewHeader.findViewById(R.id.iv_04);

        rl_account_details = (RelativeLayout) viewHeader.findViewById(R.id.rl_01);
        rl_my_invest = (RelativeLayout) viewHeader.findViewById(R.id.rl_02);
        rl_trade_details = (RelativeLayout) viewHeader.findViewById(R.id.rl_03);
        rl_release_loan = (RelativeLayout) viewHeader.findViewById(R.id.rl_04);
        rl_my_bankcard_list = (RelativeLayout) viewHeader.findViewById(R.id.rl_05);
        rl_my_red_paper = (RelativeLayout) viewHeader.findViewById(R.id.rl_06);

        rl_gift = (RelativeLayout) viewHeader.findViewById(R.id.rl_07);
        tv_gift = (TextView) viewHeader.findViewById(R.id.tv_12);
    }

    private Boolean hintDialogHasShow = false;

    private void FillView() {
        if (null != accountInfoBean) {

            SPUtils.put(getActivity(), Const.CUSTOM_TEL, accountInfoBean.customerservice);

            tv_userName.setText(accountInfoBean.userName);
            if (accountInfoBean.isRelease == LoanReleasedStateEnum.RELEASED.getValue()) {
                tv_is_show.setText(getResources().getString(R.string.is_released));
            }
            if (accountInfoBean.isRelease == LoanReleasedStateEnum.UNRELEASED.getValue()) {
                tv_is_show.setText(getResources().getString(R.string.not_released));
            }
            is_open_eye = UserBiz.getInstance(getActivity()).Is_Show_Money();
            if (is_open_eye) {
                OpenEye();
            } else {
                CloseEye();
            }
            tv_gift.setText(accountInfoBean.giftNum + "");
            tv_red_pokage_num.setText(accountInfoBean.myRedPakageNum);
            tv_custom_tel.setText(accountInfoBean.customerservice);
            tv_working_time.setText(accountInfoBean.workingTime);
            ImageWithGlideUtils.lodeFromUrlRoundTransform(accountInfoBean.userImageUrl, R.mipmap.default_user_photo_smarll_bg, iv_potoh, getActivity());
            UserBiz.getInstance(getActivity()).UpdateUserPhotoAddr(accountInfoBean.userImageUrl);

            if (accountInfoBean.isShowHint == 1) {
                if (!hintDialogHasShow) {
                    hintDialogHasShow = true;
                    ycDialogUtils.showSingleDialog(getActivity().getResources().getString(R.string.dialog_kindly_title), accountInfoBean.hintContent, new YCDialogUtils.MySingleBtnclickLisener() {
                        @Override
                        public void onBtnClick(View v) {
                            ycDialogUtils.DismissMyDialog();
                            if (accountInfoBean.turnWhere == AccountTurnWhereEnum.JUMP_MY_GIFT_PAPAER.getValue()) {
                                JumpCenter.JumpActivity(getActivity(), MyGiftActivity.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
                            }
                            if (accountInfoBean.turnWhere == AccountTurnWhereEnum.JUMP_MY_RED_PAPAER.getValue()) {
                                JumpCenter.JumpActivity(getActivity(), MyRedPaperActivity.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
                            }
                        }
                    }, true);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //我的红包
            case R.id.rl_06:
                JumpCenter.JumpActivity(getActivity(), MyRedPaperActivity.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
                break;
            //我的银行卡
            case R.id.rl_05:
                goToMyBankInfo();
                break;
            //账户明细
            case R.id.rl_01:
                JumpCenter.JumpActivity(getActivity(), AccountDetailsActivity.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
                break;
            //我的投资
            case R.id.rl_02:
                JumpCenter.JumpActivity(getActivity(), MyInvestActivity.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
                break;
            //闭眼
            case R.id.iv_04:
                if (UserBiz.getInstance(getActivity()).Is_Show_Money()) {
                    CloseEye();
                } else {
                    OpenEye();
                }
                break;
            //账户设置
            case R.id.iv_01:
                JumpCenter.JumpActivity(getActivity(), AccountSetActivity.class, null, null, IS_CHANGE_USER_PHOTO, JumpCenter.INVAILD_FLAG, false, true);
                break;
            //交易记录
            case R.id.rl_03:
                JumpCenter.JumpActivity(getActivity(), TradeRecordActivity.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
                break;
            //发布借款
            case R.id.rl_04:
                JumpCenter.JumpActivity(getActivity(), AddLoanActivity.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
                break;
            //我的礼品
            case R.id.rl_07:
                JumpCenter.JumpActivity(getActivity(), MyGiftActivity.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
                break;
            //充值
            case R.id.tv_02:
                doRechargePre();
                break;
            //点击服务热线
            case R.id.tv_07:
                if (accountInfoBean != null && accountInfoBean.customerservice != null) {
                    ycDialogUtils.showCallDialog(accountInfoBean.customerservice, new YCDialogUtils.MyTwoBtnclickLisener() {
                        @Override
                        public void onFirstBtnClick(View v) {
                            ycDialogUtils.DismissMyDialog();
                        }

                        @Override
                        public void onSecondBtnClick(View v) {
                            ycDialogUtils.DismissMyDialog();
                            PhoneUtils.callDierct(getActivity(), accountInfoBean.customerservice);
                        }
                    }, true);
                }
                break;
        }
    }

    private void goToMyBankInfo() {

        addSubscription(RetrofitClient.GetMyBankInfo(null, getActivity(), new YCNetSubscriber<MyBankInfoBean>(getActivity(), true) {

            @Override
            public void onYcNext(MyBankInfoBean model) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(MyBankInfoActivity.MY_BANK_INFO, model);
                JumpCenter.JumpActivity(getActivity(), MyBankInfoActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
            }

            @Override
            public void onYCError(APIException e) {
                dismissLoadingView();
                if (e.code == APIService.NO_AUTHENTICATION_CODE) {
                    //code 2  代表未认证
                    ycDialogUtils.showAuthenticationDialog(e.getMessage(), new YCDialogUtils.MyTwoBtnclickLisener() {

                        @Override
                        public void onSecondBtnClick(View v) {

                            //ok
                            ycDialogUtils.DismissMyDialog();
                            Bundle bundle = new Bundle();
                            bundle.putString(RealNameAuthenticationActivity.SHOW_BACK_TXT, getResources().getString(R.string.nav_account));
                            bundle.putSerializable(RealNameAuthenticationActivity.NEXT_ACTIVITY_CLASS, RechargeActivity.class);
                            JumpCenter.JumpActivity(getActivity(), RealNameAuthenticationActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);

                        }

                        @Override
                        public void onFirstBtnClick(View v) {
                            ycDialogUtils.DismissMyDialog();
                        }
                    }, true);

                }
                if (e.code == APIService.NO_BANKINFO_CODE) {
                    //code == 4  代表无绑定银行卡
                    Bundle bundle = new Bundle();
                    bundle.putString(AddBankActivity.BACK_TXT, getString(R.string.nav_account));
                    JumpCenter.JumpActivity(getActivity(), AddBankActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
                } else {
                    ycDialogUtils.showSingleDialog(getActivity().getResources().getString(R.string.dialog_title), getActivity().getResources().getString(R.string.request_erro_str), new YCDialogUtils.MySingleBtnclickLisener() {
                        @Override
                        public void onBtnClick(View v) {
                            ycDialogUtils.DismissMyDialog();
                        }
                    }, true);
                }

            }

        }));
    }

    private void doRechargePre() {
        addSubscription(RetrofitClient.GetRechargeInitalInfo(null, getActivity(), new YCNetSubscriber<RechargeInfoBean>(getActivity(), true) {

            @Override
            public void onYcNext(RechargeInfoBean model) {
                Bundle bundle = new Bundle();
                bundle.putString(RechargeActivity.SHOW_BACK_TXT, getResources().getString(R.string.nav_account));
                bundle.putInt(RechargeActivity.RECHARGE_RIGHT_TURN_TO_MAIN_INDEX, MainFragmentEnum.ACCOUNT.getValue());
                bundle.putParcelable(RechargeActivity.RECHARGEINFO_DATA, model);
                JumpCenter.JumpActivity(getActivity(), RechargeActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
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
                            bundle.putString(RealNameAuthenticationActivity.SHOW_BACK_TXT, getResources().getString(R.string.nav_account));
                            bundle.putSerializable(RealNameAuthenticationActivity.NEXT_ACTIVITY_CLASS, RechargeActivity.class);
                            JumpCenter.JumpActivity(getActivity(), RealNameAuthenticationActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);

                        }

                        @Override
                        public void onFirstBtnClick(View v) {
                            ycDialogUtils.DismissMyDialog();
                        }
                    }, true);

                } else {
                    ycDialogUtils.showSingleDialog(getActivity().getResources().getString(R.string.dialog_title), getActivity().getResources().getString(R.string.request_erro_str), new YCDialogUtils.MySingleBtnclickLisener() {
                        @Override
                        public void onBtnClick(View v) {
                            ycDialogUtils.DismissMyDialog();
                        }
                    }, true);
                }

            }
        }));


    }

    private void OpenEye() {
        UserBiz.getInstance(getActivity()).UpdateUserMoenyShowState(IsHidenMoneyShowEnum.SHOW.getValue());
        iv_open_eye.setImageResource(R.mipmap.eye_open_ico);
        tv_total_aring.setText(accountInfoBean.totalEaring);
        tv_acccount_total_money.setText(accountInfoBean.accountTotalMoney);
        tv_already_lean_moeny.setText(accountInfoBean.allreadyLoanMoeny);
    }

    private void CloseEye() {
        UserBiz.getInstance(getActivity()).UpdateUserMoenyShowState(IsHidenMoneyShowEnum.HIDDEN.getValue());
        iv_open_eye.setImageResource(R.mipmap.eye_close_ico);
        tv_total_aring.setText(R.string.six_start_str);
        tv_acccount_total_money.setText(R.string.six_start_str);
        tv_already_lean_moeny.setText(R.string.six_start_str);
    }

    private boolean isFrist = true;

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden()) {
            StatService.onResume(this);
        }
        if (isFrist) {
            isFrist = false;
        }

    }

    private boolean isHidden;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isHidden = hidden;
        if (hidden) {
            //不在最前端界面显示
            StatService.onPause(this);
        } else {//
            // 重新显示到最前端中
            StatService.onResume(this);
            MyGetData();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isFrist = true;
        if (!isHidden) {
            StatService.onPause(this);
        }
    }

    @Override
    public void getDataOnActivityCreated() {
        MyGetData();
    }

    public void MyGetData() {

        //TestResultUtils.getSussefulResult3()
        addSubscription(RetrofitClient.getAccountInfo(null, getActivity(), new YCNetSubscriber<AccountInfoBean>(getActivity()) {
            @Override
            public void onYcFinish() {
                pullDowmView.endUpdate();
            }

            @Override
            public void onYcNext(AccountInfoBean model) {
                accountInfoBean = model;
                FillView();
                ycLoadingBg.dissmiss();
            }

            @Override
            public void onYCError(APIException e) {
                ycLoadingBg.showErroBg(new YCLoadingBg.YCErroLisener() {
                    @Override
                    public void onTryAgainClick() {
                        MyGetData();
                    }
                });
            }

        }));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (IS_CHANGE_USER_PHOTO == requestCode && resultCode == Activity.RESULT_OK) {
            MyGetData();
        }
    }
}
