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

        orgId=getIntent().getStringExtra("orgId");
        orgName=getIntent().getStringExtra("orgName");
        seletedFoundation=findViewById(R.id.foundationSpinner);
        if (orgName!=null&&!orgName.equalsIgnoreCase("")){
        seletedFoundation.setText("Foundation: "+orgName);}
        else{
            Toast.makeText(mContext, "Foundation not found...!!", Toast.LENGTH_SHORT).show();
        }



        productId = findViewById(R.id.productId);
        // foundationsSpinner = (TextView) findViewById(R.id.foundationSpinner);
        addressId = findViewById(R.id.addressId);
        img_locationId = findViewById(R.id.img_locationId);

        SizeId = findViewById(R.id.SizeId);
        mobileId = findViewById(R.id.mobileId);
 /*

        foundationsSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              // TODO Auto-generated method stub

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(

                        WantToDonateFragment.this);

                LayoutInflater inflater = getLayoutInflater();

                // create view for add item in dialog

                View convertView = (View) inflater.inflate(R.layout.listview, null);

                // on dialog cancel button listner

                alertDialog.setNegativeButton("Cancel",

                        new DialogInterface.OnClickListener() {

                            @Override

                            public void onClick(DialogInterface dialog, int which) {

                                // TODO Auto-generated method stub

                            }

                        });

                // add custom view in dialog

                alertDialog.setView(convertView);

                lv = (ListView) convertView.findViewById(R.id.mylistview);
                GetFoundations();

                final AlertDialog alert = alertDialog.create();

                alert.setTitle(" Select Foundation"); // Title


                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override

                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                        // TODO Auto-generated method stub

                        Toast.makeText(WantToDonateFragment.this,

                                "You have selected -: " + foundations.get(position),

                                Toast.LENGTH_SHORT).show();
                        foundationsSpinner.setText(foundations.get(position));
                        String getFoundationId=String.valueOf(arg0.getSelectedItemId());
                        Toast.makeText(mContext, "Id=> "+getFoundationId, Toast.LENGTH_SHORT).show();





                        alert.cancel();

                    }

                });

                // show dialog

                alert.show();
            }
        });*/
        //Initializing Spinner
       // spinner1 = findViewById(R.id.spinner1);

        //Adding an Item Selected Listener to our Spinner
        //As we have implemented the class Spinner.OnItemSelectedListener to this class iteself we are passing this to setOnItemSelectedListener
      //  spinner1.setOnItemSelectedListener(this);

        img_locationId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            /*AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                    "You don't have internet connection.", false);*/
        }


     /*   try {


//        places=(AutoCompleteTextView)view.findViewById(R.id.places);
            adapter = new MyPlacesAdapter(this);

            addressId.setAdapter(adapter);
// text changed listener to get results precisely according to our search
            addressId.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (count > 0) {
//calling getfilter to filter the results
                        adapter.getFilter().filter(s);
//notify the adapters after results changed
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

// handling click of autotextcompleteview items

            addressId.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MyGooglePlaces googlePlaces = (MyGooglePlaces) parent.getItemAtPosition(position);
                    addressId.setText(googlePlaces.getAddress());
                    p_lat = String.valueOf(googlePlaces.getLatitude());
                    p_lng = String.valueOf(googlePlaces.getLongitude());
                }
            });
        }
        catch (Exception e){
            Toast.makeText(mContext, "Not Found....Try Again..!!"+e, Toast.LENGTH_SHORT).show();
        }*/

      /*  adapter2=new MyPlacesAdapter(getActivity());

        addressId.setAdapter(adapter2);
// text changed listener to get results precisely according to our search

        et_drop_point.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count>0) {
//calling getfilter to filter the results
                    adapter2.getFilter().filter(s);
//notify the adapters after results changed
                    adapter2.notifyDataSetChanged();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

// handling click of autotextcompleteview items
        et_drop_point.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyGooglePlaces googlePlaces=(MyGooglePlaces)parent.getItemAtPosition(position);
                et_drop_point.setText(googlePlaces.getName());
                d_lat = String.valueOf(googlePlaces.getLatitude());
                d_lng = String.valueOf(googlePlaces.getLongitude());
            }
        });
*/

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int poistion, long l) {


        //  Toast.makeText(mContext, "Select: "+adapterView.getItemIdAtPosition(poistion), Toast.LENGTH_SHORT).show();
        foundationId = String.valueOf(adapterView.getSelectedItemId());
        Log.i(TAG, "foundationId=>" + foundationId);
        Toast.makeText(mContext, "foundationId=>" + foundationId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        //textViewName.setText(getName(position));


    }


/*
    class MainListHolder {

        private TextView tvText;

    }


    private class ViewHolder {

        TextView tvSname;

    }

    class MyAdapter extends ArrayAdapter<String> {

        LayoutInflater inflater;

        Context myContext;

        List<String> newList;

        public MyAdapter(Context context, int resource, List<String> list) {

            super(context, resource, list);

            // TODO Auto-generated constructor stub

            myContext = context;

            newList = list;

            inflater = LayoutInflater.from(context);

        }

        @Override

        public View getView(final int position, View view, ViewGroup parent) {

            final ViewHolder holder;

            if (view == null) {

                holder = new ViewHolder();

                view = inflater.inflate(R.layout.listview_item, null);

                holder.tvSname = (TextView) view.findViewById(R.id.tvtext_item);

                view.setTag(holder);

            } else {

                holder = (ViewHolder) view.getTag();

            }

            holder.tvSname.setText(newList.get(position).toString());


            return view;

        }

    }*/


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
                                        String location = object1.getString("location");
                                        String description = object1.getString("description");


                                        foundations.add(object1.getString("org_name"));
                                        // foundations.add(object1.getString("org_name"));

                                        Log.e(TAG, "foundId=>" + org_name);


                                    }
                                }


                            } else {

                                progressDialog.dismiss();

                                Toast.makeText(mContext, "Data Not Found: " + message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();

                            e.printStackTrace();
                            //Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();


                        }
                       /* lv.setAdapter(new MyAdapter(WantToDonateFragment.this,
                                R.layout.listview_item, foundations));


*/

                        //spinner1.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_spinner_item,R.id.tvCustName, foundations));


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


            // http://bai-hai.com/webservice/donate_product?
            // user_id=1&
            // driver_id=1&
            // organization_id=1&
            // product=tets&
            // address=nknk&
            // lat=946.465&
            // lon=45425&
            // size=10*20*20&
            // mobile=4946


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

                                    PrefManager.setString(PrefManager.Key_FoundatonId,orgId);
                                    PrefManager.setString(PrefManager.Key_DonateProductId,request_id);
                                    PrefManager.setString(PrefManager.Key_AmountId,amount);



                                    Toast.makeText(mContext, "First You Make a Payment...!!", Toast.LENGTH_LONG).show();
                                   // startActivity(new Intent(DonateToFoundationFragment.this, ThankyouPointActivity.class));
                                    //startActivity(new Intent(DonateToFoundationFragment.this, DeliveryFragment.class));
                                   // Animatoo.animateInAndOut(mContext);
                                    //finish();
                                    DialogConfirmPayment(amount);


                                } else if (status.equals("0")) {

                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(mContext, "Failed, Please check your connnection !!", Toast.LENGTH_LONG).show();

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


    private void DialogConfirmPayment(String amount){
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
        paid_paymentsID.setText("$"+amount);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                PrefManager.setBoolean(PrefManager.KEY_BaiHai_Status,false);


            }
        });
        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                PrefManager.setBoolean(PrefManager.KEY_BaiHai_Status,true);
                finish();
                startActivity(new Intent(DonateToFoundationFragment.this, StripePaymentActivity.class));
                Animatoo.animateZoom(mContext);



            }
        });
        amount_cancelId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                PrefManager.setBoolean(PrefManager.KEY_BaiHai_Status,false);


            }
        });
        alertDialog.show();
    }



}