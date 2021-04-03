package com.chhotumaharajsupermarketbusiness.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.chhotumaharajsupermarketbusiness.Activity.QueryActivity;
import com.chhotumaharajsupermarketbusiness.R;
import com.chhotumaharajsupermarketbusiness.Utility.exoplayer.BitmapOverlayVideoProcessor;
import com.chhotumaharajsupermarketbusiness.Utility.exoplayer.VideoProcessingGLSurfaceView;
import com.chhotumaharajsupermarketbusiness.constant.Constant;
import com.chhotumaharajsupermarketbusiness.constant.MaintainRequestQueue;
import com.chhotumaharajsupermarketbusiness.constant.SharedPrefrenceObj;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.EventLogger;
import com.google.android.exoplayer2.util.GlUtil;
import com.google.android.exoplayer2.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class TopicWiseDetailFragment extends Fragment implements View.OnClickListener {
    public static View fragment;

    TextView topic_name, language;
    //VideoView videoView;
    ProgressBar progressBar;
    Button understand, query;
    Button chat, call;
    String topicName, prefered_language, topicVideo, ppt;

    private static final String TAG = "TopicWiseDetailFragment";
    View mVideoLayout;
    @Nullable private PlayerView videoView;
    @Nullable private VideoProcessingGLSurfaceView videoProcessingGLSurfaceView;

    @Nullable private SimpleExoPlayer player;

    private static final String ACTION_VIEW = "com.google.android.exoplayer.gldemo.action.VIEW";
    private static final String EXTENSION_EXTRA = "extension";
    private static final String DRM_SCHEME_EXTRA = "drm_scheme";
    private static final String DRM_LICENSE_URL_EXTRA = "drm_license_url";

    int videoId, parentVideoId;
    String[] videoLanguage = null;
    ProgressDialog progressDialog;
    public static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;

    String topic;
    LinearLayout question_layout, topic_layout;
    Button not_understood, question_understood;
    Button viewpdf;

    ImageView zoom;
    long sec;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.activity_topic_wise_detail, container, false);

        topic_name = fragment.findViewById(R.id.topic_detail_name);
        language = fragment.findViewById(R.id.language);

        progressBar = fragment.findViewById(R.id.progressBar);
        understand = fragment.findViewById(R.id.understand);
        query = fragment.findViewById(R.id.query);
        chat = fragment.findViewById(R.id.chat_me);
        call = fragment.findViewById(R.id.call_me);
        question_layout = fragment.findViewById(R.id.question_layout);
        topic_layout = fragment.findViewById(R.id.topic_layout);
        question_understood = fragment.findViewById(R.id.question_understand);
        not_understood = fragment.findViewById(R.id.not_understood);
        viewpdf = fragment.findViewById(R.id.detail_pdf);

        mVideoLayout = fragment.findViewById(R.id.video_layout);
        videoView = fragment.findViewById(R.id.videoView);

        boolean requestSecureSurface = getActivity().getIntent().hasExtra(DRM_SCHEME_EXTRA);
        if (requestSecureSurface && !GlUtil.isProtectedContentExtensionSupported(getActivity())) {
            Toast.makeText(
                    getActivity(), R.string.error_protected_content_extension_not_supported, Toast.LENGTH_LONG)
                    .show();
        }

        VideoProcessingGLSurfaceView videoProcessingGLSurfaceView =
                new VideoProcessingGLSurfaceView(
                        getActivity(), requestSecureSurface, new BitmapOverlayVideoProcessor(getActivity()));
        FrameLayout contentFrame = fragment.findViewById(R.id.exo_content_frame);
        contentFrame.addView(videoProcessingGLSurfaceView);
        this.videoProcessingGLSurfaceView = videoProcessingGLSurfaceView;

        topicName = getArguments().getString("topic_name");
        prefered_language = SharedPrefrenceObj.getSharedValue(getActivity(), "language");
        topicVideo = getArguments().getString("topic_video").trim();
        videoId = getArguments().getInt("video_id", 0);
        parentVideoId = videoId;
        topic = getArguments().getString("topic");
        ppt = getArguments().getString("pdf");

        progressDialog = new ProgressDialog(getActivity());

        topic_name.setText(topicName);
        progressBar.setVisibility(View.GONE);

        if (Util.SDK_INT > 23) {
            if (videoView != null) {
                videoView.onPause();
            }
            releasePlayer();
        }
        topicVideo = topicVideo.replace(" ", "%20");
        topicVideo = topicVideo.replace("\n", "%0A");
        initializePlayer(Constant.VIDEO_PATH + topicVideo);

        if (topic.equalsIgnoreCase("topic")) {
            topic_layout.setVisibility(View.VISIBLE);
            question_layout.setVisibility(View.GONE);
            getVideoForTopic(prefered_language);

        } else {
            topic_layout.setVisibility(View.GONE);
            question_layout.setVisibility(View.VISIBLE);
            getVideoForQuestion(prefered_language);
        }

        language.setOnClickListener(this);
        understand.setOnClickListener(this);
        not_understood.setOnClickListener(this);
        question_understood.setOnClickListener(this);
        query.setOnClickListener(this);
        chat.setOnClickListener(this);
        call.setOnClickListener(this);

        if (ppt.equalsIgnoreCase("null")) {
            viewpdf.setText("PDF file Not Available");
        } else {
            viewpdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    videoView.onPause();
                    updatePDF(videoId, ppt);
                }
            });
        }

        return fragment;
    }



    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            if (videoView != null) {
                videoView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            if (videoView != null) {
                videoView.onPause();
            }
            releasePlayer();
        }
    }

    private void releasePlayer() {
        Assertions.checkNotNull(videoView).setPlayer(null);
        if (player != null) {
            player.release();
            Assertions.checkNotNull(videoProcessingGLSurfaceView).setVideoComponent(null);
            player = null;
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
                        Toast.makeText(getActivity(), "Your response is required", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.language) {
            try {

                ArrayList<String> languageList = new ArrayList<String>();

                for (String language : videoLanguage) {
                    languageList.add(language);
                }

                final ArrayAdapter<String> languageAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, languageList);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Select Language");

                builder.setAdapter(languageAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selected_language = languageAdapter.getItem(which);
                        language.setText(selected_language);
                        dialog.dismiss();

                        videoView.onPause();
                        //progressBar.setVisibility(View.VISIBLE);
                        if (topic.equalsIgnoreCase("topic")) {
                            getVideoForTopic(selected_language);
                        } else {
                            getVideoForQuestion(selected_language);
                        }
                    }
                });

                AlertDialog alertDialog = builder.create();
                ListView listView = alertDialog.getListView();
                listView.setDivider(new ColorDrawable(Color.GRAY));
                listView.setDividerHeight(2);
                alertDialog.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (view.getId() == R.id.understand) {
            videoView.onPause();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(videoView.getControllerShowTimeoutMs());
            updateUnderstood(String.valueOf(seconds));
        }

        if (view.getId() == R.id.not_understood) {
            videoView.onPause();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(videoView.getControllerShowTimeoutMs());
            updateNotUnderstood(String.valueOf(seconds));
        }

        if (view.getId() == R.id.question_understand) {
            videoView.onPause();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(videoView.getControllerShowTimeoutMs());
            updateUnderstood(String.valueOf(seconds));
        }

        if (view.getId() == R.id.query) {
            videoView.onPause();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(videoView.getControllerShowTimeoutMs());
            Intent intent = new Intent(getActivity(), QueryActivity.class);
            intent.putExtra("video_id", videoId);
            intent.putExtra("parentvideo_id", parentVideoId);
            intent.putExtra("language_id", 1);
            intent.putExtra("video_time", String.valueOf(seconds));
            startActivity(intent);
        }

        if (view.getId() == R.id.chat_me) {
            videoView.onPause();
            updateChatMe();
        }

        if (view.getId() == R.id.call_me) {
            videoView.onPause();
            updateCallMe();

        }
    }

    private void getVideoForTopic(final String selectedLanguage) {
        //progressBar.setVisibility(View.GONE);

        String url = Constant.URL + "videoDetailsForLanguage";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("Loading data.....");
        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {
                        JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                        JSONObject video = dataJsonObject.getJSONObject("list");
                        JSONArray videoLanguageArray = dataJsonObject.getJSONArray("video_languages");

                        videoLanguage = new String[videoLanguageArray.length()];

                        for (int i = 0; i < videoLanguageArray.length(); i++) {
                            JSONObject object = videoLanguageArray.getJSONObject(i);
                            videoLanguage[i] = object.getString("lang_name");
                        }

                        topicVideo = video.getString("video").replace(" ", "%20");
                        videoId = video.getInt("id");
                        String video_lang = video.getString("language_name");

                        language.setText(video_lang);
                        if (Util.SDK_INT > 23) {
                            if (videoView != null) {
                                videoView.onPause();
                            }
                            releasePlayer();
                        }
                        initializePlayer(Constant.VIDEO_PATH + topicVideo);
                        //videoView.setVideoPath(Constant.VIDEO_PATH + topicVideo);
                        //videoView.requestFocus();

                    }

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
                params.put("Cache-control", "no-cache");
                Log.d("params..", params.toString());
                return params;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("video_id", String.valueOf(parentVideoId));
                params.put("preferred_language", selectedLanguage);
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

    private void getVideoForQuestion(final String selectedLanguage) {

        String url = Constant.URL + "videoDetailsForQuestionary";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("Loading data.....");
        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {
                        JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                        JSONObject video = dataJsonObject.getJSONObject("list");
                        JSONArray videoLanguageArray = dataJsonObject.getJSONArray("video_languages");

                        videoLanguage = new String[videoLanguageArray.length()];

                        for (int i = 0; i < videoLanguageArray.length(); i++) {
                            JSONObject object = videoLanguageArray.getJSONObject(i);
                            videoLanguage[i] = object.getString("lang_name");
                        }

                        topicVideo = video.getString("video").replace(" ", "%20");
                        videoId = video.getInt("id");
                        String video_lang = video.getString("language_name");

                        language.setText(video_lang);
                        if (Util.SDK_INT > 23) {
                            if (videoView != null) {
                                videoView.onPause();
                            }
                            releasePlayer();
                        }
                        topicVideo = topicVideo.replace(" ", "%20");
                        topicVideo = topicVideo.replace("\n", "%0A");
                        initializePlayer(Constant.VIDEO_PATH + topicVideo);


                    }

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

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("video_id", String.valueOf(parentVideoId));
                params.put("preferred_language", selectedLanguage);
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

    private void updateChatMe() {

        String url = Constant.URL + "chatMe";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("updating data...");
        StringRequest req = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {

                        PackageManager packageManager = getActivity().getPackageManager();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        String phone = "917718802588";
                        String message = "I'm interested in your Chhotu Maharaj Cine Cafe Franchise";
                        try {
                            String url = "https://api.whatsapp.com/send?phone=" + phone + "&text=" + URLEncoder.encode(message, "UTF-8");
                            i.setPackage("com.whatsapp");
                            i.setData(Uri.parse(url));
                            if (i.resolveActivity(packageManager) != null) {
                                startActivity(i);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
            public Request.Priority getPriority() {
                return Request.Priority.IMMEDIATE;
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

    private void updateCallMe() {

        String url = Constant.URL + "callMe";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("updating data...");
        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("")
                                .setMessage("Our concern person will call you shortly")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        TopicWiseFragment topicWiseFragment = new TopicWiseFragment();
                                        Bundle args = new Bundle();
                                        args.putString("topic", topic);
                                        topicWiseFragment.setArguments(args);
                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                                        transaction.replace(R.id.frame_container, topicWiseFragment);
                                        transaction.commit();
                                    }

                                })
                                .show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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

    private void updateUnderstood(final String video_time) {

        String url = Constant.URL + "update_VideoHistory";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("Submitting query.....");
        StringRequest req = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    Toast.makeText(getActivity(), "Thank you for your response..", Toast.LENGTH_LONG).show();
                    TopicWiseFragment topicWiseFragment = new TopicWiseFragment();
                    Bundle args = new Bundle();
                    args.putString("topic", topic);
                    topicWiseFragment.setArguments(args);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.frame_container, topicWiseFragment);
                    transaction.commit();
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
            public Request.Priority getPriority() {
                return Request.Priority.IMMEDIATE;
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
                params.put("sub_query", "");
                params.put("watch_video", video_time);
                params.put("query_type", "understood");
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

    private void updateNotUnderstood(final String video_time) {

        String url = Constant.URL + "update_VideoHistory";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("updating data...");
        StringRequest req = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    Toast.makeText(getActivity(), "Thank you for your response..", Toast.LENGTH_LONG).show();
                    TopicWiseFragment topicWiseFragment = new TopicWiseFragment();
                    Bundle args = new Bundle();
                    args.putString("topic", topic);
                    topicWiseFragment.setArguments(args);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.frame_container, topicWiseFragment);
                    transaction.commit();
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
            public Request.Priority getPriority() {
                return Request.Priority.IMMEDIATE;
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
                params.put("sub_query", "");
                params.put("watch_video", video_time);
                params.put("query_type", "not_understood");
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

    private void initializePlayer(String DEFAULT_MEDIA_URI) {
        Intent intent = getActivity().getIntent();
        String action = intent.getAction();
        Uri uri =
                ACTION_VIEW.equals(action)
                        ? Assertions.checkNotNull(intent.getData())
                        : Uri.parse(DEFAULT_MEDIA_URI);
        DrmSessionManager drmSessionManager;
        if (Util.SDK_INT >= 18 && intent.hasExtra(DRM_SCHEME_EXTRA)) {
            String drmScheme = Assertions.checkNotNull(intent.getStringExtra(DRM_SCHEME_EXTRA));
            String drmLicenseUrl = Assertions.checkNotNull(intent.getStringExtra(DRM_LICENSE_URL_EXTRA));
            UUID drmSchemeUuid = Assertions.checkNotNull(Util.getDrmUuid(drmScheme));
            HttpDataSource.Factory licenseDataSourceFactory = new DefaultHttpDataSourceFactory();
            HttpMediaDrmCallback drmCallback =
                    new HttpMediaDrmCallback(drmLicenseUrl, licenseDataSourceFactory);
            drmSessionManager =
                    new DefaultDrmSessionManager.Builder()
                            .setUuidAndExoMediaDrmProvider(drmSchemeUuid, FrameworkMediaDrm.DEFAULT_PROVIDER)
                            .build(drmCallback);
        } else {
            drmSessionManager = DrmSessionManager.getDummyDrmSessionManager();
        }

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity());
        MediaSource mediaSource;
        @C.ContentType int type = Util.inferContentType(uri, intent.getStringExtra(EXTENSION_EXTRA));
        if (type == C.TYPE_DASH) {
            mediaSource =
                    new DashMediaSource.Factory(dataSourceFactory)
                            .setDrmSessionManager(drmSessionManager)
                            .createMediaSource(MediaItem.fromUri(uri));
        } else if (type == C.TYPE_OTHER) {
            mediaSource =
                    new ProgressiveMediaSource.Factory(dataSourceFactory)
                            .setDrmSessionManager(drmSessionManager)
                            .createMediaSource(MediaItem.fromUri(uri));
        } else {
            throw new IllegalStateException();
        }

        SimpleExoPlayer player = new SimpleExoPlayer.Builder(getActivity()).build();
        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.setMediaSource(mediaSource);
        player.prepare();
        player.play();
        VideoProcessingGLSurfaceView videoProcessingGLSurfaceView =
                Assertions.checkNotNull(this.videoProcessingGLSurfaceView);
        videoProcessingGLSurfaceView.setVideoComponent(
                Assertions.checkNotNull(player.getVideoComponent()));
        Assertions.checkNotNull(videoView).setPlayer(player);
        player.addAnalyticsListener(new EventLogger(/* trackSelector= */ null));
        this.player = player;
    }

}
