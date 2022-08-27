package com.techno.baihai.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.techno.baihai.R;
import com.techno.baihai.api.Constant;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import www.develpoeramit.mapicall.ApiCallBuilder;

public class
DriverInfoActivity extends AppCompatActivity {

    private String Provider_driverId;
    private String Provider_requestId;
    private String Provider_firstname;
    private String pickup_status;
    private TextView current_driverNameId;
    private TextView current_driverId;
    private TextView status_id;
    private LinearLayout layout_providerCard;
    private TextView providerSatus_Id;
    private String driver_imgUrl;
    private String driver_number;
    private RoundedImageView provider_ImgId;
    private ImageView provider_callId;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_driver_info);


        mContext = DriverInfoActivity.this;


        Provider_driverId = getIntent().getStringExtra("driver_id");
        Provider_requestId = getIntent().getStringExtra("request_id");
        Provider_firstname = getIntent().getStringExtra("provider_firstname");
        pickup_status = getIntent().getStringExtra("pickup_status");
        driver_imgUrl = getIntent().getStringExtra("driver_imgUrl");
        driver_number = getIntent().getStringExtra("driver_number");


        // id's find.......
        current_driverNameId = findViewById(R.id.current_driverNameId);
        current_driverId = findViewById(R.id.current_driverId);
        status_id = findViewById(R.id.status_id);
        layout_providerCard = findViewById(R.id.layout_providerCard);
        providerSatus_Id = findViewById(R.id.providerSatus_Id);
        provider_ImgId = findViewById(R.id.provider_ImgId);
        provider_callId = findViewById(R.id.Provider_callId);

        PrefManager.save(this, "pickup_status", pickup_status);
        PrefManager.save(this, "driver_id", Provider_driverId);
        PrefManager.save(this, "request_id", Provider_requestId);
        PrefManager.save(this, "provider_firstname", Provider_firstname);
        PrefManager.save(this, "driver_imgUrl", driver_imgUrl);
        PrefManager.save(this, "driver_number", driver_number);


        //Toast.makeText(mContext, "status" + PrefManager.get(mContext, "driver_imgUrl"), Toast.LENGTH_SHORT).show();


        try {


            if (PrefManager.get(this, "pickup_status").equals("Accept") &&
                    PrefManager.get(this, "pickup_status") != null) {

                providerSatus_Id.setVisibility(View.GONE);
                layout_providerCard.setVisibility(View.VISIBLE);

                current_driverNameId.setText("Name: " + PrefManager.getString("provider_firstname"));
                current_driverId.setText("Order:545962417" + PrefManager.getString("driver_id"));
                status_id.setText(PrefManager.getString("pickup_status"));
                Glide.with(this).load(PrefManager.get(this, "driver_imgUrl")).
                        error(R.drawable.profile_img).into(provider_ImgId);

                provider_callId.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setLog("el usuario ya le llevan su pedido ");
                        String number = PrefManager.get(DriverInfoActivity.this, "driver_number");
                        Uri call = Uri.parse("tel:91" + number);
                        Intent surf = new Intent(Intent.ACTION_DIAL, call);
                        startActivity(surf);
                        Animatoo.animateZoom(mContext);

                    }
                });


            } else {
                providerSatus_Id.setVisibility(View.VISIBLE);
                layout_providerCard.setVisibility(View.GONE);


            }
        } catch (Exception e) {

        }


    }

    public void onBackFromdriverInfo(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        Animatoo.animateSlideLeft(this);
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