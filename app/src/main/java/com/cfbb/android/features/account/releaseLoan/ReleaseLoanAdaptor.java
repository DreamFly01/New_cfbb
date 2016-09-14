package com.cfbb.android.features.account.releaseLoan;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseadaptor.CommonListAdapter;
import com.cfbb.android.protocol.bean.MyLoanListBean;

/**
 * Created by MrChang45 on 2016/4/22.
 */
public class ReleaseLoanAdaptor extends CommonListAdapter<MyLoanListBean> {

    public ReleaseLoanAdaptor(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyLoanListBean model = mDataSource.get(position);
        ViewHolder viewHolder = null;
        LayoutInflater inflate = null;
        if (convertView == null) {
            inflate = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflate.inflate(R.layout.listview_my_loan_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_date.setText(model.loanDate);
        viewHolder.tv_moeny.setText(model.loanMoeny);
        try {
            viewHolder.tv_state.setTextColor(Color.parseColor("#"+model.txtColor));
        } catch (Exception e) {
            viewHolder.tv_state.setTextColor(mContext.getResources().getColor(R.color.balck));
        }
        viewHolder.tv_state.setText(model.loanSate);
        viewHolder.tv_title.setText(model.loanTitle);
        return convertView;
    }

    private static final class ViewHolder {

        TextView tv_title;
        TextView tv_state;
        TextView tv_moeny;
        TextView tv_date;

        public ViewHolder(View view) {
            tv_title = (TextView) view.findViewById(R.id.tv_03);
            tv_state = (TextView) view.findViewById(R.id.tv_02);
            tv_moeny = (TextView) view.findViewById(R.id.tv_title);
            tv_date = (TextView) view.findViewById(R.id.tv_04);
        }
    }
}
