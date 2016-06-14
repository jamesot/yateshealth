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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.oneshoppoint.yates.yates.Model.ItemDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Bind;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;

/**
 * Created by stephineosoro on 31/05/16.
 */

public class CreatePrescription extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "CreatePrescription";
    public static List<String> INN= new ArrayList<String>();
    public static List<String> INNID= new ArrayList<String>();
    Spinner spinner2;
    @Bind(R.id.dosage_form)
    EditText _dosageForm;
    @Bind(R.id.duration)
    EditText _duration;
    @Bind(R.id.quantity)
    EditText _quantity;
    @Bind(R.id.btn_signup)
    Button _signupButton;

    @Bind(R.id.per_day)
    EditText per_day;
    @Bind(R.id.unit)
    EditText unit;
    @Bind(R.id.note)
    EditText Note;

    int succ = 0;
    private static String innId,selected_item;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_prescription);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Create a Prescription");
        ButterKnife.bind(this);
        this.spinner2 = (Spinner) findViewById(R.id.inn_spinner);
        spinner2.setOnItemSelectedListener(this);
        GetInn();


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

        final ProgressDialog progressDialog = new ProgressDialog(CreatePrescription.this,
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

        String df = _dosageForm.getText().toString();
        String s = _duration.getText().toString();
        String q = _quantity.getText().toString();
        String pd = per_day.getText().toString();
        String w = unit.getText().toString();
        String note = Note.getText().toString();

        if (df.isEmpty()) {
            _dosageForm.setError("Input dosage form");
            valid = false;
        } else {
            _dosageForm.setError(null);
        }
        if (q.isEmpty()) {
            _quantity.setError("Input quantity");
            valid = false;
        } else {
            _quantity.setError(null);
        }
        if (s.isEmpty()) {
            _duration.setError("input a duration");
            valid = false;
        } else {
            _duration.setError(null);
        }

        if (w.isEmpty()) {
            unit.setError("Invalid number of unit");
            valid = false;
        } else {
            unit.setError(null);
        }
        if (pd.isEmpty()) {
            per_day.setError("Input per day");
            valid = false;
        } else {
            per_day.setError(null);
        }


        if(selected_item.matches("")||selected_item==null){
            Toast.makeText(getBaseContext(), "Choose a valid INN ", Toast.LENGTH_LONG).show();
            valid=false;
        }
        return valid;
    }

    private void storeprescription() {
        // Toast.makeText(getBaseContext(), "Inside function!", Toast.LENGTH_SHORT).show();
        JSONObject finalJS = new JSONObject();
        try {
            JSONObject js = new JSONObject();
            JSONArray ja = new JSONArray();
            Log.e("INN ID", innId);
            js.put("innId", innId);
            js.put("duration", _duration.getText().toString());
            js.put("unit", unit.getText().toString());
            js.put("note", Note.getText().toString());
            js.put("dosageForm", _dosageForm.getText().toString());
            js.put("frequencyQuantity", _quantity.getText().toString());
            js.put("frequencyPerDay", per_day.getText().toString());
            ja.put(js);
            finalJS.put("prescriptionItems", ja);
            finalJS.put("patientId", getIntent().getStringExtra("ID"));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSONErrorin serializing", e.toString());
        }
        Log.e("JSON serializing", finalJS.toString());
        String tag_string_req = "req_Categories";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, "https://www.oneshoppoint.com/api/prescription/", finalJS,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response from server is", response.toString());


                        String success = null;
                        try {


//                            res = jObj.getJSONArray("All");
                            //successfully gotten matatu data
//                        String regno = jObj.getString("regno");
                            String status = response.getString("status");
                            if (status.equals("CREATED")) {
                                Toast.makeText(getBaseContext(), "Successfully added a new prescription ", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getBaseContext(), ShowPatients.class);
                                startActivity(intent);
                            }
//

//
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
                headers.put("Content-Type", "application/json; charset=utf-8");
                String creds = String.format("%s:%s", "odhiamborobinson@hotmail.com", "powerpoint1994");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                headers.put("Authorization", auth);
                return headers;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
        Log.e("request is", jsonObjReq.toString());
    }

    private void GetInn() {
        Log.d("URL is", "https://www.oneshoppoint.com/api/inn/");
        String tag_string_req = "req_inn";
        StringRequest strReq = new StringRequest(Request.Method.GET, "https://www.oneshoppoint.com/api/inn/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response from server is", response.toString());


                String success = null;
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray data = jObj.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);
                        String name=c.getString("name");
                        Log.e("Each data" + i, name);
                        INN.add(name);
                        INNID.add(c.getString("id"));

                        Log.e("Each data" + i, c.getString("id"));
                    }

                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),
                            android.R.layout.simple_spinner_item, INN);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adapter);

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
                headers.put("Content-Type", "application/json; charset=utf-8");
                String creds = String.format("%s:%s", "odhiamborobinson@hotmail.com", "powerpoint1994");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                headers.put("Authorization", auth);
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
//        parent.getItemAtPosition(position);
        innId = INNID.get(position);
        selected_item = parent.getItemAtPosition(position).toString();
        Log.e("INN ID",innId);
        Log.e("INN",selected_item);
//        Toast.makeText(getBaseContext(), "Seleccted an ITEM", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}