package com.cfbb.android.widget;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.cfbb.android.R;


/***
 * 加载对话框
 *
 * @author MrChang
 *         无敌菊花转
 */
public class CustomProgress extends Dialog {

    public CustomProgress(Context context) {
        super(context);
    }

    public CustomProgress(Context context, int theme) {
        super(context, theme);
    }

    public void onWindowFocusChanged(boolean hasFocus) {

        ImageView imageView = (ImageView) findViewById(R.id.spinnerImageView);
        AnimationDrawable spinner = (AnimationDrawable) imageView.getBackground();
        assert spinner != null;
        spinner.start();

    }

    public static CustomProgress show(Context context, DialogInterface.OnCancelListener cancelListener) {

        if (null != context) {
            CustomProgress dialog = new CustomProgress(context, R.style.CustomProgress);
            dialog.setTitle("");
            dialog.setContentView(R.layout.custom_prograss_jhz);
            dialog.setCancelable(false);
            dialog.setOnCancelListener(cancelListener);
            dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.dimAmount = 0.2f;
            dialog.getWindow().setAttributes(lp);
            dialog.show();
            return dialog;
        } else {
            return null;
        }

    }

    public static final int SHOW_UP = 1;
    public static final int SHOW_DOWN = 2;
    private static PopupWindow popupWindow;

    public static PopupWindow ShowLoaing(Context context, int showUpOrDown, View anchor) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(context).inflate(
                R.layout.custom_progress, null);
        popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        if (showUpOrDown == SHOW_UP) {
            popupWindow.showAsDropDown(anchor);
        } else {
            popupWindow.showAsDropDown(anchor);
        }
        return popupWindow;
    }

    public void Dissmiss() {
        popupWindow.dismiss();
    }

}
