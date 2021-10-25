package com.techno.baihai.utils;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.techno.baihai.R;


public class UiHelper {

    public static void showConfirmationDialog(final Context context, String msg, DialogInterface.OnClickListener positiveButtonListener, boolean isCancelable) {
        new AlertDialog.Builder(context, R.style.AlertDialogStyle)
                .setTitle(R.string.app_name)
                .setMessage(msg)
                .setIcon(R.drawable.logo)
                .setCancelable(isCancelable)
                .setPositiveButton(R.string.ok, positiveButtonListener)
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

}