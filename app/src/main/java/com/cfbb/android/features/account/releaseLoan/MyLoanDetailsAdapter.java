package com.cfbb.android.features.account.releaseLoan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseadaptor.CommonListAdapter;
import com.cfbb.android.commom.utils.image.ImageWithGlideUtils;
import com.cfbb.android.protocol.bean.MyLoanDetailsBean;

/**
 * @author MrChang45
 * @time 2016/11/10
 * @desc
 */
public class MyLoanDetailsAdapter extends CommonListAdapter<MyLoanDetailsBean.ImageInfo> {
    public MyLoanDetailsAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyLoanDetailsBean.ImageInfo model = mDataSource.get(position);
        ViewHolder viewHolder = null;
        LayoutInflater inflate = null;
        if (convertView == null) {
            inflate = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflate.inflate(R.layout.item_loan_details, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(model.name);
        ImageWithGlideUtils.lodeFromUrl(model.url,R.mipmap.default_sh_bg,viewHolder.img,mContext);
        return convertView;
    }

    public static class ViewHolder{
        private ImageView img;
        private TextView name;

        public ViewHolder(View view) {
            img = (ImageView)view.findViewById(R.id.iv_07);
            name = (TextView)view.findViewById(R.id.tv_07);
        }
    }
}
