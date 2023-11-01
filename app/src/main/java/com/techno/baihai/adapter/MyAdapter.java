package com.techno.baihai.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.techno.baihai.fragment.PaymentFoundationHistory;
import com.techno.baihai.fragment.PaymentUserProductHistory;


public class MyAdapter extends FragmentStatePagerAdapter {


    Context context;
    int totalTabs;

    public MyAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                PaymentUserProductHistory tab1 = new PaymentUserProductHistory();
                return tab1;
            case 1:
                PaymentFoundationHistory tab2 = new PaymentFoundationHistory();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}



