package com.techno.baihai.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
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
import androidx.cardview.widget.CardView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.techno.baihai.R;
import com.techno.baihai.api.APIClient;
import com.techno.baihai.api.APIInterface;
import com.techno.baihai.api.Constant;
import com.techno.baihai.model.GetCategoryModel;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.CustomSnakbar;
import com.techno.baihai.utils.GPSTracker;
import com.techno.baihai.utils.PrefManager;
import com.techno.baihai.utils.RealPathUtil;
import com.techno.baihai.utils.Tools;


import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.develpoeramit.mapicall.ApiCallBuilder;


public class UpdateProductActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener {

    private static final String TAG = "ProductDonateFrag";
    private static final int AUTOCOMPLETE_REQUEST_CODE = 111;
    private final Context mContext = UpdateProductActivity.this;
    public ImageView updateBackId;
    CardView tv_deletePost, tv_donatePost;
    ImageView pickImg;
    String uid, image, pathOfImg;
    double lat, lng;
    String latitude, longitude, product_status;
    EditText et_productDesc, et_productName;
    EditText Uet_productLocation;
    private Boolean isInternetPresent = false;
    private TextView id_accepted;
    private TextView id_sold_out;
    private String productCatId, productId, productImgUrl, productName, productAddress, productUsed, productDesc;
    private ImageView id_acceptedImg;
    private ImageView id_sold_outImg;
    private File file;
    private ImageView Umap_location;
    private String code;
    ArrayList<String> category = new ArrayList<>();
    ArrayList<GetCategoryModel.Result> category_new = new ArrayList<>();
    private Spinner spinner;
    private String catid;
    private String category_id;
    private String category_name;
    private APIInterface apiInterface;
    private String Category_id;
    private String PcategoryName;
    private boolean check = true;
    private String productLat;
    private String productLong;
    private String productStatus;
    private TextView statusId;
    private LinearLayout grp_status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_update_product);


        PrefManager.isConnectingToInternet(mContext);
        isInternetPresent = PrefManager.isNetworkConnected(mContext);

        apiInterface = APIClient.getClient().create(APIInterface.class);


        User user = PrefManager.getInstance(mContext).getUser();

        uid = String.valueOf(user.getId());
        Log.i(TAG, "user_id: " + uid);


        getCurrentLocation();

        category = new ArrayList<String>();


        //Initializing Spinner
        spinner = findViewById(R.id.spinneredit);

        //Adding an Item Selected Listener to our Spinner
        //As we have implemented the class Spinner.OnItemSelectedListener to this class iteself we are passing this to setOnItemSelectedListener
        spinner.setOnItemSelectedListener(this);


        et_productName = findViewById(R.id.et_productNameUpdate);
        et_productDesc = findViewById(R.id.et_productDescUpdate);


        tv_donatePost = findViewById(R.id.tv_donatePost);


        tv_deletePost = findViewById(R.id.tv_deletePost);


        pickImg = findViewById(R.id.pickImgUpdate);
        pickImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(UpdateProductActivity.this)
                        .withPermissions(Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {

                                    showPictureDialog();

                                } else {

                                    showSettingDialogue();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();

                            }


                        }).check();
            }
        });

        id_accepted = findViewById(R.id.id_accepted);
        id_sold_out = findViewById(R.id.id_sold_out);
        id_acceptedImg = findViewById(R.id.id_acceptedImg);
        id_sold_outImg = findViewById(R.id.id_sold_outImg);

        Uet_productLocation = findViewById(R.id.Uet_productLocation);
        Umap_location = findViewById(R.id.Umap_location);

        productCatId = getIntent().getStringExtra("categoryId");
        PcategoryName = getIntent().getStringExtra("categoryName");

        productId = getIntent().getStringExtra("productId");
        productImgUrl = getIntent().getStringExtra("productImgUrl");
        productName = getIntent().getStringExtra("productName");
        productAddress = getIntent().getStringExtra("productAddress");
        productUsed = getIntent().getStringExtra("productUsed");
        productDesc = getIntent().getStringExtra("productDesc");

        productLat = getIntent().getStringExtra("productLat");
        productLong = getIntent().getStringExtra("productLong");
        productStatus = getIntent().getStringExtra("productStatus");
        Log.e(TAG, "productStatus=>" + productStatus);


        statusId = findViewById(R.id.statusId);
        grp_status = findViewById(R.id.grp_status);

        if (productStatus.equals("Active")) {
            statusId.setText("Status:");
            grp_status.setVisibility(View.VISIBLE);

        } else {

            statusId.setVisibility(View.VISIBLE);
            statusId.setText("Status:- Pending");
            grp_status.setVisibility(View.GONE);
        }


        Glide.with(mContext).load(productImgUrl).error(R.drawable.user).into(pickImg);
        et_productName.setText(productName);
        et_productDesc.setText(productDesc);
        Uet_productLocation.setText(productAddress);

        //  product_status = "Active";
        id_accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_acceptedImg.setImageResource(R.drawable.radio_circle_fill);
                id_sold_outImg.setImageResource(R.drawable.radio_circlebg);
                product_status = "Active";
            }
        });

        id_sold_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_acceptedImg.setImageResource(R.drawable.radio_circlebg);
                id_sold_outImg.setImageResource(R.drawable.radio_circle_fill);
                product_status = "Deactive";


            }
        });
        tv_donatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isInternetPresent) {
                    Validate(view);
                } else {
                    PrefManager prefManager = new PrefManager(mContext);
                    PrefManager.showSettingsAlert(mContext);
                }

            }
        });
        tv_deletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isInternetPresent) {
                    DeleteProductApi();
                } else {
                    PrefManager prefManager = new PrefManager(mContext);
                    PrefManager.showSettingsAlert(mContext);
            /*AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                    "You don't have internet connection.", false);*/
                }

            }
        });


        Umap_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(UpdateProductActivity.this, PinLocationActivity.class), AUTOCOMPLETE_REQUEST_CODE);


            }
        });


        if (isInternetPresent) {
            GetUserCategoryApi();
        } else {
            PrefManager.showSettingsAlert(mContext);

        }


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int poistion, long l) {

        try {
            if (check) {
                for (int i = 1; i <= category_new.size(); i++) {

                    if (category_new.get(i).getCategoryName().equals(PcategoryName)) {
                        adapterView.setSelection(i);
                        check = false;

                    }

                }
            }


            catid = String.valueOf(adapterView.getItemIdAtPosition(poistion));
            Category_id = category_new.get(poistion).getId();
            Log.e(TAG, "catId=>" + Category_id);
            Log.e(TAG, "pcatId=>" + productCatId);
            Log.e(TAG, "pcatName=>" + PcategoryName);
        } catch (Exception e) {

            Log.e(TAG, "bhhfg=>" + e.getMessage());

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }


    @Override
    public void onResume() {
        super.onResume();


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


    private void Validate(View view) {

        String p_name = et_productName.getText().toString().trim();
        String p_description = et_productDesc.getText().toString().trim();

        if (p_name.equalsIgnoreCase("")) {
            CustomSnakbar.showDarkSnakabar(mContext, view, "Please enter product name!");
            et_productName.requestFocus();
        } else if (p_description.equalsIgnoreCase("")) {
            CustomSnakbar.showDarkSnakabar(mContext, view, "Please enter description!");
            et_productDesc.requestFocus();
        } else {

            UpdateProduct(p_name, p_description, view);

        }
    }


    private void GetUserCategoryApi() {


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        String langua = "EN";
        String lang = PrefManager.get(mContext, "lang");
        if (lang.equals("es") && lang != null) {
            langua = "ES";
        }

        HashMap<String, String> param = new HashMap<>();
        param.put("language", langua);
        Call<GetCategoryModel> call = apiInterface.get_category(param);

        Log.e("get_user_category", "" + call.request().headers());

        call.enqueue(new Callback<GetCategoryModel>() {
            @Override
            public void onResponse(@NotNull Call<GetCategoryModel> call, @NotNull Response<GetCategoryModel> response) {
                progressDialog.dismiss();

                try {
                    GetCategoryModel commentModel = response.body();
                    category.clear();

                    if (commentModel != null) {
                        if (commentModel.getStatus().equals("1")) {
                            int position=0;
                            category_new = (ArrayList<GetCategoryModel.Result>) commentModel.getResult();
                            //list = (ArrayList<GetTopicDataModel>) commentModel.getResult();
                            for (int i = 0; i < commentModel.getResult().size(); i++) {


                                category.add(commentModel.getResult().get(i).getCategoryName());
                                if(commentModel.getResult().get(i).getCategoryName().equals(PcategoryName)){
                                    position=i;
                                }

                            }
                            if (category != null && !category.equals("")) {


                                spinner.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, category));
                                spinner.setSelection(position);
                            } else {
                                Toast.makeText(mContext, "category null..!!", Toast.LENGTH_SHORT).show();
                            }


                        } else {

                            Toast.makeText(mContext, "No Category found", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(mContext, "Model not correct", Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<GetCategoryModel> call, Throwable t) {
                progressDialog.dismiss();

                Toast.makeText(mContext, "" + call, Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void UpdateProduct(final String p_name, String p_description, final View view) {


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        if (String.valueOf(lat) != null && String.valueOf(lng) != null && product_status != null && Category_id != null) {

        } else {
            lat = Double.parseDouble(productLat);
            lng = Double.parseDouble(productLong);
            product_status = productStatus;
            Category_id = productCatId;
        }
        String getNewAddress = Uet_productLocation.getText().toString().trim();

        HashMap<String, String> parms = new HashMap<>();
        parms.put("user_id", uid);
        parms.put("name", p_name);
        parms.put("description", p_description);
        parms.put("category_id", Category_id);
        parms.put("product_id", productId);
        parms.put("price", "0.00");
        parms.put("address", getNewAddress);
        parms.put("used", productUsed);
        parms.put("lat", String.valueOf(lat));
        parms.put("lon", String.valueOf(lng));
        parms.put("status", "Deactive");

        HashMap<String, File> fileHashMap = new HashMap<String, File>();
        fileHashMap.put("image1", file);
        Log.e("camPath", String.valueOf(file));

        // *http://bai-hai.com/webservice/update_product?product_id=1&user_id=71&name=this%20is%20tes&
        // category_id=21&description=this%20is%20tes&price=10&address=12hd&lat=78.12&lon=4615.45&
        // used=Yes&status=Active

        AndroidNetworking.upload(Constant.BASE_URL + "update_product?")
                .addMultipartParameter(parms)
                .addMultipartFile(fileHashMap)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e("ResponseUpdate", "" + response);


                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("1")) {


                                startActivity(new Intent(mContext, HomeActivity.class));
                                Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "Fail To Update, Check Network", Toast.LENGTH_SHORT).show();


                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();

                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();

                    }
                });
    }


    private void DeleteProductApi() {


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        HashMap<String, String> parms = new HashMap<>();
        parms.put("user_id", uid);
        parms.put("product_id", productId);
        ApiCallBuilder.build(this)
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "delete_product?")
                .setParam(parms)
                // .setFile("image1", pathOfImg)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        progressDialog.dismiss();
                        Log.e("ResponseUpdate", "" + response);


                        Log.e("selectedImagePath=>", "-------->" + pathOfImg);


                        try {

                            JSONObject object = new JSONObject(response);
                            String status = object.optString("status");
                            String message = object.optString("message");
                            if (status.equals("1")) {


                                et_productName.setText("");
                                et_productDesc.setText("");
                                // et_productLocation.setText("");

                                // CustomSnakbar.showSnakabar(mContext, view, "Thankyou for Donation\nplease check your email for further instructions!");
                                androidx.appcompat.app.AlertDialog.Builder builder =
                                        new androidx.appcompat.app.AlertDialog.Builder(mContext);
                                builder.setTitle("Donation");
                                builder.setMessage("Your Post Deleted\nSuccesfully!!");
                                builder.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                                startActivity(new Intent(mContext, HomeActivity.class));
                                                Animatoo.animateInAndOut(mContext);


                                            }
                                        });
                                builder.show();


                            } else {
                                progressDialog.dismiss();

                                //CustomSnakbar.showDarkSnakabar(mContext, view, "" + message);
                            }

                        } catch (JSONException e) {

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


    private void showPictureDialog() {
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
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {

                    try {
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");

                        pickImg.setImageBitmap(imageBitmap);

                        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                        Uri tempUri = getImageUri(mContext, imageBitmap);


                        pathOfImg = RealPathUtil.getRealPath(mContext, tempUri);
                        file = new File(pathOfImg);

                        Toast.makeText(mContext, "CamHerePath=>  " + pathOfImg, Toast.LENGTH_LONG).show();

                        image = pathOfImg;

                    } catch (Exception e) {
                        e.printStackTrace();
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


                        pathOfImg = RealPathUtil.getRealPath(mContext, tempUri);
                        file = new File(pathOfImg);


                        Toast.makeText(mContext, "GalleryHerePath " + pathOfImg, Toast.LENGTH_LONG).show();

                        image = pathOfImg;

                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }

                }
        }
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            lat = data.getExtras().getDouble("lat");
            lng = data.getExtras().getDouble("lng");
            Uet_productLocation.setText(Tools.getCompleteAddressString(UpdateProductActivity.this, lat, lng));
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
        Cursor cursor = mContext.getContentResolver().query(uri, null, null,
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

    public void onBackFromEditProduct(View view) {
        onBackPressed();
    }
}