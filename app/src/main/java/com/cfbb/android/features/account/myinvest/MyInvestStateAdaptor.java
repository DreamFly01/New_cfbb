package com.cfbb.android.features.account.myinvest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseadaptor.CommonListAdapter;
import com.cfbb.android.commom.state.MyInvestStateEnum;

/**
 * Created by MrChang45 on 2016/3/30.
 */
public class MyInvestStateAdaptor extends CommonListAdapter<MyInvestStateBean> {

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    private String selected = MyInvestStateEnum.ALL_INVEST.getValue() + "";

    public MyInvestStateAdaptor(Context context, String selected) {
        super(context);
        this.selected = selected;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        LayoutInflater inflate = null;
        if (convertView == null) {
            inflate = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflate.inflate(R.layout.popupwindow_invest_tyoe_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_name.setText(mDataSource.get(position).investStateName);
        if (selected.equals(mDataSource.get(position).investStateType)) {
            //选中状态
            viewHolder.tv_name.setTextColor(mContext.getResources().getColor(R.color.balck));
        } else {
            viewHolder.tv_name.setTextColor(mContext.getResources().getColor(R.color.txt_9));
        }
        return convertView;
    }

    static final class ViewHolder {
        TextView tv_name;

        public ViewHolder(View view) {
            tv_name = (TextView) view.findViewById(R.id.tv_title);
        }
    }
}
