package com.cfbb.android.features.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseadaptor.CommonListAdapter;
import com.cfbb.android.protocol.bean.BidRecordBean;

/**
 * Created by dell on 2016/3/25.
 * 投标列表
 */
public class BidRecordAdaptor extends CommonListAdapter<BidRecordBean> {

    public BidRecordAdaptor(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        LayoutInflater inflate = null;
        BidRecordBean model = mDataSource.get(position);
        if (convertView == null) {
            inflate = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflate.inflate(R.layout.listview_bidrecord_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_name.setText(model.name);
        viewHolder.tv_moeny.setText(model.money);
        viewHolder.tv_date.setText(model.date);
        return convertView;
    }

    public static final class ViewHolder {

        TextView tv_name;
        TextView tv_moeny;
        TextView tv_date;

        public ViewHolder(View v) {
            tv_name = (TextView) v.findViewById(R.id.tv_title);
            tv_moeny = (TextView) v.findViewById(R.id.tv_02);
            tv_date = (TextView) v.findViewById(R.id.tv_03);
        }
    }
}
