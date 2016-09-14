package com.cfbb.android.features.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseadaptor.CommonListAdapter;
import com.cfbb.android.commom.utils.image.ImageWithGlideUtils;
import com.cfbb.android.protocol.bean.VertifyInfoBean;

/**
 * Created by MrChang45 on 2016/3/25.
 * 审核信息 审核资料
 */
public class GridViewVertifyImageAdptor extends CommonListAdapter<VertifyInfoBean.VertifyImageGroupInfo> {


    public GridViewVertifyImageAdptor(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        LayoutInflater inflate = null;
        VertifyInfoBean.VertifyImageGroupInfo model = mDataSource.get(position);
        if (convertView == null) {
            inflate = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflate.inflate(R.layout.product_vertify_gridview_images, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_name.setText(model.name);
        ImageWithGlideUtils.lodeFromUrl(model.url,R.mipmap.default_sh_bg,viewHolder.iv_flag,mContext);
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
