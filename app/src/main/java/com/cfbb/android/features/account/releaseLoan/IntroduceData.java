package com.cfbb.android.features.account.releaseLoan;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.utils.activityJump.JumpCenter;
import com.cfbb.android.protocol.bean.ImgArrayBean;
import com.cfbb.android.protocol.bean.ImgData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class IntroduceData extends BaseActivity implements AdapterView.OnItemClickListener {
    private ListView introduceDataList;
    private IntroduceListAdapter adapter;
    private IntroduceLoanAdapter loanAdapter;
    private List<String> list;
    private List<List<String>> imgData = new ArrayList<>();
    private TextView menu;
    private TextView back,title;
    private TextView tianjia, tijiao;
    private int flag = 1;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_introduce_data);

    }

    @Override
    public void getDataOnCreate() {
        super.getDataOnCreate();
        verifyDialog();
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
//        System.out.println(type+"................");
        if (type.equals("个人类借款")) {
            list = new ArrayList<>();
            list.add("身份证");
            list.add("个人信用报告");
            list.add("工资流水");
            adapter.setList(list);
        }
        if (type.startsWith("车辆")) {
            list = new ArrayList<>();
            list.add("身份证");
            list.add("个人信用报告");
            list.add("机动车证件");
            list.add("行驶证件");
            adapter.setList(list);
        }
        if (type.startsWith("房产")) {
            list = new ArrayList<>();
            list.add("身份证");
            list.add("个人信用报告");
            list.add("房屋证件");
            list.add("土地证件");
            adapter.setList(list);
        }
        if (type.startsWith("企业")) {
            list = new ArrayList<>();
            list.add("身份证");
            list.add("个人信用报告");
            list.add("营业证件");
            list.add("机构证件");
            list.add("税务证件");
            list.add("财务证件");
            adapter.setList(list);
        }
    }

    @Override
    public void setUpViews() {
        adapter = new IntroduceListAdapter(this);
        loanAdapter = new IntroduceLoanAdapter(this);
        introduceDataList = (ListView) findViewById(R.id.ll_01);
        menu = (TextView) findViewById(R.id.tv_menu);
        back = (TextView) findViewById(R.id.tv_back);
        tianjia = (TextView) findViewById(R.id.tianjia);
        tijiao = (TextView) findViewById(R.id.txt_tijiao2);
        title = (TextView)findViewById(R.id.tv_title);
        menu.setVisibility(View.VISIBLE);
        menu.setTextColor(Color.RED);
        back.setVisibility(View.VISIBLE);
        back.setText("我要借款");
        menu.setText("删除");
        title.setText("上传证明材料");

        introduceDataList.setOnItemClickListener(this);
        menu.setOnClickListener(this);
        back.setOnClickListener(this);
        tianjia.setOnClickListener(this);
        tijiao.setOnClickListener(this);
        introduceDataList.setAdapter(adapter);
    }

    @Override
    public void setUpLisener() {

    }

    private Intent intent;

    String imgPosition;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        intent = new Intent(IntroduceData.this, AddImgActivity.class);
        String name = list.get(position);
        intent.putExtra("name", name);
        if (imgData.size() > 0) {
            if (imgData.size() >= (position + 1)) {
                imgPosition = gson.toJson(imgData.get(position));
            }
        }
        System.out.println("发送的imgPosition:" + imgPosition);

        intent.putExtra("imgPosition", imgPosition);
        startActivityForResult(intent, 1);
    }

    Gson gson = new GsonBuilder().create();

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_menu:
                if (flag == 2) {
                    flag = 1;
                    menu.setText("删除");
                    adapter.setReduceStatu(flag);
                    adapter.notifyDataSetChanged();
                } else if (flag == 1) {
                    flag = 2;
                    menu.setText("取消");
                    adapter.setReduceStatu(flag);
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.tianjia:
                intent = new Intent(IntroduceData.this, AddImgActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.txt_tijiao2:
                List<ImgData> listData ;
                List<ImgArrayBean> proofMaterial = new ArrayList<>();
                System.out.println(imgData.size());
                if(imgData.size()>0){
                for (int i = 0; i < imgData.size(); i++) {
                    imgArr = new ImgArrayBean();
                    listData = new ArrayList<>();
                    for (int i1 = 0; i1 < imgData.get(i).size(); i1++) {
                        imgdata = new ImgData();
                        imgdata.setType("png");
                        imgdata.setData(getImageToView(imgData.get(i).get(i1)));
                        listData.add(imgdata);
                    }
                    System.out.println(list.get(i));
                    imgArr.setName(list.get(i));
                    imgArr.setImgDatas(listData);
                    proofMaterial.add(imgArr);
                }
                }

                String result = gson.toJson(proofMaterial);
//                System.out.println(result);
                saveFile(result, "imgArr.txt");
//                showShortToast("保存成功");
                JumpCenter.JumpActivity(IntroduceData.this, ValidateLoan.class, null, null, JumpCenter.NORMALL_REQUEST, JumpCenter.INVAILD_FLAG, true, true);

                break;
            case R.id.tv_back:
                this.finish();
                break;
        }
    }

    private ImgData imgdata;
    private ImgArrayBean imgArr;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (data == null) {
                    break;
                }
                if (data.getStringExtra("imglist") != null) {
//                    System.out.println(data.getStringExtra("imglist") + "------------");
                    Gson gson = new Gson();
                    ArrayList<String> list1 = gson.fromJson(data.getStringExtra("imglist"), new TypeToken<ArrayList<String>>() {
                    }.getType());
                    System.out.println("传过来的imglist:"+data.getStringExtra("imglist"));
                    System.out.println("list1的大小：" + list1.size()+"-");
                    imgData.add(list1);
//                    System.out.println(list.get(0) + "*************");
                    adapter.setImgData(imgData);

                }
                if (data.getStringExtra("name") != null) {
                    list.add(data.getStringExtra("name"));
                    adapter.setList(list);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    private Drawable drawable;

    private String getImageToView(String path) {

        // 获得bitmap
        Bitmap photo = BitmapFactory.decodeFile(path);

        drawable = new BitmapDrawable(this.getResources(), photo);
        byte[] base64 = Base64.encode(Bitmap2Bytes(photo), Base64.DEFAULT);
        String file = "";
        file = "base64," + URLEncoder.encode(new String(base64));
        return file;
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

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
    private void verifyDialog()
    {
        final Dialog dialog = new Dialog(IntroduceData.this, R.style.popupDialog);
        dialog.setContentView(R.layout.verify_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        TextView message = (TextView)dialog.getWindow().findViewById(R.id.messageTxt);
        TextView okBtn = (TextView) dialog.getWindow().findViewById(R.id.dismissBtn);
        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(dialog!=null && dialog.isShowing())
                {
                    dialog.dismiss();
                }
            }
        });
        if(dialog!=null && !dialog.isShowing())
        {
            dialog.show();
        }
    }
}
