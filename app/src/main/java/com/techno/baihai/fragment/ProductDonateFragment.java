package com.techno.baihai.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.techno.baihai.R;
import com.techno.baihai.activity.PinLocationActivity;
import com.techno.baihai.activity.ThankyouPointActivity;
import com.techno.baihai.api.Constant;
import com.techno.baihai.listner.FragmentListener;
import com.techno.baihai.model.User;
import com.techno.baihai.service.MyPlacesAdapter;
import com.techno.baihai.utils.CustomSnakbar;
import com.techno.baihai.utils.GPSTracker;
import com.techno.baihai.utils.PrefManager;
import com.techno.baihai.utils.RealPathUtil;
import com.techno.baihai.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import www.develpoeramit.mapicall.ApiCallBuilder;

import static android.app.Activity.RESULT_OK;


public class  ProductDonateFragment extends Fragment implements Spinner.OnItemSelectedListener {

    private static final String TAG = "ProductDonateFrag";
    private static final int AUTOCOMPLETE_REQUEST_CODE = 111;
    Context mContext;
    FragmentListener listener;
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
    private Boolean isInternetPresent = false;
    private File file;

    String test;
    String p_lat = "", p_lng = "", d_lat = "", d_lng = "";
    MyPlacesAdapter adapter;
    ImageView map_location;


    public ProductDonateFragment(FragmentListener listener) {
        // Required empty public constructor
        this.listener = listener;


    }

    public ProductDonateFragment() {
        // Required empty public constructor


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        mContext = getActivity();
        view = inflater.inflate(R.layout.fragment_product_donate, container, false);


        PrefManager.isConnectingToInternet(getActivity());
        isInternetPresent = PrefManager.isNetworkConnected(getActivity());

        category = new ArrayList<String>();

        //Initializing Spinner
        spinner = view.findViewById(R.id.spinner);

        //Adding an Item Selected Listener to our Spinner
        //As we have implemented the class Spinner.OnItemSelectedListener to this class iteself we are passing this to setOnItemSelectedListener
        spinner.setOnItemSelectedListener(this);


        iv_donate = view.findViewById(R.id.tv_donate);
        iv_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // listener.click(new T(listener));
                startActivity(new Intent(mContext, ThankyouPointActivity.class));
            }
        });


        pr_donateYesid = view.findViewById(R.id.pr_donateYesid);
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
                //Toast.makeText(mContext, "Used=>" + usedTxt, Toast.LENGTH_SHORT).show();


            }
        });


        pr_donateNoid = view.findViewById(R.id.pr_donateNoid);
        pr_donateNoid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pr_donateYesid.setBackgroundColor(Color.parseColor("#727272"));
                pr_donateNoid.setBackgroundColor(Color.parseColor("#257712"));
                pr_donateYesid.setTextColor(Color.parseColor("#000000"));
                pr_donateNoid.setTextColor(Color.parseColor("#FFFFFF"));

                usedTxt = "";
                usedTxt = pr_donateNoid.getText().toString().trim();
                //Toast.makeText(mContext, "Used=>" + usedTxt, Toast.LENGTH_SHORT).show();
            }
        });

        User user = PrefManager.getInstance(getActivity()).getUser();

        uid = String.valueOf(user.getId());
        Log.i(TAG, "user_id: " + uid);


        getCurrentLocation();


        et_productName = view.findViewById(R.id.et_productName);
        et_productDesc = view.findViewById(R.id.et_productDesc);
        et_productLocation = view.findViewById(R.id.et_productLocation);
        map_location = view.findViewById(R.id.map_location);


        map_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), PinLocationActivity.class), AUTOCOMPLETE_REQUEST_CODE);

            }
        });


        tv_donate = view.findViewById(R.id.tv_donate);

        tv_donate.setOnClickListener(new View.OnClickListener() {
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

        pickImg = view.findViewById(R.id.product_pickImg);
        pickImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(getActivity())
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


        if (isInternetPresent) {
            GetCategory();
        } else {
            PrefManager prefManager = new PrefManager(mContext);
            PrefManager.showSettingsAlert(mContext);
        }

        return view;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int poistion, long l) {


        catid = String.valueOf(adapterView.getSelectedItemId());
        Log.i(TAG, "catId=>" + catid);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }


    private void getCurrentLocation() {

        GPSTracker track = new GPSTracker(getActivity());
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


    private void Validate(View view) {

        String p_name = et_productName.getText().toString();
        String p_description = et_productDesc.getText().toString();
        String p_location = et_productLocation.getText().toString();
        //String profile_pic = getStringImage(bitmap);


        if (p_name.equalsIgnoreCase("")) {
            CustomSnakbar.showDarkSnakabar(getContext(), view, "Please enter product name!");
            et_productName.requestFocus();
        } else if (spinner.getSelectedItem().toString().trim().equals("Select Category")) {
            Toast.makeText(mContext, "Please select a Category", Toast.LENGTH_SHORT).show();
        } else if (p_description.equalsIgnoreCase("")) {
            CustomSnakbar.showDarkSnakabar(getContext(), view, "Please enter description!");
            et_productDesc.requestFocus();
        } else if (p_location.equalsIgnoreCase("")) {
            CustomSnakbar.showDarkSnakabar(getContext(), view, "Please select a location!");
            et_productLocation.requestFocus();

        } else {

            UploadProductToStoreApi(uid, p_name, p_description, p_location, view);

        }
    }

    private void UploadProductToStoreApi(final String uid, String p_name, String p_description, String p_location, final View view) {

        if (usedTxt == null) {
            usedTxt = "Y";
        }


        if (file == null) {
            CustomSnakbar.showSnakabar(mContext, view, "Please Select a Product Image");

        } else {


            final ProgressDialog progressDialog;
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

            HashMap<String, File> fileHashMap = new HashMap<String, File>();
            fileHashMap.put("image1", file);
            Log.e("camPath", String.valueOf(file));


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
                           // Toast.makeText(mContext, "apifall" + anError, Toast.LENGTH_LONG).show();


                        }
                    });
        }
    }


    private void GetCategory() {


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
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


                                try {

                                    JSONArray jArray = object.optJSONArray("result");
                                    Log.e(TAG, "result=>" + jArray);
                                    //Initializing the ArrayList

                                    category.add("Select Category");


                                    if (jArray != null) {
                                        for (int i = 0; i < jArray.length(); i++) {


                                            JSONObject object1 = jArray.getJSONObject(i);


                                            // Log.e(TAG, "resulti=>" + i);
                                            category_id = object1.getString("id");
                                            category_name = object1.getString("category_name");
                                            String imageUrl = object1.getString("image");


                                            //subscriptionsList.add(new CategoryList(category_id,category_name,imageUrl));

                                            category.add(category_name);

                                            //Log.i(TAG, "cat=>" + category);

                                            // subscriptionsList.add(new CategoryList(category_id,category_name,imageUrl));


                                        }
                                    }

                                    spinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, category));


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
                       // Toast.makeText(mContext, "Error" + error, Toast.LENGTH_SHORT).show();
                    }
                });


    }


    private void showPictureDialog() {
        if (getActivity() != null) {
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
            Toast.makeText(getActivity(), "Some feilds null", Toast.LENGTH_SHORT).show();
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

        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {

                    try {
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");

                        pickImg.setImageBitmap(imageBitmap);

                        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                        Uri tempUri = getImageUri(mContext, imageBitmap);

                        // CALL THIS METHOD TO GET THE ACTUAL PATH
//                        Toast.makeText(SignUpActivity.this,"Here "+ getRealPathFromURI(tempUri), Toast.LENGTH_LONG).show();


                        pathOfImg = RealPathUtil.getRealPath(mContext, tempUri);
                        file = new File(pathOfImg);
                        //  Toast.makeText(mContext, "CamHerePath" + pathOfImg, Toast.LENGTH_LONG).show();

                        image = pathOfImg;

                    } catch (Exception e) {
                        e.printStackTrace();
                       // Toast.makeText(mContext, "" + e, Toast.LENGTH_SHORT).show();
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
                        file = new File(pathOfImg);

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
            et_productLocation.setText(Tools.getCompleteAddressString(getActivity(), lat, lng));
            //et_productLocation.setText(test);
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


}






