package com.oneshoppoint.yates.yates;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by stephineosoro on 31/05/16.
 */
public class HomeChoose extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @Bind(R.id.login)
    Button login;
    @Bind(R.id.getprescription)
    Button getprescription;
    @Bind(R.id.signup)
    Button Signup;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_choose);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Yates health");*/
        ButterKnife.bind(this);

        //  Declare a new thread to do a preference check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    Intent i = new Intent(HomeChoose.this, MyIntro.class);
                    startActivity(i);

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();



        Signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Registration.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),MedicLogin.class);
                startActivity(intent);

            }
        });
        getprescription.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), GetPrescription.class);
                startActivity(intent);

            }
        });

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
        Signup.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        Signup.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = login.getText().toString();
        String password = getprescription.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            login.setError("enter a valid email address");
            valid = false;
        } else {
            login.setError(null);
        }

        if (password.isEmpty()) {
            getprescription.setError("Password is empty");
            valid = false;
        } else {
            getprescription.setError(null);
        }

        return valid;
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


