package com.cfbb.android.features.account.releaseLoan;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.utils.base.AppUtils;
import com.cfbb.android.commom.utils.base.TimeUtils;
import com.cfbb.android.commom.utils.others.L;
import com.cfbb.android.db.user.UserBiz;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.LoanUrlBean;
import com.cfbb.android.widget.YCLoadingBg;

public class LoanWebActivity extends BaseActivity {
    private WebView mWebView;
    private YCLoadingBg ycLoadingBg;
    private LoanUrlBean loanUrlBean;
    private String params = "";

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_loan_web);

    }

    @Override
    public void getDataOnCreate() {
//        addSubscription(RetrofitClient.GetLoanUrl(null, this, new YCNetSubscriber<LoanUrlBean>(this) {
//            @Override
//            public void onYcNext(LoanUrlBean model) {
//                loanUrlBean = model;
//                if (loanUrlBean.loanUrl.contains("?")) {
//                    mWebView.loadUrl(loanUrlBean.loanUrl + "&" + params);
//                } else {
//                    mWebView.loadUrl(loanUrlBean.loanUrl + "?" + params);
//                }
//                ycLoadingBg.dissmiss();
//            }
//        }));
    }

    @Override
    public void setUpViews() {
        mWebView = (WebView) findViewById(R.id.webView);
        ycLoadingBg = (YCLoadingBg) findViewById(R.id.ycLoadingBg);
        ConfigWebView();
    }

    @Override
    public void setUpLisener() {

    }

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

        params += "app_sys=android&version_code=" + AppUtils.getAppInfo(this).getVersionCode() + "&user_id=" + UserBiz.getInstance(this).GetUserId() + "&random=" + TimeUtils.getCurTimeMills();

    }

    class webViewClient extends WebViewClient {

        //重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            L.e(url);
            if (url.contains("?")) {
                view.loadUrl(url + "&" + params); // 在当前的webview中跳转到新的url
            } else {
                view.loadUrl(url + "?" + params); // 在当前的webview中跳转到新的url
            }
            //如果不需要其他对点击链接事件的处理返回true，否则返回false
            return true;
        }
    }
}
