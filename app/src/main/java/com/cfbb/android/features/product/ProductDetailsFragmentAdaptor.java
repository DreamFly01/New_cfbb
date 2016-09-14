package com.cfbb.android.features.product;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cfbb.android.commom.baseview.BaseFragment;

import java.util.List;

/**
 * Created by MrChang45 on 2016/3/17.
 */
public class ProductDetailsFragmentAdaptor extends FragmentPagerAdapter {

    private List<BaseFragment> fragments;
    private List<String> titles;

    public ProductDetailsFragmentAdaptor(FragmentManager fm, List<BaseFragment> fragments, List<String> titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
}
