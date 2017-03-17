package com.cfbb.android.features.account.releaseLoan;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseadaptor.CommonListAdapter;
import com.cfbb.android.features.product.ProductDetailsActivity;
import com.cfbb.android.protocol.bean.AuditStateBean;

/**
 * @author MrChang45
 * @time 2016/11/14
 * @desc
 */
public class MyProductInfoAdapter extends CommonListAdapter<AuditStateBean.AuditInfo>{
    public MyProductInfoAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final AuditStateBean.AuditInfo model = mDataSource.get(position);
        ViewHold holder = null;
        LayoutInflater inflate = null;
        if(convertView == null){
            inflate = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflate.inflate(R.layout.invest_listview_item1, null);
            holder = new ViewHold(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHold) convertView.getTag();
        }
        holder.productName.setText(model.productName);
        holder.yearOfRate.setText(model.yearOfRate);
        holder.investDate.setText(model.investDate);
        String str = model.remiansMoeny;
        String str1 = str.substring(0,str.length()-3);
        long money = Integer.parseInt(str1);
        if(money>=10000){
           double money1 = (double) money/10000;
            holder.remiansMoeny.setText(money1+"");
            holder.yuan.setText("万");
        }else{
        holder.remiansMoeny.setText(money+"");
        }
        holder.invest.setText(model.btntxt);
        if(model.unit==1){
            holder.unit.setText("天");
        }
        if(model.unit==3){
            holder.unit.setText("个月");
        }
        if(model.canInvest==1){
            holder.invest.setEnabled(true);
            holder.invest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ProductDetailsActivity.class);
                    intent.putExtra("product_id",model.prodcutId);
                    intent.putExtra("loan_typeid","1");
                    intent.putExtra("flag","1");
                    System.out.println("借款页面------prodcutId------"+model.prodcutId+"------loanTypeId------"+model.loanTypeId);
                    mContext.startActivity(intent);
                }
            });
        }
        if(model.canInvest==-1){
            holder.invest.setEnabled(false);
        }
        return convertView;

    }

    public class ViewHold{
        private TextView productName;//借款标题
        private TextView yearOfRate;//年化收益
        private TextView investDate;//投资期限
        private TextView remiansMoeny;//剩余可投金额
        private TextView unit;//投资期限单位
        private Button invest;//能否点击
        private TextView yuan;//投资单位


        public ViewHold(View view) {
            productName = (TextView)view.findViewById(R.id.tv_title);
            yearOfRate = (TextView)view.findViewById(R.id.tv_02);
            investDate = (TextView)view.findViewById(R.id.tv_05);
            unit = (TextView)view.findViewById(R.id.tv_06);
            remiansMoeny =(TextView)view.findViewById(R.id.tv_08);
            invest = (Button)view.findViewById(R.id.btn_invest);
            yuan = (TextView)view.findViewById(R.id.tv_09);
        }
    }
}
