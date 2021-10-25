package com.techno.baihai.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.techno.baihai.R;
import com.techno.baihai.activity.ProductDonateActivity;
import com.techno.baihai.activity.StripePaymentActivity;
import com.techno.baihai.listner.FragmentListener;
import com.techno.baihai.utils.PrefManager;

public class DonationFragment extends Fragment implements View.OnClickListener {

    Context mContext;
    ImageView iv_back;
    CardView iv_card1, iv_card2, iv_card3;


    FragmentListener listener;

    public DonationFragment (FragmentListener listener) {

        this.listener = listener;
    }

    public DonationFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        View view = inflater.inflate(R.layout.fragment_donation, container, false);

        iv_back = view.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.click(new HomeFragment(listener));
            }
        });

        iv_card1 = view.findViewById(R.id.donatio_card1);
        iv_card1.setOnClickListener(this);


        iv_card2 = view.findViewById(R.id.donatio_card2);
        iv_card2.setOnClickListener(this);


        iv_card3 = view.findViewById(R.id.donatio_card3);
        iv_card3.setOnClickListener(this);

        return view;


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.donatio_card1:
                listener.click(new FoundationFragment(listener));

                break;

            case R.id.donatio_card2:
               // listener.click(new ProductDonateFragment(listener));
                startActivity(new Intent(getActivity(), ProductDonateActivity.class));


                break;

            case R.id.donatio_card3:
                //listener.click(new PaymentFragment(listener));
                PrefManager.setBoolean(PrefManager.KEY_BaiHai_Status,false);
                startActivity(new Intent(getActivity(), StripePaymentActivity.class));

                break;


        }
    }
}
