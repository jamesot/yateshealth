package com.oneshoppoint.yates.yates;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.oneshoppoint.yates.yates.Model.ItemDetails;
import com.oneshoppoint.yates.yates.Model.Items;
import com.oneshoppoint.yates.yates.Model.Prescriptions;

import it.gmariotti.cardslib.library.view.CardViewNative;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by stephineosoro on 02/06/16.
 */
public class ShowEachPrescription extends AppCompatActivity {
    Prescriptions card;
    CardViewNative card2;
    static String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        card2 = (CardViewNative) findViewById(R.id.carddemo);
        setSupportActionBar(toolbar);
        setTitle("Patient's prescriptions");
        getPrescription();

        final FloatingActionButton actionA = (FloatingActionButton) findViewById(R.id.action_a);
        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionA.setTitle("Send Prescription");
                SendPrescription(ID);

            }
        });
    }

    private void getPrescription() {
        Log.d("URL is", MyShortcuts.baseURL() + "prescription?patientId=" + getIntent().getStringExtra("ID"));
        String tag_string_req = "req_Categories";
        StringRequest strReq = new StringRequest(Request.Method.GET, MyShortcuts.baseURL() + "prescription?id=" + getIntent().getStringExtra("ID"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response from server is", response.toString());
                card2.setVisibility(View.VISIBLE);

                String success = null;
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONObject data = jObj.getJSONObject("data");
//                    JSONObject c = data.getJSONObject("data");
                    JSONArray prescription = data.getJSONArray("prescriptionItems");
                    Log.e("PRESCRIPTION ITEMS", prescription.toString());
//                    Log.e("result: ", prescriptions.toString());
//                    ArrayList<Card> cards = new ArrayList<Card>();
//
                    ID = data.getString("id");
//                    // looping through All res
                    ArrayList<ItemDetails> results = new ArrayList<ItemDetails>();
//                    for (int i = 0; i < data.length(); i++) {
//                        JSONObject c = data.getJSONObject("data");
//                        Log.e("Each data" + i, c.toString());
//                        JSONArray prescription = c.getJSONArray("prescriptionItems");
                    Log.e("Prescription object", prescription.toString());

                    for (int j = 0; j < prescription.length(); j++) {


                        ItemDetails items = new ItemDetails();
                        JSONObject d = prescription.getJSONObject(j);
                        JSONObject inn = null;
                        if (d.getString("type").equals("product") && !(d.get("product").equals(null))) {
                            inn = d.getJSONObject("product");
                            String name = inn.getString("name");
                            String strength = d.getString("note");
                            String dosageForm = d.getString("dosageForm");
                            String frequencyQuantity = d.getString("frequencyQuantity");
                            String frequencyPerDay = d.getString("frequencyPerDay");
//                            ID = d.getString("id");
                            items.setName(name);
                            items.setItemDescription(strength);
                            items.setID(dosageForm);
                            items.setQuantity(frequencyQuantity);
                            items.setTotal(frequencyPerDay);
//                        items.setEmail(d.getString("weeks"));

                            String duration = d.getString("duration");
                            String unit = d.getString("unit");
                            items.setEmail(duration + " " + unit);
                            results.add(items);
                        } else if (d.getString("type").equals("inn") && !(d.get("inn").equals(null))) {
                            inn = d.getJSONObject("inn");
                            String name = inn.getString("name");
                            String strength = d.getString("note");
                            String dosageForm = d.getString("dosageForm");
                            String frequencyQuantity = d.getString("frequencyQuantity");
                            String frequencyPerDay = d.getString("frequencyPerDay");
                            ID = d.getString("id");
                            items.setName(name);
                            items.setItemDescription(strength);
                            items.setID(dosageForm);
                            items.setQuantity(frequencyQuantity);
                            items.setTotal(frequencyPerDay);
//                        items.setEmail(d.getString("weeks"));

                            String duration = d.getString("duration");
                            String unit = d.getString("unit");
                            items.setEmail(duration + " " + unit);
                            results.add(items);
                        } else {

                        }


//                            Log.e("Each INN" + d + i, inn.toString());
                    }

                    // Storing each json item in variable

//                        String name = c.getString("firstname");
//                    String description = c.getString("description");
                    //                                children1 = c.getJSONArray("children");
//                        Log.e("CategoryFragment", name);


//                    }

                    initCard(results, "Prescription details");
//                    if (prescriptions.length() == 0) {
//                        Toast.makeText(getBaseContext(), "No prescription done for this patient", Toast.LENGTH_LONG).show();
//                    }


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
                String creds = String.format("%s:%s", MyShortcuts.getDefaults("email", getBaseContext()), MyShortcuts.getDefaults("password", getBaseContext()));

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

    private void initCard(ArrayList<ItemDetails> info, String title) {
        card = new Prescriptions(getBaseContext(), info, title);
        card.init();
        CardViewNative cardView = (CardViewNative) findViewById(R.id.carddemo);
        cardView.setCard(card);
    }

    private void SendPrescription(String ID) {
        Log.d("URL is", MyShortcuts.baseURL() + "prescription?id=" + ID);
        String tag_string_req = "req_Categories";
        StringRequest strReq = new StringRequest(Request.Method.PUT, MyShortcuts.baseURL() + "prescription/mobile/send?id=" + ID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response from server is", response.toString());


                String success = null;
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONObject data = jObj.getJSONObject("data");
                    String sent = jObj.getString("message");
//                            data.getString("sent");

                    if (sent.equals("Successfully sent the prescription")) {
                        Toast.makeText(getBaseContext(), "Prescription sent!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getBaseContext(), "Sorry, Prescription had been already sent!", Toast.LENGTH_LONG).show();
                    }

                    if (jObj.getJSONArray("data").equals(false)) {
                        Toast.makeText(getBaseContext(), "No prescription done for this patient", Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "No prescription done for this patient", Toast.LENGTH_LONG).show();
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
                headers.put("Content-Type", "application/json; charset=utf-8");
                String creds = String.format("%s:%s", MyShortcuts.getDefaults("email", getBaseContext()), MyShortcuts.getDefaults("password", getBaseContext()));

                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                headers.put("Authorization", auth);
//                headers.put("X-HTTP-Method-Override", "PATCH");
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
