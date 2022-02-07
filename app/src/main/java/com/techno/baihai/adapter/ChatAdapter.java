package com.techno.baihai.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.techno.baihai.R;
import com.techno.baihai.model.ChatModel;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.PrefManager;
import com.techno.baihai.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyView> {

    Context context;
    private List<ChatModel> list = new ArrayList<>();


    public ChatAdapter(Context context) {
        this.context = context;
    }

    public void setChatData(List<ChatModel> list) {
        this.list = list;
    }

    @NotNull
    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat, parent, false);

        return new MyView(itemView);
    }


    @Override
    public void onBindViewHolder(final MyView holder, final int position) {


        User user = PrefManager.getInstance(context).getUser();
        String uid = String.valueOf(user.getId());
        Log.e("red_ID", "-------->" + uid);


        try {

            holder.tv_date.setText(list.get(position).getDate().equals(new Utils(context).getCurrentDate()) ?
                    "Today" : list.get(position).getDate());
            if (position > 0) {
                if (list.get(position).getDate().equalsIgnoreCase(list.get(position - 1).getDate())) {
                    holder.tv_date.setVisibility(View.GONE);
                } else {
                    holder.tv_date.setVisibility(View.VISIBLE);
                }
            } else {
                holder.tv_date.setVisibility(View.VISIBLE);
            }

            if (list.get(position).getSender_id().equals(uid)) {
                holder.chat_right_msg_layout.setVisibility(View.VISIBLE);
                holder.chat_left_msg_layout.setVisibility(View.GONE);
                holder.chat_right_msg_text_view.setText(list.get(position).getChat_message());
                holder.tv_time_right.setText(list.get(position).getTime());
                Log.e("sender:", list.get(position).getSender_image());

            } else {
                holder.chat_left_msg_layout.setVisibility(View.VISIBLE);
                holder.chat_right_msg_layout.setVisibility(View.GONE);
                holder.chat_left_msg_text_view.setText(list.get(position).getChat_message());
                holder.tv_time_left.setText(list.get(position).getTime());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (list == null) {

            return 0;
        } else {
            return list.size();
        }
    }


    class MyView extends RecyclerView.ViewHolder {

        private final TextView tv_date;

        private final TextView tv_time_left;
        private final RelativeLayout chat_left_msg_layout;
        private final TextView chat_left_msg_text_view;

        private final TextView tv_time_right;
        private final RelativeLayout chat_right_msg_layout;
        private final TextView chat_right_msg_text_view;

        private final RelativeLayout chat_left_img_msg_layout;
        private final ImageView iv_chat_Left;
        private final TextView chat_left_msg_text_view_img;
        private final TextView tv_time_left_img;

        private final RelativeLayout chat_right_img_msg_layout;
        private final ImageView iv_chat_right;
        private final TextView chat_right_msg_text_view_img;
        private final TextView tv_time_right_img;
        private final CircleImageView left_chat_image;
        private final CircleImageView right_chat_image;


        private MyView(View view) {
            super(view);

            tv_date = view.findViewById(R.id.tv_date);

            tv_time_left = view.findViewById(R.id.tv_time_left);
            chat_left_msg_layout = view.findViewById(R.id.chat_left_msg_layout);
            left_chat_image = view.findViewById(R.id.left_chat_image);
            chat_left_msg_text_view = view.findViewById(R.id.chat_left_msg_text_view);

            tv_time_right = view.findViewById(R.id.tv_time_right);
            chat_right_msg_layout = view.findViewById(R.id.chat_right_msg_layout);
            right_chat_image = view.findViewById(R.id.right_chat_image);
            chat_right_msg_text_view = view.findViewById(R.id.chat_right_msg_text_view);

            //==========================================================

            chat_left_img_msg_layout = view.findViewById(R.id.chat_left_img_msg_layout);
            iv_chat_Left = view.findViewById(R.id.iv_chat_Left);
            chat_left_msg_text_view_img = view.findViewById(R.id.chat_left_msg_text_view_img);
            tv_time_left_img = view.findViewById(R.id.tv_time_left_img);

            chat_right_img_msg_layout = view.findViewById(R.id.chat_right_img_msg_layout);
            iv_chat_right = view.findViewById(R.id.iv_chat_right);
            chat_right_msg_text_view_img = view.findViewById(R.id.chat_right_msg_text_view_img);
            tv_time_right_img = view.findViewById(R.id.tv_time_right_img);

        }
    }


}
