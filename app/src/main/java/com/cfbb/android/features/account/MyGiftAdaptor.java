package com.cfbb.android.features.account;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseadaptor.CommonListAdapter;
import com.cfbb.android.commom.utils.image.ImageWithGlideUtils;
import com.cfbb.android.protocol.bean.MyGiftBean;

/**
 * @author MrChang45
 * @time 2016/5/16
 * @desc
 */
public class MyGiftAdaptor extends CommonListAdapter<MyGiftBean> {


    public MyGiftAdaptor(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyGiftBean model = mDataSource.get(position);
        ViewHolder viewHolder = null;
        LayoutInflater inflate = null;
        if (convertView == null) {
            inflate = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflate.inflate(R.layout.gridview_my_gfit_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_title.setText(model.title);
        viewHolder.tv_descri.setText(model.describe);
        viewHolder.tv_date.setText(model.date);
        try {
            viewHolder.tv_state.setTextColor(Color.parseColor("#" + model.txtColor));
        } catch (Exception e) {
            viewHolder.tv_state.setTextColor(Color.BLACK);
        }
        viewHolder.tv_state.setText(model.state);
        ImageWithGlideUtils.lodeFromUrl(model.imgUrl,R.mipmap.my_gift_default,viewHolder.iv_photo,mContext);
        return convertView;
    }

    private static final class ViewHolder {
        TextView tv_title;
        TextView tv_descri;
        TextView tv_date;
        TextView tv_state;
        ImageView iv_photo;

        public ViewHolder(View view) {
            tv_title = (TextView) view.findViewById(R.id.tv_02);
            tv_descri = (TextView) view.findViewById(R.id.tv_03);
            tv_date = (TextView) view.findViewById(R.id.tv_04);
            tv_state = (TextView) view.findViewById(R.id.tv_05);
            iv_photo = (ImageView) view.findViewById(R.id.iv_01);
        }
    }
}
