package com.techno.baihai.utils;

import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by rakesh on 16/5/17.
 */
public class AlertConnection {
    private static Context mCtx;

    private AlertConnection(Context context) {
        mCtx = context;
    }


    public static void showAlertDialog(Context context, String title, String message,
                                       Boolean status) {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
//        alertDialog.setIcon((status) ? R.drawable.logo
//                : R.drawable.logo);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

}
