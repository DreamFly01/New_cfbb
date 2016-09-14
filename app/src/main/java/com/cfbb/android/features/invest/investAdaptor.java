package com.cfbb.android.features.invest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.baseadaptor.CommonListAdapter;
import com.cfbb.android.commom.state.InvestStateEnum;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.commom.utils.others.Utils;
import com.cfbb.android.protocol.bean.ProductInfoBean;


/**
 * Created by MrChang45 on 2016/3/22.
 */
public class investAdaptor extends CommonListAdapter<ProductInfoBean.ProductInfo> {


    public investAdaptor(Context context, InvestOnclickLisenr investOnclickLisenr) {
        super(context);
        mContext = context;
        this.investOnclickLisenr = investOnclickLisenr;
    }

    public interface InvestOnclickLisenr {
        void onClick(int position);
    }

    private InvestOnclickLisenr investOnclickLisenr;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProductInfoBean.ProductInfo model = mDataSource.get(position);
        ViewHolder viewHolder = null;
        LayoutInflater inflate = null;
        if (convertView == null) {
            inflate = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflate.inflate(R.layout.invest_listview_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (model.canInvest == InvestStateEnum.DISABLE_INVEST.getValue()) {
            viewHolder.btn_invest.setEnabled(false);
        } else {
            viewHolder.btn_invest.setEnabled(true);
        }
        viewHolder.btn_invest.setText(model.btntxt);
        viewHolder.btn_invest.setTag(position);
        viewHolder.btn_invest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(investOnclickLisenr != null) {
                    if(StrUtil.isNumeric(v.getTag().toString()))
                    {
                        investOnclickLisenr.onClick(Integer.parseInt(v.getTag().toString()));
                    }
                }
            }
        });
        viewHolder.tv_period_date.setText(model.investDate);
        viewHolder.tv_productName.setText(model.productName);
        viewHolder.tv_yearOfRate.setText(model.yearOfRate);
       // Utils.FillterFLagIcons(viewHolder.iv_dan, viewHolder.iv_dai, model.flagIcon);
        viewHolder.tv_unit.setText(Utils.getInvestUnitNameByFlag(model.unit));
        Utils.FillMoenyAndUnit(viewHolder.tv_remians_money, viewHolder.tv_moeny_unit, model.remiansMoeny);
        return convertView;
    }

    private static final class ViewHolder {
        TextView tv_productName;
        TextView tv_yearOfRate;
        TextView tv_period_date;
        TextView tv_unit;
        TextView tv_remians_money;
        Button btn_invest;
        ImageView iv_dan;
        ImageView iv_dai;
        TextView tv_moeny_unit;

        public ViewHolder(View view) {
            tv_productName = (TextView) view.findViewById(R.id.tv_title);
            tv_yearOfRate = (TextView) view.findViewById(R.id.tv_02);
            tv_period_date = (TextView) view.findViewById(R.id.tv_05);
            tv_unit = (TextView) view.findViewById(R.id.tv_06);
            tv_remians_money = (TextView) view.findViewById(R.id.tv_08);
            btn_invest = (Button) view.findViewById(R.id.btn_invest);
            iv_dan = (ImageView) view.findViewById(R.id.iv_01);
            iv_dai = (ImageView) view.findViewById(R.id.iv_02);
            tv_moeny_unit = (TextView) view.findViewById(R.id.tv_back);
        }


    }

}
