package com.techno.baihai.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.techno.baihai.R;


public class CustomSnakbar {

    public static void showSnakabar(Context context, View v, String msg) {

        Snackbar snackbar;
        snackbar = Snackbar.make(v, msg, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(context.getResources().getColor(R.color.colorsnack));
        TextView textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(context.getResources().getColor(R.color.colorGreen));
        textView.setGravity(Gravity.CENTER);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        snackbar.getView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        snackbar.show();
    }

    public static void showDarkSnakabar(Context context, View v, String msg) {

        Snackbar snackbar;
        snackbar = Snackbar.make(v, msg, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));
        TextView textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(context.getResources().getColor(R.color.colorWhite));
        textView.setGravity(Gravity.CENTER);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        snackbar.getView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        snackbar.show();
    }

}
