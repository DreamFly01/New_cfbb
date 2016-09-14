package com.cfbb.android.features.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cfbb.android.R;
import com.cfbb.android.commom.config.Const;
import com.cfbb.android.commom.state.InvestStateEnum;
import com.cfbb.android.commom.utils.image.ImageWithGlideUtils;
import com.cfbb.android.commom.utils.others.Utils;
import com.cfbb.android.protocol.bean.HomeInfoBean;
import com.cfbb.android.widget.InvestPrograssView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2016/3/11.
 */
public class homeAdaptor extends BaseAdapter {

    private Context mContext;
    private List<HomeInfoBean.OtherproductsBean> data = new ArrayList<>();

    private homeAdaptorClickInterface mHomeAdaptorClickInterface;

    public interface homeAdaptorClickInterface {
        void OnInvestClick(int position);
    }

    public homeAdaptor(Context context, homeAdaptorClickInterface mHomeAdaptorClickInterface) {
        mContext = context;
        this.mHomeAdaptorClickInterface = mHomeAdaptorClickInterface;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        LayoutInflater inflate = null;
        HomeInfoBean.OtherproductsBean model = data.get(position);
        if (convertView == null) {
            inflate = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflate.inflate(R.layout.home_listview_item, null);
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
        if (model.canInvest == InvestStateEnum.ENABLE_INVEST.getValue()) {
            viewHolder.btn_invest.setEnabled(true);
        } else {
            viewHolder.btn_invest.setEnabled(false);
        }
        viewHolder.btn_invest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHomeAdaptorClickInterface.OnInvestClick(Integer.parseInt(v.getTag().toString()));
            }
        });
        viewHolder.investPrograssView.setCurrentCount(Float.parseFloat(model.prograss));
        viewHolder.tv_prograss.setText(model.prograss + Const.PER);
        viewHolder.tv_title.setText(model.productName);
        viewHolder.tv_rate.setText(model.yearOfRate);
        viewHolder.tv_unit_invest_date.setText(Utils.getInvestUnitNameByFlag(model.unit));
        viewHolder.tv_mounths.setText(model.investDate);
        ImageWithGlideUtils.lodeFromUrl(model.flagIconUrl,R.mipmap.product_type_default_ico,viewHolder.iv_ico,mContext);
        return convertView;
    }

    public void setData(List<HomeInfoBean.OtherproductsBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    static final class ViewHolder {
        TextView tv_title;
        TextView tv_rate;
        TextView tv_mounths;
        TextView tv_prograss;
        ImageView iv_ico;
        Button btn_invest;
        TextView tv_unit_invest_date;
        InvestPrograssView investPrograssView;

        public ViewHolder(View view) {
            tv_unit_invest_date = (TextView) view.findViewById(R.id.tv_06);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_rate = (TextView) view.findViewById(R.id.tv_02);
            tv_mounths = (TextView) view.findViewById(R.id.tv_05);
            tv_prograss = (TextView) view.findViewById(R.id.tv_08);
            iv_ico = (ImageView) view.findViewById(R.id.iv_03);
            btn_invest = (Button) view.findViewById(R.id.btn_invest);
            investPrograssView = (InvestPrograssView) view.findViewById(R.id.pb_progressbar);
        }

    }
}
