package com.cfbb.android.features.invest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.state.MainFragmentEnum;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.features.product.ProductDetailsActivity;
import com.cfbb.android.features.main.MainActivity;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.BuyRightBean;
import com.cfbb.android.widget.autoFit.AutofitTextView;
import com.cfbb.android.widget.YCLoadingBg;

/***
 * 投标成功 界面
 */
public class InvestBidRightActivity extends BaseActivity  {

    public static final String INVEST_RIGHT_TURN_TO_ACTIVITY_CLASS = "invest_right_turn_to_activity_class";
    public static final String INVEST_RIGHT_TURN_TO_MAIN_INDEX = "invest_right_turn_to_main_index";

    //标的ID
    public static final String PRODUCT_ID = "product_id";
    //标的类型
    public static final String LOAN_TYPEID = "loan_typeid";
    //标的类型
    public static final String INVEST_ID = "invest_id";

    private Class turnToActivity;
    private String invest_id;
    private String prodcutId;
    private String loan_typeId;
    private TextView tv_back;
    private TextView tv_title;
    private TextView tv_moeny;
    private TextView tv_expectedReturn;
    private Button btn_ok;
    private int index;
    private YCLoadingBg ycLoadingBg;
    private Bundle bundle;
    private AutofitTextView aTv_msg;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_invest_bid_right);
        mIntent = getIntent();
        if (null != mIntent) {
            bundle = mIntent.getExtras();
            turnToActivity = (Class) bundle.getSerializable(INVEST_RIGHT_TURN_TO_ACTIVITY_CLASS);
            invest_id = bundle.getString(INVEST_ID);
            prodcutId =bundle.getString(PRODUCT_ID);
            loan_typeId = bundle.getString(LOAN_TYPEID);
            index = bundle.getInt(INVEST_RIGHT_TURN_TO_MAIN_INDEX, MainFragmentEnum.INVALID.getValue());
        }
    }

    @Override
    public void setUpViews() {
        aTv_msg  = (AutofitTextView) findViewById(R.id.tv_12);
        ycLoadingBg = (YCLoadingBg) findViewById(R.id.ycLoadingBg);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setText(getResources().getString(R.string.buy));
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getResources().getString(R.string.buy_right));
        tv_back.setVisibility(View.VISIBLE);
        tv_moeny = (TextView) findViewById(R.id.tv_04);
        tv_expectedReturn = (TextView) findViewById(R.id.tv_06);
        btn_ok = (Button) findViewById(R.id.btn_ok);
    }

    @Override
    public void setUpLisener() {
        tv_back.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void getDataOnCreate() {

        addSubscription(RetrofitClient.BuyRight(null, invest_id, this, new YCNetSubscriber<BuyRightBean>(this) {

            @Override
            public void onYcNext(BuyRightBean model) {
                FillView(model);
                ycLoadingBg.dissmiss();
            }

            @Override
            public void onYCError(APIException e){
                ycLoadingBg.showErroBg(new YCLoadingBg.YCErroLisener() {
                    @Override
                    public void onTryAgainClick() {
                        getDataOnCreate();
                    }
                });
            }
        }));
    }

    private void FillView(BuyRightBean model) {
        tv_moeny.setText(model.moeny);
        tv_expectedReturn.setText(model.expectedReturn);
        aTv_msg.setText(model.message);
    }


    @Override
    public void onUserClick(View v) {
        super.onUserClick(v);
        switch (v.getId()) {

            case R.id.tv_back:
                finish();
                break;
            //完成
            case R.id.btn_ok:
                TurnToOtherActivity();
                break;
        }
    }




    private void TurnToOtherActivity() {
        if (turnToActivity == null) {
            if(bundle == null) {
                bundle = new Bundle();
            }
            if (index != MainFragmentEnum.INVALID.getValue()) {
                bundle.putInt(MainActivity.SHOW_FRAGMENT_INDEX, index);
                JumpCenter.JumpActivity(this, MainActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, true);

            } else {
                bundle.putInt(MainActivity.SHOW_FRAGMENT_INDEX, MainFragmentEnum.HOME.getValue());
                JumpCenter.JumpActivity(this, MainActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, true);

            }
        } else {
            if(turnToActivity == ProductDetailsActivity.class)
            {
                JumpCenter.JumpActivity(this, turnToActivity, bundle, null, JumpCenter.NORMALL_REQUEST, Intent.FLAG_ACTIVITY_CLEAR_TOP, true, true);
            }
            else
            {
                JumpCenter.JumpActivity(this, turnToActivity, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, true);
            }
         }

    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

}
