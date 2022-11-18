package com.techno.baihai.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.techno.baihai.R;
import com.techno.baihai.api.Constant;
import com.techno.baihai.fragment.ChatFragment;
import com.techno.baihai.fragment.HomeFragment;
import com.techno.baihai.listner.FragmentListener;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import www.develpoeramit.mapicall.ApiCallBuilder;

public class HomeActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener, FragmentListener {
    FragmentManager fragmentManager;
    private BottomNavigationView navigationView;
    private boolean doubleBackToExitPressedOnce;
    private String chats;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private final Context mContext = this;
    private ProgressBar progressBar;
    private PaymentsClient paymentsClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_home);

        setColorOnBar(R.color.colorPrimaryDark);

        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);


        try {

            chats = getIntent().getStringExtra("Chatrequest");

            if (chats != null && chats.equals("1")) {
                setColorOnBar(R.color.colorPrimaryDark);
                loadFragment(new ChatFragment(this));
                navigationView.setSelectedItemId(R.id.chat);
                setLog("El usuario se movio al chat");

            } else {
                setColorOnBar(R.color.colorPrimaryDark);
                loadFragment(new HomeFragment(this));
                progressBar = findViewById(R.id.progressBar);
                navigationView.setSelectedItemId(R.id.home);
                this.LegalInfo(navigationView);
                this.sendNotification();
                setLog("El usuario se al home principal");

            }
            BottomNavigationView navBar  = findViewById(R.id.bottom_navigation);
            navBar.getOrCreateBadge(1).setNumber(3);




        } catch (Exception e) {
            e.printStackTrace();
            Log.e("home", String.valueOf(e));
            //Toast.makeText(this, "home" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        Log.e("Cdate", currentDate);
        Log.e("CTime", currentTime);
        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        Log.e("CDate2", mydate);
        Date cTime = Calendar.getInstance().getTime();
        Log.e("CDate3", String.valueOf(cTime));
        this.sendNotificationDonate();

    }

    private void loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().addToBackStack("home").replace(R.id.frameContainer, fragment).commit();
        }
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.home:
                setColorOnBar(R.color.colorPrimaryDark);
                setLog("El usuario se redirigio al home con menu inferior");
                HomeFragment homeFragment = new HomeFragment(this);
                if (homeFragment != null) {
                    loadFragment(homeFragment);
                } else {
                    startActivity(new Intent(this, SplashActivity.class));
                }

                break;
            case R.id.chat:
                setColorOnBar(R.color.colorPrimaryDark);
                setLog("El usuario se redirigio al chat con menu inferior");
                loadFragment(new ChatFragment(this));
                break;
            case R.id.profile:
                setLog("El usuario se redirigio a informacion de perfil con menu inferior");
                setColorOnBar(R.color.colorPrimaryDark);
                startActivity(new Intent(HomeActivity.this, AccountActivity.class));
                break;
        }

        return true;

    }

    private void sendNotification() {
        User user = PrefManager.getInstance(this).getUser();
        HashMap<String, String> parms = new HashMap<>();
        parms.put("id_user", user.getId());
        parms.put("message", "message user");
        ApiCallBuilder.build(this)
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "notify_donate?")
                .setParam(parms)
//                    .setFile("image", "file_path")
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        Log.d(TAG, "respoLogin:" + response);

                        if (!response.equals("\r\n")) {
                            try {
                                JSONObject object = new JSONObject(response);
                                String status = object.getString("status");
                                String message = object.getString("message");

                                Log.e(TAG, "STATUS_:" + status);

                                if (status.equals("1")) {

                                    Toast.makeText(mContext, "Notification sended",
                                            Toast.LENGTH_SHORT).show();


                                }


                            } catch (JSONException e) {


                                Log.i(TAG, "errorL", e);
                                //Toast.makeText(mContext, "Error:" + e, Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void Failed(String error) {
                        //Toast.makeText(mContext, "" + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void sendNotificationDonate() {
        User user = PrefManager.getInstance(this).getUser();
        HashMap<String, String> parms = new HashMap<>();
        parms.put("id_user", user.getId());
        parms.put("message", "Please help us  donate products to Bye-Hi");
        ApiCallBuilder.build(this)
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "notify_donate?")
                .setParam(parms)
//                    .setFile("image", "file_path")
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        Log.d(TAG, "respoLogin:" + response);

                        if (!response.equals("\r\n")) {
                            try {
                                JSONObject object = new JSONObject(response);
                                String status = object.getString("status");
                                String message = object.getString("message");

                                Log.e(TAG, "STATUS_:" + status);

                                if (status.equals("1")) {

                                    Toast.makeText(mContext, "Notification sended",
                                            Toast.LENGTH_SHORT).show();


                                }


                            } catch (JSONException e) {


                                Log.i(TAG, "errorL", e);
                                //Toast.makeText(mContext, "Error:" + e, Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void Failed(String error) {
                        //Toast.makeText(mContext, "" + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void LegalInfo(View view) {
        User user = PrefManager.getInstance(this).getUser();


        String legal = user.getLegalinfo();

        if (legal.equals("0")) {
            String langua = "Yes";
            String langua1 = "No";
            String lang = PrefManager.get(mContext, "lang");
            setLog("El usuario  se encuentra en idioma ingles");
            if (lang.equals("es") && lang != null) {
                langua = "Si";
                langua1 = "No";
                setLog("El usuario  se encuentra en idioma español");
            }
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(mContext);
            // Configura el titulo.
            alertDialogBuilder.setTitle(getResources().getString(R.string.legal_information));

            // Configura el mensaje.
            alertDialogBuilder
                    .setMessage(getResources().getString(R.string.legal_mesagge))
                    .setCancelable(false)
                    .setPositiveButton(langua, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            setLegal();

                        }
                    })

                    .setNegativeButton(langua1, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).create().show();


        }


    }

    @Override
    public void click(Fragment fragment) {
        loadFragment(fragment);
    }

    void setLegal() {
        User user = PrefManager.getInstance(this).getUser();
        String email = null;
        if (user.getEmail() == "") {
            email = user.getId();
        } else {
            email = user.getEmail();
        }

        HashMap<String, String> parms1 = new HashMap<>();
        parms1.put("email", email);
        parms1.put("activity", "0");
        ApiCallBuilder.build(this)
                .setUrl(Constant.BASE_URL + Constant.LEGAL)
                .setParam(parms1)
                .execute(new ApiCallBuilder.onResponse() {
                    public void Success(String response) {
                        try {
                            Log.d(TAG, "respoLogin:" + response);
                            JSONObject object = new JSONObject(response);
                            String message = object.getString("message");
                            User user2 = new User(
                                    user.getId(),
                                    user.getUsername(),
                                    user.getEmail(),
                                    user.getPassword(),
                                    user.getPhone(),
                                    user.getImage(),
                                    "1",
                                    user.getGuide(),
                                    user.getGuideFree(),
                                    user.getGuideGiveFree(),
                                    user.getSuscribe()
                            );
                            setLog("El usuario confirmo informacion legal");

                            PrefManager.getInstance(getApplicationContext()).userLogin(user2);

                        } catch (JSONException e) {


                            Log.i(TAG, "errorL", e);
                            //Toast.makeText(mContext, "Error:" + e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }

                    public void Failed(String error) {
                    }
                });
    }


    void setColorOnBar(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(color, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(color));
        }
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {

            if (getSupportFragmentManager().getBackStackEntryCount() <= 0) {
                finish();
            }
            setLog("El usuario presiono boton para salir de atras para salir de app");
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Closing Activity")
                    .setMessage("Are you sure you want to close this App?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();


        } else if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack();
        }

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