package com.cfbb.android.features.account;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.features.authentication.RealNameAuthenticationActivity;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.CertificationResultBean;
import com.cfbb.android.protocol.bean.MyBankInfoBean;
import com.cfbb.android.widget.YCLoadingBg;
import com.cfbb.android.widget.dialog.YCDialogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的银行卡
 */
public class MyBankInfoActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private TextView tv_back;
    private TextView tv_title;
    private YCLoadingBg ycLoadingBg;
    private RelativeLayout rl_01;
    private ListView listView;
    private MyBankInfoAdaptor myBankInfoAdaptor;

    private YCDialogUtils ycDialogUtils;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_bank_info);
    }

    @Override
    public void setUpViews() {


        ycDialogUtils = new YCDialogUtils(this);

        rl_01 = (RelativeLayout) findViewById(R.id.rl_01);
        ycLoadingBg = (YCLoadingBg) findViewById(R.id.ycLoadingBg);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        listView = (ListView) findViewById(R.id.listView);

        myBankInfoAdaptor = new MyBankInfoAdaptor(this);
        tv_back.setText(getResources().getString(R.string.nav_account));
        tv_back.setVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.my_bankcard_list_str));
        listView.setAdapter(myBankInfoAdaptor);
    }

    @Override
    public void setUpLisener() {
        tv_back.setOnClickListener(this);
        rl_01.setOnClickListener(this);
        listView.setOnItemClickListener(this);
    }


    private List<MyBankInfoBean> myBankInfoList;

    public void MyGetData() {

        //TestResultUtils.getSussefulResult17()
        addSubscription(RetrofitClient.GetMyBankInfo(null, this, new YCNetSubscriber<List<MyBankInfoBean>>(this) {

            @Override
            public void onYcNext(List<MyBankInfoBean> model) {

                if (model == null || model.size() == 0) {
                    rl_01.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                } else {
                    rl_01.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    myBankInfoList = new ArrayList<>();
                    myBankInfoList = model;
                    FilLView();
                }
                ycLoadingBg.dissmiss();
            }

            @Override
            public void onYCError(APIException e) {
                ycLoadingBg.showErroBg(new YCLoadingBg.YCErroLisener() {
                    @Override
                    public void onTryAgainClick() {
                        getDataOnCreate();
                    }
                });
            }

        }));

    }

    @Override
    protected void onResume() {
        super.onResume();
        MyGetData();
        StatService.onResume(this);
    }

    private void FilLView() {

        myBankInfoAdaptor.clear();
        myBankInfoAdaptor.addAll(myBankInfoList);
        myBankInfoAdaptor.notifyDataSetChanged();


    }


    @Override
    public void onUserClick(View v) {
        super.onUserClick(v);
        switch (v.getId()) {

            case R.id.tv_back:
                finish();
                break;
            // 删除
            case R.id.btn_gono_xj:
                DissmissDialog();
                DeleteBank();
                break;
            // 关闭对话框
            case R.id.btn_cancel:
                DissmissDialog();
                break;
            //添加
            case R.id.rl_01:
                DissmissDialog();
                AddBank();
                break;
        }
    }


    private void DissmissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private void AddBank() {

        //TestResultUtils.getBaseFaliedResult()
        addSubscription(RetrofitClient.getCertificationInfo(null, this, new YCNetSubscriber<CertificationResultBean>(this, true) {



            @Override
            public void onYcNext(CertificationResultBean model) {
                JumpCenter.JumpActivity(MyBankInfoActivity.this, AddBankActivity.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
            }

            @Override
            public void onYCError(APIException e) {
                if (e.code == 2) {
                    //code  2  代表未认证
                    dismissLoadingView();
                    ycDialogUtils.showDialog(getResources().getString(R.string.dialog_kindly_title), e.getMessage(), new YCDialogUtils.MyTwoBtnclickLisener() {
                        @Override
                        public void onFirstBtnClick(View v) {
                            //ok
                            ycDialogUtils.DismissMyDialog();
                            Bundle bundle = new Bundle();
                            bundle.putString(RealNameAuthenticationActivity.SHOW_BACK_TXT, getResources().getString(R.string.my_bankcard_list_str));
                            bundle.putSerializable(RealNameAuthenticationActivity.NEXT_ACTIVITY_CLASS, AddBankActivity.class);
                            JumpCenter.JumpActivity(MyBankInfoActivity.this, RealNameAuthenticationActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);

                        }

                        @Override
                        public void onSecondBtnClick(View v) {
                            ycDialogUtils.DismissMyDialog();
                        }
                    }, true);


                } else {
                    ycDialogUtils.showSingleDialog(MyBankInfoActivity.this.getResources().getString(R.string.dialog_title), MyBankInfoActivity.this.getResources().getString(R.string.request_erro_str), new YCDialogUtils.MySingleBtnclickLisener() {
                        @Override
                        public void onBtnClick(View v) {
                            ycDialogUtils.DismissMyDialog();
                        }
                    }, true);
                }

            }
        }));

    }

    private void DeleteBank() {

        ycDialogUtils.showDialog(getResources().getString(R.string.dialog_title), getString(R.string.check_delete), new YCDialogUtils.MyTwoBtnclickLisener() {
            @Override
            public void onFirstBtnClick(View v) {
                //ok
                ycDialogUtils.DismissMyDialog();
                addSubscription(RetrofitClient.DeleteBank(null, myBankInfo.bankCardId, MyBankInfoActivity.this, new YCNetSubscriber(MyBankInfoActivity.this, true) {

                    @Override
                    public void onYcNext(Object model) {
                        MyGetData();
                    }

                }));
            }

            @Override
            public void onSecondBtnClick(View v) {
                ycDialogUtils.DismissMyDialog();
            }
        }, true);

    }

    private MyBankInfoBean myBankInfo;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        myBankInfo = myBankInfoList.get(position);
        showDialog();
    }

    private Dialog dialog;

    /**
     * 顯示自定義對話框
     */
    private void showDialog() {
        View view = LayoutInflater.from(this).inflate(
                R.layout.bank_manager_dialog, null);
        // 找到对应控件，注册事件
        Button btn_xj_goon = (Button) view.findViewById(R.id.btn_gono_xj);
        btn_xj_goon.setOnClickListener(this);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);

        dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        // window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();

        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.MATCH_PARENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);

        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
