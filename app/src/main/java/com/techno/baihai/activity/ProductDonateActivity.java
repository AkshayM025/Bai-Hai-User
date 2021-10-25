package com.techno.baihai.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.techno.baihai.R;
import com.techno.baihai.adapter.ProSliderAdapter;
import com.techno.baihai.api.Constant;
import com.techno.baihai.databinding.ActivityProductDonateBinding;
import com.techno.baihai.model.User;
import com.techno.baihai.service.MyPlacesAdapter;
import com.techno.baihai.utils.CustomSnakbar;
import com.techno.baihai.utils.GPSTracker;
import com.techno.baihai.utils.PrefManager;
import com.techno.baihai.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import www.develpoeramit.mapicall.ApiCallBuilder;

public class ProductDonateActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener {


    private static final String TAG = "ProductDonateFrag";
    private static final int AUTOCOMPLETE_REQUEST_CODE = 111;
    private static ActivityProductDonateBinding binding;
    Context mContext = ProductDonateActivity.this;
    CardView iv_donate, tv_donate;
    ImageView pickImg;
    String uid, image, pathOfImg;
    double lat, lng;
    String latitude, longitude;
    String regID;
    View view;
    EditText et_productDesc, et_productName;
    TextView pr_donateYesid, pr_donateNoid;
    TextView et_productLocation;
    TextView tv_CategoryId;
    ImageView spinedropId;
    String category_name;
    String category_id;
    String catid;
    ArrayList<String> category;
    Spinner spinner;
    String usedTxt;
    ArrayList<String> returnValue;
    String test;
    String p_lat = "", p_lng = "", d_lat = "", d_lng = "";
    MyPlacesAdapter adapter;
    ImageView map_location;
    private Boolean isInternetPresent = false;
    private File file1, file2, file3, file4, file5;
    private MyPagerAdapter myPagerAdapter;
    private RelativeLayout rl_Pager;
    private ViewPager photos_viewpager;
    private LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    private ImageView image2;
    private ProSliderAdapter proSliderAdapter;
    private HashMap<String, File> fileHashMap;
    private ProgressDialog progressDialog;
    private Options options;

    public static void Task() {
        binding.imageSlider.setVisibility(View.GONE);
        binding.image1.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_product_donate);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_donate);


        // Inflate the layout for this fragment


        PrefManager.isConnectingToInternet(mContext);
        isInternetPresent = PrefManager.isNetworkConnected(mContext);

        category = new ArrayList<String>();


        //Initializing Spinner
        spinner = findViewById(R.id.spinner);

        //Adding an Item Selected Listener to our Spinner
        //As we have implemented the class Spinner.OnItemSelectedListener to this class iteself we are passing this to setOnItemSelectedListener
        spinner.setOnItemSelectedListener(this);


        iv_donate = findViewById(R.id.tv_donate);

        iv_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // listener.click(new T(listener));
                startActivity(new Intent(mContext, ThankyouPointActivity.class));
            }
        });


        pr_donateYesid = findViewById(R.id.pr_donateYesid);

        pr_donateYesid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // listener.click(new T(listener));
                pr_donateYesid.setBackgroundColor(Color.parseColor("#257712"));
                pr_donateNoid.setBackgroundColor(Color.parseColor("#727272"));
                pr_donateYesid.setTextColor(Color.parseColor("#FFFFFF"));
                pr_donateNoid.setTextColor(Color.parseColor("#000000"));

                usedTxt = "";
                usedTxt = pr_donateYesid.getText().toString().trim();
                Toast.makeText(mContext, "Used=>" + usedTxt, Toast.LENGTH_SHORT).show();


            }
        });


        pr_donateNoid = findViewById(R.id.pr_donateNoid);

        pr_donateNoid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pr_donateYesid.setBackgroundColor(Color.parseColor("#727272"));
                pr_donateNoid.setBackgroundColor(Color.parseColor("#257712"));
                pr_donateYesid.setTextColor(Color.parseColor("#000000"));
                pr_donateNoid.setTextColor(Color.parseColor("#FFFFFF"));

                usedTxt = "";
                usedTxt = pr_donateNoid.getText().toString().trim();
                Toast.makeText(mContext, "Used=>" + usedTxt, Toast.LENGTH_SHORT).show();
            }
        });

        User user = PrefManager.getInstance(mContext).getUser();

        uid = String.valueOf(user.getId());
        Log.i(TAG, "user_id: " + uid);


        getCurrentLocation();


        et_productName = findViewById(R.id.et_productName);
        et_productDesc = findViewById(R.id.et_productDesc);
        et_productLocation = findViewById(R.id.et_productLocation);
        map_location = findViewById(R.id.map_location);


        map_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(ProductDonateActivity.this,
                        PinLocationActivity.class), AUTOCOMPLETE_REQUEST_CODE);

            }
        });


        tv_donate = findViewById(R.id.tv_donate);

        tv_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isInternetPresent) {
                    Validate(view);
                } else {
                    PrefManager prefManager = new PrefManager(mContext);
                    PrefManager.showSettingsAlert(mContext);
            /*AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                    "You don't have internet connection.", false);*/
                }

            }
        });

      /*  pickImg = findViewById(R.id.image1);

        pickImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/


        if (isInternetPresent) {
            GetCategory();
        } else {
            PrefManager prefManager = new PrefManager(mContext);
            PrefManager.showSettingsAlert(mContext);
            /*AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                    "You don't have internet connection.", false);*/
        }

  /*      try {


//        places=(AutoCompleteTextView)view.findViewById(R.id.places);
            adapter = new MyPlacesAdapter(getActivity());

            et_productLocation.setAdapter(adapter);
// text changed listener to get results precisely according to our search
            et_productLocation.addTextChangedListener(new TextWatcher() {
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

            et_productLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MyGooglePlaces googlePlaces = (MyGooglePlaces) parent.getItemAtPosition(position);
                    et_productLocation.setText(googlePlaces.getAddress());
                    p_lat = String.valueOf(googlePlaces.getLatitude());
                    p_lng = String.valueOf(googlePlaces.getLongitude());
                }
            });
        }
        catch (Exception e) {
            Toast.makeText(mContext, "Not Found..Try again..!!"+ e, Toast.LENGTH_SHORT).show();
        }*/
        image2 = findViewById(R.id.image1);
        photos_viewpager = findViewById(R.id.photos_viewpager);
        rl_Pager = findViewById(R.id.rl_Pager);
        sliderDotspanel = findViewById(R.id.SliderDots);



        image2.setOnClickListener(v -> {
            Dexter.withActivity(ProductDonateActivity.this)
                    .withPermissions(Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {

                               // showPictureDialog();
                              //  Pix.start(ProductDonateActivity.this,100);
                                try {


                                    options = Options.init()
                                            .setRequestCode(100)                                           //Request code for activity results
                                            .setCount(5)                                                   //Number of images to restict selection count
                                            .setFrontfacing(true)                                         //Front Facing camera on start
                                            //.setPreSelectedUrls(returnValue)                               //Pre selected Image Urls
                                            .setSpanCount(4)                                               //Span count for gallery min 1 & max 5
                                            .setMode(Options.Mode.All)                                     //Option to select only pictures or videos or both
                                            .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
                                            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                                            .setPath("pix/bai-hai");                                       //Custom Path For media Storage

                                    Pix.start(ProductDonateActivity.this, options);
                                }catch (Exception e){
                                    Log.i(TAG, "cdfcsef: " + e.getMessage());

                                }

                                // Pix.start(ProductDonateActivity.this, Options.init().setRequestCode(100));



                            } else {

                                showSettingDialogue();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();

                        }


                    }).check();

        });

        binding.btnRemoveimage.setVisibility(View.GONE);
 /*       binding.btnRemoveimage.setOnClickListener(v -> {
            ProSliderAdapter proSliderAdapter= new ProSliderAdapter(this);

                new ProSliderAdapter(this).deleteItem(binding.imageSlider.getCurrentPagePosition());
                proSliderAdapter.notifyDataSetChanged();
           *//* }else {
                binding.imageSlider.setVisibility(View.GONE);
                binding.btnRemoveimage.setVisibility(View.GONE);
                binding.image1.setVisibility(View.VISIBLE);
            }*//*
        });*/


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int poistion, long l) {


        //  Toast.makeText(mContext, "Select: "+adapterView.getItemIdAtPosition(poistion), Toast.LENGTH_SHORT).show();
        catid = String.valueOf(adapterView.getSelectedItemId());
        Log.i(TAG, "catId=>" + catid);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        //textViewName.setText(getName(position));


    }





/*    public File saveBitmapToFile(File file){
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }*/

    private void getCurrentLocation() {

        GPSTracker track = new GPSTracker(mContext);
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
    @Override
    public void onDestroy(){
        super.onDestroy();
        if ( progressDialog!=null && progressDialog.isShowing() ){
            progressDialog.cancel();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Pix.start(ProductDonateActivity.this, options.setRequestCode(100));
            } else {
                Toast.makeText(ProductDonateActivity.this, "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void Validate(View view) {

        String p_name = et_productName.getText().toString();
        String p_description = et_productDesc.getText().toString();
        String p_location = et_productLocation.getText().toString();
        //String profile_pic = getStringImage(bitmap);


        if (p_name.equalsIgnoreCase("")) {
            CustomSnakbar.showDarkSnakabar(mContext, view, "Please enter product name!");
            et_productName.requestFocus();
        } else if (spinner.getSelectedItem().toString().trim().equals("Select Category")) {
            Toast.makeText(mContext, "Please select a Category", Toast.LENGTH_SHORT).show();
        } else if (p_description.equalsIgnoreCase("")) {
            CustomSnakbar.showDarkSnakabar(mContext, view, "Please enter description!");
            et_productDesc.requestFocus();
        } else if (p_location.equalsIgnoreCase("")) {
            CustomSnakbar.showDarkSnakabar(mContext, view, "Please select a location!");
            et_productLocation.requestFocus();

        } else {

            UploadProductToStoreApi(uid, p_name, p_description, p_location, view);

        }
    }



/*
    private void ProductDonate(final String uid, String p_name, String p_description, String p_location, final View view) {
        //  http://bai-hai.com/webservice/add_product_by_user?name=testproduct&description=thisis%20test&address=vijay&
        // lat=789456&lon=5464&category_id=1&user_id=12
        try {
            if (pathOfImg == null) {
                CustomSnakbar.showSnakabar(mContext, view, "Please Select a Product Image");

            } else if (usedTxt == null) {
                CustomSnakbar.showSnakabar(mContext, view, "Please Select a Product Used");

            } else {
                final ProgressDialog progressDialog;
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Please wait...");
                progressDialog.show();


                HashMap<String, String> parms = new HashMap<>();
                parms.put("user_id", uid);
                parms.put("name", p_name);
                parms.put("description", p_description);
                parms.put("address", p_location);
                parms.put("used", usedTxt);
                parms.put("category_id", catid);
                parms.put("lat", latitude);
                parms.put("lon", longitude);


                ApiCallBuilder.build(getActivity())
                        .isShowProgressBar(false)
                        .setUrl(Constant.BASE_URL + "add_product_by_user?")
                        .setParam(parms)
                        .setFile("image1", pathOfImg)
                        .execute(new ApiCallBuilder.onResponse() {
                            @Override
                            public void Success(String response) {
                                progressDialog.dismiss();
                                Log.e("Responsep=>", "" + response);


                                Log.e("selectedImagePath=>", "-------->" + pathOfImg);


                                try {

                                    JSONObject object = new JSONObject(response);
                                    String status = object.optString("status");
                                    String message = object.optString("message");
                                    if (status.equals("1")) {


                                        et_productName.setText("");
                                        et_productDesc.setText("");
                                        et_productLocation.setText("");


    // CustomSnakbar.showSnakabar(mContext, view, "Thankyou for Donation\nplease check your email for further instructions!");
                                        androidx.appcompat.app.AlertDialog.Builder builder =
                                                new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                                        builder.setTitle("Donation");
                                        builder.setMessage("Your Product Donated\nplease check your email for further instructions!");
                                        builder.setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,
                                                                        int which) {
                                                        getActivity().finish();

                                                    }
                                                });
                                        builder.show();

                                        startActivity(new Intent(mContext, ThankyouPointActivity.class));
                                        Animatoo.animateInAndOut(mContext);


                                    } else {
                                        progressDialog.dismiss();

                                        CustomSnakbar.showDarkSnakabar(getContext(), view, "" + message);
                                    }

                                }
                                catch (JSONException e) {

                                    progressDialog.dismiss();
                                    // Toast.makeText(mContext, "Error:" + e, Toast.LENGTH_SHORT).show();
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
        } catch (Exception e) {
            //progressDialog.dismiss();

            Toast.makeText(mContext, "Error:" + e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }


    }


 */

    private void UploadProductToStoreApi(final String uid, String p_name, String p_description,
                                         String p_location, final View view) {

        if (usedTxt == null) {
            usedTxt = "Y";
        }


        if (fileHashMap == null) {
            CustomSnakbar.showSnakabar(mContext, view, "Please Select a Product Image");

        } else {


            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

            HashMap<String, String> parms = new HashMap<>();
            parms.put("user_id", uid);
            parms.put("name", p_name);
            parms.put("description", p_description);
            parms.put("address", p_location);
            parms.put("used", usedTxt);
            parms.put("category_id", catid);
            parms.put("lat", String.valueOf(lat));
            parms.put("lon", String.valueOf(lng));


            Log.e("camPath", String.valueOf(fileHashMap));


//  http://bai-hai.com/webservice/add_product_by_user?name=testproduct&description=thisis%20test&address=vijay&
            // lat=789456&lon=5464&category_id=1&user_id=12

            AndroidNetworking.upload(Constant.BASE_URL + "add_product_by_user?")
                    .addMultipartParameter(parms)
                    .addMultipartFile(fileHashMap)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            Log.e("ResponseUpdate", "" + response);


                            try {

                                JSONObject object = new JSONObject(response);
                                String status = object.optString("status");
                                String message = object.optString("message");
                                if (status.equals("1")) {


                                    et_productName.setText("");
                                    et_productDesc.setText("");
                                    et_productLocation.setText("");

                                    /*    // CustomSnakbar.showSnakabar(mContext, view, "Thankyou for Donation\nplease check your email for further instructions!");
                                        androidx.appcompat.app.AlertDialog.Builder builder =
                                                new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                                        builder.setTitle("Donation");
                                        builder.setMessage("Your Product Donated\nplease check your email for further instructions!");
                                        builder.setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,
                                                                        int which) {
                                                        getActivity().finish();

                                                    }
                                                });
                                        builder.show();*/

                                    finish();
                                    startActivity(new Intent(mContext, ThankyouPointActivity.class));
                                    Animatoo.animateInAndOut(mContext);


                                } else {
                                    progressDialog.dismiss();

                                    CustomSnakbar.showDarkSnakabar(mContext, view, "" + message);
                                }

                            } catch (JSONException e) {

                                progressDialog.dismiss();
                                // Toast.makeText(mContext, "Error:" + e, Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(ANError anError) {
                            progressDialog.dismiss();
                            Toast.makeText(mContext, "apifall" + anError, Toast.LENGTH_LONG).show();


                        }
                    });
        }
    }

  /*  private void selectImage() {
        final PickImageDialog dialog = PickImageDialog.build(new PickSetup());
        dialog.setOnPickCancel(new IPickCancel() {
            @Override
            public void onCancelClick() {
                dialog.dismiss();
            }
        }).setOnPickResult(new IPickResult() {
            @Override
            public void onPickResult(PickResult r) {

                if (r.getError() == null) {
                    //If you want the Uri.
                    //Mandatory to refresh image from Uri.
                    //getImageView().setImageURI(null);
                    //Setting the real returned image.
                    //getImageView().setImageURI(r.getUri());
                    //If you want the Bitmap.
                    Glide.with(mContext).load(r.getUri()).error(R.drawable.user).into(pickImg);

//                    binding.image.setImageBitmap(r.getBitmap());
                    image = r.getPath();
                    Log.e("Imagepath", image);

                    //r.getPath();
                } else {
                    //Handle possible errors
                    //TODO: do what you have to do with r.getError();
                    Toast.makeText(mContext, r.getError().getMessage(), Toast.LENGTH_LONG).show();
                }

            }

        }).show(getActivity());
    }*/

    private void GetCategory() {


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        HashMap<String, String> param = new HashMap<>();
        param.put("lat", latitude);
        param.put("lon", longitude);
        ApiCallBuilder.build(mContext)
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


                                try {

                                    JSONArray jArray = object.optJSONArray("result");
                                    // Log.e(TAG, "result=>" + jArray);
                                    //Initializing the ArrayList

                                    category.add("Select Category");


                                    if (jArray != null) {
                                        for (int i = 0; i < jArray.length(); i++) {


                                            JSONObject object1 = jArray.getJSONObject(i);


                                            //Log.e(TAG, "resulti=>" + i);
                                            category_id = object1.getString("id");
                                            category_name = object1.getString("category_name");
                                            String imageUrl = object1.getString("image");


                                            //subscriptionsList.add(new CategoryList(category_id,category_name,imageUrl));

                                            category.add(category_name);

                                            //Log.i(TAG, "cat=>" + category);

                                            // subscriptionsList.add(new CategoryList(category_id,category_name,imageUrl));


                                        }
                                    }
                                    if (category != null && !category.equals("")) {


                                        spinner.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, category));

                                    } else {
                                        Toast.makeText(mContext, "Category Not Found..!!", Toast.LENGTH_SHORT).show();
                                    }


                                } catch (Exception e) {
                                    progressDialog.dismiss();

                                    e.printStackTrace();
                                }
                            } else {

                                progressDialog.dismiss();

                                Toast.makeText(mContext, "Status" + message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(mContext, "Exception" + e, Toast.LENGTH_SHORT).show();


                        }


                    }

                    @Override
                    public void Failed(String error) {

                        progressDialog.dismiss();
                        //CustomSnakbar.showDarkSnakabar(mContext, mview, "" + error);
                        Toast.makeText(mContext, "Error" + error, Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void showPictureDialog() {
        if (mContext != null) {
            android.app.AlertDialog.Builder pictureDialog = new AlertDialog.Builder(mContext);
            pictureDialog.setTitle("Select Action");
            String[] pictureDialogItems = {
                    "Select photo from gallery",
                    "Capture photo from camera"};
            pictureDialog.setItems(pictureDialogItems,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    choosePhotoFromGallary();
                                    break;
                                case 1:
                                    takePhotoFromCamera();
                                    break;
                            }
                        }
                    });
            pictureDialog.show();

        } else {
            Toast.makeText(mContext, "Some feilds null", Toast.LENGTH_SHORT).show();
        }
    }

    public void choosePhotoFromGallary() {

        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
    }

    private void takePhotoFromCamera() {

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(mContext.getPackageManager()) != null)
            startActivityForResult(cameraIntent, 0);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK && requestCode == 100) {
                returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
                image2.setVisibility(View.GONE);
                //  myPagerAdapter = new MyPagerAdapter(mContext, returnValue);
                // photos_viewpager.setAdapter(myPagerAdapter);
                rl_Pager.setVisibility(View.GONE);
                binding.btnRemoveimage.setVisibility(View.GONE);
                binding.imageSlider.setVisibility(View.VISIBLE);

                proSliderAdapter = new ProSliderAdapter(getApplicationContext(), returnValue);

                binding.imageSlider.setSliderAdapter(proSliderAdapter);
                proSliderAdapter.notifyDataSetChanged();
                binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.THIN_WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                binding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                binding.imageSlider.setIndicatorSelectedColor(Color.WHITE);
                binding.imageSlider.setIndicatorUnselectedColor(Color.GRAY);
                binding.imageSlider.setScrollTimeInSec(4); //set scroll delay in seconds :
                binding.imageSlider.startAutoCycle();



     /*       dotscount = myPagerAdapter.getCount();
            dots = new ImageView[dotscount];

            for (int i = 0; i < dotscount; i++) {

                dots[i] = new ImageView(this);
                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.non_active_dot));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                params.setMargins(8, 0, 8, 0);

                sliderDotspanel.addView(dots[i], params);

            }

            dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.active_dot));

            photos_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int pos) {

                    for (int i = 0; i < dotscount; i++) {
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                                R.drawable.non_active_dot));
                    }

                    dots[pos].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                            R.drawable.active_dot));


                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }


            });*/

                SharePost();
            }
        }catch (Exception e){
            Log.e("TAG", "cdsf" + e.getMessage());

        }
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            lat = data.getExtras().getDouble("lat");
            lng = data.getExtras().getDouble("lng");
            et_productLocation.setText(Tools.getCompleteAddressString(mContext, lat, lng));
            Log.e("TAG", "latisda" + lat);
            Log.e("TAG", "longidbh" + lng);

            //et_productLocation.setText(test);
        }
    }

    private void SharePost() {


        if (returnValue.size() > 0) {
            fileHashMap = new HashMap<String, File>();

            //     images = new ArrayList<>();
            for (int i = 0; i < returnValue.size(); i++) {

                String path = returnValue.get(i);
                Log.e("vcfgxb", String.valueOf(returnValue.size()));

                if (i == 0) {
                    file1 = new File(returnValue.get(0));
                    fileHashMap.put("image1", file1);
                } else if (i == 1) {
                    file2 = new File(returnValue.get(1));
                    fileHashMap.put("image2", file2);


                } else if (i == 2) {
                    file3 = new File(returnValue.get(2));
                    fileHashMap.put("image3", file3);


                } else if (i == 3) {
                    file4 = new File(returnValue.get(3));
                    fileHashMap.put("image4", file4);

                } else if (i == 4) {
                    file5 = new File(returnValue.get(4));
                    fileHashMap.put("image5", file5);

                }


                Log.e("bfgbf", String.valueOf(fileHashMap));




             /*   if (path.equalsIgnoreCase("")) {
                    System.out.print(path);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("image[]", "");
                  //  images.add(body);
                }
                else {
                    System.out.print(path);
                    File file = new File(path);
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("image[]", file.getName(), requestFile);
                   // images.add(body);
                }*/
            }

            //  AddPost(uid,postdesc,tags,location,images,v);

        } else {

            CustomSnakbar.showDarkSnakabar(mContext, binding.getRoot(), "Please Select Image!");
        }


    }


/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {

//                    Uri selectedImage = data.getData();
//
//                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//
//                    user_profile.setImageBitmap(bitmap);

                    try {
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");

                        pickImg.setImageBitmap(imageBitmap);

                        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                        Uri tempUri = getImageUri(mContext, imageBitmap);

                        // CALL THIS METHOD TO GET THE ACTUAL PATH
//                        Toast.makeText(SignUpActivity.this,"Here "+ getRealPathFromURI(tempUri), Toast.LENGTH_LONG).show();


                        pathOfImg = RealPathUtil.getRealPath(mContext, tempUri);
                        file1 = new File(pathOfImg);
                       // Toast.makeText(mContext, "CamHerePath" + pathOfImg, Toast.LENGTH_LONG).show();

                        image = pathOfImg;

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(mContext, ""+e, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {

                    Uri selectedImage = data.getData();

                    try {

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), selectedImage);

                        pickImg.setImageBitmap(bitmap);


                        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                        Uri tempUri = getImageUri(mContext, bitmap);

                        // CALL THIS METHOD TO GET THE ACTUAL PATH
//                        Toast.makeText(SignUpActivity.this,"Here "+ getRealPathFromURI(tempUri), Toast.LENGTH_LONG).show();


                        pathOfImg = RealPathUtil.getRealPath(mContext, tempUri);
                        file1 = new File(pathOfImg);

                       // Toast.makeText(mContext, "galleryHerePath" + pathOfImg, Toast.LENGTH_LONG).show();

                        image = pathOfImg;

                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }

                }


        }
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            lat = data.getExtras().getDouble("lat");
            lng = data.getExtras().getDouble("lng");
            et_productLocation.setText(Tools.getCompleteAddressString(mContext, lat, lng));
            Log.e("TAG", "latisda" + lat);
            Log.e("TAG", "longidbh" + lng);

            //et_productLocation.setText(test);
        }


    }*/


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage,
                "Title" + System.currentTimeMillis(), null);
        return Uri.parse(path);
    }


    public String getRealPathFromURI(Uri uri) {
        @SuppressLint("Recycle") Cursor cursor = mContext.getContentResolver().query(uri, null, null,
                null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    private void showSettingDialogue() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                openSetting();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }


    private void openSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


    public void onBackFromUploadProduct(View view) {
        onBackPressed();
    }


    private class MyPagerAdapter extends PagerAdapter {

        Context context;
        LayoutInflater layoutInflater;
        ArrayList<String> imgArrayList;
//        int[] img = {R.drawable.roomimg, R.drawable.img, R.drawable.img};


        public MyPagerAdapter(Context mContext, ArrayList<String> img) {
            this.context = mContext;
            this.imgArrayList = img;
        }


        @Override
        public int getCount() {
            return imgArrayList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {

            return view == o;

        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) context.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.pager_img_layout, null);
            ImageView images = (ImageView) view.findViewById(R.id.imageView);
            //   ImageView btn_removeimage = (ImageView) view.findViewById(R.id.btn_removeimage);

//            images.setImageResource(imgArrayList.get(position));
            Log.e("image=", "" + imgArrayList.get(position));
            File imgFile = new File(imgArrayList.get(position));
            final int delPosition = position;


            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                images.setImageBitmap(myBitmap);

            }
            ;
//            Picasso.get().load(imgArrayList.get(position)).into(images);

            photos_viewpager = (ViewPager) container;
            photos_viewpager.addView(view, 0);
            return view;

        }


        void delCurrentPageFnc() {
            int delIdxVar = photos_viewpager.getCurrentItem();

            if (imgArrayList.size() < 1) {
                photos_viewpager.setCurrentItem(delIdxVar - 1);
                notifyDataSetChanged();
            } else {
                photos_viewpager.setCurrentItem(delIdxVar + 1);
                imgArrayList.clear();
                myPagerAdapter.notifyDataSetChanged();

            }
        }


        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {


            photos_viewpager = (ViewPager) container;
            View view = (View) object;

            photos_viewpager.removeView(view);


        }
    }


}
