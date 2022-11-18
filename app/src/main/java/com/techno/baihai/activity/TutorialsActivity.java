package com.techno.baihai.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.marozzi.roundbutton.RoundButton;
import com.techno.baihai.R;
import com.techno.baihai.adapter.TutorialListAdapter;
import com.techno.baihai.api.Constant;
import com.techno.baihai.listner.tutorialListener;
import com.techno.baihai.model.TutorialsModelList;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import www.develpoeramit.mapicall.ApiCallBuilder;

public class TutorialsActivity extends AppCompatActivity implements tutorialListener {

    private final String TAG = "TutorialsActivity";

    ImageView iv_back;
    Context mContext = this;

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    MediaController mediaController;
    List<TutorialsModelList> tutorialsModelLists;
    SwipeRefreshLayout swipLayout;
    private String request_id;
    private WebView webView;

    String videoUrl = "";
    TextView tv_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_tutorials);


        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        User user = PrefManager.getInstance(this).getUser();
        String uid = String.valueOf(user.getId());

        PrefManager.save(mContext, "uid", uid);

        recyclerView = findViewById(R.id.recycleViewTutorials);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

       tutorialsModelLists = new ArrayList<>();
        tv_cancel = findViewById(R.id.tv_cancel);

        //tv_cancel.setOnClickListener(v -> finish());


        swipLayout = findViewById(R.id.swipLayout);



        swipLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetTutorials();
            }
        });




        GetTutorials();




        // handlerMethod();

    }

    private void GetTutorials() {



        String langua = "EN";
        String lang = PrefManager.get(mContext, "lang");
        if (lang.equals("es") && lang != null) {
            langua = "ES";
        }

        HashMap<String, String> param = new HashMap<>();
        param.put("language", langua);
        ApiCallBuilder.build(mContext)
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "get_tutorials?") //http://bai-hai.com/webservice/get_category
                .setParam(param)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        Log.e("Response=>", "" + response);
                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.optString("status");
                            String message = object.optString("message");
                            if (status.equals("1")) {


                                try {



                                        JSONArray result = object.optJSONArray("result");
                                        //   Log.e(TAG, "result=>" + result);

                                        tutorialsModelLists.clear();


                                        if (result != null) {
                                            for (int i = 0; i < result.length(); i++) {

                                                JSONObject object1 = result.getJSONObject(i);


                                                Log.e(TAG, "resulti=>" + i);
                                                String tutorial_id = object1.optString("id");
                                                String tutorial_name = object1.optString("name");
                                                String tutorial_url = object1.optString("url");
                                                String tutorial_status = object1.optString("status");
                                                String tutorial_language = object1.optString("language");


                                                tutorialsModelLists.add(new TutorialsModelList(tutorial_id, tutorial_name, tutorial_status, tutorial_language,tutorial_url));


                                            }
                                        }





                                } catch (Exception e) {

                                    e.printStackTrace();
                                }
                            } else {

                                Toast.makeText(mContext, "Status" + message, Toast.LENGTH_SHORT).show();
                                swipLayout.setRefreshing(false);
                            }
                            mAdapter = new TutorialListAdapter(getApplicationContext(), tutorialsModelLists, TutorialsActivity.this);
                            recyclerView.removeAllViews();
                            recyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            swipLayout.setRefreshing(false);
                            Log.i(TAG, "errorL", e);
                            e.printStackTrace();

                        }


                    }

                    @Override
                    public void Failed(String error) {
                        swipLayout.setRefreshing(false);
                        //CustomSnakbar.showDarkSnakabar(mContext, mview, "" + error);
                        // Toast.makeText(mContext, "Error" + error, Toast.LENGTH_SHORT).show();
                    }
                });


    }

    @Override
    public void click(String id) {
         setLog("Se solicita informacion de faq"+id+"");
        GetTutorialResultApi(id);

    }

    private void GetTutorialResultApi(String tutorial_id) {


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        String langua = "EN";
        String lang = PrefManager.get(mContext, "lang");
        if (lang.equals("es") && lang != null) {
            langua = "ES";
        }


        HashMap<String, String> parms = new HashMap<>();
        parms.put("tutorial_id", tutorial_id);
        parms.put("language", langua);

        ApiCallBuilder.build(this)
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "get_tutorial?")
                .setParam(parms)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        progressDialog.dismiss();
                        swipLayout.setRefreshing(false);

                        Log.e("tutorial_response", String.valueOf(response));


                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status");
                            Log.d(TAG, "STATUS_:" + status);
                            try {


                                if (status.equals("1")) {
                                    String url="";
                                    JSONArray result = object.optJSONArray("result");
                                    if (result != null) {
                                        for (int i = 0; i < result.length(); i++) {
                                            JSONObject object1 = result.getJSONObject(i);
                                             url = object1.getString("url");
                                            Log.d("url", url);
                                        }
                                    }

                                    final Dialog dialog = new Dialog(mContext);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setCancelable(false);
                                    dialog.setContentView(R.layout.dialog_tutorials);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));





                                    final RoundButton bt = (RoundButton)dialog.findViewById(R.id.bt_success);
                                    bt.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            bt.startAnimation();
                                            bt.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    bt.setResultState(RoundButton.ResultState.SUCCESS);
                                                }
                                            }, 1000);
                                            bt.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    dialog.dismiss();
                                                    // bt.revertAnimation();
                                                }
                                            }, 2000);

                                        }
                                    });
                                    String youTubeUrl=url;
                                    String frameVideo = "<html><body>Tutorials<br><iframe width=\"280\" height=\"300\" " +
                                            "src='" + youTubeUrl + "' frameborder=\"0\" allowfullscreen>" +
                                            "</iframe></body></html>";
                                    webView= dialog.findViewById(R.id.webViewVideo);
                                    String regexYoutUbe = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+";
                                    if (youTubeUrl.matches(regexYoutUbe)) {
                                        webView.setWebViewClient(new WebViewClient() {
                                            @Override
                                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                                return false;
                                            }
                                        });
                                        WebSettings webSettings = webView.getSettings();
                                        webSettings.setJavaScriptEnabled(true);
                                        webView.loadData(frameVideo, "text/html", "utf-8");
                                    }else {
                                        Toast.makeText(TutorialsActivity.this, "This is other video",
                                                Toast.LENGTH_SHORT).show();
                                    }


                                    dialog.show();
                                    /*videoUrl="https://bai-hai.com/uploads/video/tutorials/"+url;
                                    Uri videoUri = Uri.parse(videoUrl);
                                    final VideoView video_player_view = (VideoView) dialog.findViewById(R.id.videoviewt);

                                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                    lp.copyFrom(dialog.getWindow().getAttributes());
                                    dialog.getWindow().setAttributes(lp);

                                    video_player_view.setVideoURI(videoUri);
                                    video_player_view.setMediaController(new MediaController(mContext));
                                    video_player_view.start();
                                    video_player_view.requestFocus();
                                    progressDialog.show();

                                    video_player_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                                        public void onPrepared(MediaPlayer mp) {
                                            // TODO Auto-generated method stub
                                            progressDialog.dismiss();
                                        }
                                    });*/




                                } else if (status.equals("0")) {

                                    progressDialog.dismiss();
                                    swipLayout.setRefreshing(false);
                                  }
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }


                        } catch (JSONException e) {

                            progressDialog.dismiss();
                            swipLayout.setRefreshing(false);
                            Log.i(TAG, "errorL", e);
                            //Toast.makeText(mContext, "Check Your Network "+e, Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void Failed(String error) {
                        progressDialog.dismiss();
                        swipLayout.setRefreshing(false);
                        //Toast.makeText(mContext, "Check Your Connection " + error, Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void setLog(String message) {
        User user = PrefManager.getInstance(mContext).getUser();
        String id = null;
        if (user.getId() == "") {
            id = "1";
        } else {
            id = user.getId();
        }

        HashMap<String, String> parms1 = new HashMap<>();
        parms1.put("user_id", id);
        parms1.put("activity", message);
        ApiCallBuilder.build(mContext)
                .setUrl(Constant.BASE_URL + Constant.LOG_APP)
                .setParam(parms1)
                .execute(new ApiCallBuilder.onResponse() {
                    public void Success(String response) {
                        try {
                            Log.e("selectedresponse=>", "-------->" + response);
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status");
                            if(status.equals("true")){
                                Log.e("selectedresponse=>", "-------->exitoso" );
                            }


                        } catch (JSONException e) {


                            //Toast.makeText(mContext, "Error:" + e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }

                    public void Failed(String error) {
                    }
                });
    }



}
