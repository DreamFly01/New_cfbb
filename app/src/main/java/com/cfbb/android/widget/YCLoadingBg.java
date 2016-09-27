package com.cfbb.android.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cfbb.android.R;

/**
 * Created by MrChang45 on 2016/3/31.
 * 加载动画
 */
public class YCLoadingBg extends LinearLayout implements View.OnClickListener {


    public YCLoadingBg(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialView(context);

    }


    private ImageView iv_loading;
    private LinearLayout ll_erro_bg;
    private LinearLayout ll_loading_bg;
    private Button btn_cs;
    private LinearLayout ll_view_bg;

    @Override
    public void onClick(View v) {
        iv_loading.setVisibility(View.VISIBLE);
        ll_loading_bg.setVisibility(View.VISIBLE);
        ll_erro_bg.setVisibility(View.GONE);
        if (null != ycErro) {
            ycErro.onTryAgainClick();
        }
    }

    private YCErroLisener ycErro;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /***
     * 单击重试
     */
    public interface YCErroLisener {
        void onTryAgainClick();
    }

    private void initialView(Context context) {
        View.inflate(context, R.layout.custom_progress, this);
        setVisibility(View.VISIBLE);
        ll_loading_bg = (LinearLayout) findViewById(R.id.ll_loading_bg);
        ll_view_bg = (LinearLayout) findViewById(R.id.ll_02);
        ll_view_bg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //阻止事件向下传递
            }
        });
        iv_loading = (ImageView) findViewById(R.id.spinnerImageView);
        ll_erro_bg = (LinearLayout) findViewById(R.id.ll_01);
        btn_cs = (Button) findViewById(R.id.btn_try_again);
        spinner = (AnimationDrawable) iv_loading.getBackground();
        assert spinner != null;
       // 默认一加载该布局就开始执行 加载动画
        spinner.start();
    }

    private AnimationDrawable spinner;

    public void showErroBg(YCErroLisener ycerro) {
        this.ycErro = ycerro;
        setVisibility(View.VISIBLE);
        iv_loading.setVisibility(View.GONE);
        ll_erro_bg.setVisibility(View.VISIBLE);
        btn_cs.setOnClickListener(this);
        ll_loading_bg.setVisibility(View.GONE);

    }


    public void showLoadingBg() {
        setVisibility(View.VISIBLE);
        iv_loading.setVisibility(View.VISIBLE);
        ll_erro_bg.setVisibility(View.GONE);
        ll_loading_bg.setVisibility(View.VISIBLE);
    }



    public void dissmiss() {
        setVisibility(View.GONE);
    }

}
