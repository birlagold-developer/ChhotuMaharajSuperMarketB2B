package com.chhotumaharajsupermarketbusiness.Activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chhotumaharajsupermarketbusiness.Fragment.LiveDemoFragment;
import com.chhotumaharajsupermarketbusiness.Fragment.MainFragment;
import com.chhotumaharajsupermarketbusiness.Fragment.TopicWiseFragment;
import com.chhotumaharajsupermarketbusiness.R;
import com.chhotumaharajsupermarketbusiness.constant.Constant;
import com.chhotumaharajsupermarketbusiness.constant.MaintainRequestQueue;
import com.chhotumaharajsupermarketbusiness.constant.SharedPrefrenceObj;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    String page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");

        page = getIntent().getStringExtra("query_page");

        //birlagold.developer@gmail.com
        //baw_x63E#HJv^r&S
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(Color.BLACK);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View view = navigationView.getHeaderView(0);
        TextView user_name = view.findViewById(R.id.user_name);
        TextView user_email = view.findViewById(R.id.user_email);

        if (SharedPrefrenceObj.getSharedValue(NavigationActivity.this, "name") != null) {
            user_name.setText(SharedPrefrenceObj.getSharedValue(NavigationActivity.this, "name"));
        } else {
            user_name.setText("");
        }

        if (SharedPrefrenceObj.getSharedValue(NavigationActivity.this, "email") != null) {
            user_email.setText(SharedPrefrenceObj.getSharedValue(NavigationActivity.this, "email"));
        } else {
            user_email.setText("");
        }
        if (page.equalsIgnoreCase("1")) {
            TopicWiseFragment topicWiseFragment = new TopicWiseFragment();
            Bundle args = new Bundle();
            args.putString("topic", "topic");
            topicWiseFragment.setArguments(args);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame_container, topicWiseFragment);
            transaction.commit();

        } else {
            MainFragment mainFragment = new MainFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.frame_container, mainFragment);
            transaction.commit();
        }

        /*FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("newToken", newToken);
                SharedPrefrenceObj.setSharedValue(NavigationActivity.this, "token", newToken);

                saveFCMToken(SharedPrefrenceObj.getSharedValue(NavigationActivity.this, "mobile"), newToken);

            }
        });*/

    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            MainFragment mainFragment = new MainFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame_container, mainFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        } else if (id == R.id.nav_appointment) {
            LiveDemoFragment mainFragment = new LiveDemoFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame_container, mainFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        } else if (id == R.id.nav_exist) {
            System.exit(0);
            finish();
        } else if (id == R.id.nav_logout) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Log Out")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPrefrenceObj.setIntegerval(NavigationActivity.this, "id", 0);
                            Intent intent = new Intent(NavigationActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void saveFCMToken(final String mobile, final String fcmToken) {

        String url = Constant.URL + "fcm_token";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        StringRequest req = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {

                        System.out.println("fcmToken Updated");

                    } else {
                        System.out.println(jsonObject.optString("error"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error: " + error.getMessage());
            }

        }) {
            @Override
            public Request.Priority getPriority() {
                return Request.Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(NavigationActivity.this, "auth_token"));
                params.put("content-type", "application/x-www-form-urlencoded");
                Log.d("params..", params.toString());
                return params;
            }

            //
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", mobile);
                params.put("token", fcmToken);
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

}
