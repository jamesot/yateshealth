package com.oneshoppoint.yates.yates.controllers;

import android.content.Context;

import com.android.volley.Request;
import com.oneshoppoint.yates.yates.Interface.ServerCallback;
import com.oneshoppoint.yates.yates.Interface.VolleyCallback;
import com.oneshoppoint.yates.yates.Model.Get;
import com.oneshoppoint.yates.yates.utils.Utils;

import org.json.JSONObject;

import java.util.Map;


/**
 * Created by stephineosoro on 29/03/2018.
 */

public class GetData {

    Context context;

    public GetData(Context context) {
        this.context = context;
    }

    public void online_data(final String t_url, final Map<String, String> parameters, final Map<String, String> headers, final ServerCallback callback) {
        Get.getResponse(Request.Method.GET, Utils.baseURL() + t_url, null, context, parameters, headers,
                new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(String result) {

                        String res = result;
                        callback.onSuccess(res);
                    }

                    @Override
                    public void onSuccessResponse(JSONObject response) {

                    }
                });
    }


}
