package com.techno.baihai.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import com.techno.baihai.R;
import com.techno.baihai.utils.PrefManager;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {

    TextView tv_about_us, tv_account, tv_support;
    ImageView iv_back;
    private final Context mContext = SettingActivity.this;
    private RadioGroup radioGroup;
    private RadioButton spanish_btn;
    private RadioButton english_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_setting);


        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });


    }


    private static void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    public void languageAlert(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.language_alert, null);

        CardView btn_okay = mView.findViewById(R.id.btn_okay);

        english_btn = mView.findViewById(R.id.english_btn);
        spanish_btn = mView.findViewById(R.id.spanish_btn);
        radioGroup = mView.findViewById(R.id.radio);


        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.setCanceledOnTouchOutside(false);


        String lang = PrefManager.get(mContext, "lang");
        Log.e("lang", lang);

        if (lang.equals("es") && lang != null) {
            english_btn.setChecked(false);
            spanish_btn.setChecked(true);
        } else {
            PrefManager.save(mContext, "lang", "en");
            english_btn.setChecked(true);
            spanish_btn.setChecked(false);
            updateResources(mContext, "en");

        }


        english_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(getApplicationContext(), "Select English", Toast.LENGTH_LONG).show();
                updateResources(mContext, "en");
                PrefManager.save(mContext, "lang", "en");
                english_btn.setChecked(true);
                spanish_btn.setChecked(false);
            }
        });
        spanish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(getApplicationContext(), "Select Spanish", Toast.LENGTH_LONG).show();
                updateResources(mContext, "es");
                PrefManager.save(mContext, "lang", "es");
                english_btn.setChecked(false);
                spanish_btn.setChecked(true);

            }
        });


        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                startActivity(new Intent(mContext, HomeActivity.class));
            }
        });


        alertDialog.show();

    }
}
