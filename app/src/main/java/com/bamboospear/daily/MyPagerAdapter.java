package com.bamboospear.daily;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MyPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mData;

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);

        mData = new ArrayList<>();
        mData.add(new FirstFragment());
        mData.add(new SecondFragment());
        mData.add(new ThirdFragment());

    }

    @Override
    public Fragment getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position==0) {
            return "일기장";
        } else if (position==1) {
            return "뮤직플레이어";
        } else {
            return "주소록";
        }
    }
}