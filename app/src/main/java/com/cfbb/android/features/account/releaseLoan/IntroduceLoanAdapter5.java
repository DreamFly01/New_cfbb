package com.cfbb.android.features.account.releaseLoan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cfbb.android.R;
import com.cfbb.android.commom.utils.image.ImageWithGlideUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @author MrChang45
 * @time 2016/12/22
 * @desc
 */
public class IntroduceLoanAdapter5 extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<String> list = new ArrayList<>();
    List<String> proofMaterial = new ArrayList<>();
    public IntroduceLoanAdapter5(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.introduce_loan_item, null);
            holder.imgAdd = (ImageView)convertView.findViewById(R.id.imgAdd);
            holder.imgDel = (ImageView)convertView.findViewById(R.id.imgDel);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageWithGlideUtils.lodeFromUrlRoundTransformImg(list.get(position), holder.imgAdd, context);
        holder.imgDel.setVisibility(View.VISIBLE);
        final ViewHolder finalHolder = holder;
        holder.imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(list.get(position));
                proofMaterial.remove(proofMaterial.get(position));
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public void addList(List<String> strings) {
//        this.list.addAll(strings);
//        Collections.reverse(list);
        this.list = strings;
        notifyDataSetChanged();
    }
    public class ViewHolder {
        private ImageView imgAdd, imgDel;

    }
    public void setImgList(List<String> proofMaterial){
        this.proofMaterial = proofMaterial;
        notifyDataSetChanged();
    }
}
