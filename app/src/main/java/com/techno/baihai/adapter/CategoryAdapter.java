package com.techno.baihai.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.techno.baihai.R;
import com.techno.baihai.activity.MyProductListActivity;
import com.techno.baihai.model.CategoryList;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final Context context;
    private final List<CategoryList> categoryLists;


    public CategoryAdapter(Context context, List<CategoryList> categoryList) {
        this.context = context;
        this.categoryLists = categoryList;
    }


    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_item, parent, false);
        CategoryAdapter.ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    public void updateData(ArrayList<CategoryList> viewModels) {
        categoryLists.clear();
        categoryLists.addAll(viewModels);
        notifyDataSetChanged();
    }

    public void addItem(int position, CategoryList viewModel) {
        categoryLists.add(position, viewModel);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        categoryLists.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        holder.itemView.setTag(categoryLists.get(position));

        CategoryList pu = categoryLists.get(position);
        Glide.with(context)
                .load(pu.getCategory_imageurl())
                .into(holder.category_Icon);

        holder.category_name.setText(pu.getCategory_name());

        holder.category_Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                if (position == 0) {
                    intent = new Intent(context, MyProductListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("categoryId", categoryLists.get(position).getCategory_id());
                    intent.putExtra("categoryImage", categoryLists.get(position).getCategory_imageurl());
                    intent.putExtra("categoryName", categoryLists.get(position).getCategory_name());
                    context.startActivity(intent);
                } else {
                    intent = new Intent(context, MyProductListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("categoryId", categoryLists.get(position).getCategory_id());
                    intent.putExtra("categoryImage", categoryLists.get(position).getCategory_imageurl());
                    intent.putExtra("categoryName", categoryLists.get(position).getCategory_name());
                    //  Toast.makeText(context, "cate"+String.valueOf(categoryLists.get(position).getCategory_id()), Toast.LENGTH_SHORT).show();

                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (categoryLists == null) {

            return 0;
        } else {
            return categoryLists.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ImageView category_Icon;
        public TextView category_name;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            category_Icon = itemView.findViewById(R.id.category_IconId);
            category_name = itemView.findViewById(R.id.Category_nameId);


        }
    }
}
