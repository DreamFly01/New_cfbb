package com.cfbb.android.features.invest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseadaptor.CommonListAdapter;
import com.cfbb.android.protocol.bean.RatesBean;

import java.util.List;

/**
 * @author FDL
 * @time 2017/03/10
 * @desc
 */
public class RateAdapter extends CommonListAdapter<RatesBean.Item1> {
    private String flag;
    private String state;
    private int stateFlag;

    public RateAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold holder = null;
        LayoutInflater inflate = null;
        if (convertView == null) {
            inflate = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflate.inflate(R.layout.listview_my_rate_item, null);
            holder = new ViewHold(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHold) convertView.getTag();
        }
        if(state!=null){
        if (stateFlag == 1) {
            holder.state.setVisibility(View.GONE);
        } else if (Integer.parseInt(state) == position) {
            holder.state.setVisibility(View.VISIBLE);
            System.out.println(state+"--------"+position+"---------"+mDataSource.size());
        }else{
            holder.state.setVisibility(View.GONE);
        }
        }else {
            holder.state.setVisibility(View.GONE);
        }


//        if (state != null) {
//            if (Integer.parseInt(state) == position) {
//                holder.state.setVisibility(View.VISIBLE);
//            }
//        }
        holder.rates.setText(mDataSource.get(position).interest_rate + "%");
        holder.dateTime.setText("(" + mDataSource.get(position).interest_beginTime + "~" + mDataSource.get(position).interest_endTime + ")");
        holder.name.setText(mDataSource.get(position).interest_name);
        return convertView;
    }

    @Override
    public void addAll(List<RatesBean.Item1> items) {
        super.addAll(items);
    }

    public void setFlag(String flag) {
        this.flag = flag;
        notifyDataSetChanged();
    }

    public void setState(String state) {
        this.state = state;
        notifyDataSetChanged();
    }

    public void setStateFlag(int stateFlag) {
        this.stateFlag = stateFlag;
        notifyDataSetChanged();
    }

    public static class ViewHold {
        TextView rates;
        TextView dateTime;
        TextView name;
        ImageView state;

        public ViewHold(View view) {
            name = (TextView) view.findViewById(R.id.tv_04);
            rates = (TextView) view.findViewById(R.id.tv_rates);
            dateTime = (TextView) view.findViewById(R.id.tv_ratestime);
            state = (ImageView) view.findViewById(R.id.iv_01);
        }
    }
}
