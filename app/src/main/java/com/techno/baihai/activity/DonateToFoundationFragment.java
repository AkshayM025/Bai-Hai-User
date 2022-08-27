package com.techno.baihai.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.techno.baihai.R;
import com.techno.baihai.api.Constant;
import com.techno.baihai.model.User;
import com.techno.baihai.service.MyPlacesAdapter;
import com.techno.baihai.utils.GPSTracker;
import com.techno.baihai.utils.PrefManager;
import com.techno.baihai.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import www.develpoeramit.mapicall.ApiCallBuilder;


public class DonateToFoundationFragment extends AppCompatActivity implements Spinner.OnItemSelectedListener {

    private static final String TAG = SubscribeFoundationActivity.class.getSimpleName();
    private static final int AUTOCOMPLETE_REQUEST_CODE = 111;
    Context mContext = this;
    ImageView iv_back;
    String foundationId;
    Spinner spinner1;
    double lat, lng;
    String latitude, longitude;
    ArrayList<String> foundations;
    AlertDialog.Builder alertDialog;
    //   MyAdapter myadapter;
    ListView lv;
    private EditText productId, SizeId, mobileId;
    TextView addressId;
    private ProgressBar progressBar;
    private Boolean isInternetPresent = false;
    private String uid;
    String p_lat = "", p_lng = "", d_lat = "", d_lng = "";
    MyPlacesAdapter adapter;
    private ImageView img_locationId;
    private String orgName;
    private String orgId;
    private TextView seletedFoundation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.fragment_foundation_to_donate);

        iv_back = findViewById(R.id.iv_backFromWantDonate);
        foundations = new ArrayList<String>();


        PrefManager.isConnectingToInternet(this);
        isInternetPresent = PrefManager.isNetworkConnected(this);

        User user = PrefManager.getInstance(this).getUser();
        uid = String.valueOf(user.getId());

        alertDialog = new AlertDialog.Builder(DonateToFoundationFragment.this);

        orgId = getIntent().getStringExtra("orgId");
        orgName = getIntent().getStringExtra("orgName");
        seletedFoundation = findViewById(R.id.foundationSpinner);
        if (orgName != null && !orgName.equalsIgnoreCase("")) {
            seletedFoundation.setText("Foundation: " + orgName);
        } else {
            Toast.makeText(mContext, "Foundation not found...!!", Toast.LENGTH_SHORT).show();
        }


        productId = findViewById(R.id.productId);
        // foundationsSpinner = (TextView) findViewById(R.id.foundationSpinner);
        addressId = findViewById(R.id.addressId);
        img_locationId = findViewById(R.id.img_locationId);

        SizeId = findViewById(R.id.SizeId);
        mobileId = findViewById(R.id.mobileId);


        img_locationId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLog("Busqueda  de ubicacion  de fundacion en el mapa");
                startActivityForResult(new Intent(DonateToFoundationFragment.this,
                        PinLocationActivity.class), AUTOCOMPLETE_REQUEST_CODE);

            }
        });
        progressBar = findViewById(R.id.progressBarId);


        getCurrentLocation();

        if (isInternetPresent) {
            GetFoundations();
        } else {
            PrefManager prefManager = new PrefManager(mContext);
            PrefManager.showSettingsAlert(mContext);
        }


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int poistion, long l) {


        foundationId = String.valueOf(adapterView.getSelectedItemId());
        Log.i(TAG, "foundationId=>" + foundationId);
       // Toast.makeText(mContext, "foundationId=>" + foundationId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }


    private void GetFoundations() {


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        HashMap<String, String> param = new HashMap<>();
        param.put("lat", latitude);
        param.put("lon", longitude);

        ApiCallBuilder.build(this)
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "get_organization")
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


                                JSONArray result = object.optJSONArray("result");
                                //Log.e(TAG, "result=>" + result);


                                foundations.add("Select Foundations");

                                if (result != null) {
                                    for (int i = 0; i < result.length(); i++) {

                                        JSONObject object1 = result.getJSONObject(i);


                                        // Log.e(TAG, "resulti=>" + i);
                                        String org_name = object1.getString("org_name");
                                        String org_id = object1.getString("id");

                                        String contact_name = object1.getString("contact_name");
                                        String email = object1.getString("email");
                                        String mobile = object1.getString("phone_no");
                                        String webpage = object1.getString("webpage");
                                        String location = object1.getString("location");
                                        String description = object1.getString("description");


                                        foundations.add(object1.getString("org_name"));

                                        Log.e(TAG, "foundId=>" + org_name);


                                    }
                                }


                            } else {

                                progressDialog.dismiss();

                                //Toast.makeText(mContext, "Data Not Found: " + message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();

                            e.printStackTrace();
                            //Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();


                        }


                    }

                    @Override
                    public void Failed(String error) {

                        progressDialog.dismiss();
                        Toast.makeText(mContext, "check your network: " + error, Toast.LENGTH_SHORT).show();
                    }
                });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            lat = data.getExtras().getDouble("lat");
            lng = data.getExtras().getDouble("lng");
            addressId.setText(Tools.getCompleteAddressString(this, lat, lng));
        }

    }


    private void getCurrentLocation() {

        GPSTracker track = new GPSTracker(DonateToFoundationFragment.this);
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


    private void donateToFoundation(View view) {

        final String product = productId.getText().toString().trim();
        //final String foundationList = fo

        final String location = addressId.getText().toString().trim();
        final String size = SizeId.getText().toString().trim();
        final String mobile = mobileId.getText().toString().trim();
        setLog("Guardo la fundacion correspondiente");

        //first we will do the validations
        int length = 10;
        if (product.equalsIgnoreCase("")) {

            Toast.makeText(mContext, "Please enter product name", Toast.LENGTH_SHORT).show();
            productId.requestFocus();

/*        } else if (spinner1.getSelectedItem().toString().trim().equals("Select Foundations")) {
            Toast.makeText(mContext, "Please select a foundation", Toast.LENGTH_SHORT).show();*/
        } else if (location.equalsIgnoreCase("")) {
            Toast.makeText(mContext, "Please select/enter a location", Toast.LENGTH_SHORT).show();
            addressId.requestFocus();

        } else if (size.equalsIgnoreCase("")) {
            Toast.makeText(mContext, "Please enter a size", Toast.LENGTH_SHORT).show();
            SizeId.requestFocus();
        } else if (mobile.equalsIgnoreCase("")) {
            Toast.makeText(mContext, "Please enter the number", Toast.LENGTH_SHORT).show();
            mobileId.requestFocus();
        } else {

            progressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> parms = new HashMap<>();
            parms.put("user_id", uid);
            parms.put("driver_id", "");
            parms.put("organization_id", orgId);
            parms.put("product", product);
            parms.put("address", location);
            parms.put("size", size);
            parms.put("mobile", mobile);
            parms.put("lat", String.valueOf(lat));
            parms.put("lon", String.valueOf(lng));

            ApiCallBuilder.build(this)
                    .isShowProgressBar(false)
                    .setUrl(Constant.BASE_URL + "donate_product?")
                    .setParam(parms)
                    .execute(new ApiCallBuilder.onResponse() {
                        @Override
                        public void Success(String response) {
                            progressBar.setVisibility(View.GONE);

                            Log.e("donate_product", String.valueOf(response));
                            // do anything with response
                            try {
                                JSONObject object = new JSONObject(response);
                                String status = object.optString("status");
                                String message = object.optString("message");
                                Log.e("status", status);
                                Log.d(TAG, "STATUS_:" + status);

                                if (status.equals("1")) {
                                    progressBar.setVisibility(View.GONE);
                                    String request_id = object.optString("request_id");
                                    String amount = object.optString("amount");


                                    productId.setText("");
                                    addressId.setText("");
                                    mobileId.setText("");
                                    SizeId.setText("");

                                    PrefManager.setString(PrefManager.Key_FoundatonId, orgId);
                                    PrefManager.setString(PrefManager.Key_DonateProductId, request_id);
                                    PrefManager.setString(PrefManager.Key_AmountId, amount);


                                    Toast.makeText(mContext, "First You Make a Payment...!!", Toast.LENGTH_LONG).show();

                                    DialogConfirmPayment(amount);


                                } else if (status.equals("0")) {

                                    progressBar.setVisibility(View.GONE);
                                   // Toast.makeText(mContext, "Failed, Please check your connnection !!", Toast.LENGTH_LONG).show();

                                }


                            } catch (JSONException e) {

                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(mContext, "Please check your Network !!" + e, Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void Failed(String error) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(mContext, "Check Your Network", Toast.LENGTH_LONG).show();

                        }
                    });


            // startActivity(new Intent(this, DeliveryFragment.class));


        }


    }


    public void SubmitProductInit(View view) {
        donateToFoundation(view);

    }


    public void iv_backFromWantDonate(View view) {
        onBackPressed();
        finish();
    }


    private void DialogConfirmPayment(String amount) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(DonateToFoundationFragment.this);

        View mView = getLayoutInflater().inflate(R.layout.dialog_payment_confirmation, null);

        final TextView paid_paymentsID = (TextView) mView.findViewById(R.id.paid_paymentsID);
        ImageView btn_cancel = (ImageView) mView.findViewById(R.id.uncheck);
        ImageView amount_cancelId = (ImageView) mView.findViewById(R.id.amount_cancelId);

        ImageView btn_okay = (ImageView) mView.findViewById(R.id.check);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.setCanceledOnTouchOutside(false);
        paid_paymentsID.setText("$" + amount);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                setLog("Cancela el pago a la  fundacion");
                PrefManager.setBoolean(PrefManager.KEY_BaiHai_Status, false);


            }
        });
        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLog("Confirma el pago a la  fundacion");
                alertDialog.dismiss();
                PrefManager.setBoolean(PrefManager.KEY_BaiHai_Status, true);
                finish();
                startActivity(new Intent(DonateToFoundationFragment.this, StripePaymentActivity.class));
                Animatoo.animateZoom(mContext);


            }
        });
        amount_cancelId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                PrefManager.setBoolean(PrefManager.KEY_BaiHai_Status, false);


            }
        });
        alertDialog.show();
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