
package com.oneshoppoint.yates.yates.Model;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.oneshoppoint.yates.yates.Interface.VolleyCallback;
import com.oneshoppoint.yates.yates.utils.AppController;
import com.oneshoppoint.yates.yates.utils.Utils;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by stephineosoro on 20/06/16.
 */
public class Post {

//    public static String baseURL = "http://169.254.85.197";

    private static Context context;

    public Post(Context context) {
        this.context = context;
    }


    public static void PostString(String url, final Map<String, String> parameters, final Map<String, String> headers, final VolleyCallback callback) {

        StringRequest req = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccessResponse(response);
                    }
                }, new Error()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (parameters != null) {
                    params = parameters;
                }


                return params;
            }

           /* @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<String, String>();

                if (headers != null) {
                    setRetryPolicy(new DefaultRetryPolicy(5 * DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
                    setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
                    header = headers;
                }

                return header;
            }*/
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
        Log.e("request is:", req.toString());

    }


    public static void PostJSON(String url, JSONObject parameter, final Map<String, String> headers, final VolleyCallback callback) {

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, parameter,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccessResponse(response);
                    }
                }, new Error()) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<String, String>();

                setRetryPolicy(new DefaultRetryPolicy(5 * DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
                setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
                header = headers;

                return header;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
        Log.e("request is", req.toString());

    }

    public static void getData(String url, Response.Listener<String> response) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response, new Error());
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
        Log.e("request is", stringRequest.toString());
    }

    private static class Error implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            NetworkResponse response = error.networkResponse;
            if (error instanceof ServerError && response != null) {
                try {
                    String res = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                    // Now you can use any deserializer to make sense of data
//                    TODO change this to be back to an object
//                    JSONObject obj = new JSONObject(res);
                    Log.e("obj", res);
                } catch (UnsupportedEncodingException e1) {
                    // Couldn't properly decode data to string
                    Log.e("e1", e1.toString());
                    e1.printStackTrace();
                }
                /*catch (JSONException e2) {
                    // returned data is not JSONObject?
                    e2.printStackTrace();
                    Log.e("e2", e2.toString());
                }*/
            }

            Utils.showToast("Sorry! Something went wrong, but we're looking into it.", context);

            /*Intent intent = new Intent(context, ErrorActivity.class);
            context.startActivity(intent);*/

        }
    }

}
