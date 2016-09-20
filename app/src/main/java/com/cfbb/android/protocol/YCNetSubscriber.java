package com.cfbb.android.protocol;

import android.app.Activity;
import android.view.View;

import com.cfbb.android.BuildConfig;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.utils.others.L;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.protocol.bean.BaseResultBean;
import com.cfbb.android.protocol.bean.VertifyCodeInfoBean;
import com.cfbb.android.widget.CustomProgress;
import com.cfbb.android.widget.dialog.YCDialogUtils;

import rx.Subscriber;

/**
 * @author MrChang
 *         created  at  2016/2/25.
 * @description 默认开启网络请求之前 打开加载中  结束请求 关闭加载中。。。
 */
public abstract class YCNetSubscriber<T> extends Subscriber<T> {

    /***
     * 菊花转,默认情况下是关闭状态
     */
    private CustomProgress mCustomProgress;

    /***
     * 关联页面引用
     */
    private Activity mActivity;

    /**
     * 是否显示菊花转
     */
    private boolean mIsShowLoading = false;

    private YCDialogUtils mYcDialogUtils;

    public YCNetSubscriber(Activity activity) {

        if (null != activity) {
            this.mActivity = activity;
        }

    }

    /***
     * @param activity
     * @param isShowLoading 是否显示菊花转
     */
    public YCNetSubscriber(Activity activity, boolean isShowLoading) {

        if (null != activity) {
            this.mActivity = activity;
            this.mIsShowLoading = isShowLoading;
        }

    }

    public void dismissLoadingView() {
        if (mIsShowLoading && null != mCustomProgress) {
            mCustomProgress.dismiss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (null != mActivity && mIsShowLoading) {
            mCustomProgress = CustomProgress.show(mActivity, null);
        }
    }

    @Override
    public void onCompleted() {
        dismissLoadingView();
        onYcFinish();
    }

    /***
     * 在onNext 出现异常也会被捕获 然后执行onError
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        L.e("错误信息："+e.getMessage());
        // onYCError(new APIException("错误信息:" + e.getMessage(), -99));
        onYcFinish();
    }

    @Override
    public void onNext(T o) {
        dismissLoadingView();
        if (BuildConfig.DEBUG) {
            //在测试环境下，默认弹出验证码
            if (o instanceof VertifyCodeInfoBean) {
                ((BaseActivity) mActivity).showLongToast(((VertifyCodeInfoBean) o).mark);
            }
        }

        if (o instanceof BaseResultBean) {
            BaseResultBean tempBean = (BaseResultBean) o;
            if (tempBean.code != APIService.OK_CODE) {
                onYCError(new APIException(tempBean.msg, tempBean.code));
            } else {
                onYcNext(o);
            }
        } else {
            onYcNext(o);
        }

    }


    public abstract void onYcNext(T model);


    /***
     * 请求错误都在这里执行，包括onYcNext方法里面出现异常也会执行
     *
     * @param erro
     */
    public void onYCError(APIException erro) {

        dismissLoadingView();
        if (null == mYcDialogUtils) {
            mYcDialogUtils = new YCDialogUtils(mActivity);
        }
        String erroMsg = erro.getMessage();
        if (StrUtil.isEmpty(erroMsg)) {
            erroMsg = mActivity.getResources().getString(R.string.request_erro_str);
        }
        mYcDialogUtils.showSingleDialog(mActivity.getResources().getString(R.string.dialog_title), erroMsg, new YCDialogUtils.MySingleBtnclickLisener() {
            @Override
            public void onBtnClick(View v) {
                mYcDialogUtils.DismissMyDialog();
            }
        }, true);
    }

    /***
     * 最后一个必定执行的方法，一般用来关闭刷新控件
     */
    public void onYcFinish() {
        //do nothing by default
    }

}
