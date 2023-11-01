package com.techno.baihai.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.techno.baihai.R;
import com.techno.baihai.api.Constant;
import com.techno.baihai.activity.CategoryProductActivity;
import com.techno.baihai.model.MyProductModeListl;
import com.techno.baihai.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends
        SliderViewAdapter<SliderAdapter.SliderAdapterVH> {


    private final Context context;
    private List<MyProductModeListl> myProductModeList = new ArrayList<>();


    public SliderAdapter(Context context, List<MyProductModeListl> myProductModeListls) {
        this.context = context;
        this.myProductModeList = myProductModeListls;


    }


    public void renewItems(List<MyProductModeListl> sliderItems) {
        this.myProductModeList = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.myProductModeList.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(MyProductModeListl sliderItem) {
        this.myProductModeList.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        MyProductModeListl sliderItem = myProductModeList.get(position);

        viewHolder.textViewDescription.setText(sliderItem.getProduct_description());
        viewHolder.textViewDescription.setTextSize(16);
        viewHolder.textViewDescription.setTextColor(Color.WHITE);
        Glide.with(viewHolder.itemView)
                .load(sliderItem.getProduct_image1Url())
                .fitCenter()
                .into(viewHolder.imageViewBackground);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "This is item in position " + position, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();

                if (position == 0) {
                    PrefManager.setString(Constant.RECEIVER_ID, myProductModeList.get(position).getSeller_id());
                    intent = new Intent(context, CategoryProductActivity.class);
                    intent.putExtra("getSellerId", myProductModeList.get(position).getSeller_id());
                    intent.putExtra("getSellerName", myProductModeList.get(position).getProduct_seller_name());
                    Log.e("getSellerN", myProductModeList.get(position).getProduct_seller_name());

                    intent.putExtra("getProductId", myProductModeList.get(position).getProduct_id());
                    intent.putExtra("getProductCategoryId", myProductModeList.get(position).getProduct_category_id());
                    intent.putExtra("getProductCategoryImageUrl", myProductModeList.get(position).getCategory_image());
                    intent.putExtra("getProductCategoryName", myProductModeList.get(position).getCategory_name());
                    intent.putExtra("getProductName", myProductModeList.get(position).getProduct_name());
                    intent.putExtra("getProductImageUrl", myProductModeList.get(position).getProduct_image1Url());
                    intent.putExtra("getProductDecrip", myProductModeList.get(position).getProduct_description());
                    intent.putExtra("getProductAddress", myProductModeList.get(position).getProduct_address());
                    intent.putExtra("getProductlat", myProductModeList.get(position).getProduct_lat());
                    intent.putExtra("getProductlon", myProductModeList.get(position).getProduct_lon());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(intent);

                } else {
                    PrefManager.setString(Constant.RECEIVER_ID, myProductModeList.get(position).getSeller_id());
                    intent = new Intent(context, CategoryProductActivity.class);
                    intent.putExtra("getSellerId", myProductModeList.get(position).getSeller_id());
                    intent.putExtra("getSellerName", myProductModeList.get(position).getProduct_seller_name());
                    Log.e("getSellerN", myProductModeList.get(position).getProduct_seller_name());


                    intent.putExtra("getProductId", myProductModeList.get(position).getProduct_id());
                    intent.putExtra("getProductCategoryId", myProductModeList.get(position).getProduct_category_id());
                    intent.putExtra("getProductCategoryImageUrl", myProductModeList.get(position).getCategory_image());
                    intent.putExtra("getProductCategoryName", myProductModeList.get(position).getCategory_name());
                    intent.putExtra("getProductName", myProductModeList.get(position).getProduct_name());
                    intent.putExtra("getProductImageUrl", myProductModeList.get(position).getProduct_image1Url());
                    intent.putExtra("getProductDecrip", myProductModeList.get(position).getProduct_description());
                    intent.putExtra("getProductAddress", myProductModeList.get(position).getProduct_address());
                    intent.putExtra("getProductlat", myProductModeList.get(position).getProduct_lat());
                    intent.putExtra("getProductlon", myProductModeList.get(position).getProduct_lon());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return myProductModeList.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;
        TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }


}
