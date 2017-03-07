package com.cfbb.android.features.account.releaseLoan;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.features.main.MainActivity;
import com.cfbb.android.widget.MyGridView;
import com.cfbb.android.widget.dialog.YCDialogUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class AddImgActivity extends BaseActivity {
    private TextView titleName, tijiao, tianjia,title,back;
    private EditText etName;
    private MyGridView myGridView;
    private IntroduceLoanAdapter adapter;
    private YCDialogUtils ycDialogUtils;
    private ArrayList<String> list;


    private String name;
    private String imgPosition;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_img);

    }

    Gson gson = new Gson();

    @Override
    public void getDataOnCreate() {
        super.getDataOnCreate();
        ycDialogUtils = new YCDialogUtils(this);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        imgPosition = intent.getStringExtra("imgPosition");


        if (name != null) {
            etName.setVisibility(View.GONE);
            titleName.setVisibility(View.VISIBLE);
            titleName.setText("请添加" + name + "证明材料:");
        } else {
            etName.setVisibility(View.VISIBLE);
            titleName.setVisibility(View.GONE);
        }
        if(imgPosition!=null){
             list = gson.fromJson(imgPosition, new TypeToken<List<String>>() {
            }.getType());
            System.out.println("接收的imgPosition:"+imgPosition);
            if (list.size() > 0) {
                adapter.addList(list);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void setUpViews() {
        titleName = (TextView) findViewById(R.id.txt_zhenming);
        tijiao = (TextView) findViewById(R.id.txt_tijiao2);
        tianjia = (TextView) findViewById(R.id.txt_tianjia);
        title = (TextView)findViewById(R.id.tv_title);
        back = (TextView)findViewById(R.id.tv_back);
        etName = (EditText) findViewById(R.id.et_01);
        myGridView = (MyGridView) findViewById(R.id.gv_01);



        title.setVisibility(View.VISIBLE);
        title.setText("选择证明材料");
        back.setText("返回");
        back.setVisibility(View.VISIBLE);
        adapter = new IntroduceLoanAdapter(this);
        myGridView.setAdapter(adapter);
        tianjia.setOnClickListener(this);
        tijiao.setOnClickListener(this);
        title.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void setUpLisener() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int flag = 0;

        switch (v.getId()) {
            case R.id.txt_tijiao2:
                Intent intent = new Intent(AddImgActivity.this, IntroduceData.class);
                if (name == null) {
                    if (etName.getText().toString().equals("")) {
                        dialog1("请输入证明材料名称");
                        break;
                    }
                }

                if(list==null){
                if (imgList.size() <= 0) {
                    dialog1("请添加图片证明材料");
                    break;
                }
                }else{
                    dialog1("已经提交过请不要重复提交");
                    break;
                }
                if (!etName.getText().toString().equals("")) {
                    intent.putExtra("name", etName.getText().toString());
//                    setResult(RESULT_OK, intent);
//                    AddImgActivity.this.finish();
//                    break;
                }
                Gson gson = new GsonBuilder().create();
                System.out.println("imgList的大小:"+imgList.size());
                String imgStr = gson.toJson(imgList);
                System.out.println("imgStr:"+imgStr);
                intent.putExtra("imglist", imgStr);
//                System.out.println(imgStr);
                setResult(RESULT_OK, intent);
                AddImgActivity.this.finish();
                break;
            case R.id.txt_tianjia:
                showDialog();
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
            case R.id.tv_back:
                AddImgActivity.this.finish();
                break;
        }
    }

    private Dialog dialog;
    private static final int IMAGE_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int RESULT_REQUEST_CODE = 3;

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
//            Intent intentFromCapture = new Intent(
//                    MediaStore.ACTION_IMAGE_CAPTURE);
//            File path = Environment
//                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//            File file = new File(path, "");
//            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
//                    Uri.fromFile(file));
//            startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
            String state = Environment.getExternalStorageState();
            if (state.equals(Environment.MEDIA_MOUNTED)) {
                Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(getImageByCamera, CAMERA_REQUEST_CODE);
            }
            else {
                Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
            }
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

    private ArrayList<String> imgList = new ArrayList<>();

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
//                    if (state.equals(Environment.MEDIA_MOUNTED)) {
////                        File path = Environment
////                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
////                        File tempFile = new File(path, "");
////                        startPhotoZoom(Uri.fromFile(tempFile));
//                    } else {
//                        showShortToast(R.string.can_not_find_SD);
//                    }
                    startPhotoZoom(data.getData());
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
        System.out.println("uri:"+uri.toString());
        if(uri!=null){
        imgList.add(getRealFilePath(this, uri));
        adapter.addList(imgList);
        adapter.notifyDataSetChanged();
        }
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
//            System.out.println();
            drawable = new BitmapDrawable(this.getResources(), photo);
            byte[] base64 = Base64.encode(Bitmap2Bytes(photo), Base64.DEFAULT);
            String file = "";
            file = "base64," + URLEncoder.encode(new String(base64));
            //上传
        }
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
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

    /**
     * 将图片uri转换为路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

}
