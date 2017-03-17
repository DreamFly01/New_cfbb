package com.cfbb.android.features.account.releaseLoan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.features.product.ShowPicActivity;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.MyLoanDetailsBean;
import com.cfbb.android.widget.MyGridView;
import com.cfbb.android.widget.YCLoadingBg;

public class LoanDetailsActivity extends BaseActivity implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {
    private TextView back;
    private TextView menu;
    private TextView title;
    private TextView loanStat;//借款状态
    private TextView loanDate;//申请时间
    private TextView loanSateText;//借款状态
    private TextView loanTitle;//借款标题
    private TextView loanMoeny;//借款金额
    private TextView loanLimit;//借款期限
    private TextView returnWay;//还款方式
    private TextView bindingDays;//竞标天数
    private TextView loanDescrib;//借款描述
    private TextView loanerInfo;//借款人信息
    private RelativeLayout rlInfo;
    private RelativeLayout rlDes;
    private RelativeLayout rlAudit;
    private ScrollView scrollView;

    private MyGridView mGridView;
    private MyLoanDetailsAdapter adapter;
    private YCLoadingBg ycLoadingBg;

    private int loanState;
    private String loanDes;


    private String prodcutId;
    private String url;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_loan_details);
        Intent intent = this.getIntent();
        prodcutId = intent.getStringExtra("prodcutId");
    }

    @Override
    public void getDataOnCreate() {
        super.getDataOnCreate();
        addSubscription(RetrofitClient.GetMyLoansDetails(null, prodcutId, this, new YCNetSubscriber<MyLoanDetailsBean>(this) {
            @Override
            public void onYcFinish() {
                super.onYcFinish();
            }

            @Override
            public void onYcNext(MyLoanDetailsBean model) {
                fillView(model);
                loanState = model.loanStats;
                System.out.println(loanState);
                System.out.println(loanState + "--------数据中获取的--------");

                loanDes = model.loanDescrib;
                adapter.addAll(model.relativeData);
                adapter.notifyDataSetChanged();
                ycLoadingBg.dissmiss();
                scrollView.smoothScrollBy(0, 0);
            }
        }));
    }
    /**
     * 填充数据
     *
     * @param model
     */
    private void fillView(final MyLoanDetailsBean model) {
        title.setText("借款详情");

        loanDate.setText(model.loanTime);
        loanDescrib.setText(model.loanDescrib);
        loanLimit.setText(model.loanDate);
        loanMoeny.setText(model.loanMoeny);
        loanTitle.setText(model.loanStatName);
        loanSateText.setText(model.loanSateText);
        returnWay.setText(model.returnWay);
        bindingDays.setText(model.bindingDays);
        loanerInfo.setText(model.loanerInfo);
        if (model.loanStats != 2) {
//            menu.setText("修改");
//            menu.setVisibility(View.VISIBLE);
//            menu.setTextColor(Color.RED);
        } else {
//            menu.setTextColor(Color.GRAY);
//            menu.setVisibility(View.VISIBLE);
//            menu.setText("修改");
//            menu.setEnabled(false);
        }
        /**
         * 证明材料图片点击时间
        */

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LoanDetailsActivity.this, ShowPicActivity.class);
                intent.putExtra("image_url", model.relativeData.get(position).url);
                System.out.println("-----传的url------"+model.relativeData.get(position).url);
                startActivity(intent);
            }
        });


    }

    @Override
    public void setUpViews() {
        back = (TextView) findViewById(R.id.tv_back);
//        menu = (TextView) findViewById(R.id.tv_menu);
        title = (TextView) findViewById(R.id.tv_title);

        loanDate = (TextView) findViewById(R.id.tv_04);
        loanSateText = (TextView) findViewById(R.id.tv_05);
        loanTitle = (TextView) findViewById(R.id.tv_06);
        loanMoeny = (TextView) findViewById(R.id.tv_07);
        loanLimit = (TextView) findViewById(R.id.tv_08);
        returnWay = (TextView) findViewById(R.id.tv_09);
        bindingDays = (TextView) findViewById(R.id.tv_10);
        loanDescrib = (TextView) findViewById(R.id.tv_11);
        loanerInfo = (TextView) findViewById(R.id.tv_12);

        rlInfo = (RelativeLayout) findViewById(R.id.rl_01);
        rlDes = (RelativeLayout) findViewById(R.id.rl_02);
        rlAudit = (RelativeLayout) findViewById(R.id.rl_03);
        mGridView = (MyGridView) findViewById(R.id.gridView);
        ycLoadingBg = (YCLoadingBg) findViewById(R.id.ycLoadingBg);
        scrollView = (ScrollView) findViewById(R.id.sc_view);


        adapter = new MyLoanDetailsAdapter(this);
        mGridView.setAdapter(adapter);
        back.setVisibility(View.VISIBLE);
    }

    @Override
    public void setUpLisener() {

        rlInfo.setOnClickListener(this);
        rlDes.setOnClickListener(this);
        rlAudit.setOnClickListener(this);

        back.setOnClickListener(this);
//        menu.setOnClickListener(this);

    }

    @Override
    public void onUserClick(View v) {
        super.onUserClick(v);
        Bundle bundle = null;
        switch (v.getId()) {
            case R.id.tv_back:
                this.finish();
                break;
            case R.id.tv_menu:
//                getUrlData();
//
//                System.out.println("-----------------------"+url+"-----------------------");
//                bundle = new Bundle();
//                bundle.putString("url",url);
//                JumpCenter.JumpActivity(this, OtherActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
                break;
//            借款描述
            case R.id.rl_01:
                bundle = new Bundle();
                bundle.putInt("flag", 2);
                bundle.putString("loanDes", loanDes);
                JumpCenter.JumpActivity(this, AuditScheduleActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
                break;
//            借款人信息
            case R.id.rl_02:
                bundle = new Bundle();
                bundle.putInt("flag", 3);
                bundle.putString("prodcutId", prodcutId);
                JumpCenter.JumpActivity(this, LoanPersonInfoActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
                break;
//            审核状态
            case R.id.rl_03:
                bundle = new Bundle();
                bundle.putInt("loanState", loanState);
                bundle.putInt("flag", 1);
                bundle.putString("prodcutId", prodcutId);
                System.out.println("---------审核状态-------" + loanState + "---------产品id----------" + prodcutId);


                JumpCenter.JumpActivity(this, AuditScheduleActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
                break;
        }
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

}
