package com.cfbb.android.commom.baseview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cfbb.android.commom.utils.others.L;

import java.util.Calendar;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author MrChang45
 * @time 2016/4/27
 * @desc
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {


    //管理异步处理与Fragment生命周期,避免出现内存泄漏
    private CompositeSubscription mCompositeSubscription;

    protected void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(initContentView(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViews(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpLisener();
        getDataOnActivityCreated();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
    }

    /***
     * 设置视图布局，必须实现
     */
    public abstract int initContentView();

    /***
     * 初始化试图
     */
    public abstract void setUpViews(View view);

    /***
     * 注册事件
     */
    public abstract void setUpLisener();

    public void getDataOnActivityCreated() {

    }

    public void onClickFragment() {

    }

    public void onUserClick(View v) {

    }

    //两次点击最小间隔时间
    public static final int MIN_CLICK_DELAY_TIME = 500;
    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {

        //防止重复点击
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onUserClick(v);
        }

    }

}
