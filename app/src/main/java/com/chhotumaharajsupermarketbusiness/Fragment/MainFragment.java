package com.chhotumaharajsupermarketbusiness.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


import com.chhotumaharajsupermarketbusiness.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainFragment extends Fragment implements View.OnClickListener {

    public static View fragment;
    LinearLayout linearLayout, linearLayoutl;
    Button view_more, topic_wise, question_wise, book_appointment;
    ProgressDialog progressDialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.activity_main, container, false);

        progressDialog = new ProgressDialog(getActivity());

        linearLayout = fragment.findViewById(R.id.layout);
        linearLayoutl = fragment.findViewById(R.id.layout1);

        view_more = fragment.findViewById(R.id.view_more);
        topic_wise = fragment.findViewById(R.id.topic_wise);
        question_wise = fragment.findViewById(R.id.question_wise);
        book_appointment = fragment.findViewById(R.id.book_appointment);

        view_more.setOnClickListener(this);
        topic_wise.setOnClickListener(this);
        question_wise.setOnClickListener(this);
        book_appointment.setOnClickListener(this);

        return fragment;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.view_more:
                linearLayout.setVisibility(View.GONE);
                linearLayoutl.setVisibility(View.VISIBLE);
                break;

            case R.id.topic_wise:
                TopicWiseFragment topicWiseFragment = new TopicWiseFragment();
                Bundle args = new Bundle();
                args.putString("topic", "topic");
                topicWiseFragment.setArguments(args);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_container, topicWiseFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;

            case R.id.question_wise:
                TopicWiseFragment topicWiseFragment1 = new TopicWiseFragment();
                Bundle args1 = new Bundle();
                args1.putString("topic", "question");
                topicWiseFragment1.setArguments(args1);
                FragmentManager fragmentManager1 = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction1 = fragmentManager1.beginTransaction();
                transaction1.replace(R.id.frame_container, topicWiseFragment1);
                transaction1.addToBackStack(null);
                transaction1.commit();
                break;

            case R.id.book_appointment:
                LiveDemoFragment liveDemoFragment = new LiveDemoFragment();
                FragmentManager fragmentManager2 = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction2 = fragmentManager2.beginTransaction();
                transaction2.replace(R.id.frame_container, liveDemoFragment);
                transaction2.addToBackStack(null);
                transaction2.commit();
                break;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        System.exit(0);
                        //   Toast.makeText(getActivity(), "Your response is required", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                return false;
            }
        });


    }


}
