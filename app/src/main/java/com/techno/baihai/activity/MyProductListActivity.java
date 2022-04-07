package com.techno.baihai.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.techno.baihai.R;
import com.techno.baihai.adapter.MyProductListAdapter;
import com.techno.baihai.api.Constant;
import com.techno.baihai.model.MyProductModeListl;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.GPSTracker;
import com.techno.baihai.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;
import www.develpoeramit.mapicall.ApiCallBuilder;


public class MyProductListActivity extends AppCompatActivity {


    private static final String TAG = "MyProductListActivity";
    EditText search_Product;
    Spinner spinner;
    String popupDistance;
    private Boolean isInternetPresent = false;
    private Context mContext;
    private RecyclerView product_recyclerView;
    private RecyclerView.Adapter product_mAdapter;
    private CardView card_view, card_view1, card_view2, card_view3;
    private String latitude, longitude;
    private List<MyProductModeListl> myProductModeListls;
    private TextView location_productId, noDataList;
    private String uid, CatId, searchProductTxt;
    private String distance;
    private String popupFillter;
    private String fillte;
    List<String> categoryLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_my_product_list);

        mContext = this;
        categoryLists = new ArrayList<String>();
        isInternetPresent = PrefManager.isNetworkConnected(mContext);
        location_productId = findViewById(R.id.location_productId);
        card_view = findViewById(R.id.card_view);
        card_view1 = findViewById(R.id.card_view1);
        card_view2 = findViewById(R.id.card_view2);
        card_view3 = findViewById(R.id.card_view3);

        User user = PrefManager.getInstance(this).getUser();
        uid = String.valueOf(user.getId());
        Log.i(TAG, "user_id: " + uid);

        getCurrentLocation();


        ImageView drop = findViewById(R.id.drop_downProductId);

        drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(mContext, drop);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.poupup_menu1, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        Toast.makeText(mContext, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        popupDistance = (String) item.getTitle();
                        PrefManager.save(mContext, PrefManager.KEY_DISTANCE, popupDistance);
                        Toast.makeText(mContext, "Miles" + popupDistance, Toast.LENGTH_LONG).show();
                        item.setCheckable(true);
                        item.setChecked(true);


                        Toast.makeText(mContext, "S=> " + popupDistance, Toast.LENGTH_LONG).show();
                        GetSearchProduct();

                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });
        popupDistance = PrefManager.get(mContext, PrefManager.KEY_DISTANCE);
        Log.e("popupDistance", popupDistance);


        product_recyclerView = findViewById(R.id.recycleViewProductList);
        product_recyclerView.setHasFixedSize(true);
        myProductModeListls = new ArrayList<>();

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);


        product_recyclerView.setLayoutManager(layoutManager); // set LayoutManager to RecyclerView


        search_Product = findViewById(R.id.search_Product);
        search_Product.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isInternetPresent) {
                    String productStxt = search_Product.getText().toString().trim();
                    if(!productStxt.equals("")){
                        GetSearchProduct();
                    }
                } else {
                    PrefManager prefManager = new PrefManager(mContext);
                    PrefManager.showSettingsAlert(mContext);
                }
            }
        });
        categoryLists.add("None");
        categoryLists.add("Technology");
        categoryLists.add("Sport");
        categoryLists.add("Furniture");
        categoryLists.add("Tools");
        categoryLists.add("Toys");
        categoryLists.add("Cutlery");
        categoryLists.add("Vehicle");
        categoryLists.add("Pets");
        categoryLists.add("Clothes");

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter ad
                = new ArrayAdapter(
                this, android.R.layout.simple_spinner_item, categoryLists);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        spinner.setAdapter(ad);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {

                String item = adapterView.getItemAtPosition(position).toString();
                Log.e("spinner=>", "" + item);
                GetSearchProduct();
                Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

                //set as selected item.
                spinner.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // vacio

            }
        });
        if (isInternetPresent) {
            if (latitude != null && longitude != null) {
                GetProductList();
            } else {
                Toast.makeText(mContext, "location null", Toast.LENGTH_SHORT).show();
            }
        } else {
            PrefManager prefManager = new PrefManager(mContext);
            PrefManager.showSettingsAlert(mContext);
        }




        noDataList = findViewById(R.id.not_DataList);


    }

    @Override
    protected void onResume() {
        if (isInternetPresent) {
            GetProductList();
        } else {
            PrefManager prefManager = new PrefManager(mContext);
            PrefManager.showSettingsAlert(mContext);
        }
        super.onResume();


    }

    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String addressStreet = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();


                Log.e("Addresss===", addressStreet + " " + city + " " + country);
                location_productId.setText(city + " " + country);
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }


    private void getCurrentLocation() {

        GPSTracker track = new GPSTracker(this);
        if (track.canGetLocation()) {
            latitude = String.valueOf(track.getLatitude());
            Log.e("lat=>", "-------->" + latitude);

            longitude = String.valueOf(track.getLongitude());
            Log.e("lon=>", "-------->" + longitude);
            final double lat1 = track.getLatitude();
            final double lon1 = track.getLongitude();
            getAddress(lat1, lon1);

        } else {
            track.showSettingsAlert();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        User user = PrefManager.getInstance(this).getUser();
        if (!user.getGuideFree().equals("1")) {
            ShowIntro(getResources().getString(R.string.guide_filters), getResources().getString(R.string.guide_filters1), card_view, 1);

        }

    }

    private void GetProductList() {

        String distance = "";
        if (!popupDistance.equals("0")) {

            if (popupDistance.equals("Unlimited")) {

                distance = "10000";
            } else {
                distance = PrefManager.get(mContext, PrefManager.KEY_DISTANCE).split(" ")[0];

            }
        } else {
            distance = "10";
        }
        if (spinner.getSelectedItemPosition() != 0) {
            CatId = "" + spinner.getSelectedItemPosition() + "";
        } else {
            CatId = "0";
        }
        fillte = "DESC";
        Log.e("distance", distance);
        Log.e("latitude", latitude);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", uid);
        param.put("lat", latitude);
        param.put("lon", longitude);
        param.put("category_id", CatId);
        param.put("distance", distance);
        param.put("sort_by", fillte);


        Log.e("uid", uid);
        // http://bai-hai.com/webservice/get_product_by_category?user_id=98&lat=testproduct

        ApiCallBuilder.build(this)
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "get_product_by_category?") // http://bai-hai.com/webservice/get_product_by_category?
                .setParam(param)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        Log.e("ResponseList==>", "" + response);
                        progressDialog.dismiss();
                        noDataList.setVisibility(View.GONE);

                        try {


                            if (myProductModeListls != null) {
                                myProductModeListls.clear();
                            }
                            if(!response.equals("") && response!=null  ) {
                                JSONObject object = new JSONObject(response);
                                String status = object.optString("status");
                                String message = object.optString("message");
                                if (status.equals("1")) {


                                    JSONArray result = object.optJSONArray("result");
                                    //Log.e(TAG, "result=>" + result.length());


                                    // if (result != null) {
                                    if (result != null) {
                                        for (int i = 0; i < result.length(); i++) {

                                            JSONObject object1 = result.getJSONObject(i);

                                            JSONObject object2 = object1.optJSONObject("user_details");

                                            String seller_name = "null";
                                            if (object2 != null) {
                                                seller_name = object2.getString("name");
                                            }
                                            Log.e("", "name=>" + seller_name);

                                            JSONObject object3 = object1.optJSONObject("category_details");

                                            String category_image = "null";
                                            if (object3 != null) {
                                                category_image = object3.getString("cat_image");
                                            }
                                            String category_name = "null";
                                            if (object3 != null) {
                                                category_name = object3.getString("cat_name");
                                            }
                                            Log.e("", "category_image=>" + category_image);
                                            String catImag = category_image;
                                            String catName = category_name;

                                            String product_id = object1.optString("id");

                                            String seller_id = object1.optString("user_id");

                                            String category_id = object1.optString("category_id");
                                            String product_name = object1.optString("name");
                                            String product_name_imageUrl = object1.optString("image1");
                                            String product_description = object1.optString("description");
                                            String product_address = object1.optString("address");
                                            String product_used = object1.optString("used");
                                            String product_lat = object1.getString("lat");
                                            String product_lon = object1.getString("lon");
                                            String product_status = object1.getString("status");
                                            String product_dateTime = object1.getString("date_time");


                                            myProductModeListls.add(new MyProductModeListl(product_id, seller_id, seller_name, category_id,
                                                    product_name, product_description, "",
                                                    product_address, "product_used", product_name_imageUrl,
                                                    "", category_image, category_name, product_lat, product_lon, product_status, product_dateTime));


                                        }

                                    }
                              /*  }else {
                                    noDataList.setVisibility(View.VISIBLE);
                                    Toast.makeText(mContext, "Data Not Found ", Toast.LENGTH_LONG).show();

                                }*/


                                } else {

                                    noDataList.setVisibility(View.VISIBLE);
                                    Toast.makeText(mContext, "Data Not Found ", Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(mContext, "" + e, Toast.LENGTH_LONG).show();


                        }
                            product_mAdapter = new MyProductListAdapter(getApplicationContext(), myProductModeListls);
                            product_recyclerView.removeAllViews();


                            product_recyclerView.setAdapter(product_mAdapter);



                    }

                    @Override
                    public void Failed(String error) {

                        progressDialog.dismiss();
                        noDataList.setVisibility(View.VISIBLE);
                        Toast.makeText(mContext, "" + error, Toast.LENGTH_LONG).show();
                    }
                });


    }

    private void GetSearchProduct() {


/*        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();*/

        // http://bai-hai.com/webservice/product_search?category_id=1&name=testproduct
        // category_id=1&
        // name=testproduct
        if (!popupDistance.equals("0")) {

            if (popupDistance.equals("Unlimited")) {

                distance = "10000";
            } else {
                distance = PrefManager.get(mContext, PrefManager.KEY_DISTANCE).split(" ")[0];

            }
        } else {
            distance = "10";
        }

        Log.e("distance", distance);
        HashMap<String, String> param = new HashMap<>();
        String productStxt = search_Product.getText().toString().trim();
        if (spinner.getSelectedItemPosition() != 0) {
            CatId = "" + spinner.getSelectedItemPosition() + "";
        } else {
            CatId = "0";
        }
        Log.e("Spinner==>", "" + spinner.getSelectedItemPosition());

        Toast.makeText(mContext, "productTxt=> " + productStxt, Toast.LENGTH_SHORT).show();
        param.put("category_id", CatId);
        param.put("name", productStxt);
        param.put("lat", latitude);
        param.put("lon", longitude);
        param.put("distance", distance);
        param.put("sort_by", "DESC");


        ApiCallBuilder.build(this)
                .isShowProgressBar(false)// http://bai-hai.com/webservice/product_search?category_id=5&name=&lat=37.4220014&lon=-122.0840214&distance=10&sort_by=DESC
                .setUrl(Constant.BASE_URL + "product_search?")
                .setParam(param)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        Log.e("ResponseSearch==>", "" + response);
                        // progressDialog.dismiss();
                        noDataList.setVisibility(View.GONE);

                        try {
                            if(!response.equals("") && response!=null  ) {
                                JSONObject object = new JSONObject(response);
                                String status = object.optString("status");
                                String message = object.optString("message");
                                if (status.equals("1")) {


                                    JSONArray result = object.optJSONArray("result");
                                    Log.e(TAG, "result=>" + result);

                                    myProductModeListls.clear();

                                    if (result != null) {
                                        for (int i = 0; i < result.length(); i++) {

                                            JSONObject object1 = result.getJSONObject(i);

                                            JSONObject object3 = object1.optJSONObject("category_details");
                                            JSONObject object2 = object1.optJSONObject("user_details");
                                            String category_image = null;
                                            if (object3 != null) {
                                                category_image = object3.getString("cat_image");
                                            }
                                            String category_name = null;
                                            if (object3 != null) {
                                                category_name = object3.getString("cat_name");
                                            }
                                            Log.e("", "category_image=>" + category_image);

                                            String catImag = category_image;
                                            String catName = category_name;


                                            String seller_id = object1.getString("user_id");
                                            String seller_name = object2.getString("name");
                                            String product_id = object1.getString("id");


                                            String category_id = object1.getString("category_id");

                                            String product_name = object1.getString("name");
                                            String product_name_imageUrl = object1.getString("image1");
                                            String product_description = object1.getString("description");

                                            String product_address = object1.getString("address");

                                            String product_used = object1.getString("used");
                                            String product_lat = object1.getString("lat");
                                            String product_lon = object1.getString("lon");
                                            String product_status = object1.getString("status");
                                            String product_dateTime = object1.getString("date_time");

                                            myProductModeListls.add(new MyProductModeListl(product_id, seller_id,
                                                    seller_name, category_id,
                                                    product_name, product_description, "",
                                                    product_address, product_used, product_name_imageUrl,
                                                    "", category_image, category_name, product_lat, product_lon, product_status, product_dateTime));

                                            product_mAdapter.notifyDataSetChanged();


                                        }

                                    } else {

                                        myProductModeListls.clear();
                                        product_recyclerView.removeAllViews();
                                        noDataList.setVisibility(View.VISIBLE);


                                        Toast.makeText(mContext, "No Data", Toast.LENGTH_LONG).show();
                                    }


                                } else {

                                    myProductModeListls.clear();
                                    product_recyclerView.removeAllViews();
                                    noDataList.setVisibility(View.VISIBLE);


                                    Toast.makeText(mContext, "" + message, Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            noDataList.setVisibility(View.GONE);

                            Toast.makeText(mContext, "Check Your Network: ", Toast.LENGTH_LONG).show();


                        }

                            product_mAdapter = new MyProductListAdapter(getApplicationContext(), myProductModeListls);
                            product_recyclerView.removeAllViews();

                            product_recyclerView.setAdapter(product_mAdapter);



                    }

                    @Override
                    public void Failed(String error) {

                        // progressDialog.dismiss();
                        //CustomSnakbar.showDarkSnakabar(mContext, mview, "" + error);
                        noDataList.setVisibility(View.GONE);

                        Toast.makeText(mContext, "Data not Found: " + error, Toast.LENGTH_LONG).show();
                    }
                });


    }

    private List<String> GetCategory() {


        List<String> categoryProd = new ArrayList<String>();
        String langua = "EN";
        String lang = PrefManager.get(mContext, "lang");
        if (lang.equals("es") && lang != null) {
            langua = "ES";
        }

        HashMap<String, String> param = new HashMap<>();
        param.put("lat", latitude);
        param.put("lon", longitude);
        param.put("language", langua);


        ApiCallBuilder.build(this)
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "get_category") //http://bai-hai.com/webservice/get_category
                .setParam(param)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        Log.e("ResponseCategory=>", "" + response);

                        try {
                            if(!response.equals("") && response!=null  ) {
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
                                            final String category_id = object1.getString("id");

                                            final String category_name = object1.getString("category_name");
                                            final String imageUrl = object1.getString("image");


                                            categoryProd.add(category_name);


                                        }
                                    }


                                } else {

                                    Toast.makeText(mContext, "Not Match", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(mContext, "Exception" + e, Toast.LENGTH_SHORT).show();


                        }


                    }

                    @Override
                    public void Failed(String error) {

                        //CustomSnakbar.showDarkSnakabar(mContext, mview, "" + error);
                        Toast.makeText(mContext, "Error" + error, Toast.LENGTH_SHORT).show();
                    }
                });

        return categoryProd;
    }

    private void ShowIntro(String title, String text, CardView viewId, final int type) {

        new GuideView.Builder(mContext)
                .setTitle(title)
                .setContentText(text)
                .setGravity(Gravity.center)
                .setContentTextSize(12)//optional
                .setTitleTextSize(14)//optional
                .setDismissType(DismissType.anywhere) //optional - default dismissible by TargetView
                .setTargetView(viewId)
                .setGuideListener(new GuideListener() {
                    @Override
                    public void onDismiss(View view) {
                        if (type == 1) {
                            ShowIntro(getResources().getString(R.string.guide_filters2), getResources().getString(R.string.guide_filters21), card_view1, 6);
                        } else if (type == 6) {
                            ShowIntro(getResources().getString(R.string.guide_filters3), getResources().getString(R.string.guide_filters31), card_view2, 5);
                        } else if (type == 5) {
                            card_view3.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 750));
                            ShowIntro(getResources().getString(R.string.guide_product_list), getResources().getString(R.string.guide_product_list1), card_view3, 4);
                        } else if (type == 4) {
                            setGuideProducts();
                            card_view3.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

                            SharedPreferences.Editor sharedPreferencesEditor = mContext.getSharedPreferences("show_case_pref",
                                    Context.MODE_PRIVATE).edit();
                            sharedPreferencesEditor.putBoolean("showcase", false);
                        }
                    }
                })
                .build()
                .show();
    }

    private void setGuideProducts() {
        User user = PrefManager.getInstance(this).getUser();
        String id = null;
        id = user.getId();

        HashMap<String, String> parms1 = new HashMap<>();
        parms1.put("user_id", id);
        parms1.put("activity", "0");
        ApiCallBuilder.build(this)
                .setUrl(Constant.BASE_URL + Constant.GUIDE_FREE)
                .setParam(parms1)
                .execute(new ApiCallBuilder.onResponse() {
                    public void Success(String response) {
                        try {
                            if(!response.equals("") && response!=null  ) {
                                Log.e("selectedresponse=>", "-------->" + response);
                                JSONObject object = new JSONObject(response);
                                String message = object.getString("message");
                                User user2 = new User(
                                        user.getId(),
                                        user.getUsername(),
                                        user.getEmail(),
                                        user.getPassword(),
                                        user.getPhone(),
                                        user.getImage(),
                                        user.getLegalinfo(),
                                        user.getGuide(),
                                        "1",
                                        user.getGuideGiveFree()
                                );

                                PrefManager.getInstance(mContext).userLogin(user2);

                            }
                        } catch (JSONException e) {


                            Toast.makeText(mContext, "Error:" + e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }

                    public void Failed(String error) {
                    }
                });
    }

}
