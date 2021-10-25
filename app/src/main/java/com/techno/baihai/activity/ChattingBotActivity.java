package com.techno.baihai.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.techno.baihai.R;
import com.techno.baihai.adapter.ChatAdapter;
import com.techno.baihai.api.APIClient;
import com.techno.baihai.api.APIInterface;
import com.techno.baihai.model.ChatModel;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.PrefManager;
import com.techno.baihai.utils.Utils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChattingBotActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext = this;
    String getProductId,getChatSellerId,getRecieverId,getStatusUpdate;
    List<ChatModel> list;
    private RecyclerView recycler_view;
    private APIInterface apiInterface;
    private Utils utils;
    private Boolean isInternetPresent = false;
    private ImageView iv_send, iv_back;
    private EditText et_text_messgae;
    private LinearLayout layout_chat;
    private ChatAdapter adaper;
    private boolean moveToBottom = true;
    private String uid;
    private LinearLayout  layout_bottomEditTxt, editTxt_layout;
    private ImageView iv_Img_product;
    private String getChatName;
    private String getChatImgUrl;
    private ImageView iv_receiverImg;
    private TextView tv_name_receiver;
    private String statusss="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_chatting_bot);

        utils = new Utils(ChattingBotActivity.this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        PrefManager.isConnectingToInternet(this);
        isInternetPresent = PrefManager.isNetworkConnected(this);

        User user = PrefManager.getInstance(this).getUser();
        uid = String.valueOf(user.getId());
        Log.e("red_ID", "-------->" + uid);


        iv_send = findViewById(R.id.iv_send);
        iv_back = findViewById(R.id.iv_back);

        et_text_messgae = findViewById(R.id.et_text_messgae);
        recycler_view = findViewById(R.id.recycler_view);


        layout_chat = findViewById(R.id.layout_chat);



        editTxt_layout = findViewById(R.id.editTxt_layout);

        layout_bottomEditTxt = findViewById(R.id.layout_bottomEditTxt);

       // tv_name_receiver.setText(getIntent().getStringExtra("name"));
//        Glide.with(ChattingBotActivity.this).load(getIntent().getStringExtra("image")).error(R.drawable.unnamed)
//                .placeholder(R.drawable.user).into(iv_receiver);

       // PrefManager.setString(Constant.RECEIVER_ID, getIntent().getStringExtra("id"));

        iv_send.setOnClickListener(this);

        iv_back.setOnClickListener(this);


        try {


            getChatSellerId = getIntent().getStringExtra("getSellerId");
            getProductId = getIntent().getStringExtra("getProductId");
            statusss = getIntent().getStringExtra("statusss");


            getRecieverId = getIntent().getStringExtra("getRecieverId");
            getStatusUpdate = getIntent().getStringExtra("getStatusUpdate");

            getChatName = getIntent().getStringExtra("getChatName");
            getChatImgUrl = getIntent().getStringExtra("getChatImgUrl");
            Log.e("chatName", getChatName);
            Log.e("chatImg", getChatImgUrl);

            iv_receiverImg = findViewById(R.id.iv_receiverImg);

            tv_name_receiver = findViewById(R.id.tv_name_receiver);
            Glide.with(mContext).load(getChatImgUrl).error(R.drawable.profile_img).into(iv_receiverImg);

            tv_name_receiver.setText(getChatName);

        }catch (Exception e){
            Log.e("chateee",e.getMessage());

        }
//        PrefManager.setString(Constant.RECEIVER_ID,getRecieverId);
//        PrefManager.setString(Constant.USER_ID,uid);




       list=new ArrayList<ChatModel>();
        adaper = new ChatAdapter(ChattingBotActivity.this);
        recycler_view.setLayoutManager(new LinearLayoutManager(ChattingBotActivity.this));
        adaper.setChatData(list);
        recycler_view.setAdapter(adaper);

        if (isInternetPresent) {
            getChatApi();
            handlerMethod();
        }
        else {
            PrefManager prefManager = new PrefManager(mContext);
            PrefManager.showSettingsAlert(mContext);

        }

    }

  /*  @Override
    public void onBackPressed() {
        if (statusss.equals("1")){
            startActivity(new Intent(ChattingBotActivity.this,HomeActivity.class));
            finish();
            Animatoo.animateSwipeRight(this);
        }else {
            super.onBackPressed();
        }
    }*/

    private void sendMessage() {
        if (et_text_messgae.getText().toString().equals("")) {
           Utils.showToast("");
        } else {
            if (isInternetPresent) {
                insertChatApi();
            } else {
                PrefManager prefManager = new PrefManager(mContext);
                PrefManager.showSettingsAlert(mContext);
            /*    AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                    "You don't have internet connection.", false);*/
            }        }
    }


    private void insertChatApi() {

        Log.e("reciverId",getRecieverId);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(ChattingBotActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        String toServer = et_text_messgae.getText().toString().trim();
        String toServerUnicodeEncoded = StringEscapeUtils.escapeJava(toServer);


        Call<ResponseBody> call = apiInterface.insertChat(getRecieverId,uid,getProductId, toServerUnicodeEncoded);
        //, PrefManager.getString(Constant.REGISTER_ID));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {

                try {

                    et_text_messgae.setText("");

                    String result = null;
                    if (response.body() != null) {
                        result = response.body().string();
                    }
                    if (result != null) {
                        Log.e("insertChat",result);
                    }
                    if (isInternetPresent) {
                        getChatApi();
                        moveToBottom = true;
                        progressDialog.dismiss();

                    } else {
                        PrefManager prefManager = new PrefManager(mContext);
                        PrefManager.showSettingsAlert(mContext);
            /*    AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                    "You don't have internet connection.", false);*/
                    }





                    

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("eption", String.valueOf(e));


                }

            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                call.cancel();
                progressDialog.dismiss();
                //Toast.makeText(mContext, "call:"+call, Toast.LENGTH_SHORT).show();
            }
        });


    }


   /* private void inserttChatApi() {

        Log.e("reciverId",getRecieverId);

        AndroidNetworking.post(Constant.BASE_URL+Constant.insert_chat)
                .addBodyParameter("receiver_id", getRecieverId)
                .addBodyParameter("sender_id", uid)
                .addBodyParameter("chat_message", et_text_messgae.getText().toString().trim())
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                // do anything with response
                Log.e("jsonresponse", response);



                try {
                    getChatApi();//REGISTER_ID
                    moveToBottom = true;
                    et_text_messgae.setText("");


                    // moveToBottom=true;


                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {

            }
        });


    }*/


    private void handlerMethod() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (isInternetPresent) {
                    getChatApi();
                    handlerMethod();
                } else {
                    PrefManager prefManager = new PrefManager(mContext);
                    PrefManager.showSettingsAlert(mContext);
            /*    AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                    "You don't have internet connection.", false);*/
                }

            }
        }, 1000);
    }

    private void getChatApi() {

        Log.e("reciverId",getRecieverId);


        Call<ResponseBody> call = apiInterface.getChat(getRecieverId,uid,getProductId);
        //, PrefManager.getString(Constant.REGISTER_ID));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {

            /*    try {
                            String result = response.body().string();
                            JSONObject jsonObject = new JSONObject(result);

                    if (jsonObject.optString("status").equals("1")) {

                        JSONArray jsonArray = jsonObject.optJSONArray("result");

                        list = new ArrayList<ChatModel>();
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            JSONObject jsonObject2= jsonObject1.getJSONObject("receiver_detail");
                            String reciver_Image= jsonObject2.optString("receiver_image");
                            JSONObject jsonObject3=jsonObject1.getJSONObject("sender_detail");
                            String sender_image= jsonObject3.optString("sender_image");
                            Log.e("senderimage",sender_image);
                            Log.e("reciver_Img",getChatImgUrl);

                            ChatModel chatModel = new ChatModel();
                            chatModel.setChat_message(jsonObject1.optString("chat_message"));
                            chatModel.setReceiver_id(jsonObject1.optString("receiver_id"));
                            chatModel.setSender_id(jsonObject1.optString("sender_id"));
                            chatModel.setSender_image(sender_image);
                            chatModel.setReceiver_image(getChatImgUrl);
                            chatModel.setStatus(jsonObject1.optString("status"));//NOTSEEN/SEEN
                            chatModel.setDate(new Utils(getApplicationContext()).convertDate(jsonObject1.optString("date")));
                            chatModel.setTime(new Utils(getApplicationContext()).convertTime(jsonObject1.optString("date")));
                            list.add(chatModel);
                            adaper.notifyDataSetChanged();

                        }

                        adaper.setChatData(list);


                        if(moveToBottom) {
                            if (list.size() > 0) {
                                recycler_view.scrollToPosition(list.size() - 1);
                            }
                            moveToBottom=false;
                        }
*/


                try {

                    String result = null;
                    if (response.body() != null) {
                        result = response.body().string().trim();
                    }
                    JSONObject jsonObject = null;
                    if (result != null) {
                        jsonObject = new JSONObject(result);
                    }
                    Log.e("getChat:", String.valueOf(jsonObject));
                    String status = "null";
                    if (jsonObject != null) {
                        status = jsonObject.optString("status");
                    }


                    if (status.equals("1")) {

                        JSONArray jsonArray = jsonObject.optJSONArray("result");

                        list = new ArrayList<ChatModel>();
                        if (jsonArray != null) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                // JSONObject jsonObject2= jsonObject1.getJSONObject("receiver_detail");
                                //  String reciver_Image= jsonObject2.optString("receiver_image");
                                // JSONObject jsonObject3=jsonObject1.getJSONObject("sender_detail");
                                // String sender_image= jsonObject3.optString("sender_image");
                                // Log.e("senderimage",sender_image);
                                // Log.e("reciver_Img",getChatImgUrl);

                                String serverResponse = jsonObject1.optString("chat_message");
                                String fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava(serverResponse);
                                ChatModel chatModel = new ChatModel();
                                chatModel.setChat_message(fromServerUnicodeDecoded);
                                chatModel.setReceiver_id(jsonObject1.optString("receiver_id"));
                                chatModel.setSender_id(jsonObject1.optString("sender_id"));
                                //  chatModel.setSender_image(sender_image);
                                //  chatModel.setReceiver_image(getChatImgUrl);
                                chatModel.setStatus(jsonObject1.optString("status"));//NOTSEEN/SEEN
                                chatModel.setDate(new Utils(getApplicationContext()).convertDate(jsonObject1.optString("date")));
                                chatModel.setTime(new Utils(getApplicationContext()).convertTime(jsonObject1.optString("date")));
                                list.add(chatModel);
                                adaper.notifyDataSetChanged();

                            }
                        }

                        adaper.setChatData(list);


                        if (moveToBottom) {
                            if (list.size() > 0) {
                                recycler_view.scrollToPosition(list.size() - 1);
                            }
                            moveToBottom = false;
                        }


                    }
                }
                catch (Exception e) {
                    e.printStackTrace();


                }

            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                call.cancel();
                //Toast.makeText(mContext, "call:"+call, Toast.LENGTH_SHORT).show();
            }
        });


    }


/*

    private void gettChatApi() {


        AndroidNetworking.post(Constant.BASE_URL+Constant.get_chat)
                .addBodyParameter("receiver_id", getRecieverId)
                .addBodyParameter("sender_id", uid)
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        // do anything with response
                        Log.e("getChat:", String.valueOf(jsonObject));
                        try {
//                            String result = response.body().string().trim();
//                            JSONObject jsonObject = new JSONObject(result);

                            if (jsonObject.optString("status").equals("1")) {

                                JSONArray jsonArray = jsonObject.optJSONArray("result");

                                list = new ArrayList<ChatModel>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                   // JSONObject jsonObject2= jsonObject1.getJSONObject("receiver_detail");
                                  //  String reciver_Image= jsonObject2.optString("receiver_image");
                                   // JSONObject jsonObject3=jsonObject1.getJSONObject("sender_detail");
                                   // String sender_image= jsonObject3.optString("sender_image");
                                   // Log.e("senderimage",sender_image);
                                   // Log.e("reciver_Img",getChatImgUrl);

                                    String serverResponse = jsonObject1.optString("chat_message");
                                    String fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava(serverResponse);
                                    ChatModel chatModel = new ChatModel();
                                    chatModel.setChat_message(fromServerUnicodeDecoded);
                                    chatModel.setReceiver_id(jsonObject1.optString("receiver_id"));
                                    chatModel.setSender_id(jsonObject1.optString("sender_id"));
                                  //  chatModel.setSender_image(sender_image);
                                  //  chatModel.setReceiver_image(getChatImgUrl);
                                    chatModel.setStatus(jsonObject1.optString("status"));//NOTSEEN/SEEN
                                    chatModel.setDate(new Utils(getApplicationContext()).convertDate(jsonObject1.optString("date")));
                                    chatModel.setTime(new Utils(getApplicationContext()).convertTime(jsonObject1.optString("date")));
                                    list.add(chatModel);
                                    adaper.notifyDataSetChanged();

                                }

                                adaper.setChatData(list);


                                if(moveToBottom) {
                                    if (list.size() > 0) {
                                        recycler_view.scrollToPosition(list.size() - 1);
                                    }
                                    moveToBottom=false;
                                }



                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });


    }
*/






/*    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HomeActivity.class));
        Animatoo.animateShrink(mContext);
           // super.onBackPressed();

    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_send:
                sendMessage();
                break;


        }
    }
}