package com.techno.baihai.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.techno.baihai.adapter.MyAdapter;
import com.techno.baihai.api.APIClient;
import com.techno.baihai.api.APIInterface;
import com.techno.baihai.databinding.FragmentMoneyDoationBinding;
import com.techno.baihai.listner.FragmentListener;
import com.techno.baihai.utils.PrefManager;
import com.techno.baihai.utils.Utils;


public class MoneyDoationFragment extends Fragment {



    //FragmentBookingBinding binding;
    private Context mContext;
    private APIInterface apiInterface;
    private Utils utils;
    private Boolean isInternetPresent = false;
    FragmentListener listener;

    //This is our viewPager


    public MoneyDoationFragment(FragmentListener listener) {
        this.listener = listener;
    }

    FragmentMoneyDoationBinding binding;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding= FragmentMoneyDoationBinding.inflate(LayoutInflater.from(getContext()));
       // return inflater.inflate(R.layout.fragment_money_doation, container, false);


        mContext = getActivity();

        utils = new Utils(mContext);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        PrefManager.isConnectingToInternet(mContext);
        isInternetPresent = PrefManager.isNetworkConnected(mContext);




        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Foundation History"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Bai-Hai History"));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.viewPager.setAdapter(new MyAdapter(getActivity(), getChildFragmentManager(), binding.tabLayout.getTabCount()));
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        return binding.getRoot();
    }
}