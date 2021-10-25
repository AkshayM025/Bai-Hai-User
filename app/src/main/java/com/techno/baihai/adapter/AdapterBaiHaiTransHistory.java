package com.techno.baihai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.techno.baihai.R;
import com.techno.baihai.databinding.AdapterBaihaiTransactionBinding;
import com.techno.baihai.model.BaiHaiTransactionDataModel;


import java.util.ArrayList;

public class AdapterBaiHaiTransHistory extends RecyclerView.Adapter<AdapterBaiHaiTransHistory.MyView>  {




    // private final List<SubjCatModelList> catModelLists;
    private final Context context;
    private ArrayList<BaiHaiTransactionDataModel> transactionDataModels;


    public AdapterBaiHaiTransHistory(Context context, ArrayList<BaiHaiTransactionDataModel> models) {
        this.transactionDataModels = models;
        this.context = context;
        //  this.contactListFiltered = list;

    }

    @Override
    public AdapterBaiHaiTransHistory.MyView onCreateViewHolder(ViewGroup viewGroup, int viewType) {

     //   View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_dose, parent, false);
        AdapterBaihaiTransactionBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.adapter_baihai_transaction, viewGroup, false);

        return new AdapterBaiHaiTransHistory.MyView(binding);
    }



    @Override
    public void onBindViewHolder(final AdapterBaiHaiTransHistory.MyView holder, final int position) {


        BaiHaiTransactionDataModel dataModel = transactionDataModels.get(position);


        holder.itemDoseBinding.foundationOrderid.setText("OrderNo: "+dataModel.getId());
        holder.itemDoseBinding.foundationAmount.setText("$ "+dataModel.getAmount());
        holder.itemDoseBinding.foundationOrgname.setText("Foundation: Bye-Hi");
        holder.itemDoseBinding.foundationProductname.setVisibility(View.GONE);
        holder.itemDoseBinding.foundationDateTimeId.setText("Date&Time: "+dataModel.getDateTime());
        holder.itemDoseBinding.foundationStatusId.setText(dataModel.getStatus());









    }

    @Override
    public int getItemCount() {
        // return contactListFiltered.size();

        return transactionDataModels.size();
    }

/*    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = catModelLists;
                } else {
                    List<SubjCatModelList> filteredList = new ArrayList<>();
                    for (SubjCatModelList row : catModelLists) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getSubcategory_name().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<SubjCatModelList>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }*/

    public static class MyView extends RecyclerView.ViewHolder {
        AdapterBaihaiTransactionBinding itemDoseBinding;



        public MyView(AdapterBaihaiTransactionBinding itemDoseBinding) {
            super(itemDoseBinding.getRoot());
            this.itemDoseBinding = itemDoseBinding;






        }
    }
}
