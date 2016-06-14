package com.oneshoppoint.yates.yates;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by stephineosoro on 31/05/16.
 */

public class Registration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "CreatePrescription";
    public static List<String> medictype = new ArrayList<String>();
    public static List<String> medictypeID = new ArrayList<String>();
    public static List<String> location = new ArrayList<String>();
    public static List<String> locationID = new ArrayList<String>();
    Spinner spinner2;
    static String lID;
    @Bind(R.id.btn_signup)
    Button _signupButton;
    @Bind(R.id.input_name)
    EditText firstName;
    @Bind(R.id.last_name)
    EditText lastName;
    @Bind(R.id.email)
    EditText _email;
    @Bind(R.id.input_street)
    EditText street;
    @Bind(R.id.residential_name)
    EditText __residential;
    @Bind(R.id.phone_number)
    EditText _phone;
    @Bind(R.id.input_password)
    EditText _password;
    @Bind(R.id.confirm_password)
    EditText _confirm;
    @Bind(R.id.national_id)
    EditText _nationalID;
    @Bind(R.id.medical_id)
    EditText _medicalID;
    @Bind(R.id.etlocation)
    AutoCompleteTextView _Location;


    int succ = 0;
    private static String medicTypeID, selected_item;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Medic Registration");
        ButterKnife.bind(this);
        this.spinner2 = (Spinner) findViewById(R.id.inn_spinner);
        spinner2.setOnItemSelectedListener(this);
        GetLocations();
        GetMedicType();


        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();

            }
        });


    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

//        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(Registration.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();


        // TODO: Implement your own signup logic here.
        storeprescription();
//        saveResults();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
//                        onSignupSuccess();
                        // onSignupFailed();
                        if (succ == 1) {
                            Intent intent = new Intent(getBaseContext(), ShowPatients.class);
                            startActivity(intent);
                        }
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
//        Toast.makeText(getBaseContext(), "Creating Patient failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String password = _password.getText().toString();
        String confirm = _confirm.getText().toString();
        String email = _email.getText().toString();
        String lastname = lastName.getText().toString();
        String firstname = firstName.getText().toString();
        String phone = _phone.getText().toString();
        String medicalid = _medicalID.getText().toString();
        String nationalid = _nationalID.getText().toString();
        String streetname = street.getText().toString();
        String residentialName = __residential.getText().toString();
        String location = _Location.getText().toString();


        if (medicalid.isEmpty() || medicalid.length() < 3) {
            _medicalID.setError("Input medical ID");
            valid = false;
        } else {
            _medicalID.setError(null);
        }
        if (nationalid.isEmpty() || nationalid.length() < 3) {
            _nationalID.setError("Input national ID");
            valid = false;
        } else {
            _nationalID.setError(null);
        }
        if (residentialName.isEmpty()) {
            __residential.setError("Input residential area");
            valid = false;
        } else {
            __residential.setError(null);
        }
        if (streetname.isEmpty()) {
            street.setError("Street");
            valid = false;
        } else {
            street.setError(null);
        }
        if (password.isEmpty() || password.length() < 6) {
            _password.setError("Input vald");
            valid = false;
        } else {
            _password.setError(null);
        }
        if (confirm.isEmpty() || password.length() < 6) {
            _confirm.setError("Input first name");
            valid = false;
        } else {
            _confirm.setError(null);
        }


        if (firstname.isEmpty() || firstname.length() < 3) {
            firstName.setError("Input first name");
            valid = false;
        } else {
            firstName.setError(null);
        }
        if (location.isEmpty()) {
            _Location.setError("Input location");
            valid = false;
        } else {
            _Location.setError(null);
        }
        if (firstname.isEmpty()) {
            firstName.setError("Input first name");
            valid = false;
        } else {
            firstName.setError(null);
        }
        if (lastname.isEmpty()) {
            lastName.setError("Input last name");
            valid = false;
        } else {
            lastName.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _email.setError("input a valid email");
            valid = false;
        } else {
            _email.setError(null);
        }

        if (phone.isEmpty()) {
            _phone.setError("Invalid phone number");
            valid = false;
        } else {
            _phone.setError(null);
        }
        if (!password.equals(confirm)) {
            _password.setError("passwords do not match");
            _confirm.setError("passwords do not match");
            valid = false;
        } else {
            _phone.setError(null);
        }

        return valid;
    }

    private void storeprescription() {
        // Toast.makeText(getBaseContext(), "Inside function!", Toast.LENGTH_SHORT).show();
        JSONObject finalJS = new JSONObject();
        try {


            JSONObject js = new JSONObject();
            JSONArray ja = new JSONArray();

            finalJS.put("password", _password.getText().toString());
            finalJS.put("lastname", lastName.getText().toString());
            finalJS.put("firstname", firstName.getText().toString());
            finalJS.put("medicalId", _medicalID.getText().toString());
            finalJS.put("nationalId", _nationalID.getText().toString());
            js.put("email", _email.getText().toString());
            js.put("phoneNumber", _phone.getText().toString());
            js.put("streetAddress", street.getText().toString());
            js.put("residentialName", __residential.getText().toString());
            js.put("locationId", lID);
//            ja.put(js);
            finalJS.put("address", js);
            finalJS.put("registration", true);
            finalJS.put("enabled", false);
            finalJS.put("customer", false);
            /*finalJS.put("carrier", false);
            finalJS.put("affiliate", false);
            finalJS.put("pharmacist", false);*/
            finalJS.put("medicTypeId", medicTypeID);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSONErrorin serializing", e.toString());
        }
        Log.e("JSON serializing", finalJS.toString());
        String tag_string_req = "req_Categories";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, "https://www.oneshoppoint.com/api/user/", finalJS,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response from server is", response.toString());

                        try {


//
                            String status = response.getString("status");
                            if (status.equals("CREATED")) {
                                Toast.makeText(getBaseContext(), "You have successfully registered to the medics program.We " +
                                        "will send you an email once your registration has been verified by the appropriate " +
                                        "regulatory body. ", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getBaseContext(), MedicLogin.class);

                                startActivity(intent);
                            } else {
                                MyShortcuts.showToast(response.getString("message"), getBaseContext());
                            }
//

//
                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getBaseContext(), "Server errror, Try again later", Toast.LENGTH_LONG).show();
                        }
                    }


                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("VolleyError", "Error: " + error.getMessage());
//                hideProgressDialog()
                MyShortcuts.showToast("Check you internet connection or try again later", getBaseContext());
                Log.d("error volley", error.toString());
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                setRetryPolicy(new DefaultRetryPolicy(5 * DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
                setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
                headers = MyShortcuts.AunthenticationHeaders(getBaseContext());
                return headers;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
        Log.e("request is", jsonObjReq.toString());
    }

    private void GetMedicType() {
        Log.d("URL is", "https://www.oneshoppoint.com/api/inn/");
        String tag_string_req = "req_medicType";
        StringRequest strReq = new StringRequest(Request.Method.GET, "https://www.oneshoppoint.com/api/medictype/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response from server is", response.toString());


                String success = null;
                try {

                    if (medictype.size() > 0 && medictypeID.size() > 0) {
                        medictypeID.clear();
                        medictype.clear();
                    }
                    JSONObject jObj = new JSONObject(response);
                    JSONArray data = jObj.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);
                        String name = c.getString("name");
                        Log.e("Each data" + i, name);
                        medictype.add(name);
                        medictypeID.add(c.getString("id"));

                        Log.e("Each data" + i, c.getString("id"));
                    }

                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),
                            android.R.layout.simple_spinner_item, medictype);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adapter);


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
//                    Toast.makeText(getBaseContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("JSON ERROR", e.toString());
                }
            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("VolleyError", "Error: " + error.getMessage());
//                hideProgressDialog();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                setRetryPolicy(new DefaultRetryPolicy(5 * DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
                setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
                headers = MyShortcuts.AunthenticationHeaders(getBaseContext());
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
//                Log.e("category id", getIntent().getStringExtra("category_id"));
//                params.put("categoryId", 2 + "");


                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        Log.e("request is", strReq.toString());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.getItemAtPosition(position);
        medicTypeID = medictypeID.get(position);
        selected_item = parent.getItemAtPosition(position).toString();
        Log.e("medictype ID", medicTypeID);
        Log.e("Medic type", selected_item);
//        Toast.makeText(getBaseContext(), selected_item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void GetLocations() {
        Log.d("URL is", "https://www.oneshoppoint.com/api/location/");
        String tag_string_req = "req_inn";
        StringRequest strReq = new StringRequest(Request.Method.GET, "https://www.oneshoppoint.com/api/location/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response from server is", response.toString());


                String success = null;
                try {
                    if (location.size() > 0 && locationID.size() > 0) {
                        location.clear();
                        locationID.clear();
                    }
                    JSONObject jObj = new JSONObject(response);
                    JSONArray data = jObj.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);
                        String name = c.getString("name");
                        Log.e("Each data" + i, name);
                        location.add(name);
                        locationID.add(c.getString("id"));

                        Log.e("Each data" + i, c.getString("id"));
                    }


                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),
                            android.R.layout.simple_dropdown_item_1line, location);

                    _Location.setAdapter(adapter);

                    _Location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);

//                            TheSacco = Country.getText().toString();
                            String l = _Location.getText().toString();
                            for (int i = 0; i < location.size(); i++) {
                                if (location.get(i).equals(l)) {
                                    lID = locationID.get(i);
                                    Log.e("Location ID chosen is", lID);
                                }
                            }

                        }
                    });


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("JSON ERROR", e.toString());
                }
            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("VolleyError", "Error: " + error.getMessage());
//                hideProgressDialog();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                setRetryPolicy(new DefaultRetryPolicy(5 * DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
                setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
                headers = MyShortcuts.AunthenticationHeaders(getBaseContext());
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
//                Log.e("category id", getIntent().getStringExtra("category_id"));
//                params.put("categoryId", 2 + "");


                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        Log.e("request is", strReq.toString());
    }
}