package com.chhotumaharajsupermarketbusiness.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
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
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class PreAnalysisActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private ArrayList<String> qualfArray, professionArray,stateArray, landArray, typeArray, sizeArray,business_typeArray,investment_typeArray,investment_budgetArray,decision_makerArray,business_mgmntArray,start_businessArray;
    private Button pre_analysis_next;
    private EditText name,pre_analysis_age,pre_analysis_city,pre_analysis_land_area,pre_analysis_business_details;
    private String ageVal, qualification, profession, stateVal, cityVal, landVal, landType, areaVal, landSizeVal, landLocationVal, businessTypeVal,businessDetailsVal, investmentTypeVal,investmentBudgetVal,decisionMakerVal,businessMgmVal,startBusinessVal;
    private int regionVal, staionlocationVal;
    private Spinner pre_analysis_qlf, pre_analysis_profession, pre_analysis_state, pre_analysis_land, pre_analysis_type,  pre_analysis_size,pre_analysis_business_type,pre_analysis_investment_type,pre_analysis_investment_budget,pre_analysis_decision_maker,pre_analysis_business_mgmnt,pre_analysis_start_business;
    private ProgressDialog progressDialog;
    private CheckBox reg_terms;
    private ScrollView scrollViewMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_analysis);

        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(PreAnalysisActivity.this);
        scrollViewMain = findViewById(R.id.scrollViewMain);
        scrollViewMain.setSmoothScrollingEnabled(true);
        name = findViewById(R.id.pre_analysis_name);
        pre_analysis_age = findViewById(R.id.pre_analysis_age);
        pre_analysis_qlf = findViewById(R.id.pre_analysis_qualification);
        pre_analysis_profession = findViewById(R.id.pre_analysis_profession);

        pre_analysis_state = findViewById(R.id.pre_analysis_state);
        pre_analysis_city = findViewById(R.id.pre_analysis_city);

        pre_analysis_land = findViewById(R.id.pre_analysis_land);
        pre_analysis_type = findViewById(R.id.pre_analysis_landtype);
        pre_analysis_land_area = findViewById(R.id.pre_analysis_land_area);
        pre_analysis_size = findViewById(R.id.pre_analysis_landsize);
        pre_analysis_business_details = findViewById(R.id.pre_analysis_business_details);

        pre_analysis_business_type = findViewById(R.id.pre_analysis_business_type);
        pre_analysis_investment_type = findViewById(R.id.pre_analysis_investment_type);
        pre_analysis_investment_budget = findViewById(R.id.pre_analysis_investment_budget);
        pre_analysis_decision_maker = findViewById(R.id.pre_analysis_decision_maker);
        pre_analysis_business_mgmnt = findViewById(R.id.pre_analysis_business_mgmnt);
        pre_analysis_start_business = findViewById(R.id.pre_analysis_start_business);

        pre_analysis_next = findViewById(R.id.pre_analysis_next);

        reg_terms = findViewById(R.id.reg_terms);

        pre_analysis_next.setOnClickListener(this);

        name.setEnabled(false);

        if (SharedPrefrenceObj.getIntegerval(PreAnalysisActivity.this, "step") == 3) {
            Intent intent = new Intent(PreAnalysisActivity.this, NavigationActivity.class);
            startActivity(intent);
            finish();
        }

        try {
            if (SharedPrefrenceObj.getSharedValue(PreAnalysisActivity.this, "name") != null) {
                name.setText(SharedPrefrenceObj.getSharedValue(PreAnalysisActivity.this, "name"));
            } else {
                name.setText("");
            }

        } catch (Exception e) {

        }
        stateArray = new ArrayList<>();

        qualfArray = new ArrayList<>();
        qualfArray.add("Select Qualification");
        qualfArray.add("Under Graduate");
        qualfArray.add("Graduate");
        qualfArray.add("Post Graduate");

        professionArray = new ArrayList<>();
        professionArray.add("Select Profession");
        professionArray.add("Business");
        professionArray.add("Pvt. Service");
        professionArray.add("Govt. Service");
        professionArray.add("Student");

        landArray = new ArrayList<>();
        landArray.add("Select Land");
        landArray.add("Owned");
        landArray.add("To Be Taken On Lease/Rent");

        typeArray = new ArrayList<>();
        typeArray.add("Select Land Type");
        typeArray.add("Commercial");
        typeArray.add("Agriculture");
        typeArray.add("Non Agriculture");

        sizeArray = new ArrayList<>();
        sizeArray.add("Select Land Size");
        sizeArray.add("2000 to 3000 Sq. Ft.");
        sizeArray.add("3000 to 5000 Sq. Ft.");
        sizeArray.add("5000 to 10000 Sq. Ft.");
        sizeArray.add("Above 10000 Sq. Ft.");

        business_typeArray = new ArrayList<>();
        business_typeArray.add("Select Business Type");
        business_typeArray.add("Retail");
        business_typeArray.add("Real Estate");
        business_typeArray.add("Textile");
        business_typeArray.add("Export");

        investment_typeArray = new ArrayList<>();
        investment_typeArray.add("Select Investment Type");
        investment_typeArray.add("Self/Family Funded");
        investment_typeArray.add("Partial Loan");
        investment_typeArray.add("Fully Dependent On Loan");

        investment_budgetArray = new ArrayList<>();
        investment_budgetArray.add("Select Investment Budget");
        investment_budgetArray.add("75 to 80 L");
        investment_budgetArray.add("81 - Above");

        decision_makerArray = new ArrayList<>();
        decision_makerArray.add("Select Decision Maker");
        decision_makerArray.add("Self");
        decision_makerArray.add("Family Head");
        decision_makerArray.add("Partner");

        business_mgmntArray = new ArrayList<>();
        business_mgmntArray.add("Select Business Management");
        business_mgmntArray.add("Self");
        business_mgmntArray.add("Family Member");
        business_mgmntArray.add("Partner");

        start_businessArray = new ArrayList<>();
        start_businessArray.add("Planning to Start Business");
        start_businessArray.add("Immediately");
        start_businessArray.add("2 to 3 Months");
        start_businessArray.add("4 to 6 Months");
        start_businessArray.add("More Than 6 Months");

        ArrayAdapter<String> categoryAdap = new ArrayAdapter<String>(PreAnalysisActivity.this, R.layout.spinner_text_layout, qualfArray);
        pre_analysis_qlf.setAdapter(categoryAdap);
        categoryAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pre_analysis_qlf.setAdapter(categoryAdap);

        ArrayAdapter<String> professionAdap = new ArrayAdapter<String>(PreAnalysisActivity.this, R.layout.spinner_text_layout, professionArray);
        pre_analysis_profession.setAdapter(professionAdap);
        professionAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pre_analysis_profession.setAdapter(professionAdap);

        final ArrayAdapter<String> land = new ArrayAdapter<String>(PreAnalysisActivity.this, R.layout.spinner_text_layout, landArray);
        pre_analysis_land.setAdapter(land);
        land.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pre_analysis_land.setAdapter(land);

        final ArrayAdapter<String> landtype = new ArrayAdapter<String>(PreAnalysisActivity.this, R.layout.spinner_text_layout, typeArray);
        pre_analysis_type.setAdapter(landtype);
        landtype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pre_analysis_type.setAdapter(landtype);

        ArrayAdapter<String> landsize = new ArrayAdapter<String>(PreAnalysisActivity.this, R.layout.spinner_text_layout, sizeArray);
        pre_analysis_size.setAdapter(landsize);
        landsize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pre_analysis_size.setAdapter(landsize);

        final ArrayAdapter<String> businesstypeAdapter = new ArrayAdapter<String>(PreAnalysisActivity.this, R.layout.spinner_text_layout, business_typeArray);
        pre_analysis_business_type.setAdapter(businesstypeAdapter);
        businesstypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pre_analysis_business_type.setAdapter(businesstypeAdapter);

        final ArrayAdapter<String> investmenttypeAdapter = new ArrayAdapter<String>(PreAnalysisActivity.this, R.layout.spinner_text_layout, investment_typeArray);
        pre_analysis_investment_type.setAdapter(investmenttypeAdapter);
        investmenttypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pre_analysis_investment_type.setAdapter(investmenttypeAdapter);

        final ArrayAdapter<String> investmentbudgetAdapter = new ArrayAdapter<String>(PreAnalysisActivity.this, R.layout.spinner_text_layout, investment_budgetArray);
        pre_analysis_investment_budget.setAdapter(investmentbudgetAdapter);
        investmentbudgetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pre_analysis_investment_budget.setAdapter(investmentbudgetAdapter);

        final ArrayAdapter<String> decisionmakerAdapter = new ArrayAdapter<String>(PreAnalysisActivity.this, R.layout.spinner_text_layout, decision_makerArray);
        pre_analysis_decision_maker.setAdapter(decisionmakerAdapter);
        decisionmakerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pre_analysis_decision_maker.setAdapter(decisionmakerAdapter);

        final ArrayAdapter<String> businessmanagementAdapter = new ArrayAdapter<String>(PreAnalysisActivity.this, R.layout.spinner_text_layout, business_mgmntArray);
        pre_analysis_business_mgmnt.setAdapter(businessmanagementAdapter);
        businessmanagementAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pre_analysis_business_mgmnt.setAdapter(businessmanagementAdapter);

        final ArrayAdapter<String> startbusinessAdapter = new ArrayAdapter<String>(PreAnalysisActivity.this, R.layout.spinner_text_layout, start_businessArray);
        pre_analysis_start_business.setAdapter(startbusinessAdapter);
        startbusinessAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pre_analysis_start_business.setAdapter(startbusinessAdapter);

        pre_analysis_qlf.setOnItemSelectedListener(this);
        pre_analysis_profession.setOnItemSelectedListener(this);
        pre_analysis_state.setOnItemSelectedListener(this);
        pre_analysis_land.setOnItemSelectedListener(this);
        pre_analysis_type.setOnItemSelectedListener(this);
        pre_analysis_size.setOnItemSelectedListener(this);
        pre_analysis_business_type.setOnItemSelectedListener(this);

        pre_analysis_investment_type.setOnItemSelectedListener(this);
        pre_analysis_investment_budget.setOnItemSelectedListener(this);
        pre_analysis_decision_maker.setOnItemSelectedListener(this);
        pre_analysis_business_mgmnt.setOnItemSelectedListener(this);
        pre_analysis_start_business.setOnItemSelectedListener(this);

        getState();

    }

    @Override
    public void onClick(View view) {
        Log.e("onClick","==>first");
        ageVal = pre_analysis_age.getText().toString();
        cityVal = pre_analysis_age.getText().toString();
        businessDetailsVal = pre_analysis_business_details.getText().toString();

        if (pre_analysis_age.getText().toString().equalsIgnoreCase("")) {
            pre_analysis_age.setError("Age Require");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                scrollViewMain.scrollToDescendant(pre_analysis_age);
            }
        }else if (pre_analysis_qlf.getSelectedItem().toString().equalsIgnoreCase("Select Qualification")) {
            TextView errorText = (TextView) pre_analysis_qlf.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Qualification Require");
        }else if (pre_analysis_profession.getSelectedItem().toString().equalsIgnoreCase("Select Profession")) {
            TextView errorText = (TextView) pre_analysis_profession.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Profession Require");
        }else if (pre_analysis_state.getSelectedItem().toString().equalsIgnoreCase("Select State")) {
            TextView errorText = (TextView) pre_analysis_state.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("State Require");
        }else if (pre_analysis_city.getText().toString().equalsIgnoreCase("")) {
            pre_analysis_city.setError("City Require");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                scrollViewMain.scrollToDescendant(pre_analysis_city);
            }
        }else if (pre_analysis_land.getSelectedItem().toString().equalsIgnoreCase("Select Land")) {
            TextView errorText = (TextView) pre_analysis_land.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Land Require");
        } else if (pre_analysis_land.getSelectedItem().toString().equalsIgnoreCase("Owned")) {
            if (pre_analysis_type.getSelectedItem().toString().equalsIgnoreCase("Select Land Type")) {
                TextView errorText = (TextView) pre_analysis_type.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);
                errorText.setText("Land Type Require");
            }else if (pre_analysis_size.getSelectedItem().toString().equalsIgnoreCase("Select Land Size")) {
                TextView errorText = (TextView) pre_analysis_size.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);
                errorText.setText("Land size Require");
            }else if (pre_analysis_land_area.getText().toString().equalsIgnoreCase("")) {
                pre_analysis_land_area.setError("Area Require");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    scrollViewMain.scrollToDescendant(pre_analysis_land_area);
                }
            }else if (pre_analysis_business_type.getSelectedItem().toString().equalsIgnoreCase("Select Business Type")) {
                TextView errorText = (TextView) pre_analysis_business_type.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);
                errorText.setText("Business Type Require");
            }else if (pre_analysis_business_details.getText().toString().equalsIgnoreCase("")) {
                pre_analysis_business_details.setError("Business Detail Require");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    scrollViewMain.scrollToDescendant(pre_analysis_business_details);
                }
            }else if (pre_analysis_investment_type.getSelectedItem().toString().equalsIgnoreCase("Select Investment Type")) {
                TextView errorText = (TextView) pre_analysis_investment_type.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);
                errorText.setText("Investment Type Require");
            }else if (pre_analysis_investment_budget.getSelectedItem().toString().equalsIgnoreCase("Select Investment Budget")) {
                TextView errorText = (TextView) pre_analysis_investment_budget.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);
                errorText.setText("Investment Budget Require");
            }else if (pre_analysis_decision_maker.getSelectedItem().toString().equalsIgnoreCase("Select Decision Maker")) {
                TextView errorText = (TextView) pre_analysis_decision_maker.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);
                errorText.setText("Decision Maker Require");
            }else if (pre_analysis_business_mgmnt.getSelectedItem().toString().equalsIgnoreCase("Select Business Management")) {
                TextView errorText = (TextView) pre_analysis_business_mgmnt.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);
                errorText.setText("Business Management Require");
            }else if (pre_analysis_start_business.getSelectedItem().toString().equalsIgnoreCase("Planning to Start Business")) {
                TextView errorText = (TextView) pre_analysis_start_business.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);
                errorText.setText("Planning to Start Business Require");
            }else if (reg_terms.isChecked() == false) {
                Toast.makeText(PreAnalysisActivity.this, "Please check the checkbox", Toast.LENGTH_SHORT).show();
            }else {
                Log.e("onClick","==>last");
                saveFranchisDetail();
            }
        }
        else if (!pre_analysis_land.getSelectedItem().toString().equalsIgnoreCase("Owned")) {
            if (pre_analysis_land_area.getText().toString().equalsIgnoreCase("")) {
                pre_analysis_land_area.setError("Area Require");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    scrollViewMain.scrollToDescendant(pre_analysis_land_area);
                }
            }else if (pre_analysis_business_type.getSelectedItem().toString().equalsIgnoreCase("Select Business Type")) {
                TextView errorText = (TextView) pre_analysis_business_type.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);
                errorText.setText("Business Type Require");
            }else if (pre_analysis_business_details.getText().toString().equalsIgnoreCase("")) {
                pre_analysis_business_details.setError("Business Detail Require");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    scrollViewMain.scrollToDescendant(pre_analysis_business_details);
                }
            }else if (pre_analysis_investment_type.getSelectedItem().toString().equalsIgnoreCase("Select Investment Type")) {
                TextView errorText = (TextView) pre_analysis_investment_type.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);
                errorText.setText("Investment Type Require");
            }else if (pre_analysis_investment_budget.getSelectedItem().toString().equalsIgnoreCase("Select Investment Budget")) {
                TextView errorText = (TextView) pre_analysis_investment_budget.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);
                errorText.setText("Investment Budget Require");
            }else if (pre_analysis_decision_maker.getSelectedItem().toString().equalsIgnoreCase("Select Decision Maker")) {
                TextView errorText = (TextView) pre_analysis_decision_maker.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);
                errorText.setText("Decision Maker Require");
            }else if (pre_analysis_business_mgmnt.getSelectedItem().toString().equalsIgnoreCase("Select Business Management")) {
                TextView errorText = (TextView) pre_analysis_business_mgmnt.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);
                errorText.setText("Business Management Require");
            }else if (pre_analysis_start_business.getSelectedItem().toString().equalsIgnoreCase("Planning to Start Business")) {
                TextView errorText = (TextView) pre_analysis_start_business.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);
                errorText.setText("Planning to Start Business Require");
            }else if (reg_terms.isChecked() == false) {
                Toast.makeText(PreAnalysisActivity.this, "Please check the checkbox", Toast.LENGTH_SHORT).show();
            }else {
                Log.e("onClick","==>last");
                saveFranchisDetail();
            }
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
            case R.id.pre_analysis_qualification:
                qualification = String.valueOf(adapterView.getSelectedItem());
                break;
            case R.id.pre_analysis_profession:
                profession = String.valueOf(adapterView.getSelectedItem());
                break;
            case R.id.pre_analysis_state:
                stateVal = String.valueOf(adapterView.getSelectedItem());
                break;
            case R.id.pre_analysis_land:
                if (!pre_analysis_land.getSelectedItem().toString().equalsIgnoreCase("Select Land")) {

                    if (pre_analysis_land.getSelectedItem().toString().equalsIgnoreCase("Owned")) {
                        pre_analysis_type.setVisibility(View.VISIBLE);
                        pre_analysis_size.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        pre_analysis_type.setVisibility(View.GONE);
                        pre_analysis_size.setVisibility(View.GONE);
                    }
                } else {
                    pre_analysis_type.setVisibility(View.GONE);
                    pre_analysis_size.setVisibility(View.GONE);
                }
                landVal = String.valueOf(adapterView.getSelectedItem());
                break;
            case R.id.pre_analysis_landtype:
                landType = String.valueOf(adapterView.getSelectedItem());
                break;
            case R.id.pre_analysis_landsize:
                landSizeVal = String.valueOf(adapterView.getSelectedItem());
                break;
            case R.id.pre_analysis_business_type:
                businessTypeVal = String.valueOf(adapterView.getSelectedItem());
                break;
            case R.id.pre_analysis_investment_type:
                investmentTypeVal = String.valueOf(adapterView.getSelectedItem());
                break;
            case R.id.pre_analysis_investment_budget:
                investmentBudgetVal = String.valueOf(adapterView.getSelectedItem());
                break;
            case R.id.pre_analysis_decision_maker:
                decisionMakerVal = String.valueOf(adapterView.getSelectedItem());
                break;
            case R.id.pre_analysis_business_mgmnt:
                businessMgmVal = String.valueOf(adapterView.getSelectedItem());
                break;
            case R.id.pre_analysis_start_business:
                startBusinessVal = String.valueOf(adapterView.getSelectedItem());
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void saveFranchisDetail() {

        //String url = Constant.URL + "savefranchise";
        String url = Constant.URL + "saveform3";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("Saving data....");
        StringRequest req = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {

                        SharedPrefrenceObj.setIntegerval(PreAnalysisActivity.this, "step", 3);

                        Intent intent = new Intent(PreAnalysisActivity.this, ConceptBusinessActivity.class);
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
                params.put("authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(PreAnalysisActivity.this, "auth_token"));
                params.put("content-type", "application/x-www-form-urlencoded");
                Log.d("params..", params.toString());
                return params;
            }

            //
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("age", ageVal);
                params.put("qualification", qualification);
                params.put("profession", profession);
                params.put("states", stateVal);
                params.put("cities", cityVal);
                params.put("land", landVal);
                params.put("land_type", landType);
                params.put("area_with_landmark", areaVal);
                params.put("land_size", landSizeVal);
                params.put("business_type", businessTypeVal);
                /*params.put("business_type", businessTypeVal);*/
                params.put("id",String.valueOf(SharedPrefrenceObj.getIntegerval(PreAnalysisActivity.this, "id")));

                params.put("investment_type", investmentTypeVal);
                params.put("budget", investmentBudgetVal);
                params.put("business_mgmnt", businessMgmVal);
                params.put("decision_maker", decisionMakerVal);
                params.put("start_business", startBusinessVal);
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

                        ArrayAdapter<String> statee = new ArrayAdapter<String>(PreAnalysisActivity.this, R.layout.spinner_text_layout, stateArray);
                        pre_analysis_state.setAdapter(statee);
                        statee.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        pre_analysis_state.setAdapter(statee);



                        if (SharedPrefrenceObj.getSharedValue(PreAnalysisActivity.this, "state") != null) {

                            for(int i = 0; i < pre_analysis_state.getCount(); i++){
                                if(pre_analysis_state.getItemAtPosition(i).toString().equals(""+SharedPrefrenceObj.getSharedValue(PreAnalysisActivity.this, "state"))){
                                    stateVal = ""+SharedPrefrenceObj.getSharedValue(PreAnalysisActivity.this, "state");
                                    pre_analysis_state.setSelection(i);
                                    break;
                                }
                            }
                        } else {
                            pre_analysis_state.setSelection(0);
                        }

                        if (SharedPrefrenceObj.getSharedValue(PreAnalysisActivity.this, "city") != null) {
                            pre_analysis_city.setText(SharedPrefrenceObj.getSharedValue(PreAnalysisActivity.this, "city"));
                            cityVal = ""+SharedPrefrenceObj.getSharedValue(PreAnalysisActivity.this, "city");
                        } else {
                            pre_analysis_city.setText("");
                            cityVal = "";
                        }


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

}
