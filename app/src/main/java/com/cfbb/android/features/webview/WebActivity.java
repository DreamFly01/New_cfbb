package com.cfbb.android.features.webview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.utils.base.AppUtils;
import com.cfbb.android.commom.utils.base.TimeUtils;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.db.user.UserBiz;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.ShareInfoBean;

public class WebActivity extends BaseActivity {
    private WebView mWebView;
    private String url;
    private String textIndex;
    private String params="";

    private TextView tvTitle;
    private TextView back;
    private TextView menu;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_web);

    }

    @Override
    public void getDataOnCreate() {
        super.getDataOnCreate();
        Intent intent = getIntent();
        textIndex = intent.getStringExtra("text_index");
        url = intent.getStringExtra("url");
        params +="app_sys=android&version_code=" + AppUtils.getAppInfo(this).getVersionCode() + "&user_id=" + UserBiz.getInstance(this).GetUserId()+"&random="+ TimeUtils.getCurTimeMills();




        System.out.println(url);
//        getShareData(textIndex);
        ConfigWebView();
        if(url.contains("?"))
        {
            mWebView.loadUrl(url+"&"+params); // 在当前的webview中跳转到新的url
        }
        else
        {
            mWebView.loadUrl(url+"?"+params); // 在当前的webview中跳转到新的url
        }
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public void setUpViews() {
        mWebView = (WebView) findViewById(R.id.webView);
        tvTitle = (TextView)findViewById(R.id.tv_title);
        back = (TextView)findViewById(R.id.tv_back);
        menu = (TextView)findViewById(R.id.tv_menu);

        back.setVisibility(View.VISIBLE);
        back.setText("首页");
        menu.setVisibility(View.VISIBLE);
        menu.setText("分享");
    }

    @Override
    public void setUpLisener() {
        back.setOnClickListener(this);
        menu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.tv_back:
                this.finish();
                break;
            case R.id.tv_menu:
                break;
        }
    }

    private String title;
    private String descr;
    private String thumImage;
    private String thumUrl;
    private void getShareData(String textIndex) {
        addSubscription(RetrofitClient.GetShareInfo(null, textIndex, this, new YCNetSubscriber<ShareInfoBean>(this) {
            @Override
            public void onYcNext(ShareInfoBean model) {
                title = model.title;
                descr = model.descr;
                thumImage = model.thumImage;
                thumUrl = model.thumUrl;
            }
        }));
    }
    private void ConfigWebView() {

        // 设置title
        // 设置setWebChromeClient对象
//        mWebView.setWebChromeClient(wvcc);

        // 设置触摸焦点作用
        mWebView.requestFocus();
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.getSettings().setAllowFileAccess(true);// 设置可访问文件
        mWebView.setAlwaysDrawnWithCacheEnabled(false);
        // 屏幕适配
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        // mWebView.getSettings().setBuiltInZoomControls(true);
        // 启用javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        // 取消滚动条
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.setWebViewClient(new WebViewClient() {

            // 打开链接前回调此放方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if(url.contains("?"))
                {
                    view.loadUrl(url+"&"+params); // 在当前的webview中跳转到新的url
                }
                else
                {
                    view.loadUrl(url+"?"+params); // 在当前的webview中跳转到新的url
                }

                return true;
            }

        });
    }
}
