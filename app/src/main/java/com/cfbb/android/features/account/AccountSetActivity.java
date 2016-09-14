package com.cfbb.android.features.account;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.app.MyApplication;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.state.BindingStateEnum;
import com.cfbb.android.commom.state.CertificationEnum;
import com.cfbb.android.commom.state.IsOpenGestureEnum;
import com.cfbb.android.commom.state.MainFragmentEnum;
import com.cfbb.android.commom.state.SetingEnum;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.commom.utils.image.ImageWithGlideUtils;
import com.cfbb.android.commom.utils.others.SPUtils;
import com.cfbb.android.db.user.UserBiz;
import com.cfbb.android.features.aboutus.AboutUsActivity;
import com.cfbb.android.features.aboutus.FeedBackActivity;
import com.cfbb.android.features.account.accountdetails.ModifyPasswordActivity;
import com.cfbb.android.features.account.changephone.ChangeMobilePhoneStepOneActivity;
import com.cfbb.android.features.authentication.RealNameAuthenticationActivity;
import com.cfbb.android.features.gesture.GestureEditActivity;
import com.cfbb.android.features.gesture.GestureSetActivity;
import com.cfbb.android.features.main.MainActivity;
import com.cfbb.android.features.webview.OtherActivity;
import com.cfbb.android.protocol.APIException;
import com.cfbb.android.protocol.RetrofitClient;
import com.cfbb.android.protocol.YCNetSubscriber;
import com.cfbb.android.protocol.bean.AccountSetInfoBean;
import com.cfbb.android.protocol.bean.UploadPhotoBean;
import com.cfbb.android.widget.YCLoadingBg;
import com.cfbb.android.widget.dialog.YCDialogUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URLEncoder;

/***
 * 账户设置
 */
public class AccountSetActivity extends BaseActivity {


    /**
     * 头像名称
     */
    private static final String IMAGE_FILE_NAME = "image.jpg";
    /**
     * 请求码
     */
    private static final int IMAGE_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int RESULT_REQUEST_CODE = 3;
    private static final int RZ_REQUEST_CODE = 4;
    private TextView tv_back;
    private TextView tv_title;

    private TextView tv_name;
    private ImageView iv_photo;
    private TextView tv_is_rz;
    private TextView tv_is_bind;
    private TextView tv_is_gesature_set;

    private RelativeLayout rl_01;
    private RelativeLayout rl_02;
    private RelativeLayout rl_03;
    private RelativeLayout rl_04;
    private RelativeLayout rl_05;
    private RelativeLayout rl_06;
    private RelativeLayout rl_07;
    private RelativeLayout rl_08;
    private RelativeLayout rl_09;
    private RelativeLayout rl_10;
    private YCLoadingBg ycLoadingBg;

    private YCDialogUtils ycDialogUtils;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_account_set);
    }

    @Override
    public void setUpViews() {

        ycDialogUtils = new YCDialogUtils(this);
        ycLoadingBg = (YCLoadingBg) findViewById(R.id.ycLoadingBg);

        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);

        tv_name = (TextView) findViewById(R.id.tv_06);
        iv_photo = (ImageView) findViewById(R.id.iv_01);
        tv_is_rz = (TextView) findViewById(R.id.tv_02);
        tv_is_bind = (TextView) findViewById(R.id.tv_03);
        tv_is_gesature_set = (TextView) findViewById(R.id.tv_05);

        rl_01 = (RelativeLayout) findViewById(R.id.rl_01);
        rl_02 = (RelativeLayout) findViewById(R.id.rl_02);
        rl_03 = (RelativeLayout) findViewById(R.id.rl_03);
        rl_04 = (RelativeLayout) findViewById(R.id.rl_04);
        rl_05 = (RelativeLayout) findViewById(R.id.rl_05);
        rl_06 = (RelativeLayout) findViewById(R.id.rl_06);
        rl_07 = (RelativeLayout) findViewById(R.id.rl_07);
        rl_08 = (RelativeLayout) findViewById(R.id.rl_08);
        rl_09 = (RelativeLayout) findViewById(R.id.rl_09);
        rl_10 = (RelativeLayout) findViewById(R.id.rl_10);
        tv_back.setVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.account_set_str));


    }

    @Override
    public void setUpLisener() {

        tv_back.setOnClickListener(this);
        rl_01.setOnClickListener(this);
        rl_02.setOnClickListener(this);
        rl_03.setOnClickListener(this);
        rl_04.setOnClickListener(this);
        rl_05.setOnClickListener(this);
        rl_06.setOnClickListener(this);
        rl_07.setOnClickListener(this);
        rl_08.setOnClickListener(this);
        rl_09.setOnClickListener(this);
        rl_10.setOnClickListener(this);

    }

    private AccountSetInfoBean mAccountSetInfoBean;

    @Override
    public void getDataOnCreate() {

        //TestResultUtils.getSussefulResult6()
        addSubscription(RetrofitClient.getAccountSetInfo(null, this, new YCNetSubscriber<AccountSetInfoBean>(this) {

            @Override
            public void onYcNext(AccountSetInfoBean model) {

                mAccountSetInfoBean = model;
                ycLoadingBg.dissmiss();
                if (mAccountSetInfoBean.isBind == BindingStateEnum.BINDED_MOBILE.getValue()) {
                    tv_is_bind.setText(BindingStateEnum.BINDED_MOBILE.getName());
                } else {
                    tv_is_bind.setText(BindingStateEnum.UNBINDED_MOBILE.getName());
                }
                if (mAccountSetInfoBean.isAuthentication == CertificationEnum.PASS_CERTIFICATION.getValue()) {
                    tv_is_rz.setText(CertificationEnum.PASS_CERTIFICATION.getName());
                } else {
                    tv_is_rz.setText(CertificationEnum.UNPASS_CERTIFICATION.getName());
                }

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // 结果码不等于取消时候
        if (resultCode != Activity.RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    // data.getData()获得拍照返回的Uri
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
                //认证结果
                case RZ_REQUEST_CODE:
                    if (resultCode == RESULT_OK) {
                        mAccountSetInfoBean.isAuthentication = CertificationEnum.PASS_CERTIFICATION.getValue();
                        tv_is_rz.setText(CertificationEnum.PASS_CERTIFICATION.getName());
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onUserClick(View v) {
        super.onUserClick(v);
        int flag = 0;
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            //头像更改
            case R.id.rl_01:
                showDialog();
                break;
            //实名认证
            case R.id.rl_02:
                Bundle bundle = new Bundle();
                bundle.putInt(RealNameAuthenticationActivity.CertificationState, mAccountSetInfoBean.isAuthentication);
                JumpCenter.JumpActivity(this, RealNameAuthenticationActivity.class, bundle, null, RZ_REQUEST_CODE, JumpCenter.INVAILD_FLAG, false, true);
                break;
            //手机绑定
            case R.id.rl_03:
                JumpCenter.JumpActivity(this, ChangeMobilePhoneStepOneActivity.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
                break;
            //登录密码
            case R.id.rl_04:
                JumpCenter.JumpActivity(this, ModifyPasswordActivity.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
                break;
            //手势密码
            case R.id.rl_05:

                if (UserBiz.getInstance(this).Is_Setted_Gesture()) {
                    JumpCenter.JumpActivity(this, GestureSetActivity.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
                } else {
                    //直接跳转设置界面
                    bundle = new Bundle();
                    bundle.putBoolean(GestureEditActivity.IS_SHOW_BACK, true);
                    bundle.putBoolean(GestureEditActivity.IS_FINISH_ACTIVITY, true);
                    JumpCenter.JumpActivity(this, GestureEditActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
                }
                break;
            //关于我们
            case R.id.rl_06:
                bundle = new Bundle();
                bundle.putString(AboutUsActivity.ABOUT_US_URL, mAccountSetInfoBean.aboutUsUrl);
                JumpCenter.JumpActivity(this, AboutUsActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
                break;
            //意见反馈
            case R.id.rl_07:
                JumpCenter.JumpActivity(this, FeedBackActivity.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
                break;
            //自动投标
            case R.id.rl_08:
                JumpCenter.JumpActivity(this, AutoInvestActivity.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
                break;
            //帮助中心
            case R.id.rl_09:
                bundle = new Bundle();
                bundle.putString(OtherActivity.BACK_STR, getResources().getString(R.string.account_set_str));
                bundle.putString(OtherActivity.URL, mAccountSetInfoBean.helperCenterUrl);
                JumpCenter.JumpActivity(this, OtherActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, false, true);
                break;
            //退出登录
            case R.id.rl_10:
                ycDialogUtils.showDialog(getResources().getString(R.string.dialog_title), getResources().getString(R.string.check_exsit), new YCDialogUtils.MyTwoBtnclickLisener() {
                    @Override
                    public void onFirstBtnClick(View v) {
                        //ok
                        SPUtils.put(AccountSetActivity.this, UserBiz.getInstance(AccountSetActivity.this).GetUserName(), UserBiz.getInstance(AccountSetActivity.this).GetGesturePassWord() + "." + (UserBiz.getInstance(AccountSetActivity.this).Is_Open_Gesture() ? 1:-1));
                        ycDialogUtils.DismissMyDialog();
                        UserBiz.getInstance(AccountSetActivity.this).ExitLogin();
                        Bundle bundle = new Bundle();
                        bundle.putInt(MainActivity.SHOW_FRAGMENT_INDEX, MainFragmentEnum.HOME.getValue());
                        JumpCenter.JumpActivity(AccountSetActivity.this, MainActivity.class, bundle, null, JumpCenter.NORMALL_REQUEST, Intent.FLAG_ACTIVITY_NEW_TASK, true, false);
                    }

                    @Override
                    public void onSecondBtnClick(View v) {
                        ycDialogUtils.DismissMyDialog();
                    }
                }, true);

                break;
            // 拍照
            case R.id.btn_gono_xj:
                flag = 1;
                // 从相册选取
            case R.id.btn_auto_xj:
                if (flag != 1) {
                    flag = 2;
                }
                dialog.dismiss();
                GetPhotoAndUpload(flag);
                break;
            // 关闭对话框
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
        }
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
        btn_xj_goon.setOnClickListener(this);
        Button btn_xj_auto = (Button) view.findViewById(R.id.btn_auto_xj);
        btn_xj_auto.setOnClickListener(this);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
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
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 340);
        intent.putExtra("outputY", 340);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    private Drawable drawable;

    /**
     * 保存裁剪之后的图片数据
     *
     * @param data
     */
    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            // 获得bitmap
            Bitmap photo = extras.getParcelable("data");
            drawable = new BitmapDrawable(this.getResources(), photo);
            byte[] base64 = Base64.encode(Bitmap2Bytes(photo), Base64.DEFAULT);
            String file = "";
            file = URLEncoder.encode(new String(base64));
            //上传
            addSubscription(RetrofitClient.UploadPhoto(null, file, "jpg", this, ycNetSubscriber));
        }
    }

    private YCNetSubscriber<UploadPhotoBean> ycNetSubscriber = new YCNetSubscriber<UploadPhotoBean>(this) {

        @Override
        public void onYcNext(UploadPhotoBean model) {
            //iv_photo.setImageDrawable(drawable);
            setResult(RESULT_OK);
            UserBiz.getInstance(AccountSetActivity.this).UpdateUserPhotoAddr(model.imageUrl);
            ImageWithGlideUtils.lodeFromUrlRoundTransform(model.imageUrl, iv_photo, AccountSetActivity.this);
        }

    };

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
        mIntent = getIntent();
        if (mIntent != null) {
            tv_name.setText(UserBiz.getInstance(this).GetUserName());
            ImageWithGlideUtils.lodeFromUrlRoundTransform(UserBiz.getInstance(this).GetUserImg(), R.mipmap.default_user_photo_large_bg, iv_photo, this);
            if (UserBiz.getInstance(this).CheckGesturePassword()) {
                tv_is_gesature_set.setText(SetingEnum.SETED_STATE.getName());
            } else {
                tv_is_gesature_set.setText(SetingEnum.UNSETED_STATE.getName());
            }
        }
        if (UserBiz.getInstance(this).CheckGesturePassword()) {
            tv_is_gesature_set.setText(SetingEnum.SETED_STATE.getName());
        } else {
            tv_is_gesature_set.setText(SetingEnum.UNSETED_STATE.getName());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
