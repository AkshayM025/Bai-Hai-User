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
import com.techno.baihai.adapter.AdapterUserFoundnTransaction;
import com.techno.baihai.api.APIClient;
import com.techno.baihai.api.APIInterface;
import com.techno.baihai.api.Constant;
import com.techno.baihai.databinding.FragmentPaymentUserProductHistoryBinding;
import com.techno.baihai.model.GetUserFoundnTransDataModel;
import com.techno.baihai.model.GetUserFoundnTransModel;
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


public class PaymentUserProductHistory extends Fragment {
    FragmentPaymentUserProductHistoryBinding binding;


    private Context mContext;
    ArrayList<GetUserFoundnTransDataModel> list = new ArrayList<>();

    private APIInterface apiInterface;
    private Utils utils;
    private Boolean isInternetPresent = false;
    private String uid;
    private String topicId;
    private AdapterUserFoundnTransaction madapter;
    private String uid1;


    public PaymentUserProductHistory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment_user_product_history,
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


        binding.rvProduct.setLayoutManager(new LinearLayoutManager(mContext));


        if (isInternetPresent) {
            //Animatoo.animateSwipeLeft(mContext);
            GetUserProductTransactionApi(binding.getRoot());

        } else {
            PrefManager prefManager = new PrefManager(mContext);
            PrefManager.showSettingsAlert(mContext);
            /*AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                    "You don't have internet connection.", false);*/
        }
        // SubCatApi();


        return binding.getRoot();
    }

    private void GetUserProductTransactionApi(final View v) {

        DataManager.getInstance().showProgressMessage(getActivity(), "Please wait...");

        Call<GetUserFoundnTransModel> call = apiInterface.get_foundation_user_donation(uid1);

        Log.e("get_baihai", "" + call.request().headers());

        call.enqueue(new Callback<GetUserFoundnTransModel>() {
            @Override
            public void onResponse(@NotNull Call<GetUserFoundnTransModel> call,
                                   @NotNull Response<GetUserFoundnTransModel> response) {
                // loading_spinner.setVisibility(View.GONE);

                try {
                    GetUserFoundnTransModel commentModel = response.body();

                    if (commentModel != null) {
                        if (commentModel.getStatus().equals("1")) {

                            binding.tvNotTranUserProId.setVisibility(View.GONE);
                            list = (ArrayList<GetUserFoundnTransDataModel>) commentModel.getResult();


                            madapter = new AdapterUserFoundnTransaction(mContext, list);
                            binding.rvProduct.removeAllViews();
                            binding.rvProduct.setAdapter(madapter);

                        } else {
                            binding.rvProduct.removeAllViews();
                            DataManager.getInstance().hideProgressMessage();
                            binding.swipeUserProductId.setRefreshing(false);
                            binding.tvNotTranUserProId.setVisibility(View.VISIBLE);
                            CustomSnakbar.showSnakabar(mContext, v, getResources().getString(R.string.transaction_nt_fnd));

                        }

                        DataManager.getInstance().hideProgressMessage();
                        binding.swipeUserProductId.setRefreshing(false);
                    } else {
                        DataManager.getInstance().hideProgressMessage();
                        binding.swipeUserProductId.setRefreshing(false);
                        binding.tvNotTranUserProId.setVisibility(View.VISIBLE);
                        CustomSnakbar.showSnakabar(mContext, v, getResources().getString(R.string.transaction_nt_fnd));

                    }

                } catch (Exception e) {
                    DataManager.getInstance().hideProgressMessage();
                    binding.swipeUserProductId.setRefreshing(false);
                    //Toast.makeText(mContext, "" + e, Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<GetUserFoundnTransModel> call, Throwable t) {
                DataManager.getInstance().hideProgressMessage();
                binding.swipeUserProductId.setRefreshing(false);
                //Toast.makeText(mContext, "" + call, Toast.LENGTH_SHORT).show();
                CustomSnakbar.showSnakabar(mContext, v, call.toString());

            }
        });

    }

}