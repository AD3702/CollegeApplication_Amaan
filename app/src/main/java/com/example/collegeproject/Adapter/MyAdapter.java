package com.example.collegeproject.Adapter;


import android.content.Context;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.collegeproject.Fragment.Applied_Fragment;
import com.example.collegeproject.Fragment.Apply_For_Fragment;
import com.google.android.material.tabs.TabLayout;

public class MyAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;
    TabLayout tabLayout;
    Toolbar toolbar;

    public MyAdapter(Context context, FragmentManager fm, int totalTabs, TabLayout tabLayout,Toolbar toolbar) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
        this.tabLayout = tabLayout;
        this.toolbar = toolbar;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Apply_For_Fragment apply_for_fragment = new Apply_For_Fragment();
                return apply_for_fragment;
            case 1:
                Applied_Fragment applied_fragment = new Applied_Fragment();
                return applied_fragment;
            default:
                return null;
        }
    }

    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
