package com.techno.baihai.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.techno.baihai.R;
import com.techno.baihai.adapter.CategoryAdapter;
import com.techno.baihai.adapter.SliderAdapter;
import com.techno.baihai.api.Constant;
import com.techno.baihai.listner.FragmentListener;
import com.techno.baihai.model.CategoryList;
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

import www.develpoeramit.mapicall.ApiCallBuilder;


public class CategoryFragment extends Fragment {

    private static final String TAG = "CategoryFragment";
    private static final int AUTOCOMPLETE_REQUEST_CODE = 111;
    Context mContext;
    FragmentListener listener;
    CardView product_1;
    TextView heart_searchfragId;
    String latitude;
    String longitude;
    RecyclerView category_recyclerView;
    RecyclerView.Adapter category_mAdapter;
    double lat, lng;
    List<CategoryList> categoryLists;
    String uid;
    EditText search_Category;
    ImageView cat_refresh;
    String searchTxt;
    SliderView sliderView;
    private Boolean isInternetPresent = false;
    private SliderAdapter adapter;
    private List<MyProductModeListl> myProductModeListls;
    private ImageView BackFromCategoryId;


    public CategoryFragment(FragmentListener listener) {
        // Required empty public constructor
        this.listener = listener;
    }

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContext = getActivity();
        isInternetPresent = PrefManager.isNetworkConnected(mContext);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        BackFromCategoryId=view.findViewById(R.id.BackFromCategoryId);
        BackFromCategoryId.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        heart_searchfragId = view.findViewById(R.id.heart_searchfragId);

       /* heart_searchfragId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), PinLocationActivity.class), AUTOCOMPLETE_REQUEST_CODE);

            }
        });
*/


        category_recyclerView = view.findViewById(R.id.recycleViewCategory);
        category_recyclerView.setHasFixedSize(true);
        myProductModeListls = new ArrayList<>();
        categoryLists = new ArrayList<>();



// set a StaggeredGridLayoutManager with 3 number of columns and vertical orientation


        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager
                (3, LinearLayoutManager.VERTICAL);
        category_recyclerView.setLayoutManager(staggeredGridLayoutManager); // set LayoutManager to RecyclerView


        User user = PrefManager.getInstance(getActivity()).getUser();
        uid = String.valueOf(user.getId());
        Log.i(TAG, "user_id: " + uid);
        getCurrentLocation();


        if (isInternetPresent) {
            if (latitude!=null&&longitude!=null){
            GetCategory();
            }else {
                Toast.makeText(mContext, "location null", Toast.LENGTH_SHORT).show();
            }
        } else {
            PrefManager prefManager = new PrefManager(mContext);
            PrefManager.showSettingsAlert(mContext);
        }


     /*   search_Category = view.findViewById(R.id.search_Category);

        search_Category.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isInternetPresent) {
                    GetSearchCategory();
                } else {
                    PrefManager prefManager = new PrefManager(mContext);
                    PrefManager.showSettingsAlert(mContext);
                }

            }
        });
*/

       /* cat_refresh = view.findViewById(R.id.cat_refresh);
        cat_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isInternetPresent) {
                    final Animation myAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
                    cat_refresh.startAnimation(myAnim);
                    GetCategory();
                } else {
                    PrefManager prefManager = new PrefManager(mContext);
                    PrefManager.showSettingsAlert(mContext);
                }
            }
        });*/


        sliderView = view.findViewById(R.id.imageSlider);


        if (isInternetPresent) {
            if (longitude!=null&&latitude!=null){
            GetSliderProductList();}
            else {
                Toast.makeText(mContext, "Location Null", Toast.LENGTH_SHORT).show();
            }
        } else {
            PrefManager prefManager = new PrefManager(mContext);
            PrefManager.showSettingsAlert(mContext);
        }

        return view;
    }
    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
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
                Log.e("Addresss===",addressStreet + " " + city + " " + country);
                // tvServiceLocation.setText(addressStreet + " " + city + " " + country);
                heart_searchfragId.setText(city+" "+country);
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }



    private void GetSliderProductList() {


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", uid);
        param.put("lat", latitude);
        param.put("lon", longitude);


        ApiCallBuilder.build(getActivity())
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "get_latest_product") //http://bai-hai.com/webservice/get_latest_product
                .setParam(param)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        Log.e("get_latest_product=>", "" + response);
                        progressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.optString("status");
                            String message = object.optString("message");
                            if (status.equals("1")) {


                                JSONArray jArray = object.optJSONArray("result");
                               // Log.e(TAG, "get_latest_product=>" + jArray);

                                if (jArray != null) {
                                    for (int i = 0; i < jArray.length(); i++) {


                                        JSONObject object1 = jArray.getJSONObject(i);
                                        JSONObject object3 = object1.optJSONObject("cate_details");
                                        JSONObject object2 = object1.optJSONObject("user_details");

                                        String category_image = object1.getString("cate_image");
                                        String category_name = object3.getString("category_name");
                                        Log.e("", "category_image=>" + category_image);
                                        String seller_name = object2.getString("name");




                                        // Log.e(TAG, "get_latest_product=>" + i);
                                        final String category_id = object1.optString("category_id");


                                        String product_id = object1.optString("id");


                                        String seller_id = object1.optString("user_id");


                                        String product_name = object1.optString("name");
                                        String product_name_imageUrl = object1.optString("image1");
                                        String product_description = object1.optString("description");

                                        String product_address = object1.optString("address");

                                        String product_used = object1.optString("used");
                                        String product_lat=object1.getString("lat");
                                        String product_lon=object1.getString("lon");


                                        // mSliderItems.add(new SliderItem(category_name, imageUrl));
                                        //adapter.notifyDataSetChanged();


                                        myProductModeListls.add(new MyProductModeListl(product_id,seller_id,
                                                seller_name,category_id, product_name,product_description,
                                                "",product_address,product_used,product_name_imageUrl,
                                                "", category_image, category_name, product_lat, product_lon, "product_status", "product_dateTime"));


                                    }
                                }else {
                                    Toast.makeText(mContext, "You have no new product\nnear by ", Toast.LENGTH_SHORT).show();
                                    }



                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(mContext, "Exception" + e, Toast.LENGTH_SHORT).show();


                        }

                        adapter = new SliderAdapter(getActivity(), myProductModeListls);

                        sliderView.setSliderAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        sliderView.setIndicatorAnimation(IndicatorAnimationType.THIN_WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                        sliderView.setIndicatorSelectedColor(Color.WHITE);
                        sliderView.setIndicatorUnselectedColor(Color.GRAY);
                        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
                        sliderView.startAutoCycle();

                    }

                    @Override
                    public void Failed(String error) {

                        progressDialog.dismiss();
                        //CustomSnakbar.showDarkSnakabar(mContext, mview, "" + error);
                        Toast.makeText(mContext, "Error" + error, Toast.LENGTH_SHORT).show();
                    }
                });


    }


    private void getCurrentLocation() {

        GPSTracker track = new GPSTracker(getActivity());
        if (track.canGetLocation()) {
            latitude = String.valueOf(track.getLatitude());
            Log.e("lat=>", "-------->" + latitude);

            longitude = String.valueOf(track.getLongitude());
            Log.e("lon=>", "-------->" + longitude);
            final double lat1 = track.getLatitude();
            final double lon1 = track.getLongitude();
            //heart_searchfragId.setText(Tools.getCompleteAddressString(getActivity(), lat1, lon1));
            getAddress(lat1,lon1);


            //latLng = new LatLng(latitude, longitude);

        } else {
            track.showSettingsAlert();
        }
    }


   /* private void GetSearchCategory()   {


  */
    /*      final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
*/
    /*
        searchTxt = search_Category.getText().toString().trim();
        Toast.makeText(mContext, "searchTxt=>" + searchTxt, Toast.LENGTH_SHORT).show();


        HashMap<String, String> param = new HashMap<>();
        param.put("name", searchTxt);
        // param.put("lon",longitude);

        ApiCallBuilder.build(getActivity())
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "category_search?") //http://bai-hai.com/webservice/category_search?
                .setParam(param)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        Log.e("ResponseS=>", "" + response);
                        // progressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.optString("status");
                            String message = object.optString("message");
                            if (status.equals("1")) {

                                JSONArray jArray = object.optJSONArray("result");
                                Log.e(TAG, "result=>" + jArray);

                                categoryLists.clear();


                                assert jArray != null;
                                for (int i = 0; i < jArray.length(); i++) {


                                    JSONObject object1 = jArray.getJSONObject(i);


                                    Log.e(TAG, "resulti=>" + i);
                                    final String category_id = object1.getString("id");

                                    final String category_name = object1.getString("category_name");
                                    final String imageUrl = object1.getString("image");
                                    Log.e(TAG, "cateSName=>" + category_name);


                                    categoryLists.add(new CategoryList(category_id, category_name, imageUrl));
                                    Toast.makeText(mContext, "cat=> " + category_name, Toast.LENGTH_SHORT).show();

                                    category_mAdapter.notifyDataSetChanged();

                                }


                            } else {
                            categoryLists.clear();
                            category_recyclerView.removeAllViews();
                            Toast.makeText(mContext, "Search Not Match", Toast.LENGTH_SHORT).show();
                        }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(mContext, "Check Your Network: " + e, Toast.LENGTH_SHORT).show();


                        }
                        category_mAdapter = new CategoryAdapter(getActivity(), categoryLists);

                        category_recyclerView.removeAllViews();
                        category_recyclerView.setAdapter(category_mAdapter);


                    }

                    @Override
                    public void Failed(String error) {

                        //  progressDialog.dismiss();
                        //CustomSnakbar.showDarkSnakabar(mContext, mview, "" + error);
                        Toast.makeText(mContext, "check your network" + error, Toast.LENGTH_SHORT).show();
                    }
                });


    }*/


    private void GetCategory() {


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        HashMap<String, String> param = new HashMap<>();
        param.put("lat", latitude);
        param.put("lon", longitude);


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
                                        final String category_id = object1.getString("id");

                                        final String category_name = object1.getString("category_name");
                                        final String imageUrl = object1.getString("image");


                                        categoryLists.add(new CategoryList(category_id, category_name, imageUrl));


                                    }
                                }


                            } else {

                                Toast.makeText(mContext, "Not Match", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(mContext, "Exception" + e, Toast.LENGTH_SHORT).show();


                        }

                        category_mAdapter = new CategoryAdapter(getActivity(), categoryLists);
                        category_recyclerView.removeAllViews();

                        category_recyclerView.setAdapter(category_mAdapter);


                    }

                    @Override
                    public void Failed(String error) {

                        progressDialog.dismiss();
                        //CustomSnakbar.showDarkSnakabar(mContext, mview, "" + error);
                        Toast.makeText(mContext, "Error" + error, Toast.LENGTH_SHORT).show();
                    }
                });


    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            lat = data.getExtras().getDouble("lat");
            lng = data.getExtras().getDouble("lng");
            // heart_searchfragId.setText(Tools.getCompleteAddressString(getActivity(),lat,lng));
        }

    }


}
