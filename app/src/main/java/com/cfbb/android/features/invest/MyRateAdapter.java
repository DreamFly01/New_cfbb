package com.cfbb.android.features.invest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseadaptor.CommonListAdapter;
import com.cfbb.android.protocol.bean.MyRatesBean;
import com.cfbb.android.protocol.bean.RatesBean;

import java.util.List;

/**
 * @author FDL
 * @time 2017/03/10
 * @desc
 */
public class MyRateAdapter extends CommonListAdapter<MyRatesBean> {
    private String flag;

    public MyRateAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold holder = null;
        LayoutInflater inflate = null;
        if (convertView == null) {
            inflate = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflate.inflate(R.layout.activity_item_rates, null);
            holder = new ViewHold(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHold) convertView.getTag();
        }
        holder.rates.setText(mDataSource.get(position).rate);
        holder.name.setText(mDataSource.get(position).name);
        holder.dateTime.setText(mDataSource.get(position).beginTime+"~"+mDataSource.get(position).endTime);
        return convertView;
    }

    public void setFlag(String flag) {
        this.flag = flag;
        notifyDataSetChanged();
    }

    public static class ViewHold {
        TextView rates;
        TextView dateTime;
        TextView name;

        public ViewHold(View view) {
            name = (TextView)view.findViewById(R.id.tv_05) ;
            rates = (TextView) view.findViewById(R.id.tv_04);
            dateTime = (TextView) view.findViewById(R.id.tv_06);
        }
    }
}
