package com.cfbb.android.features.account.withdrawAndrecharge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseadaptor.CommonListAdapter;
import com.cfbb.android.protocol.bean.WithdrawRecordInfoBean;

/**
 * Created by dell on 2016/4/6.
 */
public class WithDrawRecordAdaptor extends CommonListAdapter<WithdrawRecordInfoBean> {

    public WithDrawRecordAdaptor(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WithdrawRecordInfoBean model = mDataSource.get(position);
        ViewHolder viewHolder = null;
        LayoutInflater inflate = null;
        if (convertView == null) {
            inflate = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflate.inflate(R.layout.listview_withdraw_record_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_withdrawState.setText(model.withdrawState);
        viewHolder.tv_withdrawMoeny.setText(model.withDrawlMoeny);
        viewHolder.tv_withdrawDate.setText(model.withdrawDate);
        viewHolder.tv_tips.setText(model.tips);

        return convertView;
    }

    private static final class ViewHolder {
        TextView tv_withdrawDate;
        TextView tv_withdrawState;
        TextView tv_withdrawMoeny;
        TextView tv_tips;


        public ViewHolder(View view) {
            tv_withdrawDate = (TextView) view.findViewById(R.id.tv_title);
            tv_withdrawMoeny = (TextView) view.findViewById(R.id.tv_02);
            tv_withdrawState = (TextView) view.findViewById(R.id.tv_03);
            tv_tips = (TextView) view.findViewById(R.id.tv_04);

        }
    }
}
