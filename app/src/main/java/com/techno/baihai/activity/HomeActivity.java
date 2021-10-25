package com.techno.baihai.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.techno.baihai.R;
import com.techno.baihai.fragment.ChatFragment;
import com.techno.baihai.fragment.HomeFragment;
import com.techno.baihai.listner.FragmentListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener, FragmentListener {
    FragmentManager fragmentManager;
    private BottomNavigationView navigationView;
    private boolean doubleBackToExitPressedOnce;
    private String chats;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_home);

        setColorOnBar(R.color.colorPrimaryDark);

        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);



        try {

             chats= getIntent().getStringExtra("Chatrequest");

                if (chats!=null && chats.equals("1")){
                    setColorOnBar(R.color.colorPrimaryDark);
                    loadFragment(new ChatFragment(this));
                    navigationView.setSelectedItemId(R.id.chat);


                }else {
                    setColorOnBar(R.color.colorPrimaryDark);
                    loadFragment(new HomeFragment(this));
                    navigationView.setSelectedItemId(R.id.home);


                }


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("home", String.valueOf(e));
            Toast.makeText(this, "home"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        Log.e("Cdate", currentDate);
        Log.e("CTime", currentTime);
        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        Log.e("CDate2", mydate);
        Date cTime = Calendar.getInstance().getTime();
        Log.e("CDate3", String.valueOf(cTime));


    }

    private void loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().addToBackStack("home").replace(R.id.frameContainer, fragment).commit();
        }
    }


    /*  public void loadFragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction().

                replace(R.id.frameContainer, fragment)
                .addToBackStack("home").commit();

    }*/


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.home:
                setColorOnBar(R.color.colorPrimaryDark);
                HomeFragment homeFragment = new HomeFragment(this);
                if (homeFragment != null) {
                    loadFragment(homeFragment);
                } else {
                    startActivity(new Intent(this, SplashActivity.class));
                }

                break;
           /* case R.id.search:
                setColorOnBar(R.color.colorWhite);
                loadFragment(new SearchFragment(this));
                break;*/
            case R.id.chat:
                setColorOnBar(R.color.colorPrimaryDark);
                loadFragment(new ChatFragment(this));
                break;
            case R.id.profile:
                setColorOnBar(R.color.colorPrimaryDark);
                startActivity(new Intent(HomeActivity.this, AccountActivity.class));
                break;
           /* case R.id.search:
                setColorOnBar(R.color.colorPrimaryDark);
                startActivity(new Intent(HomeActivity.this, DriverInfoActivity.class));
                break;*/
        }

        return true;

    }


    @Override
    public void click(Fragment fragment) {
        loadFragment(fragment);
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
            //ExitApp();
            //                super.onBackPressed();
           /* if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 1000);
*/
            if (getSupportFragmentManager().getBackStackEntryCount() <= 0) {
                finish();
            }

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

            //for (int i = 1; i < fragmentManager.getBackStackEntryCount(); ++i) { }
        }

    }

}