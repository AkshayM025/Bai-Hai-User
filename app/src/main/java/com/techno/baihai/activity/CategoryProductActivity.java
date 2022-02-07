package com.techno.baihai.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;
import com.techno.baihai.R;
import com.techno.baihai.adapter.ProSliderAdapter;
import com.techno.baihai.adapter.SliderAdapter;
import com.techno.baihai.api.Constant;
import com.techno.baihai.databinding.FragmentProductCategoryBinding;
import com.techno.baihai.listner.FragmentListener;
import com.techno.baihai.model.GetProDetailModel;
import com.techno.baihai.model.MyProductModeListl;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.GPSTracker;
import com.techno.baihai.utils.PrefManager;
import com.techno.baihai.utils.Preference;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import www.develpoeramit.mapicall.ApiCallBuilder;


public class
CategoryProductActivity extends AppCompatActivity implements OnMapReadyCallback {

    Context mContext = this;
    FragmentListener listener;
    ImageView chat, catImageView, product_ImgdetailsId;
    TextView catTxtViewId, details_locationId, product_DricrptionId;
    LinearLayout layout_status;
    RoundedImageView iv_Img_product;
    TextView iv_txt_product;
    private String latitude, longitude;
    private String uid;
    private String product_SellerId, productId, product_categoryId,
            productName, productImgUrl, productDescrip, productAddress;
    private String Status, message;
    private String product_categoryImage;
    private String product_categoryName;
    private SliderAdapter adapter;
    private List<MyProductModeListl> myProductModeListls;


    private Circle mCircle;
    private Marker mMarker;
    private GoogleMap mMap;
    private String productlat;
    private String productlon;
    private TextView productNameId;
    private EditText iv_message_product;
    private CircleImageView user_imgId;
    private CardView btn_user_dialog;
    private TextView user_mobileId;
    private TextView nameId;
    private ImageView img_cancel;
    private String product_SellerName;
    private TextView sellerNameId;
    ArrayList<String> returnValue;


    private Boolean isInternetPresent = false;
    private ProgressBar loading_spinnerId;
    private ProSliderAdapter proSliderAdapter;
    private FragmentProductCategoryBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = DataBindingUtil.setContentView(this,R.layout.fragment_product_category);
      //  setContentView(R.layout.fragment_product_category);
        PrefManager.isConnectingToInternet(this);
        isInternetPresent = PrefManager.isNetworkConnected(this);

        Preference.get(mContext, "categoryId");

        catImageView = findViewById(R.id.cat_produImgId);

        catTxtViewId = findViewById(R.id.cat_prodoTxtId);
        details_locationId = findViewById(R.id.details_productLocationId);
        sellerNameId = findViewById(R.id.sellerNameId);


        User user = PrefManager.getInstance(this).getUser();
        uid = String.valueOf(user.getId());
        Log.e("red_ID", "-------->" + uid);




        try {
            product_SellerId = getIntent().getStringExtra("getSellerId");
            productId = getIntent().getStringExtra("getProductId");
            product_SellerName = getIntent().getStringExtra("getSellerName");





            // Toast.makeText(mContext, "productId=> " + productId, Toast.LENGTH_SHORT).show();

            product_categoryId = getIntent().getStringExtra("getProductCategoryId");
            product_categoryImage = getIntent().getStringExtra("getProductCategoryImageUrl");
            product_categoryName = getIntent().getStringExtra("getProductCategoryName");

            productName = getIntent().getStringExtra("getProductName");
            productImgUrl = getIntent().getStringExtra("getProductImageUrl");
            productDescrip = getIntent().getStringExtra("getProductDecrip");
            productAddress = getIntent().getStringExtra("getProductAddress");
            productlat = getIntent().getStringExtra("getProductlat");
            productlon = getIntent().getStringExtra("getProductlon");

            productNameId = findViewById(R.id.prodNameId);
            productNameId.setText("Name:- " + productName);
            sellerNameId.setText("SellerName: "+product_SellerName);

            sellerNameId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SellerDailog();
                }
            });

            PrefManager.setString(Preference.KEY_getSellerId, product_SellerId);
            PrefManager.setString(Preference.KEY_getProductId, productId);

        } catch (Exception exception) {
            exception.printStackTrace();
            Toast.makeText(mContext, "" + exception, Toast.LENGTH_LONG).show();
        }

       GetSliderProductList(productId);

    /*
        PrefManager.setString(Preference.KEY_getProductCategoryId,product_categoryId);
        PrefManager.setString(Preference.KEY_getProductName,productName);
        PrefManager.setString(Preference.KEY_getProductImageUrl,productImgUrl);
        PrefManager.setString(Preference.KEY_getProductDecrip,productDescrip);
        PrefManager.setString(Preference.KEY_getProductAddress,productAddress);


*/


       // product_ImgdetailsId = findViewById(R.id.product_ImgdetailsId);
        product_DricrptionId = findViewById(R.id.product_DricrptionId);

        product_DricrptionId.setText(productDescrip);


       // Picasso.get().load(productImgUrl).placeholder(R.drawable.product_placeholder).into(product_ImgdetailsId);


        Picasso.get().load(product_categoryImage).placeholder(R.drawable.unnamed).into(catImageView);
        catTxtViewId.setText(product_categoryName);


        chat = findViewById(R.id.chat);

        if (uid.equals(product_SellerId)) {
            chat.setVisibility(View.GONE);

        } else {
            chat.setVisibility(View.VISIBLE);
            chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent();
                    //GetChatStatusApi();
                    ChatRequestDialog();

                }
            });

        }

        getCurrentLocation();
        //getAddress()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        getAddress(Double.parseDouble(productlat), Double.parseDouble(productlon));

    }

    private void GetSliderProductList(String productId) {


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        HashMap<String, String> param = new HashMap<>();
        param.put("product_id",productId);



        ApiCallBuilder.build(this)
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "product_details?") //http://bai-hai.com/webservice/product_details?product_id=46
                .setParam(param)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        Log.e("get_latest_product=>", "" + response);
                        progressDialog.dismiss();
                        try {
                            GetProDetailModel  commentModel = new Gson().fromJson(response, GetProDetailModel.class);

                            JSONObject object = new JSONObject(response);
                            String status = object.optString("status");
                            String message = object.optString("message");
                            String result = object.optString("result");
                            JSONObject object1 = new JSONObject(result);
                            if (status.equals("1")) {
                                Log.e("get_image_2=>", "" + object1.optString("image1").split("images/").length);
                                binding.imageSlider.setVisibility(View.VISIBLE);
                                myProductModeListls = new ArrayList<>();
                                if( object1.optString("image1").split("images/").length == 2){
                                    myProductModeListls.add(new MyProductModeListl(productId,commentModel.getResult().getUserId(),
                                            commentModel.getResult().getUserDetails().getUserName(),commentModel.getResult().getCategoryId(),commentModel.getResult().getName(),commentModel.getResult().getDescription(),
                                            "",commentModel.getResult().getAddress(),commentModel.getResult().getUsed(),commentModel.getResult().getImage1(),
                                            "", commentModel.getResult().getCategoryDetails().getCatImage(), commentModel.getResult().getCategoryDetails().getCatName(),  commentModel.getResult().getLat(), commentModel.getResult().getLon(), "product_status", "product_dateTime"));

                                }
                                if( object1.optString("image2").split("images/").length == 2){
                                    myProductModeListls.add(new MyProductModeListl(productId,commentModel.getResult().getUserId(),
                                            commentModel.getResult().getUserDetails().getUserName(),commentModel.getResult().getCategoryId(),commentModel.getResult().getName(),commentModel.getResult().getDescription(),
                                            "",commentModel.getResult().getAddress(),commentModel.getResult().getUsed(),commentModel.getResult().getImage2(),
                                            "", commentModel.getResult().getCategoryDetails().getCatImage(), commentModel.getResult().getCategoryDetails().getCatName(),  commentModel.getResult().getLat(), commentModel.getResult().getLon(), "product_status", "product_dateTime"));

                                }
                                if( object1.optString("image3").split("images/").length == 2){
                                    myProductModeListls.add(new MyProductModeListl(productId,commentModel.getResult().getUserId(),
                                            commentModel.getResult().getUserDetails().getUserName(),commentModel.getResult().getCategoryId(),commentModel.getResult().getName(),commentModel.getResult().getDescription(),
                                            "",commentModel.getResult().getAddress(),commentModel.getResult().getUsed(),commentModel.getResult().getImage3(),
                                            "", commentModel.getResult().getCategoryDetails().getCatImage(), commentModel.getResult().getCategoryDetails().getCatName(),  commentModel.getResult().getLat(), commentModel.getResult().getLon(), "product_status", "product_dateTime"));

                                }
                                if( object1.optString("image4").split("images/").length == 2){
                                    myProductModeListls.add(new MyProductModeListl(productId,commentModel.getResult().getUserId(),
                                            commentModel.getResult().getUserDetails().getUserName(),commentModel.getResult().getCategoryId(),commentModel.getResult().getName(),commentModel.getResult().getDescription(),
                                            "",commentModel.getResult().getAddress(),commentModel.getResult().getUsed(),commentModel.getResult().getImage4(),
                                            "", commentModel.getResult().getCategoryDetails().getCatImage(), commentModel.getResult().getCategoryDetails().getCatName(),  commentModel.getResult().getLat(), commentModel.getResult().getLon(), "product_status", "product_dateTime"));

                                }
                                if( object1.optString("image5").split("images/").length == 2){
                                    myProductModeListls.add(new MyProductModeListl(productId,commentModel.getResult().getUserId(),
                                            commentModel.getResult().getUserDetails().getUserName(),commentModel.getResult().getCategoryId(),commentModel.getResult().getName(),commentModel.getResult().getDescription(),
                                            "",commentModel.getResult().getAddress(),commentModel.getResult().getUsed(),commentModel.getResult().getImage5(),
                                            "", commentModel.getResult().getCategoryDetails().getCatImage(), commentModel.getResult().getCategoryDetails().getCatName(),  commentModel.getResult().getLat(), commentModel.getResult().getLon(), "product_status", "product_dateTime"));

                                }




                                adapter = new SliderAdapter(getApplicationContext(), myProductModeListls);

                                binding.imageSlider.setSliderAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.THIN_WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                                binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                                binding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                                binding.imageSlider.setIndicatorSelectedColor(Color.WHITE);
                                binding.imageSlider.setIndicatorUnselectedColor(Color.GRAY);
                                binding.imageSlider.setScrollTimeInSec(4); //set scroll delay in seconds :
                                binding.imageSlider.startAutoCycle();





                            }else {
                                binding.imageSlider.setVisibility(View.GONE);
                                binding.productImgdetailsId.setVisibility(View.VISIBLE);

                                Toast.makeText(mContext, "You have no new product\nnear by ", Toast.LENGTH_SHORT).show();
                                }





                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(mContext, "Exception" + e, Toast.LENGTH_SHORT).show();


                        }

                    }

                    @Override
                    public void Failed(String error) {

                        progressDialog.dismiss();
                        //CustomSnakbar.showDarkSnakabar(mContext, mview, "" + error);
                        Toast.makeText(mContext, "Error" + error, Toast.LENGTH_SHORT).show();
                    }
                });


    }


    private void updateMarkerWithCircle(LatLng position) {
        mCircle.setCenter(position);
        mMarker.setPosition(position);
    }

    private void drawMarkerWithCircle(LatLng position) {
        double radiusInMeters = 100;
        int strokeColor = 0xffff0000; //red outline
        int shadeColor = 0x44ff0000; //opaque red fill

        CircleOptions circleOptions = new CircleOptions().center(position).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(2);
        mCircle = mMap.addCircle(circleOptions);
        int height = 16;
        int width = 16;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.task);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        MarkerOptions markerOptions = new MarkerOptions().position(position).
                icon(BitmapDescriptorFactory.fromBitmap((smallMarker)));// .icon(bitmapDescriptorFromVector(this, R.drawable.ic_circle));


        //5256 2208 0046 7254
        // mMarker = mMap.addMarker(markerOptions);

    }

  /*  private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_circle);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }*/


    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng Point = new LatLng(Double.parseDouble(productlat), Double.parseDouble(productlon));
        // mMap.addMarker(new MarkerOptions().position(Point).title("Product"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(Point));
        if (mCircle == null || mMarker == null) {
            drawMarkerWithCircle(Point);
        } else {
            updateMarkerWithCircle(Point);
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Point, 16));


/*        CircleOptions circleOptions = new CircleOptions()
                .center(Point)   //set center
                .radius(500)   //set radius in meters
                .fillColor(Color.TRANSPARENT)  //default
                .strokeColor(Color.BLUE)
                .strokeWidth(5);

        mCircle  = mMap.addCircle(circleOptions);*/

      /*  mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                if(mCircle == null || mMarker == null){
                    drawMarkerWithCircle(Point);
                }else{
                    updateMarkerWithCircle(Point);
                }
            }
        });
*/

    }


    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String addressStreet = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();

                // result.append(address.getLocality()).append("\n");
                // result.append(address.getCountryName());
                Log.e("Address===", addressStreet + " " + city + " " + country);
                Log.e("addressStreet===", addressStreet);
//                Log.e("city===", city);
                Log.e("country===", country);

                // tvServiceLocation.setText(addressStreet + " " + city + " " + country);
                details_locationId.setText(city + " " + country);
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }


    private void ChatRequestDialog() {

        final AlertDialog.Builder alert = new AlertDialog.Builder(CategoryProductActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.alert_chat_request, null);

        layout_status = findViewById(R.id.layout_status);

        iv_Img_product = mView.findViewById(R.id.iv_Img_product);
        iv_txt_product = mView.findViewById(R.id.iv_txt_product);
        iv_message_product = mView.findViewById(R.id.iv_message_product);
        //iv_txtstatus_product.setText(Status);
        //iv_txt_product.setText(productName);

        // Toast.makeText(mContext, "" + message, Toast.LENGTH_LONG).show();
        Glide.with(CategoryProductActivity.this).load(productImgUrl).error(R.drawable.unnamed).placeholder(R.drawable.product_placeholder).into(iv_Img_product);

        TextView btn_okay = mView.findViewById(R.id.btn_okay);
        TextView btn_exit = mView.findViewById(R.id.btn_exit);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.setCanceledOnTouchOutside(true);
        btn_exit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            alertDialog.dismiss();

                                        }
                                    });
        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myCustomMessage = iv_message_product.getText().toString().trim();
                if (!myCustomMessage.equalsIgnoreCase("")) {
                    alertDialog.dismiss();
                    if (isInternetPresent) {
                        GetChatStatusApi(myCustomMessage);
                    } else {
                        PrefManager prefManager = new PrefManager(mContext);
                        PrefManager.showSettingsAlert(mContext);
            /*    AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                    "You don't have internet connection.", false);*/
                    }
                } else {
                    Toast.makeText(mContext, "Please enter message..!!", Toast.LENGTH_LONG).show();
                }

            }
        });
        alertDialog.show();


    }

    private void ConfirmationDialog(String message) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(CategoryProductActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.chat_confirm_dailog, null);

     //   layout_status = findViewById(R.id.layout_status);

    ImageView cancel = mView.findViewById(R.id.cancel);


        Button btn_okay = mView.findViewById(R.id.btn_okay);
        TextView txt = mView.findViewById(R.id.txt);
        txt.setText(message);

        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.setCanceledOnTouchOutside(false);
        cancel.setOnClickListener(v -> {
            alertDialog.dismiss();

        });
        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (message.equals("You have already sent a request,please wait for approval")) {

/*
                                    final AlertDialog.Builder alert = new AlertDialog.Builder(CategoryProductFragment.this);
                                    View mView = getLayoutInflater().inflate(R.layout.chat_request_alertbox, null);

                                    layout_status = findViewById(R.id.layout_status);

                                    iv_Img_product = mView.findViewById(R.id.iv_Img_product);
                                    iv_txt_product = mView.findViewById(R.id.iv_txt_product);
                                    iv_txtstatus_product = mView.findViewById(R.id.iv_status_product);
                                    iv_txtstatus_product.setText(Status);
                                    iv_txt_product.setText(productName);
                                    Toast.makeText(mContext, "" + message, Toast.LENGTH_LONG).show();
                                    Glide.with(CategoryProductFragment.this).load(productImgUrl).error(R.drawable.unnamed)
                                            .placeholder(R.drawable.product_placeholder).into(iv_Img_product);

                                    TextView btn_okay = mView.findViewById(R.id.btn_okay);
                                    alert.setView(mView);
                                    final AlertDialog alertDialog = alert.create();
                                    alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                    alertDialog.setCanceledOnTouchOutside(false);
                                    btn_okay.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // myCustomMessage.setText(txt_inputText.getText().toString());

                                            alertDialog.dismiss();
                                            startActivity(new Intent(mContext, HomeActivity.class).
                                                    setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                            Intent.FLAG_ACTIVITY_NEW_TASK));
                                            Animatoo.animateShrink(mContext);
                                            finish();

                                        }
                                    });
                                    alertDialog.show();*/



                   // Toast.makeText(mContext, "" + message, Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            alertDialog.dismiss();

                            Intent intent = new Intent(mContext, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                            Animatoo.animateSlideDown(mContext);

                        }
                    }, 2000);


                }
                else {

                   // Toast.makeText(mContext, "Your chat request:" + Status, Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            alertDialog.dismiss();

                            Intent intent = new Intent(mContext, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                            Animatoo.animateSlideDown(mContext);

                        }
                    }, 2000);


                }


            }
        });
        alertDialog.show();


    }



    private void GetChatStatusApi(String chatmessage) {


        // http://bai-hai.com/webservice/chat_request?
        // user_id=1&
        // seller_id=2&
        // product_id=1


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        HashMap<String, String> parms = new HashMap<>();
        parms.put("user_id", uid);
        parms.put("seller_id", product_SellerId);
        parms.put("message", chatmessage);
        parms.put("product_id", productId);

        Toast.makeText(mContext, "seller_id: " + product_SellerId, Toast.LENGTH_SHORT).show();
        Toast.makeText(mContext, "product_id: " + productId, Toast.LENGTH_SHORT).show();


        Log.e("user_id=>", String.valueOf(uid));
        Log.e("seller_id=>", String.valueOf(product_SellerId));
        Log.e("product_id=>", String.valueOf(productId));


        ApiCallBuilder.build(this)
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "chat_request?")
                .setParam(parms)       // http://bai-hai.com/webservice/chat_request?
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        progressDialog.dismiss();

                        Log.e("responsechat=>", String.valueOf(response));
                        // do anything with response
                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.optString("status");
                            message = object.optString("message");
                            Log.e("status", status);
                            Log.d("TAG", "STATUS_:" + status);

                            if (status.equals("1")) {

                                JSONObject result = object.getJSONObject("result");


                                String status_id = result.optString("id");
                                String seller_id = result.optString("seller_id");

                                Status = result.optString("status");

                                PrefManager.setString("Status", Status);
                                PrefManager.setString("SellerId", seller_id);

                                Log.e("", "status=>" + Status);
                                ConfirmationDialog(message);

                             /*   if (message.equals("You have already sent a request,please wait for approval")) {

*//*
                                    final AlertDialog.Builder alert = new AlertDialog.Builder(CategoryProductFragment.this);
                                    View mView = getLayoutInflater().inflate(R.layout.chat_request_alertbox, null);

                                    layout_status = findViewById(R.id.layout_status);

                                    iv_Img_product = mView.findViewById(R.id.iv_Img_product);
                                    iv_txt_product = mView.findViewById(R.id.iv_txt_product);
                                    iv_txtstatus_product = mView.findViewById(R.id.iv_status_product);
                                    iv_txtstatus_product.setText(Status);
                                    iv_txt_product.setText(productName);
                                    Toast.makeText(mContext, "" + message, Toast.LENGTH_LONG).show();
                                    Glide.with(CategoryProductFragment.this).load(productImgUrl).error(R.drawable.unnamed)
                                            .placeholder(R.drawable.product_placeholder).into(iv_Img_product);

                                    TextView btn_okay = mView.findViewById(R.id.btn_okay);
                                    alert.setView(mView);
                                    final AlertDialog alertDialog = alert.create();
                                    alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                    alertDialog.setCanceledOnTouchOutside(false);
                                    btn_okay.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // myCustomMessage.setText(txt_inputText.getText().toString());

                                            alertDialog.dismiss();
                                            startActivity(new Intent(mContext, HomeActivity.class).
                                                    setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                            Intent.FLAG_ACTIVITY_NEW_TASK));
                                            Animatoo.animateShrink(mContext);
                                            finish();

                                        }
                                    });
                                    alertDialog.show();*//*



                                    Toast.makeText(mContext, "" + message, Toast.LENGTH_LONG).show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(mContext, HomeActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                            Animatoo.animateSlideDown(mContext);

                                        }
                                    }, 2000);


                                }
                                else {

                                    Toast.makeText(mContext, "Your chat request:" + Status, Toast.LENGTH_LONG).show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            startActivity(new Intent(CategoryProductActivity.this, HomeActivity.class));
                                            finish();

                                        }
                                    }, 2000);


                                }
*/

                            } else if (status.equals("0")) {

                                progressDialog.dismiss();
                                Toast.makeText(CategoryProductActivity.this, " " + message, Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {

                            progressDialog.dismiss();
                            Toast.makeText(mContext, "Error:" + e, Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Failed(String error) {
                        progressDialog.dismiss();
                        Toast.makeText(mContext, "Error=" + error, Toast.LENGTH_LONG).show();

                    }
                });


    }


    private void getCurrentLocation() {

        GPSTracker track = new GPSTracker(this);
        if (track.canGetLocation()) {
            latitude = String.valueOf(track.getLatitude());
            Log.e("lat=>", "-------->" + latitude);

            longitude = String.valueOf(track.getLongitude());
            Log.e("lon=>", "-------->" + longitude);
            final double lat1 = track.getLatitude();
            final double lon1 = track.getLongitude();
            //details_locationId.setText(Tools.getCompleteAddressString(this, lat1, lon1));


            //latLng = new LatLng(latitude, longitude);

        } else {
            track.showSettingsAlert();
        }
    }

    public void detailBack(View view) {
        onBackPressed();
    }


    private void GetProfile(String uid) {

        //loading_spinnerId.setVisibility(View.VISIBLE);
        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", uid);
        ApiCallBuilder.build(this)
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "get_profile?")
                .setParam(param)
                .isShowProgressBar(true)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        Log.e("Response=>", "" + response);
                        try {

                            JSONObject object = new JSONObject(response);
                            String status = object.optString("status");
                            String message = object.optString("message");
                            if (status.equals("1")) {
                              //  loading_spinnerId.setVisibility(View.GONE);


                                //   https://www.shipit.ng/BaiHai/webservice/get_profile?user_id=1

                                JSONObject result = object.optJSONObject("result");
                                //String id = result.optString("id");
                                //Log.e("resultt=>", "" + response);


                                String user_ID = "null";
                                if (result != null) {
                                    user_ID = result.optString("id");
                                }
                                String username = "null";
                                if (result != null) {
                                    username = result.optString("name");
                                }
                                String mobile = "null";
                                if (result != null) {
                                    mobile = result.optString("mobile");
                                }
                                String email = "null";
                                if (result != null) {
                                    email = result.optString("email");
                                }
                                String password = "null";
                                if (result != null) {
                                    password = result.optString("password");
                                }
                                String image = "null";
                                if (result != null) {
                                    image = result.optString("image");
                                }
                                String legal_info = "null";
                                if (result != null) {
                                    legal_info = result.optString("legal_info");
                                }
                                String guide = "null";
                                if (result != null) {
                                    guide = result.optString("guide");
                                }
                                String guide_free = "null";
                                if (result != null) {
                                    guide_free = result.optString("guide_free");
                                }
                                String guide_give_free = "null";
                                if (result != null) {
                                    guide_give_free = result.optString("guide_give_free");
                                }
                                // Log.e("image=>", "-------->" + image);


                                User user = new User(user_ID, username, email, password, mobile,image, legal_info,guide,guide_free,guide_give_free);

                                PrefManager.getInstance(getApplicationContext()).userLogin(user);



                                final AlertDialog.Builder alert = new AlertDialog.Builder(CategoryProductActivity.this);

                                View mView = getLayoutInflater().inflate(R.layout.user_dialog, null);
                                nameId = mView.findViewById(R.id.nameId);
                                user_mobileId = mView.findViewById(R.id.user_mobileId);
                                user_imgId = mView.findViewById(R.id.user_imgId);
                                btn_user_dialog = mView.findViewById(R.id.btn_user_dialog);
                                img_cancel = mView.findViewById(R.id.img_cancel);
                                loading_spinnerId = mView.findViewById(R.id.loading_spinnerId);


                                alert.setView(mView);
                                final AlertDialog alertDialog = alert.create();
                                alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                alertDialog.setCanceledOnTouchOutside(false);
                                nameId.setText(user.getUsername());
                                user_mobileId.setText(mobile);
                                //et_passwordID.setText(user.getPassword());
                                //Glide.with(mContext).load(image).error(R.drawable.profile_img).into(civ_User);
                                Picasso.get().load(image).placeholder(R.drawable.profile_img).into(user_imgId);
                                img_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();

                                    }
                                });

                                btn_user_dialog.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();

                                    }
                                });



                                alertDialog.show();


                            } else {
                                //loading_spinnerId.setVisibility(View.GONE);

                                Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            //loading_spinnerId.setVisibility(View.GONE);

                            Log.e("error: ", String.valueOf(e));
                            //Toast.makeText(EditAccountActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void Failed(String error) {
                        // progess.dismiss();
                        //loading_spinnerId.setVisibility(View.GONE);


                        // CustomSnakbar.showDarkSnakabar(EditAccountActivity.this, mview, "" + error);
                        Toast.makeText(mContext, "Failed" + error, Toast.LENGTH_SHORT).show();
                    }
                });


    }


    private void SellerDailog() {


        if (isInternetPresent) {
            GetProfile(product_SellerId);
        } else {
            PrefManager prefManager = new PrefManager(mContext);
            PrefManager.showSettingsAlert(mContext);
            /*    AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                    "You don't have internet connection.", false);*/
        }


 /*       final AlertDialog.Builder alert = new AlertDialog.Builder(CategoryProductActivity.this);

        View mView = getLayoutInflater().inflate(R.layout.user_dialog, null);
        nameId = mView.findViewById(R.id.nameId);
        user_mobileId = mView.findViewById(R.id.user_mobileId);
        user_imgId = mView.findViewById(R.id.user_imgId);
        btn_user_dialog = mView.findViewById(R.id.user_imgId);
        img_cancel = mView.findViewById(R.id.img_cancel);
        loading_spinnerId = mView.findViewById(R.id.loading_spinnerId);

        if (isInternetPresent) {
            GetProfile(product_SellerId);
        } else {
            PrefManager prefManager = new PrefManager(mContext);
            PrefManager.showSettingsAlert(mContext);
            */
        /*    AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                    "You don't have internet connection.", false);*/
        /*
        }


        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.setCanceledOnTouchOutside(false);
        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });

        btn_user_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });



        alertDialog.show();*/
    }

}
