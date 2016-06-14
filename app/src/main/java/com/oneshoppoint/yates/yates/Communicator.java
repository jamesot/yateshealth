package com.oneshoppoint.yates.yates;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
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
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.DefaultRetryPolicy;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by stephineosoro on 31/05/16.
 */

public class Communicator<T> {
    private T obj;
    private Class type;

    public void get(String url, HashMap<String, String> params, String username,Response.Listener<T> response, String password) {
        Set<String> keys = params.keySet();
        String urlParam ="";
        Integer count = 0;
        for (String key : keys) {
            if(count == 0) {
                urlParam="?"+key+"="+params.get(key);
            } else {
                urlParam="&"+key+"="+params.get(key);
            }

        }

        url+=urlParam;
        get(url,username,response,password);

    }

    public void get(String url, String username,Response.Listener<T> response, String password) {

        HashMap<String, String> header = new HashMap<String, String>();
        String creds = String.format("%s:%s", username, password);
        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
        header.put("Authorization", auth);
        header.put("Content-Type", "application/json");
        GenericRequest<T> request = new GenericRequest<T>(url, type, response ,new Error(), header);
    }

    public void send(String url, HashMap<String, String> params, String username,Response.Listener<T> response, String password) {
        Set<String> keys = params.keySet();
        String urlParam ="";
        Integer count = 0;
        for (String key : keys) {
            if(count == 0) {
                urlParam="?"+key+"="+params.get(key);
            } else {
                urlParam="&"+key+"="+params.get(key);
            }

        }

        url+=urlParam;

        HashMap<String, String> header = new HashMap<String, String>();
        String creds = String.format("%s:%s", username, password);
        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
        header.put("Authorization", auth);
        header.put("Content-Type", "application/json");
        GenericRequest<T> request = new GenericRequest<T>(url, type, response ,new Error(), header);
    }


    private class Error implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            Log.e("Error from server", "error:" + error.getMessage());
        }
    }


}