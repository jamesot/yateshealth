package com.oneshoppoint.yates.yates;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.oneshoppoint.yates.yates.Interface.ServerCallback;
import com.oneshoppoint.yates.yates.controllers.PostData;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.oneshoppoint.yates.yates.utils.Utils.applyFontForToolbarTitle;
import static com.oneshoppoint.yates.yates.utils.Utils.baseURL;
import static com.oneshoppoint.yates.yates.utils.Utils.showToast;

public class postFeedback extends AppCompatActivity {

    private EditText name, email, phone, subject, area, feedback, feedback_date;
    protected Typeface mTfLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        applyFontForToolbarTitle(this);
        mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");

        name = (EditText) findViewById(R.id.first_name);
        name.setTypeface(mTfLight);

        subject = (EditText) findViewById(R.id.subject);
        subject.setTypeface(mTfLight);

        phone = (EditText) findViewById(R.id.phone_number);
        phone.setTypeface(mTfLight);

        email = (EditText) findViewById(R.id.email);
        email.setTypeface(mTfLight);

        area = (EditText) findViewById(R.id.area);
        area.setTypeface(mTfLight);

        feedback = (EditText) findViewById(R.id.feedback);
        feedback.setTypeface(mTfLight);

        feedback_date = (EditText) findViewById(R.id.feedback_date);
        feedback_date.setTypeface(mTfLight);


        Button submit = (Button) findViewById(R.id.btn_submit);
        submit.setTypeface(mTfLight);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });
    }


    private void uploadData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name.getText().toString());
        params.put("subject", subject.getText().toString());
        params.put("email", email.getText().toString());
        params.put("phone", phone.getText().toString());
        params.put("area", area.getText().toString());
        params.put("feedback", feedback.getText().toString());
        params.put("feedback_date", feedback_date.getText().toString());


        PostData post = new PostData(getBaseContext());
        post.post( "feedback", params, null, null, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("string result", result);
            }

            @Override
            public void onSuccess(JSONObject response) {
                Log.e("json result", response.toString());

            }
        });

    }
}
