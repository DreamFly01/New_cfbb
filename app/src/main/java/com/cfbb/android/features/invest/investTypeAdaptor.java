package com.cfbb.android.features.invest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseadaptor.CommonListAdapter;
import com.cfbb.android.protocol.bean.ProductTypeBean;

/**
 * Created by MrChang45 on 2016/3/14.
 */
public class investTypeAdaptor extends CommonListAdapter<ProductTypeBean> {

    public String getSelected() {
        return selectedTypeName;
    }

    public void setSelected(String selected) {
        this.selectedTypeName = selected;
    }

    private String selectedTypeName="";
    public investTypeAdaptor(Context context,String selected) {
        super(context);
        this.selectedTypeName =selected;
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
        viewHolder.tv_name.setText(mDataSource.get(position).productTypeName);
        if(selectedTypeName.equals(mDataSource.get(position).productTypeName.trim()))
        {
            //选中状态
            viewHolder.tv_name.setTextColor(mContext.getResources().getColor(R.color.balck));
        }
        else {
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
