package com.cfbb.android.features.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseFragment;
import com.cfbb.android.commom.utils.base.AppUtils;
import com.cfbb.android.commom.utils.base.TimeUtils;
import com.cfbb.android.commom.utils.others.L;
import com.cfbb.android.db.user.UserBiz;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.ProductProjectInfoBean;
import com.cfbb.android.widget.YCLoadingBg;

/**
 * Created by MrChang45 on 2016/3/24.
 * 项目详情 项目信息
 */
public class ProductInfoFragment extends BaseFragment {

    private static final String PRODUCT_ID = "product_id";
    private String product_id;
    private boolean isInit = true; // 是否可以开始加载数据

    public static ProductInfoFragment newInstance(String productId) {
        ProductInfoFragment fragment = new ProductInfoFragment();
        Bundle args = new Bundle();
        args.putString(PRODUCT_ID, productId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        product_id = getArguments().getString(PRODUCT_ID);
    }


    @Override
    public int initContentView() {

        return R.layout.fragment_projectinfo;
    }


    private WebView mWebView;
    private YCLoadingBg ycLoadingBg;

    @Override
    public void setUpViews(View view) {

        mWebView = (WebView) view.findViewById(R.id.webView);
        ycLoadingBg = (YCLoadingBg) view.findViewById(R.id.ycLoadingBg);
        ycLoadingBg.setContentView(mWebView);
        ConfigWebView();
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
        //StatService.onResume(this);
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
            //TestResultUtils.getSussefulResult27()
            addSubscription(RetrofitClient.getProductProjectInfo(null, product_id, getActivity(), new YCNetSubscriber<ProductProjectInfoBean>(getActivity()) {

                @Override
                public void onYcNext(ProductProjectInfoBean model) {
                    productProjectInfoBean = model;
                    if (productProjectInfoBean.url.contains("?")) {
                        mWebView.loadUrl(productProjectInfoBean.url + "&" + params);
                    } else {
                        mWebView.loadUrl(productProjectInfoBean.url + "?" + params);
                    }
                    ycLoadingBg.dissmiss();
                }

                @Override
                public void onYCError(APIException e) {
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


    private ProductProjectInfoBean productProjectInfoBean;


    private String params = "";

    /***
     * 配置webview
     */
    private void ConfigWebView() {


        // 设置触摸焦点作用
        mWebView.setWebViewClient(new webViewClient());
        mWebView.requestFocus();
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // mWebView.getSettings().setAllowFileAccess(true);// 设置可访问文件
        mWebView.setAlwaysDrawnWithCacheEnabled(false);
        // 屏幕适配
        mWebView.getSettings().setUseWideViewPort(true);
        // mWebView.getSettings().setLoadWithOverviewMode(true);
        // mWebView.getSettings().setBuiltInZoomControls(true);
        // 启用javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        // 取消滚动条
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        params += "app_sys=android&version_code=" + AppUtils.getAppInfo(getActivity()).getVersionCode() + "&user_id=" + UserBiz.getInstance(getActivity()).GetUserId() + "&random=" + TimeUtils.getCurTimeMills();

    }

    class webViewClient extends WebViewClient {

        //重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            L.e(url);
            if(url.contains("?"))
            {
                view.loadUrl(url+"&"+params); // 在当前的webview中跳转到新的url
            }
            else
            {
                view.loadUrl(url+"?"+params); // 在当前的webview中跳转到新的url
            }
            //如果不需要其他对点击链接事件的处理返回true，否则返回false
            return true;
        }
    }
}
