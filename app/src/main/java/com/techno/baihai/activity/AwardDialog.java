package com.techno.baihai.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.marozzi.roundbutton.RoundButton;
import com.techno.baihai.R;
import com.techno.baihai.adapter.AdapterCoinModel;
import com.techno.baihai.api.APIClient;
import com.techno.baihai.api.APIInterface;
import com.techno.baihai.api.Constant;
import com.techno.baihai.model.RewardsHistoryModel;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.DataManager;
import com.techno.baihai.utils.PrefManager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.develpoeramit.mapicall.ApiCallBuilder;

public class AwardDialog extends Dialog {


    public Activity mContext;
    public Dialog d;
    public Button yes, no;
    private RoundButton bt;
    private ImageView award_cancelId;
    private Boolean isInternetPresent = false;
    private String uid;
    private APIInterface apiInterface;

    ArrayList<RewardsHistoryModel.Result> list = new ArrayList<>();
    private AdapterCoinModel madapter;
    private RecyclerView recycleViewbaihaiId;
    private SwipeRefreshLayout swipLayoutFoundation;


    public AwardDialog(@NonNull Activity activity) {
        super(activity);
        // TODO Auto-generated constructor stub
        this.mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_reward);

        PrefManager.isConnectingToInternet(mContext);
        isInternetPresent = PrefManager.isNetworkConnected(mContext);
        apiInterface = APIClient.getClient().create(APIInterface.class);


        User user = PrefManager.getInstance(mContext).getUser();
        uid = String.valueOf(user.getId());
        Log.e("user_id: ", uid);

        recycleViewbaihaiId = findViewById(R.id.recycleViewbaihaiId);
        swipLayoutFoundation = findViewById(R.id.swipLayoutFoundation);
        recycleViewbaihaiId.setLayoutManager(new LinearLayoutManager(mContext));


        award_cancelId = (ImageView) findViewById(R.id.award_cancelId);
        award_cancelId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if (isInternetPresent) {
            GetLevelsApi(uid);
        } else {
            PrefManager.showSettingsAlert(mContext);

        }

        if (isInternetPresent) {
            GetBaiHaiPaymentTransactionApi();
        } else {
            PrefManager.showSettingsAlert(mContext);

        }


    }

    private void GetLevelsApi(String Uid) {


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        HashMap<String, String> parms = new HashMap<>();
        parms.put("user_id", Uid);


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

                                //   https://www.shipit.ng/BaiHai/webservice/get_profile?user_id=1

                                JSONObject result = object.optJSONObject("result");
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

    private void GetBaiHaiPaymentTransactionApi() {

        DataManager.getInstance().showProgressMessage(mContext, "Please wait...");

        Call<RewardsHistoryModel> call = apiInterface.get_coin_history(uid);


        call.enqueue(new Callback<RewardsHistoryModel>() {
            @Override
            public void onResponse(@NotNull Call<RewardsHistoryModel> call, @NotNull Response<RewardsHistoryModel> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    RewardsHistoryModel commentModel = response.body();

                    if (commentModel != null) {
                        if (commentModel.getStatus().equals("1")) {

                            list = (ArrayList<RewardsHistoryModel.Result>) commentModel.getResult();

                            madapter = new AdapterCoinModel(mContext, list);

                            recycleViewbaihaiId.removeAllViews();
                            recycleViewbaihaiId.setAdapter(madapter);

                        } else {
                            recycleViewbaihaiId.removeAllViews();

                        }

                    } else {
                        DataManager.getInstance().hideProgressMessage();

                    }

                } catch (Exception e) {
                    DataManager.getInstance().hideProgressMessage();
                    Toast.makeText(mContext, "" + e, Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<RewardsHistoryModel> call, Throwable t) {
                DataManager.getInstance().hideProgressMessage();
                Toast.makeText(mContext, "" + call, Toast.LENGTH_SHORT).show();

            }
        });

    }


}


