package com.oneshoppoint.yates.yates;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import android.widget.TextView;
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

import butterknife.ButterKnife;
import butterknife.Bind;

/**
 * Created by stephineosoro on 31/05/16.
 */

public class GetPrescription extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    public static List<String> location = new ArrayList<String>();
    public static List<String> locationID = new ArrayList<String>();
    static String lID;
    @Bind(R.id.uuid)
    EditText _uuid;
    @Bind(R.id.etlocation)
    AutoCompleteTextView _Location;
    @Bind(R.id.btn_signup)
    Button _signupButton;
    @Bind(R.id.error_message)
    TextView errormessage;

    int succ = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_prescription);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Get Prescription");
        ButterKnife.bind(this);
        GetLocations();

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
//                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
//                startActivity(intent);
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

//        final ProgressDialog progressDialog = new ProgressDialog(GetPrescription.this,
//                R.style.AppTheme_Dark_Dialog);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Creating Account...");
//        progressDialog.show();


        // TODO: Implement your own signup logic here.
        storesignup();
//        saveResults();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
//                        onSignupSuccess();
                        // onSignupFailed();
//                        if (succ == 1) {
//                            Intent intent = new Intent(getBaseContext(), ShowPatients.class);
//                            startActivity(intent);
//                        }
//                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "adding prescription to cart failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String uuid = _uuid.getText().toString();
        String location = _Location.getText().toString();


        if (uuid.isEmpty()) {
            _uuid.setError("Input a prescription code");
            valid = false;
        } else {
            _uuid.setError(null);
        }
        if (location.isEmpty()) {
            _Location.setError("Input location");
            valid = false;
        } else {
            _Location.setError(null);
        }


        return valid;
    }

    private void storesignup() {
        // Toast.makeText(getBaseContext(), "Inside function!", Toast.LENGTH_SHORT).show();
        // Tag used to cancel the request

        JSONObject js = new JSONObject();
        try {
            JSONObject jsonobject_one = new JSONObject();
            jsonobject_one.put("quantity", "null");
            jsonobject_one.put("medical", true);
            jsonobject_one.put("uuid", _uuid.getText().toString());
            JSONArray ja = new JSONArray();
            ja.put(jsonobject_one);
            js.put("cart", ja);
            js.put("locationId", lID);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSONErrorin serializing", e.toString());
        }
        Log.e("JSON serializing", js.toString());
        String tag_string_req = "req_Categories";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, "http://www.oneshoppoint.com/api/checkout/", js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response from server is", response.toString());
                        try {
                            JSONArray data = response.getJSONArray("data");
                            MyShortcuts.setDefaults("locationId", lID, getBaseContext());
                            MyShortcuts.setDefaults("uuid", _uuid.getText().toString(), getBaseContext());
                            Intent intent = new Intent(getBaseContext(), Retailers.class);
                            intent.putExtra("retailers", response.toString());
                            startActivity(intent);


                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getBaseContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }


                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("VolleyError", "Error: " + error.getMessage());
//                hideProgressDialog()
                Log.d("error volley", error.toString());
                errormessage.setVisibility(View.VISIBLE);
                errormessage.setText("Prescription code or location is not valid. Input a valid input");
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


    public void GetLocations() {
        Log.d("URL is", "https://www.oneshoppoint.com/api/location/");
        String tag_string_req = "req_inn";
        StringRequest strReq = new StringRequest(Request.Method.GET, "https://www.oneshoppoint.com/api/location/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response from server is", response.toString());


                String success = null;
                try {
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