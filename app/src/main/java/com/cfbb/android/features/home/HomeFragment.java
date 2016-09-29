package com.cfbb.android.features.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseFragment;
import com.cfbb.android.commom.config.Const;
import com.cfbb.android.commom.state.MainFragmentEnum;
import com.cfbb.android.commom.state.RechargeStateEnum;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.commom.utils.image.ImageWithGlideUtils;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.commom.utils.others.Utils;
import com.cfbb.android.db.user.UserBiz;
import com.cfbb.android.features.account.AddBankActivity;
import com.cfbb.android.features.account.withdrawAndrecharge.RechargeActivity;
import com.cfbb.android.features.account.withdrawAndrecharge.WithDrawActivity;
import com.cfbb.android.features.authentication.LoginActivity;
import com.cfbb.android.features.authentication.RealNameAuthenticationActivity;
import com.cfbb.android.features.invest.InvestBidActivity;
import com.cfbb.android.features.main.MainActivity;
import com.cfbb.android.features.product.ProductDetailsActivity;
import com.cfbb.android.features.webview.OtherActivity;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.APIService;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.HomeInfoBean;
import com.cfbb.android.protocol.bean.RechargeInfoBean;
import com.cfbb.android.protocol.bean.UserMsgBean;
import com.cfbb.android.protocol.bean.WithDrawInfoBean;
import com.cfbb.android.widget.PullDownView;
import com.cfbb.android.widget.YCLoadingBg;
import com.cfbb.android.widget.convenientbanner.ConvenientBanner;
import com.cfbb.android.widget.convenientbanner.holder.CBViewHolderCreator;
import com.cfbb.android.widget.convenientbanner.holder.Holder;
import com.cfbb.android.widget.convenientbanner.listener.OnItemClickListener;
import com.cfbb.android.widget.dialog.YCDialogUtils;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/***
 * 首页
 */
public class HomeFragment extends BaseFragment implements AdapterView.OnItemClickListener, ImagePagerAdapter.ImageClickListener, OnItemClickListener {

    private PullDownView pullDowmView;
    private ListView listView;
    private ImageView iv_menu;
    private View viewHeader;
    private homeAdaptor mHomeAdaptor;
    private HomeInfoBean homeInfoBean;
    private YCLoadingBg ycLoadingBg;
    private ViewFlipper homepage_notice_vf;
    private LinearLayout ll_hot_hotitem;
    private TextView tv_hot_title;
    private ImageView iv_hot_repayment_way;
    private TextView tv_hot_rate;
    private TextView tv_hot_total_money;
    private TextView tv_hot_invest_date;
    private TextView tv_hot_statr_invest_moeny;
    private Button btn_hot_invest;
    private ImageView iv_product_type;
    private int mCurrPos;
    private ScheduledExecutorService scheduledExecutorService = null;
    private PopupWindow popupWindow;
    private LinearLayout ll_recharge;
    private LinearLayout ll_withdraw;
    private LinearLayout ll_infocenter;
    private LinearLayout ll_helpercenter;
    private YCDialogUtils ycDialogUtils;

    private ConvenientBanner convenientBanner;//顶部广告栏控件

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_home;
    }

    @Override
    public void getDataOnActivityCreated() {

        //TestResultUtils.getSussefulResult15()
        addSubscription(RetrofitClient.getHomeRequest(null, getActivity(), new YCNetSubscriber<HomeInfoBean>(getActivity()) {

            @Override
            public void onYcNext(HomeInfoBean model) {

                homeInfoBean = model;
                adsBeens = homeInfoBean.ads;
                initBanner(homeInfoBean.ads);
                initRollNotice();
                FillHeaderView(homeInfoBean);
                ycLoadingBg.dissmiss();

            }

            @Override
            public void onYCError(APIException e) {

                ycLoadingBg.showErroBg(new YCLoadingBg.YCErroLisener() {
                    @Override
                    public void onTryAgainClick() {
                        getDataOnActivityCreated();
                    }
                });

            }

            @Override
            public void onYcFinish() {
                pullDowmView.endUpdate();
            }

        }));

    }


    @Override
    public void setUpLisener() {
        ll_hot_hotitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != homeInfoBean) {
                    TurnToProductDetailsActivity(homeInfoBean.hotrecommend.prodcutId, homeInfoBean.hotrecommend.loanTypeId);
                }
            }
        });
        listView.setOnItemClickListener(this);
        btn_hot_invest.setOnClickListener(this);
        iv_menu.setOnClickListener(this);
        pullDowmView.setUpdateHandle(new PullDownView.UpdateHandle() {
            @Override
            public void onUpdate() {
                getDataOnActivityCreated();
            }
        });

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void setUpViews(View view) {

        ycDialogUtils = new YCDialogUtils(getActivity());
        ycLoadingBg = (YCLoadingBg) view.findViewById(R.id.ycLoadingBg);
        pullDowmView = (PullDownView) view.findViewById(R.id.pullDownView);

        listView = (ListView) view.findViewById(R.id.listView);
        iv_menu = (ImageView) view.findViewById(R.id.iv_02);
        iv_menu.setVisibility(View.VISIBLE);

        //头部view
        viewHeader = LayoutInflater.from(getActivity()).inflate(R.layout.home_listview_header, null);

        convenientBanner = (ConvenientBanner) viewHeader.findViewById(R.id.convenientBanner);
        convenientBanner.setPullDownView(pullDowmView);
        homepage_notice_vf = (ViewFlipper) viewHeader.findViewById(R.id.homepage_notice_vf);
        ll_hot_hotitem = (LinearLayout) viewHeader.findViewById(R.id.ll_01);
        tv_hot_title = (TextView) viewHeader.findViewById(R.id.tv_title);
        iv_hot_repayment_way = (ImageView) viewHeader.findViewById(R.id.iv_02);
        tv_hot_rate = (TextView) viewHeader.findViewById(R.id.tv_02);
        tv_hot_total_money = (TextView) viewHeader.findViewById(R.id.tv_03);
        tv_hot_invest_date = (TextView) viewHeader.findViewById(R.id.tv_04);
        tv_hot_statr_invest_moeny = (TextView) viewHeader.findViewById(R.id.tv_05);
        btn_hot_invest = (Button) viewHeader.findViewById(R.id.btn_invest);
        iv_product_type = (ImageView) viewHeader.findViewById(R.id.iv_01);

        listView.setHeaderDividersEnabled(true);
        listView.addHeaderView(viewHeader);

        mHomeAdaptor = new homeAdaptor(getActivity().getApplicationContext(), new homeAdaptor.homeAdaptorClickInterface() {
            @Override
            public void OnInvestClick(int position) {
                TurnToInvestActivity(homeInfoBean.otherproducts.get(position).prodcutId, homeInfoBean.otherproducts.get(position).loanTypeId);
            }
        });
        listView.setAdapter(mHomeAdaptor);

    }

    public void TurnToInvestActivity(String prodcutId, String loanTypeId) {

        Bundle bundle = new Bundle();
        bundle.putString(InvestBidActivity.SHOW_BACK_TXT, getResources().getString(R.string.nav_home));
        bundle.putString(InvestBidActivity.PRODUCT_ID, prodcutId);
        bundle.putString(InvestBidActivity.LOAN_TYPEID, loanTypeId);
        bundle.putInt(InvestBidActivity.INVEST_TURN_TO_MAIN_INDEX, MainFragmentEnum.HOME.getValue());
        bundle.putSerializable(JumpCenter.TO_ACTIVITY, InvestBidActivity.class);
        JumpCenter.JumpActivity(getActivity(), InvestBidActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);

    }

    /***
     * 填充视图
     *
     * @param homeInfoBean
     */
    private void FillHeaderView(HomeInfoBean homeInfoBean) {

        tv_hot_title.setText(homeInfoBean.hotrecommend.productName);
        tv_hot_rate.setText(homeInfoBean.hotrecommend.yearOfRate);
        btn_hot_invest.setText(homeInfoBean.hotrecommend.btntxt);
        tv_hot_total_money.setText(homeInfoBean.hotrecommend.investTatoalMoeny + Const.YUAN_STR);
        tv_hot_statr_invest_moeny.setText(homeInfoBean.hotrecommend.startMoeny + Const.YUAN_STR);
        tv_hot_invest_date.setText(homeInfoBean.hotrecommend.investDate + Utils.getInvestUnitNameByFlag(homeInfoBean.hotrecommend.unit));
        Utils.FillterReturnWay(iv_hot_repayment_way, homeInfoBean.hotrecommend.repaymentType);
        ImageWithGlideUtils.lodeFromUrl(homeInfoBean.hotrecommend.flagIconUrl, R.mipmap.product_type_default_ico, iv_product_type, getActivity());
        btn_hot_invest.setText(homeInfoBean.hotrecommend.btntxt);
        mHomeAdaptor.setData(homeInfoBean.otherproducts);

    }

    private List<HomeInfoBean.AdsBean> adsBeens;

    /***
     * 初始化广告条
     *
     * @param adsBeens
     */
    private void initBanner(List<HomeInfoBean.AdsBean> adsBeens) {

        //网络加载
        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, adsBeens)
                .setPageIndicator(new int[]{R.drawable.point_bg_normal, R.drawable.point_bg_enable})
                .startTurning(3000)
                .setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(int position) {
        if (!StrUtil.isEmpty(adsBeens.get(position).clickurl)) {
            Bundle bundle = new Bundle();
            bundle.putString(OtherActivity.BACK_STR, getResources().getString(R.string.nav_home));
            bundle.putString(OtherActivity.URL, adsBeens.get(position).clickurl);
            bundle.putSerializable(OtherActivity.TURN_TO_ACTIVITY_CLASS, MainActivity.class);
            JumpCenter.JumpActivity(getActivity(), OtherActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, false);

        }
    }

    public class NetworkImageHolderView implements Holder<HomeInfoBean.AdsBean> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, HomeInfoBean.AdsBean data) {
            ImageWithGlideUtils.lodeFromUrl(data.imageUrl, R.mipmap.default_banner_bg, imageView, context);
        }
    }

    /***
     * 滚动资讯
     */
    private void initRollNotice() {
        if (null != scheduledExecutorService) {
            scheduledExecutorService.shutdown();
        }
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (getActivity() != null) {
                            moveNext();
                        }
                    }
                });
            }
        }, 0, 4, TimeUnit.SECONDS);
    }

    private void moveNext() {
        setView(this.mCurrPos, this.mCurrPos + 1);
        this.homepage_notice_vf.setInAnimation(getActivity(), R.anim.in_bottomtop);
        this.homepage_notice_vf.setOutAnimation(getActivity(), R.anim.out_bottomtop);
        this.homepage_notice_vf.showNext();
    }

    private void setView(int curr, int next) {

        try {

            View noticeView = getActivity().getLayoutInflater().inflate(R.layout.notice_item,
                    null);
            TextView notice_tv = (TextView) noticeView.findViewById(R.id.notice_tv);
            if ((curr < next) && (next > (homeInfoBean.notice.size() - 1))) {
                next = 0;
            } else if ((curr > next) && (next < 0)) {
                next = homeInfoBean.notice.size() - 1;
            }
            notice_tv.setText(homeInfoBean.notice.get(next).content);
            notice_tv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (!StrUtil.isEmpty(homeInfoBean.notice.get(mCurrPos).clickurl)) {
                        Bundle bundle = new Bundle();
                        bundle.putString(OtherActivity.BACK_STR, getResources().getString(R.string.nav_home));
                        bundle.putString(OtherActivity.URL, homeInfoBean.notice.get(mCurrPos).clickurl);
                        bundle.putSerializable(OtherActivity.TURN_TO_ACTIVITY_CLASS, MainActivity.class);
                        JumpCenter.JumpActivity(getActivity(), OtherActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, false);

                    }
                }
            });
            if (homepage_notice_vf.getChildCount() > 1) {
                homepage_notice_vf.removeViewAt(0);
            }
            homepage_notice_vf.addView(noticeView, homepage_notice_vf.getChildCount());
            mCurrPos = next;
        } catch (Exception e) {

        }
    }

    private TextView tv_num;

    private void ShowPopUpWindow() {

        if (popupWindow == null) {
            // 一个自定义的布局，作为显示的内容
            View contentView = LayoutInflater.from(getActivity()).inflate(
                    R.layout.popwindow_home_menu, null);
            ll_recharge = (LinearLayout) contentView.findViewById(R.id.ll_01);
            ll_recharge.setOnClickListener(this);
            ll_withdraw = (LinearLayout) contentView.findViewById(R.id.ll_02);
            ll_withdraw.setOnClickListener(this);
            ll_infocenter = (LinearLayout) contentView.findViewById(R.id.ll_03);
            ll_infocenter.setOnClickListener(this);
            ll_helpercenter = (LinearLayout) contentView.findViewById(R.id.ll_04);
            ll_helpercenter.setOnClickListener(this);
            tv_num = (TextView) contentView.findViewById(R.id.tv_num);

            if (StrUtil.isEmpty(homeInfoBean.otherHomeInfo.unReadMsgNum) || homeInfoBean.otherHomeInfo.unReadMsgNum.equals("0")) {
                tv_num.setVisibility(View.GONE);
            } else {
                tv_num.setText(homeInfoBean.otherHomeInfo.unReadMsgNum);
                tv_num.setVisibility(View.VISIBLE);
            }

            popupWindow = new PopupWindow(contentView,
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setTouchable(true);
            popupWindow.setOutsideTouchable(true);
            // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
            // 我觉得这里是API的一个bug
            popupWindow.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.transparent_bg));
            popupWindow.showAsDropDown(iv_menu, 0, 0);
        } else {

            if (UserBiz.getInstance(getActivity()).CheckLoginState()) {
                addSubscription(RetrofitClient.GetUnReadMsgCount(null, getActivity(), new YCNetSubscriber<UserMsgBean>(getActivity()) {

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onYcNext(UserMsgBean model) {
                        if (!StrUtil.isEmpty(model.unReadMsgNum) && Integer.parseInt(model.unReadMsgNum) > 0) {
                            tv_num.setText(model.unReadMsgNum);
                        } else {
                            tv_num.setVisibility(View.GONE);
                        }
                    }
                }));
            }
            if (StrUtil.isEmpty(homeInfoBean.otherHomeInfo.unReadMsgNum) || homeInfoBean.otherHomeInfo.unReadMsgNum.equals("0")) {
                tv_num.setVisibility(View.GONE);
            } else {
                tv_num.setText(homeInfoBean.otherHomeInfo.unReadMsgNum);
                tv_num.setVisibility(View.VISIBLE);
            }
            popupWindow.showAsDropDown(iv_menu, 0, 0);
        }
    }

    private void DissmissDialog() {
        if (null != popupWindow) {
            popupWindow.dismiss();
        }
    }


    @Override
    public void onUserClick(View v) {
        super.onUserClick(v);
        switch (v.getId()) {
            //菜单按钮
            case R.id.iv_02:
                if (homeInfoBean != null) {
                    ShowPopUpWindow();
                }
                break;
            //充值
            case R.id.ll_01:
                doRechargePre();
                break;
            //提现
            case R.id.ll_02:
                doWithdraw();
                DissmissDialog();
                break;
            //消息中心
            case R.id.ll_03:
                goToInfomationCenter();
                break;
            //帮助中心
            case R.id.ll_04:
                Bundle bundle = new Bundle();
                bundle.putString(OtherActivity.BACK_STR, getResources().getString(R.string.nav_home));
                bundle.putString(OtherActivity.URL, homeInfoBean.otherHomeInfo.helperUrl);
                bundle.putSerializable(OtherActivity.TURN_TO_ACTIVITY_CLASS, MainActivity.class);
                JumpCenter.JumpActivity(getActivity(), OtherActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, false);
                DissmissDialog();
                break;
            //热门推荐投资
            case R.id.btn_invest:
                TurnToInvestActivity(homeInfoBean.hotrecommend.prodcutId, homeInfoBean.hotrecommend.loanTypeId);
                break;

        }
    }


    public void goToInfomationCenter() {
        Bundle bundle = new Bundle();
        bundle.putString(OtherActivity.BACK_STR, getResources().getString(R.string.nav_home));
        bundle.putString(OtherActivity.URL, homeInfoBean.otherHomeInfo.messageUrl);
        bundle.putSerializable(OtherActivity.TURN_TO_ACTIVITY_CLASS, MainActivity.class);
        bundle.putSerializable(JumpCenter.TO_ACTIVITY, OtherActivity.class);
        JumpCenter.JumpActivity(getActivity(), OtherActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
        DissmissDialog();
    }

    public void doRechargePre() {


        if (UserBiz.getInstance(getActivity()).CheckLoginState()) {

            addSubscription(RetrofitClient.GetRechargeInitalInfo(null, getActivity(), new YCNetSubscriber<RechargeInfoBean>(getActivity(), true) {

                @Override
                public void onYcNext(RechargeInfoBean model) {
                    if (model.rechargeState == RechargeStateEnum.FIRST_RECHARGE.getValue()) {
                        ycDialogUtils.showBindBankDialog(getString(R.string.bind_bankcrad_hint), new YCDialogUtils.MyTwoBtnclickLisener() {

                            @Override
                            public void onSecondBtnClick(View v) {

                                ycDialogUtils.DismissMyDialog();
                                Bundle bundle = new Bundle();
                                bundle.putString(AddBankActivity.BACK_TXT, getString(R.string.nav_home));
                                bundle.putSerializable(AddBankActivity.ADDBANK_RIGHT_TURN_TO_ACTIVITY_CLASS, RechargeActivity.class);
                                JumpCenter.JumpActivity(getActivity(), AddBankActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);

                            }

                            @Override
                            public void onFirstBtnClick(View v) {
                                ycDialogUtils.DismissMyDialog();
                            }

                        }, true);

                    }
                    else
                    {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(RechargeActivity.RECHARGEINFO_DATA, model);
                        bundle.putInt(RechargeActivity.RECHARGE_RIGHT_TURN_TO_MAIN_INDEX, MainFragmentEnum.HOME.getValue());
                        bundle.putString(RechargeActivity.SHOW_BACK_TXT, getResources().getString(R.string.nav_home));
                        JumpCenter.JumpActivity(getActivity(), RechargeActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);

                    }

                }

                @Override
                public void onYCError(APIException e) {
                    if (e.code == APIService.NO_AUTHENTICATION_CODE) {

                        ycDialogUtils.showAuthenticationDialog(e.getMessage(), new YCDialogUtils.MyTwoBtnclickLisener() {

                            @Override
                            public void onSecondBtnClick(View v) {

                                ycDialogUtils.DismissMyDialog();
                                Bundle bundle = new Bundle();
                                bundle.putString(RealNameAuthenticationActivity.SHOW_BACK_TXT, getResources().getString(R.string.nav_home));
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

        } else {
            Bundle bundle = new Bundle();
            bundle.putInt(MainActivity.WITCH_TO_DO, MainActivity.RECHANGER_GOON);
            JumpCenter.JumpActivity(getActivity(), LoginActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, false);
        }
        DissmissDialog();
    }

    /***
     * 发起提现
     */
    public void doWithdraw() {
        if (UserBiz.getInstance(getActivity()).CheckLoginState()) {
            DissmissDialog();
            addSubscription(RetrofitClient.GetWithdrawInfo(null, getActivity(), new YCNetSubscriber<WithDrawInfoBean>(getActivity(), true) {

                @Override
                public void onYcNext(WithDrawInfoBean model) {

                    //获取提现初始化信息成功
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(WithDrawActivity.WITHDRAW_INFO, model);
                    bundle.putInt(WithDrawActivity.WITHDRAW_RIGHT_TURN_TO_MAIN_INDEX, MainFragmentEnum.HOME.getValue());
                    bundle.putString(WithDrawActivity.SHOW_BACK_TXT, getResources().getString(R.string.nav_home));
                    JumpCenter.JumpActivity(getActivity(), WithDrawActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);

                }
            }));

        } else {
            Bundle bundle = new Bundle();
            bundle.putInt(MainActivity.WITCH_TO_DO, MainActivity.WITHDRAW_GOON);
            JumpCenter.JumpActivity(getActivity(), LoginActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, false);
        }

    }

    public void TurnToProductDetailsActivity(String prodcutId, String loanTypeId) {

        Bundle bundle = new Bundle();
        bundle.putString(ProductDetailsActivity.PRODUCT_ID, prodcutId);
        bundle.putString(ProductDetailsActivity.LOAN_TYPEID, loanTypeId);
        bundle.putSerializable(JumpCenter.TO_ACTIVITY, ProductDetailsActivity.class);
        JumpCenter.JumpActivity(getActivity(), ProductDetailsActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TurnToProductDetailsActivity(homeInfoBean.otherproducts.get(position - 1).prodcutId, homeInfoBean.hotrecommend.loanTypeId);
    }

    @Override
    public void onImageClick(String url) {
        if (!StrUtil.isEmpty(url)) {
            Bundle bundle = new Bundle();
            bundle.putString(OtherActivity.BACK_STR, getResources().getString(R.string.nav_home));
            bundle.putString(OtherActivity.URL, url);
            bundle.putSerializable(OtherActivity.TURN_TO_ACTIVITY_CLASS, MainActivity.class);
            JumpCenter.JumpActivity(getActivity(), OtherActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, false);

        }
    }

    private boolean isHidden;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isHidden = hidden;
        if (hidden) {
            //不在最前端界面显示
            //相当于Fragment的onPause
            StatService.onPause(this);
        } else {//
            // 重新显示到最前端中
            //相当于Fragment的onResume
            StatService.onResume(this);
        }
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

    @Override
    public void onPause() {
        super.onPause();
        if (!isHidden) {
            StatService.onPause(this);
        }
        isFrist = true;
    }

}
