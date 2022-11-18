package com.techno.baihai.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.techno.baihai.api.Constant;
import com.techno.baihai.listner.FragmentListener;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;
import www.develpoeramit.mapicall.ApiCallBuilder;

public class DonationFragment extends Fragment implements View.OnClickListener {

    Context mContext;
    ImageView iv_back;
    CardView iv_card1, iv_card2, iv_card3;


    FragmentListener listener;

    public DonationFragment(FragmentListener listener) {

        this.listener = listener;
    }

    public DonationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();

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
                PrefManager.setBoolean(PrefManager.KEY_BaiHai_Status, false);
                startActivity(new Intent(getActivity(), StripePaymentActivity.class));

                break;


        }
    }

    public void onStart() {
        super.onStart();
        User user = PrefManager.getInstance(getActivity()).getUser();


        if (!user.getGuideGiveFree().equals("1")) {
            ShowIntro(getResources().getString(R.string.guide_donation_donate), getResources().getString(R.string.guide_donation_donate1), iv_card2, 1);

        }

    }

    private void ShowIntro(String title, String text, CardView viewId, final int type) {
        final int data= iv_card2.getTop();
        if (type == 1) {
            iv_card2.setTop(900);

             }
        new GuideView.Builder(mContext)
                .setTitle(title)
                .setContentText(text)
                .setGravity(Gravity.center)
                .setContentTextSize(12)//optional
                .setTitleTextSize(14)//optional
                .setDismissType(DismissType.anywhere) //optional - default dismissible by TargetView
                .setTargetView(viewId)
                .setGuideListener(new GuideListener() {
                    @Override
                    public void onDismiss(View view) {
                        if (type == 1) {
                            ShowIntro(getResources().getString(R.string.guide_donation_non_profit), getResources().getString(R.string.guide_donation_non_profit1), iv_card1, 6);
                        } else if (type == 6) {
                            ShowIntro(getResources().getString(R.string.guide_donation_non_profit2), getResources().getString(R.string.guide_donation_non_profit21), iv_card3, 5);
                        } else if (type == 5) {
                            setGuideDonation();
                            SharedPreferences.Editor sharedPreferencesEditor = mContext.getSharedPreferences("show_case_pref",
                                    Context.MODE_PRIVATE).edit();
                            sharedPreferencesEditor.putBoolean("showcase", false);
                        }
                    }
                })
                .build()
                .show();
    }

    private void setGuideDonation() {
        User user = PrefManager.getInstance(getActivity()).getUser();
        String id = null;
        if (user.getId() == "") {
            id = user.getId();
        } else {
            id = user.getId();
        }

        HashMap<String, String> parms1 = new HashMap<>();
        parms1.put("user_id", id);
        parms1.put("activity", "0");
        ApiCallBuilder.build(mContext)
                .setUrl(Constant.BASE_URL + Constant.GUIDE_GIVE_FREE)
                .setParam(parms1)
                .execute(new ApiCallBuilder.onResponse() {
                    public void Success(String response) {
                        try {
                            Log.e("selectedresponse=>", "-------->" + response);
                            JSONObject object = new JSONObject(response);
                            String message = object.getString("message");
                            User user2 = new User(
                                    user.getId(),
                                    user.getUsername(),
                                    user.getEmail(),
                                    user.getPassword(),
                                    user.getPhone(),
                                    user.getImage(),
                                    user.getLegalinfo(),
                                    user.getGuide(),
                                    user.getGuideFree(),
                                    "1",
                                    user.getSuscribe()
                            );

                            PrefManager.getInstance(getActivity()).userLogin(user2);


                        } catch (JSONException e) {


                            //Toast.makeText(mContext, "Error:" + e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }

                    public void Failed(String error) {
                    }
                });
    }

}
