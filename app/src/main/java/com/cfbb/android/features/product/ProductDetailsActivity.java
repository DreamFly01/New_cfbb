package com.cfbb.android.features.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.baseview.BaseFragment;
import com.cfbb.android.commom.state.ProductTypeEnum;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.features.invest.InvestBidActivity;
import com.cfbb.android.widget.viewpagerindicator.YCPagerIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/***
 * 标的展示详情页
 * craete by MrChang45 on 2016-3-17
 */
public class ProductDetailsActivity extends BaseActivity {

    //标的ID
    public static final String PRODUCT_ID = "product_id";
    //标的类型
    public static final String LOAN_TYPEID = "loan_typeid";

    private ViewPager viewpager;
    //private TabPageIndicator pagerTabStrip;
    private List<BaseFragment> fragments = new ArrayList<>();// view数组
    private List<String> titleList = new ArrayList<>();
    private ProductDetailsFragmentAdaptor productDetailsFragmentAdaptor;
    private TextView tv_back;
    private Button btn_invest;
    private String prodcutId;
    private String loan_typeId;
    private Intent intent;
    private TextView tv_title;
    private YCPagerIndicator horizontalScrollView;

    private Bundle bundle;
    private String flag;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_product_details);
        intent = getIntent();
        if (null != intent) {
            bundle = intent.getExtras();
            prodcutId = bundle.getString(PRODUCT_ID);
            loan_typeId = bundle.getString(LOAN_TYPEID);

            System.out.println("详情页面得到的参数------prodcutId------" + prodcutId + "------loanTypeId------" + loan_typeId);
        }
        if (loan_typeId.equals(ProductTypeEnum.DEBTS.getValue())) {
            //投资服务计划
            titleList = Arrays.asList(getResources().getStringArray(R.array.product2_tab_names));
            ProductBaseInfoFragment productBaseInfoFragment = ProductBaseInfoFragment.newInstance(prodcutId, loan_typeId);
            fragments.add(productBaseInfoFragment);
            PlanExplainFragment planExpainFragment = PlanExplainFragment.newInstance(prodcutId);
            fragments.add(planExpainFragment);
            PlanPrograssFragment planPrograssFragment = new PlanPrograssFragment();
            fragments.add(planPrograssFragment);
            BidRecordFragment bidFragment = BidRecordFragment.newInstance(prodcutId);
            fragments.add(bidFragment);
            PlanFlowFragment planFlowFragmnet = new PlanFlowFragment();
            fragments.add(planFlowFragmnet);


        } else {
            //普通标的
            titleList = Arrays.asList(getResources().getStringArray(R.array.product_tab_names));  //标题列表数组
            ProductBaseInfoFragment productBaseInfoFragment = ProductBaseInfoFragment.newInstance(prodcutId, loan_typeId);
            fragments.add(productBaseInfoFragment);
            ProductInfoFragment productInfoFragment = ProductInfoFragment.newInstance(prodcutId);
            fragments.add(productInfoFragment);
            VertifyInfoFragment vertifyFragment = VertifyInfoFragment.newInstance(prodcutId);
            fragments.add(vertifyFragment);
            BidRecordFragment bidFragment = BidRecordFragment.newInstance(prodcutId);
            fragments.add(bidFragment);
        }
        productDetailsFragmentAdaptor = new ProductDetailsFragmentAdaptor(getSupportFragmentManager(), fragments, titleList);

    }

    public void setUpViews() {
        horizontalScrollView = (YCPagerIndicator) findViewById(R.id.horizontalScrollView);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getResources().getString(R.string.product_details_str));
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setVisibility(View.VISIBLE);
        btn_invest = (Button) findViewById(R.id.btn_invest);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setAdapter(productDetailsFragmentAdaptor);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        horizontalScrollView.setViewPager(viewpager);
        horizontalScrollView.setTitles(dm.widthPixels, titleList, false);
        // pagerTabStrip = (TabPageIndicator) findViewById(R.id.titlePageIndicator);
        //hold state
        horizontalScrollView.setFragments(fragments);
        viewpager.setOffscreenPageLimit(8);
        // btn_invest.setVisibility(View.VISIBLE);
            flag = intent.getStringExtra("flag");

    }

    public void setUpLisener() {
        tv_back.setOnClickListener(this);
        btn_invest.setOnClickListener(this);
    }


    @Override
    public void onUserClick(View v) {
        super.onUserClick(v);
        switch (v.getId()) {
            //回退
            case R.id.iv_01:
            case R.id.tv_back:
                finish();
                break;
            //投资
            case R.id.btn_invest:
                if (flag!=null) {
                    btn_invest.setEnabled(false);
                    System.out.println("flag--------------------"+flag);
                    return;
                }
                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putSerializable(InvestBidActivity.INVEST_TURN_TO_ACTIVITY_CLASS, ProductDetailsActivity.class);
                bundle.putString(InvestBidActivity.SHOW_BACK_TXT, getResources().getString(R.string.product_details_str));
                bundle.putString(InvestBidActivity.PRODUCT_ID, prodcutId);
                bundle.putString(InvestBidActivity.LOAN_TYPEID, loan_typeId);
                JumpCenter.JumpActivity(this, InvestBidActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);

                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewpager = null;
        fragments = null;
    }

    public void setBtnEnable(boolean enable) {
        if (null != btn_invest) {
            btn_invest.setEnabled(enable);
            btn_invest.setVisibility(View.VISIBLE);
        }
    }


    public void setBtnTxt(String txt) {
        if (null != btn_invest) {
            btn_invest.setText(txt);
        }
    }

}
