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
//                Glide.with(context).load(list.get(position).getSender_image()).
//                        error(R.drawable.profile_img).into(holder.right_chat_image);
                Log.e("sender:",list.get(position).getSender_image());

            } else {
                holder.chat_left_msg_layout.setVisibility(View.VISIBLE);
                holder.chat_right_msg_layout.setVisibility(View.GONE);
                holder.chat_left_msg_text_view.setText(list.get(position).getChat_message());
                holder.tv_time_left.setText(list.get(position).getTime());
//                Glide.with(context).load(list.get(position).getReceiver_image()).
//                        error(R.drawable.profile_img).into(holder.left_chat_image);
            }

         /*   if (list.get(position).getSender_id().equals(uid)) {
                holder.chat_right_img_msg_layout.setVisibility(View.VISIBLE);
                holder.chat_left_img_msg_layout.setVisibility(View.GONE);
                holder.chat_right_msg_text_view.setText(list.get(position).getChat_message());
                holder.tv_time_right_img.setText(list.get(position).getTime());


                if(!list.get(position).getChat_document().equals("")) {
                    holder.chat_right_msg_text_view_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ImageActivity.class);
                            intent.putExtra("image", list.get(position).getChat_document());
                            context.startActivity(intent);
                        }
                    });
                }else if(!list.get(position).getChat_audio().equals("")) {
                    holder.chat_right_msg_text_view_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ImageActivity.class);
                            intent.putExtra("image", list.get(position).getChat_audio());
                            context.startActivity(intent);
                        }
                    });
                }else if(!list.get(position).getChat_video().equals("")) {
                    holder.chat_right_msg_text_view_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ImageActivity.class);
                            intent.putExtra("image", list.get(position).getChat_video());
                            context.startActivity(intent);
                        }
                    });
                }else{

                }


                if(list.get(position).getChat_image().equals("")){
                    holder.iv_chat_right.setVisibility(View.GONE);
                    LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    holder.chat_right_msg_text_view_img.setLayoutParams(textParam);

                }else{
                    holder.iv_chat_right.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams
                            (200, LinearLayout.LayoutParams.WRAP_CONTENT);

                    holder.chat_right_msg_text_view_img.setLayoutParams(textParam);

                    Glide.with(context)
                            .load(list.get(position).getChat_image())
//                            .error(R.drawable.ellipse)
//                            .listener(new RequestListener<Drawable>() {
//                                @Override
//                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                    // log exception
//                                    holder.iv_chat_right.setVisibility(View.GONE);
//                                    LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams
//                                            (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//                                    holder.chat_right_msg_text_view_img.setLayoutParams(textParam);
//
//                                    Log.e("TAG", "Error loading image", e);
//                                    return false; // important to return false so the error placeholder can be placed
//                                }
//
//                                @Override
//                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                    holder.iv_chat_right.setVisibility(View.VISIBLE);
//                                    LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams
//                                            (200, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//                                    holder.chat_right_msg_text_view_img.setLayoutParams(textParam);
////                            holder.chat_right_msg_text_view_img.setWidth(200);
//                                    return false;
//                                }
//                            })
                            .into(holder.iv_chat_right);
                    holder.iv_chat_right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ImageActivity.class);
                            intent.putExtra("image", list.get(position).getChat_image());
                            context.startActivity(intent);
                        }
                    });
                }

            } else {
                holder.chat_left_img_msg_layout.setVisibility(View.VISIBLE);
                holder.chat_right_img_msg_layout.setVisibility(View.GONE);
                holder.chat_left_msg_text_view_img.setText(list.get(position).getChat_message());
                holder.tv_time_left_img.setText(list.get(position).getTime());


                if(!list.get(position).getChat_document().equals("")) {
                    holder.chat_right_img_msg_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ImageActivity.class);
                            intent.putExtra("image", list.get(position).getChat_document());
                            context.startActivity(intent);
                        }
                    });
                }else if(!list.get(position).getChat_audio().equals("")) {
                    holder.chat_right_img_msg_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ImageActivity.class);
                            intent.putExtra("image", list.get(position).getChat_audio());
                            context.startActivity(intent);
                        }
                    });
                }else if(!list.get(position).getChat_video().equals("")) {
                    holder.chat_right_img_msg_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ImageActivity.class);
                            intent.putExtra("image", list.get(position).getChat_video());
                            context.startActivity(intent);
                        }
                    });
                }else{

                }


                if(list.get(position).getChat_image().equals("")) {
                    holder.iv_chat_Left.setVisibility(View.GONE);
                    LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    holder.chat_left_msg_text_view_img.setLayoutParams(textParam);
                }else{
                        holder.iv_chat_Left.setVisibility(View.VISIBLE);
                        LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams
                                (200, LinearLayout.LayoutParams.WRAP_CONTENT);

                        holder.chat_left_msg_text_view_img.setLayoutParams(textParam);
                        Glide.with(context)
                                .load(list.get(position).getChat_image())
//                            .error(R.drawable.camera)
//                                .listener(new RequestListener<Drawable>() {
//                                    @Override
//                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                        // log exception
//                                        holder.iv_chat_Left.setVisibility(View.GONE);
//                                        LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams
//                                                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//                                        holder.chat_left_msg_text_view_img.setLayoutParams(textParam);
//                                        Log.e("TAG", "Error loading image", e);
//                                        return false; // important to return false so the error placeholder can be placed
//                                    }
//
//                                    @Override
//                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                        holder.iv_chat_Left.setVisibility(View.VISIBLE);
//                                        LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams
//                                                (200, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//                                        holder.chat_left_msg_text_view_img.setLayoutParams(textParam);
////                            holder.chat_left_msg_text_view_img.setWidth(200);
//                                        return true;
//                                    }
//                                })
                                .into(holder.iv_chat_Left);
                        holder.iv_chat_Left.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, ImageActivity.class);
                                intent.putExtra("image", list.get(position).getChat_image());
                                context.startActivity(intent);
                            }
                        });
                    holder.chat_left_msg_text_view_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ImageActivity.class);
                            intent.putExtra("image", list.get(position).getChat_image());
                            context.startActivity(intent);
                        }
                    });
                }
            }*/
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

    /*   boolean isimage=false;
       private boolean isImage(String url){

           new Thread(new Runnable() { // if already doing the checking on network thread then no need to add this thread
               @Override
               public void run() {
                   try {
                       URLConnection connection = new URL(url).openConnection();
                       String contentType = connection.getHeaderField("Content-Type");
                       isimage = contentType.startsWith("image/"); //true if image
                       Log.i("IS IMAGE", "" + isimage);


                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
           }).start();
           return isimage;
       }
   */
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
            left_chat_image= view.findViewById(R.id.left_chat_image);
            chat_left_msg_text_view = view.findViewById(R.id.chat_left_msg_text_view);

            tv_time_right = view.findViewById(R.id.tv_time_right);
            chat_right_msg_layout = view.findViewById(R.id.chat_right_msg_layout);
            right_chat_image= view.findViewById(R.id.right_chat_image);
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


//    public static void openFile(Context context, File url) throws IOException {
//        // Create URI
//        File file=url;
//        Uri uri = Uri.fromFile(file);
//
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        // Check what kind of file you are trying to open, by comparing the url with extensions.
//        // When the if condition is matched, plugin sets the correct intent (mime) type,
//        // so Android knew what application to use to open the file
//        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
//            // Word document
//            intent.setDataAndType(uri, "application/msword");
//        } else if(url.toString().contains(".pdf")) {
//            // PDF file
//            intent.setDataAndType(uri, "application/pdf");
//        } else if(url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
//            // Powerpoint file
//            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
//        } else if(url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
//            // Excel file
//            intent.setDataAndType(uri, "application/vnd.ms-excel");
//        } else if(url.toString().contains(".zip") || url.toString().contains(".rar")) {
//            // ZIP Files
//            intent.setDataAndType(uri, "application/zip");
////            // WAV audio file
////            intent.setDataAndType(uri, "application/x-wav");
//        } else if(url.toString().contains(".rtf")) {
//            // RTF file
//            intent.setDataAndType(uri, "application/rtf");
//        } else if(url.toString().contains(".wav") || url.toString().contains(".mp3")) {
//            // WAV audio file
//            intent.setDataAndType(uri, "audio/x-wav");
//        } else if(url.toString().contains(".gif")) {
//            // GIF file
//            intent.setDataAndType(uri, "image/gif");
//        } else if(url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
//            // JPG file
//            intent.setDataAndType(uri, "image/jpeg");
//        } else if(url.toString().contains(".txt")) {
//            // Text file
//            intent.setDataAndType(uri, "text/plain");
//        } else if(url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
//            // Video files
//            intent.setDataAndType(uri, "video/*");
//        } else {
//            //if you want you can also define the intent type for any other file
//
//            //additionally use else clause below, to manage other unknown extensions
//            //in this case, Android will show all applications installed on the device
//            //so you can choose which application to use
//            intent.setDataAndType(uri, "*/*");
//        }
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//    }

}
