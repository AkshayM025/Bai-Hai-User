package com.techno.baihai.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.techno.baihai.R;
import com.techno.baihai.adapter.AdapterEnquiry;
import com.techno.baihai.api.Constant;
import com.techno.baihai.listner.FragmentListener;
import com.techno.baihai.model.ChatEnquiryModel;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import www.develpoeramit.mapicall.ApiCallBuilder;


public class ChatEnquiryFragment extends Fragment {


    FragmentListener listener;
    Context mContext;
    TextView txt_idNodataFound;
    String uid;
    private Boolean isInternetPresent = false;
    private List<ChatEnquiryModel> chatEnquiryModelList;
    private RecyclerView chatEnquiry_recyclerView;
    private RecyclerView.Adapter chatEnquiry_mAdapter;
    private String chat_usernameId;


    public ChatEnquiryFragment(FragmentListener listener) {
        this.listener = listener;
    }

    public ChatEnquiryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        View view = inflater.inflate(R.layout.fragment_chat_enquiry, container, false);
        mContext = getActivity();
        PrefManager.isConnectingToInternet(getActivity());
        isInternetPresent = PrefManager.isNetworkConnected(getActivity());

        User user = PrefManager.getInstance(getActivity()).getUser();
        uid = String.valueOf(user.getId());
        Log.i("TAG", "user_id: " + uid);

        txt_idNodataFound = view.findViewById(R.id.txt_idNodataFound);

        chatEnquiry_recyclerView = view.findViewById(R.id.chat_enquiry_recyclerview);
        chatEnquiry_recyclerView.setHasFixedSize(true);
        chatEnquiryModelList = new ArrayList<>();

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


        chatEnquiry_recyclerView.setLayoutManager(layoutManager); // set LayoutManager to RecyclerView

        if (isInternetPresent) {
            GetChatEnquiry();
        } else {
            PrefManager prefManager = new PrefManager(mContext);
            PrefManager.showSettingsAlert(mContext);
        }

        return view;
    }


    private void GetChatEnquiry() {


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        // http://bai-hai.com/webservice/get_chat_request?
        // seller_id=71


        HashMap<String, String> param = new HashMap<>();
        param.put("seller_id", uid);
        param.put("status", "Pending");
        ApiCallBuilder.build(getActivity())
                .isShowProgressBar(false)        // http://bai-hai.com/webservice/get_chat_request?
                .setUrl(Constant.BASE_URL + "get_chat_request?")
                .setParam(param)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        Log.e("ResponseChatE==>", "" + response);
                        progressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.optString("status");
                            String message = object.optString("message");
                            if (status.equals("1")) {


                                JSONArray result = object.optJSONArray("result");


                                if (result != null) {
                                    for (int i = 0; i < result.length(); i++) {
                                        try {

                                            JSONObject object1 = result.getJSONObject(i);
                                            JSONObject object2 = object1.optJSONObject("user_details");
                                            JSONObject object3 = object1.optJSONObject("product_details");
                                            String chat_username = "null";
                                            if (object2 != null) {
                                                chat_username = object2.getString("name");
                                                chat_usernameId = object2.getString("id");

                                            }
                                            String chat_imgUrl = "null";
                                            if (object2 != null) {
                                                chat_imgUrl = object2.optString("image1");
                                            }

                                            Log.e("", "name=>" + chat_username);

                                            String request_id = object1.optString("id");
                                            String product_name = object1.optString("name");
                                            String product_message = "Name product: " + object3.optString("name") + "  -   " + object1.optString("message");
                                            String enquiry_time = object1.optString("date_time");
                                            String enquiry_status = object1.optString("status");

                                            String enquiry_productImageUrl = object1.optString("image1");


                                            chatEnquiryModelList.add(new ChatEnquiryModel(request_id, enquiry_status,
                                                    chat_username, chat_imgUrl, product_name, "",
                                                    enquiry_time, enquiry_productImageUrl, product_message));
                                        } catch (Exception e) {
                                            Log.e("Innner" + e, "");
                                        }


                                    }

                                }


                            } else {

                                txt_idNodataFound.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                        try {


                            chatEnquiry_mAdapter = new AdapterEnquiry(getActivity(), chatEnquiryModelList);
                            chatEnquiry_recyclerView.removeAllViews();
                            chatEnquiry_recyclerView.setAdapter(chatEnquiry_mAdapter);
                        } catch (Exception exception) {
                            Log.e("adapter" + exception, "");
                        }


                    }

                    @Override
                    public void Failed(String error) {

                        progressDialog.dismiss();
                        Toast.makeText(mContext, "Check Your Network: " , Toast.LENGTH_LONG).show();
                    }
                });


    }


    private void GetProfile(String uid) {

        //loading_spinnerId.setVisibility(View.VISIBLE);
        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", uid);
        ApiCallBuilder.build(getActivity())
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


                                //   https://www.shipit.ng/BaiHai/webservice/get_profile?user_id=1

                                JSONObject result = object.optJSONObject("result");


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

                                User user = new User(user_ID, username, email, password, mobile, image, legal_info, guide, guide_free, guide_give_free);

                                PrefManager.getInstance(mContext.getApplicationContext()).userLogin(user);


                                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                                View mView = getLayoutInflater().inflate(R.layout.user_dialog, null);
                                TextView nameId = mView.findViewById(R.id.nameId);
                                TextView user_mobileId = mView.findViewById(R.id.user_mobileId);
                                CircleImageView user_imgId = mView.findViewById(R.id.user_imgId);
                                CardView btn_user_dialog = mView.findViewById(R.id.btn_user_dialog);
                                ImageView img_cancel = mView.findViewById(R.id.img_cancel);


                                alert.setView(mView);
                                final AlertDialog alertDialog = alert.create();
                                alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                alertDialog.setCanceledOnTouchOutside(false);
                                nameId.setText(user.getUsername());
                                user_mobileId.setText(mobile);
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

                                //Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
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
                        //Toast.makeText(mContext, "Failed" + error, Toast.LENGTH_SHORT).show();
                    }
                });


    }


}