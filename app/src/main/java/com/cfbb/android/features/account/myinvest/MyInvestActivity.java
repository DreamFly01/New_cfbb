package com.cfbb.android.features.account.myinvest;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.baseview.BaseFragment;
import com.cfbb.android.commom.state.MyInvestStateEnum;
import com.cfbb.android.widget.viewpagerindicator.YCPagerIndicator;

import java.util.ArrayList;
import java.util.List;

/***
 * 我的投资
 */
public class MyInvestActivity extends BaseActivity  {

    private TextView tv_title;
    private TextView tv_back;
    private YCPagerIndicator ycPagerIndicator;
    private ViewPager viewPager;
    private MyInvestFragmentAdaptor myInvestFragmentAdaptor;
    private List<String> titleList = new ArrayList<>();
    private List<BaseFragment> fragments = new ArrayList<>();// view数组

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

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_invest);

    }

    @Override
    public void setUpViews() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ycPagerIndicator = (YCPagerIndicator) findViewById(R.id.horizontalScrollView);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setText(getResources().getString(R.string.nav_account));
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(R.string.my_invest_str);
        tv_back.setVisibility(View.VISIBLE);
        CreateContent();
    }

    @Override
    public void setUpLisener() {
        tv_back.setOnClickListener(this);
    }

    @Override
    public void onUserClick(View v) {
        super.onUserClick(v);
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
        }
    }


    private void CreateContent() {

        //后期优化策略:改为从服务器获取,做成动态
        titleList.clear();
        titleList.add(MyInvestStateEnum.HOLDING.getName());
        titleList.add(MyInvestStateEnum.INVESTING.getName());
        titleList.add(MyInvestStateEnum.COMPLETE.getName());

        MyInvestFragment productBaseInfoFragment;

        productBaseInfoFragment = MyInvestFragment.newInstance(MyInvestStateEnum.HOLDING.getValue() + "");
        fragments.add(productBaseInfoFragment);

        productBaseInfoFragment = MyInvestFragment.newInstance(MyInvestStateEnum.INVESTING.getValue() + "");
        fragments.add(productBaseInfoFragment);

        productBaseInfoFragment = MyInvestFragment.newInstance(MyInvestStateEnum.COMPLETE.getValue() + "");
        fragments.add(productBaseInfoFragment);

        ycPagerIndicator.setViewPager(viewPager);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        ycPagerIndicator.setTitles(dm.widthPixels, titleList, true);

        myInvestFragmentAdaptor = new MyInvestFragmentAdaptor(getSupportFragmentManager(), fragments, titleList);
        viewPager.setAdapter(myInvestFragmentAdaptor);
        viewPager.setOffscreenPageLimit(8);
    }
}
