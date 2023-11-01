package com.techno.baihai.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.techno.baihai.R;
import com.techno.baihai.listner.FragmentListener;


public class TabDonateFragment extends Fragment implements FragmentListener, View.OnClickListener {


    FragmentListener listener;

    View view;
    LinearLayout linear_tabs;
    TextView tab_txt1, tab_txt2;
    FrameLayout frameLayout1;
    ImageView iv_back;


    public TabDonateFragment(FragmentListener listener) {
        this.listener = listener;
    }

    public TabDonateFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        view = inflater.inflate(R.layout.fragment_tab_donate, container, false);


        iv_back = view.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.click(new HomeFragment(listener));
            }
        });

        frameLayout1 = view.findViewById(R.id.frameContainer1);

        loadFragment(new MyDonationFragment(this));


        tab_txt1 = view.findViewById(R.id.tab_txt1);
        tab_txt2 = view.findViewById(R.id.tab_txt2);

        // adding a listner
        tab_txt1.setOnClickListener(this);
        tab_txt2.setOnClickListener(this);


        return view;

    }

    private void loadFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction().replace(R.id.frameContainer1, fragment).commit();

    }


    @SuppressLint({"UseCompatLoadingForDrawables", "NonConstantResourceId"})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tab_txt1:

                tab_txt1.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_bg));
                tab_txt2.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_whitebg));
                tab_txt1.setTextColor(Color.parseColor("#FFFFFF"));
                tab_txt2.setTextColor(Color.parseColor("#000000"));

                loadFragment(new MyDonationFragment(this));


                break;


            case R.id.tab_txt2:

                tab_txt1.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_whitebg));
                tab_txt2.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_bg));
                tab_txt1.setTextColor(Color.parseColor("#000000"));
                tab_txt2.setTextColor(Color.parseColor("#FFFFFF"));

                loadFragment(new MoneyDoationFragment(this));


                break;

        }

    }

    @Override
    public void click(Fragment fragment) {

        loadFragment(fragment);


    }
}