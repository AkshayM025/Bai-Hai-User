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
import com.techno.baihai.api.Constant;
import com.techno.baihai.activity.CategoryProductActivity;
import com.techno.baihai.model.MyProductModeListl;
import com.techno.baihai.utils.PrefManager;

import java.util.List;

public class MyProductListAdapter extends RecyclerView.Adapter<MyProductListAdapter.ViewHolder> {

    private final Context context;
    private final List<MyProductModeListl> myProductModeListls;


    public MyProductListAdapter(Context context, List MyProductModeListl) {
        this.context = context;
        this.myProductModeListls = MyProductModeListl;
    }


    @NonNull
    @Override
    public MyProductListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myproduct_list, parent, false);
        MyProductListAdapter.ViewHolder viewHolder = new MyProductListAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyProductListAdapter.ViewHolder holder, int position) {

        holder.itemView.setTag(myProductModeListls.get(position));

        MyProductModeListl pu = myProductModeListls.get(position);

        Glide.with(context)
                .load(pu.getProduct_image1Url())
                .into(holder.product_Icon);

        holder.product_name.setText(pu.getProduct_name());
        holder.product_desc.setText(pu.getProduct_description());
        holder.product_sellerName.setText(pu.getProduct_seller_name());
        holder.product_dateTimeId.setText(pu.getProduct_dateTime());


        holder.card_productListId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();

                if (myProductModeListls.get(position).getProduct_category_id().equals(0)) {
                    PrefManager.setString(Constant.RECEIVER_ID, myProductModeListls.get(position).getSeller_id());

                    intent = new Intent(context, CategoryProductActivity.class);
                    intent.putExtra("getSellerId", myProductModeListls.get(position).getSeller_id());
                    intent.putExtra("getSellerName", myProductModeListls.get(position).getProduct_seller_name());

                    intent.putExtra("getProductId", myProductModeListls.get(position).getProduct_id());
                    intent.putExtra("getProductCategoryId", myProductModeListls.get(position).getProduct_category_id());
                    intent.putExtra("getProductCategoryImageUrl", myProductModeListls.get(position).getCategory_image());
                    intent.putExtra("getProductCategoryName", myProductModeListls.get(position).getCategory_name());
                    intent.putExtra("getProductName", myProductModeListls.get(position).getProduct_name());
                    intent.putExtra("getProductImageUrl", myProductModeListls.get(position).getProduct_image1Url());
                    intent.putExtra("getProductDecrip", myProductModeListls.get(position).getProduct_description());
                    intent.putExtra("getProductAddress", myProductModeListls.get(position).getProduct_address());
                    intent.putExtra("getProductlat", myProductModeListls.get(position).getProduct_lat());
                    intent.putExtra("getProductlon", myProductModeListls.get(position).getProduct_lon());

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(intent);

                } else {
                    PrefManager.setString(Constant.RECEIVER_ID, myProductModeListls.get(position).getSeller_id());
                    intent = new Intent(context, CategoryProductActivity.class);
                    intent.putExtra("getSellerId", myProductModeListls.get(position).getSeller_id());
                    intent.putExtra("getSellerName", myProductModeListls.get(position).getProduct_seller_name());
                    intent.putExtra("getProductId", myProductModeListls.get(position).getProduct_id());
                    intent.putExtra("getProductCategoryId", myProductModeListls.get(position).getProduct_category_id());
                    intent.putExtra("getProductCategoryImageUrl", myProductModeListls.get(position).getCategory_image());
                    intent.putExtra("getProductCategoryName", myProductModeListls.get(position).getCategory_name());
                    intent.putExtra("getProductName", myProductModeListls.get(position).getProduct_name());
                    intent.putExtra("getProductImageUrl", myProductModeListls.get(position).getProduct_image1Url());
                    intent.putExtra("getProductDecrip", myProductModeListls.get(position).getProduct_description());
                    intent.putExtra("getProductAddress", myProductModeListls.get(position).getProduct_address());
                    intent.putExtra("getProductlat", myProductModeListls.get(position).getProduct_lat());
                    intent.putExtra("getProductlon", myProductModeListls.get(position).getProduct_lon());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

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


        public ImageView product_Icon;
        public CardView card_productListId;
        public TextView product_name, product_desc, product_sellerName,product_dateTimeId;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            product_Icon = itemView.findViewById(R.id.product_IconId);
            card_productListId = itemView.findViewById(R.id.card_productListId);
            product_name = itemView.findViewById(R.id.product_nameId);
            product_desc = itemView.findViewById(R.id.product_descriptionId);
            product_sellerName = itemView.findViewById(R.id.product_sellerName);
            product_dateTimeId = itemView.findViewById(R.id.product_dateTimeId);



        }
    }

}
