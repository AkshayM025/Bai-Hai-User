package com.techno.baihai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.techno.baihai.R;
import com.techno.baihai.databinding.AdapterBaihaiTransactionBinding;
import com.techno.baihai.databinding.ItemCoinhistoryBinding;
import com.techno.baihai.model.RewardsHistoryModel;

import java.util.ArrayList;

public class AdapterCoinModel extends RecyclerView.Adapter<AdapterCoinModel.MyView> {


    private final Context context;
    private ArrayList<RewardsHistoryModel.Result> transactionDataModels;


    public AdapterCoinModel(Context context, ArrayList<RewardsHistoryModel.Result> models) {
        this.transactionDataModels = models;
        this.context = context;

    }

    @Override
    public AdapterCoinModel.MyView onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        ItemCoinhistoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.item_coinhistory, viewGroup, false);

        return new AdapterCoinModel.MyView(binding);
    }


    @Override
    public void onBindViewHolder(final AdapterCoinModel.MyView holder, final int position) {


        RewardsHistoryModel.Result dataModel = transactionDataModels.get(position);

        holder.itemDoseBinding.coinId.setText(dataModel.getName());
        Glide.with(this.context).load(dataModel.getImage()).error(R.drawable.rewards).into(holder.itemDoseBinding.myDonationImglistId);


    }

    @Override
    public int getItemCount() {

        return transactionDataModels.size();
    }


    public static class MyView extends RecyclerView.ViewHolder {
        ItemCoinhistoryBinding itemDoseBinding;


        public MyView(ItemCoinhistoryBinding itemDoseBinding) {
            super(itemDoseBinding.getRoot());
            this.itemDoseBinding = itemDoseBinding;


        }
    }
}


