package com.cfbb.android.features.product;

import android.view.View;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseFragment;
import com.easybei.photoview.PhotoView;

/**
 * Created by MrChang45 on 2016/3/24.
 * 计划运作流程
 */
public class PlanFlowFragment extends BaseFragment {

    private PhotoView photoView;

    @Override
    public int initContentView() {
        return R.layout.fragment_product_plan_flow;
    }

    @Override
    public void setUpViews(View view) {
        photoView = (PhotoView) view.findViewById(R.id.phtoview);
       //  ImageWithGlideUtils.lodeFromRes(R.drawable.plan_flow_bg, photoView, getActivity());
    }

    private boolean isInit = true;
    private boolean isHidden;

    @Override
    public void onPause() {
        super.onPause();
        if (!isHidden) {
            StatService.onPause(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        photoView = null;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isHidden = !isVisibleToUser;
        // 每次切换fragment时调用的方法
        if (isVisibleToUser) {
            if (isInit) {
                isInit = false;
            }
            StatService.onResume(this);
        } else {
            if (!isInit) {
                StatService.onPause(this);
            }
        }
    }


    @Override
    public void setUpLisener() {

    }

}
