package com.techno.baihai.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.widget.Toast;

import com.google.gson.Gson;
import com.techno.baihai.model.User;


public class PrefManager {


    public static final String KEY_DISTANCE = "KEY_DISTANCE";
    //the constants
    private static final String SHARED_PREF_NAME = "pref";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_PASSWORD = "keypassword";
    private static final String KEY_PHONE = "keyphone";
    private static final String KEY_ID = "keyid";
    private static final String KEY_IMAGE = "keyimage";
    private static final String KEY_LEGAL_INFO = "keylegalinfo";
    private static final String KEY_GUIDE = "keyguide";
    private static final String KEY_GUIDE_FREE = "keyguide_free";
    private static final String KEY_GUIDE_GIVE_FREE = "keyguide_give_free";
    private static final String KEY_SUSCRIBE = "key_suscribe";

    public static SharedPreferences sharedPreferences;
    public static String KEY_BaiHai_Status="KEY_BaiHai_Status";
    public static String Key_AmountId="Key_AmountId";
    public static String Key_DonateProductId="Key_DonateProductId";
    public static String Key_FoundatonId="Key_FoundatonId";
    public static String KEY_Fillter="KEY_Fillter";
    private static PrefManager mInstance;
    private static Context mCtx;

    public PrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized PrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PrefManager(context);
        }
        return mInstance;
    }

    public static boolean isNetworkConnected(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo bluetooth = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);
        NetworkInfo wimax = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);

        if (wifi == null && mobile == null && bluetooth == null && wimax == null) {
            return false;
        }

        if (wifi != null && wifi.isConnected()) {
            return true;
        }

        if (mobile != null && mobile.isConnected()) {
            return true;
        }

        if (bluetooth != null && bluetooth.isConnected()) {
            return true;
        }

        return wimax != null && wimax.isConnected();
    }

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            for (NetworkInfo networkInfo : info)
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                } else {
                    isNetworkConnected(context);
                }

        }
        return false;
    }

    public static void showSettingsAlert(Context context) {

        if (mCtx != null) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mCtx);
            alertDialog.setTitle("No Internet Connection");
            alertDialog.setMessage("You don't have internet connection?");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    mCtx.startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    //showSettingsAlert(mCtx);

                }
            });
            alertDialog.show();
        } else {
            Toast.makeText(mCtx, "Please Check Your Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getData(Context context, String key, String value) {
        sharedPreferences = context.getSharedPreferences("bai-haiApp", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, value);
    }

    public static void setString(String name, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name, value);
        editor.apply();
    }

    public static String getString(String name) {
        return sharedPreferences.getString(name, "123");
    }

    public static void setBoolean(String name, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(name, value);
        editor.apply();
    }

    public static boolean getBoolean(String name) {
        return sharedPreferences.getBoolean(name, false);
    }

    public static void setPreferenceObject(String key, Object modal) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonObject = gson.toJson(modal);
        editor.putString(key, jsonObject);
        editor.apply();

    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.
                getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_PASSWORD, user.getPassword());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.putString(KEY_IMAGE, user.getImage());
        editor.putString(KEY_LEGAL_INFO, user.getLegalinfo());
        editor.putString(KEY_GUIDE, user.getGuide());
        editor.putString(KEY_GUIDE_FREE, user.getGuideFree());
        editor.putString(KEY_GUIDE_GIVE_FREE, user.getGuideGiveFree());
        editor.putString(KEY_SUSCRIBE, user.getSuscribe());

        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.
                getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.
                getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return new User(
                sharedPreferences.getString(KEY_ID, "0"),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_PASSWORD, null),
                sharedPreferences.getString(KEY_PHONE, null),
                sharedPreferences.getString(KEY_IMAGE, null),
                sharedPreferences.getString(KEY_LEGAL_INFO, null),
                sharedPreferences.getString(KEY_GUIDE, null),
                sharedPreferences.getString(KEY_GUIDE_FREE, null),
                sharedPreferences.getString(KEY_GUIDE_GIVE_FREE, null),
                sharedPreferences.getString(KEY_SUSCRIBE,null)


        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.
                getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        //mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
    }

    //-----------------------------------

    public static String KEY_IS_LOGIN = "is_login";
    public static String KEY_USER_ID = "id";
    public static String KEY_FIRSTNAME = "first_name";
    public static String KEY_LASTNAME = "last_name";

    public static String KEY_CATEGORY_ID = "category_id";

    public static String REGISTER_ID = "REGISTER_ID";


    public static String lat = "lat";
    public static String lng = "lng";
    public static String address = "address";

    public static String KEY_RECEIVER_ID = "receiver_id";



    public static String KEY_REQUEST_ID = "request_id";
    public static String booking_status = "booking_status";







    public static String GET_VIMEO = "/config";
    public static String KEY_Category_ID = "category_id";
    public static String KEY_SubCategory_ID = "subcategory_id";
    public static String KEY_orderWithIngredents = "orderWithIngredents";
    public static String KEY_ITEM_ID = "itemid";
    public static String KEY_ORderType = "ORderType";
    public static String KEY_IngredentAmt = "IngredentAmt";
    public static String KEY_ProductName = "productName";
    public static String KEY_ProductName_half = "ProductNamehalf";
    public static String KEY_USER_Video_ID = "Video_id";
    public static String KEY_Name= "name";
    public static String KEY_OrderiD= "name";
    public static String KEY_amount = "amount";
    public static String KEY_Address_api= "name";
    public static String KEY_isKeepMe = "isKeepMe";
    public static String KEY_Email = "email";
    public static String KEY_Address = "pic";
    public static String KEY_ProductId = "ProductId";
    public static String KEY_SubProductId = "SubProductId";
    public static String KEY_SubSubProductId = "SubSubProductId";
    public static String KEY_Product_details = "Product_details";
    public static String KEY_CardNumber = "cardnumber";
    public static String KEY_CardNumberNew = "cardnumberNee";
    public static String KEY_ExpiryDate = "card_exprDate";
    public static String KEY_CardHolderName = "card_CardHolderName";
    public static String KEY_Address_Id = "pic1";
    public static String KEY_CategoryId = "category_id";
    public static String KEY_DEsCriptionFinal = "DEsCriptionFinal";

    public static String KEY_Main_CategoryId = "main_category_id";

    public static String KEY_Ordertype= "Ordertype";
    public static String KEY_OrderDay= "OrderDay";
    public static String KEY_OrderTime= "OrderTime";
    public static String KEY_ZipCode = "add";
    public static String key_FILEPATH = "filepath";
    public static String key_IMMAGEURL = "IMAGurL";
    public static String key_Video_URl = "Video_str";
    public static String key_Checked = "check";
    public static String key_Image_path = "check";
    public static String key_PlaceUser_name = "name_place";
    public static String key_PlaceUser_email = "email_place";
    public static String key_PlaceUser_address = "address_place";

    public static String key_Title = "key_Title";
    public static String key_Description = "description";
    public static String key_price = "price";
    public static String key_Currency = "Currency1";
    public static String key_isFromMyvideolist = "isFromMyvideolist";
    public static String key_Category = "Category";
    public static String Key_shipping_charge = "shipping";
    public static String key_stock = "stock";
    public static String ADD_VIEW_COUNT = "addViewCount";


    /// payments Keys
    public static String ADD_PKTest = "pk_test_51I583oC2nlYcvIcBh4uyn1NH6J4zY8rA9UfQnjTIpDSDfAOp00HhYbFYteqA70DOmvJqmvGZpgKGuePcNDL2d2Do00wcDAb4Ch";
    public static String ADD_PKLive = "pk_live_51I583oC2nlYcvIcBkZN20pxW2pEWqAeAjNrwuf8pwE4raX1ig1R2IXrw6WWVai2nDt0cuPfJn2qK8BW61EjBFJIx00fnvZRt6J";
    //sk_test_51I583oC2nlYcvIcBMp0o25rOf8xiDpkjFm9pEUWzwrtLCuPvLRnsIojKIfFGeGomONO7wMVvtqQy4Sb3BzxwE19P00QZ6LCy4p





    public static String get(Context context , String key) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, 0);
        String userId = sharedPreferences.getString(key, "0");
        return userId;
    }
    public static void save(Context context, String key, String value) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static void saveInt(Context context, String key, int value) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(key, value);
        edit.commit();
    }
    public static void saveFloat(Context context, String key, Float value) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putFloat(key, value);
        edit.commit();
    }

    public static int getInt(Context context , String key) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, 0);
        int userId = sharedPreferences.getInt(key,0);
        return userId;
    }

    public static Float getFloat(Context context , String key) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, 0);
        Float userId = sharedPreferences.getFloat(key,0);
        return userId;
    }


    public static boolean saveBool(Context context, String key, Boolean value) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(key, value);
        edit.commit();
        return false;
    }

    public static Boolean getBool(Context context , String key) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, 0);
        return sharedPreferences.getBoolean(key,false);
    }

    public static void clearPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.apply();
    }





}
