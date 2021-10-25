package com.techno.baihai.api;

import android.Manifest;

public class Constant {

    public static final String BASE_URL = "http://bai-hai.com/webservice/";

    public static final String LOGIN = "login?";
    public static final String POST_CREATE_AN_USER = "signup?";
    public static final String RAGISTER_ORGANIZATION = "register_organization?";
    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";
    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static String SHARED_PREFERENCES_NAME = "Bai-Hai";
    public static String USER_ID = "USER_ID";
    public static String REGISTER_ID = "REGISTER_ID";
    public static String IS_LOGIN = "IS_LOGIN";
    public static String RECEIVER_ID = "RECEIVER_ID";
    public static String USER_DATA = "USER_DATA";


    //The App constant fields...............................................................................................
    public static String screen_name_list = "screen_name_list";
    public static String sub_screen_name_list = "sub_screen_name_list";
    public static String get_user_list = "get_user_list";
    public static String get_chat = "get_chat";
    public static String insert_chat = "insert_chat";
    int RC_SIGN_IN = 999;
    int SUCCESS = 1;
    int INVALID = -1;
    int FAILURE = 0;
    int UNAUTHORISED = 3;
    String KEY_EXTRA_TITLE_TEXT = "extra_title_text";
    int READ_TIMEOUT = 60;
    int CONNECT_TIMEOUT = READ_TIMEOUT;
    String STORE_SEARCH_RANGE_KMS = "20";
    String LOGIN_TYPE_FACEBOOK = "F";
    //String APP_VERSION = "10";
    String DEVICE_TYPE = "A";
    int PLACE_PICKER_REQUEST = 2004;
    int REQUEST_CAMERA_AND_GALLERY = 1;
    int REQUEST_TASK_DETAIL = 2001;
    int REQUEST_RESCHUDLE_TASK = 2008;
    int REQUEST_GALLERY = 101;
    int REQUEST_IS_CONTINUE_BOOKING = 108;
    int REQUEST_COUPON_LIST = 20356;
    String[] PERMISSIONS_GALLERY_AND_CAMERA = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    String[] REQUIRED_PERMISSIONS_FACEBOOK = {"email", "public_profile"};
    String FACEBOOK_REQUEST_PARAMS = "name,email";
    int MAX_IMAGES_TO_SHOW_IN_TED_PICKER = 10000;
    int INITIAL_QUANTITY = 0;
    int CATOGORIES_TO_DISPLAY_AT_HOME_SCREEN = 3;
    String YES = "Y";
    String NO = "N";
    String ONE = "1";
    String CART_TYPE_SELF = "M";
    String CART_TYPE_FAMILY = "F";
    String EXTRA_KEY_BRAND = "key_brand";
    String EXTRA_KEY_SUB_CATEGORY = "extra_sub_category";
    String EXTRA_KEY_CATEGORY = "key_category";
    //    String PREF_KEY_IS_STORE_SELECTED = "pref_is_store_selected";
    String EXTRA_KEY_SUB_CATEGORY_DETAIL = "key_sub_category_detail";
    String EXTRA_KEY_PRODUCT = "key_product";
    String EXTRA_KEY_PRODUCTS = "key_products";
    String EXTRA_KEY_TITLE = "key_title";
    String PREF_KEY_AUTH_KEY = "pref_auth_key";
    String KEY_FB_NAME = "fbname";
    String KEY_FB_IMAGE = "fbimage";
    String KEY_FB_ID = "fbid";
    String KEY_FB_EMAIL = "fb_email";
    String KEY_EXTRA_CATEGORY = "key_extra_category";
    String KEY_EXTRA_CATEGORY_ID = "key_extra_category_id";
    String KEY_EXTRA_MAIN_CATEGORY_ID = "key_extra_main_category_id";
    String KEY_EXTRA_USER = "extra_user";
    String KEY_EXTRA_SERVICE_DETAILS = "extra_service_details";
    String KEY_EXTRA_SELECTED_TASK = "extra_selected_task";
    String KEY_EXTRA_EXPECTED_HOUR = "extra_expected_hour";
    String KEY_EXTRA_RECURING_LABLE = "extra_recuring_lable";
    String KEY_EXTRA_RECURING_TYPE = "extra_recuring_type";
    String KEY_EXTRA_RECURING_AMOUNT = "extra_recuring_amount";
    String KEY_EXTRA_PRICE = "extra_price";
    String KEY_EXTRA_DATE = "extra_date";
    String KEY_EXTRA_TIME = "extra_time";
    String KEY_EXTRA_BOOKING_REQUEST = "extra_key_booking_request";
    String KEY_EXTRA_SELECTED_SERVICE = "extra_selected_service";
    String KEY_EXTRA_TASK = "extra_key_task";
    String KEY_EXTRA_REQUEST_RESPONSE = "extra_key_request_response";
    String KEY_EXTRA_RESPONSE = "extra_key_response";
    String KEY_EXTRA_IS_INSTANCE_SERVICE = "extra_key_is_instant_service";
    String KEY_EXTRA_IS_HIRE_BOOKING = "extra_key_is_hire_booking";
    String KEY_EXTRA_BOOKING_ID = "extra_key_bookingId";
    String KEY_EXTRA_PROVIDER_ID = "extra_key_provider_id";
    String KEY_EXTRA_IS_BOOKING = "extra_key_is_booking";
    String KEY_EXTRA_IS_IN_EDIT_MODE = "extra_key_is_in_edit_mode";
    String KEY_EXTRA_COUPON_CODE = "extra_key_coupon_code";
    String KEY_EXTRA_DISCOUNT = "extra_key_discount";
    String KEY_EXTRA_FINAL_AMOUNT = "extra_key_fina_amount";
    String KEY_EXTRA_SUB_CATEGORY_ID = "extra_key_sub_category_id";
    String KEY_EXTRA_TASK_ARRAY = "extra_key_task_array";
    String KEY_EXTRA_REQUEST_BOOKING_SUMMARY = "extra_key_request_booking_summary";
    String KEY_EXTRA_BOOKING_SERVICE_DETAIL = "extra_key_booking_service_detail";
    String KEY_ADDRESS = "key_address";
    String TYPE_PROVIDER = "P";
    String TYPE_USER = "U";
    String CATEGORY_TYPE_INSTANT = "I";
    String CATEGORY_TYPE_QUOTATION = "Q";
    String STATUS_TYPE_ACCEPTED = "Accepted";
    String STATUS_TYPE_REJECTED = "Rejected";
    String STATUS_TYPE_IN_PROGRESS = "In Progress";
    String STATUS_TYPE_COMPLETED = "Completed";
    String STATUS_TYPE_CONFIRMED = "Confirmed";
    String FCM_KEY_TYPE = "type";
    String FCM_KEY_TITLE = "title";
    String FCM_KEY_MESSAGE = "message";
    String FCM_KEY_TICKER = "ticker";
    String FCM_KEY_ADDITIONAL_DATA = "additional_data";
    String FCM_KEY_BOOKIND_ID = "iBookingId";
    String FCM_KEY_FULL_NAME = "vFullName";
    String FCM_KEY_PROVIDER_ID = "iProviderId";
    String NOTIFICATION_TYPE_CHAT = "chat";
    String NOTIFICATION_TYPE_HIRE_PROVIDER = "hire_provider";
    String NOTIFICATION_TYPE_DECLINE_PROVIDER = "decline_provider";
    String NOTIFICATION_TYPE_SUBMIT_REVIEW = "submit_review";
    String EXTRA_PARAM_IMAGE_URL = "extra_param_image_url";
    String KEY_EXTRA_COUPON_DATA = "extra_key_coupon_data";
    String KEY_TYPE_INPUT = "input";
    String KEY_TYPE_SELECT = "select";
    String KEY_TYPE_DISPLAY = "display";

//The App constant fields...............................................................................................


    //The App Config fields...............................................................................................
    String KEY_EXTRA_NOTES = "key_extra_notes";
    String KEY_TYPE_V_TASK = "vTask";
    String KEY_TYPE_V_TOTAL = "vTotal";
    String KEY_TYPE_V_QTY = "vQty";
    String KEY_TYPE_V_HOUR_REQUIRED_VALUE = "vHoursRequired";


//The App Config fields...............................................................................................


}
