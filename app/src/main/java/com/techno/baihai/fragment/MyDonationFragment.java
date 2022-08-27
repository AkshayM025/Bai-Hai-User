package com.techno.baihai.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.techno.baihai.R;
import com.techno.baihai.adapter.TabDonationListAdapter;
import com.techno.baihai.api.Constant;
import com.techno.baihai.listner.FragmentListener;
import com.techno.baihai.model.MyProductModeListl;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import www.develpoeramit.mapicall.ApiCallBuilder;


public class MyDonationFragment extends Fragment {

    private static final String TAG = "MyProductListActivity";
    FragmentListener listener;
    private Boolean isInternetPresent = false;

    private Context mContext;
    private List<MyProductModeListl> myProductModeListls;
    private RecyclerView myDonation_recyclerview;
    private RecyclerView.Adapter tabDonationListmAdapter;
    private String uid, CatId;
    private TextView myDonation_NoDataList;


    public MyDonationFragment(FragmentListener listener) {
        this.listener = listener;
    }

    public MyDonationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        View view = inflater.inflate(R.layout.fragment_my_donation, container, false);

        mContext = getActivity();
        isInternetPresent = PrefManager.isNetworkConnected(mContext);


        User user = PrefManager.getInstance(getActivity()).getUser();
        uid = String.valueOf(user.getId());
        Log.i(TAG, "user_id: " + uid);


        myDonation_recyclerview = view.findViewById(R.id.myDonation_recyclerview);
        myDonation_recyclerview.setHasFixedSize(true);
        myProductModeListls = new ArrayList<>();


        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


        myDonation_recyclerview.setLayoutManager(layoutManager); // set LayoutManager to RecyclerView


        if (isInternetPresent) {
            GetProductIntrustCountApi();
        } else {
            PrefManager prefManager = new PrefManager(mContext);
            PrefManager.showSettingsAlert(mContext);
        }


        myDonation_NoDataList = view.findViewById(R.id.myDonation_NoDataList);


        return view;
    }


    private void GetProductIntrustCountApi() {
        //http://bai-hai.com/webservice/get_user_product_list?
        // user_id=71;
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", uid);
        ApiCallBuilder.build(getActivity())
                .isShowProgressBar(false) //http://bai-hai.com/webservice/get_user_product_list?user_id=71
                .setUrl(Constant.BASE_URL + "get_user_product_list?")
                .setParam(param)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        Log.e("Response==>", "" + response);
                        progressDialog.dismiss();
                        myDonation_NoDataList.setVisibility(View.GONE);
                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.optString("status");
                            String message = object.optString("message");
                            if (status.equals("1")) {


                                JSONArray result = object.optJSONArray("result");
                                // Log.e(TAG, "result=>" + result);


                                if (result != null) {
                                    for (int i = 0; i < result.length(); i++) {

                                        JSONObject object1 = result.getJSONObject(i);


                                        String product_id = object1.optString("id");


                                        String seller_id = object1.optString("user_id");


                                        String category_id = object1.optString("category_id");
                                        String category_name = object1.optString("category_name");
                                        String product_status = object1.optString("status");


                                        String product_name = object1.optString("name");
                                        String product_imageUrl = object1.optString("image1");
                                        String product_description = object1.optString("description");

                                        String product_address = object1.optString("address");

                                        String product_used = object1.optString("used");
                                        String product_total_chat = object1.optString("totalchatrequest");
                                        String product_lat = object1.optString("lat");
                                        String product_lon = object1.optString("lon");
                                        Log.e(TAG, "productStatus=>" + product_status);


                                        myProductModeListls.add(new MyProductModeListl(product_id, seller_id, "", category_id,
                                                product_name, product_description, product_status,
                                                product_address, product_used, product_imageUrl, product_total_chat,
                                                "category_image", category_name, product_lat, product_lon, product_status, "product_dateTime"));


                                    }
                                }


                            } else {

                                //myDonation_NoDataList.setText("Check Your connection");
                                myDonation_NoDataList.setVisibility(View.VISIBLE);
                                Toast.makeText(mContext, "Data Not Found ", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(mContext, "Check Your Network: " , Toast.LENGTH_LONG).show();


                        }
                        tabDonationListmAdapter = new TabDonationListAdapter(getActivity(), myProductModeListls);
                        myDonation_recyclerview.removeAllViews();

                        myDonation_recyclerview.setAdapter(tabDonationListmAdapter);


                    }

                    @Override
                    public void Failed(String error) {

                        progressDialog.dismiss();
                        myDonation_NoDataList.setText("check your connection");
                        myDonation_NoDataList.setVisibility(View.VISIBLE);
                       // Toast.makeText(mContext, "Check Your Network: " + error, Toast.LENGTH_LONG).show();
                    }
                });


    }

}