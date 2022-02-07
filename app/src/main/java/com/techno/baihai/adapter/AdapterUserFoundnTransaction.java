package com.techno.baihai.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.techno.baihai.R;
import com.techno.baihai.databinding.AdapterBaihaiTransactionBinding;
import com.techno.baihai.model.GetUserFoundnTransDataModel;

import java.util.ArrayList;

public class AdapterUserFoundnTransaction extends RecyclerView.Adapter<AdapterUserFoundnTransaction.MyView> {

    private final Context context;
    private ArrayList<GetUserFoundnTransDataModel> transactionDataModels;


    public AdapterUserFoundnTransaction(Context context, ArrayList<GetUserFoundnTransDataModel> models) {
        this.transactionDataModels = models;
        this.context = context;

    }

    @Override
    public AdapterUserFoundnTransaction.MyView onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        AdapterBaihaiTransactionBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.adapter_baihai_transaction, viewGroup, false);

        return new AdapterUserFoundnTransaction.MyView(binding);
    }


    @Override
    public void onBindViewHolder(final AdapterUserFoundnTransaction.MyView holder, final int position) {


        GetUserFoundnTransDataModel dataModel = transactionDataModels.get(position);

        try {
            holder.itemDoseBinding.foundationOrderid.setText("OrderNo: " + dataModel.getId());
            holder.itemDoseBinding.foundationAmount.setText("$ " + dataModel.getAmount());
            holder.itemDoseBinding.foundationOrgname.setText("Non profit: " + dataModel.getFoundationDetails().getOrgName());
            holder.itemDoseBinding.foundationProductname.setText("Product: " + dataModel.getProductDetails().product);
            holder.itemDoseBinding.foundationDateTimeId.setText("Date&Time: " + dataModel.getDateTime());
            holder.itemDoseBinding.foundationStatusId.setText(dataModel.getStatus());


        } catch (Exception exception) {
            exception.printStackTrace();
            Toast.makeText(context, "adapter_exception" + exception.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("adapter_exception", exception.getMessage());
        }


    }

    @Override
    public int getItemCount() {

        return transactionDataModels.size();
    }


    public static class MyView extends RecyclerView.ViewHolder {
        AdapterBaihaiTransactionBinding itemDoseBinding;


        public MyView(AdapterBaihaiTransactionBinding itemDoseBinding) {
            super(itemDoseBinding.getRoot());
            this.itemDoseBinding = itemDoseBinding;


        }
    }
}
