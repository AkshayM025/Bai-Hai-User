package com.techno.baihai.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.techno.baihai.R;
import com.techno.baihai.listner.FragmentListener;

public class ChatFragment extends Fragment implements FragmentListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    Context mContext;
    FragmentListener listener;

    View view;
    LinearLayout linear_tabs;
    TextView chat_txtEnquiry, available_txtchatId, accepted_txtchatId;
    FrameLayout frameLayoutchat;
    ImageView iv_back;
    CardView text_card1, text_card2, text_card3;

    public ChatFragment(FragmentListener listener) {
        // Required empty public constructor
        this.listener = listener;
    }

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContext = getActivity();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        view = inflater.inflate(R.layout.fragment_chat, container, false);


        frameLayoutchat = view.findViewById(R.id.frameContainerchat);

        loadFragment(new ChatEnquiryFragment(this));


        text_card1 = view.findViewById(R.id.text_card1);
        text_card2 = view.findViewById(R.id.text_card2);
        text_card3 = view.findViewById(R.id.text_card3);

        chat_txtEnquiry = view.findViewById(R.id.chat_txtEnquiry);
        accepted_txtchatId = view.findViewById(R.id.accepted_txtchatId);
        available_txtchatId = view.findViewById(R.id.available_txtchatId);


        // adding a listner
        chat_txtEnquiry.setOnClickListener(this);
        available_txtchatId.setOnClickListener(this);
        accepted_txtchatId.setOnClickListener(this);


        return view;
    }


    private void loadFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction().replace(R.id.frameContainerchat, fragment).commit();

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.chat_txtEnquiry:

                // text_card1.setBackgroundColor(Color.parseColor("#257712"));
                text_card1.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
                text_card2.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorLightGray));
                text_card3.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorLightGray));
                chat_txtEnquiry.setTextColor(Color.parseColor("#FFFFFF"));
                accepted_txtchatId.setTextColor(Color.parseColor("#000000"));
                available_txtchatId.setTextColor(Color.parseColor("#000000"));

                loadFragment(new ChatEnquiryFragment(this));


                break;


            case R.id.accepted_txtchatId:

                text_card1.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorLightGray));
                text_card2.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
                text_card3.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorLightGray));
                chat_txtEnquiry.setTextColor(Color.parseColor("#000000"));
                accepted_txtchatId.setTextColor(Color.parseColor("#FFFFFF"));
                available_txtchatId.setTextColor(Color.parseColor("#000000"));


                loadFragment(new AcceptedChatListFragment(this));


                break;

            case R.id.available_txtchatId:

                text_card1.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorLightGray));
                text_card2.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorLightGray));
                text_card3.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
                chat_txtEnquiry.setTextColor(Color.parseColor("#000000"));
                available_txtchatId.setTextColor(Color.parseColor("#FFFFFF"));
                accepted_txtchatId.setTextColor(Color.parseColor("#000000"));


                loadFragment(new AvailableChatListFragment(this));


                break;

        }

    }

    @Override
    public void click(Fragment fragment) {

        loadFragment(fragment);


    }

    @Override
    public void onRefresh() {
        loadFragment(new ChatEnquiryFragment(this));
    }
}
