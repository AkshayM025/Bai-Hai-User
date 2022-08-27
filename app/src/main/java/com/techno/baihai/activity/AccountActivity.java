package com.techno.baihai.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationManagerCompat;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;
import com.techno.baihai.BuildConfig;
import com.techno.baihai.R;
import com.techno.baihai.api.Constant;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import www.develpoeramit.mapicall.ApiCallBuilder;

public class AccountActivity extends AppCompatActivity {

    private static final String TAG = "AccountActivity";
    TextView tv_cancel, tv_edit_account;
    LinearLayout tv_aboutus, tv_support, tv_deletedata;
    CardView setting;
    CircleImageView profile_imgId;
    TextView acc_nameId;
    Context mContext = AccountActivity.this;
    String imagePath;
    private User user;
    private Boolean isInternetPresent = false;
    private LinearLayout tv_shareApp;
    private String uid;
    private final int REQUEST_CODE_MY_PICK = 1;
    private ShareDialog shareDialog;
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_account);
        PrefManager.isConnectingToInternet(this);
        isInternetPresent = PrefManager.isNetworkConnected(this);


        profile_imgId = findViewById(R.id.profile_img);

        acc_nameId = findViewById(R.id.acc_nameId);


        tv_cancel = findViewById(R.id.tv_cancel);

        tv_cancel.setOnClickListener(v -> {
            setLog("presiono en boton cancelar de sesion de cuenta");
            startActivity(new Intent(AccountActivity.this, HomeActivity.class));
            Animatoo.animateSlideRight(mContext);
        });

        tv_edit_account = findViewById(R.id.tv_edit_account);

        tv_edit_account.setOnClickListener(v -> {
            setLog("presiono en boton de editar cuenta");
            startActivity(new Intent(AccountActivity.this, EditAccountActivity.class));

        });

        setting = findViewById(R.id.setting);

        setting.setOnClickListener(v -> {
            setLog("presiono en boton de configuraciones para idioma de sesion de cuenta");
            startActivity(new Intent(AccountActivity.this, SettingActivity.class));});


        tv_aboutus = findViewById(R.id.tv_aboutus);

        tv_aboutus.setOnClickListener(v -> {
            setLog("presiono en boton de acerca de nosotros de sesion de cuenta");
            startActivity(new Intent(AccountActivity.this, AboutUsActivity.class));
        });
        tv_deletedata = findViewById(R.id.tv_deletedata);
        tv_deletedata.setOnClickListener(v -> {
            setLog("presiono en boton de eliminar mi cuenta");
            Deletedata(getWindow().getCurrentFocus());

        });
        tv_support = findViewById(R.id.tv_support);

        tv_support.setOnClickListener(v ->{
            setLog("presiono en boton de respuesta a preguntas frecuentes de sesion de cuenta");
            startActivity(new Intent(AccountActivity.this, SupportActivity.class));});

        tv_shareApp = findViewById(R.id.tv_shareApp);

        tv_shareApp.setOnClickListener(v -> {
            setLog("presiono en boton de compartir app de sesion de cuenta");
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setQuote("Hey check out my app at:")
                        .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID))
                        .build();
                shareDialog.show(linkContent);
            }


        });

        User user = PrefManager.getInstance(this).getUser();
        uid = String.valueOf(user.getId());
        Log.e(TAG, "user_id: " + uid);


        getUserName();


        shareDialog = new ShareDialog((AppCompatActivity) mContext);
        callbackManager = CallbackManager.Factory.create();
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.e("TAG", "Facebook Share Success");
                //logoutFacebook();

            }

            @Override
            public void onCancel() {
                Log.e("TAG", "Facebook Sharing Cancelled by User");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("TAG", "Facebook Share Success: Error: " + error.getLocalizedMessage());
            }
        });

    }


    private void ShareApp() {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out my app at:\n https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        sendIntent.setType("text/plain");
        setResult(RESULT_OK, sendIntent);
        startActivityForResult(Intent.createChooser(sendIntent, "Share with"), REQUEST_CODE_MY_PICK);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_MY_PICK) {
            Toast.makeText(AccountActivity.this.getApplicationContext(), "Got Callback yeppeee...:", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(AccountActivity.this.getApplicationContext(), "Got", Toast.LENGTH_SHORT).show();

        }
    }


    private void UpdateProfilePoints() {

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(AccountActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", uid);
        ApiCallBuilder.build(this)
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "update_reward?")
                .setParam(param)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        Log.e("Responseupdate_reward=>", "" + response);
                        progressDialog.dismiss();
                        try {

                            JSONObject object = new JSONObject(response);
                            String status = object.optString("status");
                            String message = object.optString("message");
                            if (status.equals("1")) {
                                Toast.makeText(mContext, "You get +3Coins, Check Your Wallet...!!" + message, Toast.LENGTH_LONG).show();
                                setLog("gano 3 puntos por ingresar a cuenta");

                            } else {
                                progressDialog.dismiss();

                                Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.dismiss();

                            Log.i(TAG, "error: " + e);
                            //Toast.makeText(EditAccountActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void Failed(String error) {
                        progressDialog.dismiss();

                        //Toast.makeText(mContext, "Failed" + error, Toast.LENGTH_SHORT).show();
                    }
                });


    }
    public void Deletedata(View view) {
        User user = PrefManager.getInstance(this).getUser();



            String langua = "Yes";
            String langua1 = "No";
            String lang = PrefManager.get(mContext, "lang");
            if (lang.equals("es") && lang != null) {
                langua = "Si";
                langua1 = "No";

            }
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(mContext);
            // Configura el titulo.
            alertDialogBuilder.setTitle(getResources().getString(R.string.delete_data_title));

            // Configura el mensaje.
            alertDialogBuilder
                    .setMessage(getResources().getString(R.string.delete_data_info))
                    .setCancelable(false)
                    .setPositiveButton(langua, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            setDeleteData();

                        }
                    })

                    .setNegativeButton(langua1, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).create().show();





    }
    void setDeleteData() {
        User user = PrefManager.getInstance(this).getUser();
        String email = null;
        if (user.getEmail() == "") {
            email = user.getId();
        } else {
            email = user.getEmail();
        }

        HashMap<String, String> parms1 = new HashMap<>();
        parms1.put("email", email);
        ApiCallBuilder.build(this)
                .setUrl(Constant.BASE_URL + Constant.DELETE)
                .setParam(parms1)
                .execute(new ApiCallBuilder.onResponse() {
                    public void Success(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status");
                            String uid = user.getId();
                        if(status.equalsIgnoreCase("1")){
                            setLog("El usuario confirmo eliminacion de datos");
                            Toast.makeText(mContext, "You Are Logout Succesfully", Toast.LENGTH_SHORT).show();
                            PrefManager.getInstance(getApplicationContext()).logout();
                            NotificationManagerCompat.from(mContext).cancelAll();
                            finish();
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            Animatoo.animateSlideDown(mContext);
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

    @Override
    protected void onRestart() {
        super.onRestart();
        getUserName();
    }

    public void getUserName() {

        user = PrefManager.getInstance(this).getUser();

        acc_nameId.setText(String.valueOf(user.getUsername()));


        Picasso.get().load(user.getImage()).placeholder(R.drawable.profile_img).into(profile_imgId);
        Log.i(TAG, "image=>" + imagePath);


    }

    public void LogoutBtn(View view) {

        String uid = user.getId();


        Toast.makeText(mContext, "You Are Logout Succesfully", Toast.LENGTH_SHORT).show();
        PrefManager.getInstance(getApplicationContext()).logout();
        NotificationManagerCompat.from(mContext).cancelAll();
        finish();
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Animatoo.animateSlideDown(mContext);


    }


    private void LogoutApi(String Uid) {


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(AccountActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        HashMap<String, String> parms = new HashMap<>();
        parms.put("user_id", Uid);


        ApiCallBuilder.build(this)
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "logout_register?")
                .setParam(parms)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {


                        Log.e("selectedresponse=>", "-------->" + response);


                        try {

                            JSONObject object = new JSONObject(response);
                            String status = object.optString("status");
                            String message = object.optString("message");
                            if (status.equals("1")) {
                                progressDialog.dismiss();


                                Toast.makeText(mContext, "You Are Logout " + message, Toast.LENGTH_SHORT).show();
                                PrefManager.getInstance(getApplicationContext()).logout();
                                Intent intent = new Intent(mContext, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                                Animatoo.animateSlideDown(mContext);

                            } else {
                                progressDialog.dismiss();

                                //Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();

                           // Toast.makeText(mContext, "" + e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void Failed(String error) {
                        progressDialog.dismiss();
                      //  Toast.makeText(mContext, "" + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void changePasswordInit(View view) {

        startActivity(new Intent(AccountActivity.this, ChangePasswordActivity.class));

    }

    public void yourFoundationsInit(View view) {

        startActivity(new Intent(AccountActivity.this, YourFoundations.class));
    }

    public void myPaymentInit(View view) {
        startActivity(new Intent(AccountActivity.this, MyPaymentHistoryActivity.class));
        Animatoo.animateSwipeLeft(mContext);


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






