package com.techno.baihai.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.techno.baihai.R;
import com.techno.baihai.fragment.PersonalDonateFragment;
import com.techno.baihai.listner.FragmentListener;
import com.techno.baihai.model.FoundationsList;

import java.util.List;

public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder> {
    private static final String TAG = "CustomRecyclerAdapter";
    FragmentListener listener;
    AppCompatActivity activity;
    FoundationsList pu;
    int itemPosition;
    private final Context context;
    private final List<FoundationsList> personUtils;


    public CustomRecyclerAdapter(Context context, List personUtils) {
        this.context = context;
        this.personUtils = personUtils;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.foundation_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(personUtils.get(position));

        pu = personUtils.get(position);

        holder.org_name.setText(pu.getOrganizationName());
        holder.contact_name.setText(pu.getContactName());
      //  holder.email.setText(pu.getEmail());
     //   holder.mobile.setText(pu.getMobile());
        holder.location.setText(pu.getLocation());
        holder.description.setText(pu.getDescription());
        holder.foundationCardId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                if (position == 0) {
                    intent = new Intent(context, PersonalDonateFragment.class);
                    intent.putExtra("org_name", String.valueOf(personUtils.get(position).getOrganizationName()));
                    intent.putExtra("org_id",personUtils.get(position).getOrg_id());


                    context.startActivity(intent);
                } else {
                    intent = new Intent(context, PersonalDonateFragment.class);
                    intent.putExtra("org_name", String.valueOf(personUtils.get(position).getOrganizationName()));
                    intent.putExtra("org_id",personUtils.get(position).getOrg_id());
                    context.startActivity(intent);
                }
            }
        });

        //activity = (AppCompatActivity) view.getContext();
        //
        //           // holder.pJobProfile.setText(pu.getContactName());

    }

    @Override
    public int getItemCount() {
        return personUtils.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView org_name;
        public CardView foundationCardId;
        public TextView contact_name, email, mobile, location, description;

        public ViewHolder(View itemView) {
            super(itemView);

            org_name = itemView.findViewById(R.id.org_name);
            contact_name = itemView.findViewById(R.id.contact_name);
            email = itemView.findViewById(R.id.email);
            mobile = itemView.findViewById(R.id.mobile);
            location = itemView.findViewById(R.id.address);
            description = itemView.findViewById(R.id.description);
            foundationCardId = itemView.findViewById(R.id.foundationCardId);


        }
    }

}
