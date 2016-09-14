package com.cfbb.android.features.account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseadaptor.CommonListAdapter;
import com.cfbb.android.commom.state.MoneyIsAddEnum;
import com.cfbb.android.protocol.bean.TradeRecordBean;

/**
 * Created by dell on 2016/3/31.
 * 交易记录适配器
 */
public class TradeRecordAdaptor extends CommonListAdapter<TradeRecordBean> {


    public TradeRecordAdaptor(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TradeRecordBean model = mDataSource.get(position);
        ViewHolder viewHolder = null;
        LayoutInflater inflate = null;
        if (convertView == null) {
            inflate = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflate.inflate(R.layout.listview_traderecord_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_tradeName.setText(model.tradeName);
        viewHolder.tv_tradeDate.setText(model.tradeDate);
        viewHolder.tv_tradeMoeny.setText(model.tradeMoney);
        viewHolder.tv_tradeType.setText(model.tradeTypeName);
        if (model.isAdding == MoneyIsAddEnum.IS_ADD_MOENY.getValue()) {
            viewHolder.tv_tradeMoeny.setTextColor(mContext.getResources().getColor(R.color.txt_green));
        } else {
            viewHolder.tv_tradeMoeny.setTextColor(mContext.getResources().getColor(R.color.txt_red));
        }

        return convertView;
    }

    private static final class ViewHolder {
        TextView tv_tradeName;
        TextView tv_tradeDate;
        TextView tv_tradeMoeny;
        TextView tv_tradeType;


        public ViewHolder(View view) {
            tv_tradeName = (TextView) view.findViewById(R.id.tv_04);
            tv_tradeDate = (TextView) view.findViewById(R.id.tv_03);
            tv_tradeMoeny = (TextView) view.findViewById(R.id.tv_02);
            tv_tradeType = (TextView) view.findViewById(R.id.tv_title);

        }
    }

}
