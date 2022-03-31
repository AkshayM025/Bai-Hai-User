package com.techno.baihai.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.techno.baihai.R;
import com.techno.baihai.adapter.AcceptedChatAdapter;
import com.techno.baihai.api.Constant;
import com.techno.baihai.listner.FragmentListener;
import com.techno.baihai.model.AcceptedChatModal;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import www.develpoeramit.mapicall.ApiCallBuilder;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AcceptedChatListFragment extends Fragment {

    FragmentListener listener;
    Context mContext = getActivity();
    TextView txt_acceptedChat;
    String uid;
    private Boolean isInternetPresent = false;
    private List<AcceptedChatModal> acceptedChatModals;
    private RecyclerView accepted_Chat_recyclerview;
    private RecyclerView.Adapter accepted_chat_mAdapter;

    public AcceptedChatListFragment(FragmentListener listener) {
        this.listener = listener;
    }

    public AcceptedChatListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        View view = inflater.inflate(R.layout.fragment_accepted, container, false);


        PrefManager.isConnectingToInternet(getActivity());
        isInternetPresent = PrefManager.isNetworkConnected(getActivity());

        User user = PrefManager.getInstance(getActivity()).getUser();
        uid = String.valueOf(user.getId());
        txt_acceptedChat = view.findViewById(R.id.txt_acceptedChat);

        accepted_Chat_recyclerview = view.findViewById(R.id.aceepted_Chatrecyclerview);
        accepted_Chat_recyclerview.setHasFixedSize(true);
        acceptedChatModals = new ArrayList<>();


        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);


        accepted_Chat_recyclerview.setLayoutManager(layoutManager); // set LayoutManager to RecyclerView

        if (isInternetPresent) {
            GetAcceptedChat();
        } else {
            PrefManager prefManager = new PrefManager(mContext);
            PrefManager.showSettingsAlert(mContext);
        }


        return view;
    }


    private void GetAcceptedChat() {


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        // http://bai-hai.com/webservice/get_chat_request?
        // seller_id=71

        User user = PrefManager.getInstance(getActivity()).getUser();
        HashMap<String, String> param = new HashMap<>();
        param.put("seller_id", user.getId());
        param.put("status", "Accepted");
        ApiCallBuilder.build(getActivity())
                .isShowProgressBar(false)        // http://bai-hai.com/webservice/get_chat_request?seller_id=71&status=Accepted
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
                                Log.e("TAG", "result=>" + result);



                                if (result != null) {
                                    for (int i = 0; i < result.toString().length(); i++) {

                                        JSONObject object1 = result.getJSONObject(i);

                                        JSONObject object2 = object1.optJSONObject("user_details");
                                        JSONObject object3 = object1.optJSONObject("product_details");

                                        if (object2 != null && object3 != null) {

                                            try {
                                                String chat_username = object2.getString("name");
                                                String userchat_img = object2.getString("image1");

                                                String product_name = object3.getString("name");
                                                String product_imgUrl = object3.getString("image1");
                                                String product_desc = object3.getString("description");


                                                Log.e("", "name=>" + chat_username);


                                                String request_id = object1.optString("id");
                                                String status_id = object1.optString("status");
                                                String reciver_id = object1.optString("user_id");
                                                String seller_id = object1.optString("seller_id");
                                                String product_id = object1.optString("product_id");
                                                if(seller_id.equals(user.getId())){
                                                    acceptedChatModals.add(new AcceptedChatModal(reciver_id, seller_id,
                                                            product_id, chat_username, userchat_img, status_id, "",
                                                            product_name, product_imgUrl, product_desc));


                                                }


                                                }catch(JSONException e){
                                                Log.e("jsonEx", Objects.requireNonNull(e.getMessage()));
                                            }



                                    }


                                    }
                                } else {

                                    txt_acceptedChat.setVisibility(View.VISIBLE);

                                }


                            } else {

                                txt_acceptedChat.setVisibility(View.VISIBLE);
                                // Toast.makeText(mContext, "You don't have an any request" + message, Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        accepted_chat_mAdapter = new AcceptedChatAdapter(getActivity(), acceptedChatModals);
                        accepted_Chat_recyclerview.removeAllViews();
                        accepted_Chat_recyclerview.setAdapter(accepted_chat_mAdapter);


                    }

                    @Override
                    public void Failed(String error) {

                        progressDialog.dismiss();
                        Toast.makeText(mContext, "Check Your Network: " + error, Toast.LENGTH_LONG).show();
                    }
                });


    }

}