 package com.chhotumaharajsupermarketbusiness.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ConceptBusinessActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    //private VideoView videoView;
    private ProgressBar progressBar;
    private int position = 0;
    private MediaController mediaController;
    private Button interested, not_interested;
    private String language, name;
    private TextView user_name;
    private ProgressDialog progressDialog;
    private static final int RECOVERY_REQUEST = 1;
    //private YouTubePlayer player;
    private ImageView rotate;
    private long seconds;

    //==============================================
    private static final String TAG = "TopicWiseDetailFragment";
    View mVideoLayout;
    @Nullable
    private PlayerView videoView;
    @Nullable private VideoProcessingGLSurfaceView videoProcessingGLSurfaceView;

    @Nullable private SimpleExoPlayer player;

    private static final String ACTION_VIEW = "com.google.android.exoplayer.gldemo.action.VIEW";
    private static final String EXTENSION_EXTRA = "extension";
    private static final String DRM_SCHEME_EXTRA = "drm_scheme";
    private static final String DRM_LICENSE_URL_EXTRA = "drm_license_url";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concept_business);

        getSupportActionBar().hide();

        TextView tv = findViewById(R.id.chottu_txt);
        TextView tv1 = findViewById(R.id.maharaj_txt);
        Typeface face = Typeface.createFromAsset(getAssets(), "cooperblackstd.otf");

        tv.setTypeface(face);
        tv1.setTypeface(face);

        videoView = findViewById(R.id.videoView);
        progressBar = findViewById(R.id.progressBar);
        interested = findViewById(R.id.interested);
        not_interested = findViewById(R.id.not_interested);
        user_name = findViewById(R.id.user_login_name);
        //   rotate  = findViewById(R.id.zoom);
        progressBar.setVisibility(View.GONE);
        interested.setOnClickListener(this);
        not_interested.setOnClickListener(this);

        mVideoLayout = findViewById(R.id.video_layout);

        boolean requestSecureSurface = ConceptBusinessActivity.this.getIntent().hasExtra(DRM_SCHEME_EXTRA);
        if (requestSecureSurface && !GlUtil.isProtectedContentExtensionSupported(ConceptBusinessActivity.this)) {
            Toast.makeText(
                    ConceptBusinessActivity.this, R.string.error_protected_content_extension_not_supported, Toast.LENGTH_LONG)
                    .show();
        }

        VideoProcessingGLSurfaceView videoProcessingGLSurfaceView =
                new VideoProcessingGLSurfaceView(
                        ConceptBusinessActivity.this, requestSecureSurface, new BitmapOverlayVideoProcessor(ConceptBusinessActivity.this));
        FrameLayout contentFrame = findViewById(R.id.exo_content_frame);
        contentFrame.addView(videoProcessingGLSurfaceView);
        this.videoProcessingGLSurfaceView = videoProcessingGLSurfaceView;

        user_name.setText(SharedPrefrenceObj.getSharedValue(ConceptBusinessActivity.this, "name"));

        Log.d("State..", SharedPrefrenceObj.getSharedValue(ConceptBusinessActivity.this, "state"));
        Log.d("State..", SharedPrefrenceObj.getSharedValue(ConceptBusinessActivity.this, "city"));

        progressDialog = new ProgressDialog(ConceptBusinessActivity.this);

        /*if (SharedPrefrenceObj.getIntegerval(ConceptBusinessActivity.this, "step") == 2) {
            Intent intent = new Intent(ConceptBusinessActivity.this, PreAnalysisActivity.class);
            startActivity(intent);
            finish();
        }*/



        /*if (SharedPrefrenceObj.getIntegerval(ConceptBusinessActivity.this, "step") == 1) {

            //Intent intent = new Intent(FranchiseActivity.this, ConceptBusinessActivity.class);
            Intent intent = new Intent(ConceptBusinessActivity.this, PreAnalysisActivity.class);
            startActivity(intent);
            finish();
        } else*/
        if (SharedPrefrenceObj.getIntegerval(ConceptBusinessActivity.this, "step") == 2) {
            Intent intent = new Intent(ConceptBusinessActivity.this, PreAnalysisActivity.class);
            intent.putExtra("query_page", "0");
            startActivity(intent);
            finish();
        } else if (SharedPrefrenceObj.getIntegerval(ConceptBusinessActivity.this, "step") == 3) {
            Intent intent = new Intent(ConceptBusinessActivity.this, NavigationActivity.class);
            intent.putExtra("query_page", "0");
            startActivity(intent);
            finish();
        } else if (SharedPrefrenceObj.getIntegerval(ConceptBusinessActivity.this, "step") == 4) {
            Intent intent = new Intent(ConceptBusinessActivity.this, NavigationActivity.class);
            intent.putExtra("query_page", "0");
            startActivity(intent);
            finish();
        } else if (SharedPrefrenceObj.getIntegerval(ConceptBusinessActivity.this, "step") == 5) {
            Intent intent = new Intent(ConceptBusinessActivity.this, NavigationActivity.class);
            intent.putExtra("query_page", "0");
            startActivity(intent);
            finish();
        }
        else
        {
            try {
                getVideoName();

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
        }
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

    @Override
    public void onBackPressed() {
        Toast.makeText(ConceptBusinessActivity.this, "Please give your view", Toast.LENGTH_LONG).show();
//          String timeeeee = String.valueOf(player.getCurrentTimeMillis());
//          Log.d("time........",timeeeee);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.interested) {
            //long minutes = TimeUnit.MILLISECONDS.toMinutes(videoView.getCurrentPosition());
            //long seconds = TimeUnit.MILLISECONDS.toSeconds(videoView.getCurrentPosition());
            long seconds = TimeUnit.MILLISECONDS.toSeconds(videoView.getControllerShowTimeoutMs());
            updateVideo(String.valueOf(seconds));
        } else {
            //long minutes = TimeUnit.MILLISECONDS.toMinutes(videoView.getCurrentPosition());
            //long seconds = TimeUnit.MILLISECONDS.toSeconds(videoView.getCurrentPosition());
            long seconds = TimeUnit.MILLISECONDS.toSeconds(videoView.getControllerShowTimeoutMs());
            updateVideo1(String.valueOf(seconds));
        }

    }

    private void getVideoName() {

        if (Util.SDK_INT > 23) {
            if (videoView != null) {
                videoView.onPause();
            }
            releasePlayer();
        }
        initializePlayer(Constant.VIDEO_PATH2);


        /*String url = Constant.URL + "getConceptBusinessVideo";
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
                        String video = dataJsonObject.getString("video");

                        if (Util.SDK_INT > 23) {
                            if (videoView != null) {
                                videoView.onPause();
                            }
                            releasePlayer();
                        }
                        initializePlayer(Constant.VIDEO_PATH2);

                        //videoView.setVideoPath(Constant.VIDEO_PATH + video.replace(" ", "%20"));
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
                params.put("authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(ConceptBusinessActivity.this, "auth_token"));
                params.put("content-type", "application/x-www-form-urlencoded");
                Log.d("params..", params.toString());
                return params;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", String.valueOf(SharedPrefrenceObj.getIntegerval(ConceptBusinessActivity.this, "id")));
                System.out.println("Param value..........." + params);
                Log.e("userid","===>"+String.valueOf(SharedPrefrenceObj.getIntegerval(ConceptBusinessActivity.this, "id")));
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
        MaintainRequestQueue.getInstance(ConceptBusinessActivity.this).addToRequestQueue(req, "tag");*/
    }

    private void updateVideo(final String video_time) {

        String url = Constant.URL + "updatevideop1p2";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("Saving data ....");
        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {

                        SharedPrefrenceObj.setIntegerval(ConceptBusinessActivity.this, "step", 2);
                        Intent intent = new Intent(ConceptBusinessActivity.this, PreAnalysisActivity.class);
                        startActivity(intent);
                        finish();
                        progressDialog.dismiss();

                    } else {

                        progressDialog.dismiss();
                    }
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
                params.put("authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(ConceptBusinessActivity.this, "auth_token"));
                params.put("content-type", "application/x-www-form-urlencoded");
                Log.d("params..", params.toString());
                return params;
            }

            //
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("video_interested", "1");
                params.put("watch_video", video_time);
                params.put("id", String.valueOf(SharedPrefrenceObj.getIntegerval(ConceptBusinessActivity.this, "id")));
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
        MaintainRequestQueue.getInstance(this).addToRequestQueue(req, "tag");


    }

    private void updateVideo1(final String video_time) {

        String url = Constant.URL + "updatevideop1p2";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("Saving data ....");
        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {

                        /*SharedPrefrenceObj.setIntegerval(ConceptBusinessActivity.this, "step", 1);
                        Intent intent = new Intent(ConceptBusinessActivity.this, InterestedActivity.class);
                        intent.putExtra("concept", "not_interested");
                        startActivity(intent);*/
                        finish();
                        progressDialog.dismiss();
                    } else {

                        progressDialog.dismiss();
                    }
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
                params.put("authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(ConceptBusinessActivity.this, "auth_token"));
                params.put("content-type", "application/x-www-form-urlencoded");
                Log.d("params..", params.toString());
                return params;
            }

            //
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("video_interested", "0");
                params.put("watch_video", video_time);
                params.put("id", String.valueOf(SharedPrefrenceObj.getIntegerval(ConceptBusinessActivity.this, "id")));
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
        MaintainRequestQueue.getInstance(this).addToRequestQueue(req, "tag");


    }

    private void releasePlayer() {
        Assertions.checkNotNull(videoView).setPlayer(null);
        if (player != null) {
            player.release();
            Assertions.checkNotNull(videoProcessingGLSurfaceView).setVideoComponent(null);
            player = null;
        }
    }

    private void initializePlayer(String DEFAULT_MEDIA_URI) {
        Intent intent = ConceptBusinessActivity.this.getIntent();
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

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(ConceptBusinessActivity.this);
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

        SimpleExoPlayer player = new SimpleExoPlayer.Builder(ConceptBusinessActivity.this).build();
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
