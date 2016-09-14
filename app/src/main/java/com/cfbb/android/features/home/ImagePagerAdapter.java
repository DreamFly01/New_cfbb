package com.cfbb.android.features.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cfbb.android.R;
import com.cfbb.android.commom.utils.image.ImageWithGlideUtils;
import com.cfbb.android.protocol.bean.HomeInfoBean;

import java.util.List;

/**
 * Created by MrChange45 on 2016/3/21.
 */
public class ImagePagerAdapter extends BaseAdapter implements View.OnClickListener {

    private List<HomeInfoBean.AdsBean> adsBeen;
    private Context context;
    private int size;
    private ImageClickListener imageClickListener;

    @Override
    public void onClick(View v) {
        imageClickListener.onImageClick(v.getTag().toString());
    }

    public interface ImageClickListener
    {
       void  onImageClick(String url);
    }


    public ImagePagerAdapter(List<HomeInfoBean.AdsBean> adsBeen, Context context,ImageClickListener imageClickListener)
    {
        this.context =context;
        this.adsBeen = adsBeen;
        if (adsBeen != null) {
            this.size = adsBeen.size();
        }
        this.imageClickListener = imageClickListener;
    }
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object getItem(int position) {
        return adsBeen.get(position % size);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        LayoutInflater inflate = null;
        HomeInfoBean.AdsBean model = adsBeen.get(position % size);
        if (convertView == null) {
            inflate = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflate.inflate(R.layout.banner_view_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ImageWithGlideUtils.lodeFromUrl(model.imageUrl,R.mipmap.default_banner_bg,viewHolder.imageView,context);
        viewHolder.imageView.setTag(model.clickurl);
        viewHolder.imageView.setOnClickListener(this);
        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
        public ViewHolder(View view)
        {
            imageView = (ImageView) view.findViewById(R.id.iv_01);
        }
    }
}
