package com.cfbb.android.features.slidingFinishView;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.cfbb.android.R;


/**
 * 想要实现向右滑动删除Activity效果只需要继承SwipeBackActivity即可，如果当前页面含有ViewPager
 * 只需要调用SwipeBackLayout的setViewPager()方法即可
 *
 * @author MrChang
 */
public class SwipeBackActivity extends Activity {
    protected SwipeBackLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
                R.layout.base, null);
        layout.attachToActivity(this);

        //依据InsideLetterActivity里面的ListViewForDel的mIsShown值设置
        //layout.setEnabled();
    }

    public void setBackEnable(boolean isOpen) {
        layout.setIsEnable(isOpen);
    }
}
