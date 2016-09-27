package com.cfbb.android.features.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseFragment;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.VertifyInfoBean;
import com.cfbb.android.widget.MyGridView;
import com.cfbb.android.widget.YCLoadingBg;

/**
 * Created by MrChang45 on 2016/3/24
 * 审核信息.
 */
public class VertifyInfoFragment extends BaseFragment {

    private boolean isInit = true; // 是否可以开始加载数据
    private static final String PRODUCT_ID = "product_id";
    private String product_id;

    private MyGridView gv_vertify_result;
    private MyGridView gv_vertify_images;
    private GridViewVertifyImageAdptor gridViewVertifyImageAdptor;
    private GridViewVertifyResultAdaptor gridViewVertifyResultAdaptor;

    private Intent intent;

    public static VertifyInfoFragment newInstance(String productId) {
        VertifyInfoFragment fragment = new VertifyInfoFragment();
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
        return R.layout.fragment_product_vertify_info;
    }


    private YCLoadingBg ycLoadingBg;

    @Override
    public void setUpViews(View view) {
        ycLoadingBg = (YCLoadingBg) view.findViewById(R.id.ycLoadingBg);
        gv_vertify_result = (MyGridView) view.findViewById(R.id.gv_01);
        gv_vertify_images = (MyGridView) view.findViewById(R.id.gv_02);
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
            //TestResultUtils.getSussefulResult33()
            RetrofitClient.getVertifyInfo(null, product_id, getActivity(), new YCNetSubscriber<VertifyInfoBean>(getActivity()) {

                @Override
                public void onYcNext(VertifyInfoBean model) {
                    vertifyInfo = model;
                    FillView();
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
            });
        }
    }

    private VertifyInfoBean vertifyInfo;

    private void FillView() {
        gridViewVertifyImageAdptor = new GridViewVertifyImageAdptor(getActivity());
        gridViewVertifyImageAdptor.mDataSource = vertifyInfo.relativeData;
        gridViewVertifyResultAdaptor = new GridViewVertifyResultAdaptor(getActivity());
        gridViewVertifyResultAdaptor.mDataSource = vertifyInfo.vertifyResultlGroup;

        gv_vertify_result.setAdapter(gridViewVertifyResultAdaptor);
        gv_vertify_images.setAdapter(gridViewVertifyImageAdptor);


        gv_vertify_images.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(getActivity(), ShowPicActivity.class);
                intent.putExtra(ShowPicActivity.IMAGE_URL, vertifyInfo.relativeData.get(position).url);
                startActivity(intent);
            }
        });
    }
}
