package com.cfbb.android.features.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseadaptor.CommonListAdapter;
import com.cfbb.android.commom.state.VertifyStateEnum;
import com.cfbb.android.protocol.bean.VertifyInfoBean;

/**
 * Created by MrChang45 on 2016/3/25.
 * 审核信息->审核结果
 */
public class GridViewVertifyResultAdaptor extends CommonListAdapter<VertifyInfoBean.VertifyGroupInfo> {

    public GridViewVertifyResultAdaptor(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        LayoutInflater inflate = null;
        VertifyInfoBean.VertifyGroupInfo model = mDataSource.get(position);
        if (convertView == null) {
            inflate = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflate.inflate(R.layout.product_vertify_gridview_vertify_result, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_name.setText(model.vertifyName);
        if (model.vertifyResult == VertifyStateEnum.PASS.getValue()) {
            viewHolder.iv_flag.setImageResource(R.mipmap.cross_ico);
        } else {
            viewHolder.iv_flag.setImageResource(R.mipmap.no_cross_ico);
        }
        return convertView;
    }

    public static final class ViewHolder {
        TextView tv_name;
        ImageView iv_flag;

        public ViewHolder(View v) {
            tv_name = (TextView) v.findViewById(R.id.tv_title);
            iv_flag = (ImageView) v.findViewById(R.id.iv_01);
        }
    }
}
