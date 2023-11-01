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
import com.techno.baihai.listner.tutorialListener;
import com.techno.baihai.model.TutorialsModelList;

import java.util.List;


public class TutorialListAdapter extends RecyclerView.Adapter<TutorialListAdapter.ViewHolder> {
    private static final String TAG = "CustomRecyclerAdapter";
    private final Context context;
    private final List<TutorialsModelList> productPickupLists;
    tutorialListener listener;
    AppCompatActivity activity;
    int itemPosition;


    public TutorialListAdapter(Context context, List personUtils, tutorialListener listener) {
        this.context = context;
        this.productPickupLists = personUtils;
        this.listener=listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tutorial_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.itemView.setTag(productPickupLists.get(position));

        TutorialsModelList tutorialModelList = productPickupLists.get(position);
        holder.tutorial_Name.setText(tutorialModelList.getTutorial_id()+" "+tutorialModelList.getTutorial_name());

        holder.tutorial_cardid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0){

                    listener.click(productPickupLists.get(position).getTutorial_id());

                }else{

                    listener.click(productPickupLists.get(position).getTutorial_id());

                }

            }
        });

        }


    @Override
    public int getItemCount() {
        return productPickupLists.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

      public   TextView tutorial_Name;
        CardView tutorial_cardid;
        public ViewHolder(View itemView) {
            super(itemView);

            tutorial_Name=itemView.findViewById(R.id.tutorial_Name);
            tutorial_cardid = itemView.findViewById(R.id.tutorial_cardid);



        }
    }

}
