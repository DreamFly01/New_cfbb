package com.cfbb.android.widget.viewpagerindicator;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.cfbb.android.R;
import com.cfbb.android.commom.baseview.BaseFragment;
import com.cfbb.android.commom.utils.base.PhoneUtils;
import com.cfbb.android.commom.utils.others.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrChang45 on 2016/4/13.
 */
public class YCPagerIndicator extends HorizontalScrollView implements ViewPager.OnPageChangeListener, View.OnClickListener {

    //一个文本的额外宽度，主要是为了增加前后间距
    private static final float extraWidth = 30;

    private Context context;
    private List<BaseFragment> fragments;
    //字体大小
    private int textSize = 15;
    private ViewPager viewPager;
    //是否显示下划线
    private Boolean isShowUnderLine = false;
    //屏幕宽度
    private int windowWidth = 0;

    public YCPagerIndicator(Context context) {
        this(context, null);
    }

    public YCPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YCPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    //单个文本宽度集合
    private List<Integer> tv_width_list = new ArrayList<>();
    //文本集合
    private List<TextView> tv_list = new ArrayList<>();
    //水平Linearlayout
    private LinearLayout linearHorizontalLayout;
    //垂直LinearLayout
    private LinearLayout linearVerticalLayout;
    //LayoutParams参数
    private LinearLayout.LayoutParams layoutParams;
    //指示器
    private ImageView iv_indicator;

    //经过计算的最后额外宽度
    private int extraNeedWidth;

    //指示线默认高度
    private int lineHeight = 1;

    public void setLineHeight(int height) {
        this.lineHeight = height;
    }


    /***
     * 设置文本
     *
     * @param screenW 显示宽度
     * @param titles  文本集合
     */
    public void setTitles(int screenW, List<String> titles, Boolean isShowUnderLine) {

        this.isShowUnderLine = isShowUnderLine;

        //设置该参数 才会撑满
        setFillViewport(true);

        //得到屏幕宽度
        windowWidth = screenW;


        //构建垂直LinearLayout 白色背景
        linearVerticalLayout = new LinearLayout(context);
        linearVerticalLayout.setOrientation(LinearLayout.VERTICAL);
        layoutParams = new LinearLayout.LayoutParams(-3, LinearLayout.LayoutParams.MATCH_PARENT);
        linearVerticalLayout.setLayoutParams(layoutParams);
        linearVerticalLayout.setBackgroundColor(context.getResources().getColor(R.color.white));

        //构建水平的LinearLayout
        linearHorizontalLayout = new LinearLayout(context);
        linearHorizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
        layoutParams = new LinearLayout.LayoutParams(-3, 0);
        layoutParams.weight = 1;
        linearHorizontalLayout.setLayoutParams(layoutParams);
        linearHorizontalLayout.setBackgroundColor(context.getResources().getColor(R.color.white));


        //文本整体宽度
        float totalWidth = 0;
        for (int i = 0; i < titles.size(); i++) {
            TextView tv = new TextView(context);
            tv.setText(titles.get(i));
            tv.setTag(i);
            tv.setOnClickListener(this);
            tv.setTextColor(context.getResources().getColor(R.color.txt_3));
            tv.setTextSize(textSize);
            tv.setGravity(Gravity.CENTER);
            TextPaint textPaint = tv.getPaint();
            float textPaintWidth = textPaint.measureText(titles.get(i));
            totalWidth += textPaintWidth;
            tv_width_list.add((int) textPaintWidth);
            tv_list.add(tv);
            linearHorizontalLayout.addView(tv);
        }

        //将水平LinearLayout添加到垂直LinearLayout
        linearVerticalLayout.addView(linearHorizontalLayout);


        //计算文本额外需要的宽度
        if (totalWidth < windowWidth) {
            if ((windowWidth - totalWidth) > (extraWidth * tv_list.size())) {
                extraNeedWidth = (int) ((windowWidth - totalWidth) / tv_list.size());
            } else {
                extraNeedWidth = (int) extraWidth;
            }
        } else {
            extraNeedWidth = (int) extraWidth;
        }
        setTextViewWidth(extraNeedWidth);


        if (isShowUnderLine) {
            //构建指示条
            iv_indicator = new ImageView(context);
            iv_indicator.setBackgroundResource(R.drawable.red_line);
            //默认第一个title宽度
            layoutParams = new LinearLayout.LayoutParams(tv_width_list.get(0) + extraNeedWidth, PhoneUtils.dip2px(context, lineHeight));
            iv_indicator.setLayoutParams(layoutParams);

            //添加指示条
            linearVerticalLayout.addView(iv_indicator);
        }

        //添加垂直LinearLayout到view
        addView(linearVerticalLayout);

        //默认第一个显示红色被选中
        tv_list.get(0).setTextColor(context.getResources().getColor(R.color.txt_red));

    }

    /***
     * 设置文本宽度
     *
     * @param extraNeedWidth 额外需要的宽度
     */
    private void setTextViewWidth(int extraNeedWidth) {
        if (tv_list != null && tv_list.size() > 0) {
            for (int i = 0; i < tv_list.size(); i++) {
                tv_list.get(i).setLayoutParams(new LinearLayout.LayoutParams((extraNeedWidth + tv_width_list.get(i)), ViewGroup.LayoutParams.MATCH_PARENT));
            }
        }
    }


    /***
     * 计算从0到position的宽度
     *
     * @param position
     * @return
     */
    private int addTotalWidth(int position) {
        int result = 0;
        if (position >= 1) {
            for (int i = 0; i < position; i++) {
                result += (tv_width_list.get(i) + extraNeedWidth);
            }
        }
        return result;
    }

    private int prePosition = 0;

    // position :往右滑动，下一个页面序号 全部滑动完毕页面序号已经是下一个序号所以不变 , 往左滑动 ，是当前页面，全部滑动完毕是下一个页面序号。
    // positionOffset:当前页面偏移的百分比
    // arg2:当前页面偏移的像素位置
    @Override
    public void onPageScrolled(int position, float positionOffset, int arg2) {

         L.e("positionOffset=" + positionOffset + ",position=" + position + ",arg2=" + arg2);
        // 滚动文字
        if (arg2 != 0) {
            scrollTo((int) ((tv_width_list.get(position) + extraNeedWidth) * positionOffset + addTotalWidth(position)), 0);
        }

        //是否显示指示条
        if (isShowUnderLine) {

            if (arg2 != 0) {
                //移动指示线
                layoutParams.leftMargin = (int) ((tv_width_list.get(position) + extraNeedWidth) * positionOffset + addTotalWidth(position));

                //计算宽度变化
                if (prePosition == position) {

                    //左滑  position = 当前页面序号 prePosition前一个页面序号
                    //判断是否到达最后一个
                    if (position < tv_width_list.size() - 1) {

                        // 如果下一个宽度大于当前
                        if (tv_width_list.get(position) > tv_width_list.get(position + 1)) {
                            layoutParams.width = (int) (tv_width_list.get(position) + extraNeedWidth - (tv_width_list.get(position) - tv_width_list.get(position + 1)) * positionOffset);
                        } else if (tv_width_list.get(position) < tv_width_list.get(position + 1)) {
                            // 如果下一个宽度小于当前
                            layoutParams.width = (int) (tv_width_list.get(position) + extraNeedWidth + (tv_width_list.get(position + 1) - tv_width_list.get(position)) * positionOffset);
                        } else {
                            // 如果下一个宽度等 于当前
                            layoutParams.width = (tv_width_list.get(position) + extraNeedWidth);
                        }
                    }

                } else {

                    //右滑 position = 下一个页面序号  prePosition当前页面序号
                    //判断是否到达最后一个
                    if (position > 0) {

                        if (tv_width_list.get(prePosition) > tv_width_list.get(position)) {
                            layoutParams.width = (int) (tv_width_list.get(prePosition) + extraNeedWidth - (tv_width_list.get(prePosition) - tv_width_list.get(position)) * (1 - positionOffset));
                        } else if (tv_width_list.get(prePosition) < tv_width_list.get(position)) {
                            layoutParams.width = (int) (tv_width_list.get(prePosition) + extraNeedWidth + (tv_width_list.get(position) - tv_width_list.get(prePosition)) * (1 - positionOffset));
                        } else {
                            layoutParams.width = (tv_width_list.get(prePosition) + extraNeedWidth);
                        }

                    }

                }

                //重新定位 确定宽度
                iv_indicator.setLayoutParams(layoutParams);


            } else if (arg2 == 0) {
                prePosition = position;
            }
        }
    }

    @Override
    public void onPageSelected(int position) {

        prePosition = position;
        for (int i = 0; i < tv_list.size(); i++) {
            if (i == position) {
                tv_list.get(i).setTextColor(context.getResources().getColor(R.color.txt_red));
            } else {
                tv_list.get(i).setTextColor(context.getResources().getColor(R.color.txt_3));
            }
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // L.e("prePosition state=" + prePosition);
    }


    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        if (null != fragments) {
            this.fragments.get(position).onClickFragment();
        }
        viewPager.setCurrentItem(position);
        StatService.onEvent(context,tv_list.get(position).getText().toString(),"点击",1);
    }

    public void setFragments(List<BaseFragment> fragments) {
        this.fragments = fragments;
    }

    public List<BaseFragment> getFragments() {
        return fragments;
    }


    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

}
