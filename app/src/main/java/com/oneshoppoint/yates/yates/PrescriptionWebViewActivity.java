package com.oneshoppoint.yates.yates;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.oneshoppoint.yates.yates.Interface.ServerCallback;
import com.oneshoppoint.yates.yates.Interface.VolleyCallback;
import com.oneshoppoint.yates.yates.Model.Post;
import com.oneshoppoint.yates.yates.controllers.GetData;
import com.oneshoppoint.yates.yates.controllers.PostData;
import com.thefinestartist.Base;
import com.thefinestartist.finestwebview.FinestWebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.oneshoppoint.yates.yates.utils.Utils.applyFontForToolbarTitle;
import static com.oneshoppoint.yates.yates.utils.Utils.baseURL;

public class PrescriptionWebViewActivity extends AppCompatActivity {
    protected Typeface mTfLight;
    ArrayList<String> items = new ArrayList<>();
    private EditText first_name, last_name, phone, email, medic_id;
    private String county = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_web_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        applyFontForToolbarTitle(this);
        mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");

        webView();

        first_name = (EditText) findViewById(R.id.first_name);
        first_name.setTypeface(mTfLight);

        last_name = (EditText) findViewById(R.id.last_name);
        last_name.setTypeface(mTfLight);

        phone = (EditText) findViewById(R.id.phone_number);
        phone.setTypeface(mTfLight);

        email = (EditText) findViewById(R.id.email);
        email.setTypeface(mTfLight);

        medic_id = (EditText) findViewById(R.id.medic_id);
        medic_id.setTypeface(mTfLight);

        Button submit = (Button) findViewById(R.id.btn_submit);
        submit.setTypeface(mTfLight);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });


        String[] new_route_items = new String[]{"No", "Yes"};

        GetData getData = new GetData(this);
        getData.online_data("counties", null, null, new ServerCallback() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray data = jsonObject.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {
                        items.add(data.getJSONObject(i).getString("name"));
                    }

                    Spinner dropdown = (Spinner) findViewById(R.id.choose_county);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, items);
                    dropdown.setAdapter(adapter);

                    dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            county = parent.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(JSONObject response) {

            }
        });


//        new FinestWebView.Builder(this).show("http://yatehealths.com/prescription.html");


       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    private void webView() {
        new FinestWebView.Builder(this).theme(R.style.FinestWebViewTheme)
                .titleDefault("Yates")
                .showUrl(false)
                .statusBarColorRes(R.color.bluePrimaryDark)
                .toolbarColorRes(R.color.bluePrimary)
                .titleColorRes(R.color.finestWhite)
                .urlColorRes(R.color.bluePrimaryLight)
                .iconDefaultColorRes(R.color.finestWhite)
                .progressBarColorRes(R.color.finestWhite)
                .stringResCopiedToClipboard(R.string.copied_to_clipboard)
                .stringResCopiedToClipboard(R.string.copied_to_clipboard)
                .stringResCopiedToClipboard(R.string.copied_to_clipboard)
                .showSwipeRefreshLayout(true)
                .swipeRefreshColorRes(R.color.bluePrimaryDark)
                .menuSelector(R.drawable.selector_light_theme)
                .menuTextGravity(Gravity.CENTER)
                .menuTextPaddingRightRes(R.dimen.defaultMenuTextPaddingLeft)
                .dividerHeight(0)
                .gradientDivider(false)
                .setCustomAnimations(R.anim.slide_up, R.anim.hold, R.anim.hold, R.anim.slide_down)
                .show("http://yatehealths.com/prescription.html");
    }

    private void uploadData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("firstname", first_name.getText().toString());
        params.put("lastname", last_name.getText().toString());
        params.put("email", email.getText().toString());
        params.put("phonenumber", phone.getText().toString());
        params.put("county", county);
        params.put("medic_id", medic_id.getText().toString());


        PostData post = new PostData(getBaseContext());
        post.post(baseURL() + "apipost", params, null, null, new ServerCallback() {
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
