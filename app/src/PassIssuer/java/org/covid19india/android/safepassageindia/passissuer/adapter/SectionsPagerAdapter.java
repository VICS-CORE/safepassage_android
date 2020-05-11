package org.covid19india.android.safepassageindia.passissuer.adapter;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();

    public SectionsPagerAdapter(@NonNull FragmentManager fm, int behavior, Context context, List<Fragment> fragmentList, List<String> titleList) {
        super(fm, behavior);
        this.context = context;
        this.fragmentList = fragmentList;
        this.titleList = titleList;
    }

    public SectionsPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }
    //use BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    public void setFragments(List<Fragment> fragmentList, List<String> titleList) {
        this.titleList = titleList;
        this.fragmentList = fragmentList;
    }
}
