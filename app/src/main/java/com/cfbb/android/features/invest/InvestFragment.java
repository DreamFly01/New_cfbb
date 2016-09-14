package com.cfbb.android.features.invest;


import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseFragment;
import com.cfbb.android.commom.config.Const;
import com.cfbb.android.commom.utils.others.SPUtils;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.ProductTypeBean;
import com.cfbb.android.widget.viewpagerindicator.YCPagerIndicator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/***
 * 投资列表
 */
public class InvestFragment extends BaseFragment {

    private TextView tv_title;
    private YCPagerIndicator ycPagerIndicator;
    private ViewPager viewPager;
    private List<ProductTypeBean> productTypeBeanList;
    private InvestFragmentAdaptor investFragmentAdaptor;

    public InvestFragment() {
    }


    @Override
    public int initContentView() {
        return R.layout.fragment_invest;
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


    @Override
    public void setUpLisener() {

    }


    @Override
    public void setUpViews(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        ycPagerIndicator = (YCPagerIndicator) view.findViewById(R.id.horizontalScrollView);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText(getResources().getString(R.string.nav_invest));
    }

    private List<String> titleList = new ArrayList<>();
    private List<BaseFragment> fragments = new ArrayList<>();// view数组

    @Override
    public void getDataOnActivityCreated() {
        String str_ProductTypes = (String) SPUtils.get(getActivity(), Const.PRODUCT_STR, "");
        if (!StrUtil.isEmpty(str_ProductTypes)) {
            Gson gson = new Gson();
            productTypeBeanList = gson.fromJson(str_ProductTypes, new TypeToken<List<ProductTypeBean>>() {
            }.getType());
            CreateContent();
        } else {
            addSubscription(RetrofitClient.getProductTypesRequest(null, getActivity(), new YCNetSubscriber<List<ProductTypeBean>>(getActivity(), true) {

                @Override
                public void onYCError(APIException erro) {

                }

                @Override
                public void onYcNext(List<ProductTypeBean> model) {
                    productTypeBeanList = model;
                    CreateContent();
                }

            }));
        }
    }

    private void CreateContent() {
        titleList.clear();
        InvestContentFragment productBaseInfoFragment;
        for (ProductTypeBean productTypeBean :
                productTypeBeanList) {
            titleList.add(productTypeBean.productTypeName);

            productBaseInfoFragment = InvestContentFragment.newInstance(productTypeBean.prodcutTypeId, productTypeBean.loanTypeId + "");
            fragments.add(productBaseInfoFragment);

        }
        ycPagerIndicator.setViewPager(viewPager);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        ycPagerIndicator.setTitles(dm.widthPixels, titleList, true);
        investFragmentAdaptor = new InvestFragmentAdaptor(getChildFragmentManager(), fragments, titleList);
        viewPager.setAdapter(investFragmentAdaptor);
        viewPager.setOffscreenPageLimit(8);
    }


}
