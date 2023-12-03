package com.techno.baihai.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.techno.baihai.R;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import www.develpoeramit.mapicall.ApiCallBuilder;

public class ProductDonateActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener {


    private static final String TAG = "ProductDonateFrag";
    private static final int AUTOCOMPLETE_REQUEST_CODE = 111;
    private static ActivityProductDonateBinding binding;

    Context mContext = ProductDonateActivity.this;
    static ArrayList<Uri> returnValue = new ArrayList<Uri>();
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
    CardView tv_donate;
    String test;
    String p_lat = "", p_lng = "", d_lat = "", d_lng = "";
    MyPlacesAdapter adapter;
    ImageView map_location;
    private Boolean isInternetPresent = false;
    private File file1, file2, file3, file4, file5;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia1;
    private ViewPager photos_viewpager;
    private LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    private ImageView btnRemoveImage;
    private ImageView image2;
    ActivityResultLauncher<Intent> pickMedia;
    private HashMap<String, File> fileHashMap;
    private ProgressDialog progressDialog;
    static SliderView imageSlider;
    static RoundedImageView image1;
    //    private MyPagerAdapter myPagerAdapter;
    private RelativeLayout rl_Pager;
    private SliderAdapter proSliderAdapter;

    public static void Task() {
        imageSlider.setVisibility(View.GONE);
        image1.setVisibility(View.VISIBLE);
    }

    public static void clearList(int position) {
        Log.e("btn_removeImage>>", "Press");

//        returnValue.remove(position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        pickMedia1 = registerForActivityResult(
//                new ActivityResultContracts.PickVisualMedia(), uri -> {
//                    // Callback is invoked after the user selects a media i tem or closes the
//                    // photo picker.
//                    if (uri != null) {
//                        //Log.d("PhotoPicker", "Selected URI: " + uri.toString());
//                        // String info = "/"+uri.toString().split(":")[1].split("//")[1];
//
//                        returnValue.add(uri);
//
//                    } else {
//                        Log.d("PhotoPicker", "No media selected");
//                    }
//
//                });
        pickMedia = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            ClipData clipData = data.getClipData();
                            if (clipData != null) {
                                int itemCount = clipData.getItemCount();
                                for (int i = 0; i < itemCount; i++) {
                                    Uri imageUri = clipData.getItemAt(i).getUri();
                                    // Process each selected image (imageUri)
                                    returnValue.add(imageUri);
                                    setUserImage();


                                }


                            }
                        }
                    }
                }
        );


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_product_donate);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_donate);


        // Inflate the layout for this fragment


        PrefManager.isConnectingToInternet(mContext);
        isInternetPresent = PrefManager.isNetworkConnected(mContext);

        category = new ArrayList<String>();
        //set  imageSlider
        imageSlider = findViewById(R.id.imageSlider);
        image1 = findViewById(R.id.image1);
        //Initializing Spinner
        spinner = findViewById(R.id.spinner);

        //Adding an Item Selected Listener to our Spinner
        //As we have implemented the class Spinner.OnItemSelectedListener to this class iteself we are passing this to setOnItemSelectedListener
        spinner.setOnItemSelectedListener(this);


// Registers a photo picker activity launcher in single-select mode.

        pr_donateYesid = findViewById(R.id.pr_donateYesid);

        pr_donateYesid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // listener.click(new T(listener));
                setLog("el producto que donara el usuario es usado");
                pr_donateYesid.setBackgroundColor(Color.parseColor("#257712"));
                pr_donateNoid.setBackgroundColor(Color.parseColor("#727272"));
                pr_donateYesid.setTextColor(Color.parseColor("#FFFFFF"));
                pr_donateNoid.setTextColor(Color.parseColor("#000000"));

                usedTxt = "";
                usedTxt = pr_donateYesid.getText().toString().trim();
                //Toast.makeText(mContext, "Used=>" + usedTxt, Toast.LENGTH_SHORT).show();


            }
        });


        pr_donateNoid = findViewById(R.id.pr_donateNoid);

        pr_donateNoid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLog("el producto que donara el usuario no es usado");
                pr_donateYesid.setBackgroundColor(Color.parseColor("#727272"));
                pr_donateNoid.setBackgroundColor(Color.parseColor("#257712"));
                pr_donateYesid.setTextColor(Color.parseColor("#000000"));
                pr_donateNoid.setTextColor(Color.parseColor("#FFFFFF"));

                usedTxt = "";
                usedTxt = pr_donateNoid.getText().toString().trim();
                //Toast.makeText(mContext, "Used=>" + usedTxt, Toast.LENGTH_SHORT).show();
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

                    setLog("el usuario dono un producto");
                    Validate(view);
                } else {
                    PrefManager prefManager = new PrefManager(mContext);
                    PrefManager.showSettingsAlert(mContext);
                }

            }
        });


        if (isInternetPresent) {
            GetCategory();
        } else {
            PrefManager prefManager = new PrefManager(mContext);
            PrefManager.showSettingsAlert(mContext);
        }

        image2 = findViewById(R.id.image1);
        photos_viewpager = findViewById(R.id.photos_viewpager);
        rl_Pager = findViewById(R.id.rl_Pager);
        sliderDotspanel = findViewById(R.id.SliderDots);


        image2.setOnClickListener(v -> {
            String camera = Manifest.permission.CAMERA;
            //WRITE_EXTERNAL_STORAGE
            String permission_additional = Manifest.permission.READ_EXTERNAL_STORAGE;
            String permission_additional2 = Manifest.permission.READ_EXTERNAL_STORAGE;
            String permission_addtional3 = Manifest.permission.READ_EXTERNAL_STORAGE;
            String permission_addtional4 = Manifest.permission.READ_EXTERNAL_STORAGE;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permission_additional2 = Manifest.permission.READ_MEDIA_VIDEO;
                permission_additional = Manifest.permission.READ_MEDIA_IMAGES;
                permission_addtional3 = Manifest.permission.READ_MEDIA_AUDIO;
                permission_addtional4 = Manifest.permission.READ_MEDIA_IMAGES;
            }

            Dexter.withContext(ProductDonateActivity.this)
                    .withPermissions(camera,
                            permission_additional,
                            permission_additional2,
                            permission_addtional3,
                            permission_addtional4)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {

                                try {


                                    // options.setPreSelectedUrls(ArrayList<Uri> data);
                                    //  Pix.start(ProductDonateActivity.this, options);

//                                    ActivityResultContracts.PickVisualMedia.VisualMediaType mediaType = (ActivityResultContracts.PickVisualMedia.VisualMediaType)
//                                            ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE;
//                                    PickVisualMediaRequest request = new PickVisualMediaRequest.Builder()
//                                            .setMediaType(mediaType)
//                                            .build();
//                                    pickMedia1.launch(request);

                                    pickMultipleImages();

                                } catch (Exception e) {
                                    Log.i(TAG, "cdfcsef: " + e.getMessage());

                                }


                            } else {

                                showSettingDialogue();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();

                        }


                    }).withErrorListener(new PermissionRequestErrorListener() {
                        @Override
                        public void onError(DexterError error) {
                            Log.e("Dexter", "There was an error: " + error.toString());
                        }
                    }).check();

        });
        btnRemoveImage = findViewById(R.id.btn_removeimage);
        btnRemoveImage.setVisibility(View.GONE);


    }

    public void pickMultipleImages() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        pickMedia.launch(intent);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int poistion, long l) {


        catid = String.valueOf(adapterView.getSelectedItemId());
        Log.i(TAG, "catId=>" + catid);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        //textViewName.setText(getName(position));


    }


    private void getCurrentLocation() {

        GPSTracker track = new GPSTracker(mContext);
        if (track.canGetLocation()) {
            latitude = String.valueOf(track.getLatitude());
            Log.e("lat=>", "-------->" + latitude);

            longitude = String.valueOf(track.getLongitude());
            Log.e("lon=>", "-------->" + longitude);

        } else {
            track.showSettingsAlert();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    // @Override
    /*public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Pix.start(ProductDonateActivity.this, options.setRequestCode(100));
            } else {
                Toast.makeText(ProductDonateActivity.this, "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
            }
        }
    }*/

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


                                    finish();

                                    setLog("se cargo el producto donado por el usuario " + p_name + "");
                                    startActivity(new Intent(mContext, ThankyouPointActivity.class));
                                    Animatoo.animateInAndOut(mContext);


                                } else {
                                    progressDialog.dismiss();

                                    CustomSnakbar.showDarkSnakabar(mContext, view, "" + message);
                                }

                            } catch (JSONException e) {

                                progressDialog.dismiss();
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(ANError anError) {
                            progressDialog.dismiss();
                            // Toast.makeText(mContext, "apifall" + anError, Toast.LENGTH_LONG).show();


                        }
                    });
        }
    }

    private void GetCategory() {


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        String langua = "EN";
        String lang = PrefManager.get(mContext, "lang");
        if (lang.equals("es") && lang != null) {
            langua = "ES";
        }

        HashMap<String, String> param = new HashMap<>();
        param.put("lat", latitude);
        param.put("lon", longitude);
        param.put("language", langua);
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
                                    if (lang.equals("es") && lang != null) {
                                        category.add("Seleccionar Categoria");
                                    } else {
                                        category.add("Select Category");
                                    }


                                    if (jArray != null) {
                                        for (int i = 0; i < jArray.length(); i++) {


                                            JSONObject object1 = jArray.getJSONObject(i);


                                            //Log.e(TAG, "resulti=>" + i);
                                            category_id = object1.getString("id");
                                            category_name = object1.getString("category_name");
                                            String imageUrl = object1.getString("image");


                                            category.add(category_name);


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

                                //Toast.makeText(mContext, "Status" + message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                            //Toast.makeText(mContext, "Exception" + e, Toast.LENGTH_SHORT).show();


                        }


                    }

                    @Override
                    public void Failed(String error) {

                        progressDialog.dismiss();
                        //CustomSnakbar.showDarkSnakabar(mContext, mview, "" + error);
                        //Toast.makeText(mContext, "Error" + error, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(mContext, "Some fields in image  are null", Toast.LENGTH_SHORT).show();
        }
    }

    public void choosePhotoFromGallary() {

        Intent pickPhoto = new Intent(Intent.EXTRA_ALLOW_MULTIPLE,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
    }

    private void takePhotoFromCamera() {

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(mContext.getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 0);
        }


    }

    public void setUserImage() {

        try {

            image2.setVisibility(View.GONE);
            rl_Pager.setVisibility(View.GONE);
            btnRemoveImage.setVisibility(View.GONE);
            imageSlider.setVisibility(View.VISIBLE);
            Log.e(TAG, "ImageUri" + returnValue.toString());

            if (returnValue.size() > 0) {
                rotateImages();
                ArrayList<String> dataImg = new ArrayList<String>();

                for (int i = 0; i < returnValue.size(); i++) {
                    try {
                        File file = getFile(mContext, returnValue.get(i));//create path from uri
                        final String[] Info = file.getPath().split(":");
                        dataImg.add(Info[0]);
                    } catch (Exception e) {

                    }
                }
                proSliderAdapter = new SliderAdapter(getApplicationContext(), dataImg);


                imageSlider.setSliderAdapter(proSliderAdapter);
                proSliderAdapter.notifyDataSetChanged();
                imageSlider.setIndicatorAnimation(IndicatorAnimationType.THIN_WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                imageSlider.setIndicatorSelectedColor(Color.WHITE);
                imageSlider.setIndicatorUnselectedColor(Color.GRAY);
                imageSlider.setScrollTimeInSec(4); //set scroll delay in seconds :
                imageSlider.startAutoCycle();


                SharePost();
            }
        } catch (Exception e) {
            Log.e("TAG", "cdsf" + e.getMessage());

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK || requestCode == 100) {
                /* aqui carga las imagenes*/
                //  returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
                // Log.e("TAG",data.getData().getPath());
                if (returnValue.size() == 0) {
                    returnValue.add(data.getData());
                }
                image2.setVisibility(View.GONE);
                //  myPagerAdapter = new MyPagerAdapter(mContext, returnValue);
                // photos_viewpager.setAdapter(myPagerAdapter);
                rl_Pager.setVisibility(View.GONE);
                btnRemoveImage.setVisibility(View.GONE);
                imageSlider.setVisibility(View.VISIBLE);

                Log.e(TAG, "ImageUri" + returnValue.toString());

                if (returnValue.size() > 0) {
                    rotateImages();
                    ArrayList<String> dataImg = new ArrayList<String>();

                    for (int i = 0; i < returnValue.size(); i++) {
                        try {
                            File file = getFile(mContext, returnValue.get(i));//create path from uri
                            final String[] Info = file.getPath().split(":");
                            dataImg.add(Info[0]);
                        } catch (Exception e) {

                        }
                    }
                    proSliderAdapter = new SliderAdapter(getApplicationContext(), dataImg);

                    imageSlider.setSliderAdapter(proSliderAdapter);
                    proSliderAdapter.notifyDataSetChanged();
                    imageSlider.setIndicatorAnimation(IndicatorAnimationType.THIN_WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                    imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                    imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                    imageSlider.setIndicatorSelectedColor(Color.WHITE);
                    imageSlider.setIndicatorUnselectedColor(Color.GRAY);
                    imageSlider.setScrollTimeInSec(4); //set scroll delay in seconds :
                    imageSlider.startAutoCycle();


                    SharePost();
                }
            }
        } catch (Exception e) {
            Log.e("TAG", "cdsf" + e.getMessage());

        }
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            lat = data.getExtras().getDouble("lat");
            lng = data.getExtras().getDouble("lng");
            et_productLocation.setText(Tools.getCompleteAddressString(mContext, lat, lng));
            Log.e("TAG", "latisda" + lat);
            Log.e("TAG", "longidbh" + lng);
        }
    }

    public static File getFile(Context context, Uri uri) throws IOException {
        File destinationFilename = new File(context.getFilesDir().getPath() + File.separatorChar + queryName(context, uri));
        try (InputStream ins = context.getContentResolver().openInputStream(uri)) {
            createFileFromStream(ins, destinationFilename);
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
        return destinationFilename;
    }

    public static void createFileFromStream(InputStream ins, File destination) {
        try (OutputStream os = new FileOutputStream(destination)) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = ins.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static String queryName(Context context, Uri uri) {
        Cursor returnCursor =
                context.getContentResolver().query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

    private void SharePost() throws IOException {


        if (returnValue.size() > 0) {
            fileHashMap = new HashMap<String, File>();

            //     images = new ArrayList<>();
            for (int i = 0; i < returnValue.size(); i++) {

                Log.e("vcfgxb", String.valueOf(returnValue.size()));

                if (i == 0) {
                    file1 = getFile(getApplicationContext(), returnValue.get(i));
                    fileHashMap.put("image1", file1);
                } else if (i == 1) {
                    file2 = getFile(getApplicationContext(), returnValue.get(i));
                    fileHashMap.put("image2", file2);


                } else if (i == 2) {
                    file3 = getFile(getApplicationContext(), returnValue.get(i));
                    fileHashMap.put("image3", file3);


                } else if (i == 3) {
                    file4 = getFile(getApplicationContext(), returnValue.get(i));
                    fileHashMap.put("image4", file4);

                } else if (i == 4) {
                    file5 = getFile(getApplicationContext(), returnValue.get(i));
                    fileHashMap.put("image5", file5);

                }


                Log.e("bfgbf", String.valueOf(fileHashMap));


            }
            // returnValue=null;
        } else {

            CustomSnakbar.showDarkSnakabar(mContext, binding.getRoot(), "Please Select Image!");
        }


    }


    public void rotateImages() {
        if (returnValue.size() > 0) {
            //     images = new ArrayList<>();
            for (int i = 0; i < returnValue.size(); i++) {


                Log.e("vcfgxb", String.valueOf(returnValue.size()));
                InputStream in = null;
                try {
                    in = new FileInputStream(mContext.getContentResolver().openFileDescriptor(returnValue.get(i), "r").getFileDescriptor());
                } catch (FileNotFoundException e) {
                    Log.e("TAG", "originalFilePath is not valid", e);
                }

                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                Matrix matrix = new Matrix();

                matrix.postRotate(90);

                bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);

                byte[] byteArray = stream.toByteArray();

                // Storing Back
                   /* FileOutputStream outStream = null;
                    try {
                        outStream = new FileOutputStream(mContext.getContentResolver().openFileDescriptor(returnValue.get(i), "w").getFileDescriptor());
                        outStream.write(byteArray);
                        outStream.close();
                    } catch (Exception e) {
                        Log.e("TAG","could not save", e);
                    }*/


            }


        } else {

            CustomSnakbar.showDarkSnakabar(mContext, binding.getRoot(), "Please Select Image!");
        }
    }

    public Bitmap getBitmapFromUri(Uri imageUri) {

        getContentResolver().notifyChange(imageUri, null);
        ContentResolver cr = getContentResolver();
        Bitmap bitmap;

        try {
            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, imageUri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

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
                            if (status.equals("true")) {
                                Log.e("selectedresponse=>", "-------->exitoso");
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

    public void onBackFromUploadProduct(View view) {
        onBackPressed();
    }



/*
    private class MyPagerAdapter extends PagerAdapter {

        Context context;
        LayoutInflater layoutInflater;
        ArrayList<String> imgArrayList;


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
            Log.e("image=", "" + imgArrayList.get(position));
            File imgFile = new File(imgArrayList.get(position));
            final int delPosition = position;


            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                images.setImageBitmap(myBitmap);

            }


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
*/

    private static class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderVH> {

        ArrayList<String> imgArrayList;


        public SliderAdapter(Context context, ArrayList<String> imgArrayList) {
            this.imgArrayList = imgArrayList;


        }


        public void deleteItem(int position) {
            this.imgArrayList.remove(position);
            // ProductDonateActivity.clearList(position);
            notifyDataSetChanged();
        }

        @Override
        public SliderVH onCreateViewHolder(ViewGroup parent) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
            return new SliderVH(inflate);
        }

        @Override
        public void onBindViewHolder(SliderVH viewHolder, final int position) {

            Log.e("image=", "" + imgArrayList.get(position));
            File imgFile = new File(imgArrayList.get(position));
            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                viewHolder.imageViewBackground.setImageBitmap(myBitmap);

            }
            viewHolder.btn_removeimage.setOnClickListener(v -> {
                if (getCount() != -1) {
                    deleteItem(position);

                    if (getCount() == 0) {
                        ProductDonateActivity.Task();
                    }
                    notifyDataSetChanged();


                } else {
                    ProductDonateActivity.Task();
                }
            });

        }

        @Override
        public int getCount() {
            //slider view count could be dynamic size
            return imgArrayList.size();
        }

        static class SliderVH extends SliderViewAdapter.ViewHolder {

            View itemView;
            ImageView imageViewBackground;
            ImageView imageGifContainer;
            TextView textViewDescription;
            ImageView btn_removeimage;


            public SliderVH(View itemView) {
                super(itemView);
                imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
                imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
                textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
                btn_removeimage = itemView.findViewById(R.id.btn_removeimage);
                btn_removeimage.setVisibility(View.VISIBLE);

                this.itemView = itemView;
            }
        }


    }
}