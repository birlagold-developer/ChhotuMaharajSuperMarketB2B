package com.chhotumaharajsupermarketbusiness.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FranchiseActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText name, mobile, email, city;
    private CheckBox check;
    private String nameVal, mobileVal, emailVal, cityVal;
    private Button submit;
    private ProgressDialog progressDialog;
    //   Spinner language;
    private ArrayList<String> languageArray;
    private ArrayList<String> stateArray;
    private String languageVal, stateVal;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private Spinner language, state;
    private TextView terms_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_franchise);

        getSupportActionBar().hide();

        TextView tv = findViewById(R.id.chottu_txt);
        TextView tv1 = findViewById(R.id.maharaj_txt);
        Typeface face = Typeface.createFromAsset(getAssets(), "cooperblackstd.otf");

        tv.setTypeface(face);
        tv1.setTypeface(face);

        name = findViewById(R.id.franchise_name);
        email = findViewById(R.id.franchise_email);
        state = findViewById(R.id.franchise_state);
        city = findViewById(R.id.franchise_city);
        check = findViewById(R.id.reg_terms);
        language = findViewById(R.id.franchise_language);
        terms_txt = findViewById(R.id.terms_text);

        progressDialog = new ProgressDialog(FranchiseActivity.this);

        terms_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(FranchiseActivity.this)
                        .setTitle("Terms & Condition")
                        .setMessage("I hereby request & Authorize KSS to contact me on my above registered phone number,Whatsapp & Email Id.\n" +
                                "\n" +
                                "I acknowledge & allow such official approach by KSS with TRAI rules & regulations rules."
                        )
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        languageVal = "";

        stateArray = new ArrayList<>();
        languageArray = new ArrayList<>();
        languageArray.add("Select Language");

        if (SharedPrefrenceObj.getIntegerval(FranchiseActivity.this, "step") == 1) {
            //Intent intent = new Intent(FranchiseActivity.this, ConceptBusinessActivity.class);
            Intent intent = new Intent(FranchiseActivity.this, PreAnalysisActivity.class);
            startActivity(intent);
            finish();
//            return;
        } else if (SharedPrefrenceObj.getIntegerval(FranchiseActivity.this, "step") == 2) {
            Intent intent = new Intent(FranchiseActivity.this, NavigationActivity.class);
            startActivity(intent);
            finish();
//            return;
        } else if (SharedPrefrenceObj.getIntegerval(FranchiseActivity.this, "step") == 3) {
            Intent intent = new Intent(FranchiseActivity.this, NavigationActivity.class);
            intent.putExtra("query_page", "0");
            startActivity(intent);
            finish();
//            return;
        } else if (SharedPrefrenceObj.getIntegerval(FranchiseActivity.this, "step") == 4) {
            Intent intent = new Intent(FranchiseActivity.this, NavigationActivity.class);
            intent.putExtra("query_page", "0");
            startActivity(intent);
            finish();
//            return;
        } else if (SharedPrefrenceObj.getIntegerval(FranchiseActivity.this, "step") == 5) {
            Intent intent = new Intent(FranchiseActivity.this, NavigationActivity.class);
            intent.putExtra("query_page", "0");
            startActivity(intent);
            finish();
//            return;
        }

        submit = findViewById(R.id.franchise_submit);
        submit.setOnClickListener(this);

        ArrayAdapter<String> lang = new ArrayAdapter<String>(FranchiseActivity.this, R.layout.spinner_text_layout, languageArray);
        language.setAdapter(lang);
        lang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        language.setAdapter(lang);

        getLanguagerequest();

        getState();

        language.setOnItemSelectedListener(this);
        state.setOnItemSelectedListener(this);

       /* FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("newToken", newToken);
                SharedPrefrenceObj.setSharedValue(FranchiseActivity.this, "token", newToken);

                saveFCMToken(SharedPrefrenceObj.getSharedValue(FranchiseActivity.this, "mobile"), newToken);

            }
        });*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.franchise_submit:

                nameVal = name.getText().toString();
                emailVal = email.getText().toString().trim();
                cityVal = city.getText().toString();

                if (name.getText().toString().equalsIgnoreCase("")) {
                    name.setError("Invalid name");
                } else if (email.getText().toString().equalsIgnoreCase("")) {
                    email.setError("Invalid email");
                } else if (!isValidEmail(emailVal)) {
                    email.setError("Invalid email");
                } else if (state.getSelectedItem().toString().equalsIgnoreCase("Select State")) {
                    TextView errorText = (TextView) state.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Select State");
                } else if (city.getText().toString().equalsIgnoreCase("")) {
                    city.setError("Invalid city");
                    //Toast.makeText(FranchiseActivity.this,"Invalid city",Toast.LENGTH_SHORT).show();
                } else if (languageVal.equalsIgnoreCase("Select Language")) {
                    TextView errorText = (TextView) language.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Select language");
                    // Toast.makeText(FranchiseActivity.this,"Select language",Toast.LENGTH_SHORT).show();

                } else if (check.isChecked() == false) {
                    Toast.makeText(FranchiseActivity.this, "Please accept terms and condition", Toast.LENGTH_SHORT).show();
                } else {
                    saveFranchiserequest(nameVal, emailVal, stateVal, cityVal, languageVal);
                }

                break;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        TextView textView = (TextView) view;

        if (textView != null) {
            textView.setSingleLine(false);
            textView.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3.0f, getResources().getDisplayMetrics()), 1.0f);

            String firstValue = (String) adapterView.getItemAtPosition(0);
            if (adapterView.getSelectedItemPosition() == 0) {
                textView.setText(firstValue);
            } else {
                textView.setText(Html.fromHtml(firstValue + "<font color='#ED3237'><br/>" + adapterView.getSelectedItem().toString() + "</font>"));
            }
        }

        switch (adapterView.getId()) {
            case R.id.franchise_language:
                languageVal = String.valueOf(adapterView.getSelectedItem());
                break;
            case R.id.franchise_state:
                stateVal = String.valueOf(adapterView.getSelectedItem());
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void getLanguagerequest() {



        String url = Constant.URL + "language";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        progressDialog.show();
        progressDialog.setMessage("loading data....");
        StringRequest req = new StringRequest(com.android.volley.Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {
                        //{"success":true,"error":"","data":{"languages":["Tamil","Hindi","English","Telegu"]}}
                        JSONArray languagesArray = jsonObject.optJSONObject("data").getJSONArray("languages");
                        for (int i = 0; i < languagesArray.length(); i++) {
                            languageArray.add(languagesArray.getString(i));
                        }

                        SharedPrefrenceObj.setSharedValue(FranchiseActivity.this, "all_language", jsonObject.optJSONObject("data").toString());

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
            public Request.Priority getPriority() {
                return Request.Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(FranchiseActivity.this, "auth_token"));
                params.put("content-type", "application/x-www-form-urlencoded");
                Log.d("params..", params.toString());
                return params;
            }

            //
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
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

    private void saveFranchiserequest(final String name, final String email, final String state,
                                      final String city, final String language) {

        String url = Constant.URL + "saveFranchiserequest";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("Saving data....");
        StringRequest req = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {

                        SharedPrefrenceObj.setIntegerval(FranchiseActivity.this, "step", 1);
                        SharedPrefrenceObj.setSharedValue(FranchiseActivity.this, "language", language);
                        SharedPrefrenceObj.setSharedValue(FranchiseActivity.this, "name", name);
                        SharedPrefrenceObj.setSharedValue(FranchiseActivity.this, "email", email);
                        SharedPrefrenceObj.setSharedValue(FranchiseActivity.this, "state", state);
                        SharedPrefrenceObj.setSharedValue(FranchiseActivity.this, "city", city);

                        Intent intent = new Intent(FranchiseActivity.this, ConceptBusinessActivity.class);
                        intent.putExtra("sec", 0);
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
            public Request.Priority getPriority() {
                return Request.Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(FranchiseActivity.this, "auth_token"));
                params.put("content-type", "application/x-www-form-urlencoded");
                Log.d("params..", params.toString());
                return params;
            }

            //
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("states", state);
                params.put("cities", city);
                params.put("prefered_language", language);
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
                params.put("authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(FranchiseActivity.this, "auth_token"));
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

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void getState() {

        String url = Constant.URL + "state";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("Saving data....");
        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {

                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject1 = data.getJSONObject(i);
                            String stateName = jsonObject1.getString("state_name");

                            stateArray.add(stateName);

                        }
                        stateArray.add(0, "Select State");

                        ArrayAdapter<String> statee = new ArrayAdapter<String>(FranchiseActivity.this, R.layout.spinner_text_layout, stateArray);
                        state.setAdapter(statee);
                        statee.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        state.setAdapter(statee);

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
            public Request.Priority getPriority() {
                return Request.Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //       params.put("authorization","Bearer "+SharedPrefrenceObj.getSharedValue(FranchiseActivity.this,"auth_token"));
                params.put("content-type", "application/x-www-form-urlencoded");
                Log.d("params..", params.toString());
                return params;
            }

            //
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();


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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }
}