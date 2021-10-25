package com.techno.baihai.fragment;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.techno.baihai.R;
import com.techno.baihai.activity.AccountActivity;
import com.techno.baihai.activity.AwardDialog;
import com.techno.baihai.activity.RewardPointsActivity;
import com.techno.baihai.activity.SubscribeFoundationActivity;
import com.techno.baihai.api.Constant;
import com.techno.baihai.listner.FragmentListener;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.GPSTracker;
import com.techno.baihai.utils.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import www.develpoeramit.mapicall.ApiCallBuilder;

public class HomeFragment extends Fragment {

    Context mContext;
    FragmentListener listener;
    LinearLayout donation, point, id_prize;
    CardView card, card2, card3, card4;
    CircleImageView home_profileId;
    TextView home_usernameId;
    String latitude, longitude;
    private boolean checkReward=true;
    private Boolean isInternetPresent = false;
    private String uid;


    public HomeFragment(FragmentListener listener) {
        // Required empty public constructor
        this.listener = listener;

    }

    public HomeFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContext = getActivity();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        home_usernameId = view.findViewById(R.id.home_usernameId);

        home_profileId = view.findViewById(R.id.home_profileId);
        home_profileId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  listener.click(new DonateFragment(listener));
                startActivity(new Intent(mContext, AccountActivity.class));

            }
        });

        PrefManager.isConnectingToInternet(mContext);
        isInternetPresent = PrefManager.isNetworkConnected(mContext);



        User user = PrefManager.getInstance(mContext).getUser();
        uid = String.valueOf(user.getId());
        Log.e("user_id: ", uid);




        donation = view.findViewById(R.id.donation);
        donation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //listener.click(new FoundationFragment(listener));
                listener.click(new TabDonateFragment(listener));

            }
        });

        id_prize = view.findViewById(R.id.id_prize);
        id_prize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AwardDialog cdd=new AwardDialog(getActivity());
                cdd.setCancelable(true);
                cdd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                cdd.show();
                //startActivity(new Intent(mContext, AwardDialog.class));
            }
        });
        point = view.findViewById(R.id.point);
        point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, RewardPointsActivity.class));
            }
        });
        card = view.findViewById(R.id.card);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  listener.click(new DonateFragment(listener));
                //startActivity(new Intent(getActivity(), DonationFragment.class));
                listener.click(new DonationFragment(listener));


            }
        });

        card2 = view.findViewById(R.id.card2);
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.click(new CategoryFragment(listener));
            }
        });
        card3 = view.findViewById(R.id.card3);
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getActivity().getPackageManager()
                        .getLaunchIntentForPackage("com.tech.baihaiprovider");
                if (launchIntent != null) {
                    startActivity(launchIntent);
                } else {

                    Toast.makeText(getActivity(), "Provider App Not Installed...!!", Toast.LENGTH_SHORT).show();
                    launchMarket();
                }
            }
        });
        card4 = view.findViewById(R.id.card4);
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, SubscribeFoundationActivity.class));
            }
        });

        getUserUpdate();
        if (isInternetPresent) {
          //  GetLevelsApi(uid);
        } else {
            PrefManager prefManager = new PrefManager(mContext);
            PrefManager.showSettingsAlert(mContext);
        }

getCurrentLocation();
        return view;
    }

    private void GetLevelsApi(String Uid) {


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        HashMap<String, String> parms = new HashMap<>();
        parms.put("user_id", uid);


        ApiCallBuilder.build(mContext)
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "get_user_level?")
                .setParam(parms)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {


                        Log.e("selectedresponse=>", "-------->" + response);


                        try {

                            JSONObject object = new JSONObject(response);
                            String status = object.optString("status");
                            String message = object.optString("message");
                            if (status.equals("1")) {
                                progressDialog.dismiss();


                                //Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();

                                //   https://www.shipit.ng/BaiHai/webservice/get_profile?user_id=1

                                JSONObject result = object.optJSONObject("result");
                                //String id = result.optString("id");
                                //Log.e("resultt=>", "" + response);
                                String awardId = "null";
                                String awardName = "null";
                                String awardMinCoin = "null";
                                String awardMaxCoin = "null";
                                String awardImage = "null";
                                String awardMessage = "null";

                                if (result != null) {
                                    awardId = result.optString("id");
                                    awardName = result.optString("name");
                                    awardMinCoin = result.optString("min_coin");
                                    awardMaxCoin = result.optString("max_coin");
                                    awardImage = result.optString("image");
                                    awardMessage = result.optString("message");

                                    checkReward= PrefManager.getBool(mContext,"showDailog");

                                    if (checkReward==true&&awardMinCoin.equals(50)){

                                        PrefManager.setBoolean("showDailog",false);


                                        AwardDialog cdd=new AwardDialog(getActivity());
                                        cdd.setCancelable(false);
                                        cdd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                        cdd.show();
                                    }else {
                                        Toast.makeText(mContext, "Share a App & get a new rewards..!!", Toast.LENGTH_SHORT).show();
                                    }


                                }




                            } else {
                                progressDialog.dismiss();

                                Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();

                            Toast.makeText(mContext, "" + e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void Failed(String error) {
                        progressDialog.dismiss();
                        Toast.makeText(mContext, "" + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void launchMarket() {
        Uri uri = Uri.parse("http://play.google.com/store/apps/details?id=" +"com.tech.mrnle");
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), " unable to find market app", Toast.LENGTH_LONG).show();
        }

//        String imageToShare = "http://s1.dmcdn.net/hxdt6/x720-qef.jpg"; //Image You wants to share
//
//
//        String title = "Title to share"; //Title you wants to share
//
//        Intent shareIntent = new Intent();
//        shareIntent.setAction(Intent.ACTION_SEND);
//        shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
//        shareIntent.setType("*/*");
//        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        shareIntent.putExtra(Intent.EXTRA_TEXT, imageToShare);
//        startActivity(Intent.createChooser(shareIntent, "Select App to Share Text and Image"));




    }






    @Override
    public void onResume() {


        getUserUpdate();
        getCurrentLocation();
        super.onResume();

    }


    public void getUserUpdate() {

        User user = PrefManager.getInstance(getActivity()).getUser();

        home_usernameId.setText(String.valueOf(user.getUsername()));


        Picasso.get().load(user.getImage()).placeholder(R.drawable.profile_img).into(home_profileId);
        // Log.i(TAG,"image=>"+imagePath);


    }
    private void getCurrentLocation() {

        GPSTracker track = new GPSTracker(getActivity());
        if (track.canGetLocation()) {
            latitude = String.valueOf(track.getLatitude());
            Log.e("lat=>", "-------->" + latitude);

            longitude = String.valueOf(track.getLongitude());
            Log.e("lon=>", "-------->" + longitude);

            //latLng = new LatLng(latitude, longitude);

        } else {
            track.showSettingsAlert();
        }
    }



}
