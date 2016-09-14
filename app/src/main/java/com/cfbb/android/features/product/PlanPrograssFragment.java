package com.cfbb.android.features.product;

import android.view.View;
import android.widget.ImageView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseFragment;
import com.cfbb.android.commom.utils.image.ImageWithGlideUtils;

/**
 * Created by MrChang45 on 2016/3/24.
 * 计划进度
 */
public class PlanPrograssFragment extends BaseFragment {

    private ImageView iv_bg;

    @Override
    public int initContentView() {
        return R.layout.fragment_product_plan_prograss;
    }

    @Override
    public void setUpViews(View view) {
        iv_bg = (ImageView) view.findViewById(R.id.iv_01);
        ImageWithGlideUtils.lodeFromRes(R.drawable.plan_prograss_bg, iv_bg, getActivity());
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
        iv_bg = null;
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
