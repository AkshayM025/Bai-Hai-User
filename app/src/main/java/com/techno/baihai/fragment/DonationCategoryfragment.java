package com.techno.baihai.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.techno.baihai.R;
import com.techno.baihai.adapter.MyProductListAdapter;
import com.techno.baihai.api.Constant;
import com.techno.baihai.listner.FragmentListener;
import com.techno.baihai.model.CategoryList;
import com.techno.baihai.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import www.develpoeramit.mapicall.ApiCallBuilder;


public class DonationCategoryfragment extends Fragment {

    private static final String TAG = "DonationCatgoryActivity";
    private static final int AUTOCOMPLETE_REQUEST_CODE = 111;
    Context mContext;
    RecyclerView donation_recyclerView;
    RecyclerView.Adapter donation_mAdapter;
    double lat, lng;
    List<CategoryList> categoryLists;
    String uid;
    FragmentListener listener;
    private final Boolean isInternetPresent = false;

    public DonationCategoryfragment(FragmentListener listener) {
        this.listener = listener;
    }

    public DonationCategoryfragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        View view = inflater.inflate(R.layout.activity_donation_category, container, false);


        return view;

    }


    private void GetCategory() {


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        String langua = "EN";
        String lang = PrefManager.get(mContext, "lang");
        if (lang.equals("es") && lang != null) {
            langua = "ES";
        }

        HashMap<String, String> param = new HashMap<>();
        param.put("language", langua);
        ApiCallBuilder.build(getActivity())
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "get_category") //http://bai-hai.com/webservice/get_category
                .setParam(param)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        Log.e("Response=>", "" + response);
                        progressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.optString("status");
                            String message = object.optString("message");
                            if (status.equals("1")) {


                                JSONArray jArray = object.optJSONArray("result");
                                //Log.e(TAG, "result=>" + jArray);
                                if (jArray != null) {
                                    for (int i = 0; i < jArray.length(); i++) {


                                        JSONObject object1 = jArray.getJSONObject(i);


                                        // Log.e(TAG, "resulti=>" + i);
                                        String category_id = object1.getString("category_name");

                                        String category_name = object1.getString("category_name");
                                        String imageUrl = object1.getString("image");


                                        categoryLists.add(new CategoryList(category_id, category_name, imageUrl));


                                    }
                                }


                            } else {

                                Toast.makeText(mContext, "Status" + message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(mContext, "Exception" + e, Toast.LENGTH_SHORT).show();


                        }

                        donation_mAdapter = new MyProductListAdapter(getActivity(), categoryLists);

                        donation_recyclerView.setAdapter(donation_mAdapter);


                    }

                    @Override
                    public void Failed(String error) {

                        progressDialog.dismiss();
                        //CustomSnakbar.showDarkSnakabar(mContext, mview, "" + error);
                        //Toast.makeText(mContext, "Error" + error, Toast.LENGTH_SHORT).show();
                    }
                });


    }
}