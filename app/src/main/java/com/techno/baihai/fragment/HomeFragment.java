package com.techno.baihai.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.squareup.picasso.Picasso;
import com.techno.baihai.R;
import com.techno.baihai.activity.AccountActivity;
import com.techno.baihai.activity.AwardDialog;
import com.techno.baihai.activity.MyProductListActivity;
import com.techno.baihai.activity.RewardPointsActivity;
import com.techno.baihai.activity.SubscribeFoundationActivity;
import com.techno.baihai.activity.StripePaymentActivity;
import com.techno.baihai.api.Constant;
import com.techno.baihai.listner.FragmentListener;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.GPSTracker;
import com.techno.baihai.utils.PrefManager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.*;

import de.hdodenhof.circleimageview.CircleImageView;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;
import www.develpoeramit.mapicall.ApiCallBuilder;

public class HomeFragment extends Fragment {
    BillingClient billingClient;
    Context mContext;
    FragmentListener listener;
    LinearLayout donation, point, id_prize;
    CardView card, card2, card3, card4, card5, donation_2, id_prize2, point2;
    Switch language;
    CircleImageView home_profileId;
    SkuDetails itemInfo;
    TextView home_usernameId,textDonation,textAwards,textCoins,textGive,textGet,textNonProfit,textSuscribe;
    String latitude, longitude, guide;
    private boolean checkReward = true;
    private Boolean isInternetPresent = false;
    private static final String SHOWCASE_ID = "1";
    private String uid;
    private View view;


    public HomeFragment(FragmentListener listener) {
        // Required empty public constructor
        this.listener = listener;

    }

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContext = getActivity();


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        view = inflater.inflate(R.layout.fragment_home, container, false);
        textCoins= view.findViewById(R.id.id_text_coins);
        textDonation= view.findViewById(R.id.id_text_donation);
        textAwards= view.findViewById(R.id.id_text_prize);
        textGet = view.findViewById(R.id.id_text_get);
        textGive = view.findViewById(R.id.id_text_give);
        textNonProfit = view.findViewById(R.id.id_text_non_profit);
        textSuscribe = view.findViewById(R.id.id_text_suscribe);
        home_usernameId = view.findViewById(R.id.home_usernameId);

        home_profileId = view.findViewById(R.id.home_profileId);
        home_profileId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, AccountActivity.class));

            }
        });

        PrefManager.isConnectingToInternet(mContext);
        isInternetPresent = PrefManager.isNetworkConnected(mContext);


        User user = PrefManager.getInstance(mContext).getUser();
        uid = String.valueOf(user.getId());
        guide = String.valueOf(user.getGuide());
        Log.e("user_id: ", uid);


        donation_2 = view.findViewById(R.id.donation_2);
        point2 = view.findViewById(R.id.point2);
        id_prize2 = view.findViewById(R.id.id_prize2);
        donation = view.findViewById(R.id.donation);
        donation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.click(new TabDonateFragment(listener));

            }
        });

        id_prize = view.findViewById(R.id.id_prize);
        id_prize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AwardDialog cdd = new AwardDialog(getActivity());
                cdd.setCancelable(true);
                cdd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                cdd.show();
            }
        });
        point = view.findViewById(R.id.point);
        point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, RewardPointsActivity.class));
            }
        });
        card = view.findViewById(R.id.card);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.click(new DonationFragment(listener));


            }
        });

        card2 = view.findViewById(R.id.card2);
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, MyProductListActivity.class));
            }
        });
        card3 = view.findViewById(R.id.card3);
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getActivity().getPackageManager()
                        .getLaunchIntentForPackage("com.tech.baihaiprovider");
                if (launchIntent != null) {
                    startActivity(launchIntent);
                } else {

                    Toast.makeText(getActivity(), "Provider App Not Installed...!!", Toast.LENGTH_SHORT).show();
                    launchMarket();
                }
            }
        });
        card4 = view.findViewById(R.id.card4);
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, SubscribeFoundationActivity.class));
            }
        });
        card5 = view.findViewById(R.id.card5);
        billingClient = BillingClient.newBuilder(mContext)
                .enablePendingPurchases()
                .setListener(new PurchasesUpdatedListener() {
                    @Override
                    public void onPurchasesUpdated(@NotNull  BillingResult billingResult,@Nullable  List<Purchase> purchases) {
                        // To be implemented in a later section.
                        if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null){
                            for(Purchase purchase: purchases){
                                if(purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged()){
                                    verifyPurchase(purchase);
                                }
                            }
                        }
                    }
                }).build();
        connectToGooglePlayBilling();
        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                billingClient.launchBillingFlow(
                        getActivity(),
                        BillingFlowParams.newBuilder().setSkuDetails(itemInfo).build()
                );
            }
        });
        getUserUpdate();
        /*card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("informacion suscribe: ", uid);
                startActivity(new Intent(mContext, StripePaymentActivity.class));


            }
        });*/
        language = view.findViewById(R.id.switch1);
        language.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if(isChecked){
                    Toast.makeText(mContext, "Select Spanish", Toast.LENGTH_LONG).show();
                    updateResources(mContext, "es");
                    PrefManager.save(mContext, "lang", "es");
                    textAwards.setText("Premios");
                    textCoins.setText("Monedas");
                    textDonation.setText("Donaciones");
                    textGet.setText("REGALAR COSAS GRATIS");
                    textGive.setText("OBTENER COSAS GRATIS");
                    textNonProfit.setText("SUSCRIBE TU FUNDACION");
                    textSuscribe.setText("SUSCRIBETE");


                }else{
                    Toast.makeText(mContext, "Select English", Toast.LENGTH_LONG).show();
                    updateResources(mContext, "en");
                    PrefManager.save(mContext, "lang", "en");
                    textAwards.setText("Awards");
                    textCoins.setText("Coins");
                    textDonation.setText("Donations");
                    textGet.setText("GIVE FREE STUFF");
                    textGive.setText("GET FREE STUFF");
                    textNonProfit.setText("SUBSCRIBE A NON PROFIT");
                    textSuscribe.setText("Subscribe");
                }
            }
        });
        getUserUpdate();
        if (isInternetPresent) {
        } else {
            PrefManager prefManager = new PrefManager(mContext);
            PrefManager.showSettingsAlert(mContext);
        }

        getCurrentLocation();

        return view;
    }

    private static void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
    private void verifyPurchase(Purchase purchase){
        String requestUrl ="";
        String purchaseToken = purchase.getPurchaseToken();
        String purchaseTime = Long.toString(purchase.getPurchaseTime());
        String purchaseOrderId = purchase.getOrderId();
        String purchaseValid = Integer.toString(purchase.getPurchaseState());
        User user = PrefManager.getInstance(getActivity()).getUser();
        String id = user.getId();

/*
        HashMap<String, String> parms1 = new HashMap<>();
        parms1.put("user_id", id);
        parms1.put("purchase_token", purchaseToken);
        parms1.put("purchase_time", purchaseTime);
        parms1.put("purchase_order_id", purchaseOrderId);
        parms1.put("purchase_valid", purchaseValid);
        ApiCallBuilder.build(mContext)
                .setUrl(Constant.BASE_URL + Constant.PAY)
                .setParam(parms1)
                .execute(new ApiCallBuilder.onResponse() {
                    public void Success(String response) {
                        try {
                            Log.e("selectedresponse=>", "-------->" + response);
                            JSONObject object = new JSONObject(response);
                            String isValid = object.getString("isValid");
                            if(isValid.equals("true")){
                                AcknowledgePurchaseParams acknowledgePurchaseParams
                                        = AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();
                                billingClient.acknowledgePurchase(
                                        acknowledgePurchaseParams,
                                        new AcknowledgePurchaseResponseListener() {
                                            @Override
                                            public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
                                                if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK){
                                                    Toast.makeText(mContext,"ACKNOLOWGED",Toast.LENGTH_LONG).show();

                                                }
                                            }
                                        }
                                );
                            }



                        } catch (JSONException e) {


                            Toast.makeText(mContext, "Error:" + e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }

                    public void Failed(String error) {
                    }
                });*/
        AcknowledgePurchaseParams acknowledgePurchaseParams
                = AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();
        billingClient.acknowledgePurchase(
                acknowledgePurchaseParams,
                new AcknowledgePurchaseResponseListener() {
                    @Override
                    public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
                        if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK){
                            Toast.makeText(mContext,"ACKNOLOWGED",Toast.LENGTH_LONG).show();

                        }
                    }
                }
        );

    }
    private void connectToGooglePlayBilling(){
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NotNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    getProductDetails();
                }else{}
            }

            @Override
            public void onBillingServiceDisconnected() {
                connectToGooglePlayBilling();
            }
        });
    }

    private void getProductDetails(){
        List<String> productsIds = new ArrayList<>();
        productsIds.add("14599");
        SkuDetailsParams getProductsDetailsQuery = SkuDetailsParams
                .newBuilder()
                .setType(BillingClient.SkuType.SUBS)
                .setSkusList(productsIds)
                .build();
        Activity activity= getActivity();
        billingClient.querySkuDetailsAsync(
                getProductsDetailsQuery,
                new  SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @androidx.annotation.Nullable List<SkuDetails> list) {
                        if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null){
                             itemInfo = list.get(0);

                        }
                    }
                }
        );
    }
    @Override
    public void onStart() {
        super.onStart();
        getUserUpdate();
        User user = PrefManager.getInstance(getActivity()).getUser();
        if (!user.getGuide().equals("1")) {
            ShowIntro(getResources().getString(R.string.guide_give_free_stuff), getResources().getString(R.string.guide_give_free_stuff_1), card, 1);

        }

    }

    private void GetLevelsApi(String Uid) {


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        HashMap<String, String> parms = new HashMap<>();
        parms.put("user_id", uid);


        ApiCallBuilder.build(mContext)
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "get_user_level?")
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


                                //Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();

                                //   https://www.shipit.ng/BaiHai/webservice/get_profile?user_id=1

                                JSONObject result = object.optJSONObject("result");
                                //String id = result.optString("id");
                                //Log.e("resultt=>", "" + response);
                                String awardId = "null";
                                String awardName = "null";
                                String awardMinCoin = "null";
                                String awardMaxCoin = "null";
                                String awardImage = "null";
                                String awardMessage = "null";

                                if (result != null) {
                                    awardId = result.optString("id");
                                    awardName = result.optString("name");
                                    awardMinCoin = result.optString("min_coin");
                                    awardMaxCoin = result.optString("max_coin");
                                    awardImage = result.optString("image");
                                    awardMessage = result.optString("message");

                                    checkReward = PrefManager.getBool(mContext, "showDailog");

                                    if (checkReward == true && awardMinCoin.equals(50)) {

                                        PrefManager.setBoolean("showDailog", false);


                                        AwardDialog cdd = new AwardDialog(getActivity());
                                        cdd.setCancelable(false);
                                        cdd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                        cdd.show();
                                    } else {
                                        Toast.makeText(mContext, "Share a App & get a new rewards..!!", Toast.LENGTH_SHORT).show();
                                    }


                                }


                            } else {
                                progressDialog.dismiss();

                                Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();

                            Toast.makeText(mContext, "" + e, Toast.LENGTH_SHORT).show();
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


    private void launchMarket() {
        Uri uri = Uri.parse("http://play.google.com/store/apps/details?id=" + "com.tech.mrnle");
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), " unable to find market app", Toast.LENGTH_LONG).show();
        }


    }


    private void ShowIntro(String title, String text, CardView viewId, final int type) {

        new GuideView.Builder(mContext)
                .setTitle(title)
                .setContentText(text)
                .setContentTextSize(12)//optional
                .setTitleTextSize(14)//optional
                .setDismissType(DismissType.anywhere) //optional - default dismissible by TargetView
                .setTargetView(viewId)
                .setGuideListener(new GuideListener() {
                    @Override
                    public void onDismiss(View view) {
                         if (type == 1) {
                            ShowIntro(getResources().getString(R.string.guide_get_free_stuff), getResources().getString(R.string.guide_get_free_stuff_1), card2, 6);
                        } else if (type == 6) {
                            ShowIntro(getResources().getString(R.string.guide_non_profit), getResources().getString(R.string.guide_non_profit1), card4, 5);
                        } else if (type == 5) {
                            ShowIntro(getResources().getString(R.string.guide_suscribe), getResources().getString(R.string.guide_suscribe1), card5, 4);
                        } else if (type == 4) {
                            ShowIntro(getResources().getString(R.string.guide_donation), getResources().getString(R.string.guide_donation1), donation_2, 3);
                        } else if (type == 3) {
                            ShowIntro(getResources().getString(R.string.guide_awards), getResources().getString(R.string.guide_awards1), id_prize2, 2);
                        } else if (type == 2) {
                            ShowIntro(getResources().getString(R.string.guide_coins), getResources().getString(R.string.guide_coins1), point2, 7);
                        } else if (type == 7) {
                            setGuide();
                            SharedPreferences.Editor sharedPreferencesEditor = mContext.getSharedPreferences("show_case_pref",
                                    Context.MODE_PRIVATE).edit();
                            sharedPreferencesEditor.putBoolean("showcase", false);
                        }
                    }
                })
                .build()
                .show();
    }

    @Override
    public void onResume() {
        getUserUpdate();
        getCurrentLocation();
        super.onResume();

    }


    public void getUserUpdate() {

        User user = PrefManager.getInstance(getActivity()).getUser();

        home_usernameId.setText(String.valueOf(user.getUsername()));


        Picasso.get().load(user.getImage()).placeholder(R.drawable.profile_img).into(home_profileId);
        // Log.i(TAG,"image=>"+imagePath);


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

    private void setGuide() {
        User user = PrefManager.getInstance(getActivity()).getUser();
        String id = null;
        if (user.getId() == "") {
            id = user.getId();
        } else {
            id = user.getId();
        }

        HashMap<String, String> parms1 = new HashMap<>();
        parms1.put("user_id", id);
        parms1.put("activity", "0");
        ApiCallBuilder.build(mContext)
                .setUrl(Constant.BASE_URL + Constant.GUIDE)
                .setParam(parms1)
                .execute(new ApiCallBuilder.onResponse() {
                    public void Success(String response) {
                        try {
                            Log.e("selectedresponse=>", "-------->" + response);
                            JSONObject object = new JSONObject(response);
                            String message = object.getString("message");
                            User user2 = new User(
                                    user.getId(),
                                    user.getUsername(),
                                    user.getEmail(),
                                    user.getPassword(),
                                    user.getPhone(),
                                    user.getImage(),
                                    user.getLegalinfo(),
                                    "1",
                                    user.getGuideFree(),
                                    user.getGuideGiveFree()
                            );

                            PrefManager.getInstance(getActivity()).userLogin(user2);


                        } catch (JSONException e) {


                            Toast.makeText(mContext, "Error:" + e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }

                    public void Failed(String error) {
                    }
                });
    }


}
