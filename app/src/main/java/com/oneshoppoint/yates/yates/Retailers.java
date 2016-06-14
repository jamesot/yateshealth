package com.oneshoppoint.yates.yates;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.getbase.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardGridView;

import static com.oneshoppoint.yates.yates.R.drawable.circle;

/**
 * Created by stephineosoro on 31/05/16.
 */
public class Retailers extends AppCompatActivity {
    public static List<String> paybillnumbers = new ArrayList<String>();
    public static List<String> ids = new ArrayList<String>();
    static CardGridArrayAdapter mCardArrayAdapter;
    protected ArrayList<Card> cards = new ArrayList<Card>();
    String retailers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_retailers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Choose a retailer");

        retailers = getIntent().getStringExtra("retailers");
        JSONObject Res = null;
        try {

            Res = new JSONObject(retailers);
            JSONArray data = Res.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                String total = c.getString("totalPrice");
                JSONObject ret = c.getJSONObject("retailer");
                String id = ret.getString("id");
                String paybill=ret.getString("payBillNo");
                /*paybillnumbers.add(paybill);
                ids.add(id);*/
                Log.e("id",id);
                String name = ret.getString("name");

                GplayGridCard card = new GplayGridCard(getBaseContext());
                card.headerTitle = name ;
                card.secondaryTitle="    total is Ksh. " + total;
                card.setId(id);
                card.setTitle(name );
                card.init();
                cards.add(card);
                Log.d("names",name + "    total is Ksh. " + total);

            }

            mCardArrayAdapter = new CardGridArrayAdapter(getBaseContext(), cards);

            CardGridView listView = (CardGridView) findViewById(R.id.carddemo_grid_base1);
            if (listView != null) {
                listView.setAdapter(mCardArrayAdapter);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
//        GetData();

    }

    public class GplayGridCard extends Card {


        protected String url;

        protected String headerTitle;
        protected String secondaryTitle;
        protected float rating;

        public GplayGridCard(Context context) {
            super(context, R.layout.inner_content);
        }


        public GplayGridCard(Context context, int innerLayout) {
            super(context, innerLayout);
        }

        private void init() {


            CardHeader header = new CardHeader(getContext());
            header.setButtonOverflowVisible(true);
            header.setTitle(headerTitle);
           /* header.setPopupMenu(R.menu.popupmain, new CardHeader.OnClickCardHeaderPopupMenuListener() {
                @Override
                public void onMenuItemClick(BaseCard card, MenuItem item) {
//                    Toast.makeText(getContext(), "Item " + item.getTitle(), Toast.LENGTH_SHORT).show();
                    String selected = card.getId();
                    Toast.makeText(getBaseContext(), "Patient deleted", Toast.LENGTH_LONG).show();
//                    ID = card.getId();
//                    if (mCardArrayAdapter != null) {

                    cards.remove(card); //It is an example.
                    mCardArrayAdapter.notifyDataSetChanged();
//                    }

                }
            });*/

            addCardHeader(header);

            setOnClickListener(new OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    //Do something



                    retailers = getIntent().getStringExtra("retailers");
                    JSONObject Res = null;
                    try {

                        Res = new JSONObject(retailers);
                        JSONArray data = Res.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject c = data.getJSONObject(i);
                            JSONObject ret = c.getJSONObject("retailer");
                            String id = ret.getString("id");



                            if (card.getId().equals(id)){

                                Log.e("id is", id);
                                String paybill=ret.getString("payBillNo");
                                Log.e("pb is", paybill);
                                Intent intent =new Intent(getBaseContext(),RetailerCode.class);
                                intent.putExtra("paybill",paybill);
                                intent.putExtra("id",id);
                                intent.putExtra("name",card.getTitle());
                                intent.putExtra("totalretailer",c.getString("totalPrice"));
                                startActivity(intent);
                            }


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {

            final TextView subtitle = (TextView) view.findViewById(R.id.carddemo_gplay_main_inner_subtitle);
            subtitle.setText(secondaryTitle);

        }


    }


    private void GetData() {
        // Tag used to cancel the request

        final JSONObject js = new JSONObject();
        try {
            JSONObject jsonobject_one = new JSONObject();

            jsonobject_one.put("type", "event_and_offer");
            jsonobject_one.put("devicetype", "I");

            JSONObject jsonobject_TWO = new JSONObject();
            jsonobject_TWO.put("value", "event");
            JSONObject jsonobject = new JSONObject();

            jsonobject.put("requestinfo", jsonobject_TWO);
            jsonobject.put("request", jsonobject_one);


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSONErrorin serializing", e.toString());
        }
        Log.e("JSON serializing", js.toString());
        String tag_string_req = "req_Categories";
        StringRequest strReq = new StringRequest(Request.Method.GET, "https://www.oneshoppoint.com/api/patient/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response from server is", response.toString());


                String success = null;
                try {
                    JSONObject jObj = new JSONObject(response);
//                            res = jObj.getJSONArray("All");
                    //successfully gotten matatu data
//                        String regno = jObj.getString("regno");

                    JSONArray res = jObj.getJSONArray("data");

                    Log.e("result: ", res.toString());


                    // looping through All res
                    for (int i = 0; i < res.length(); i++) {
                        JSONObject c = res.getJSONObject(i);

                        // Storing each json item in variable
                        String name = c.getString("firstname");
                        Log.e("CategoryFragment", name);
                        GplayGridCard card = new GplayGridCard(getBaseContext());

                        card.headerTitle = name + " " + c.getString("lastname");
                        card.secondaryTitle = c.getString("phoneNumber");
                        card.setId(c.getString("id"));
                        card.setTitle(c.getString("firstname") + " " + c.getString("lastname"));
                        card.init();
                        cards.add(card);


//
                    }
                    if (res.length() == 0) {
                        Toast.makeText(getBaseContext(), "No prescriptions done for this patient!,add one by clicking the + button ", Toast.LENGTH_LONG).show();
                    }
                    mCardArrayAdapter = new CardGridArrayAdapter(getBaseContext(), cards);

                    CardGridView listView = (CardGridView) findViewById(R.id.carddemo_grid_base1);
                    if (listView != null) {
                        listView.setAdapter(mCardArrayAdapter);
                    }


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
//                    Toast.makeText(getBaseContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("JSON ERROR", e.toString());
                }
            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("VolleyError", "Error: " + error.getMessage());
//                hideProgressDialog();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                setRetryPolicy(new DefaultRetryPolicy(5 * DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
                setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
                headers.put("Content-Type", "application/json; charset=utf-8");
                String creds = String.format("%s:%s", "odhiamborobinson@hotmail.com", "powerpoint1994");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                headers.put("Authorization", auth);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
//                Log.e("category id", getIntent().getStringExtra("category_id"));
//                params.put("categoryId", 2 + "");


                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        Log.e("request is", strReq.toString());
    }



}
