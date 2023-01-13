package com.techno.baihai;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.techno.baihai.activity.ChattingBotActivity;
import com.techno.baihai.activity.DriverInfoActivity;
import com.techno.baihai.activity.HomeActivity;
import com.techno.baihai.activity.RatingActivity;
import com.techno.baihai.utils.PrefManager;

import org.json.JSONObject;

import java.util.Date;
import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // log the getting message from firebase
        // Log.d(TAG, "From: " + remoteMessage.getFrom());

        //  if remote message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Messagepayload" + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            String jobType = data.get("type");


            /* Check the message contains data If needs to be processed by long running job
               so check if data needs to be processed by long running job */
            handleNow(data);

            if (true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow(data);
            }



        }

        // if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), remoteMessage.getData());
            // Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        // Log.d(TAG, "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // make a own server request here using your http client
    }

    private void scheduleJob() {
        // [START dispatch_job]
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance().beginWith(work).enqueue();
        // [END dispatch_job]
    }

    private void handleNow(Map<String, String> data) {
        sendNotification(data.get("status"), data.get("key"), data);
         /* if (data.containsKey("title") && data.containsKey("message")) {
            sendNotification(data.get("title"), data.get("message"));
        } */
    }

    private void sendNotification(String title, String messageBody, Map<String, String> data) {

        if (PrefManager.getInstance(getApplicationContext()).isLoggedIn()) {

            PrefManager.getInstance(getApplicationContext()).isLoggedIn();

            Log.e("hjasgfasfdsf", "title = " + title);
            Log.e("hjasgfasfdsf", "messageBody = " + messageBody);
            JSONObject object = new JSONObject(data);
            String status = object.optString("status");
            Intent intent;
            PendingIntent pendingIntent = null;
            String chatRequestStatus = object.optString("key4");
            String chatStatus = object.optString("key");
            String message = object.optString("key1");
            String chatStatus1 = object.optString("key2");
            String Chatresult = object.optString("type");


            int requestCode = (int) System.currentTimeMillis();


            String msg = "";
            String driver_id = "";
            String provider_firstname = "";
            String request_id = "";
            String mobile = "";
            String driver_imgUrl;
            String driver_number;
            //chat section---------------
            String receiver_id="";
            String receiver_name="";
            String sender_id="";
            String receiver_image="";
            String product_id="";




            if (status.equals("Rejected")) {
                title = object.optString("title");
                msg =  object.optString("key");
            }

            if (status.equals("Accept")) {
                try {

                    msg = object.getString("key");
                    driver_id = object.getString("driver_id");
                    request_id = object.getString("request_id");
                    driver_imgUrl = object.optString("image");
                    driver_number = object.optString("provider_number");
                    provider_firstname = object.getString("provider_firstname");

                    intent = new Intent(this, DriverInfoActivity.class);

                    intent.putExtra("driver_id", driver_id);
                    intent.putExtra("provider_firstname", provider_firstname);
                    intent.putExtra("request_id", request_id);
                    intent.putExtra("pickup_status", status);
                    intent.putExtra("driver_imgUrl", driver_imgUrl);
                    intent.putExtra("driver_number", driver_number);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                        pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
                                PendingIntent.FLAG_MUTABLE);
                    }else{
                        pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
                                PendingIntent.FLAG_ONE_SHOT);
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            } else if (status.equals("Complete")) {

                try {

                    title = object.optString("key");
                    msg = object.getString("key");//  * http://bai-hai.com/webservice/add_rating?
                    // user_id=1&provider_id=1&request_id=1&review=good%20service&rating=5
                    //driver_id = object.getString("driver_id");
                    request_id = object.getString("request_id");
                    intent = new Intent(this, RatingActivity.class);
                    //intent.putExtra("driver_id", driver_id);
                    intent.putExtra("request_id", request_id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                        pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
                                PendingIntent.FLAG_MUTABLE);
                    }else{
                        pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
                                PendingIntent.FLAG_ONE_SHOT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("excep", String.valueOf(e));

                }


            } else if (chatRequestStatus.equals("You have new chat request")) {

                try {
                    msg = object.optString("key1");
                    title = object.optString("key4");
                    intent = new Intent(this, HomeActivity.class);
                    intent.putExtra("Chatrequest", "1");
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                        pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
                                PendingIntent.FLAG_MUTABLE);
                    }else{
                        pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
                                PendingIntent.FLAG_ONE_SHOT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            } else if (chatStatus1.equals("Accepted")) {

                try {
                    msg = object.getString("key");
                    title = object.optString("message");
                    intent = new Intent(this, HomeActivity.class);
                    intent.putExtra("Chatrequest", "1");

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);


                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                        pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
                                PendingIntent.FLAG_MUTABLE);
                    }else{
                        pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
                                PendingIntent.FLAG_ONE_SHOT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }


            if (Chatresult.equals("insert_chat")) {

                try {
                    title = object.optString("key");
                    msg = object.optString("message");
                    sender_id = object.optString("sender_id");
                    receiver_id = object.optString("receiver_id");
                    receiver_name = object.optString("sender_name");
                    receiver_image = object.optString("sender_image");
                    product_id = object.optString("product_id");



                    //  * http://bai-hai.com/webservice/add_rating?
                    // user_id=1&provider_id=1&request_id=1&review=good%20service&rating=5
                    //driver_id = object.getString("driver_id");
                    //request_id = object.getString("request_id");
                    intent = new Intent(this, ChattingBotActivity.class);
                    intent.putExtra("getSellerId", receiver_id);
                    intent.putExtra("getProductId", product_id);
                    intent.putExtra("getRecieverId", sender_id);
                    intent.putExtra("getChatName", receiver_name);
                    intent.putExtra("getChatImgUrl", receiver_image);


                    intent.putExtra("statusss", "1");





                    //intent.putExtra("driver_id", driver_id);
                    //intent.putExtra("request_id", request_id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                        pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
                                PendingIntent.FLAG_MUTABLE);
                    }else{
                        pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
                                PendingIntent.FLAG_ONE_SHOT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("excep", String.valueOf(e));

                }


            }
            Date currentTime = new Date();
//
            if(messageBody.equals("message user") && (currentTime.getDate()== 16 ) ){
                msg="Necesitas algo o quieres donarlo? \n Es momento de hacerlo en bye-hi :) ";
                title="Donate to Bye - hi";
                intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);


                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                    pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
                            PendingIntent.FLAG_MUTABLE);
                }else{
                    pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
                            PendingIntent.FLAG_ONE_SHOT);
                }
            }

            if(message.equals("Product Aproved")){
                msg=chatStatus;
                title="Product Aproved";
                intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);


                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                    pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
                            PendingIntent.FLAG_MUTABLE);
                }else{
                    pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
                            PendingIntent.FLAG_ONE_SHOT);
                }
            }

            if(message.equals("Fundation Aproved")){
                msg=chatStatus;
                title="Foundation Aproved";
                intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);


                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                    pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
                            PendingIntent.FLAG_MUTABLE);
                }else{
                    pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
                            PendingIntent.FLAG_ONE_SHOT);
                }
            }

            if(messageBody.equals("Please help us  donate products to Bye-Hi")){
                msg="Stop! Don't throw that out! Upload it instead and help others";
                title="Donate Products";
                intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);


                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                    pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
                            PendingIntent.FLAG_MUTABLE);
                }else{
                    pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
                            PendingIntent.FLAG_ONE_SHOT);
                }
            }


if((messageBody.equals("message user") && (currentTime.getDate()== 16 ))
        || Chatresult.equals("insert_chat") ||
        chatStatus1.equals("Accepted")  ||
        status.equals("Complete") ||
        status.equals("Accept") ||
        status.equals("Rejected") || chatRequestStatus.equals("You have new chat request")  ||messageBody.equals("Please help us  donate products to Bye-Hi") ||message.equals("Product Aproved") || message.equals("Fundation Aproved")){
    String channelId = getString(R.string.default_notification_channel_id);
    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
            .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
            .setSmallIcon(R.drawable.noti_icon)
            // .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.noti_icon))
            .setContentTitle(title)
            .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
            .setContentText(msg)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent);





    NotificationManager notificationManager = (NotificationManager)
            getSystemService(Context.NOTIFICATION_SERVICE);
    // Since android Oreo notification channel is needed.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Channel human readable title
        NotificationChannel channel = new NotificationChannel(channelId,
                "Cloud Messaging Service",
                NotificationManager.IMPORTANCE_DEFAULT);

        notificationManager.createNotificationChannel(channel);
    }

    notificationManager.notify(getNotificationId(), notificationBuilder.build());

}

        }

        else {
            //Toast.makeText(this, "You have a new message,Please Login..!!", Toast.LENGTH_SHORT).show();
        }
    }

            private static int getNotificationId () {
                Random rnd = new Random();
                return 100 + rnd.nextInt(900000);
            }



}

