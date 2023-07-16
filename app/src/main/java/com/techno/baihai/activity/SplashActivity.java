package com.techno.baihai.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.techno.baihai.R;
import com.techno.baihai.utils.GPSTracker;
import com.techno.baihai.utils.PrefManager;
import com.techno.baihai.utils.Preference;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import static com.techno.baihai.utils.PrefManager.getData;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "splash";
    private static final int REQUEST_CODE_PERMISSION = 2;
    public static Activity splashScreen;
    private final int SPLASH_TIME = 5000;
    Context context = this;
    String latitude, longitude;
    String regID;
    String[] mPermission = {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_MEDIA_LOCATION,


    };
    private Boolean isInternetPresent = false;

    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "HashKey: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_splash);
        PrefManager.isConnectingToInternet(this);
      isInternetPresent = PrefManager.isNetworkConnected(this);
        printHashKey(this);


        //--------------------------------------------------------------------------

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission[0])
                    != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[1])
                            != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[2])
                            != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[3])
                            != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[4])
                            != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[5])
                            != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[6])
                            != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[7])
                            != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[8])
                            != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[9])
                            != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(this, mPermission, REQUEST_CODE_PERMISSION);

                // If any permission aboe not allowed by user, this condition will execute every tim, else your else part will work
            } else {


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        if (PrefManager.getInstance(SplashActivity.this).isLoggedIn()) {
                            Intent intent= new Intent(SplashActivity.this,HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }else {
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }



                    }
                }, SPLASH_TIME);


            }
        }
        catch (Exception e) {
            e.printStackTrace();

        }








        regID = getData(getApplicationContext(), "regId", null);
        getCurrentLocation();


        try {



            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(SplashActivity.this,
                    new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            String newToken = s;
                            Log.e("newToken=>", newToken);
                            Preference.save(SplashActivity.this, Preference.REGISTER_ID, newToken);

                        }
                    });

        } catch (Exception e) {
            //Toast.makeText(context, "Error=>" + e, Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }


        String lang=PrefManager.get(context,"lang");
        Log.e("lang",lang);

        if (lang!=null){
            updateResources(context,lang);
        }else
        {
            updateResources(context,"en");
        }




    }


    public void languageAlert(View view) {
        final androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.language_alert, null);

        CardView btn_okay = mView.findViewById(R.id.btn_okay);

        RadioButton english_btn = mView.findViewById(R.id.english_btn);
        RadioButton spanish_btn = mView.findViewById(R.id.spanish_btn);
        RadioGroup radioGroup = mView.findViewById(R.id.radio);

        alert.setView(mView);
        final androidx.appcompat.app.AlertDialog alertDialog = alert.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.setCanceledOnTouchOutside(false);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View radioButton = radioGroup.findViewById(checkedId);
                int index = radioGroup.indexOfChild(radioButton);

                // Add logic here

                switch (index) {
                    case 0: // first button

                        //Toast.makeText(getApplicationContext(), "Selected button number " + index, Toast.LENGTH_LONG).show();
                        updateResources(context,"en");
                        PrefManager.save(context,"language","en");

                        break;
                    case 1: // secondbutton

                        //Toast.makeText(getApplicationContext(), "Selected button number " + index, Toast.LENGTH_LONG).show();
                        updateResources(context,"es");
                        PrefManager.save(context,"english","es");


                        break;
                }

            }
        });



        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                startActivity(new Intent(context,HomeActivity.class));
            }
        });


        alertDialog.show();

    }




    private static void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    @Override
    protected void onResume() {


        super.onResume();
    }

    private void getCurrentLocation() {

        GPSTracker track = new GPSTracker(SplashActivity.this);
        if (track.canGetLocation()) {
            latitude = String.valueOf(track.getLatitude());
            Log.e("lat=>", "-------->" + latitude);

            longitude = String.valueOf(track.getLongitude());
            Log.e("lon=>", "-------->" + longitude);


        } else {
        }
    }

    public void showSettingsAlert() {
        android.app.AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("No Internet Connection");
        alertDialog.setMessage("You don't have internet connection?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                SplashActivity.this.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                showSettingsAlert();

            }
        });
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Req Code", "" + requestCode);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length == 10 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[3] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[4] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[5] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[6] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[7] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[8] == PackageManager.PERMISSION_GRANTED ||
                    grantResults[9] == PackageManager.PERMISSION_GRANTED

            ) {


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                       Intent intent= new Intent(SplashActivity.this,LoginActivity.class);
                       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                               Intent.FLAG_ACTIVITY_NEW_TASK);
                       startActivity(intent);
                        finish();
                    }
                }, SPLASH_TIME);
            }

        } else {

            Toast.makeText(SplashActivity.this, "Denied", Toast.LENGTH_LONG).show();
            showAppSettingsAlert();

        }
    }


    public void showAppSettingsAlert() {
        android.app.AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("You not allow the permission");
        alertDialog.setMessage("Please go to setting and allow him..!!");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                SplashActivity.this.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                showSettingsAlert();

            }
        });
        alertDialog.show();
    }



}
