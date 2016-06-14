package com.oneshoppoint.yates.yates;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.Bind;
/**
 * Created by stephineosoro on 07/06/16.
 */
public class RetailerCode extends AppCompatActivity {

    @Bind(R.id.retailercode)
    EditText retailer_code;
    @Bind(R.id.btn_signup)
    Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_retailer_code);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getIntent().getStringExtra("name"));
        final String id = getIntent().getStringExtra("id");
        String paybill = getIntent().getStringExtra("paybill");
        final String retailer_name= getIntent().getStringExtra("name");
        final String totalretailer= getIntent().getStringExtra("totalretailer");
        TextView tv= (TextView) findViewById(R.id.r_paybill);
        tv.setText(paybill);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = retailer_code.getText().toString();
                MyShortcuts.setDefaults("retailercode", code, getBaseContext());
                MyShortcuts.setDefaults("retailerid", id, getBaseContext());
                MyShortcuts.setDefaults("retailername", retailer_name, getBaseContext());
                MyShortcuts.setDefaults("totalretailer", totalretailer, getBaseContext());
                MyShortcuts.showToast("submitted successfully!", getBaseContext());
                Intent intent = new Intent(getBaseContext(), CustomerInfo.class);
                startActivity(intent);
            }
        });


    }
}
