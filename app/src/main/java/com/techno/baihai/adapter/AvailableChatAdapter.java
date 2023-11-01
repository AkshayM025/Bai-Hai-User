package com.techno.baihai.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.techno.baihai.R;
import com.techno.baihai.activity.ChattingBotActivity;
import com.techno.baihai.model.AcceptedChatModal;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AvailableChatAdapter extends RecyclerView.Adapter<AvailableChatAdapter.ViewHolder> {


    private final Context context;
    private List<AcceptedChatModal> acceptedChatModalList = new ArrayList<>();


    public AvailableChatAdapter(Context context, List availableChatModal) {
        this.context = context;
        this.acceptedChatModalList = availableChatModal;
    }


    @NonNull
    @Override
    public AvailableChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_available_chat, parent,
                false);
        AvailableChatAdapter.ViewHolder viewHolder = new AvailableChatAdapter.ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull AvailableChatAdapter.ViewHolder holder, int position) {

        holder.itemView.setTag(acceptedChatModalList.get(position));

        AcceptedChatModal pu = acceptedChatModalList.get(position);


        holder.item_chatUserName.setText("Name: " + pu.getSellerchat_name());
        holder.item_chatUserId.setText("ID:545697890" + pu.getSeller_id());
        holder.item_chatUserAddress.setVisibility(View.GONE);


        holder.productNameId.setText("Product: " + pu.getProduct_name());
        holder.productDescId.setText("Description: " + pu.getProduct_desc());
        Glide.with(context)
                .load(pu.getProduct_imgUrl()).error(R.drawable.papaya)
                .into(holder.productImgUrl);


        Glide.with(context)
                .load(pu.getSellerchat_img()).error(R.drawable.profile_img)
                .into(holder.item_availableImg);

        holder.availableChat_CardId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                if (position == 0) {
                    intent = new Intent(context, ChattingBotActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("getProductId", acceptedChatModalList.get(position).getProduct_id());
                    intent.putExtra("getRecieverId", acceptedChatModalList.get(position).getReciever_id());
                    intent.putExtra("getSellerId", acceptedChatModalList.get(position).getSeller_id());
                    intent.putExtra("getChatName", acceptedChatModalList.get(position).getSellerchat_name());
                    intent.putExtra("getChatImgUrl", acceptedChatModalList.get(position).getSellerchat_img());
                    intent.putExtra("getStatusUpdate", acceptedChatModalList.get(position).getStatus());

                    context.startActivity(intent);

                } else {
                    intent = new Intent(context, ChattingBotActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("getProductId", acceptedChatModalList.get(position).getProduct_id());
                    intent.putExtra("getRecieverId", acceptedChatModalList.get(position).getReciever_id());
                    intent.putExtra("getSellerId", acceptedChatModalList.get(position).getSeller_id());
                    intent.putExtra("getStatusUpdate", acceptedChatModalList.get(position).getStatus());
                    intent.putExtra("getChatName", acceptedChatModalList.get(position).getSellerchat_name());
                    intent.putExtra("getChatImgUrl", acceptedChatModalList.get(position).getSellerchat_img());
                    intent.putExtra("getStatusUpdate", acceptedChatModalList.get(position).getStatus());
                    context.startActivity(intent);

                }
            }
        });


    }


    @Override
    public int getItemCount() {
        if (acceptedChatModalList == null) {

            return 0;
        } else {
            return acceptedChatModalList.size();
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {


        public TextView item_chatUserAddress, item_chatUserName,
                item_chatUserId, productNameId, productDescId;

        public CircleImageView item_availableImg;
        public ImageView productImgUrl;
        public LinearLayout layout_productdeatils;
        public CardView availableChat_CardId;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item_chatUserName = itemView.findViewById(R.id.item_chatUserName);
            item_chatUserId = itemView.findViewById(R.id.item_chatUserId);
            item_chatUserAddress = itemView.findViewById(R.id.item_chatUserAddress);


            availableChat_CardId = itemView.findViewById(R.id.availableChat_CardId);
            item_availableImg = itemView.findViewById(R.id.item_availableImg);

            //--------------------------------------------Prduct && Details--------------------------------------------//
            productImgUrl = itemView.findViewById(R.id.productImgUrl);
            productNameId = itemView.findViewById(R.id.productNameId);
            productDescId = itemView.findViewById(R.id.productDescId);


            layout_productdeatils = itemView.findViewById(R.id.layout_productdeatils);
            productImgUrl = itemView.findViewById(R.id.productImgUrl);


        }
    }

}
