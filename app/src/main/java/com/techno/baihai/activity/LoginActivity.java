package com.techno.baihai.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;

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


        try {

            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LoginActivity.this,
                    new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            String newToken = instanceIdResult.getToken();
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
        login_button = findViewById(R.id.login_button);
        login_button.setReadPermissions("email");
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getUserDetails(loginResult);
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
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
                    "com.tech.baihai",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {

        }


        findViewById(R.id.btn_login).setOnClickListener(this::doLoginIn);


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


                            }


                        } catch (JSONException e) {

                            progressBar.setVisibility(View.GONE);
                            Log.i(TAG, "errorL", e);
                            Toast.makeText(mContext, "Error:" + e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void Failed(String error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(mContext, "" + error, Toast.LENGTH_SHORT).show();
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
                                            result.getString("guide_give_free")
                                    );

                                    PrefManager.getInstance(getApplicationContext()).userLogin(user);
                                    startActivity(new Intent(mContext, HomeActivity.class).
                                            setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                    Intent.FLAG_ACTIVITY_NEW_TASK));
                                    Animatoo.animateSlideLeft(mContext);
                                    finish();

                                } else if (status.equals("0")) {

                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {

                                progressBar.setVisibility(View.GONE);
                                Log.i(TAG, "errorL", e);
                                Toast.makeText(mContext, "Error:" + e, Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void Failed(String error) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(mContext, "" + error, Toast.LENGTH_SHORT).show();
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
                                            user.getEmail(), "", user.getUid());
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                    System.out.println(exception);

                                    Toast.makeText(mContext, "" + exception, Toast.LENGTH_LONG).show();
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
        FacebookSdk.sdkInitialize(getApplicationContext());
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

                            socialLoginApi(username, imageUrl, email, "", socialId);

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

    private void socialLoginApi(String firstName, String imageUrl, String email, String mobile, String socialId) {

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        Call<ResponseBody> call = APIClient.loadInterface().socialLogin
                (firstName, email, "9823145677", socialId, Preference.get(LoginActivity.this, Preference.REGISTER_ID), imageUrl, latitude, longitude);

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
                                        userData.getString("guide_give_free")


                                );
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


}






