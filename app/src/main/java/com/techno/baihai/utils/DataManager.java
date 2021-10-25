package com.techno.baihai.utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.techno.baihai.R;

import ir.alirezabdn.wp7progress.WP10ProgressBar;


public class DataManager {


    private static final DataManager ourInstance = new DataManager();
    WP10ProgressBar progressBar;
    private Dialog mDialog;
    private boolean isProgressDialogRunning = false;

    private DataManager() {
    }

    public static DataManager getInstance() {
        return ourInstance;
    }

    public void showProgressMessage(Activity dialogActivity, String msg) {
        try {
            if (isProgressDialogRunning) {
                hideProgressMessage();
            }
            isProgressDialogRunning = true;
            mDialog = new Dialog(dialogActivity);
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialog.setContentView(R.layout.dialog_loading);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            TextView textView = mDialog.findViewById(R.id.textView);
            progressBar = mDialog.findViewById(R.id.progressBar);
            textView.setText(msg);
            WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
            lp.dimAmount = 0.8f;
            progressBar.showProgressBar();
            mDialog.getWindow().setAttributes(lp);
            mDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void hideProgressMessage() {
        isProgressDialogRunning = true;
        try {
            if (mDialog != null) {
                mDialog.dismiss();
                progressBar.hideProgressBar();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}
