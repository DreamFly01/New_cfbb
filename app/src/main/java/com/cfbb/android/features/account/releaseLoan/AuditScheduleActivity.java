package com.cfbb.android.features.account.releaseLoan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.features.product.ProductDetailsActivity;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.AuditStateBean;
import com.cfbb.android.widget.YCLoadingBg;

import java.util.ArrayList;
import java.util.List;

public class AuditScheduleActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private TextView back;
    private TextView title;

    private LinearLayout llReason;//筹款失败原因布局
    private LinearLayout llInvest;//筹款详情布局
    private LinearLayout fl1;
    private FrameLayout fl2;

    private TextView loanSateText;//借款状态
    private TextView failureDescribe;//筹款失败原因描述


    private TextView loanDesText;
    private ListView listView;
    private List<AuditStateBean.AuditInfo> list = new ArrayList<>();


    private String loanTypeIdStr;//借款标ID
    private String prodcutIdStr;//产品类型ID

    private YCLoadingBg ycLoadingBg;
    private MyProductInfoAdapter adapter;

    private int loanState;
    private String loanDes;
    private int flag;

    private String prodcutId;
    private String loanTypeId;
    private List<AuditStateBean.AuditInfo> auditInfos = new ArrayList<>();
    private int canInvest;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_audit_schedule);
        Intent intent = this.getIntent();
        loanState = intent.getIntExtra("loanState", 0);
        loanDes = intent.getStringExtra("loanDes");
        prodcutId = intent.getStringExtra("prodcutId");
        flag = intent.getIntExtra("flag", 0);
    }

    @Override
    public void getDataOnCreate() {
        super.getDataOnCreate();
        if (flag == 1) {
            title.setText("审核状态");
            showData();
        } else if (flag == 2) {
            title.setText("借款描述");
            ycLoadingBg.dissmiss();
        } else {
            ycLoadingBg.dissmiss();
        }
    }

    private void showData() {
        addSubscription(RetrofitClient.GetAuditState(null, prodcutId, this, new YCNetSubscriber<AuditStateBean>(this) {
            @Override
            public void onYcNext(AuditStateBean model) {
                fillView(model);
                prodcutId = model.products.prodcutId;
                loanTypeId = model.products.loanTypeId;
                auditInfos.add(model.products);
                adapter.addAll(auditInfos);
                list = auditInfos;
                canInvest = model.products.canInvest;
                ycLoadingBg.dissmiss();
                int money = Integer.parseInt(model.products.remiansMoeny);
                System.out.println(money+"*-*-*--*-*-*-*-*-*-*--*-*-*-*-*-*-");
            }
        }));
    }

    private void fillView(AuditStateBean model) {
        if (flag == 1) {
            fl1.setVisibility(View.VISIBLE);
            fl2.setVisibility(View.GONE);

            if (loanState == 8 || loanState == 9) {
                llReason.setVisibility(View.VISIBLE);
                llInvest.setVisibility(View.GONE);
                loanSateText.setText(model.loanSateText);
                failureDescribe.setText(model.failureDescribe);

            } else {
                llInvest.setVisibility(View.VISIBLE);
                llReason.setVisibility(View.GONE);
                loanSateText.setText(model.loanSateText);
            }
        } else if (flag == 2) {
            fl1.setVisibility(View.GONE);
            fl2.setVisibility(View.VISIBLE);
            loanDesText.setText(loanDes);
        }
    }


    @Override
    public void setUpViews() {
        title = (TextView) findViewById(R.id.tv_title);
        back = (TextView) findViewById(R.id.tv_back);
        ycLoadingBg = (YCLoadingBg) findViewById(R.id.ycLoadingBg);

        llReason = (LinearLayout) findViewById(R.id.audit_fail);
        llInvest = (LinearLayout) findViewById(R.id.ll_02);
        fl1 = (LinearLayout) findViewById(R.id.fl_01);
        fl2 = (FrameLayout) findViewById(R.id.fl_02);

        loanSateText = (TextView) findViewById(R.id.tv_04);
        failureDescribe = (TextView) findViewById(R.id.tv_05);

        listView = (ListView) findViewById(R.id.listView);

        loanDesText = (TextView) findViewById(R.id.tv_12);
        loanDesText.setText(loanDes);

        adapter = new MyProductInfoAdapter(this);
        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(this);

        back.setVisibility(View.VISIBLE);
    }

    @Override
    public void setUpLisener() {
        back.setOnClickListener(this);

    }

    @Override
    public void onUserClick(View v) {
        super.onUserClick(v);
        Bundle bundle = null;
        switch (v.getId()) {
            case R.id.tv_back:
                this.finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putString("product_id", prodcutId);
        bundle.putString("loan_typeid",loanTypeId);


        JumpCenter.JumpActivity(this, ProductDetailsActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);

    }
}
