package com.cfbb.android.features.invest;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseFragment;
import com.cfbb.android.commom.state.MainFragmentEnum;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.features.main.MainActivity;
import com.cfbb.android.features.product.ProductDetailsActivity;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.ProductInfoBean;
import com.cfbb.android.widget.PullDownView;
import com.cfbb.android.widget.YCLoadingBg;
import com.cfbb.android.widget.dialog.YCDialogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 投资列表内容页
 */
public class InvestContentFragment extends BaseFragment implements  AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    private static final String PARAM_PRODUCT_TYPE = "param_product_type";
    private static final String PARAM_LOAN_TYPE_ID = "param_loan_type_id";
    private boolean isInit = true;

    //升序
    private static final String SORT_ASC = "1";
    //降序
    private static final String SORT_DESC = "2";

    //默认排序
    private static final String DEFAULT_SORT_DESC = "0";

    //起始页码
    private static final int START_PAGE_INDEX = 1;

    //默认筛选规则
    private static final String SORT_DEFAULT = "0";

    //利率筛选
    private static final String SORT_RATE = "2";

    //投资期限筛选
    private static final String SORT_PERIOD = "1";


    private int mListViewHeight = 0;
    private int clickDateTimes = 0;
    private int clickRateTimes = 0;
    private int clickLatestedTimes = 0;


    private String product_type;
    private String loan_type_id;
    private String sort = SORT_DEFAULT;
    private String desc = DEFAULT_SORT_DESC;
    private int curentIndex = START_PAGE_INDEX;

    private OnProductTypeChangeListener mListener;
    private YCDialogUtils ycDialogUtils;

    public InvestContentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param product_type Parameter 1.
     * @param product_type Parameter 2.
     * @return A new instance of fragment InvestContentFragment.
     */
    public static InvestContentFragment newInstance(String product_type, String loan_type_id) {
        InvestContentFragment fragment = new InvestContentFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_PRODUCT_TYPE, product_type);
        args.putString(PARAM_LOAN_TYPE_ID, loan_type_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product_type = getArguments().getString(PARAM_PRODUCT_TYPE);
            loan_type_id = getArguments().getString(PARAM_LOAN_TYPE_ID);
            System.out.println("------------product_type--------------"+product_type+"--loan_type_id--"+loan_type_id);
        }
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_invest_content;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 判断当前fragment是否显示
        if (getUserVisibleHint()) {
            showData();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // 每次切换fragment时调用的方法
        if (isVisibleToUser) {
            showData();
        }
    }

    /**
     * 初始化数据
     */
    private void showData() {
        if (isInit) {
            isInit = false;//加载数据完成
            RetrofitClient.getProductListRequest(null, getActivity(), curentIndex, product_type, sort, desc, loan_type_id, new YCNetSubscriber<ProductInfoBean>(getActivity()) {

                @Override
                public void onYcNext(ProductInfoBean model) {
                    if (isFrist) {
                        if (mListener != null) {
                            mListener.onShowProductTypeName(model.showType.trim());
                        }
                        isFrist = false;
                    }
                    if (model != null && model.products.size() > 0) {
                        if (curentIndex == START_PAGE_INDEX) {
                            productInfoBeanList.clear();
                            investAdaptor.clear();
                            investAdaptor.notifyDataSetChanged();
                        }
                        productInfoBeanList.addAll(model.products);
                        investAdaptor.addAll(model.products);
                        investAdaptor.notifyDataSetChanged();
                        if (curentIndex == START_PAGE_INDEX) {
                            listView.setSelection(0);
                        }
                    } else {
                        ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.dialog_no_more_data), new YCDialogUtils.MySingleBtnclickLisener() {
                            @Override
                            public void onBtnClick(View v) {
                                ycDialogUtils.DismissMyDialog();
                            }
                        }, true);
                    }
                    ycLoadingBg.dissmiss();
                }

                @Override
                public void onYCError(APIException e) {
                   // super.onYCError(e);
                    isInit = true;
                    ycLoadingBg.showErroBg(new YCLoadingBg.YCErroLisener() {
                        @Override
                        public void onTryAgainClick() {
                            showData();
                        }
                    });
                }

                @Override
                public void onYcFinish() {
                    pullDownView.endUpdate();
                }
            });
        }
    }

    @Override
    public void setUpViews(View view) {
        ycDialogUtils = new YCDialogUtils(getActivity());
        ycLoadingBg = (YCLoadingBg) view.findViewById(R.id.ycLoadingBg);

        tv_latested_typeName = (TextView) view.findViewById(R.id.tv_14);
        ll_invest_date = (RelativeLayout) view.findViewById(R.id.rl_02);
        iv_invest_date_arrow = (ImageView) view.findViewById(R.id.iv_04);
        tv_invest_date_typeName = (TextView) view.findViewById(R.id.tv_02);

        rl_latested = (RelativeLayout) view.findViewById(R.id.rl_03);

        ll_invest_rate = (RelativeLayout) view.findViewById(R.id.rl_01);
        iv_invest_rate_arrow = (ImageView) view.findViewById(R.id.iv_07);
        tv_invest_rate_typeName = (TextView) view.findViewById(R.id.tv_03);

        pullDownView = (PullDownView) view.findViewById(R.id.pullDownView);
        listView = (ListView) view.findViewById(R.id.listView);

        investAdaptor = new investAdaptor(getActivity(), new investAdaptor.InvestOnclickLisenr() {
            @Override
            public void onClick(int position) {
                TurnToInvestActivity(productInfoBeanList.get(position).prodcutId, productInfoBeanList.get(position).loanTypeId);
            }
        });
        listView.setAdapter(investAdaptor);
    }

    @Override
    public void setUpLisener() {
        ll_invest_date.setOnClickListener(this);
        ll_invest_rate.setOnClickListener(this);
        rl_latested.setOnClickListener(this);
        listView.setOnScrollListener(this);
        pullDownView.setUpdateHandle(new PullDownView.UpdateHandle() {
            @Override
            public void onUpdate() {
                curentIndex = START_PAGE_INDEX;
                isInit=true;
                showData();
            }
        });

        listView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                mListViewHeight = listView.getHeight();
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                    listView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    listView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

            }
        });
        listView.setOnItemClickListener(this);


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnProductTypeChangeListener) {
            mListener = (OnProductTypeChangeListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnProductTypeChangeListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private investAdaptor investAdaptor;
    private List<ProductInfoBean.ProductInfo> productInfoBeanList = new ArrayList<>();

    private Boolean isFrist = true;

    private PullDownView pullDownView;
    private ListView listView;
    private YCLoadingBg ycLoadingBg;

    private RelativeLayout ll_invest_date;
    private RelativeLayout ll_invest_rate;
    private RelativeLayout rl_latested;

    private TextView tv_invest_date_typeName;
    private TextView tv_invest_rate_typeName;
    private TextView tv_latested_typeName;

    private ImageView iv_invest_date_arrow;
    private ImageView iv_invest_rate_arrow;


    @Override
    public void onUserClick(View v) {
        super.onUserClick(v);
        switch (v.getId()) {
            //利率
            case R.id.rl_01:
                ClickRate();
                break;
            //期限
            case R.id.rl_02:
                ClickDate();
                break;
            //最新
            case R.id.rl_03:
                ClickLatested();
                break;
        }
    }


    @Override
    public void getDataOnActivityCreated() {
//        if (!isInit) {
//            RetrofitClient.getProductListRequest(null, getActivity(), curentIndex, product_type, sort, desc, loan_type_id, new YCNetSubscriber<ProductInfoBean>(getActivity()) {
//                @Override
//                public void onYcNext(ProductInfoBean model) {
//                    if (isFrist) {
//                        if (mListener != null) {
//                            mListener.onShowProductTypeName(model.showType.trim());
//                        }
//                        isFrist = false;
//                    }
//                    if (model != null && model.products.size() > 0) {
//                        if (curentIndex == START_PAGE_INDEX) {
//                            productInfoBeanList.clear();
//                            investAdaptor.clear();
//                            investAdaptor.notifyDataSetChanged();
//                        }
//                        productInfoBeanList.addAll(model.products);
//                        investAdaptor.addAll(model.products);
//                        investAdaptor.notifyDataSetChanged();
//                        if (curentIndex == START_PAGE_INDEX) {
//                            listView.setSelection(0);
//                        }
//                    } else {
//                        ycDialogUtils.showSingleDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.dialog_no_more_data), new YCDialogUtils.MySingleBtnclickLisener() {
//                            @Override
//                            public void onBtnClick(View v) {
//                                ycDialogUtils.DismissMyDialog();
//                            }
//                        }, true);
//                    }
//                    ycLoadingBg.dissmiss();
//                }
//
//                @Override
//                public void onYCError(APIException e) {
//                    isInit = true;
//                    ycLoadingBg.showErroBg(new YCLoadingBg.YCErroLisener() {
//                        @Override
//                        public void onTryAgainClick() {
//                            showData();
//                        }
//                    });
//                }
//
//                @Override
//                public void onYcFinish() {
//
//                    super.onYcFinish();
//                    pullDownView.endUpdate();
//
//                }
//            });
//        }
    }

    //最新
    private void ClickLatested() {

        curentIndex = START_PAGE_INDEX;
        sort = SORT_DEFAULT;
        desc = DEFAULT_SORT_DESC;
        clickDateTimes = 0;
        clickRateTimes = 0;
        if (clickLatestedTimes == 0) {

            tv_latested_typeName.setTextColor(getResources().getColor(R.color.txt_red));

            iv_invest_rate_arrow.setImageDrawable(getResources().getDrawable(R.mipmap.arrow_default));
            tv_invest_rate_typeName.setTextColor(getResources().getColor(R.color.txt_3));

            iv_invest_date_arrow.setImageDrawable(getResources().getDrawable(R.mipmap.arrow_default));
            tv_invest_date_typeName.setTextColor(getResources().getColor(R.color.txt_3));

            ycLoadingBg.showLoadingBg();
            isInit=true;
            showData();
            clickLatestedTimes++;

        } else {
            //点击多次，只请求一次
        }


    }

    //期限筛选
    private void ClickDate() {
        curentIndex = START_PAGE_INDEX;
        sort = SORT_PERIOD;
        clickRateTimes = 0;
        clickLatestedTimes = 0;
        if (clickDateTimes == 0) {
            desc = SORT_ASC;
            iv_invest_date_arrow.setImageDrawable(getResources().getDrawable(R.mipmap.arrow_red_upper));
            tv_invest_date_typeName.setTextColor(getResources().getColor(R.color.txt_red));

            iv_invest_rate_arrow.setImageDrawable(getResources().getDrawable(R.mipmap.arrow_default));
            tv_invest_rate_typeName.setTextColor(getResources().getColor(R.color.txt_3));

            tv_latested_typeName.setTextColor(getResources().getColor(R.color.txt_3));

            clickDateTimes++;

        } else if (clickDateTimes == 1) {
            desc = SORT_DESC;
            iv_invest_date_arrow.setImageDrawable(getResources().getDrawable(R.mipmap.arrrow_red_down));
            clickDateTimes = 0;
        }
        ycLoadingBg.showLoadingBg();
        isInit=true;
        showData();
    }

    //利率筛选
    private void ClickRate() {
        curentIndex = START_PAGE_INDEX;
        sort = SORT_RATE;
        clickDateTimes = 0;
        clickLatestedTimes = 0;
        if (clickRateTimes == 0) {
            desc = SORT_DESC;

            iv_invest_rate_arrow.setImageDrawable(getResources().getDrawable(R.mipmap.arrrow_red_down));
            tv_invest_rate_typeName.setTextColor(getResources().getColor(R.color.txt_red));

            iv_invest_date_arrow.setImageDrawable(getResources().getDrawable(R.mipmap.arrow_default));
            tv_invest_date_typeName.setTextColor(getResources().getColor(R.color.txt_3));


            tv_latested_typeName.setTextColor(getResources().getColor(R.color.txt_3));

            clickRateTimes++;
        } else if (clickRateTimes == 1) {
            desc = SORT_ASC;
            iv_invest_rate_arrow.setImageDrawable(getResources().getDrawable(R.mipmap.arrow_red_upper));
            clickRateTimes = 0;
        }
        ycLoadingBg.showLoadingBg();
        isInit=true;
        showData();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int lastItem = firstVisibleItem + visibleItemCount;
        if(lastItem == totalItemCount) {
            View lastItemView=listView.getChildAt(listView.getChildCount()-1);
            if(null != lastItemView) {
                if ((listView.getBottom()) == lastItemView.getBottom()) {
                    curentIndex++;
                    isInit=true;
                    showData();
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ProductInfoBean.ProductInfo productBean = productInfoBeanList.get(position);
        //已完成的标的严禁跳转
        if(productBean.loanStatus != 10) {
            TurnToProductDetailsActivity(productBean.prodcutId, productBean.loanTypeId);
            System.out.println("投资页面：------loanTypeId-------"+productBean.loanTypeId);
        }
    }

    public void TurnToProductDetailsActivity(String prodcutId, String loanTypeId) {
        Bundle bundle = new Bundle();
        bundle.putString(ProductDetailsActivity.PRODUCT_ID, prodcutId);
        bundle.putString(ProductDetailsActivity.LOAN_TYPEID, loanTypeId);
        bundle.putInt(MainActivity.SHOW_FRAGMENT_INDEX, MainFragmentEnum.INVEST.getValue());
        bundle.putSerializable(JumpCenter.TO_ACTIVITY, ProductDetailsActivity.class);
        JumpCenter.JumpActivity(getActivity(), ProductDetailsActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);

    }

    private void TurnToInvestActivity(String prodcutId, String loan_TypeId) {

        Bundle params = new Bundle();
        params.putString(InvestBidActivity.SHOW_BACK_TXT, getResources().getString(R.string.nav_invest));
        params.putString(InvestBidActivity.PRODUCT_ID, prodcutId);
        params.putString(InvestBidActivity.LOAN_TYPEID, loan_TypeId);
        params.putInt(InvestBidActivity.INVEST_TURN_TO_MAIN_INDEX, MainFragmentEnum.INVEST.getValue());
        JumpCenter.JumpActivity(getActivity(), InvestBidActivity.class, params, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);

    }

    public interface OnProductTypeChangeListener {
        // TODO: Update argument type and name
        void onShowProductTypeName(String typeName);
    }
}
