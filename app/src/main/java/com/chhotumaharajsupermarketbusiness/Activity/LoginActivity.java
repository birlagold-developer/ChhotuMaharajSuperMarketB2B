package com.chhotumaharajsupermarketbusiness.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chhotumaharajsupermarketbusiness.R;
import com.chhotumaharajsupermarketbusiness.constant.Constant;
import com.chhotumaharajsupermarketbusiness.constant.MaintainRequestQueue;
import com.chhotumaharajsupermarketbusiness.constant.SharedPrefrenceObj;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mobile_number, otp_number;
    private Button send_otp, verify_otp,whatsapp;
    private LinearLayout mobile_layout, otp_layout;
    private TextView resend_otp;
    private ImageView back;
    private ProgressDialog progressDialog;
    private String mobileNumber;

    private static final String NAME = "name";
    private static final String EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();


        mobile_number = findViewById(R.id.mobile_number);
        otp_number = findViewById(R.id.otp_number);
        resend_otp = findViewById(R.id.resend_otp);

        send_otp = findViewById(R.id.send_otp);
        verify_otp = findViewById(R.id.verify_otp);
        mobile_layout = findViewById(R.id.mobile_layout);
        otp_layout = findViewById(R.id.otp_layout);
        back = findViewById(R.id.otp_back);
        whatsapp = findViewById(R.id.whatsapp);

        send_otp.setOnClickListener(this);
        verify_otp.setOnClickListener(this);
        back.setOnClickListener(this);
        resend_otp.setOnClickListener(this);
        whatsapp.setOnClickListener(this);

        progressDialog = new ProgressDialog(LoginActivity.this);


        if (SharedPrefrenceObj.getIntegerval(LoginActivity.this, "id") != 0) {
            Intent intent = new Intent(LoginActivity.this, ConceptBusinessActivity.class);
//            Intent intent = new Intent(LoginActivity.this,PreAnalysisNewActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_otp:
                mobileNumber = mobile_number.getText().toString();
                if (mobile_number.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(LoginActivity.this, "Invalid number", Toast.LENGTH_SHORT).show();
                } else if (mobileNumber.length() < 10) {
                    Toast.makeText(LoginActivity.this, "Invalid number", Toast.LENGTH_SHORT).show();
                } else {
                    loginOtp(mobileNumber);
                }
                break;

            case R.id.otp_back:
                mobile_layout.setVisibility(View.VISIBLE);
                otp_layout.setVisibility(View.GONE);
                back.setVisibility(View.GONE);
                break;

            case R.id.verify_otp:
                String otpNumber = otp_number.getText().toString();
                verifyOtp(mobileNumber, otpNumber);
                break;

            case R.id.resend_otp:
                loginOtp(mobileNumber);
                break;

            case R.id.whatsapp:
                chatonWhatsApp();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void loginOtp(final String mobile_no) {

        String url = Constant.URL + "franchiseLoginOtp";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("Getting otp...");
        StringRequest req = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {
                        mobile_layout.setVisibility(View.GONE);
                        otp_layout.setVisibility(View.VISIBLE);
                        otp_number.requestFocus();
                        back.setVisibility(View.VISIBLE);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                    } else {

                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error: " + error.getMessage());
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }) {
            @Override
            public Request.Priority getPriority() {
                return Request.Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("","");
                return params;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", mobile_no.trim());
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

    private void verifyOtp(final String mobile_no, final String otp) {

        String url = Constant.URL + "franchiseLogin";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("Logging in...");
        StringRequest req = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        String name = data.optString("name");
                        String email = data.optString("email");
                        String mobile = data.optString("mobile");
                        int step = data.getInt("step");
                        String apiToken = data.getString("api_token");
                        int id = data.getInt("id");
                        String state = data.optString("state");
                        String city = data.optString("city");
                        String preferedLanguage = data.optString("prefered_language");

                        SharedPrefrenceObj.setSharedValue(LoginActivity.this, "mobile", mobile);
                        SharedPrefrenceObj.setIntegerval(LoginActivity.this, "step", step);
                        SharedPrefrenceObj.setSharedValue(LoginActivity.this, "auth_token", apiToken);
                        SharedPrefrenceObj.setIntegerval(LoginActivity.this, "id", id);
                        SharedPrefrenceObj.setSharedValue(LoginActivity.this, "language", preferedLanguage);
                        SharedPrefrenceObj.setSharedValue(LoginActivity.this, "name", name);
                        SharedPrefrenceObj.setSharedValue(LoginActivity.this, "email", email);
                        SharedPrefrenceObj.setSharedValue(LoginActivity.this, "state", state);
                        SharedPrefrenceObj.setSharedValue(LoginActivity.this, "city", city);
                        //Intent intent = new Intent(LoginActivity.this, ConceptBusinessActivity.class);
                        Intent intent = new Intent(LoginActivity.this, FranchiseActivity.class);
                        startActivity(intent);
                        finish();
                        progressDialog.dismiss();
                    } else {
                        new AlertDialog.Builder(LoginActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Invalid OTP")
                                .setMessage("Please enter valid otp")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }

                                })
                                //   .setNegativeButton("No", null)
                                .show();

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
            public Request.Priority getPriority() {
                return Request.Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("","");
                return params;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", mobile_no.trim());
                params.put("otp", otp.trim());
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

    private void chatonWhatsApp() {

        String smsNumber = "918591108173";
        try {
            PackageManager packageManager = getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone="+ smsNumber +"&text=" + URLEncoder.encode("Hi,", "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            }
            else
            {
                Toast.makeText(LoginActivity.this,"WhatsApp Not Install in This device",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
