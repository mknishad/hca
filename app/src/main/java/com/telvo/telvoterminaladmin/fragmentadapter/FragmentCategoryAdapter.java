package com.telvo.telvoterminaladmin.fragmentadapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


/**
 * Created by Invariant on 10/23/17.
 */

public class FragmentCategoryAdapter extends FragmentPagerAdapter {
    private List<FragmentHolder> fragmentHolderList;
    public FragmentCategoryAdapter(FragmentManager fm,List<FragmentHolder> fragmentHolderList) {
        super(fm);
        this.fragmentHolderList=fragmentHolderList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentHolderList.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return fragmentHolderList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentHolderList.get(position).getTitle();
    }
}
