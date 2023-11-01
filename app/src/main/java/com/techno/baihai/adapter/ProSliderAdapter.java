package com.techno.baihai.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smarteist.autoimageslider.SliderViewAdapter;
import com.techno.baihai.R;
import com.techno.baihai.activity.kotlin.ProductDonateActivity;

import java.io.File;
import java.util.ArrayList;

public class ProSliderAdapter extends SliderViewAdapter<ProSliderAdapter.SliderAdapterVH> {

    private final Context context;
    ArrayList<String> imgArrayList;



    public ProSliderAdapter(Context context) {
        this.context = context;
    }

    public ProSliderAdapter(Context context, ArrayList<String> imgArrayList) {
        this.context = context;
        this.imgArrayList = imgArrayList;


    }



    public void renewItems(ArrayList<String> sliderItems) {
        this.imgArrayList = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.imgArrayList.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(String sliderItem) {
        this.imgArrayList.add(sliderItem);
        notifyDataSetChanged();
    }


    @Override
    public ProSliderAdapter.SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(ProSliderAdapter.SliderAdapterVH viewHolder, final int position) {

        Log.e("image=", "" + imgArrayList.get(position));
        File imgFile = new File(imgArrayList.get(position));
        if (imgFile.exists()) {

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            viewHolder.imageViewBackground.setImageBitmap(myBitmap);

        }
        viewHolder.btn_removeimage.setOnClickListener(v -> {
            if (getCount() != -1) {
                deleteItem(position);
                notifyDataSetChanged();
                if (getCount() == 0) {
                    ProductDonateActivity.task();
                    notifyDataSetChanged();
                }
            } else {
                ProductDonateActivity.task();
            }
        });

    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return imgArrayList.size();
    }

    static class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;
        TextView textViewDescription;
        ImageView btn_removeimage;


        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            btn_removeimage = itemView.findViewById(R.id.btn_removeimage);
            btn_removeimage.setVisibility(View.VISIBLE);

            this.itemView = itemView;
        }
    }


}



