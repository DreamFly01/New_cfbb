package com.cfbb.android.features.account.releaseLoan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseadaptor.CommonListAdapter;
import com.cfbb.android.protocol.bean.LoanPersonInfo;

/**
 * @author MrChang45
 * @time 2016/11/11
 * @desc
 */
public class LoanPersonInfoAdapter extends CommonListAdapter<LoanPersonInfo.PersonInfo> {
    public LoanPersonInfoAdapter(Context context) {
        super(context);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LoanPersonInfo.PersonInfo model = mDataSource.get(position);
        ViewHolder holder = null;
        LayoutInflater inflate = null;
        if(convertView == null){
            inflate = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflate.inflate(R.layout.listview_loan_info, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.loanerInfo.setText(model.loanerInfo);
        holder.info.setText(model.info);
        return convertView;
    }

    public class ViewHolder {
        private TextView loanerInfo;
        private TextView info;
        public ViewHolder(View view) {
            loanerInfo = (TextView)view.findViewById(R.id.tv_04);
            info = (TextView)view.findViewById(R.id.tv_05);
        }
    }
}
