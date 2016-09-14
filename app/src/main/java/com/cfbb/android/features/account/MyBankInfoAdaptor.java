package com.cfbb.android.features.account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseadaptor.CommonListAdapter;
import com.cfbb.android.commom.utils.image.ImageWithGlideUtils;
import com.cfbb.android.protocol.bean.MyBankInfoBean;

/**
 * @author MrChang45
 * @time 2016/5/17
 * @desc
 */
public class MyBankInfoAdaptor extends CommonListAdapter<MyBankInfoBean> {


    public MyBankInfoAdaptor(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyBankInfoBean model = mDataSource.get(position);
        ViewHolder viewHolder = null;
        LayoutInflater inflate = null;
        if (convertView == null) {
            inflate = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflate.inflate(R.layout.listview_my_bank_info_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_name.setText(model.bankNum);
        ImageWithGlideUtils.lodeFromUrl(model.imageUrl,viewHolder.iv_bank_logo,  mContext);
        return convertView;
    }

    private static final class ViewHolder {
        TextView tv_name;
        ImageView iv_bank_logo;

        public ViewHolder(View view) {
            tv_name = (TextView) view.findViewById(R.id.tv_02);
            iv_bank_logo = (ImageView) view.findViewById(R.id.iv_01);
        }
    }

}
