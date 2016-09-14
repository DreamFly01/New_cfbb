package com.cfbb.android.features.account.myinvest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseadaptor.CommonListAdapter;
import com.cfbb.android.commom.state.MyInvestStateEnum;
import com.cfbb.android.protocol.bean.MyInvestBean;
import com.cfbb.android.widget.YCRoundProgress;

/**
 * Created by Mrchang45 on 2016/3/30.
 */
public class MyInvestAdaptor extends CommonListAdapter<MyInvestBean> {

    public MyInvestAdaptor(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyInvestBean model = mDataSource.get(position);
        ViewHolder viewHolder = null;
        LayoutInflater inflate = null;
        if (convertView == null) {
            inflate = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflate.inflate(R.layout.listview_myinvest_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_productName.setText(model.productName);
        if (model.state == MyInvestStateEnum.INVESTING.getValue()) {

            viewHolder.ll_prograss.setVisibility(View.VISIBLE);
            viewHolder.ll_normal.setVisibility(View.GONE);

            viewHolder.tv_first.setVisibility(View.VISIBLE);
            viewHolder.tv_first.setText(model.yearOfRate);

            viewHolder.tv_first_per.setVisibility(View.VISIBLE);

            viewHolder.tv_first_bottom_name.setVisibility(View.VISIBLE);
            viewHolder.tv_first_bottom_name.setText(mContext.getResources().getString(R.string.expect_year_of_rate_str));

            viewHolder.tv_second.setVisibility(View.VISIBLE);
            viewHolder.tv_second.setText(model.investMoeny);

            viewHolder.tv_second_bottom_name.setVisibility(View.VISIBLE);
            viewHolder.tv_second_bottom_name.setText(mContext.getResources().getString(R.string.invest_money_str));

            viewHolder.tv_third.setVisibility(View.GONE);
            viewHolder.tv_third_bottom_name.setVisibility(View.GONE);

            viewHolder.ycRoundProgress.setVisibility(View.VISIBLE);
            viewHolder.ycRoundProgress.setProgress(Double.parseDouble(model.progress));
        }
        if (model.state == MyInvestStateEnum.COMPLETE.getValue()) {
            viewHolder.ll_prograss.setVisibility(View.GONE);
            viewHolder.ll_normal.setVisibility(View.GONE);
            viewHolder.tv_first.setVisibility(View.VISIBLE);
            viewHolder.tv_first.setText(model.totalEraning);
            viewHolder.tv_first_per.setVisibility(View.GONE);
            viewHolder.tv_first_bottom_name.setVisibility(View.VISIBLE);
            viewHolder.tv_first_bottom_name.setText(mContext.getResources().getString(R.string.total_earing_str));
            viewHolder.tv_second.setVisibility(View.VISIBLE);
            viewHolder.tv_second.setText(model.finishTime);
            viewHolder.tv_second_bottom_name.setVisibility(View.VISIBLE);
            viewHolder.tv_second_bottom_name.setText(mContext.getResources().getString(R.string.finishTime_str));
            viewHolder.tv_third.setVisibility(View.GONE);
            viewHolder.tv_third_bottom_name.setVisibility(View.GONE);
            viewHolder.ycRoundProgress.setVisibility(View.GONE);
        }
        if (model.state == MyInvestStateEnum.HOLDING.getValue()) {

            viewHolder.ll_prograss.setVisibility(View.GONE);
            viewHolder.ll_normal.setVisibility(View.VISIBLE);

            viewHolder.tv_first.setVisibility(View.VISIBLE);
            viewHolder.tv_first.setText(model.yearOfRate);

            viewHolder.tv_first_per.setVisibility(View.VISIBLE);

            viewHolder.tv_first_bottom_name.setVisibility(View.VISIBLE);
            viewHolder.tv_first_bottom_name.setText(mContext.getResources().getString(R.string.year_of_rate_str));

            viewHolder.tv_second.setVisibility(View.VISIBLE);
            viewHolder.tv_second.setText(model.investMoeny);

            viewHolder.tv_second_bottom_name.setVisibility(View.VISIBLE);
            viewHolder.tv_second_bottom_name.setText(mContext.getResources().getString(R.string.invest_money_str));

            viewHolder.tv_third.setVisibility(View.VISIBLE);
            viewHolder.tv_third.setText(model.remiansDay);

            viewHolder.tv_third_bottom_name.setVisibility(View.VISIBLE);
            viewHolder.tv_third_bottom_name.setText(mContext.getResources().getString(R.string.remainsDays_str));

            viewHolder.ycRoundProgress.setVisibility(View.GONE);

        }
        return convertView;
    }

    private static final class ViewHolder {

        TextView tv_productName;
        TextView tv_first;
        TextView tv_first_per;
        TextView tv_first_bottom_name;
        TextView tv_second;
        TextView tv_second_bottom_name;
        TextView tv_third;
        TextView tv_third_bottom_name;
        YCRoundProgress ycRoundProgress;
        LinearLayout ll_normal;
        LinearLayout ll_prograss;

        public ViewHolder(View view) {
            ll_normal = (LinearLayout) view.findViewById(R.id.ll_01);
            ll_prograss = (LinearLayout) view.findViewById(R.id.ll_02);
            tv_productName = (TextView) view.findViewById(R.id.tv_title);
            tv_first = (TextView) view.findViewById(R.id.tv_02);
            tv_first_per = (TextView) view.findViewById(R.id.tv_03);
            tv_first_bottom_name = (TextView) view.findViewById(R.id.tv_04);
            tv_second = (TextView) view.findViewById(R.id.tv_05);
            tv_second_bottom_name = (TextView) view.findViewById(R.id.tv_06);
            tv_third = (TextView) view.findViewById(R.id.tv_07);
            tv_third_bottom_name = (TextView) view.findViewById(R.id.tv_08);
            ycRoundProgress = (YCRoundProgress) view.findViewById(R.id.ycRoundProgress);
        }
    }
}
