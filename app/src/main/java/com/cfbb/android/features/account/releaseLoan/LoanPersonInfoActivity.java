package com.cfbb.android.features.account.releaseLoan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.TestResultUtils;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.LoanPersonInfo;
import com.cfbb.android.widget.YCLoadingBg;

public class LoanPersonInfoActivity extends BaseActivity {

    private TextView back;

    private LoanPersonInfoAdapter adapter;
    private ListView listView;
    private TextView title;
    private YCLoadingBg ycLoadingBg;

    private String productId;
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_loan_person_info);
        Intent intent = getIntent();
        productId = intent.getStringExtra("prodcutId");
    }

    @Override
    public void getDataOnCreate() {
        super.getDataOnCreate();
        addSubscription(RetrofitClient.GetLoanPersonInfo(null,productId, this, new YCNetSubscriber<LoanPersonInfo>(this) {
            @Override
            public void onYcNext(LoanPersonInfo model) {
                adapter.addAll(model.loanInfos);
                adapter.notifyDataSetChanged();
                ycLoadingBg.dissmiss();
            }
        }));
    }

    @Override
    public void setUpViews() {
        back = (TextView)findViewById(R.id.tv_back);
        title = (TextView)findViewById(R.id.tv_title);
        listView = (ListView)findViewById(R.id.listView);
        ycLoadingBg = (YCLoadingBg)findViewById(R.id.ycLoadingBg);


        title.setText("借款人信息");
        back.setVisibility(View.VISIBLE);
        adapter = new LoanPersonInfoAdapter(this);
        listView.setAdapter(adapter);

    }

    @Override
    public void setUpLisener() {
        back.setOnClickListener(this);
    }

    @Override
    public void onUserClick(View v) {
        super.onUserClick(v);
        switch (v.getId()){
            case R.id.tv_back:
                this.finish();
                break;
        }
    }
}
