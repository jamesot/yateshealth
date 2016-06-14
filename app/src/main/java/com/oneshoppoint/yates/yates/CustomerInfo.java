package com.oneshoppoint.yates.yates;

import android.support.v7.app.AppCompatActivity;
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
 * Created by stephineosoro on 07/06/16.
 */

public class CustomerInfo extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @Bind(R.id.input_name)
    EditText _nameText;
    @Bind(R.id.input_street)
    EditText _street;
    @Bind(R.id.last_name)
    EditText _last_name;
    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.btn_signup)
    Button _signupButton;


    @Bind(R.id.phone_number)
    EditText phone_number;

    @Bind(R.id.residential_name)
    EditText Residential;
    int succ = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_customer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("User Details");
        ButterKnife.bind(this);

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

        final ProgressDialog progressDialog = new ProgressDialog(CustomerInfo.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String email = _street.getText().toString();
        String lastname = _last_name.getText().toString();
        String phone = phone_number.getText().toString();

        // TODO: Implement your own signup logic here.
        storecustomer();
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
        String street = _street.getText().toString();
        String lastname = _last_name.getText().toString();
        String phone = phone_number.getText().toString();
        String emailA = email.getText().toString();
        String res = Residential.getText().toString();
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
        if (street.isEmpty()) {
            _street.setError("enter a street address");
            valid = false;
        } else {
            _street.setError(null);
        }
        if (res.isEmpty()) {
            Residential.setError("enter a street address");
            valid = false;
        } else {
            Residential.setError(null);
        }

        if (emailA.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailA).matches()) {
            email.setError("enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }

        if (phone.isEmpty() || phone.length() < 10 || phone.length() > 14) {
            phone_number.setError("Invalid phone number");
            valid = false;
        } else {
            phone_number.setError(null);
        }

        return valid;
    }

    private void storecustomer() {
        // Toast.makeText(getBaseContext(), "Inside function!", Toast.LENGTH_SHORT).show();
        // Tag used to cancel the request

        JSONObject js = new JSONObject();
        try {
            js.put("residentialName", Residential.getText().toString());
            js.put("email", email.getText().toString());
            js.put("streetAddress", _street.getText().toString());
            js.put("phoneNumber", phone_number.getText().toString());
            js.put("firstname", _nameText.getText().toString());
            js.put("lastname", _last_name.getText().toString());
            js.put("retailerTransactionCode", MyShortcuts.getDefaults("retailercode", this));
            js.put("locationId", MyShortcuts.getDefaults("locationId", this));
            js.put("retailerId", MyShortcuts.getDefaults("retailerid", this));
            Intent intent = new Intent(getBaseContext(), Order.class);
            intent.putExtra("customer", js.toString());
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSONErrorin serializing", e.toString());
        }
        Log.e("JSON serializing", js.toString());
    }


    public void setDefaults(String key, String value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();

    }
}