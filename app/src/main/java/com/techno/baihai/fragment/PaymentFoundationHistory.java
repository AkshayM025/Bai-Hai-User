package com.techno.baihai.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.techno.baihai.R;
import com.techno.baihai.adapter.AdapterBaiHaiTransHistory;
import com.techno.baihai.api.APIClient;
import com.techno.baihai.api.APIInterface;
import com.techno.baihai.api.Constant;
import com.techno.baihai.databinding.FragmentPaymentFoundationHistoryBinding;
import com.techno.baihai.model.BaiHaiTransactionDataModel;
import com.techno.baihai.model.GetBaiHaiTransactionModel;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.CustomSnakbar;
import com.techno.baihai.utils.DataManager;
import com.techno.baihai.utils.PrefManager;
import com.techno.baihai.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PaymentFoundationHistory extends Fragment {

    private Context mContext;
    ArrayList<BaiHaiTransactionDataModel> list = new ArrayList<>();

    private APIInterface apiInterface;
    private Utils utils;
    private Boolean isInternetPresent = false;
    private String uid;
    private String topicId;
    private AdapterBaiHaiTransHistory madapter;
    private String uid1;


    public PaymentFoundationHistory() {
        // Required empty public constructor
    }

    FragmentPaymentFoundationHistoryBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment_foundation_history,
                container, false);


        mContext = getActivity();

        utils = new Utils(mContext);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        PrefManager.isConnectingToInternet(mContext);
        isInternetPresent = PrefManager.isNetworkConnected(mContext);

        uid = PrefManager.get(mContext, Constant.USER_ID);
        User user = PrefManager.getInstance(getActivity()).getUser();
        uid1 = String.valueOf(user.getId());
        Log.e("", "user_id: " + uid);


        binding.recycleViewbaihaiId.setLayoutManager(new LinearLayoutManager(mContext));


        if (isInternetPresent) {
            //Animatoo.animateSwipeLeft(mContext);
            GetBaiHaiPaymentTransactionApi(binding.getRoot());

        } else {
            PrefManager prefManager = new PrefManager(mContext);
            PrefManager.showSettingsAlert(mContext);
        }
        // SubCatApi();


        return binding.getRoot();
    }

    private void GetBaiHaiPaymentTransactionApi(final View v) {

        DataManager.getInstance().showProgressMessage(getActivity(), "Please wait...");

        Call<GetBaiHaiTransactionModel> call = apiInterface.get_baihai_user_donation(uid1);

        Log.e("get_baihai", "" + call.request().headers());

        call.enqueue(new Callback<GetBaiHaiTransactionModel>() {
            @Override
            public void onResponse(@NotNull Call<GetBaiHaiTransactionModel> call,
                                   @NotNull Response<GetBaiHaiTransactionModel> response) {
                // loading_spinner.setVisibility(View.GONE);

                try {
                    GetBaiHaiTransactionModel commentModel = response.body();

                    if (commentModel != null) {
                        if (commentModel.getStatus().equals("1")) {
                            binding.tvNotTranfoundnId.setVisibility(View.GONE);


                            //  list.add(commentModel.getResult());
                            list = (ArrayList<BaiHaiTransactionDataModel>) commentModel.getResult();


                            // madapter = new DailyDoseAdapter(mContext,list,DailyDoseFragment.this);
                            // binding.recyclerDailyId.setAdapter(madapter);
                            madapter = new AdapterBaiHaiTransHistory(mContext, list);
                            binding.recycleViewbaihaiId.removeAllViews();
                            binding.recycleViewbaihaiId.setAdapter(madapter);

                        } else {
                            binding.recycleViewbaihaiId.removeAllViews();
                            DataManager.getInstance().hideProgressMessage();
                            binding.foundationSwipeId.setRefreshing(false);
                            binding.tvNotTranfoundnId.setVisibility(View.VISIBLE);
                            CustomSnakbar.showSnakabar(mContext, v, getResources().getString(R.string.transaction_nt_fnd));

                        }

                        DataManager.getInstance().hideProgressMessage();
                        binding.foundationSwipeId.setRefreshing(false);
                    } else {
                        DataManager.getInstance().hideProgressMessage();
                        binding.foundationSwipeId.setRefreshing(false);
                        binding.tvNotTranfoundnId.setVisibility(View.VISIBLE);
                        CustomSnakbar.showSnakabar(mContext, v, getResources().getString(R.string.transaction_nt_fnd));

                    }

                } catch (Exception e) {
                    DataManager.getInstance().hideProgressMessage();
                    binding.foundationSwipeId.setRefreshing(false);
                   // Toast.makeText(mContext, "" + e, Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<GetBaiHaiTransactionModel> call, Throwable t) {
                DataManager.getInstance().hideProgressMessage();
                binding.foundationSwipeId.setRefreshing(false);
                //Toast.makeText(mContext, "" + call, Toast.LENGTH_SHORT).show();
                CustomSnakbar.showSnakabar(mContext, v, call.toString());

            }
        });

    }

}