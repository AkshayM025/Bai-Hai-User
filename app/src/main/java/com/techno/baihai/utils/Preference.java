package com.techno.baihai.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Preference {

    public static final String APP_PREF = "Bai-Hai-Preferences";

    public static SharedPreferences sp;
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
    public static String KEY_Name = "name";
    public static String KEY_OrderiD = "name";
    public static String KEY_amount = "amount";
    public static String KEY_Address_api = "name";
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

    public static String KEY_Ordertype = "Ordertype";
    public static String KEY_OrderDay = "OrderDay";
    public static String KEY_OrderTime = "OrderTime";
    public static String KEY_ZipCode = "add";
    public static String key_FILEPATH = "filepath";
    public static String key_IMMAGEURL = "IMAGurL";
    public static String key_Video_URl = "Video_str";
    public static String key_Checked = "check";
    public static String key_Image_path = "check";
    public static String key_PlaceUser_name = "name_place";
    public static String key_PlaceUser_email = "email_place";
    public static String key_PlaceUser_address = "address_place";
    public static String key_stock = "stock";
    public static String ADD_VIEW_COUNT = "addViewCount";

    //------------------------------------------------------------------------------------------------------------------------

    public static String KEY_getSellerId = "getSellerId";
    public static String KEY_getProductId = "getProductId";
    public static String KEY_getProductCategoryId = "getProductCategoryId";
    public static String KEY_getProductName = "getProductName";
    public static String KEY_getProductImageUrl = "getProductImageUrl";
    public static String KEY_getProductDecrip = "getProductDecrip";
    public static String KEY_getProductAddress = "getProductAddress";


    private final Activity activity;
    private final Context context;

    public Preference(Activity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }

    //-----------------------------------

    public static String get(Context context, String key) {
        sp = context.getSharedPreferences(APP_PREF, 0);
        String userId = sp.getString(key, "0");
        return userId;
    }

    public static void save(Context context, String key, String value) {
        sp = context.getSharedPreferences(APP_PREF, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static void saveInt(Context context, String key, int value) {
        sp = context.getSharedPreferences(APP_PREF, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static void saveFloat(Context context, String key, Float value) {
        sp = context.getSharedPreferences(APP_PREF, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putFloat(key, value);
        edit.commit();
    }

    public static int getInt(Context context, String key) {
        sp = context.getSharedPreferences(APP_PREF, 0);
        int userId = sp.getInt(key, 0);
        return userId;
    }

    public static Float getFloat(Context context, String key) {
        sp = context.getSharedPreferences(APP_PREF, 0);
        Float userId = sp.getFloat(key, 0);
        return userId;
    }

    public static boolean saveBool(Context context, String key, Boolean value) {
        sp = context.getSharedPreferences(APP_PREF, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
        return false;
    }

    public static Boolean getBool(Context context, String key) {
        sp = context.getSharedPreferences(APP_PREF, 0);
        return sp.getBoolean(key, false);
    }

    public static void clearPreference(Context context) {
        sp = context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        edit.apply();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null;
    }
}
