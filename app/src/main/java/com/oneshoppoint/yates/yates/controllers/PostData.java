package com.oneshoppoint.yates.yates.controllers;

import android.content.Context;


import com.oneshoppoint.yates.yates.Interface.ServerCallback;
import com.oneshoppoint.yates.yates.Interface.VolleyCallback;
import com.oneshoppoint.yates.yates.Model.Post;
import com.oneshoppoint.yates.yates.utils.Utils;

import org.json.JSONObject;

import java.util.Map;


/**
 * Created by stephineosoro on 08/05/2018.
 */

public class PostData {
    Context context;

    public PostData(Context context) {
        this.context = context;
    }

    public void post(final String t_url, final Map<String, String> parameters, final JSONObject params, final Map<String, String> headers, final ServerCallback callback) {

        if (parameters != null) {
            Post post = new Post(context);
            post.PostString(Utils.baseURL() + t_url, parameters,headers, new VolleyCallback() {
                @Override
                public void onSuccessResponse(String result) {
                    callback.onSuccess(result);
                }

                @Override
                public void onSuccessResponse(JSONObject response) {
                }
            });
        } else {

            Post post = new Post(context);
            post.PostJSON(Utils.baseURL() +t_url, params,headers, new VolleyCallback() {
                @Override
                public void onSuccessResponse(String result) {
                }

                @Override
                public void onSuccessResponse(JSONObject response) {
                    callback.onSuccess(response);
                }
            });
        }
    }
}
