package com.techno.baihai.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.braintreepayments.cardform.view.CardForm;
import com.marozzi.roundbutton.RoundButton;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.techno.baihai.R;
import com.techno.baihai.api.APIClient;
import com.techno.baihai.api.APIInterface;
import com.techno.baihai.api.Constant;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.PrefManager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.develpoeramit.mapicall.ApiCallBuilder;

public class StripePaymentActivity extends AppCompatActivity {

    Context mContext;
    // FragmentListener listener;
    ImageView iv_back;

    TextView done;
    private CardForm cardForm;
    private RoundButton bt_Paymentsuccess;
    private APIInterface apiInterface;
    private Boolean isInternetPresent = false;
    private String uid;
    private boolean status;
    private String Key_FoundatonId="";
    private String Key_AmountId="";
    private String Key_DonateProductId="";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_stripe_payment);
        mContext = StripePaymentActivity.this;

        PrefManager.isConnectingToInternet(mContext);
        isInternetPresent = PrefManager.isNetworkConnected(mContext);


        User user = PrefManager.getInstance(mContext).getUser();
        uid = String.valueOf(user.getId());
        Log.e("user_id: ", uid);
        try {

            status = PrefManager.getBoolean(PrefManager.KEY_BaiHai_Status);
            Key_FoundatonId = PrefManager.getString(PrefManager.Key_FoundatonId);
            Key_DonateProductId = PrefManager.getString(PrefManager.Key_DonateProductId);
            Key_AmountId = PrefManager.getString(PrefManager.Key_AmountId);
        } catch (Exception ex) {
            ex.printStackTrace();
            //Toast.makeText(mContext, "check Yourfoundations Id" + ex, Toast.LENGTH_SHORT).show();
        }
        TextView NeedTo_payID=findViewById(R.id.NeedTo_payID);
        TextView payment_actionBarId=findViewById(R.id.payment_actionBarId);

        if(status) {
            NeedTo_payID.setVisibility(View.VISIBLE);
            NeedTo_payID.setText("$"+PrefManager.getString(PrefManager.Key_AmountId));
            payment_actionBarId.setText(getString(R.string.donate_to_a_foundaton));


        }else{
            NeedTo_payID.setVisibility(View.GONE);
            payment_actionBarId.setText(getString(R.string.donate_to_bai_hai));

        }


        iv_back = findViewById(R.id.iv_backFromPayment);

        apiInterface = APIClient.getClient().create(APIInterface.class);



        cardForm = (CardForm) findViewById(R.id.card_form);


        initView();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)

    private void initView() {
        cardForm = findViewById(R.id.card_form);
        bt_Paymentsuccess = findViewById(R.id.bt_Paymentsuccess);


        bt_Paymentsuccess.setText("Make Payment");
        cardInit();
        bt_Paymentsuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardForm.isValid()) {

                    bt_Paymentsuccess.startAnimation();
                    bt_Paymentsuccess.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bt_Paymentsuccess.setResultState(RoundButton.ResultState.SUCCESS);
                        }
                    }, 1000);
                    bt_Paymentsuccess.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            validation();
                        }
                    }, 1300);

                }else{
                    Toast.makeText(mContext, "Please enter the card details..!!", Toast.LENGTH_LONG).show();

                }
            }
        });

    }



    private void cardInit() {
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .setup(this);

        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
    }

    private void validation() {

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();
        Card.Builder card = new Card.Builder(
                cardForm.getCardNumber(),
                Integer.valueOf(cardForm.getExpirationMonth()),
                Integer.valueOf(cardForm.getExpirationYear()),
                cardForm.getCvv());

        if (!card.build().validateCard()) {
            Toast.makeText(this, "Card Not Valid", Toast.LENGTH_SHORT).show();

        }
        else {
//pk_test_51I583oC2nlYcvIcBh4uyn1NH6J4zY8rA9UfQnjTIpDSDfAOp00HhYbFYteqA70DOmvJqmvGZpgKGuePcNDL2d2Do00wcDAb4Ch
            Stripe stripe = new Stripe(mContext, PrefManager.ADD_PKTest);//ForLive adding a : PrefManager.ADD_PKLive

            stripe.createCardToken(
                    card.build(), new ApiResultCallback<Token>() {
                        @Override
                        public void onSuccess(@NotNull Token token) {

                            Log.e("token", String.valueOf(token));


                            //http://bai-hai.com/webservice/stripe_payment?user_id=20&trans_id=7&amount=11&status=Confirm&
                            // token=454&currency=USD

                            String tokenId = token.getId();
                            Log.e("tokenId", token.getId());


                            if (status) {
                                PrefManager.setBoolean(PrefManager.KEY_BaiHai_Status, false);
                                Toast.makeText(mContext, "foundationPayment!!", Toast.LENGTH_LONG).show();
                                if (uid!=null&&Key_FoundatonId!=null&&Key_AmountId!=null&&Key_DonateProductId!=null&&tokenId!=null &&

                                        !uid.equalsIgnoreCase("")&&!Key_FoundatonId.equalsIgnoreCase("")&&
                                        !Key_AmountId.equalsIgnoreCase("")&&!Key_DonateProductId.equalsIgnoreCase("")){

                                payment_to_foundation(uid, Key_FoundatonId, Key_DonateProductId, "02", Key_AmountId, "Confirm", tokenId, "USD");}
                                else {
                                    Toast.makeText(mContext, "User & tokenId not found..!!", Toast.LENGTH_SHORT).show();

                                }


                            } else {


                                final AlertDialog.Builder alert = new AlertDialog.Builder(StripePaymentActivity.this);

                                View mView = getLayoutInflater().inflate(R.layout.payment_custom_dialog, null);

                                final EditText txt_inputText = (EditText) mView.findViewById(R.id.et_amount);
                                Button btn_cancel = (Button) mView.findViewById(R.id.amount_deniedId);
                                ImageView amount_cancelId = (ImageView) mView.findViewById(R.id.amount_cancelId);

                                Button btn_okay = (Button) mView.findViewById(R.id.amount_proceedId);
                                alert.setView(mView);
                                final AlertDialog alertDialog = alert.create();
                                dialog.dismiss();
                                alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.setCancelable(false);
                                btn_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                        finish();
                                    }
                                });
                                btn_okay.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        String ammount = txt_inputText.getText().toString().trim();
                                        if (ammount.compareTo("") == 0) {
                                            Toast.makeText(mContext, "Please enter amount", Toast.LENGTH_SHORT).show();
                                        } else {
                                            alertDialog.dismiss();
                                            Toast.makeText(mContext, "DonateToBye-Hi!!", Toast.LENGTH_LONG).show();
                                            if (uid!=null&&!uid.equalsIgnoreCase("")&&tokenId!=null&&!tokenId.equalsIgnoreCase(""))
                                            {PaymentStripApi(uid, "USD", ammount, "Confirm", tokenId, "01");}
                                            else {
                                                Toast.makeText(mContext, "User & tokenId not found..!!", Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                    }
                                });
                                amount_cancelId.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                        finish();

                                    }
                                });
                                alertDialog.show();


                            }
                        }

                        @Override
                        public void onError(@NotNull Exception e) {
                            // ProjectUtil.pauseProgressDialog();
                            //Toast.makeText(mContext,
                             //       "failed"+e, Toast.LENGTH_SHORT).show();

                        }
                    });



        }


    }



    private void PaymentStripApi(String user_id, String currency, String amount,String status, String token,String trans_id) {

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();

        //Log.e("registerLogin",registerId);

        Call<ResponseBody> call = apiInterface.stripe_payment(user_id,trans_id,amount,status,token,currency);
        //, PrefManager.getString(Constant.REGISTER_ID));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {

                try {

                    String respons = response.body().string();
                    Log.e( "paymentResponse:=>",respons);

                    JSONObject jsonObject = new JSONObject(respons);
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
                    // String result = jsonObject.optString("result");
                    // Log.e("jsOnLogin", String.valueOf(jsonObject));

                    if (status.equals("1")) {

                        Toast.makeText(StripePaymentActivity.this, "Your Payment "+message, Toast.LENGTH_LONG).show();
                        SuccessPaymentDailog();


                    } else {
                        dialog.dismiss();
                        Toast.makeText(StripePaymentActivity.this, "Your Payment "+message, Toast.LENGTH_SHORT).show();

                    }
                    dialog.dismiss();

                } catch (Exception e) {
                    dialog.dismiss();
                    e.printStackTrace();
                    //Toast.makeText(StripePaymentActivity.this, " "+e, Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                call.cancel();
                Log.e("e", String.valueOf(call));
                //Toast.makeText(StripePaymentActivity.this, ""+call, Toast.LENGTH_SHORT).show();

            }
        });


    }


    public void donePaymentInit(View view) {
        startActivity(new Intent(this, DonateToFoundationFragment.class));
        finish();
    }

    public void iv_backFromPayment(View view) {
        onBackPressed();
        finish();
    }




    private void payment_to_foundation(String user_id, String foundation_id, String donate_product_id,String trans_id, String amount,String status,String token,String currency) {



        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
//http://bai-hai.com/webservice/payment_to_foundation?user_id=20&foundation_id=9&
// donate_product_id=2&trans_id=7&amount=11&status=Confirm&token=454&currency=USD


        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();


        HashMap<String, String> parms = new HashMap<>();
        parms.put("user_id", user_id);
        parms.put("foundation_id", foundation_id);
        parms.put("donate_product_id", donate_product_id);
        parms.put("trans_id", trans_id);
        parms.put("amount", amount);
        parms.put("status", "Confirm");
        parms.put("token", token);
        parms.put("currency", "USD");


        ApiCallBuilder.build(mContext)
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "payment_to_foundation?")
                .setParam(parms)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {


                        Log.e("payment_to_foundation=>", "-------->" + response);


                        try {

                            JSONObject object = new JSONObject(response);
                            String status = object.optString("status");
                            String message = object.optString("message");
                            if (status.equals("1")) {
                                progressDialog.dismiss();


                                Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
                                SuccessPaymentDailog();


                            setLog("El usuario realizo un pago  a fundacion");


                            } else {
                                progressDialog.dismiss();

                                //Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            Log.e("payment_to_foundation=>", "-------->" + e);

                            //Toast.makeText(mContext, "" + e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void Failed(String error) {
                        progressDialog.dismiss();
                        Log.e("payment_to_foundation=>", "-------->" + error);

                        //Toast.makeText(mContext, "" + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }



private void SuccessPaymentDailog(){

    final AlertDialog.Builder alert = new AlertDialog.Builder(StripePaymentActivity.this);

    View mView = getLayoutInflater().inflate(R.layout.payment_complt_dialog,null);
    final RoundButton bt = (RoundButton)mView.findViewById(R.id.bt_paymentsuccessdialog);
    final TextView custom_coinsId = mView.findViewById(R.id.custom_coinsId);
    if (status){
        custom_coinsId.setVisibility(View.GONE);
    }
    else {
        custom_coinsId.setVisibility(View.VISIBLE);

    }

    alert.setView(mView);
    final AlertDialog alertDialog = alert.create();
    alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    alertDialog.setCanceledOnTouchOutside(false);
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
                    alertDialog.dismiss();
                     bt.revertAnimation();
                    finish();
                   }
            }, 2000);

        }
    });

    alertDialog.show();
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








