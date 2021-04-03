package com.chhotumaharajsupermarketbusiness.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chhotumaharajsupermarketbusiness.Adapter.QueryAdapter;
import com.chhotumaharajsupermarketbusiness.Model.QueryModel;
import com.chhotumaharajsupermarketbusiness.Model.SubQueryModel;
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
import java.util.List;
import java.util.Map;

public class QueryActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    QueryModel queryModel;
    QueryAdapter queryAdapter;
    int videoId, parentvideo_id,language_id;
    String videoTime;
    Button query_submit, query_cancel;
    LinearLayout query_btn_layout;
    TextView no_queries;
    String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_query);

        progressDialog = new ProgressDialog(QueryActivity.this);

        recyclerView = findViewById(R.id.query_recyclerview);
        query_submit = findViewById(R.id.query_submit);
        query_cancel = findViewById(R.id.query_cancel);

        no_queries = findViewById(R.id.no_queries_txt);
        query_btn_layout = findViewById(R.id.querise_btn_layout);

        setFinishOnTouchOutside(false);

        query_cancel.setOnClickListener(this);
        query_submit.setOnClickListener(this);

        Log.d("id.....", String.valueOf(SharedPrefrenceObj.getIntegerval(QueryActivity.this, "id")));
        Log.d("id.....", String.valueOf(videoId));
        videoId = getIntent().getIntExtra("video_id", 0);
        parentvideo_id = getIntent().getIntExtra("parentvideo_id", 0);
        language_id = getIntent().getIntExtra("language_id", 0);
        videoTime = getIntent().getStringExtra("video_time");

        queryList(parentvideo_id);

    }

    private void queryList(final int videoId) {

        String url = Constant.URL + "VideoDetails";
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
                    queryModel = new QueryModel();

                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject list = jsonObject.optJSONObject("list");
                    if (list == null) {
                        no_queries.setVisibility(View.VISIBLE);
                        query_btn_layout.setVisibility(View.GONE);
                    } else {

                        int id = list.getInt("id");
                        String name = list.getString("name");
                        String ppt = list.getString("ppt");
                        String video = list.getString("video");

                        queryModel.setId(id);
                        queryModel.setName(name);
                        queryModel.setPpt(ppt);
                        queryModel.setVideo(video);

                        List<SubQueryModel> subQueryModels = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("subquerylist");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            int videoId = jsonObject1.getInt("id");
                            String videoTitle = jsonObject1.getString("video_title");
                            String videoQuery = jsonObject1.getString("video_query");

                            SubQueryModel subQueryModel = new SubQueryModel();
                            subQueryModel.setId(videoId);
                            subQueryModel.setVideo_query(videoQuery);
                            subQueryModel.setVideo_title(videoTitle);
                            subQueryModels.add(subQueryModel);

                        }

                        queryModel.setSubQueryModels(subQueryModels);

                        queryAdapter = new QueryAdapter(QueryActivity.this, queryModel);
                        recyclerView.setAdapter(queryAdapter);
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(QueryActivity.this);
                        recyclerView.setLayoutManager(mLayoutManager);


                    }

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
                params.put("authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(QueryActivity.this, "auth_token"));
                params.put("content-type", "application/x-www-form-urlencoded");
                Log.d("params..", params.toString());
                return params;
            }

            //
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("dom_type", "supermarket");
                params.put("video_id", String.valueOf(videoId));
                params.put("language_id", String.valueOf(language_id));
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
        MaintainRequestQueue.getInstance(this).addToRequestQueue(req, "tag");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.query_cancel:
                finish();

                break;

            case R.id.query_submit:
                String selectedSubQueryIDs = "";

                for (SubQueryModel subQueryModel : queryModel.getSubQueryModels()) {
                    if (subQueryModel.isSelected()) {
                        selectedSubQueryIDs += selectedSubQueryIDs == "" ? "" + subQueryModel.getId() : "," + subQueryModel.getId();
                    }
                }

                if (!selectedSubQueryIDs.equals("")) {
                    updateQuery(videoId, selectedSubQueryIDs, videoTime);
                    Log.d("queries...", selectedSubQueryIDs);
                } else {
                    Toast.makeText(QueryActivity.this, "Select Query", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        progressDialog.dismiss();
        finish();
    }

    private void updateQuery(final int videoId, final String subquery, final String videoTime) {

        String url = Constant.URL + "update_VideoHistory";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);

        progressDialog.show();
        progressDialog.setMessage("Submitting queries...");

        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    progressDialog.dismiss();

                    Intent intent = new Intent(QueryActivity.this, NavigationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("query_page", "1");
                    startActivity(intent);
                    finish();

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
                params.put("authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(QueryActivity.this, "auth_token"));
                params.put("content-type", "application/x-www-form-urlencoded");
                Log.d("params..", params.toString());
                return params;
            }

            //video_id,user_id,sub_query,query_type
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("video_id", String.valueOf(videoId));
                params.put("user_id", String.valueOf(SharedPrefrenceObj.getIntegerval(QueryActivity.this, "id")));
                params.put("sub_query", subquery);
                params.put("watch_video", videoTime);
                params.put("query_type", "Not Understood");
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
        MaintainRequestQueue.getInstance(this).addToRequestQueue(req, "tag");
    }

    @Override
    protected void onPause() {
        super.onPause();
        progressDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }

}
