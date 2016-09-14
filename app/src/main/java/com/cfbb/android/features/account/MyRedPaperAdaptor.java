package com.cfbb.android.features.account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseadaptor.CommonListAdapter;
import com.cfbb.android.commom.state.RedPaperStateEnum;
import com.cfbb.android.protocol.bean.MyRedPaperBean;

/**
 * Created by dell on 2016/4/5.
 * 我的红包适配器
 */
public class MyRedPaperAdaptor extends CommonListAdapter<MyRedPaperBean> {

    public MyRedPaperAdaptor(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyRedPaperBean model = mDataSource.get(position);
        ViewHolder viewHolder = null;
        LayoutInflater inflate = null;
        if (convertView == null) {
            inflate = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflate.inflate(R.layout.listview_redpaper_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_name.setText(model.name);
        viewHolder.tv_money.setText(model.money);
        viewHolder.tv_from.setText(model.from);
        viewHolder.tv_period.setText(model.period);
        if (model.state == RedPaperStateEnum.OVERDUE_USED.getValue()) {
            viewHolder.rl_left.setBackgroundResource(R.mipmap.my_use_redpaper_left);
            viewHolder.ll_right.setBackgroundResource(R.mipmap.my_use_redpaper_right);
            viewHolder.ll_right_bg.setBackgroundResource(R.mipmap.my_redpaper_over_used_bg_right);

            viewHolder.tv_10.setTextColor(mContext.getResources().getColor(R.color.txt_9));
            viewHolder.tv_11.setTextColor(mContext.getResources().getColor(R.color.txt_9));
            viewHolder.tv_name.setTextColor(mContext.getResources().getColor(R.color.txt_9));
            viewHolder.tv_money.setTextColor(mContext.getResources().getColor(R.color.txt_9));
            viewHolder.tv_from.setTextColor(mContext.getResources().getColor(R.color.txt_9));
            viewHolder.tv_period.setTextColor(mContext.getResources().getColor(R.color.txt_9));
        }
        if (model.state == RedPaperStateEnum.OVERDUE_REDPAPER.getValue()) {
            viewHolder.rl_left.setBackgroundResource(R.mipmap.my_use_redpaper_left);
            viewHolder.ll_right.setBackgroundResource(R.mipmap.my_use_redpaper_right);
            viewHolder.ll_right_bg.setBackgroundResource(R.mipmap.my_overdue_bg_right);

            viewHolder.tv_10.setTextColor(mContext.getResources().getColor(R.color.txt_9));
            viewHolder.tv_11.setTextColor(mContext.getResources().getColor(R.color.txt_9));
            viewHolder.tv_name.setTextColor(mContext.getResources().getColor(R.color.txt_9));
            viewHolder.tv_money.setTextColor(mContext.getResources().getColor(R.color.txt_9));
            viewHolder.tv_from.setTextColor(mContext.getResources().getColor(R.color.txt_9));
            viewHolder.tv_period.setTextColor(mContext.getResources().getColor(R.color.txt_9));
        }
        if (model.state == RedPaperStateEnum.UNUSER_REDPAPER.getValue()) {

            viewHolder.tv_10.setTextColor(mContext.getResources().getColor(R.color.white));
            viewHolder.tv_11.setTextColor(mContext.getResources().getColor(R.color.white));
            viewHolder.rl_left.setBackgroundResource(R.mipmap.my_unuse_redpaper_left);
            viewHolder.ll_right.setBackgroundResource(R.mipmap.my_unuse_redpaper_right);
            viewHolder.ll_right_bg.setBackgroundResource(R.drawable.my_redpaper_bg);
            viewHolder.tv_name.setTextColor(mContext.getResources().getColor(R.color.white));
            viewHolder.tv_money.setTextColor(mContext.getResources().getColor(R.color.white));
            viewHolder.tv_from.setTextColor(mContext.getResources().getColor(R.color.white));
            viewHolder.tv_period.setTextColor(mContext.getResources().getColor(R.color.white));
        }
        return convertView;
    }

    private static final class ViewHolder {
        TextView tv_name;
        TextView tv_money;
        TextView tv_from;
        TextView tv_period;
        RelativeLayout rl_left;
        LinearLayout ll_right;
        LinearLayout ll_right_bg;
        TextView tv_10;
        TextView tv_11;

        public ViewHolder(View view) {
            tv_10 = (TextView) view.findViewById(R.id.tv_10);
            tv_11 = (TextView) view.findViewById(R.id.tv_11);
            tv_name = (TextView) view.findViewById(R.id.tv_title);
            tv_money = (TextView) view.findViewById(R.id.tv_02);
            tv_from = (TextView) view.findViewById(R.id.tv_03);
            tv_period = (TextView) view.findViewById(R.id.tv_04);

            rl_left = (RelativeLayout) view.findViewById(R.id.rl_01);
            ll_right = (LinearLayout) view.findViewById(R.id.ll_01);
            ll_right_bg = (LinearLayout) view.findViewById(R.id.ll_02);

        }
    }

}
