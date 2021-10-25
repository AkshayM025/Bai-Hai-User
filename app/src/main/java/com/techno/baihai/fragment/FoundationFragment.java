package com.techno.baihai.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.techno.baihai.R;
import com.techno.baihai.adapter.CustomRecyclerAdapter;
import com.techno.baihai.api.Constant;
import com.techno.baihai.listner.FragmentListener;
import com.techno.baihai.model.FoundationsList;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.GPSTracker;
import com.techno.baihai.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import www.develpoeramit.mapicall.ApiCallBuilder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class FoundationFragment extends Fragment {

    private static final String TAG = "FoundationFragment";

    Context mContext;
    FragmentListener listener;
    ImageView iv_back;
    CardView iv_card1, iv_card3;
    String uid;
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<FoundationsList> foundationsLists;
    private ProgressBar progressBar1;
    private Boolean isInternetPresent = false;
    private String latitude="";
    private String longitude="";
    private TextView foundation_notFound;
    private SwipeRefreshLayout swipLayout;


    public FoundationFragment(FragmentListener listener) {
        // Required empty public constructor
        this.listener = listener;
    }
    public FoundationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContext = getActivity();
        isInternetPresent = PrefManager.isNetworkConnected(mContext);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_foundation, container, false);

        iv_back = view.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.click(new DonationFragment(listener));
            }
        });

        recyclerView = view.findViewById(R.id.recycleViewfoundations);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

        foundationsLists = new ArrayList<>();
        User user = PrefManager.getInstance(getActivity()).getUser();
        uid = String.valueOf(user.getId());
        Log.i(TAG, "user_id: " + uid);
        getCurrentLocation();
        swipLayout= view.findViewById(R.id.swipLayoutFoundation);

        swipLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (isInternetPresent) {
                    GetFoundations();
                } else {
                    //PrefManager prefManager = new PrefManager(mContext);
                    PrefManager.showSettingsAlert(mContext);
                }
            }
        });




        if (isInternetPresent) {
            GetFoundations();
        } else {
           // PrefManager prefManager = new PrefManager(mContext);
            PrefManager.showSettingsAlert(mContext);
            /*AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                    "You don't have internet connection.", false);*/
        }

        foundation_notFound=view.findViewById(R.id.foundation_notFound);
        return view;
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


    private void GetFoundations() {

/*
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();*/
        swipLayout.setRefreshing(true);


        HashMap<String, String> param = new HashMap<>();
        param.put("lat",latitude);
        param.put("lon",longitude);

        ApiCallBuilder.build(getActivity())
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "get_organization")
                .setParam(param)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        Log.e("Response=>", "" + response);
                        //progressDialog.dismiss();
                        foundation_notFound.setVisibility(GONE);
                        swipLayout.setRefreshing(false);

                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.optString("status");
                            String message = object.optString("message");
                            if (status.equals("1")) {



                                JSONArray result = object.optJSONArray("result");
                               // Log.e(TAG, "result=>" + result);
                                foundationsLists.clear();

                                if (result != null) {
                                    for (int i = 0; i < result.length(); i++) {

                                        JSONObject object1 = result.getJSONObject(i);


                                       // Log.e(TAG, "resulti=>" + i);
                                        String org_id = object1.getString("id");
                                        String org_name = object1.getString("org_name");
                                        String contact_name = object1.getString("contact_name");
                                        String email = object1.getString("email");
                                        String mobile = object1.getString("phone_no");
                                        String location = object1.getString("location");
                                        String description = object1.getString("description");


                                        foundationsLists.add(new FoundationsList(org_id,org_name, contact_name, email, mobile, location, description));


                                    }
                                }


                            }else{
                                swipLayout.setRefreshing(false);

                                foundation_notFound.setVisibility(VISIBLE);
                                Toast.makeText(mContext, "Data Not Found"+message, Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            swipLayout.setRefreshing(false);

                            e.printStackTrace();
                            Toast.makeText(mContext, "ee"+e, Toast.LENGTH_SHORT).show();


                        }

                        mAdapter = new CustomRecyclerAdapter(getActivity(), foundationsLists);

                        recyclerView.setAdapter(mAdapter);


                    }

                    @Override
                    public void Failed(String error) {
                        swipLayout.setRefreshing(false);

                        //progressDialog.dismiss();
                        Toast.makeText(mContext, "e"+error, Toast.LENGTH_SHORT).show();

                        //CustomSnakbar.showDarkSnakabar(mContext, mview, "" + error);
                    }
                });


    }


}