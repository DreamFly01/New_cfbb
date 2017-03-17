package com.cfbb.android.features.account.releaseLoan;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.commom.utils.image.ImageWithGlideUtils;
import com.cfbb.android.protocol.bean.ImgArrayBean;
import com.cfbb.android.protocol.bean.ImgData;
import com.cfbb.android.protocol.bean.LoanInfoBean;
import com.cfbb.android.widget.MyGridView;
import com.cfbb.android.widget.dialog.YCDialogUtils;
import com.cfbb.android.widget.views.PickerScrollView;
import com.cfbb.android.widget.views.Pickers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class IntroduceLoanActivity extends BaseActivity {
    private static final String IMAGE_FILE_NAME = "image.jpg";
    /**
     * 请求码
     */
    private static final int IMAGE_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int RESULT_REQUEST_CODE = 3;
    private static final int RZ_REQUEST_CODE = 4;

    private PickerScrollView pickerScrollView;
    private TextView cancel;
    private TextView select;
    private TextView tv04, tv05, tv06, tv07, tv08, tv09,
            tv10, tv11, tv12, tv13, tv14, tv15, tv081,
            tv16, tv23,
    //月薪收入
    tv161,
    // 公司名称 ，公司地址，公司业务，在职岗位，社保，工资发放形式
    tv117, tv118, tv119, tv120, tv121, tv122,
    // 车辆品牌，车辆型号，车辆归属地，市场评估价，车辆状况，车辆状态
    tv217, tv218, tv219, tv220, tv221, tv222,
    // 房产所在地，房产面积，房产评估价，房产所有人，房产状态
    tv317, tv318, tv319, tv320, tv321,
    //营业收入，企业类型，企业所在地，主营业务,成立时间
    tv417, tv418, tv419, tv420, tv421,
            tvShenfenz, tvXinyong, tvCheliang1, tvCheliang2, tvFangdai1, tvFangdai2, tvGeren1, tvQiye1, tvQiye2, tvQiye3, tvQiye4,
            tvTijiao,
            zhengming;
    private TextView back, menu, title, commit, tianjia;
    private RelativeLayout rl01;
    private List<Pickers> list;
    private YCDialogUtils ycDialogUtils;
    private ScrollView sl;
    private List<String> strings = new ArrayList<>();

    private LinearLayout introduce1, introduce2, introduce3, introduce4,
            cailiao,
            chedai, fangdai, gerendai, qiyedai,
            shenfenz, gerenziliao, geren, chedai1, chedai2, fangdai1, fangdai2, qiye1, qiye2, qiye3, qiye4,
            choseImg, llgv, llimg;
    private RelativeLayout ll04, ll05;

    private ImageView img1, img2, img3, img4, img5, img6, img7, img8, img9, img10, img11, shenfenImg1, shenfenImg2,
            shenfenDelImg1, shenfenDelImg2;

    private MyGridView myGridView1, myGridView2, myGridView3, myGridView4, myGridView5, myGridView6, myGridView7, myGridView8, myGridView9, myGridView10;
    private IntroduceLoanAdapter adapter;

    private DatePicker mDataPicker;

    private int year;
    private int month;
    private int day;
    private int flag = 0;
    private int menuFlag = 0;
    private int imgFlag = 0;
    private int listFlag = 0;


    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_introduce_loan);
    }


    @Override
    public void setUpViews() {
        ycDialogUtils = new YCDialogUtils(this);
        pickerScrollView = (PickerScrollView) findViewById(R.id.pickerscrlllview);

        title = (TextView) findViewById(R.id.tv_title);
        back = (TextView) findViewById(R.id.tv_back);
        menu = (TextView) findViewById(R.id.tv_menu);
        commit = (TextView) findViewById(R.id.txt_tijiao);
        cancel = (TextView) findViewById(R.id.tv_cancel);
        select = (TextView) findViewById(R.id.tv_true);
        rl01 = (RelativeLayout) findViewById(R.id.picker_rel);
        tv04 = (TextView) findViewById(R.id.tv_04);
        tv05 = (TextView) findViewById(R.id.tv_05);
        tv06 = (TextView) findViewById(R.id.tv_06);
        tv07 = (TextView) findViewById(R.id.tv_07);
        tv08 = (TextView) findViewById(R.id.tv_08);
        tv09 = (TextView) findViewById(R.id.tv_09);
        tv10 = (TextView) findViewById(R.id.tv_10);
        tv11 = (TextView) findViewById(R.id.tv_11);
        tv12 = (TextView) findViewById(R.id.tv_12);
        tv13 = (TextView) findViewById(R.id.tv_13);
        tv14 = (TextView) findViewById(R.id.tv_14);
        tv15 = (TextView) findViewById(R.id.tv_15);
        tv16 = (TextView) findViewById(R.id.tv_16);
        tv23 = (TextView) findViewById(R.id.tv_23);
        tv081 = (TextView) findViewById(R.id.tv_081);

        tv161 = (TextView) findViewById(R.id.tv_161);

        tv117 = (TextView) findViewById(R.id.tv_117);
        tv118 = (TextView) findViewById(R.id.tv_118);
        tv119 = (TextView) findViewById(R.id.tv_119);
        tv120 = (TextView) findViewById(R.id.tv_120);
        tv121 = (TextView) findViewById(R.id.tv_121);
        tv122 = (TextView) findViewById(R.id.tv_122);

        tv217 = (TextView) findViewById(R.id.tv_217);
        tv218 = (TextView) findViewById(R.id.tv_218);
        tv219 = (TextView) findViewById(R.id.tv_219);
        tv220 = (TextView) findViewById(R.id.tv_219);
        tv221 = (TextView) findViewById(R.id.tv_221);
        tv222 = (TextView) findViewById(R.id.tv_222);

        tv317 = (TextView) findViewById(R.id.tv_317);
        tv318 = (TextView) findViewById(R.id.tv_318);
        tv319 = (TextView) findViewById(R.id.tv_319);
        tv320 = (TextView) findViewById(R.id.tv_320);
        tv321 = (TextView) findViewById(R.id.tv_321);

        tv417 = (TextView) findViewById(R.id.tv_417);
        tv418 = (TextView) findViewById(R.id.tv_418);
        tv419 = (TextView) findViewById(R.id.tv_419);
        tv420 = (TextView) findViewById(R.id.tv_420);
        tv421 = (TextView) findViewById(R.id.tv_421);

        tvTijiao = (TextView) findViewById(R.id.txt_tijiao2);
        tianjia = (TextView) findViewById(R.id.txt_tianjia);
        sl = (ScrollView) findViewById(R.id.sl_01);
        ll04 = (RelativeLayout) findViewById(R.id.ll_04);
        ll05 = (RelativeLayout) findViewById(R.id.ll_05);
        introduce1 = (LinearLayout) findViewById(R.id.introduce1);
        introduce2 = (LinearLayout) findViewById(R.id.introduce2);
        introduce3 = (LinearLayout) findViewById(R.id.introduce3);
        introduce4 = (LinearLayout) findViewById(R.id.introduce4);

        cailiao = (LinearLayout) findViewById(R.id.ll_cailiao);

        chedai = (LinearLayout) findViewById(R.id.ll_chelian);
        fangdai = (LinearLayout) findViewById(R.id.ll_fangdai);
        gerendai = (LinearLayout) findViewById(R.id.ll_gerendai);
        qiyedai = (LinearLayout) findViewById(R.id.ll_qiye);

        llgv = (LinearLayout) findViewById(R.id.ll_gv);
        llimg = (LinearLayout) findViewById(R.id.ll_img);

        choseImg = (LinearLayout) findViewById(R.id.ll_choseImg);

        img1 = (ImageView) findViewById(R.id.cheliang_reduce1);
        img2 = (ImageView) findViewById(R.id.cheliang_reduce2);

        img3 = (ImageView) findViewById(R.id.fangdai_reduce1);
        img4 = (ImageView) findViewById(R.id.fangdai_reduce2);

        img5 = (ImageView) findViewById(R.id.geren_reduce1);

        img6 = (ImageView) findViewById(R.id.qiye_reduce1);
        img7 = (ImageView) findViewById(R.id.qiye_reduce2);
        img8 = (ImageView) findViewById(R.id.qiye_reduce3);
        img9 = (ImageView) findViewById(R.id.qiye_reduce4);

        img10 = (ImageView) findViewById(R.id.reduce1);
        img11 = (ImageView) findViewById(R.id.reduce2);

        shenfenImg1 = (ImageView) findViewById(R.id.shenfen_img1);
        shenfenImg2 = (ImageView) findViewById(R.id.shenfen_img2);

        shenfenDelImg1 = (ImageView) findViewById(R.id.shenfen_del1);
        shenfenDelImg2 = (ImageView) findViewById(R.id.shenfen_del2);

//        shenfenz,gerenziliao,geren,chedai1,chedai2,fangdai1,fangdai2,qiye1,qiye2,qiye3,qiye4;
        shenfenz = (LinearLayout) findViewById(R.id.ll_shenfenz);
        gerenziliao = (LinearLayout) findViewById(R.id.ll_gerenxinyong);
        geren = (LinearLayout) findViewById(R.id.ll_geren);
        chedai1 = (LinearLayout) findViewById(R.id.ll_cheliang1);
        chedai2 = (LinearLayout) findViewById(R.id.ll_cheliang2);
        fangdai1 = (LinearLayout) findViewById(R.id.ll_fangdai1);
        fangdai2 = (LinearLayout) findViewById(R.id.ll_fangdai2);
        qiye1 = (LinearLayout) findViewById(R.id.ll_qiye1);
        qiye2 = (LinearLayout) findViewById(R.id.ll_qiye2);
        qiye3 = (LinearLayout) findViewById(R.id.ll_qiye3);
        qiye4 = (LinearLayout) findViewById(R.id.ll_qiye4);
//        tvShenfenz,tvXinyong,tvCheliang1,tvCheliang2,tvFangdai1,tvFangdai2,tvGeren1,tvQiye1,tvQiye2,tvQiye3,tvQiye4
        tvShenfenz = (TextView) findViewById(R.id.shenfenz_txt);
        tvXinyong = (TextView) findViewById(R.id.xinyong_txt);
        tvCheliang1 = (TextView) findViewById(R.id.cheliang_txt1);
        tvCheliang2 = (TextView) findViewById(R.id.cheliang_txt2);
        tvFangdai1 = (TextView) findViewById(R.id.fangdai_txt1);
        tvFangdai2 = (TextView) findViewById(R.id.fangdai_txt2);
        tvGeren1 = (TextView) findViewById(R.id.geren_txt1);
        tvQiye1 = (TextView) findViewById(R.id.qiye_txt1);
        tvQiye2 = (TextView) findViewById(R.id.qiye_txt2);
        tvQiye3 = (TextView) findViewById(R.id.qiye_txt3);
        tvQiye4 = (TextView) findViewById(R.id.qiye_txt4);
        zhengming = (TextView) findViewById(R.id.txt_zhenming);


        adapter = new IntroduceLoanAdapter(this);

        myGridView1 = (MyGridView) findViewById(R.id.gv_01);
        myGridView2 = (MyGridView) findViewById(R.id.gv_02);
        myGridView3 = (MyGridView) findViewById(R.id.gv_03);
        myGridView4 = (MyGridView) findViewById(R.id.gv_04);
        myGridView5 = (MyGridView) findViewById(R.id.gv_05);
        myGridView6 = (MyGridView) findViewById(R.id.gv_06);
        myGridView7 = (MyGridView) findViewById(R.id.gv_07);
        myGridView8 = (MyGridView) findViewById(R.id.gv_08);
        myGridView9 = (MyGridView) findViewById(R.id.gv_09);
        myGridView10 = (MyGridView) findViewById(R.id.gv_10);
        myGridView1.setAdapter(adapter);

        mDataPicker = (DatePicker)findViewById(R.id.datePicker);
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);



        back.setVisibility(View.VISIBLE);
        menu.setVisibility(View.VISIBLE);
        back.setText("财富中国");
        title.setText("我要借款");
        menu.setText("下一步");
        menu.setTextColor(Color.RED);
        cancel.setVisibility(View.GONE);

    }

    @Override
    public void setUpLisener() {
        commit.setOnClickListener(onClickListener);
        cancel.setOnClickListener(onClickListener);
        select.setOnClickListener(onClickListener);
        tv04.setOnClickListener(onClickListener);
        tv05.setOnClickListener(onClickListener);
        tv06.setOnClickListener(onClickListener);
        tv07.setOnClickListener(onClickListener);
        tv08.setOnClickListener(onClickListener);
        tv09.setOnClickListener(onClickListener);
        tv10.setOnClickListener(onClickListener);
        tv11.setOnClickListener(onClickListener);
        tv12.setOnClickListener(onClickListener);
        tv13.setOnClickListener(onClickListener);
        tv14.setOnClickListener(onClickListener);
        tv15.setOnClickListener(onClickListener);
        tv16.setOnClickListener(onClickListener);
        tv081.setOnClickListener(onClickListener);

        tv161.setOnClickListener(onClickListener);

        //个人类借款
        tv120.setOnClickListener(onClickListener);
        tv121.setOnClickListener(onClickListener);
        tv122.setOnClickListener(onClickListener);

        //车辆贷款
        tv221.setOnClickListener(onClickListener);
        tv222.setOnClickListener(onClickListener);

        //房产类借款
        tv320.setOnClickListener(onClickListener);
        tv321.setOnClickListener(onClickListener);

        //企业贷款
        tv417.setOnClickListener(onClickListener);
        tv418.setOnClickListener(onClickListener);

        tv421.setOnClickListener(this);

        tvTijiao.setOnClickListener(onClickListener);
        tianjia.setOnClickListener(onClickListener);

        img1.setOnClickListener(onClickListener);
        img2.setOnClickListener(onClickListener);
        img3.setOnClickListener(onClickListener);
        img4.setOnClickListener(onClickListener);
        img5.setOnClickListener(onClickListener);
        img6.setOnClickListener(onClickListener);
        img7.setOnClickListener(onClickListener);
        img8.setOnClickListener(onClickListener);
        img9.setOnClickListener(onClickListener);
        img10.setOnClickListener(onClickListener);
        img11.setOnClickListener(onClickListener);

        shenfenImg1.setOnClickListener(onClickListener);
        shenfenImg2.setOnClickListener(onClickListener);

        shenfenDelImg1.setOnClickListener(onClickListener);
        shenfenDelImg2.setOnClickListener(onClickListener);

        tvShenfenz.setOnClickListener(onClickListener);
        tvXinyong.setOnClickListener(onClickListener);
        tvCheliang1.setOnClickListener(onClickListener);
        tvCheliang2.setOnClickListener(onClickListener);
        tvFangdai1.setOnClickListener(onClickListener);
        tvFangdai2.setOnClickListener(onClickListener);
        tvGeren1.setOnClickListener(onClickListener);
        tvQiye1.setOnClickListener(onClickListener);
        tvQiye2.setOnClickListener(onClickListener);
        tvQiye3.setOnClickListener(onClickListener);
        tvQiye4.setOnClickListener(onClickListener);


        back.setOnClickListener(onClickListener);
        menu.setOnClickListener(onClickListener);

    }

    public void fillView(List<String> content) {
        list = new ArrayList<Pickers>();
        for (int i = 0; i < content.size(); i++) {
            list.add(new Pickers(content.get(i), null));
            pickerScrollView.setData(list);
            pickerScrollView.setSelected(0);

        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int flag1 = 0;
            String format = "";
//            TimePickerView.Type type = null;
            switch (v.getId()) {
                case R.id.tv_cancel:
                    rl01.setVisibility(View.GONE);
                    break;
                case R.id.tv_true:
                    rl01.setVisibility(View.GONE);
                    break;
                //选择借款类别
                case R.id.tv_04:
                    strings.clear();
                    strings.add("房产类借款");
                    strings.add("车辆类借款");
                    strings.add("个人类借款");
                    strings.add("企业类借款");
                    tv04.setText(strings.get(2));
                    init();
                    cleanData();
                    pickerListener = new PickerScrollView.onSelectListener() {
                        @Override
                        public void onSelect(Pickers pickers) {
                            tv04.setText(pickers.getShowConetnt());
                            init();
                        }
                    };
                    pickerScrollView.setOnSelectListener(pickerListener);


                    if (strings != null && strings.size() > 0) {
                        fillView(strings);
                        rl01.setVisibility(View.VISIBLE);
                    }
                    break;
                //选择借款类型
                case R.id.tv_05:
                    strings.clear();
                    if (tv04.getText().toString().equals("")) {
                        dialog1("请先选择借款类别");
                        break;
                    }

                    pickerListener = new PickerScrollView.onSelectListener() {
                        @Override
                        public void onSelect(Pickers pickers) {
                            tv05.setText(pickers.getShowConetnt());
                        }
                    };
                    pickerScrollView.setOnSelectListener(pickerListener);
                    if (tv04.getText().toString().equals("房产类借款")) {
                        strings.add("现金赎楼");
                        strings.add("房屋装修");
                        strings.add("红本抵押");
                        strings.add("月供贷款");
                    }
                    if (tv04.getText().toString().equals("车辆类借款")) {
                        strings.add("车辆抵押");
                        strings.add("以租代购");
                        strings.add("短期周转");
                        tv05.setText(strings.get(1));
                        fillView(strings);
                        rl01.setVisibility(View.VISIBLE);
                        break;
                    }
                    if (tv04.getText().toString().equals("个人类借款")) {
                        strings.add("助学贷款");
                        strings.add("医疗贷款");
                        strings.add("高端消费");
                        strings.add("资金周转");
                    }
                    if (tv04.getText().toString().equals("企业类借款")) {
                        strings.add("短期拆借");
                        strings.add("企业采购");
                        strings.add("企业扩张");
                        tv05.setText(strings.get(1));
                        fillView(strings);
                        rl01.setVisibility(View.VISIBLE);
                        break;
                    }
                    tv05.setText(strings.get(2));
                    fillView(strings);
                    rl01.setVisibility(View.VISIBLE);
                    break;


                //选择还款方式
                case R.id.tv_08:
                    if (tv04.getText().toString().equals("")) {
                        dialog1("请先选择借款类别");
                        break;
                    }
                    tv081.setText("");
                    setSelectLintener(tv08);
                    strings.add("一次性");
                    strings.add("先付息到期还本");
                    strings.add("按月等额本息");
                    strings.add("按月付息到期还本");
                    tv08.setText(strings.get(2));

                    fillView(strings);
                    rl01.setVisibility(View.VISIBLE);
                    break;
                //借款期限
                case R.id.tv_081:
                    if (tv04.getText().toString().equals("")) {
                        dialog1("请先选择借款类别");
                        break;
                    }
                    if (tv08.getText().equals("")) {
                        dialog1("请先选择还款方式");
                        break;
                    }
                    setSelectLintener(tv081);
                    if (tv08.getText().toString().equals("一次性")) {
                        strings.add("7天");
                        strings.add("30天");
                    } else {
                        strings.add("24月");
                        strings.add("2月");
                        strings.add("6月");
                        strings.add("12月");
                        strings.add("36月");
                        strings.add("3月");
                    }
                    tv081.setText(strings.get(1));
                    fillView(strings);
                    rl01.setVisibility(View.VISIBLE);
                    break;

                //选择竞标时间
                case R.id.tv_09:

                    if (tv04.getText().toString().equals("")) {
                        dialog1("请先选择借款类别");
                        break;
                    }
                    setSelectLintener(tv09);
                    strings.add("1天");
                    strings.add("5天");
                    strings.add("7天");
                    strings.add("9天");
                    strings.add("3天");
                    strings.add("6天");
                    strings.add("10天");
                    strings.add("4天");
                    strings.add("8天");
                    strings.add("2天");
                    tv09.setText(strings.get(0));

                    fillView(strings);
                    rl01.setVisibility(View.VISIBLE);
                    break;
                //选择性别
                case R.id.tv_10:
                    strings.clear();
                    if (tv04.getText().toString().equals("")) {
                        dialog1("请先选择借款类别");
                        break;
                    }
                    setSelectLintener(tv10);
                    strings.add("男");
                    strings.add("女");
                    tv10.setText(strings.get(0));
                    fillView(strings);
                    rl01.setVisibility(View.VISIBLE);
                    break;
                //选择学历
                case R.id.tv_12:
                    strings.clear();
                    if (tv04.getText().toString().equals("")) {
                        dialog1("请先选择借款类别");
                        break;
                    }
                    setSelectLintener(tv12);
                    strings.add("中专");
                    strings.add("大专");
                    strings.add("本科");
                    strings.add("本科以上");

                    tv12.setText(strings.get(2));

                    fillView(strings);
                    rl01.setVisibility(View.VISIBLE);
                    break;
                //选择工作性质
                case R.id.tv_14:
                    if (tv04.getText().toString().equals("")) {
                        dialog1("请先选择借款类别");
                        break;
                    }
                    setSelectLintener(tv14);
                    strings.add("授薪人士");
                    strings.add("个体经营者");
                    strings.add("企业家");
                    strings.add("其它");
                    tv14.setText(strings.get(2));
                    fillView(strings);
                    rl01.setVisibility(View.VISIBLE);
                    break;
                //选择资产信息
                case R.id.tv_15:
                    if (tv04.getText().toString().equals("")) {
                        dialog1("请先选择借款类别");
                        break;
                    }
                    setSelectLintener(tv15);
                    strings.add("房产");
                    strings.add("车产");
                    strings.add("无");
                    tv15.setText(strings.get(1));

                    fillView(strings);
                    rl01.setVisibility(View.VISIBLE);
                    break;
//                选择月薪输入
                case R.id.tv_16:
                    setSelectLintener(tv16);
                    strings.add("0~5000元");
                    strings.add("5000~8000元");
                    strings.add("10000元以上");
                    tv16.setText(strings.get(1));
                    fillView(strings);
                    rl01.setVisibility(View.VISIBLE);
                    break;
                case R.id.tv_161:
                    setSelectLintener(tv161);
                    strings.add("1~3万");
                    strings.add("20~50万");
                    strings.add("100万以上");
                    tv161.setText(strings.get(1));
                    fillView(strings);
                    rl01.setVisibility(View.VISIBLE);
                    break;
                case R.id.tv_120:
                    setSelectLintener(tv120);
                    strings.add("普通员工");
                    strings.add("中层管理人员");
                    strings.add("高成管理人员");
                    tv120.setText(strings.get(1));

                    fillView(strings);
                    rl01.setVisibility(View.VISIBLE);
                    break;
                case R.id.tv_121:
                    setSelectLintener(tv121);
                    strings.add("是");
                    strings.add("否");
                    tv121.setText(strings.get(0));

                    fillView(strings);
                    rl01.setVisibility(View.VISIBLE);
                    break;
                case R.id.tv_122:
                    setSelectLintener(tv122);
                    strings.add("银行代发");
                    strings.add("公司转账");
                    strings.add("发现金");
                    tv122.setText(strings.get(1));
                    fillView(strings);
                    rl01.setVisibility(View.VISIBLE);
                    break;
                case R.id.tv_320:
                    setSelectLintener(tv320);
                    strings.add("本人所有");
                    strings.add("配偶所有");
                    tv320.setText(strings.get(0));
                    fillView(strings);
                    rl01.setVisibility(View.VISIBLE);
                    break;
                case R.id.tv_321:
                    setSelectLintener(tv321);
                    strings.add("已公正");
                    strings.add("已抵押");
                    strings.add("已出批复");
                    tv321.setText(strings.get(1));
                    fillView(strings);
                    rl01.setVisibility(View.VISIBLE);
                    break;
                case R.id.tv_221:
                    setSelectLintener(tv221);
                    strings.add("优");
                    strings.add("良");
                    strings.add("一般");
                    tv221.setText(strings.get(1));
                    fillView(strings);
                    rl01.setVisibility(View.VISIBLE);
                    break;
                case R.id.tv_222:
                    if (tv04.getText().toString().equals("")) {
                        dialog1("请先选择车辆状态");
                        break;
                    }
                    setSelectLintener(tv222);
                    strings.add("已质押");
                    strings.add("已抵押");
                    tv222.setText(strings.get(0));
                    fillView(strings);
                    rl01.setVisibility(View.VISIBLE);
                    break;
                case R.id.tv_417:
                    setSelectLintener(tv417);
                    strings.add("800~1000万");
                    strings.add("1500~2000万");
                    strings.add("2000万以上");
                    tv417.setText(strings.get(1));
                    fillView(strings);
                    rl01.setVisibility(View.VISIBLE);
                    break;
                case R.id.tv_418:
                    setSelectLintener(tv418);
                    strings.add("个人独资");
                    strings.add("有限公司");
                    strings.add("股份公司");
                    tv418.setText(strings.get(1));
                    fillView(strings);
                    rl01.setVisibility(View.VISIBLE);
                    break;
                case R.id.cheliang_reduce1:
                    chedai1.setVisibility(View.GONE);
                    break;
                case R.id.cheliang_reduce2:
                    chedai2.setVisibility(View.GONE);
                    break;
                case R.id.fangdai_reduce1:
                    fangdai1.setVisibility(View.GONE);
                    break;
                case R.id.fangdai_reduce2:
                    fangdai2.setVisibility(View.GONE);
                    break;
                case R.id.reduce1:
                    shenfenz.setVisibility(View.GONE);
                    break;
                case R.id.reduce2:
                    gerenziliao.setVisibility(View.GONE);
                    break;
                case R.id.geren_reduce1:
                    geren.setVisibility(View.GONE);
                    break;
                case R.id.qiye_reduce1:
                    qiye1.setVisibility(View.GONE);
                    break;
                case R.id.qiye_reduce2:
                    qiye2.setVisibility(View.GONE);
                    break;
                case R.id.qiye_reduce3:
                    qiye3.setVisibility(View.GONE);
                    break;
                case R.id.qiye_reduce4:
                    qiye4.setVisibility(View.GONE);
                    break;
                case R.id.shenfenz_txt:
                    cailiao.setVisibility(View.GONE);
                    choseImg.setVisibility(View.VISIBLE);
                    llimg.setVisibility(View.VISIBLE);
                    llgv.setVisibility(View.GONE);
                    menu.setVisibility(View.GONE);
                    break;
                case R.id.xinyong_txt:
                    imgFlag = 4;
                    zhengming.setText("上传个人信用证明材料");
                    cailiao.setVisibility(View.GONE);
                    choseImg.setVisibility(View.VISIBLE);
                    llimg.setVisibility(View.GONE);
                    llgv.setVisibility(View.VISIBLE);
                    menu.setVisibility(View.GONE);
                    showShortToast(imgFlag + "");

                    break;
                case R.id.cheliang_txt1:
                    imgFlag = 5;
                    zhengming.setText("上传机动车证明材料");
                    showShortToast(imgFlag + "");
                    cailiao.setVisibility(View.GONE);
                    llimg.setVisibility(View.GONE);
                    llgv.setVisibility(View.VISIBLE);
                    choseImg.setVisibility(View.VISIBLE);
                    menu.setVisibility(View.GONE);

                    break;
                case R.id.cheliang_txt2:
                    imgFlag = 6;
                    zhengming.setText("上传行驶证证明材料");
                    showShortToast(imgFlag + "");
                    cailiao.setVisibility(View.GONE);
                    llimg.setVisibility(View.GONE);
                    llgv.setVisibility(View.VISIBLE);
                    choseImg.setVisibility(View.VISIBLE);
                    menu.setVisibility(View.GONE);

                    break;
                case R.id.fangdai_txt1:
                    imgFlag = 7;
                    zhengming.setText("上传房屋所有权证明材料");
                    showShortToast(imgFlag + "");
                    cailiao.setVisibility(View.GONE);
                    choseImg.setVisibility(View.VISIBLE);
                    llimg.setVisibility(View.GONE);
                    llgv.setVisibility(View.VISIBLE);
                    menu.setVisibility(View.GONE);

                    break;
                case R.id.fangdai_txt2:
                    imgFlag = 8;
                    zhengming.setText("上传国有土地使用证明材料");
                    showShortToast(imgFlag + "");
                    cailiao.setVisibility(View.GONE);
                    choseImg.setVisibility(View.VISIBLE);
                    llimg.setVisibility(View.GONE);
                    llgv.setVisibility(View.VISIBLE);
                    menu.setVisibility(View.GONE);

                    break;
                case R.id.geren_txt1:
                    imgFlag = 9;
                    zhengming.setText("上传工资卡银行证明材料");

                    showShortToast(imgFlag + "");
                    cailiao.setVisibility(View.GONE);
                    choseImg.setVisibility(View.VISIBLE);
                    llimg.setVisibility(View.GONE);
                    llgv.setVisibility(View.VISIBLE);
                    menu.setVisibility(View.GONE);

                    break;
                case R.id.qiye_txt1:
                    imgFlag = 10;
                    zhengming.setText("上传营业执照证明材料");
                    showShortToast(imgFlag + "");
                    cailiao.setVisibility(View.GONE);
                    choseImg.setVisibility(View.VISIBLE);
                    llimg.setVisibility(View.GONE);
                    llgv.setVisibility(View.VISIBLE);
                    menu.setVisibility(View.GONE);

                    break;
                case R.id.qiye_txt2:
                    imgFlag = 11;
                    zhengming.setText("上传组织机构代码证明材料");
                    showShortToast(imgFlag + "");
                    cailiao.setVisibility(View.GONE);
                    choseImg.setVisibility(View.VISIBLE);
                    llimg.setVisibility(View.GONE);
                    llgv.setVisibility(View.VISIBLE);
                    menu.setVisibility(View.GONE);

                    break;
                case R.id.qiye_txt3:
                    imgFlag = 12;
                    zhengming.setText("上传税务登记证明材料");
                    showShortToast(imgFlag + "");
                    cailiao.setVisibility(View.GONE);
                    choseImg.setVisibility(View.VISIBLE);
                    llimg.setVisibility(View.GONE);
                    llgv.setVisibility(View.VISIBLE);

                    break;
                case R.id.qiye_txt4:
                    imgFlag = 13;
                    zhengming.setText("上传年度财务报表证明材料");
                    showShortToast(imgFlag + "");
                    cailiao.setVisibility(View.GONE);
                    choseImg.setVisibility(View.VISIBLE);
                    llimg.setVisibility(View.GONE);
                    llgv.setVisibility(View.VISIBLE);
                    menu.setVisibility(View.GONE);

                    break;
                case R.id.shenfen_img1:
                    imgFlag = 1;
                    showDialog();
                    break;
                case R.id.shenfen_img2:
                    imgFlag = 2;
                    showDialog();
                    break;
                case R.id.shenfen_del1:
                    ImageWithGlideUtils.lodeFromUrlRoundTransformImg(null, shenfenImg1, IntroduceLoanActivity.this);
                    shenfenDelImg1.setImageResource(R.color.app_base_color);
                    break;
                case R.id.shenfen_del2:
                    ImageWithGlideUtils.lodeFromUrlRoundTransformImg(null, shenfenImg2, IntroduceLoanActivity.this);
                    shenfenDelImg2.setImageResource(R.color.app_base_color);
                    break;
                case R.id.txt_tijiao2:
                    choseImg.setVisibility(View.GONE);
                    cailiao.setVisibility(View.VISIBLE);
                    break;
                case R.id.txt_tianjia:
                    showDialog();
                    break;
                // 拍照
                case R.id.btn_gono_xj:
                    flag1 = 1;
                    dialog1("请从相册中选取");
                    break;
                // 从相册选取
                case R.id.btn_auto_xj:
                    if (flag1 != 1) {
                        flag1 = 2;
                    }
                    dialog.dismiss();
                    GetPhotoAndUpload(flag1);
                    break;
                // 关闭对话框
                case R.id.btn_cancel:
                    dialog.dismiss();
                    break;
                case R.id.tv_421:
//                    getData();
//                    showShortToast("ssssss");
//                    type = TimePickerView.Type.YEAR_MONTH_DAY;
//                    format = "yyyy-MM-dd";
//                    Util.alertTimerPicker(IntroduceLoanActivity.this, type, format, new Util.TimerPickerCallBack() {
//                        @Override
//                        public void onTimeSelect(String date) {
//                            Toast.makeText(IntroduceLoanActivity.this, date, Toast.LENGTH_SHORT).show();
//                        }
//                    });
                    break;

                case R.id.tv_menu:
//                    if (judgeData() && !tv04.getText().equals("")) {
//                        sl.setVisibility(View.GONE);
//                        cailiao.setVisibility(View.VISIBLE);
//                        back.setText("我要借款");
//                        title.setText("上传材料");
//                        if (tv04.getText().toString().equals("个人类借款")) {
//                            gerendai.setVisibility(View.VISIBLE);
//                        }
//                        if (tv04.getText().toString().equals("企业类借款")) {
//                            qiyedai.setVisibility(View.VISIBLE);
//                        }
//                        if (tv04.getText().toString().equals("房产类借款")) {
//                            fangdai.setVisibility(View.VISIBLE);
//                        }
//                        if (tv04.getText().toString().equals("车辆类借款")) {
//                            chedai.setVisibility(View.VISIBLE);
//                        }
//                        flag = 1;
//                        if(menuFlag == 0){
//                            menu.setVisibility(View.GONE);
//                        }
////                        if (menuFlag == 0) {
////                            menu.setText("删除");
////                            changeStute(View.GONE);
////                            menuFlag = 1;
////                            break;
////                        }
////                        if (menuFlag == 1) {
////                            menu.setText("取消");
////                            changeStute(View.VISIBLE);
////                            menuFlag = 0;
////                            break;
////                        }
//
//                    }
                    if (judgeData()) {
                    Intent intent = new Intent(IntroduceLoanActivity.this, IntroduceData.class);

                    intent.putExtra("type", tv04.getText().toString());
//                    System.out.println(tv04.getText().toString());
                    startActivity(intent);
                    }
                    break;

                case R.id.txt_tijiao:
                    setDatas();
                    JumpCenter.JumpActivity(IntroduceLoanActivity.this, ValidateLoan.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, false);
                    break;
                case R.id.tv_back:
                    menu.setVisibility(View.VISIBLE);
                    if (flag == 1) {
                        sl.setVisibility(View.VISIBLE);
                        cailiao.setVisibility(View.GONE);
                        chedai.setVisibility(View.GONE);
                        fangdai.setVisibility(View.GONE);
                        gerendai.setVisibility(View.GONE);
                        qiyedai.setVisibility(View.GONE);
                        menuFlag = 0;
                        back.setText("财富中国");
                        title.setText("我要借款");
                        menu.setText("下一步");
                    } else {
                        IntroduceLoanActivity.this.finish();
                    }
                    flag = 0;
                    break;
            }
        }
    };


    private void init() {
        tv05.setText("");
        System.out.println(tv04.getText().toString() + "--------------------------");
        if (tv04.getText().toString().equals("企业类借款")) {
            ll04.setVisibility(View.GONE);
            ll05.setVisibility(View.GONE);
            introduce1.setVisibility(View.GONE);
            introduce2.setVisibility(View.GONE);
            introduce3.setVisibility(View.GONE);
            introduce4.setVisibility(View.VISIBLE);
        }
        if (tv04.getText().toString().equals("房产类借款")) {
            ll04.setVisibility(View.VISIBLE);
            ll05.setVisibility(View.VISIBLE);
            introduce1.setVisibility(View.GONE);
            introduce2.setVisibility(View.GONE);
            introduce3.setVisibility(View.VISIBLE);
            introduce4.setVisibility(View.GONE);
        }
        if (tv04.getText().toString().equals("个人类借款")) {
            ll04.setVisibility(View.VISIBLE);
            ll05.setVisibility(View.GONE);
            introduce1.setVisibility(View.VISIBLE);
            introduce2.setVisibility(View.GONE);
            introduce3.setVisibility(View.GONE);
            introduce4.setVisibility(View.GONE);
        }
        if (tv04.getText().toString().equals("车辆类借款")) {
            ll04.setVisibility(View.VISIBLE);
            ll05.setVisibility(View.VISIBLE);
            introduce1.setVisibility(View.GONE);
            introduce2.setVisibility(View.VISIBLE);
            introduce3.setVisibility(View.GONE);
            introduce4.setVisibility(View.GONE);
        }
    }


    PickerScrollView.onSelectListener pickerListener;

    /**
     * 清空数据
     */
    private void cleanData() {
        tv05.setText("");

        tv16.setText("");
        tv161.setText("");

        tv117.setText("");
        tv118.setText("");
        tv119.setText("");
        tv120.setText("");
        tv121.setText("");
        tv122.setText("");

        tv217.setText("");
        tv218.setText("");
        tv219.setText("");
        tv220.setText("");
        tv221.setText("");
        tv222.setText("");


        tv317.setText("");
        tv318.setText("");
        tv319.setText("");
        tv320.setText("");
        tv321.setText("");

        tv417.setText("");
        tv418.setText("");
        tv419.setText("");
        tv420.setText("");
        tv421.setText("");
    }

    /**
     * 更改删除栏状态
     *
     * @param stu
     */
    private void changeStute(int stu) {
        img1.setVisibility(stu);
        img2.setVisibility(stu);
        img3.setVisibility(stu);
        img4.setVisibility(stu);
        img5.setVisibility(stu);
        img6.setVisibility(stu);
        img7.setVisibility(stu);
        img8.setVisibility(stu);
        img9.setVisibility(stu);
        img10.setVisibility(stu);
        img11.setVisibility(stu);

    }

    private boolean debug = true;

    /**
     * 判断数据值
     */
    private boolean judgeData() {

        if (debug) {

            if (tv05.getText().toString().equals("")) {
                dialog1("请选择借款类型");
                return false;
            } else {
                // 判断借款金额  （输）  （最小1w最大5bw整出1k）
                String s = tv06.getText().toString();
                if (s.equals("")) {
                    dialog1("请输入金额！");
                    return false;

                } else {
                    if (!(Integer.parseInt(s) <= 5000000 && Integer.parseInt(s) >= 10000 && Integer.parseInt(s) % 1000 == 0)) {
                        dialog1("金额最小1万，最大5百万且是1千的整数倍！");
                        return false;
                    } else {
                        //  年利率    输  （最高不能超过24%）
                        String s1 = tv07.getText().toString();
                        if (s1.equals("")) {
                            dialog1("请输入年利率！");
                            return false;
                        } else {
                            if (Float.parseFloat(s1) >= 25) {
                                dialog1("年利率不能大于24%");
                                return false;
                            }
                        }
                        if (tv08.getText().toString().equals("")) {
                            dialog1("请选择还款方式");
                            return false;
                        }
                        if (tv081.getText().toString().equals("")) {
                            dialog1("请选择还款期限");
                            return false;
                        }
                        if (tv09.getText().toString().equals("")) {
                            dialog1("请选择竞标时间");
                            return false;
                        }
                        if (tv10.getText().toString().equals("")) {
                            dialog1("请选择性别");
                            return false;
                        }
                        if (tv11.getText().toString().equals("")) {
                            dialog1("请输入年龄");
                            return false;
                        }
                        if (Integer.parseInt(tv11.getText().toString()) >= 70) {
                            dialog1("年龄最大为70岁");
                            return false;
                        }
                        if (Integer.parseInt(tv11.getText().toString()) <= 16) {
                            dialog1("年龄最小为16岁");
                            return false;
                        }
                        if (tv12.getText().toString().equals("")) {
                            dialog1("请选择学历");
                            return false;
                        }
                        if (tv13.getText().toString().equals("")) {
                            dialog1("请输入地区");
                            return false;
                        }
                        if (tv14.getText().toString().equals("")) {
                            dialog1("请选择工作性质");
                            return false;
                        }
                        if (tv15.getText().toString().equals("")) {
                            dialog1("请选择个人资产信息");
                            return false;
                        }
                        if (tv04.getText().toString().equals("个人类借款")) {
                            if (tv16.getText().toString().equals("")) {
                                dialog1("请选择月薪收入");
                                return false;
                            }
                            if (tv117.getText().toString().equals("")) {
                                dialog1("请输入公司名称");
                                return false;
                            }
                            if (tv118.getText().toString().equals("")) {
                                dialog1("请输入公司地址");
                                return false;
                            }
                            if (tv119.getText().toString().equals("")) {
                                dialog1("请输入公司主营业务");
                                return false;
                            }
                            if (tv120.getText().toString().equals("")) {
                                dialog1("请选择在职岗位");
                                return false;
                            }
                            if (tv121.getText().toString().equals("")) {
                                dialog1("请选择是否缴纳社保");
                                return false;
                            }
                            if (tv122.getText().toString().equals("")) {
                                dialog1("请选择工资发放形式");
                                return false;
                            }
                        }
                        if (tv04.getText().toString().equals("车辆类借款")) {
                            if (tv16.getText().toString().equals("")) {
                                dialog1("请选择月薪收入");
                                return false;
                            }
                            if (tv161.getText().toString().equals("")) {
                                dialog1("请选择月营业额收入");
                                return false;
                            }
                            if (tv217.getText().toString().equals("")) {
                                dialog1("请输入车辆品牌");
                                return false;
                            }
                            if (tv218.getText().toString().equals("")) {
                                dialog1("请输入车辆型号");
                                return false;
                            }
                            if (tv219.getText().toString().equals("")) {
                                dialog1("请输入车辆归属地");
                                return false;
                            }
                            if (tv220.getText().toString().equals("")) {
                                dialog1("请输入车辆市场评估价");
                                return false;
                            }
                            if (tv221.getText().toString().equals("")) {
                                dialog1("请选择车辆状况");
                                return false;
                            }
                            if (tv222.getText().toString().equals("")) {
                                dialog1("请选择车辆状态");
                                return false;
                            }
                        }
                        if (tv04.getText().toString().equals("房产类借款")) {
                            if (tv16.getText().toString().equals("")) {
                                dialog1("请选择月薪收入");
                                return false;
                            }
                            if (tv161.getText().toString().equals("")) {
                                dialog1("请选择月营业额收入");
                                return false;
                            }
                            if (tv317.getText().toString().equals("")) {
                                dialog1("请输入房产所在地");
                                return false;
                            }
                            if (tv318.getText().toString().equals("")) {
                                dialog1("请输入房产面积");
                                return false;
                            }
                            if (tv319.getText().toString().equals("")) {
                                dialog1("请输入房产评估价");
                                return false;
                            }
                            if (tv320.getText().toString().equals("")) {
                                dialog1("请选择房产所有人");
                                return false;
                            }
                            if (tv321.getText().toString().equals("")) {
                                dialog1("请选择房产状态");
                                return false;
                            }
                        }
                        if (tv04.getText().toString().equals("企业类借款")) {
                            if (tv417.getText().toString().equals("")) {
                                dialog1("请选择年营业额收入");
                                return false;
                            }
                            if (tv418.getText().toString().equals("")) {
                                dialog1("请选择企业类型");
                                return false;
                            }
                            if (tv419.getText().toString().equals("")) {
                                dialog1("请输入企业经营地");
                                return false;
                            }
                            if (tv420.getText().toString().equals("")) {
                                dialog1("请输入主营业务");
                                return false;
                            }
                            if (tv421.getText().toString().equals("")) {
                                dialog1("请输入企业成立时间");
                                return false;
                            } else if (isValidDate(tv421.getText().toString())) {
                                dialog1("请按格式输入：xxxx-xx-xx");
                                return false;
                            }
                        }
                    }
                }
            }
            setDatas();

        }
        return true;
    }

    private void setSelectLintener(final TextView v) {
        strings.clear();

        pickerListener = new PickerScrollView.onSelectListener() {
            @Override
            public void onSelect(Pickers pickers) {
                v.setText(pickers.getShowConetnt());
            }
        };
        pickerScrollView.setOnSelectListener(pickerListener);
    }

    /**
     * 弹窗
     */
    private void dialog1(String str) {
        ycDialogUtils.showSingleDialog("提示", str, new YCDialogUtils.MySingleBtnclickLisener() {
            @Override
            public void onBtnClick(View v) {
                ycDialogUtils.DismissMyDialog();
            }
        }, true);
    }

    private Dialog dialog;

    /**
     * 顯示自定義對話框
     */
    private void showDialog() {
        View view = LayoutInflater.from(this).inflate(
                R.layout.photo_choose_dialog, null);
        // 找到对应控件，注册事件
        Button btn_xj_goon = (Button) view.findViewById(R.id.btn_gono_xj);
        btn_xj_goon.setOnClickListener(onClickListener);
        Button btn_xj_auto = (Button) view.findViewById(R.id.btn_auto_xj);
        btn_xj_auto.setOnClickListener(onClickListener);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(onClickListener);
        LinearLayout ll_bg = (LinearLayout) view.findViewById(R.id.ll_01);
        ll_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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

    private void GetPhotoAndUpload(int flag) {

        //权限检查
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            showShortToast("无法拍照，请授予财富中国应用的拍照权限。");
            return;
        }

        // 拍照
        if (flag == 1) {
            Intent intentFromCapture = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            File path = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            File file = new File(path, IMAGE_FILE_NAME);
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(file));
            startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
        }
        // 相册选取
        if (flag == 2) {
            Intent intentFromGallery = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                intentFromGallery = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            } else {
                intentFromGallery = new Intent(Intent.ACTION_GET_CONTENT);
            }
            intentFromGallery.setType("image/*"); // 设置文件类型
            startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
        }

    }

    private Drawable drawable;
    private String file = "";

    /**
     * 保存裁剪之后的图片数据
     *
     * @param data
     */
    private String getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            showShortToast("转换图片");
            // 获得bitmap
            Bitmap photo = extras.getParcelable("data");
            drawable = new BitmapDrawable(this.getResources(), photo);
            byte[] base64 = Base64.encode(Bitmap2Bytes(photo), Base64.DEFAULT);
            file = "base64," + URLEncoder.encode(new String(base64));
            //上传
        }
        if (imgFlag == 1) {
            shenfenList.add(file);
        }
        if (imgFlag == 2) {
            shenfenList.add(file);
        }
        if (imgFlag == 4) {
            xinyongList.add(file);
            adapter.setImgList(xinyongList);
        }

        System.out.println(file);
        return file;
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 340);
        intent.putExtra("outputY", 340);
        intent.putExtra("return-data", true);
        showShortToast("裁剪");
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    private String imgDataStr;
    private List<String> shenfenList = new ArrayList<>();
    private List<String> gongziList = new ArrayList<>();
    private List<String> xinyongList = new ArrayList<>();
    private List<String> yinyeList = new ArrayList<>();
    private List<String> jigouList = new ArrayList<>();
    private List<String> shuiwuList = new ArrayList<>();
    private List<String> nianduList = new ArrayList<>();
    private List<String> fangwuList = new ArrayList<>();
    private List<String> guoyouList = new ArrayList<>();
    private List<String> jidongList = new ArrayList<>();
    private List<String> xingshiList = new ArrayList<>();

    List<String> strings1 = new ArrayList<>();
    List<String> strings2 = new ArrayList<>();
    List<String> strings3 = new ArrayList<>();

    private List<String> strings4 = new ArrayList<>();
    private List<String> strings5 = new ArrayList<>();
    private List<String> strings6 = new ArrayList<>();
    private List<String> strings7 = new ArrayList<>();
    private List<String> strings8 = new ArrayList<>();
    private List<String> strings9 = new ArrayList<>();
    List<String> strings10 = new ArrayList<>();


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 结果码不等于取消时候
        if (resultCode != Activity.RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    imgDataStr = data.getData().toString();
                    // data.getData()获得拍照返回的Uri
                    if (imgFlag == 1) {
                        ImageWithGlideUtils.lodeFromUrlRoundTransformImg(imgDataStr, shenfenImg1, IntroduceLoanActivity.this);
                        shenfenDelImg1.setImageResource(R.drawable.del);

                    }
                    if (imgFlag == 2) {
                        ImageWithGlideUtils.lodeFromUrlRoundTransformImg(imgDataStr, shenfenImg2, IntroduceLoanActivity.this);
                        shenfenDelImg2.setImageResource(R.drawable.del);
                    }

                    if (imgFlag == 4) {
                        strings1.add(imgDataStr);
                        adapter.addList(strings1);
                        adapter.notifyDataSetChanged();
                    }


                    startPhotoZoom(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    // 判断存储卡是否可以用，可用进行存储
                    String state = Environment.getExternalStorageState();
                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                        File path = Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

                        File tempFile = new File(path, IMAGE_FILE_NAME);

                        startPhotoZoom(Uri.fromFile(tempFile));
                    } else {
                        showShortToast(R.string.can_not_find_SD);
                    }
                    break;
                case RESULT_REQUEST_CODE: // 图片缩放完成后
                    if (data != null) {
                        getImageToView(data);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    LoanInfoBean loanInfoBean = new LoanInfoBean();

    private void setDatas() {
        LoanInfoBean loanInfo = new LoanInfoBean();
        if (tv04.getText().toString().equals("房产类借款")) {
            loanInfo.setLoan_purpose("1");
        }
        if (tv04.getText().toString().equals("车辆类借款")) {
            loanInfo.setLoan_purpose("2");
        }
        if (tv04.getText().toString().equals("个人类借款")) {
            loanInfo.setLoan_purpose("3");
        }
        if (tv04.getText().toString().equals("企业类借款")) {
            loanInfo.setLoan_purpose("4");
        }
        loanInfo.setBorrow_title(tv05.getText().toString());

        loanInfo.setLoan_amount(tv06.getText().toString());
        loanInfo.setYear_of_rate(tv07.getText().toString());
        if (tv08.getText().toString().equals("一次性")) {
            loanInfo.setRepaymen_method("4");
        }
        if (tv08.getText().toString().equals("先付息到期还本")) {
            loanInfo.setRepaymen_method("3");
        }
        if (tv08.getText().toString().equals("按月等额本息")) {
            loanInfo.setRepaymen_method("5");
        }
        if (tv08.getText().toString().equals("按月付息到期还本")) {
            loanInfo.setBorrow_title("11");
        }
        if (tv081.getText().toString().endsWith("天")) {
            if (tv081.getText().toString().length() < 2) {
                loanInfo.setLoan_term("10" + tv081.getText().toString().substring(0, 1));
            } else {
                loanInfo.setLoan_term("1" + tv081.getText().toString().substring(0, 2));
            }
        }
        if (tv081.getText().toString().endsWith("月")) {
            if (tv081.getText().toString().equals("3月")) {
                loanInfo.setLoan_term("30" + tv081.getText().toString().substring(0, 1));
            }
            if (tv081.getText().toString().equals("36月")) {
                loanInfo.setLoan_term("3" + tv081.getText().toString().substring(0, 2));
            }
        }

        if (tv09.getText().toString().length() < 3) {
            loanInfo.setBidding_date(tv09.getText().toString().substring(0, 1));
        } else {
            loanInfo.setBidding_date(tv09.getText().toString().substring(0, 2));
        }

        if (tv10.getText().toString().equals("男")) {
            loanInfo.setSex("true");
        }
        if (tv10.getText().toString().equals("女")) {
            loanInfo.setSex("false");
        }
        loanInfo.setAge(tv11.getText().toString());
        if (tv12.getText().toString().equals("中专")) {
            loanInfo.setQualification("1");
        }
        if (tv12.getText().toString().equals("大专")) {
            loanInfo.setQualification("2");
        }
        if (tv12.getText().toString().equals("本科")) {
            loanInfo.setQualification("3");
        }
        if (tv12.getText().toString().equals("本科以上")) {
            loanInfo.setQualification("4");
        }

        loanInfo.setArea(tv13.getText().toString());

        if (tv14.getText().toString().equals("受薪人士")) {
            loanInfo.setWorking_property("1");
        }

        if (tv14.getText().toString().equals("个体经营者")) {
            loanInfo.setWorking_property("2");

        }

        if (tv14.getText().toString().equals("企业家")) {
            loanInfo.setWorking_property("3");

        }

        if (tv14.getText().toString().equals("其它")) {
            loanInfo.setWorking_property("4");

        }
        if (tv15.getText().toString().equals("房产")) {
            loanInfo.setPersonal_asset_information("1");
        }
        if (tv15.getText().toString().equals("车产")) {
            loanInfo.setPersonal_asset_information("2");
        }
        if (tv15.getText().toString().equals("无")) {
            loanInfo.setPersonal_asset_information("3");
        }

        loanInfo.setCompany_name(tv117.getText().toString());
        loanInfo.setCompany_address(tv118.getText().toString());
        loanInfo.setCompany_business(tv119.getText().toString());

        if (tv120.getText().toString().equals("普通员工")) {
            loanInfo.setJob_position("1");
        }
        if (tv120.getText().toString().equals("中层管理人员")) {
            loanInfo.setJob_position("2");
        }
        if (tv120.getText().toString().equals("高层管理人员")) {
            loanInfo.setJob_position("3");
        }
        if (tv121.getText().toString().equals("是")) {
            loanInfo.setSocial_security("true");
        } else {
            loanInfo.setSocial_security("false");
        }
        if (tv122.getText().toString().equals("银行代发")) {
            loanInfo.setPayroll_form("1");
        }
        if (tv122.getText().toString().equals("公司转账")) {
            loanInfo.setPayroll_form("2");
        }
        if (tv122.getText().toString().equals("发现金")) {
            loanInfo.setPayroll_form("3");
        }

        loanInfo.setHouse_address(tv317.getText().toString());
        loanInfo.setHouse_area(tv318.getText().toString());
        loanInfo.setHouse_evaluate(tv319.getText().toString());
        loanInfo.setHouse_holder(tv320.getText().toString());
        if (tv321.getText().toString().equals("已公证")) {
            loanInfo.setHouse_state("1");
        }
        if (tv321.getText().toString().equals("已抵押")) {
            loanInfo.setHouse_state("2");
        }
        if (tv321.getText().toString().equals("已出批复")) {
            loanInfo.setHouse_state("3");
        }
        loanInfo.setCar_brand(tv217.getText().toString());
        loanInfo.setCar_version(tv218.getText().toString());
        loanInfo.setCar_gsd(tv219.getText().toString());
        loanInfo.setCar_evaluate(tv220.getText().toString());
        if (tv221.getText().toString().equals("优")) {
            loanInfo.setCar_status("1");
        }
        if (tv221.getText().toString().equals("良")) {
            loanInfo.setCar_status("2");
        }
        if (tv221.getText().toString().equals("一般")) {
            loanInfo.setCar_status("3");
        }
        if (tv222.getText().toString().equals("已质押")) {
            loanInfo.setCar_state("1");
        }
        if (tv222.getText().toString().equals("已抵押")) {
            loanInfo.setCar_state("2");
        }
        if (tv418.getText().toString().equals("个人独资")) {
            loanInfo.setEnterprise_type("1");
        }
        if (tv418.getText().toString().equals("有限公司")) {
            loanInfo.setEnterprise_type("2");
        }
        if (tv418.getText().toString().equals("股份公司")) {
            loanInfo.setEnterprise_type("3");
        }
        loanInfo.setEnterprise_address(tv419.getText().toString());
        loanInfo.setMain_business(tv420.getText().toString());
        loanInfo.setFounding_time(tv421.getText().toString());
        if (tv16.getText().toString().startsWith("0")) {
            loanInfo.setMonthly_income("1");
        }
        if (tv16.getText().toString().startsWith("5")) {
            loanInfo.setMonthly_income("2");
        }
        if (tv16.getText().toString().startsWith("1")) {
            loanInfo.setMonthly_income("3");
        }

        if (tv161.getText().toString().startsWith("1")) {
            loanInfo.setBusiness_income("1");
        }
        if (tv161.getText().toString().startsWith("5")) {
            loanInfo.setBusiness_income("2");
        }
        if (tv161.getText().toString().startsWith("2")) {
            loanInfo.setBusiness_income("3");
        }
        if (tv161.getText().toString().endsWith("上")) {
            loanInfo.setBusiness_income("4");
        }
        if (tv417.getText().toString().startsWith("8")) {
            loanInfo.setYear_income("1");
        }
        if (tv417.getText().toString().startsWith("1")) {
            loanInfo.setYear_income("2");
        }
        if (tv418.getText().toString().startsWith("2")) {
            loanInfo.setYear_income("3");
        }
        loanInfo.setBewrite(tv23.getText().toString());


        List<ImgArrayBean> proofMaterial = new ArrayList<>();
        List<ImgData> imgDataList;
        ImgArrayBean imgArray;
        ImgData imgData;
        List<String> str = new ArrayList<>();
        str.add("身份证");
        str.add("个人信用");
        str.add("房屋证件");
        str.add("国有机构证件");
        str.add("机动车证件");
        str.add("行驶证件");
        str.add("工资卡流水");
        str.add("营业证件");
        str.add("机构证件");
        str.add("税务证件");
        str.add("年度报表证件");
        System.out.println(str.toArray().toString());
        List<List<String>> arr = new ArrayList<>();
        if (shenfenList != null && shenfenList.size() > 0) {
            imgArray = new ImgArrayBean();
            imgDataList = new ArrayList<>();
            for (int i = 0; i < shenfenList.size(); i++) {
                imgData = new ImgData();
                imgData.setData(shenfenList.get(i));
                imgData.setType("png");
                imgDataList.add(imgData);
                System.out.println("身份证：" + shenfenList.get(i).toString());
            }
            imgArray.setName("身份证");
            imgArray.setImgDatas(imgDataList);
            proofMaterial.add(imgArray);
        }
        if (xinyongList != null && xinyongList.size() > 0) {
            imgArray = new ImgArrayBean();
            imgDataList = new ArrayList<>();
            for (int i = 0; i < xinyongList.size(); i++) {
                imgData = new ImgData();
                imgData.setData(xinyongList.get(i));
                imgData.setType("png");
                imgDataList.add(imgData);
                System.out.println(imgDataList.toString() + "----" + imgData);
            }
            imgArray.setName("个人信用证件");
            imgArray.setImgDatas(imgDataList);
            proofMaterial.add(imgArray);
        }
        if (fangwuList != null && fangwuList.size() > 0) {
            imgArray = new ImgArrayBean();
            imgDataList = new ArrayList<>();
            for (int i = 0; i < fangwuList.size(); i++) {
                imgData = new ImgData();
                imgData.setData(fangwuList.get(i));
                imgData.setType("png");
                imgDataList.add(imgData);
            }
            imgArray.setName("房产证件");
            imgArray.setImgDatas(imgDataList);
            proofMaterial.add(imgArray);
        }
        if (jidongList != null && jidongList.size() > 0) {
            imgArray = new ImgArrayBean();
            imgDataList = new ArrayList<>();
            for (int i = 0; i < jidongList.size(); i++) {
                imgData = new ImgData();
                imgData.setData(jidongList.get(i));
                imgData.setType("png");
                imgDataList.add(imgData);
            }
            imgArray.setName("机动车证件");
            imgArray.setImgDatas(imgDataList);
            proofMaterial.add(imgArray);
        }
        if (guoyouList != null && guoyouList.size() > 0) {
            imgArray = new ImgArrayBean();
            imgDataList = new ArrayList<>();
            for (int i = 0; i < guoyouList.size(); i++) {
                imgData = new ImgData();
                imgData.setData(guoyouList.get(i));
                imgData.setType("png");
                imgDataList.add(imgData);
            }
            imgArray.setName("国有证件");
            imgArray.setImgDatas(imgDataList);
            proofMaterial.add(imgArray);
        }
        if (xingshiList != null && xingshiList.size() > 0) {
            imgArray = new ImgArrayBean();
            imgDataList = new ArrayList<>();
            for (int i = 0; i < xingshiList.size(); i++) {
                imgData = new ImgData();
                imgData.setData(xingshiList.get(i));
                imgData.setType("png");
                imgDataList.add(imgData);
                System.out.println("存入数组的数据：" + xingshiList.get(i).toString());
            }
            imgArray.setName("身份证");
            imgArray.setImgDatas(imgDataList);
            proofMaterial.add(imgArray);
        }
        if (gongziList != null && gongziList.size() > 0) {
            imgArray = new ImgArrayBean();
            imgDataList = new ArrayList<>();
            for (int i = 0; i < gongziList.size(); i++) {
                imgData = new ImgData();
                imgData.setData(gongziList.get(i));
                imgData.setType("png");
                imgDataList.add(imgData);
            }
            imgArray.setName("工资卡流水");
            imgArray.setImgDatas(imgDataList);
            proofMaterial.add(imgArray);
        }
        if (yinyeList != null && yinyeList.size() > 0) {
            imgArray = new ImgArrayBean();
            imgDataList = new ArrayList<>();
            for (int i = 0; i < yinyeList.size(); i++) {
                imgData = new ImgData();
                imgData.setData(yinyeList.get(i));
                imgData.setType("png");
                imgDataList.add(imgData);
            }
            imgArray.setName("营业证件");
            imgArray.setImgDatas(imgDataList);
            proofMaterial.add(imgArray);
        }
        if (jigouList != null && jigouList.size() > 0) {
            imgArray = new ImgArrayBean();
            imgDataList = new ArrayList<>();
            for (int i = 0; i < jigouList.size(); i++) {
                imgData = new ImgData();
                imgData.setData(jigouList.get(i));
                imgData.setType("png");
                imgDataList.add(imgData);
            }
            imgArray.setName("机构证件");
            imgArray.setImgDatas(imgDataList);
            proofMaterial.add(imgArray);
        }
        if (shuiwuList != null && shuiwuList.size() > 0) {
            imgArray = new ImgArrayBean();
            imgDataList = new ArrayList<>();
            for (int i = 0; i < shuiwuList.size(); i++) {
                imgData = new ImgData();
                imgData.setData(shuiwuList.get(i));
                imgData.setType("png");
                imgDataList.add(imgData);
            }
            imgArray.setName("税务证件");
            imgArray.setImgDatas(imgDataList);
            proofMaterial.add(imgArray);
        }
        if (nianduList != null && nianduList.size() > 0) {
            imgArray = new ImgArrayBean();
            imgDataList = new ArrayList<>();
            for (int i = 0; i < nianduList.size(); i++) {
                imgData = new ImgData();
                imgData.setData(nianduList.get(i));
                imgData.setType("png");
                imgDataList.add(imgData);
            }
            imgArray.setName("报表证件");
            imgArray.setImgDatas(imgDataList);
            proofMaterial.add(imgArray);
        }
        System.out.println("arr 的大小:" + arr.size());
        System.out.println("终极数组！！" + proofMaterial.size());
        Gson gson = new GsonBuilder().create();
        result = gson.toJson(loanInfo);
        result1 = gson.toJson(proofMaterial);

        saveFile(result, "result.txt");
        saveFile(result1, "result1.txt");
        System.out.println(result);
        System.out.println(result1);
    }

    String result;
    String result1;
    String result2;

    public static void saveFile(String str, String str1) {
        String filePath = null;
        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (hasSDCard) { // SD卡根目录的hello.text
            filePath = Environment.getExternalStorageDirectory().toString() + File.separator + str1;
        } else  // 系统下载缓存根目录的hello.text
            filePath = Environment.getDownloadCacheDirectory().toString() + File.separator + str1;

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(str.getBytes());
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean isValidDate(String s){
        try {
            // 指定日期格式为四位年/两位月份/两位日期，注意yyyy-MM-dd其中MM为大写
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2004/02/29会被接受，并转换成2004/03/01
            dateFormat.setLenient(false);
            dateFormat.parse(s);
            return true;
        }catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }
    }
}
