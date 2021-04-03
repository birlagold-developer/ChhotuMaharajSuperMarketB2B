package com.chhotumaharajsupermarketbusiness.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chhotumaharajsupermarketbusiness.Adapter.TopicWiseAdapter;
import com.chhotumaharajsupermarketbusiness.Model.TopicWiseModel;
import com.chhotumaharajsupermarketbusiness.R;
import com.chhotumaharajsupermarketbusiness.constant.Constant;
import com.chhotumaharajsupermarketbusiness.constant.MaintainRequestQueue;
import com.chhotumaharajsupermarketbusiness.constant.SharedPrefrenceObj;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TopicWiseFragment extends Fragment implements TopicWiseAdapter.ClickListener {

    public static View fragment;

    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    ArrayList<TopicWiseModel> topicWiseModels;
    TopicWiseAdapter topicWiseAdapter;
    TextView textView;
    String topic;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.activity_topic_wise, container, false);


        recyclerView = fragment.findViewById(R.id.topic_wise_recylerview);
        textView = fragment.findViewById(R.id.topic_wise_txt);

        topic = getArguments().getString("topic");
        progressDialog = new ProgressDialog(getActivity());

        if (topic.equalsIgnoreCase("topic")) {
            textView.setText("Topics");
            topicWiseList();
        } else {
            textView.setText("Questionnaire");
            questionWiseList();
        }

        topicWiseAdapter = new TopicWiseAdapter();
        topicWiseAdapter.setOnItemClickListener(this);

        return fragment;
    }

    private void topicWiseList() {

        String url = Constant.URL + "domtypeList";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("Getting data...");
        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    topicWiseModels = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String name = jsonObject.getString("name");
                        String video = jsonObject.getString("video");
                        String ppt = jsonObject.getString("ppt");

                        topicWiseModels.add(new TopicWiseModel(i + 1, id, name, ppt, video));

                    }
                    topicWiseAdapter = new TopicWiseAdapter(getActivity(), topicWiseModels);
                    recyclerView.setAdapter(topicWiseAdapter);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                    recyclerView.setLayoutManager(mLayoutManager);

                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error: " + error.getMessage());
                progressDialog.dismiss();
            }

        }) {
            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(getActivity(), "auth_token"));
                params.put("content-type", "application/x-www-form-urlencoded");
                Log.d("params..", params.toString());
                return params;
            }

            //
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("dom_type", "supermarket");
                System.out.println("Param value..........." + params);
                return checkParams(params);
            }

            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }
        };
        // Adding request to request queue
        req.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MaintainRequestQueue.getInstance(getActivity()).addToRequestQueue(req, "tag");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void questionWiseList() {

        String url = Constant.URL + "Faqtypelist";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("Getting data...");
        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    topicWiseModels = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String name = jsonObject.getString("name");
                        String video = jsonObject.getString("video");
                        String ppt = jsonObject.getString("ppt");

                        topicWiseModels.add(new TopicWiseModel(i + 1, id, name, ppt, video));

                    }

                    topicWiseAdapter = new TopicWiseAdapter(getActivity(), topicWiseModels);
                    recyclerView.setAdapter(topicWiseAdapter);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                    recyclerView.setLayoutManager(mLayoutManager);

                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error: " + error.getMessage());
                progressDialog.dismiss();
            }

        }) {
            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(getActivity(), "auth_token"));
                params.put("content-type", "application/x-www-form-urlencoded");
                Log.d("params..", params.toString());
                return params;
            }

            //
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("dom_type", "supermarket");
                System.out.println("Param value..........." + params);
                return checkParams(params);
            }

            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }
        };
        // Adding request to request queue
        req.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MaintainRequestQueue.getInstance(getActivity()).addToRequestQueue(req, "tag");
    }

    @Override
    public void onItemClick(View v, int position, String flag) {

        TopicWiseModel topicWiseModel = topicWiseAdapter.getWordAtPosition(position);

        if (flag.equalsIgnoreCase("1")) {

            TopicWiseDetailFragment topicWiseFragment1 = new TopicWiseDetailFragment();
            Bundle args1 = new Bundle();
            args1.putString("topic_name", topicWiseModel.getName());
            args1.putString("topic_video", topicWiseModel.getVideo().trim());
            args1.putInt("video_id", topicWiseModel.getId());
            args1.putString("topic", topic);
            args1.putString("pdf", topicWiseModel.getPpt());
            topicWiseFragment1.setArguments(args1);
            FragmentManager fragmentManager1 = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction1 = fragmentManager1.beginTransaction();
            transaction1.add(R.id.frame_container, topicWiseFragment1);
            transaction1.commit();

//            Intent intent = new Intent(getActivity(), TopicWiseDetailActivity1.class);
//            intent.putExtra("topic_name", topicWiseModel.getName());
//            intent.putExtra("topic_video", topicWiseModel.getVideo().trim());
//            intent.putExtra("video_id", topicWiseModel.getId());
//            intent.putExtra("topic", topic);
//            intent.putExtra("pdf", topicWiseModel.getPpt());
//            startActivity(intent);

        } else {
            if (!topicWiseModel.getPpt().equalsIgnoreCase("null")) {
                updatePDF(topicWiseModel.getId(), topicWiseModel.getPpt());
            } else {
                Toast.makeText(getActivity(), "PDF file Not Available", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void updatePDF(final int videoId, final String ppt) {

        String url = Constant.URL + "update_pdfvideo";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("Opening PDF.....");
        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(Constant.PPT_PATH + ppt.trim()));
                    startActivity(intent);
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error: " + error.getMessage());
                progressDialog.dismiss();
            }

        }) {
            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(getActivity(), "auth_token"));
                params.put("content-type", "application/x-www-form-urlencoded");
                Log.d("params..", params.toString());
                return params;
            }

            //video_id,user_id,sub_query,query_type
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("video_id", String.valueOf(videoId));
                params.put("user_id", String.valueOf(SharedPrefrenceObj.getIntegerval(getActivity(), "id")));
                System.out.println("Param value..........." + params);
                return checkParams(params);
            }

            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }
        };
        // Adding request to request queue
        req.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MaintainRequestQueue.getInstance(getActivity()).addToRequestQueue(req, "tag");
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
                        MainFragment mainFragment = new MainFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.add(R.id.frame_container, mainFragment);
                        transaction.commit();
                        //   Toast.makeText(getActivity(), "Your response is required", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                return false;
            }
        });


    }

}
