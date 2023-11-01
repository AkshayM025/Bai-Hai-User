package com.techno.baihai.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.techno.baihai.R;
import com.techno.baihai.listner.FAQListener;
import com.techno.baihai.model.FAQModelList;



import java.util.List;




public class FaqListAdapter extends RecyclerView.Adapter<FaqListAdapter.ViewHolder> {
    private static final String TAG = "CustomRecyclerAdapter";
    private final Context context;
    private final List<FAQModelList> productPickupLists;
    FAQListener listener;
    AppCompatActivity activity;
    int itemPosition;


    public FaqListAdapter(Context context, List personUtils,FAQListener listener) {
        this.context = context;
        this.productPickupLists = personUtils;
        this.listener=listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.itemView.setTag(productPickupLists.get(position));

        FAQModelList faqModelList = productPickupLists.get(position);
        holder.faq_Name.setText(faqModelList.getFaq_id()+" "+faqModelList.getFaq_name());

        holder.faq_cardid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0){

                    listener.click(productPickupLists.get(position).getFaq_id());

                }else{

                    listener.click(productPickupLists.get(position).getFaq_id());

                }

            }
        });

        }


    @Override
    public int getItemCount() {
        return productPickupLists.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

      public   TextView faq_Name;
        CardView faq_cardid;
        public ViewHolder(View itemView) {
            super(itemView);

            faq_Name=itemView.findViewById(R.id.faq_Name);
             faq_cardid = itemView.findViewById(R.id.faq_cardid);



        }
    }

}
