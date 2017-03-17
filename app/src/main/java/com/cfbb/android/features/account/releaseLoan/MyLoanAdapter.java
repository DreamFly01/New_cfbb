package com.cfbb.android.features.account.releaseLoan;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseadaptor.CommonListAdapter;
import com.cfbb.android.protocol.bean.MyLoanBean;

/**
 * @author MrChang45
 * @time 2016/11/9
 * @desc
 */
public class MyLoanAdapter extends CommonListAdapter<MyLoanBean.MyLoanArray> {
    public MyLoanAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyLoanBean.MyLoanArray model = mDataSource.get(position);
        ViewHold holder = null;
        LayoutInflater inflate = null;
        if (convertView == null) {
            inflate = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflate.inflate(R.layout.listview_my_loan_item, null);
            holder = new ViewHold(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHold) convertView.getTag();
        }
        holder.borrowName.setText(model.loanTitle);
        String str = model.loanMoeny;
        String str1 = str.substring(0, str.length() - 4);
        long money = Integer.parseInt(str1);
        System.out.println(money);
        if(money>=10000){
            double money1 = 0 ;
            money1 = (double) money/10000;
            System.out.println("-----------------"+money+"------------------------");
            holder.borrowMoney.setText(money1+"");
            holder.yuan.setText("万");
        }else{
            holder.borrowMoney.setText(model.loanMoeny);
            holder.yuan.setText("元");
        }
        holder.borrowStat.setText(model.loanSateText);
        holder.bidDay.setText(model.bindingDays);
        holder.time.setText(model.loanDate);
        holder.repayModeId.setText(model.returnWay);
        return convertView;
    }

    public static class ViewHold {
        TextView borrowName; //借款名称
        TextView borrowMoney;//借款金额
        TextView borrowStat;//借款状态
        TextView repayModeId;//还款方式
        TextView bidDay;//竞标天数
        TextView time;//申请时间
        TextView yuan;//借款单位

        public ViewHold(View view) {
            borrowName = (TextView) view.findViewById(R.id.tv_04);
            borrowMoney = (TextView) view.findViewById(R.id.tv_06);
            borrowStat = (TextView) view.findViewById(R.id.tv_08);
            repayModeId = (TextView) view.findViewById(R.id.tv_05);
            bidDay = (TextView) view.findViewById(R.id.tv_07);
            time = (TextView) view.findViewById(R.id.tv_09);
            yuan = (TextView)view.findViewById(R.id.tv_10);
        }
    }
}
