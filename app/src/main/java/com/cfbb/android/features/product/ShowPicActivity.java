package com.cfbb.android.features.product;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseActivity;
import com.cfbb.android.commom.utils.image.ImageWithGlideUtils;
import com.easybei.photoview.PhotoView;

/***
 * 显示图片activity
 *
 * @author MrChang
 */
public class ShowPicActivity extends BaseActivity {

    public static final String IMAGE_URL = "image_url";

    private PhotoView photoView;
    private ImageView iv_back;
    private String imageUrl;
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
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.imagview_dialog);
        mIntent = getIntent();

    }

    @Override
    public void setUpViews() {
        photoView = (PhotoView) findViewById(R.id.phtoview);
        iv_back = (ImageView) findViewById(R.id.iv_01);
        if (mIntent != null) {
            imageUrl = mIntent.getExtras().getString(IMAGE_URL);
            ImageWithGlideUtils.lodeFromUrl(imageUrl,photoView,getApplicationContext());
        }
    }

    @Override
    public void setUpLisener() {
        iv_back.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onUserClick(View v) {
        super.onUserClick(v);
        switch (v.getId()) {
            case R.id.iv_01:
                finish();
                break;
        }
    }


}
