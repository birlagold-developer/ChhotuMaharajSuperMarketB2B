package com.chhotumaharajsupermarketbusiness.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chhotumaharajsupermarketbusiness.R;
import com.chhotumaharajsupermarketbusiness.constant.Constant;
import com.chhotumaharajsupermarketbusiness.constant.MaintainRequestQueue;
import com.chhotumaharajsupermarketbusiness.constant.SharedPrefrenceObj;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class LiveDemoFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    public static View fragment;
    //  Spinner
    Spinner demo_slot;
    EditText date;
    Button submit;
    ArrayList<String> slotArray;
    private int mYear, mMonth, mDay;
    String visitDate, visitTime;
    ProgressDialog progressDialog;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.activity_live_demo_visit, container, false);

        progressDialog = new ProgressDialog(getActivity());

        demo_slot = fragment.findViewById(R.id.live_demo_slot);
        date = fragment.findViewById(R.id.booking_date);
        submit = fragment.findViewById(R.id.appointment_submit);


        demo_slot.setOnItemSelectedListener(this);
        submit.setOnClickListener(this);

        slotArray = new ArrayList<>();
        slotArray.add("Select Time Slot");
        slotArray.add("10:00 AM to 11:00 AM");
        slotArray.add("11:00 AM to 12:00 PM");
        slotArray.add("12:00 PM to 1:00 PM");
        slotArray.add("1:00 PM to 2:00 PM");
        slotArray.add("2:00 PM to 3:00 PM");
        slotArray.add("3:00 PM to 4:00 PM");
        slotArray.add("4:00 PM to 5:00 PM");
        slotArray.add("5:00 PM to 6:00 PM");
        slotArray.add("6:00 PM to 7:00 PM");
        slotArray.add("7:00 PM to 8:00 PM");

        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                c.add(Calendar.DAY_OF_MONTH, 2);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String monthString = String.valueOf((monthOfYear + 1));
                                if (monthString.length() == 1) {
                                    monthString = "0" + monthString;
                                }
                                String dayMonth = String.valueOf(dayOfMonth);
                                if (dayMonth.length() == 1) {
                                    dayMonth = "0" + dayMonth;
                                }
                                date.setText(year + "-" + monthString + "-" + dayMonth);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                DatePicker datePicker = datePickerDialog.getDatePicker();
                datePicker.setMinDate(c.getTimeInMillis());
            }
        });

        ArrayAdapter<String> slot = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text_layout, slotArray);
        demo_slot.setAdapter(slot);
        slot.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        demo_slot.setAdapter(slot);

        return fragment;
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
                        return true;
                    }
                }
                return false;
            }
        });
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

            case R.id.live_demo_slot:
                visitTime = adapterView.getSelectedItem().toString();
                break;
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.appointment_submit:
                visitDate = date.getText().toString();
                 if (date.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Select date", Toast.LENGTH_SHORT).show();
                } else if (demo_slot.getSelectedItem().toString().equalsIgnoreCase("Select Time Slot")) {
                    Toast.makeText(getActivity(), "Select time slot", Toast.LENGTH_SHORT).show();

                } else {
                    liveVisitSubmit(visitDate);
                }

                break;

            default:
                break;
        }
    }

    private void liveVisitSubmit(final String visitDate) {

        String url = Constant.URL + "saveappointment";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);

        progressDialog.show();
        progressDialog.setMessage("Booking appointment.....");

        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {
                        MainFragment mainFragment = new MainFragment();
                        Bundle args = new Bundle();
                        args.putString("topic", "topic");
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.frame_container, mainFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }


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

            //appointment_date,user_id,sub_query,query_type,timeslot,location
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("appointment_date", visitDate);
                params.put("amount", "");
                params.put("timeslot", visitTime);
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

}
