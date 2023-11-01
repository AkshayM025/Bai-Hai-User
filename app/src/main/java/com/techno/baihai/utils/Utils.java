package com.techno.baihai.utils;

import android.content.Context;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    public static Context context;

    public Utils(Context context) {
        Utils.context = context;
    }

    public static void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public String convertDate(String inputDateStr) throws ParseException {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        Date date = inputFormat.parse(inputDateStr);
        String outputDateStr = outputFormat.format(date);

        return outputDateStr;
    }

    public String convertTime(String date) {
        try {
            SimpleDateFormat spf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            Date newDate = spf.parse(date);
            SimpleDateFormat spf1 = new SimpleDateFormat("hh:mm");
            date = spf1.format(newDate);
            System.out.println(date);
        } catch (Exception e) {
            return "";
        }
        return date;
    }

    public String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        String date = dateFormat.format(calendar.getTime());
        return date;
    }

}
