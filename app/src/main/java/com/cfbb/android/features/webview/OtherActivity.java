package com.cfbb.android.features.webview;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.commom.utils.base.AppUtils;
import com.cfbb.android.commom.utils.base.TimeUtils;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.db.user.UserBiz;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.ShareInfoBean;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;

public class OtherActivity extends BaseActivity implements View.OnClickListener {

    public static final String URL = "url";
    public static final String BACK_STR = "back_str";
    public static final String TURN_TO_ACTIVITY_CLASS = "turn_to_activity_name";
    private String textIndex;

    private WebView mWebView;
    private TextView tv_back;
    private TextView tv_title;
    private ProgressBar progressBar;

    private String back_str;
    private String url;
    private Class turnToClass;
    private TextView tv_close;

    private LinearLayout rootLayout;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_other);
        setBackEnable(false);
    }

    @Override
    public void setUpViews() {
        rootLayout = (LinearLayout) findViewById(R.id.ll_02);
        tv_close = (TextView) findViewById(R.id.tv_junp);
        mWebView = (WebView) findViewById(R.id.webView);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        progressBar = (ProgressBar) findViewById(R.id.pb_progressbar);
        tv_title.setText("");




    }

    @Override
    public void setUpLisener() {
        tv_close.setOnClickListener(this);
        tv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    if (null != turnToClass) {
                        JumpCenter.JumpActivity(this,turnToClass,null,null,JumpCenter.NORMALL_REQUEST,JumpCenter.INVAILD_FLAG,true,false);
                    }else {
                        finish();
                    }
                }
                break;
            case R.id.tv_junp:
                if(textIndex!=null){
                    showShortToast(textIndex);
                    showShare(title,descr,thumImage,thumUrl);
                    break;
                }
                if (null != turnToClass) {
                    JumpCenter.JumpActivity(this,turnToClass,null,null,JumpCenter.NORMALL_REQUEST,JumpCenter.INVAILD_FLAG,true,false);
                }else {
                    finish();
                }
                break;
        }
    }

    private String params="";
    @Override
    public void getDataOnCreate() {

        mIntent = getIntent();
        if (null != mIntent) {
            back_str = mIntent.getStringExtra(BACK_STR);
            url = mIntent.getStringExtra(URL);
            textIndex = mIntent.getStringExtra("text_index");
            turnToClass = (Class) mIntent.getSerializableExtra(TURN_TO_ACTIVITY_CLASS);

        }
        if (StrUtil.isEmpty(back_str)) {
            tv_back.setText(R.string.back_str);
        } else {
            tv_back.setText(back_str);
        }
        getShareData(textIndex);
        if (textIndex!=null){
            mWebView.getSettings().setJavaScriptEnabled(true);
            tv_close.setText("分享");
            tv_close.setVisibility(View.VISIBLE);
        }else {
        ConfigWebView();
        }
        tv_back.setVisibility(View.VISIBLE);
        params +="app_sys=android&version_code=" + AppUtils.getAppInfo(this).getVersionCode() + "&user_id=" + UserBiz.getInstance(this).GetUserId()+"&random="+ TimeUtils.getCurTimeMills();

        if(url.contains("?"))
        {
            mWebView.loadUrl(url+"&"+params); // 在当前的webview中跳转到新的url
        }
        else
        {
            mWebView.loadUrl(url+"?"+params); // 在当前的webview中跳转到新的url
        }
        // mWebView.loadUrl(url);
    }

    private List<String> titles = new ArrayList<>();

    /**
     * 配置webview
     */
    private void ConfigWebView() {

        // 设置title
        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                tv_title.setText(StrUtil.cuttOffStr(title, 6, "..."));
                String tem = "";
                boolean isContans = false;
                for (int i = 0; i < titles.size(); i++) {
                    tem = titles.get(i);
                    if (tem.equals(title)) {
                        titles.remove(i);
                        isContans = true;
                        break;
                    }
                }
                if (!isContans) {
                    titles.add(title);
                }
                tv_close.setVisibility(View.VISIBLE);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress > 90) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }

        };
        // 设置setWebChromeClient对象
        mWebView.setWebChromeClient(wvcc);

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
                if(textIndex!=null){
                if(!textIndex.equals("")){
                    return  false;
                }
                }
                return true;
            }

        });
    }

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
    protected void onDestroy() {
        super.onDestroy();
        rootLayout.removeView(mWebView);
        mWebView.destroy();
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
    private void showShare(String title, String descr, String thumImage, String thumUrl) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl(thumUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(descr);
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(thumImage);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(descr);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(descr);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("中国财富");
//        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(thumUrl);

// 启动分享GUI
        oks.show(this);
    }

}
