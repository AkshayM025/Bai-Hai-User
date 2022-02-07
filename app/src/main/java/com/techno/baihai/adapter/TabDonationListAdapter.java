package com.techno.baihai.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.techno.baihai.R;
import com.techno.baihai.activity.UpdateProductActivity;
import com.techno.baihai.model.MyProductModeListl;

import java.util.List;

public class TabDonationListAdapter extends RecyclerView.Adapter<com.techno.baihai.adapter.TabDonationListAdapter.ViewHolder> {


    private final Context context;
    private final List<MyProductModeListl> myProductModeListls;


    public TabDonationListAdapter(Context context, List<MyProductModeListl> MyProductModeListl) {
        this.context = context;
        this.myProductModeListls = MyProductModeListl;
    }


    @NonNull
    @Override
    public TabDonationListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myproduct_count, parent, false);
        TabDonationListAdapter.ViewHolder viewHolder = new TabDonationListAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TabDonationListAdapter.ViewHolder holder, int position) {

        holder.itemView.setTag(myProductModeListls.get(position));

        MyProductModeListl pu = myProductModeListls.get(position);

        Glide.with(context)
                .load(pu.getProduct_image1Url()).error(R.drawable.product_placeholder)
                .into(holder.myDonation_imglistId);

        holder.myDonation_txtlistId.setText(pu.getProduct_name());
        holder.myDonation_TxtcountlistId.setText(pu.getProduct_IntrustTotalCount());
        holder.cardProduct_countId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Toast.makeText(context, "Click:" + myProductModeListls.get(position).getProduct_name(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();

                if (position == 0) {
                    intent = new Intent(context, UpdateProductActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("categoryId", myProductModeListls.get(position).getProduct_category_id());
                    intent.putExtra("categoryName", myProductModeListls.get(position).getCategory_name());
                    intent.putExtra("productId", myProductModeListls.get(position).getProduct_id());
                    intent.putExtra("productImgUrl", myProductModeListls.get(position).getProduct_image1Url());
                    intent.putExtra("productName", myProductModeListls.get(position).getProduct_name());
                    intent.putExtra("productAddress", myProductModeListls.get(position).getProduct_address());
                    intent.putExtra("productUsed", myProductModeListls.get(position).getProduct_used());
                    intent.putExtra("productDesc", myProductModeListls.get(position).getProduct_description());
                    intent.putExtra("productLat", myProductModeListls.get(position).getProduct_lat());
                    intent.putExtra("productLong", myProductModeListls.get(position).getProduct_lon());
                    intent.putExtra("productStatus", myProductModeListls.get(position).getProduct_price());


                    context.startActivity(intent);
                } else {
                    intent = new Intent(context, UpdateProductActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("categoryId", myProductModeListls.get(position).getProduct_category_id());
                    intent.putExtra("categoryName", myProductModeListls.get(position).getCategory_name());
                    intent.putExtra("productId", myProductModeListls.get(position).getProduct_id());
                    intent.putExtra("productImgUrl", myProductModeListls.get(position).getProduct_image1Url());
                    intent.putExtra("productName", myProductModeListls.get(position).getProduct_name());
                    intent.putExtra("productAddress", myProductModeListls.get(position).getProduct_address());
                    intent.putExtra("productUsed", myProductModeListls.get(position).getProduct_used());
                    intent.putExtra("productDesc", myProductModeListls.get(position).getProduct_description());
                    intent.putExtra("productLat", myProductModeListls.get(position).getProduct_lat());
                    intent.putExtra("productLong", myProductModeListls.get(position).getProduct_lon());
                    intent.putExtra("productStatus", myProductModeListls.get(position).getProduct_price());


                    context.startActivity(intent);
                }


            }
        });


    }

    @Override
    public int getItemCount() {
        if (myProductModeListls == null) {

            return 0;
        } else {
            return myProductModeListls.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public CardView cardProduct_countId;
        public TextView myDonation_txtlistId, myDonation_TxtcountlistId;
        public ImageView myDonation_imglistId;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardProduct_countId = itemView.findViewById(R.id.cardProduct_countId);
            myDonation_txtlistId = itemView.findViewById(R.id.myDonation_txtlistId);
            myDonation_TxtcountlistId = itemView.findViewById(R.id.myDonation_TxtcountlistId);
            myDonation_imglistId = itemView.findViewById(R.id.myDonation_imglistId);


        }
    }

}


