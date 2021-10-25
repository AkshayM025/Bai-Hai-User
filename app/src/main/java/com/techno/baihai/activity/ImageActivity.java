package com.techno.baihai.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.bumptech.glide.Glide;
import com.techno.baihai.R;

import java.io.File;

public class ImageActivity extends AppCompatActivity {
    ImageView image_view;
    WebView web_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_image);

        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (web_view != null)
                    web_view.loadUrl("");
                finish();
            }
        });
        web_view = findViewById(R.id.web_view);
        image_view = findViewById(R.id.image_view);

        if (getIntent().getStringExtra("image").contains(".jpg") ||
                getIntent().getStringExtra("image").contains(".jpeg") ||
                getIntent().getStringExtra("image").contains(".png")) {

            image_view.setVisibility(View.VISIBLE);
            web_view.setVisibility(View.GONE);

        } else {

            image_view.setVisibility(View.GONE);
            web_view.setVisibility(View.VISIBLE);
        }


        web_view.requestFocus();
        web_view.getSettings().setLightTouchEnabled(true);
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.getSettings().setGeolocationEnabled(true);
//        web_view.getSettings().setBuiltInZoomControls(true);

//        web_view.getSettings().setPluginsEnabled(true);
        web_view.getSettings().setAllowFileAccess(true);
        web_view.setSoundEffectsEnabled(true);
//        web_view.loadData(getIntent().getStringExtra("description"),
//                "text/html", "UTF-8");


        web_view.getSettings().setPluginState(WebSettings.PluginState.ON);
        web_view.setWebViewClient(new WebViewClient());

        if (getIntent().getStringExtra("image").contains(".pdf")) {
            web_view.loadUrl("https://docs.google.com/viewer?embedded=true&url=" + getIntent().getStringExtra("image"));
        } else if (checkFileToUpload(getIntent().getStringExtra("image")).equalsIgnoreCase("document")) {
            String url = getIntent().getStringExtra("image");
            url = url.replaceAll(" ", "%20");
            String newUA = "Chrome/43.0.2357.65 ";
            web_view.getSettings().setUserAgentString(newUA);
            web_view.loadUrl("https://view.officeapps.live.com/op/view.aspx?src=" + url);
//            String doc="<iframe src='http://docs.google.com/viewer?embedded=trueurl='"+getIntent().getStringExtra("image")+"'width='100%' height='100%'style='border: none;'></iframe>";
////            web_view.loadUrl(doc);
//            web_view.loadData( doc, "text/html",  "UTF-8");
        } else {
//            String url =getIntent().getStringExtra("image");
//            url=url.replaceAll(" ","%20");
//            String newUA= "Chrome/43.0.2357.65 ";
//            web_view.getSettings().setUserAgentString(newUA);
//            web_view.loadUrl("https://view.officeapps.live.com/op/view.aspx?src="+url);
//            web_view.loadUrl(getIntent().getStringExtra("image"));
//            openFile(getIntent().getStringExtra("image"));
            web_view.loadUrl(getIntent().getStringExtra("image"));

//            String fileUrl = getIntent().getStringExtra("image");//"https://cs.wmich.edu/elise/courses/cs526/Android-tutorial.docx";
//            String doc="<iframe src='http://docs.google.com/gview?embedded=true&url="+fileUrl+"' width='100%' height='100%' style='border: none;'></iframe>";
//            web_view.getSettings().setJavaScriptEnabled(true);
//            web_view.loadData( doc , "text/html",  "UTF-8");
//            web_view.getSettings().setJavaScriptEnabled(true);
//            web_view.loadUrl("https://drive.google.com/gview?embedded=true&url=" + getIntent().getStringExtra("image"));
        }


        Glide.with(ImageActivity.this)
                .load(getIntent().getStringExtra("image"))
                .into(image_view);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (web_view != null)
            web_view.loadUrl(""); // clear history
        finish();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (web_view != null)
            web_view.loadUrl("");
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (web_view != null)
            web_view.loadUrl("");
        finish();
    }


    //    @Override
//    protected void onPause() {
//        super.onPause();
//        if(web_view!=null)
//            web_view.clearHistory(); // clear history
//    }

    private String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
    }

    String checkFileToUpload(String url) {
        if (url.contains(".doc") || url.contains(".docx")) {
            // Word document
//            intent.setDataAndType(uri, "application/msword");
            return "document";
        } else if (url.contains(".pdf")) {
            // PDF file
//            intent.setDataAndType(uri, "application/pdf");
            return "document";
        } else if (url.contains(".ppt") || url.contains(".pptx")) {
            // Powerpoint file
//            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            return "document";
        } else if (url.contains(".xls") || url.contains(".xlsx")) {
            // Excel file
//            intent.setDataAndType(uri, "application/vnd.ms-excel");
            return "document";
        } else if (url.contains(".zip")) {
            // ZIP file
//            intent.setDataAndType(uri, "application/zip");
            return "document";
        } else if (url.contains(".rar")) {
            // RAR file
//            intent.setDataAndType(uri, "application/x-rar-compressed");
            return "document";
        } else if (url.contains(".rtf")) {
            // RTF file
//            intent.setDataAndType(uri, "application/rtf");
            return "document";
        } else if (url.contains(".wav") || url.contains(".mp3")) {
            // WAV audio file
//            intent.setDataAndType(uri, "audio/x-wav");
            return "audio";
        } else if (url.contains(".gif")) {
            // GIF file
//            intent.setDataAndType(uri, "image/gif");
            return "document";
        } else if (url.contains(".jpg") || url.contains(".jpeg") || url.contains(".png")) {
            // JPG file
//            intent.setDataAndType(uri, "image/jpeg");
            return "image";
        } else if (url.contains(".txt")) {
            // Text file
//            intent.setDataAndType(uri, "text/plain");
            return "document";
        } else if (url.contains(".3gp") || url.contains(".mpg") ||
                url.contains(".mpeg") || url.contains(".mpe") || url.contains(".mp4") || url.contains(".avi")) {
            // Video files
//            intent.setDataAndType(uri, "video/*");
            return "video";
        } else {
//            intent.setDataAndType(uri, "*/*");
            return "document";
        }
    }

    public void openFile(File url) {

        Uri uri = Uri.fromFile(url);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if (url.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        } else if (url.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if (url.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if (url.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            //if you want you can also define the intent type for any other file
            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            intent.setDataAndType(uri, "*/*");
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}
