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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.develpoeramit.mapicall.ApiCallBuilder;

public class    AwardDialog extends Dialog {


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
            GetBaiHaiPaymentTransactionApi(uid);
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
                                JSONObject result2 = object.optJSONObject("result2");
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
                                setLog("se envio informacion de premios  a usuario");

                                }


                            } else {
                                progressDialog.dismiss();

                                //Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            Log.e("selectedresponse=>",""+e.getMessage());
                           // Toast.makeText(mContext, "" + e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void Failed(String error) {
                        progressDialog.dismiss();
                        Log.e("Failed=>",""+error);
                    //    Toast.makeText(mContext, "" + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void GetBaiHaiPaymentTransactionApi(String Uid) {


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        HashMap<String, String> parms = new HashMap<>();
        parms.put("user_id", Uid);


        ApiCallBuilder.build(mContext)
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "get_coin_history?")
                .setParam(parms)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {


                        Log.e("selectedresponse=>", "-------->" + response);


                        try {

                            JSONObject object = new JSONObject(response);
                            String status = object.optString("status");
                            JSONArray result = object.optJSONArray("result");
                            JSONArray result2 = object.optJSONArray("result2");
                            if (status.equals("1")) {
                                progressDialog.dismiss();
                                ArrayList<RewardsHistoryModel.Result> lista= new ArrayList<RewardsHistoryModel.Result>();
                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject object1 = result.getJSONObject(i);

                                    String id = object1.getString("id");
                                    String name = object1.getString("name");
                                    String mincoin = object1.getString("min_coin");
                                    String maxcoin = object1.getString("max_coin");
                                    String image = object1.getString("image");
                                    String message = object1.getString("message");

                                    RewardsHistoryModel.Result object3 = new RewardsHistoryModel.Result(id,name,mincoin,maxcoin,image,message);
                                    lista.add(object3);
                                }

                                madapter = new AdapterCoinModel(mContext, lista);

                                recycleViewbaihaiId.removeAllViews();
                                recycleViewbaihaiId.setAdapter(madapter);


                            } else {
                                progressDialog.dismiss();

                                //Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            Log.e("selectedresponse=>",""+e.getMessage());
                            // Toast.makeText(mContext, "" + e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void Failed(String error) {
                        progressDialog.dismiss();
                        Log.e("Failed=>",""+error);
                        //    Toast.makeText(mContext, "" + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void GetBaiHaiPaymentTransactionApi2() {

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
                    Log.e("ErrorApiTransaction=>",""+e);
               //     Toast.makeText(mContext, "" + e, Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<RewardsHistoryModel> call, Throwable t) {
                DataManager.getInstance().hideProgressMessage();

                Log.e("FailedApiTrasnsaction=>",""+call);
             //   Toast.makeText(mContext, "" + call, Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void setLog(String message) {
        User user = PrefManager.getInstance(mContext).getUser();
        String id = null;
        if (user.getId() == "") {
            id = "1";
        } else {
            id = user.getId();
        }

        HashMap<String, String> parms1 = new HashMap<>();
        parms1.put("user_id", id);
        parms1.put("activity", message);
        ApiCallBuilder.build(mContext)
                .setUrl(Constant.BASE_URL + Constant.LOG_APP)
                .setParam(parms1)
                .execute(new ApiCallBuilder.onResponse() {
                    public void Success(String response) {
                        try {
                            Log.e("selectedresponse=>", "-------->" + response);
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status");
                            if(status.equals("true")){
                                Log.e("selectedresponse=>", "-------->exitoso" );
                            }


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


