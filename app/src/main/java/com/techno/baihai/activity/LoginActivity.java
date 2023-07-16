package com.techno.baihai.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;
import com.marozzi.roundbutton.RoundButton;
import com.techno.baihai.R;
import com.techno.baihai.api.APIClient;
import com.techno.baihai.api.Constant;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.AlertConnection;
import com.techno.baihai.utils.CustomSnakbar;
import com.techno.baihai.utils.GPSTracker;
import com.techno.baihai.utils.PrefManager;
import com.techno.baihai.utils.Preference;
import com.techno.baihai.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.develpoeramit.mapicall.ApiCallBuilder;

import static com.techno.baihai.utils.PrefManager.getData;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    static final int GOOGLE_SIGN_IN_REQUEST_CODE = 1234;
    private static final String TAG = LoginActivity.class.getSimpleName();
    LoginButton login_button;
    RelativeLayout rl_facebook, rl_google;
    FirebaseUser user;
    String latitude, longitude;
    String regID;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private EditText et_Email;
    private EditText et_Password;
    private final Context mContext = this;
    private ProgressBar progressBar;
    private Boolean isInternetPresent = false;
    private RadioGroup radioGroup;
    private RadioButton spanish_btn;
    private RadioButton english_btn;
    private boolean bandera = false;
    private BottomNavigationView navigationView;
    private String bandera2;
    private WebView webView;
    Context context;
    Resources resources;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        if (PrefManager.getInstance(this).isLoggedIn()) {

            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
        facebookSDKInitialize();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        setContentView(R.layout.activity_login);


        FirebaseAuth.getInstance().signOut();


        if (LoginManager.getInstance() != null) {

            LoginManager.getInstance().logOut();
        }


        PrefManager.isConnectingToInternet(this);
        isInternetPresent = PrefManager.isNetworkConnected(this);

        //if the user is already logged in we will directly start the profile activity


        et_Email = findViewById(R.id.et_email);
        et_Password = findViewById(R.id.et_password);
        progressBar = findViewById(R.id.progressBar);


        regID = getData(getApplicationContext(), "regId", null);

        getCurrentLocation();
        deleteCache(mContext);
        try {



            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(LoginActivity.this,
                    new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            String newToken = s;
                            Log.e("newToken=>", newToken);
                            Preference.save(LoginActivity.this, Preference.REGISTER_ID, newToken);

                        }
                    });

        } catch (Exception e) {

            e.printStackTrace();
            Log.e("cxc=>", e.getMessage());

        }


        mAuth = FirebaseAuth.getInstance();

// Configure Google Sign In

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(LoginActivity.this.getResources().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


// Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        rl_google = findViewById(R.id.rl_google);
        rl_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE);
            }
        });


        callbackManager = CallbackManager.Factory.create();


        rl_facebook = findViewById(R.id.rl_facbook);
        login_button =(LoginButton) findViewById(R.id.login_button);
        //login_button.setReadPermissions("email", "public_profile");
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess");
                getUserDetails(loginResult);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e("Error_facebook=>", "error" + exception.getMessage());
            }
        });
        rl_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_button.performClick();
            }
        });

        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.techno.baihai",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {

        }


        findViewById(R.id.btn_login).setOnClickListener(this::doLoginIn);
        String lang = PrefManager.get(mContext, "lang");
        Log.e("lang", lang);
        bandera2 = getIntent().getStringExtra("bandera");

        if(bandera2!=null){
            bandera=true;
        }
        if(bandera==false){

            final AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            View mView = getLayoutInflater().inflate(R.layout.language_alert, null);

            CardView btn_okay = mView.findViewById(R.id.btn_okay);

            english_btn = mView.findViewById(R.id.english_btn);
            spanish_btn = mView.findViewById(R.id.spanish_btn);
            radioGroup = mView.findViewById(R.id.radio);


            alert.setView(mView);
            final AlertDialog alertDialog1 = alert.create();
            alertDialog1.dismiss();
            alertDialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            alertDialog1.setCanceledOnTouchOutside(false);


            String lang2 = PrefManager.get(mContext, "lang");
            Log.e("lang", lang);

            if (lang2.equals("es") && lang != null) {
                PrefManager.save(mContext, "lang", "es");
                updateResources(mContext, "es");
                english_btn.setChecked(false);
                spanish_btn.setChecked(true);
                context = LocaleHelper.setLocale(mContext, "es");
                resources = context.getResources();
            } else {
                PrefManager.save(mContext, "lang", "en");
                english_btn.setChecked(true);
                spanish_btn.setChecked(false);
                updateResources(mContext, "en");
                context = LocaleHelper.setLocale(mContext, "en");
                resources = context.getResources();

            }


            english_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Toast.makeText(mContext, "Select English", Toast.LENGTH_LONG).show();
                    PrefManager.save(mContext, "lang", "en");
                    updateResources(mContext, "en");
                    context = LocaleHelper.setLocale(mContext, "en");
                    resources = context.getResources();
                    english_btn.setChecked(true);
                    spanish_btn.setChecked(false);
                }
            });
            spanish_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Toast.makeText(mContext, "Select Spanish", Toast.LENGTH_LONG).show();;
                    PrefManager.save(mContext, "lang", "es");
                    updateResources(mContext, "es");
                    context = LocaleHelper.setLocale(mContext, "es");
                    resources = context.getResources();
                    english_btn.setChecked(false);
                    spanish_btn.setChecked(true);
                }
            });


            btn_okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog1.dismiss();
                    bandera=true;
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    intent.putExtra("bandera", "true");
                    startActivity(intent);
                    finish();

                }
            });


        }

        LegalInfo();

    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);

        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public void doLoginIn(View view) {

        if (isInternetPresent) {
            userLoginIn(view);
        } else {
            PrefManager prefManager = new PrefManager(mContext);
            PrefManager.showSettingsAlert(mContext);
        }
    }


    private void getCurrentLocation() {

        GPSTracker track = new GPSTracker(LoginActivity.this);
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

    private void sendNotification() {
        User user = PrefManager.getInstance(this).getUser();
        progressBar.setVisibility(View.VISIBLE);
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


                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status");
                            String message = object.getString("message");

                            Log.e(TAG, "STATUS_:" + status);

                            if (status.equals("1")) {
                                progressBar.setVisibility(View.GONE);

                                Toast.makeText(mContext, "Notification sended",
                                        Toast.LENGTH_SHORT).show();
                                setLog("Envio notificacion de donacion");


                            }


                        } catch (JSONException e) {

                            progressBar.setVisibility(View.GONE);
                            Log.i(TAG, "errorL", e);
                           // Toast.makeText(mContext, "Error:" + e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void Failed(String error) {
                        progressBar.setVisibility(View.GONE);
                        //Toast.makeText(mContext, "" + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void userLoginIn(View view) {

        final String email = et_Email.getText().toString();
        final String pass = et_Password.getText().toString();

        //validating inputs
        if (email.equalsIgnoreCase("")) {
            CustomSnakbar.showSnakabar(mContext, view, "Please Enter Email!");
            et_Email.requestFocus();
        } else if (pass.equalsIgnoreCase("")) {
            CustomSnakbar.showSnakabar(mContext, view, "Please Enter Password!");
            et_Password.requestFocus();
        } else {


            progressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> parms = new HashMap<>();
            parms.put("email", email);
            parms.put("password", pass);
            parms.put("type", "User");
            parms.put("register_id", Preference.get(LoginActivity.this, Preference.REGISTER_ID));
            parms.put("lat", "" + latitude);
            parms.put("lon", "" + longitude);
            ApiCallBuilder.build(this)
                    .isShowProgressBar(false)
                    .setUrl(Constant.BASE_URL + Constant.LOGIN)
                    .setParam(parms)
//                    .setFile("image", "file_path")
                    .execute(new ApiCallBuilder.onResponse() {
                        @Override
                        public void Success(String response) {
                            Log.d(TAG, "respoLogin:" + response);


                            try {
                                JSONObject object = new JSONObject(response);
                                String status = object.getString("status");
                                String message = object.getString("message");

                                Log.e(TAG, "STATUS_:" + status);
                                Log.e(TAG, "MESSAGE_:" + message);

                                if (status.equals("1")) {
                                    progressBar.setVisibility(View.GONE);

                                    Toast.makeText(mContext, "Login Sucessfull",
                                            Toast.LENGTH_SHORT).show();

                                    JSONObject result = object.getJSONObject("result");

                                    User user = new User(
                                            result.getString("id"),
                                            result.getString("name"),
                                            result.getString("email"),
                                            result.getString("password"),
                                            result.getString("mobile"),
                                            result.getString("image"),
                                            result.getString("legal_info"),
                                            result.getString("guide"),
                                            result.getString("guide_free"),
                                            result.getString("guide_give_free"),
                                            result.getString("suscribe")
                                    );
                                    setLog("Login de usuario "+result.getString("name")+"");
                                    PrefManager.getInstance(getApplicationContext()).userLogin(user);
                                    startActivity(new Intent(mContext, HomeActivity.class).
                                            setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                    Intent.FLAG_ACTIVITY_NEW_TASK));
                                    Animatoo.animateSlideLeft(mContext);
                                    finish();

                                } else if (status.equals("0")) {

                                    progressBar.setVisibility(View.GONE);
                                    //Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {

                                progressBar.setVisibility(View.GONE);
                                Log.i(TAG, "errorL", e);
                                //Toast.makeText(mContext, "Error:" + e, Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void Failed(String error) {
                            progressBar.setVisibility(View.GONE);
                            //Toast.makeText(mContext, "" + error, Toast.LENGTH_SHORT).show();
                        }
                    });


        }


    }


    public void RegisterUserBtn(View view) {

        finish();
        startActivity(new Intent(mContext, SignUpActivity.class));
        Animatoo.animateSwipeRight(mContext);
    }


    public void ForgotCallInit(View view) {

        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.setContentView(R.layout.forgot_password_dialog);

        final EditText forgot_email = dialog.findViewById(R.id.forgot_email);
        Button btn_forgot_submit = dialog.findViewById(R.id.btn_forgot_submit);

        btn_forgot_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternetPresent) {

                    if (forgot_email.getText().toString().trim().equalsIgnoreCase("")) {
                        forgot_email.setError("Can't Be Empty");
                        forgot_email.requestFocus();
                    } else {
                        String f_email = forgot_email.getText().toString().trim();
                        Forgot_call(f_email, view, dialog);
                    }

                } else {
                    AlertConnection.showAlertDialog(LoginActivity.this, "No Internet Connection",
                            "You don't have internet connection.", false);
                }
            }
        });
        dialog.show();
    }


    private void Forgot_call(String f_email, final View view, final Dialog dialog) {
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, String> parms = new HashMap<>();
        parms.put("email", f_email);
        ApiCallBuilder.build(this)
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "forgot_password?")
                .setParam(parms)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("Response=>", "" + response);
                        try {

                            JSONObject object = new JSONObject(response);
                            String status = object.optString("status");
                            String message = object.optString("message");
                            if (status.equals("1")) {
                                CustomSnakbar.showDarkSnakabar(mContext, view, "Change Password Link Sent to Your Email Successfully!");

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                    }
                                }, 1500);
                            } else {
                                CustomSnakbar.showDarkSnakabar(mContext, view, "Check Your Connection: " + message);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(mContext, "Check Your Network: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void Failed(String error) {
                        progressBar.setVisibility(View.GONE);
                        CustomSnakbar.showDarkSnakabar(mContext, view, "check your network: " + error);
                    }
                });

    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        user = mAuth.getCurrentUser();
        //  updateUI(currentUser);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        signOut();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCurrentLocation();
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
// ...
                        task.getResult();
                    }
                });
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
// Sign in success, update UI with the signed-in user's information

                            user = mAuth.getCurrentUser();

                            if (user != null) {

                                try {
                                    socialLoginApi(user.getDisplayName(), String.valueOf(user.getPhotoUrl()),
                                            user.getEmail(), "", user.getUid(),"google");
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                    System.out.println(exception);

                                    //Toast.makeText(mContext, "" + exception, Toast.LENGTH_LONG).show();
                                }

                            }

                        } else {
// If sign in fails, display a message to the user.
                            Toast.makeText(mContext, "Authentication Failed.", Toast.LENGTH_SHORT).show();

                        }

// ...
                    }
                });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


// Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
// The Task returned from this call is always completed, no need to attach
// a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
// Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
// Google Sign In failed, update UI appropriately
                Log.e("xxx", String.valueOf(e));
                //Toast.makeText(this, "SHA-1 "+e, Toast.LENGTH_LONG).show();
// ...
            }
        } else if (requestCode == 64206) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }


    //**********************Facebook Integration******************************************
    protected void facebookSDKInitialize() {
        callbackManager = CallbackManager.Factory.create();
    }

    protected void getUserDetails(LoginResult loginResult) {
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject response,
                            GraphResponse response1) {
                        try {
                            Log.e("fbRespo", String.valueOf(response));
                            final String socialId = (response.getString("id"));
                            String email = null;
                            final String username = (response.get("name").toString());
                            JSONObject profile_pic_data = new JSONObject(response.get("picture").toString());
                            JSONObject profile_pic_url = new JSONObject(profile_pic_data.getString("data"));
                            final String imageUrl = profile_pic_url.getString("url");

                            Log.e("imageUrl---->", "" + imageUrl);
                            try {
                                email = (response.get("email").toString());


                            } catch (JSONException e) {
                                e.printStackTrace();

                            }

                            Log.e("socialId---->", "" + socialId);
                            Log.e("username---->", "" + username);
                            Log.e("email---->", "" + email);

                            socialLoginApi(username, imageUrl, email, "", socialId,"facebook");

                        } catch (Exception e) {

                            e.printStackTrace();

                        }
                    }

                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email, picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();

    }

    private void socialLoginApi(String firstName, String imageUrl, String email, String mobile, String socialId,String typered) {

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        Call<ResponseBody> call = APIClient.loadInterface().socialLogin
                (firstName, email, "0", socialId, Preference.get(LoginActivity.this, Preference.REGISTER_ID), imageUrl, latitude, longitude);

        // (firstName,lastName,email,socialId,PrefManager.getString(Constant.REGISTER_ID),"","","");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.optString("status").equals("1")) {
                            JSONObject userData = object.optJSONObject("result");
                            Log.e("responseData", responseData);

                            User user = null;
                            if (userData != null) {
                                user = new User(
                                        userData.getString("id"),
                                        userData.getString("name"),
                                        userData.getString("password"),
                                        userData.getString("email"),
                                        userData.getString("mobile"),
                                        userData.getString("image"),
                                        userData.getString("legal_info"),
                                        userData.getString("guide"),
                                        userData.getString("guide_free"),
                                        userData.getString("guide_give_free"),
                                        userData.getString("suscribe")


                                );
                                setLog("El usuario ingreso por una red social "+typered+""+userData.getString("name")+"");
                            }

                            PrefManager.getInstance(getApplicationContext()).userLogin(user);
                            progressDialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        } else {
                            progressDialog.dismiss();
                            Utils.showToast(object.optString("message"));
                        }

                    }
                } catch (IOException | JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                t.printStackTrace();
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

    public void LegalInfo() {
        User user = PrefManager.getInstance(this).getUser();

            String langua = "Yes";
            String langua1 = "No";
            String lang = PrefManager.get(mContext, "lang");
        PrefManager.save(mContext, "legal", "0");
            setLog("El usuario  se encuentra en idioma ingles");
            if (lang.equals("es") && lang != null) {
                langua = "Si";
                langua1 = "No";
                setLog("El usuario  se encuentra en idioma español");
            }
        final AlertDialog.Builder alert1 = new AlertDialog.Builder(mContext);

            alert1.setCancelable(false);
            alert1.setPositiveButton(langua, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                PrefManager.save(mContext, "legal", "1");
                GetTutorialResultApi();

            }

        });

        alert1.setNegativeButton(langua1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                                GetTutorialResultApi();
                            }
                        });
             AlertDialog alertDialogInfo = alert1.create();
             alertDialogInfo.dismiss();
            // Configura el titulo.
        alertDialogInfo.setTitle(getResources().getString(R.string.legal_information));

            // Configura el mensaje.
        alertDialogInfo
                    .setMessage(getResources().getString(R.string.legal_mesagge));
        alertDialogInfo.show();





    }

    private void GetTutorialResultApi() {


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        String langua = "EN";
        String tutorial_id = "2";
        String lang = PrefManager.get(mContext, "lang");
        if (lang.equals("es") && lang != null) {
            langua = "ES";
            tutorial_id = "1";
        }


        HashMap<String, String> parms = new HashMap<>();
        parms.put("tutorial_id", tutorial_id);
        parms.put("language", langua);

        ApiCallBuilder.build(this)
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "get_tutorial?")
                .setParam(parms)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        progressDialog.dismiss();

                        Log.e("tutorial_response", String.valueOf(response));


                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status");
                            Log.d(TAG, "STATUS_:" + status);
                            try {


                                if (status.equals("1")) {
                                    String url = "";
                                    JSONArray result = object.optJSONArray("result");
                                    if (result != null) {
                                        for (int i = 0; i < result.length(); i++) {
                                            JSONObject object1 = result.getJSONObject(i);
                                            url = object1.getString("url");
                                            Log.d("url", url);
                                        }
                                    }
                                    url="https://www.youtube.com/embed/sjO9uCQ8Gj8";
                                    final Dialog dialog = new Dialog(mContext);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setCancelable(false);
                                    dialog.setContentView(R.layout.dialog_tutorials);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


                                    final RoundButton bt = (RoundButton) dialog.findViewById(R.id.bt_success);
                                    bt.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            bt.startAnimation();
                                            bt.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    bt.setResultState(RoundButton.ResultState.SUCCESS);

                                                }
                                            }, 1000);
                                            bt.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    dialog.dismiss();

                                                    // bt.revertAnimation();
                                                }
                                            }, 2000);

                                        }
                                    });
                                    String youTubeUrl = url;
                                    String frameVideo = "<html><body>Tutorials<br><iframe width=\"280\" height=\"300\" " +
                                            "src='" + youTubeUrl + "' frameborder=\"0\" allowfullscreen>" +
                                            "</iframe></body></html>";
                                    webView = dialog.findViewById(R.id.webViewVideo);
                                    String regexYoutUbe = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+";
                                    if (youTubeUrl.matches(regexYoutUbe)) {
                                        webView.setWebViewClient(new WebViewClient() {
                                            @Override
                                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                                return false;
                                            }
                                        });
                                        WebSettings webSettings = webView.getSettings();
                                        webSettings.setJavaScriptEnabled(true);
                                        webView.loadData(frameVideo, "text/html", "utf-8");
                                    } else {
                                        Toast.makeText(LoginActivity.this, "This is other video",
                                                Toast.LENGTH_SHORT).show();
                                    }


                                    dialog.show();


                                } else if (status.equals("0")) {

                                    progressDialog.dismiss();
                                }
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }


                        } catch (JSONException e) {

                            progressDialog.dismiss();
                            Log.i(TAG, "errorL", e);
                            //Toast.makeText(mContext, "Check Your Network "+e, Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void Failed(String error) {
                        progressDialog.dismiss();
                        //Toast.makeText(mContext, "Check Your Connection " + error, Toast.LENGTH_LONG).show();
                    }
                });
    }
    @Override
    protected void onDestroy() {

        super.onDestroy();
    }




}






