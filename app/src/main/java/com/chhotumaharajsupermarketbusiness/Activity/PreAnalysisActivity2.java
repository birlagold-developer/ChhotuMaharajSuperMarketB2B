package com.chhotumaharajsupermarketbusiness.Activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class PreAnalysisActivity2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    ArrayList<String> typeArray, budgetArray, makerArray, businessArray, businesstimeArray;
    String name, age, state, city, qualification, profession, land, land_type, land_size;
    String investmentType, Budget, decisionMaker, business, businessTime;
    Button submit;
    Spinner pre_analysis_type, pre_analysis_budget, pre_analysis_maker, pre_analysis_business, pre_analysis_time;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_analysis2);

        progressDialog = new ProgressDialog(PreAnalysisActivity2.this);

        getSupportActionBar().hide();

        TextView tv = findViewById(R.id.chottu_txt);
        TextView tv1 = findViewById(R.id.maharaj_txt);
        Typeface face = Typeface.createFromAsset(getAssets(), "cooperblackstd.otf");

        tv.setTypeface(face);
        tv1.setTypeface(face);

        name = getIntent().getStringExtra("name");
        age = getIntent().getStringExtra("age");
        state = getIntent().getStringExtra("state");
        city = getIntent().getStringExtra("city");
        qualification = getIntent().getStringExtra("qualification");
        profession = getIntent().getStringExtra("profession");
        land = getIntent().getStringExtra("land");
        land_type = getIntent().getStringExtra("land_type");
        land_size = getIntent().getStringExtra("land_size");

        pre_analysis_budget = findViewById(R.id.pre_analysis_budget);
        pre_analysis_maker = findViewById(R.id.pre_analysis_maker);
        pre_analysis_business = findViewById(R.id.pre_analysis_business);
        pre_analysis_type = findViewById(R.id.pre_analysis_investtype);
        pre_analysis_time = findViewById(R.id.pre_analysis_business_time);
        submit = findViewById(R.id.pre_analysis_submit);

        typeArray = new ArrayList<>();
        typeArray.add("Select Investment Type");
        typeArray.add("Self / Family");
        typeArray.add("Partial Loan");
        typeArray.add("Fully Dependent On Loan");

        budgetArray = new ArrayList<>();
        budgetArray.add("Select Investment Budget");
        budgetArray.add("15 to 20 L");
        budgetArray.add("21 to 35 L");
        budgetArray.add("36 to 50 L");
        budgetArray.add("51 to 75 L");

        makerArray = new ArrayList<>();
        makerArray.add("Select Decision Maker");
        makerArray.add("Self");
        makerArray.add("Family Head");
        makerArray.add("Partner/s");

        businessArray = new ArrayList<>();
        businessArray.add("Who Will Look After Day-to-Day Operations Of This Business?");
        businessArray.add("Self");
        businessArray.add("Family Head");
        businessArray.add("Partner/s");

        businesstimeArray = new ArrayList<>();
        businesstimeArray.add("When you are Planning to Start This Business?");
        businesstimeArray.add("Immediately");
        businesstimeArray.add("2 to 4 Months");
        businesstimeArray.add("4 to 6 Months");
        businesstimeArray.add("More than 6 Months");


        ArrayAdapter<String> categoryAdap = new ArrayAdapter<String>(PreAnalysisActivity2.this, R.layout.spinner_text_layout, typeArray);
        pre_analysis_type.setAdapter(categoryAdap);
        categoryAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pre_analysis_type.setAdapter(categoryAdap);

        ArrayAdapter<String> profession = new ArrayAdapter<String>(PreAnalysisActivity2.this, R.layout.spinner_text_layout, budgetArray);
        pre_analysis_budget.setAdapter(profession);
        profession.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pre_analysis_budget.setAdapter(profession);

        ArrayAdapter<String> land = new ArrayAdapter<String>(PreAnalysisActivity2.this, R.layout.spinner_text_layout, makerArray);
        pre_analysis_maker.setAdapter(land);
        land.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pre_analysis_maker.setAdapter(land);

        ArrayAdapter<String> landtype = new ArrayAdapter<String>(PreAnalysisActivity2.this, R.layout.spinner_text_layout, businessArray);
        pre_analysis_business.setAdapter(landtype);
        landtype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pre_analysis_business.setAdapter(landtype);

        ArrayAdapter<String> landsize = new ArrayAdapter<String>(PreAnalysisActivity2.this, R.layout.spinner_text_layout, businesstimeArray);
        pre_analysis_time.setAdapter(landsize);
        landsize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pre_analysis_time.setAdapter(landsize);

        pre_analysis_type.setOnItemSelectedListener(this);
        pre_analysis_budget.setOnItemSelectedListener(this);
        pre_analysis_maker.setOnItemSelectedListener(this);
        pre_analysis_business.setOnItemSelectedListener(this);
        pre_analysis_time.setOnItemSelectedListener(this);

        submit.setOnClickListener(this);
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
            case R.id.pre_analysis_investtype:
                investmentType = String.valueOf(adapterView.getSelectedItem());
                break;
            case R.id.pre_analysis_budget:
                Budget = String.valueOf(adapterView.getSelectedItem());
                break;
            case R.id.pre_analysis_maker:
                decisionMaker = String.valueOf(adapterView.getSelectedItem());
                break;
            case R.id.pre_analysis_business:
                business = String.valueOf(adapterView.getSelectedItem());
                break;
            case R.id.pre_analysis_business_time:
                businessTime = String.valueOf(adapterView.getSelectedItem());
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {

        if (pre_analysis_type.getSelectedItem().toString().equalsIgnoreCase("Select Investment Type")) {
            TextView errorText = (TextView) pre_analysis_type.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Select Investment Type");
            //  Toast.makeText(PreAnalysisActivity2.this,"Select Investment Type",Toast.LENGTH_SHORT).show();

        } else if (pre_analysis_budget.getSelectedItem().toString().equalsIgnoreCase("Select Investment Budget")) {
            TextView errorText = (TextView) pre_analysis_budget.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Select Investment Budget");
            //      Toast.makeText(PreAnalysisActivity2.this,"Select Investment Budget",Toast.LENGTH_SHORT).show();

        } else if (pre_analysis_maker.getSelectedItem().toString().equalsIgnoreCase("Select Decision Maker")) {
            TextView errorText = (TextView) pre_analysis_maker.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Select Decision Maker");
            //   Toast.makeText(PreAnalysisActivity2.this,"Select Decision Maker",Toast.LENGTH_SHORT).show();

        } else if (pre_analysis_business.getSelectedItem().toString().equalsIgnoreCase("Who Will Look After Day-to-Day Operations Of This Business?")) {
            TextView errorText = (TextView) pre_analysis_business.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Who Will Look After Day-to-Day Operations Of This Business?");
            //      Toast.makeText(PreAnalysisActivity2.this,"Who Will Look After Day-to-Day Operations Of This Business?",Toast.LENGTH_SHORT).show();

        } else if (pre_analysis_time.getSelectedItem().toString().equalsIgnoreCase("When you are Planning to Start This Business?")) {
            TextView errorText = (TextView) pre_analysis_time.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("When you are Planning to Start This Business?");
            //  Toast.makeText(PreAnalysisActivity2.this,"When you are Planning to Start This Business?",Toast.LENGTH_SHORT).show();
        } else {
            saveForm3();
        }
    }

    private void saveForm3() {

        String url = Constant.URL + "saveform3";
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
                        SharedPrefrenceObj.setIntegerval(PreAnalysisActivity2.this, "step", 3);

                        /*Intent intent = new Intent(PreAnalysisActivity2.this, InterestedActivity.class);
                        intent.putExtra("concept", "interested");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);*/

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
                params.put("authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(PreAnalysisActivity2.this, "auth_token"));
                params.put("content-type", "application/x-www-form-urlencoded");
                Log.d("params..", params.toString());
                return params;
            }

            //
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", String.valueOf(SharedPrefrenceObj.getIntegerval(PreAnalysisActivity2.this, "id")));
                params.put("age", age);
                params.put("qualification", qualification);
                params.put("profession", profession);
                params.put("land", land);
                params.put("states", state);
                params.put("cities", city);
                params.put("land_type", land_type);
                params.put("land_size", land_size);
                params.put("investment_type", investmentType);
                params.put("budget", Budget);
                params.put("business_mgmnt", business);
                params.put("decision_maker", decisionMaker);
                params.put("start_business", businessTime);
                params.put("step", "3");
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
    //    //id,qualification,profession,land,state,city,land_type,land_size,investment_type,budget,business_mgmnt,decision_maker,start_business,step=3
}
