package com.cfbb.android.features.account.releaseLoan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cfbb.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MrChang45
 * @time 2017/1/20
 * @desc
 */
public class IntroduceListAdapter extends BaseAdapter {
    private List<String> list = new ArrayList<>();
    private Context context;
    private int statu;
    private int txtStatu;
    private LayoutInflater inflater;
    private List<List<String>> imgData = new ArrayList<>();

    public IntroduceListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
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
            convertView = inflater.inflate(R.layout.item_introduce, null);
            holder.txtName = (TextView) convertView.findViewById(R.id.txt_01);
            holder.txtStatu = (TextView) convertView.findViewById(R.id.txt_02);
            holder.reduce = (ImageView) convertView.findViewById(R.id.img_reduce);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (statu == 1) {
            holder.reduce.setVisibility(View.GONE);
        }
        if (statu == 2) {
            holder.reduce.setVisibility(View.VISIBLE);
        }

        holder.reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                if(imgData.size()>=(position+1)){

                if (imgData.get(position) != null) {
                    imgData.remove(position);
                }
                }
                notifyDataSetChanged();
            }
        });
//        if(imgData.size()>0){
//            if (list.size()-imgData.size()>0) {
//                holder.txtStatu.setVisibility(View.VISIBLE);
//            }
//        }
        if (txtStatu == 0) {
            holder.txtStatu.setVisibility(View.GONE);
        }
        holder.txtName.setText(list.get(position));
        return convertView;
    }

    public void setList(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setImgData(List<List<String>> imgData) {
        this.imgData = imgData;
        notifyDataSetChanged();
    }

    public void setReduceStatu(int statu) {
        this.statu = statu;
        notifyDataSetChanged();
    }

    public void setTxtStatu(int txtStatu) {
        this.txtStatu = txtStatu;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView txtName;
        TextView txtStatu;
        ImageView reduce;
    }
}
