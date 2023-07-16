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
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.techno.baihai.Compress;
import com.techno.baihai.R;
import com.techno.baihai.api.Constant;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.CustomSnakbar;
import com.techno.baihai.utils.GPSTracker;
import com.techno.baihai.utils.PrefManager;
import com.techno.baihai.utils.Preference;
import com.techno.baihai.utils.RealPathUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import www.develpoeramit.mapicall.ApiCallBuilder;

public class EditAccountActivity extends AppCompatActivity implements Compress.onSuccessListener {

    private static final String TAG = "EditAccountACtivity";
    TextView tv_cancel;
    EditText et_firstnameID, et_numberID, et_emailID, et_passwordID;
    String uid, image;
    String pathOfImg;
    String latitude, longitude;
    String regID;
    View mview;
    private Boolean isInternetPresent = false;
    private final Context mContext = this;
    private CircleImageView civ_User;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_edit_account);
        PrefManager.isConnectingToInternet(this);
        isInternetPresent = PrefManager.isNetworkConnected(this);


        civ_User = findViewById(R.id.civ_User);
        et_firstnameID = findViewById(R.id.et_firstnameID);
        et_numberID = findViewById(R.id.et_numberID);
        et_emailID = findViewById(R.id.et_emailID);


        tv_cancel = findViewById(R.id.tv_cancel);

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        User user = PrefManager.getInstance(this).getUser();
        uid = String.valueOf(user.getId());
        Log.e("red_ID", "-------->" + uid);


        if (isInternetPresent) {
            GetProfile(mview);
        } else {
            PrefManager prefManager = new PrefManager(mContext);
            PrefManager.showSettingsAlert(mContext);
        }
        Log.i(TAG, "user_id: " + uid);

        getCurrentLocation();


        try {

            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(EditAccountActivity.this,
                    new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            String newToken = s;
                            Log.e("newToken=>", newToken);
                            Preference.save(EditAccountActivity.this, Preference.REGISTER_ID, newToken);

                        }
                    });

        } catch (Exception e) {

            e.printStackTrace();
        }


    }


    private void getCurrentLocation() {

        GPSTracker track = new GPSTracker(EditAccountActivity.this);
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


    private void GetProfile(View mview) {

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(EditAccountActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", uid);
        ApiCallBuilder.build(this)
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "get_profile?")
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

                                JSONObject result = object.optJSONObject("result");


                                String user_ID = "null";
                                if (result != null) {
                                    user_ID = result.optString("id");
                                }
                                String username = "null";
                                if (result != null) {
                                    username = result.optString("name");
                                }
                                String mobile = "null";
                                if (result != null) {
                                    mobile = result.optString("mobile");
                                }
                                String email = "null";
                                if (result != null) {
                                    email = result.optString("email");
                                }
                                String password = "null";
                                if (result != null) {
                                    password = result.optString("password");
                                }
                                String image = "null";
                                if (result != null) {
                                    image = result.optString("image");
                                }
                                String legal_info = "null";
                                if (result != null) {
                                    legal_info = result.optString("legal_info");
                                }
                                String guide = "null";
                                if (result != null) {
                                    guide = result.optString("guide");
                                }
                                String guide_free = "null";
                                if (result != null) {
                                    guide_free = result.optString("guide_free");
                                }
                                String guide_give_free = "null";
                                if (result != null) {
                                    guide_give_free = result.optString("guide_give_free");
                                }


                                User user = new User(user_ID, username, email, password, mobile, image, legal_info, guide, guide_free, guide_give_free,"false");
                                Picasso.get().load(image).placeholder(R.drawable.profile_img).into(civ_User);

                                PrefManager.getInstance(getApplicationContext()).userLogin(user);

                                et_firstnameID.setText(user.getUsername());
                                et_numberID.setText(mobile);
                                et_emailID.setText(user.getEmail());
                                Picasso.get().load(image).placeholder(R.drawable.profile_img).into(civ_User);


                            } else {
                                progressDialog.dismiss();

                                Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.dismiss();

                            Log.i(TAG, "error: " + e);
                        }
                    }

                    @Override
                    public void Failed(String error) {
                        progressDialog.dismiss();

                        //Toast.makeText(mContext, "Failed" + error, Toast.LENGTH_SHORT).show();
                    }
                });


    }


    public void card_updateInit(View view) {

        if (isInternetPresent) {
            Validate(view);
        } else {
            PrefManager prefManager = new PrefManager(mContext);
            PrefManager.showSettingsAlert(mContext);

        }
    }


    private void Validate(View view) {

        String name = et_firstnameID.getText().toString();
        String number = et_numberID.getText().toString();
        String email = et_emailID.getText().toString();
        //String profile_pic = getStringImage(bitmap);

        String MobilePattern = "[0-9]{10}";
        //String email1 = email.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        if (name.equalsIgnoreCase("")) {
            CustomSnakbar.showDarkSnakabar(this, view, "Please Enter UserName!");
            et_firstnameID.requestFocus();
        } else if (email.equalsIgnoreCase("")) {
            CustomSnakbar.showDarkSnakabar(this, view, "Please Email Can't Empty!");
            et_emailID.requestFocus();
        } else if (number.equalsIgnoreCase("")) {
            CustomSnakbar.showDarkSnakabar(this, view, "Please Email Can't Empty!");
            et_numberID.requestFocus();
        } else if (!number.matches(MobilePattern)) {

            Toast.makeText(getApplicationContext(), "Please enter valid 10 digit phone number", Toast.LENGTH_SHORT).show();
        } else {
            setLog("El usuario solicito actualizar  la informacion de perfil correspondiente");
            UpdateAProfo(uid, name, email, number, view);

        }
    }

    private void UpdateAProfo(final String uid, String name, String email, String number, final View view) {


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        HashMap<String, String> parms = new HashMap<>();
        parms.put("user_id", uid);
        parms.put("name", name);
        parms.put("email", email);
        parms.put("mobile", number);
        parms.put("register_id", Preference.get(EditAccountActivity.this, Preference.REGISTER_ID));
        parms.put("lat", latitude);
        parms.put("lon", longitude);

        HashMap<String, File> fileHashMap = new HashMap<String, File>();
        fileHashMap.put("image", file);


        AndroidNetworking.upload(Constant.BASE_URL + "update_profile?")
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

                                //   https://www.shipit.ng/BaiHai/webservice/get_profile?user_id=1

                                JSONObject result = object.optJSONObject("result");
                                //String id = result.optString("id");
                                Log.e("resultt=>", "" + response);


                                String user_ID = "null";
                                if (result != null) {
                                    user_ID = result.optString("id");
                                }
                                String username = "null";
                                if (result != null) {
                                    username = result.optString("name");
                                }
                                String mobile = "null";
                                if (result != null) {
                                    mobile = result.optString("mobile");
                                }
                                String email = "null";
                                if (result != null) {
                                    email = result.optString("email");
                                }
                                String password = "null";
                                if (result != null) {
                                    password = result.optString("password");
                                }
                                String image = "null";
                                if (result != null) {
                                    image = result.optString("image");
                                }
                                String legal_info = "null";
                                if (result != null) {
                                    legal_info = result.optString("legal_info");
                                }
                                String guide = "null";
                                if (result != null) {
                                    guide = result.optString("guide");
                                }
                                String guide_free = "null";
                                if (result != null) {
                                    guide_free = result.optString("guide_free");
                                }
                                String guide_give_free = "null";
                                if (result != null) {
                                    guide_give_free = result.optString("guide_give_free");
                                }


                                User user = new User(user_ID, username, email, password, mobile, image, legal_info, guide, guide_free, guide_give_free,"false");
                                Picasso.get().load(image).placeholder(R.drawable.profile_img).into(civ_User);

                                PrefManager.getInstance(getApplicationContext()).userLogin(user);

                                et_firstnameID.setText(user.getUsername());
                                et_numberID.setText(mobile);
                                et_emailID.setText(user.getEmail());
                                Picasso.get().load(image).placeholder(R.drawable.profile_img).into(civ_User);

                                CustomSnakbar.showDarkSnakabar(EditAccountActivity.this, view, "Profile Updated Successfully!");
                                Toast.makeText(EditAccountActivity.this, "Profile Updated Successfully!", Toast.LENGTH_LONG).show();
                                GetProfile(view);

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(mContext, AccountActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("imagePath", "image");
                                        startActivity(intent);
                                        Animatoo.animateZoom(mContext);
                                        finish();


                                    }
                                }, 2000);

                            } else {
                                progressDialog.dismiss();

                                CustomSnakbar.showDarkSnakabar(EditAccountActivity.this, view, "" + message);
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
                                setLog("Solicito tomar una foto de la galeria");
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                setLog("Solicito tomar una foto con la camara");
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

                        civ_User.setImageBitmap(imageBitmap);

                        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                        Uri tempUri = getImageUri(mContext, imageBitmap);

                        // CALL THIS METHOD TO GET THE ACTUAL PATH

                        pathOfImg = RealPathUtil.getRealPath(mContext, tempUri);
                        file = new File(pathOfImg);


                        //Toast.makeText(mContext, "HerePath=>  " + pathOfImg, Toast.LENGTH_LONG).show();

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

                        civ_User.setImageBitmap(bitmap);


                        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                        Uri tempUri = getImageUri(mContext, bitmap);


                        pathOfImg = RealPathUtil.getRealPath(mContext, tempUri);
                        file = new File(pathOfImg);


                       // Toast.makeText(mContext, "Here " + pathOfImg, Toast.LENGTH_LONG).show();

                        image = pathOfImg;

                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }

                }
        }
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage,
                "Title", null);
        return Uri.parse(path);
    }


    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = mContext.getContentResolver().query(uri, null, null,
                null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    public void ProfiePicInit(View view) {


        Dexter.withContext(this)
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


    private void showSettingDialogue() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setLog("Solicita permisos adicionales");
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

    @Override
    public void response(boolean status, String message, File file) {

        Log.e("imageStatus: ", String.valueOf(status));
        Log.e("imageMessage", message);
        Log.e("ImageFile: ", String.valueOf(file));
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

