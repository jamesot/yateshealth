package com.oneshoppoint.yates.yates;


        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.preference.PreferenceManager;
        import android.util.Base64;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.android.volley.AuthFailureError;
        import com.android.volley.DefaultRetryPolicy;
        import com.android.volley.Request;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.VolleyLog;
        import com.android.volley.toolbox.JsonObjectRequest;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.HashMap;
        import java.util.Map;

        import butterknife.ButterKnife;
        import butterknife.Bind;

/**
 * Created by stephineosoro on 02/06/16.
 */


public class EditPatient extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @Bind(R.id.input_name)
    EditText _nameText;
    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.last_name)
    EditText _last_name;
    @Bind(R.id.btn_signup)
    Button _signupButton;

    @Bind(R.id.id_number)
    EditText id_number;
    @Bind(R.id.phone_number)
    EditText phone_number;
    int succ = 0;
    String

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_patient);
        ButterKnife.bind(this);
       getIntent().getStringExtra("ID");
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

        final ProgressDialog progressDialog = new ProgressDialog(EditPatient.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String lastname = _last_name.getText().toString();
        String id = id_number.getText().toString();
        String phone = phone_number.getText().toString();

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
        Toast.makeText(getBaseContext(), "Creating Patient failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String lastname = _last_name.getText().toString();
        String id = id_number.getText().toString();
        String phone = phone_number.getText().toString();

        if (name.isEmpty()) {
            _nameText.setError("Input first name");
            valid = false;
        } else {
            _nameText.setError(null);
        }
        if (lastname.isEmpty()) {
            _last_name.setError("Input last name");
            valid = false;
        } else {
            _last_name.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (phone.isEmpty() || phone.length() < 10 || phone.length() > 14) {
            phone_number.setError("Invalid phone number");
            valid = false;
        } else {
            phone_number.setError(null);
        }
        if (id.isEmpty() || id.length() < 8) {
            id_number.setError("Input a valid ID number");
            valid = false;
        } else {
            id_number.setError(null);
        }
        return valid;
    }

    private void storesignup() {
        // Toast.makeText(getBaseContext(), "Inside function!", Toast.LENGTH_SHORT).show();
        // Tag used to cancel the request

        JSONObject js = new JSONObject();
        try {
//            JSONObject jsonobject_one = new JSONObject();
//
//            jsonobject_one.put("type", "event_and_offer");
//            jsonobject_one.put("devicetype", "I");
//
//            JSONObject jsonobject_TWO = new JSONObject();
//            jsonobject_TWO.put("value", "event");
//            JSONObject jsonobject = new JSONObject();
//
//            jsonobject.put("requestinfo", jsonobject_TWO);
//            jsonobject.put("request", jsonobject_one);


            js.put("email", _emailText.getText().toString());
            js.put("phoneNumber", phone_number.getText().toString());
            js.put("firstname", _nameText.getText().toString());
            js.put("lastname", _last_name.getText().toString());
            js.put("idNumber", id_number.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSONErrorin serializing", e.toString());
        }
        Log.e("JSON serializing", js.toString());
        String tag_string_req = "req_Categories";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT, "https://www.oneshoppoint.com/api/patient/", js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response from server is", response.toString());


                        String success = null;
                        try {


//                            res = jObj.getJSONArray("All");
                            //successfully gotten matatu data
//                        String regno = jObj.getString("regno");
                            String status=response.getString("CREATED");
                            if (status.equals("CREATED")){
                                Toast.makeText(getBaseContext(), "Successfully added a new patient " , Toast.LENGTH_LONG).show();
                            }
//                            JSONObject c = response.getJSONObject("data");
                            //successfully gotten matatu data
//                        String regno = jObj.getString("regno");

//                            res = response.getJSONArray("data");
//                            JSONArray children1 = null;
//                            Log.e("result of data is: ", c.toString());

                            // looping through All res
                           /* for (int i = 0; i < res.length(); i++) {
                                JSONObject c = res.getJSONObject(i);

                                // Storing each json item in variable

                                String name = c.getString("name");
                                children1 = c.getJSONArray("children");
                                Log.e("CategoryFragment", name);
                                Items items = new Items();
                                items.setTitle(name);
                                items.setTheID(c.getString("id"));
                                JSONObject a = c.getJSONObject("image");
                                items.setThumbnailUrl("https://www.oneshoppoint.com/images" + a.getString("path"));
                              itemsList.add(items); */
//                                Group group = new Group(name,a.getString("path"));
//                                for (int j = 0; j < children1.length(); j++) {
//                                    JSONObject d = children1.getJSONObject(j);
//                                    JSONObject e = d.getJSONObject("image");
//
//                                    items.setTheID(d.getString("id"));
//                                }
//                                groups.append(i, group);
//
//                                ExpandableListView listView = (ExpandableListView) findViewById(R.id.listView);
//                                HomeAdapter adapter = new HomeAdapter(context, groups);
//                                listView.setAdapter(adapter);
//
//                                String sacco = c.getString("sacco");
//                                String question = c.getString("question");
//                                items.setTitle("winner sacco: " + sacco);
//                                items.setDescription("Best in: " + question);
//                            items.setPrice("Total Rating is: " + rate);

//
//                            }
////
//                            adapter = new CategoryAdapter(getActivity(), itemsList);
//                            mRecyclerView.setAdapter(adapter);

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
                Log.d("error volley",error.toString());
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                setRetryPolicy(new DefaultRetryPolicy(5*DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
                setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
                headers.put("Content-Type", "application/json; charset=utf-8");
                String creds = String.format("%s:%s","odhiamborobinson@zmail.com","powerpoint1994");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                headers.put("Authorization", auth);
                return headers;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
        Log.e("request is", jsonObjReq.toString());
    }
    public void saveResults() {

        JSONObject js = new JSONObject();
        try {
//            JSONObject jsonobject_one = new JSONObject();
//
//            jsonobject_one.put("type", "event_and_offer");
//            jsonobject_one.put("devicetype", "I");
//
//            JSONObject jsonobject_TWO = new JSONObject();
//            jsonobject_TWO.put("value", "event");
//            JSONObject jsonobject = new JSONObject();
//
//            jsonobject.put("requestinfo", jsonobject_TWO);
//            jsonobject.put("request", jsonobject_one);


            js.put("email", _emailText.getText().toString());
            js.put("phoneNumber", phone_number.getText().toString());
            js.put("firstname", _nameText.getText().toString());
            js.put("lastname", _last_name.getText().toString());
            js.put("idNumber", id_number.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSONErrorin serializing", e.toString());
        }
        GenericRequest req = new GenericRequest(Request.Method.POST, "https://www.oneshoppoint.com/api/patient/", JSONObject.class, js,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String answer) {
                        Log.e("RESPONSE is",answer.toString());

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(req);
    }

    public void setDefaults(String key, String value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();

    }
}