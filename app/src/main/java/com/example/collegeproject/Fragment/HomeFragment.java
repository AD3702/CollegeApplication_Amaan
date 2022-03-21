package com.example.collegeproject.Fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.collegeproject.Adapter.MyAdapter;
import com.example.collegeproject.R;
import com.google.android.material.tabs.TabLayout;

public class HomeFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager frameLayout;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    public static HomeFragment homeFragment = new HomeFragment();
    public static int frame_id_int;
    Toolbar toolbar_home;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initializeUI(view);
        final MyAdapter adapter = new MyAdapter(getActivity(), getActivity().getSupportFragmentManager(), tabLayout.getTabCount(), tabLayout, toolbar_home);
        frameLayout.setAdapter(adapter);
        frameLayout.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                frameLayout.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    toolbar_home.setTitle("Apply For");
                } else {
                    toolbar_home.setTitle("Applied");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                frameLayout.setCurrentItem(tab.getPosition());
            }
        });
        Apply_For_Fragment apply_for_fragment = new Apply_For_Fragment();
        fragmentTransaction.replace(R.id.frame_layout, apply_for_fragment);
        fragmentTransaction.commit();
        toolbar_home.setBackground(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.main_color)));
        toolbar_home.setTitleTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        toolbar_home.setTitle("Apply For");
        return view;
    }

    public void initializeUI(View v) {
        tabLayout = (TabLayout) v.findViewById(R.id.main_screen_tablayout);
        frameLayout = v.findViewById(R.id.frame_layout);
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        frame_id_int = R.id.frame_layout;
        toolbar_home = v.findViewById(R.id.toolbar_actionbar_home);
        fragment = null;
    }



}