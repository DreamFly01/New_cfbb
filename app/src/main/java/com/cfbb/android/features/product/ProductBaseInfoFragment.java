package com.cfbb.android.features.product;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseFragment;
import com.cfbb.android.commom.config.Const;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.commom.utils.others.Utils;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.ProductBaseInfoBean;
import com.cfbb.android.widget.ColorArcProgressBar;
import com.cfbb.android.widget.YCLoadingBg;

import java.math.BigDecimal;

/***
 * 项目基本信息
 */
public class ProductBaseInfoFragment extends BaseFragment {


    private static final String PRODUCT_ID = "product_id";
    private static final String LOAN_TYPE_ID = "loan_type_id";

    private String product_id;
    private String loan_type_id;
    private boolean isInit = true; // 是否可以开始加载数据
    // private MyInvestProgress myInvestProgress;

    private TextView tv_productName;
    private TextView tv_remiansMoney;

    private TextView tv_yearOfDate;
    private TextView tv_investDate;
    private TextView tv_loanTotalMoney;
    private TextView tv_returnMoneyWay;
    private TextView tv_startMoeny;
    private TextView tv_investNum;
    private TextView tv_remainDate;
    private TextView tv_prograss;
    private CountDownTimer countDownTimer;
    private YCLoadingBg ycLoadingBg;
    private ProductBaseInfoBean productBase;


    public static ProductBaseInfoFragment newInstance(String productId, String loan_type_id) {
        ProductBaseInfoFragment fragment = new ProductBaseInfoFragment();
        Bundle args = new Bundle();
        args.putString(PRODUCT_ID, productId);
        args.putString(LOAN_TYPE_ID, loan_type_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        product_id = getArguments().getString(PRODUCT_ID);
        loan_type_id = getArguments().getString(LOAN_TYPE_ID);
    }

    @Override
    public int initContentView() {

        return R.layout.fragment_product_base_info;
    }

    private ColorArcProgressBar bar3;

    @Override
    public void setUpViews(View view) {
        ycLoadingBg = (YCLoadingBg) view.findViewById(R.id.ycLoadingBg);
        tv_prograss = (TextView) view.findViewById(R.id.tv_11);
        tv_productName = (TextView) view.findViewById(R.id.tv_title);
        tv_remiansMoney = (TextView) view.findViewById(R.id.tv_02);
        tv_yearOfDate = (TextView) view.findViewById(R.id.tv_03);
        tv_investDate = (TextView) view.findViewById(R.id.tv_04);
        tv_loanTotalMoney = (TextView) view.findViewById(R.id.tv_05);

        tv_returnMoneyWay = (TextView) view.findViewById(R.id.tv_07);
        tv_startMoeny = (TextView) view.findViewById(R.id.tv_08);
        tv_investNum = (TextView) view.findViewById(R.id.tv_back);
        tv_remainDate = (TextView) view.findViewById(R.id.tv_10);

        // myInvestProgress = (MyInvestProgress) view.findViewById(R.id.myInvestPrograss);
        // myInvestProgress.setTitle(getResources().getString(R.string.invest_prograss_str));
        //  myInvestProgress.setMax(100);

        bar3 = (ColorArcProgressBar) view.findViewById(R.id.bar1);

    }

    @Override
    public void setUpLisener() {

    }

    private boolean isHidden;

    @Override
    public void onPause() {
        super.onPause();
        if (!isHidden) {
            StatService.onPause(this);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        // StatService.onResume(this);
        // 判断当前fragment是否显示
        if (getUserVisibleHint()) {
            showData();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isHidden = !isVisibleToUser;
        // 每次切换fragment时调用的方法
        if (isVisibleToUser) {
            showData();
            StatService.onResume(this);
        } else {
            if (!isInit) {
                StatService.onPause(this);
            }
        }
    }

    /**
     * 初始化数据
     */
    private void showData() {
        if (isInit) {
            isInit = false;//加载数据完成
            // 加载各种数据
            //TestResultUtils.getSussefulResult24()
            addSubscription(RetrofitClient.geProductBaseInfo(null, product_id, loan_type_id, getActivity(), new YCNetSubscriber<ProductBaseInfoBean>(getActivity()) {

                @Override
                public void onYcNext(ProductBaseInfoBean model) {
                    productBase = model;
                    FillView();
                    ycLoadingBg.dissmiss();
                }

                @Override
                public void onYCError(APIException erro) {
                    ycLoadingBg.showErroBg(new YCLoadingBg.YCErroLisener() {
                        @Override
                        public void onTryAgainClick() {
                            isInit = true;
                            showData();
                        }
                    });
                }
            }));
        }
    }

    private void FillView() {
        tv_productName.setText(productBase.productName);
        tv_remiansMoney.setText(productBase.remiansMoeny);
        tv_yearOfDate.setText(productBase.yearOfRate + Const.PER);
        tv_investDate.setText(productBase.investDate + Utils.getInvestUnitNameByFlag(productBase.unit));
        tv_loanTotalMoney.setText(productBase.loanTotalMoney);
        tv_returnMoneyWay.setText(Utils.getReturnWayNameByType(productBase.repaymentType));
        tv_startMoeny.setText(productBase.startMoney + Const.YUAN_STR);
        tv_investNum.setText(productBase.investNum);


        countDownTimer = new CountDownTimer((long) Double.parseDouble(productBase.remiansDay), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_remainDate.setText(StrUtil.getTimeDiff(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                tv_remainDate.setText(getResources().getString(R.string.is_over));
            }
        };
        countDownTimer.start();
        ValueAnimator animator = ValueAnimator.ofInt(0, ((int) (Float.parseFloat(productBase.progress)) + 1));
        animator.setDuration(1500).start();
        // animator.setInterpolator(new AnticipateOvershootInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int prograss = (int) animation.getAnimatedValue();
                if (prograss < 0) {
                    prograss = 0;
                }
                if (prograss > 100) {
                    prograss = 100;
                }
                if (prograss < Float.parseFloat(productBase.progress)) {
                    // myInvestProgress.setProgress(prograss);
                } else {
                    // myInvestProgress.setProgress(prograss);
                }
                // L.e("prograss=" + prograss);
            }
        });

        bar3.setCurrentValues(new BigDecimal(productBase.progress).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());

        ValueAnimator animator2 = ValueAnimator.ofFloat(0, Float.parseFloat(productBase.progress));
        animator2.setDuration(1500).start();
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float prograss = (float) animation.getAnimatedValue();
                if (prograss < 0) {
                    prograss = 0;
                } else {
                    BigDecimal b = new BigDecimal(prograss);
                    prograss = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                }
                if (prograss < Float.parseFloat(productBase.progress)) {
                    tv_prograss.setText(prograss + "");
                } else {
                    tv_prograss.setText(productBase.progress + "");
                }

            }
        });

        ProductDetailsActivity productDetailsActivity = (ProductDetailsActivity) getActivity();
        if (null != productDetailsActivity) {
            productDetailsActivity.setBtnTxt(productBase.btntxt);
            productDetailsActivity.setBtnEnable(productBase.canInvest == 1 ? true : false);
        }
    }

}
