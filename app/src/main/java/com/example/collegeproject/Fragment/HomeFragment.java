package com.example.collegeproject.Fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.collegeproject.R;
import com.google.android.material.tabs.TabLayout;

public class HomeFragment extends Fragment {

    TabLayout tabLayout;
    FrameLayout frameLayout;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    public static int frame_id_int;
    Toolbar toolbar_home;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initializeUI(view);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new Apply_For_Fragment();
                        toolbar_home.setTitle("Apply For");
                        break;
                    case 1:
                        fragment = new Applied_Fragment();
                        toolbar_home.setTitle("Applied");
                        break;
                }
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frame_layout, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new Apply_For_Fragment();
                        toolbar_home.setTitle("Apply For");
                        break;
                    case 1:
                        fragment = new Applied_Fragment();
                        toolbar_home.setTitle("Applied");
                        break;
                }
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frame_layout, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
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
        frameLayout = (FrameLayout) v.findViewById(R.id.frame_layout);
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        frame_id_int = R.id.frame_layout;
        toolbar_home = v.findViewById(R.id.toolbar_actionbar_home);
        fragment = null;
    }
}