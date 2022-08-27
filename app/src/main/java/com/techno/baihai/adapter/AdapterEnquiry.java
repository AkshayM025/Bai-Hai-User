package com.techno.baihai.adapter;

import android.app.MediaRouteButton;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.techno.baihai.R;
import com.techno.baihai.api.Constant;
import com.techno.baihai.listner.FragmentListener;
import com.techno.baihai.model.ChatEnquiryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import www.develpoeramit.mapicall.ApiCallBuilder;

public class AdapterEnquiry extends RecyclerView.Adapter<AdapterEnquiry.ViewHolder> {

    private Context context;
    private List<ChatEnquiryModel> chatEnquiryModelList = new ArrayList<>();


    private FragmentListener listener;


    public AdapterEnquiry(FragmentListener listener) {
        this.listener = listener;
    }


    public AdapterEnquiry(Context context, List chatEnquiryModel) {
        this.context = context;
        this.chatEnquiryModelList = chatEnquiryModel;
    }


    @NonNull
    @Override
    public AdapterEnquiry.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_enquiry, parent,
                false);
        AdapterEnquiry.ViewHolder viewHolder = new AdapterEnquiry.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterEnquiry.ViewHolder holder, int position) {

        holder.itemView.setTag(chatEnquiryModelList.get(position));

        ChatEnquiryModel pu = chatEnquiryModelList.get(position);
        holder.item_EnquiryproductName.setText(pu.getChat_username());
        holder.txt_messageId.setText(pu.getChat_message());


        holder.item_status.setText(pu.getStatus());
        if(pu.getStatus().equals("In Progress")){
            holder.item_buttonsEnquery.setVisibility(View.INVISIBLE);
        }
        // holder.item_EnquiryproductName.setText(pu.getProduct_name());
        holder.item_enquiryTime.setText(pu.getDate_time());
        holder.item_EnquiryproductName.setText(pu.getChat_username());


        if (context!=null){
            Glide.with(context).load(pu.getChat_imgUrl()).error(R.drawable.profile_img).into(holder.item_enquiryImg);
        }else {
            Log.e("context", String.valueOf(context));
        }


        holder.item_statusAccepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final ProgressDialog progressDialog;
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                // http://bai-hai.com/webservice/update_chat_status?
                // request_id=71&
                // status=Accepted


                HashMap<String, String> param = new HashMap<>();
                param.put("request_id", chatEnquiryModelList.get(position).getRequestId());
                param.put("status", "Accepted");

                Log.e("request_id=>", String.valueOf(chatEnquiryModelList.get(position).getRequestId()));

                ApiCallBuilder.build(context)
                        .isShowProgressBar(false)        // http://bai-hai.com/webservice/update_chat_status?
                        .setUrl(Constant.BASE_URL + "update_chat_status?")
                        .setParam(param)
                        .execute(new ApiCallBuilder.onResponse() {
                            @Override
                            public void Success(String response) {
                                Log.e("responceUpdate=>", "" + response);
                                progressDialog.dismiss();
                                try {
                                    JSONObject object = new JSONObject(response);
                                    String status = object.optString("status");
                                    String message = object.optString("message");
                                    if (status.equals("1")) {

                                        JSONArray result = object.optJSONArray("result");
                                        Log.e("TAG", "result=>" + result);
                                        chatEnquiryModelList.remove(position);
                                        notifyItemRemoved(position);
                                        notifyDataSetChanged();


                                    } else {

                                       // Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(context, "Check Your Network: ", Toast.LENGTH_LONG).show();


                                }


                            }

                            @Override
                            public void Failed(String error) {

                                progressDialog.dismiss();
                                //CustomSnakbar.showDarkSnakabar(mContext, mview, "" + error);
                                //Toast.makeText(context, "Check Your Network: " + error, Toast.LENGTH_LONG).show();
                            }
                        });


            }
        });

        holder.item_statusRejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final ProgressDialog progressDialog;
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                // http://bai-hai.com/webservice/update_chat_status?
                // request_id=71&
                // status=Accepted
                HashMap<String, String> param = new HashMap<>();
                param.put("request_id", chatEnquiryModelList.get(position).getRequestId());
                param.put("status", "Rejected");

                Log.e("request_id=>", String.valueOf(chatEnquiryModelList.get(position).getRequestId()));
                //  Log.e("seller_id=>", String.valueOf(PrefManager.getString(Preference.KEY_getSellerId)));

                ApiCallBuilder.build(context)
                        .isShowProgressBar(false)        // http://bai-hai.com/webservice/update_chat_status?
                        .setUrl(Constant.BASE_URL + "update_chat_status?")
                        .setParam(param)
                        .execute(new ApiCallBuilder.onResponse() {
                            @Override
                            public void Success(String response) {
                                Log.e("responceUpdate=>", "" + response);
                                progressDialog.dismiss();
                                try {
                                    JSONObject object = new JSONObject(response);
                                    String status = object.optString("status");
                                    String message = object.optString("message");
                                    if (status.equals("1")) {

                                        JSONArray result = object.optJSONArray("result");
                                        Log.e("TAG", "result=>" + result);
                                        chatEnquiryModelList.remove(position);
                                        notifyItemRemoved(position);
                                        notifyDataSetChanged();


                                    } else {

                                        //Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(context, "Check Your Network: ", Toast.LENGTH_LONG).show();


                                }


                            }

                            @Override
                            public void Failed(String error) {

                                progressDialog.dismiss();
                                //CustomSnakbar.showDarkSnakabar(mContext, mview, "" + error);
                                //Toast.makeText(context, "Check Your Network: " + error, Toast.LENGTH_LONG).show();
                            }
                        });

            }
        });
    }


    @Override
    public int getItemCount() {
        if (chatEnquiryModelList == null) {

            return 0;
        } else {
            return chatEnquiryModelList.size();
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {


        public TextView item_status, item_statusAccepted,
                item_statusRejected, item_EnquiryproductName,
                item_enquiryTime,txt_messageId;

        public CircleImageView item_enquiryImg;
        public LinearLayout item_buttonsEnquery;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item_enquiryImg = itemView.findViewById(R.id.item_enquiryImg);
            item_status = itemView.findViewById(R.id.item_statusId);
            item_statusAccepted = itemView.findViewById(R.id.item_statusAccepted);
            item_statusRejected = itemView.findViewById(R.id.item_statusRejected);
            item_buttonsEnquery = itemView.findViewById(R.id.item_buttonsEnquery);
            item_EnquiryproductName = itemView.findViewById(R.id.item_EnquiryproductName);
            item_enquiryTime = itemView.findViewById(R.id.item_enquiryTime);
            txt_messageId = itemView.findViewById(R.id.txt_messageId);


        }
    }

}

