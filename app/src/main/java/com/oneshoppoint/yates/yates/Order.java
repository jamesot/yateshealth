package com.oneshoppoint.yates.yates;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.android.volley.toolbox.HttpHeaderParser;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardGridView;

/**
 * Created by stephineosoro on 07/06/16.
 */
public class Order extends AppCompatActivity {
    JSONObject customer;
    static CardGridArrayAdapter mCardArrayAdapter;
    protected ArrayList<Card> cards = new ArrayList<Card>();
    static String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_order);
        Button submit = (Button) findViewById(R.id.orderbtn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Order Confirmation");

        try {
            customer = new JSONObject(getIntent().getStringExtra("customer"));
            name = customer.getString("firstname");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        GplayGridCard card = new GplayGridCard(getBaseContext());


        card.secondaryTitle = name;
        card.code = MyShortcuts.getDefaults("retailercode", this);
        card.prescriptionCode = MyShortcuts.getDefaults("uuid", this);
        card.Retailer = MyShortcuts.getDefaults("retailername", this);
        card.totalPrice = MyShortcuts.getDefaults("totalretailer", this);
        card.setTitle("Order Confirmation");
        card.init();
        cards.add(card);
        mCardArrayAdapter = new CardGridArrayAdapter(getBaseContext(), cards);
        CardGridView listView = (CardGridView) findViewById(R.id.carddemo_grid_base1);
        if (listView != null)

        {
            listView.setAdapter(mCardArrayAdapter);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostOrder();
            }
        });


    }

    public class GplayGridCard extends Card {

        protected TextView mTitle;
        protected TextView mSecondaryTitle;
        protected RatingBar mRatingBar;
        protected int resourceIdThumbnail = -1;
        protected int count;
        protected String url;

        protected String headerTitle;
        protected String secondaryTitle;
        protected String Retailer;
        protected String prescriptionCode;
        protected String totalPrice;
        protected String code;

        public GplayGridCard(Context context) {
            super(context, R.layout.inner_content_order);
        }


        public GplayGridCard(Context context, int innerLayout) {
            super(context, innerLayout);
        }

        private void init() {
            CardHeader header = new CardHeader(getContext());
            header.setButtonOverflowVisible(true);
            header.setTitle(headerTitle);
            addCardHeader(header);
          /*  OnCardClickListener clickListener = new OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    //Do something
                }
            };

     */


        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {
            final TextView title = (TextView) view.findViewById(R.id.prescription_code);
            title.setText(prescriptionCode);

            final TextView retailer = (TextView) view.findViewById(R.id.retailer_name);
            retailer.setText(Retailer);

            final TextView tCode = (TextView) view.findViewById(R.id.transaction_code);
            tCode.setText(code);

            final TextView tPrice = (TextView) view.findViewById(R.id.total_price);
            tPrice.setText("Ksh. " + totalPrice);

            final TextView fname = (TextView) view.findViewById(R.id.customer_name);
            fname.setText(secondaryTitle);


        }

        class GplayGridThumb extends CardThumbnail {

            public GplayGridThumb(Context context) {
                super(context);
            }

            @Override
            public void setupInnerViewElements(ViewGroup parent, View viewImage) {
                //viewImage.getLayoutParams().width = 196;
                //viewImage.getLayoutParams().height = 196;

            }
        }

    }

    private void PostOrder() {

        try {
            JSONObject jsonobject_one = new JSONObject();
            jsonobject_one.put("quantity", null);
            jsonobject_one.put("medical", true);
            jsonobject_one.put("uuid",MyShortcuts.getDefaults("uuid", getBaseContext()));
            JSONArray ja = new JSONArray();
            ja.put(jsonobject_one);
            customer.put("cart", ja);
            Log.e("JSON Serializing object",customer.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, "http://41.89.64.134:8080/api/checkout/order/", customer,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response for order is",response.toString());
                        try {
                            if (response.getString("CREATED").equals("CREATED")){
                                MyShortcuts.showToast("Your order has been submited successfully",getBaseContext());
                                Intent intent=new Intent(getBaseContext(),GetPrescription.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                        Log.e("obj",obj.toString());
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        Log.e("e1",e1.toString());
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                        Log.e("e2", e2.toString());
                    }
                }

            }
        }){

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                setRetryPolicy(new DefaultRetryPolicy(5 * DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
                setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
                headers.put("Content-Type", "application/json; charset=utf-8");
                String creds = String.format("%s:%s", "web@oneshoppoint.com", "spr0iPpQAiwS8u");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                headers.put("Authorization", auth);
                return headers;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
        Log.e("request is", req.toString());
    }

}
