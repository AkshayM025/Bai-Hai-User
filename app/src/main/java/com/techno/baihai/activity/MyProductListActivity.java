package com.techno.baihai.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.techno.baihai.R;
import com.techno.baihai.adapter.MyProductListAdapter;
import com.techno.baihai.api.Constant;
import com.techno.baihai.model.MyProductModeListl;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.GPSTracker;
import com.techno.baihai.utils.PrefManager;
import com.techno.baihai.utils.Preference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import www.develpoeramit.mapicall.ApiCallBuilder;


public class MyProductListActivity extends AppCompatActivity {


    private static final String TAG = "MyProductListActivity";
    EditText search_Product;
    ImageView product_refresh;
    String popupDistance;
    private Boolean isInternetPresent = false;
    private Context mContext;
    private RecyclerView product_recyclerView;
    private RecyclerView.Adapter product_mAdapter;
    //    private static final int AUTOCOMPLETE_REQUEST_CODE = 111;
//    private double lat, lng;
    private String latitude, longitude;
    private List<MyProductModeListl> myProductModeListls;
    private TextView location_productId, noDataList;
    private String uid, CatId, searchProductTxt;
    private String distance;
    private String catImag;
    private ImageView cat_ImgId;
    private TextView cat_TxtId;
    private String catName;
    private String popupFillter;
    private String fillte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_my_product_list);

        mContext = this;
        isInternetPresent = PrefManager.isNetworkConnected(mContext);
        location_productId = findViewById(R.id.location_productId);


        User user = PrefManager.getInstance(this).getUser();
        uid = String.valueOf(user.getId());
        Log.i(TAG, "user_id: " + uid);

        getCurrentLocation();

        cat_ImgId = findViewById(R.id.cat_ImgId);
        cat_TxtId = findViewById(R.id.cat_TxtId);


        ImageView drop = findViewById(R.id.drop_downProductId);

        CatId = getIntent().getStringExtra("categoryId");

        String catImgUrl = getIntent().getStringExtra("categoryImage");
        String catName = getIntent().getStringExtra("categoryName");

        Preference.save(mContext, "categoryId", CatId);
        Preference.save(mContext, "categoryName", catName);
        Preference.save(mContext, "categoryImage", catImgUrl);


        Log.i(TAG, "cetId: " + CatId);

        //Toast.makeText(mContext, "categoryId=:" + CatId, Toast.LENGTH_SHORT).show();

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
                        GetProductList();

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
                    GetSearchProduct();
                } else {
                    PrefManager prefManager = new PrefManager(mContext);
                    PrefManager.showSettingsAlert(mContext);
                }
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


        product_refresh = findViewById(R.id.product_refresh);
        product_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(mContext, product_refresh);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.poupup_menu2, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(mContext, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        popupFillter = (String) item.getTitle();
                        PrefManager.save(mContext, PrefManager.KEY_Fillter, popupFillter);
                        Toast.makeText(mContext, "fillter" + popupFillter, Toast.LENGTH_LONG).show();
                        item.setCheckable(true);
                        item.setChecked(true);


                        Toast.makeText(mContext, "S=> " + popupFillter, Toast.LENGTH_LONG).show();
                        GetProductList();

                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });


        noDataList = findViewById(R.id.not_DataList);

        Glide.with(mContext)
                .load(catImgUrl).error(R.drawable.profile_img)
                .into(cat_ImgId);
        // Picasso.get().load(catImag).placeholder(R.drawable.unnamed).into(cat_ImgId);
        cat_TxtId.setText(catName);


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

                // result.append(address.getLocality()).append("\n");
                // result.append(address.getCountryName());
                Log.e("Addresss===", addressStreet + " " + city + " " + country);
                // tvServiceLocation.setText(addressStreet + " " + city + " " + country);
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
            //location_productId.setText(Tools.getCompleteAddressString(this, lat1, lon1));
            getAddress(lat1, lon1);
            //latLng = new LatLng(latitude, longitude);

        } else {
            track.showSettingsAlert();
        }
    }

   /* private void GetRefreshProduct() {


        Log.e("distance", distance);


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        // http://bai-hai.com/webservice/get_product_by_category?
        // category_id=1&
        // user_id=12&
        // distance=10
        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", uid);
        param.put("category_id", CatId);
        param.put("distance", "10");
        ApiCallBuilder.build(this)
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "get_product_by_category?") //http://bai-hai.com/webservice/get_product_by_category?
                .setParam(param)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        Log.e("Response==>", "" + response);
                        progressDialog.dismiss();
                        noDataList.setVisibility(View.GONE);
                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.optString("status");
                            String message = object.optString("message");
                            if (status.equals("1")) {


                                JSONArray result = object.optJSONArray("result");
                                Log.e(TAG, "result=>" + result);
                                myProductModeListls.clear();


                                // if (result != null) {
                                for (int i = 0; i < result.length(); i++) {

                                    JSONObject object1 = result.getJSONObject(i);
                                    JSONObject object2 = object1.optJSONObject("user_details");
                                    String seller_name = object2.getString("name");

                                    Log.e("", "name=>" + seller_name);

                                    JSONObject object3 = object1.optJSONObject("category_details");
                                    String category_image = object3.getString("cat_image");
                                    String category_name = object3.getString("cat_name");
                                    Log.e("", "category_image=>" + category_image);



                                    String product_id = object1.optString("id");


                                    String seller_id = object1.optString("user_id");


                                    String category_id = object1.optString("category_id");

                                    String product_name = object1.optString("name");
                                    String product_name_imageUrl = object1.optString("image1");
                                    String product_description = object1.optString("description");

                                    String product_address = object1.optString("address");

                                    String product_used = object1.optString("used");
                                    String product_lat=object1.getString("lat");
                                    String product_lon=object1.getString("lon");


                                    myProductModeListls.add(new MyProductModeListl(product_id, seller_id, seller_name, category_id,
                                            product_name, product_description, "",
                                            product_address, product_used, product_name_imageUrl,
                                            "", category_image, category_name, product_lat, product_lon));
                                    product_mAdapter.notifyDataSetChanged();

                                }


                            } else {
                                try {
                                    myProductModeListls.clear();
                                    product_recyclerView.removeAllViews();
                                    product_mAdapter.notifyDataSetChanged();
                                    noDataList.setVisibility(View.VISIBLE);
                                    Toast.makeText(mContext, "Data Not Found ", Toast.LENGTH_LONG).show();
                                }catch (Exception e){
                                    Log.e("noDataException", String.valueOf(e));
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(mContext, "Check Your Network: " , Toast.LENGTH_LONG).show();


                        }
                        product_mAdapter = new MyProductListAdapter(getApplicationContext(), myProductModeListls);
                        product_recyclerView.removeAllViews();
                        product_recyclerView.setAdapter(product_mAdapter);



                    }

                    @Override
                    public void Failed(String error) {

                        progressDialog.dismiss();
                        noDataList.setVisibility(View.VISIBLE);
                        Toast.makeText(mContext, "Check Your Network: " + error, Toast.LENGTH_LONG).show();
                    }
                });


    }
*/

    private void GetProductList() {



        if (!popupDistance.equals("0")) {

            if (popupDistance.equals("Unlimited")) {

                distance = "10000";
            }else {
                distance = PrefManager.get(mContext, PrefManager.KEY_DISTANCE);
            }
        } else {
            distance = "10";
        }
        if (popupFillter!=null&&popupFillter.equals("Old Product"))
        {
            fillte="ASC";

        }else{
            fillte="DESC";
        }
        Log.e("distance", distance);


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        // http://bai-hai.com/webservice/get_product_by_category?
        // category_id=1&
        // user_id=12&
        // distance=10

        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", uid);
        param.put("lat", latitude);
        param.put("lon", longitude);
        param.put("category_id", CatId);
        param.put("distance", distance);
        param.put("sort_by", fillte);


        Log.e("uid", uid);


        ApiCallBuilder.build(this)
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "get_product_by_category?") // http://bai-hai.com/webservice/get_product_by_category?
                .setParam(param)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        Log.e("Response==>", "" + response);
                        progressDialog.dismiss();
                        noDataList.setVisibility(View.GONE);

                        try {

                            if (myProductModeListls != null) {
                                myProductModeListls.clear();
                            }
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
                                        catImag = category_image;
                                        catName = category_name;

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
                                                "", category_image, category_name, product_lat, product_lon,product_status,product_dateTime));


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


        HashMap<String, String> param = new HashMap<>();
        String productStxt = search_Product.getText().toString().trim();
        Toast.makeText(mContext, "productTxt=> " + productStxt, Toast.LENGTH_SHORT).show();
        param.put("category_id", CatId);
        param.put("name", productStxt);
        param.put("lat", latitude);
        param.put("lon", longitude);
        param.put("sort_by", "DESC");


        ApiCallBuilder.build(this)
                .isShowProgressBar(false)// http://bai-hai.com/webservice/product_search?category_id=1&name=testproduct
                .setUrl(Constant.BASE_URL + "product_search?")
                .setParam(param)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        Log.e("Response==>", "" + response);
                        // progressDialog.dismiss();
                        noDataList.setVisibility(View.GONE);

                        try {
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
                                        /*JSONObject object2=object1.optJSONObject("user_details");
                                        assert object2 != null;
                                        String seller_name=object2.getString("name");
                                        Log.e("","name=>"+seller_name);
    */
                                        JSONObject object3 = object1.optJSONObject("category_details");
                                        String category_image = null;
                                        if (object3 != null) {
                                            category_image = object3.getString("cat_image");
                                        }
                                        String category_name = null;
                                        if (object3 != null) {
                                            category_name = object3.getString("cat_name");
                                        }
                                        Log.e("", "category_image=>" + category_image);

                                        catImag = category_image;
                                        catName = category_name;


                                        String seller_id = object1.getString("user_id");

                                        String product_id = object1.getString("category_id");


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
                                                "", category_id,
                                                product_name, product_description, "",
                                                product_address, product_used, product_name_imageUrl,
                                                "", category_image, category_name, product_lat, product_lon, product_status, product_dateTime));

                                        product_mAdapter.notifyDataSetChanged();


                                    }
                                }


                            } else {

                                myProductModeListls.clear();
                                product_recyclerView.removeAllViews();
                                noDataList.setVisibility(View.VISIBLE);


                                Toast.makeText(mContext, "" + message, Toast.LENGTH_LONG).show();
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

                        Toast.makeText(mContext, "Check Your Network: " + error, Toast.LENGTH_LONG).show();
                    }
                });


    }


}
