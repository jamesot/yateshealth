package com.oneshoppoint.yates.yates;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Bind;

/**
 * Created by stephineosoro on 31/05/16.
 */
public class MedicLogin extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Yates");
        ButterKnife.bind(this);
        _emailText.setText(getDefaults("email", this));
        _passwordText.setText(getDefaults("password", this));

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();

            }
        });

    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(MedicLogin.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        logindetail(email,password);
        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty()) {
            _passwordText.setError("Password is empty");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    private void logindetail(final String username, final String password) {
        String tag_string_req = "req_login";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                "https://www.oneshoppoint.com/api/login/medic", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("All Data", "response from the server is: " + response.toString());
//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
//                    String success = jObj.getString("success");
                    setDefaults("email", _emailText.getText().toString(), getBaseContext());
                    setDefaults("password", _passwordText.getText().toString(), getBaseContext());
                    MyShortcuts.set( _emailText.getText().toString(),_passwordText.getText().toString(),getBaseContext());
                    Intent intent = new Intent(getApplicationContext(), ShowPatients.class);
                    startActivity(intent);

                } catch (JSONException e) {
                    // JSON error
                   loginUser(username,password);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Getting data error", "Error: " + error.getMessage());
                /*Toast.makeText(getApplicationContext(),
                        "No internet connection!", Toast.LENGTH_LONG).show();*/
                loginUser(username,password);
//                hideDialog();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                setRetryPolicy(new DefaultRetryPolicy(5*DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
                setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
                headers.put("Content-Type", "application/json; charset=utf-8");
                String creds = String.format("%s:%s",username,password);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                headers.put("Authorization", auth);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", _emailText.getText().toString());
                params.put("password", _passwordText.getText().toString());
                setDefaults("email", _emailText.getText().toString(), getBaseContext());
                setDefaults("password",_passwordText.getText().toString(),getBaseContext());

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }




    private void loginUser(final String username, final String password) {
        String tag_string_req = "req_login";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                "https://www.oneshoppoint.com/api/login/customer", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("All Data", "response from the server is: " + response.toString());
//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
//                    String success = jObj.getString("success");
                    setDefaults("email", _emailText.getText().toString(), getBaseContext());
                    setDefaults("password", _passwordText.getText().toString(), getBaseContext());
                    MyShortcuts.set( _emailText.getText().toString(),_passwordText.getText().toString(),getBaseContext());
                    Intent intent = new Intent(getApplicationContext(), GetPrescription.class);
                    startActivity(intent);

                } catch (JSONException e) {
                    // JSON error
                    MyShortcuts.showToast("Wrong username/password",getBaseContext());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Getting data error", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "wrogn username/password", Toast.LENGTH_LONG).show();
//                hideDialog();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                setRetryPolicy(new DefaultRetryPolicy(5*DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
                setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
                headers.put("Content-Type", "application/json; charset=utf-8");
                String creds = String.format("%s:%s",username,password);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                headers.put("Authorization", auth);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", _emailText.getText().toString());
                params.put("password", _passwordText.getText().toString());
                setDefaults("email", _emailText.getText().toString(), getBaseContext());
                setDefaults("password",_passwordText.getText().toString(),getBaseContext());

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    public void setDefaults(String key, String value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();

    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

}


