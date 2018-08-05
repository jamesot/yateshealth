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
import com.oneshoppoint.yates.yates.Model.ItemDetails;
import com.oneshoppoint.yates.yates.Model.Prescriptions;

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
import it.gmariotti.cardslib.library.view.CardViewNative;

import static com.oneshoppoint.yates.yates.R.drawable.circle;

/**
 * Created by stephineosoro on 31/05/16.
 */
public class AllPrescriptions extends AppCompatActivity {
    protected ScrollView mScrollView;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    //    private List<Items> itemsList = new ArrayList<>();
    JSONArray res = null;
    private static final int ITEM_COUNT = 100;
    ImageLoader imageLoader;
    static CardGridArrayAdapter mCardArrayAdapter;
    protected ArrayList<Card> cards = new ArrayList<Card>();
    BaseCard removedcard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_prescriptions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Prescriptions");
        getPrescription();

        final FloatingActionButton actionA = (FloatingActionButton) findViewById(R.id.action_a);
        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionA.setTitle("Add a Prescription");
                Intent intent = new Intent(getApplicationContext(), CreatePrescription.class);
                intent.putExtra("ID", getIntent().getStringExtra("ID"));
                startActivity(intent);

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
        protected float rating;

        public GplayGridCard(Context context) {
            super(context, R.layout.inner_content_detail);

        }


        public GplayGridCard(Context context, int innerLayout) {
            super(context, innerLayout);
        }

        private void init() {
//
//            UniversalCardThumbnail cardThumbnail = new UniversalCardThumbnail(mContext);
//            cardThumbnail.setExternalUsage(true);
//            addCardThumbnail(cardThumbnail);

            CardHeader header = new CardHeader(getContext());
            header.setButtonOverflowVisible(true);
            header.setTitle(headerTitle + " (" + secondaryTitle + " )");
            header.setPopupMenu(R.menu.popupmain, new CardHeader.OnClickCardHeaderPopupMenuListener() {
                @Override
                public void onMenuItemClick(BaseCard card, MenuItem item) {
//                    Toast.makeText(getContext(), "Item " + item.getTitle(), Toast.LENGTH_SHORT).show();
                    String selected = card.getId();

                    if (card.getTitle().endsWith("undispensed")) {
                        removedcard = card;
                        cards.remove(card); //It is an example.
                        mCardArrayAdapter.notifyDataSetChanged();
//                    }
                        Delete(selected);
                    } else {
                        Toast.makeText(getContext(), "The prescription is already dispensed, can't be deleted", Toast.LENGTH_SHORT).show();
                    }
//                    SendPrescription(selected);

//                    Toast.makeText(getBaseContext(), "Item ID is" + selected, Toast.LENGTH_LONG).show();
                    /*Intent intent =new Intent(getBaseContext(),ProductDetail.class);
                    intent.putExtra("id",selected);
                    intent.putExtra("product_name",card.getTitle());
                    startActivity(intent);*/
                }
            });

            addCardHeader(header);
//            Log.e("URL", url);


//            NetworkImageView thumbnail1 =(NetworkImageView) getActivity().findViewById(R.id.card_thumbnail_image);
//            thumbnail1.setImageUrl(url,imageLoader);
//
//            GplayGridThumb thumbnail = new GplayGridThumb(getContext());
//            thumbnail.setUrlResource(url);
//            if (resourceIdThumbnail > -1)
//                thumbnail.setDrawableResource(resourceIdThumbnail);
//            else
//                thumbnail.setDrawableResource(R.drawable.ic_launcher);
//            addCardThumbnail(thumbnail);


          /*  OnCardClickListener clickListener = new OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    //Do something
                }
            };

            addPartialOnClickListener(Card.CLICK_LISTENER_CONTENT_VIEW, clickListener);*/
         /*   setOnClickListener(new OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
//                    Do something
                    String selected= card.getId();
                    Toast.makeText(getBaseContext(), "Item ID is" + selected, Toast.LENGTH_LONG).show();
                   *//* Intent intent =new Intent(getBaseContext(),ProductDetail.class);
                    intent.putExtra("id",selected);
                    intent.putExtra("product_name",card.getTitle());
                    startActivity(intent);*//*
                }
            });*/


        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {

//            TextView title = (TextView) view.findViewById(R.id.carddemo_gplay_main_inner_title);
//            title.setText("I-shop");

            final TextView subtitle = (TextView) view.findViewById(R.id.carddemo_gplay_main_inner_subtitle);
            subtitle.setText("Edit");
//            subtitle.setText(secondaryTitle);
            subtitle.setClickable(true);
            subtitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(getContext(), EditPrescription.class);
                    intent.putExtra("ID", getId());
                    intent.putExtra("pID", getIntent().getStringExtra("ID"));
                    startActivity(intent);

                }
            });

            final TextView subtitle2 = (TextView) view.findViewById(R.id.carddemo_gplay_main_inner_subtitle2);
//            subtitle2.setText(secondaryTitle);
            subtitle2.setText("Info");
            subtitle2.setClickable(true);
            subtitle2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    getParentCard().getId();
                    Intent intent = new Intent(getContext(), ShowEachPrescription.class);
                    intent.putExtra("ID", getId());
                    startActivity(intent);
                }
            });
//            NetworkImageView thumbnail =(NetworkImageView)view.findViewById(R.id.card_thumbnail_image);
//            thumbnail.setImageUrl(url,imageLoader);


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


    private void GetData() {
        // Toast.makeText(getBaseContext(), "Inside function!", Toast.LENGTH_SHORT).show();
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

//            Log.e("CategoryId", getIntent().getStringExtra("category_id"));
//            js.put("categoryId", getIntent().getStringExtra("category_id"));

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSONErrorin serializing", e.toString());
        }
        Log.e("JSON serializing", js.toString());
        String tag_string_req = "req_Categories";
        StringRequest strReq = new StringRequest(Request.Method.GET, MyShortcuts.baseURL()+"patient/", new Response.Listener<String>() {
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
                    ArrayList<Card> cards = new ArrayList<Card>();

                    // looping through All res
                    for (int i = 0; i < res.length(); i++) {
                        JSONObject c = res.getJSONObject(i);

                        // Storing each json item in variable

                        String name = c.getString("firstname");
//                    String description = c.getString("description");
                        //                                children1 = c.getJSONArray("children");
                        Log.e("CategoryFragment", name);
//                    Items items = new Items();
////                    items.setTitle(name);
////                    items.setTheID(c.getString("uuid"));
//                    JSONObject a = c.getJSONObject("primaryImage");
//                    String path = "https://www.oneshoppoint.com/images" + a.getString("path");
//                    items.setThumbnailUrl("https://www.oneshoppoint.com/images" + a.getString("path"));
//                    itemsList.add(items);


                        GplayGridCard card = new GplayGridCard(getBaseContext());

                        card.headerTitle = name;
                        card.secondaryTitle = c.getString("phoneNumber");
//                    card.id = c.getString("price");
                        card.setId(c.getString("id"));
//                    card.url = path;
                        card.setTitle(c.getString("firstname") + " " + c.getString("lastname"));
//                    CardThumbnail thumb = new CardThumbnail(getBaseContext());
//
//                    //Set URL resource
//                    thumb.setUrlResource(path);
//
//                    //Error Resource ID
//                    thumb.setErrorResource(circle);
//
//                    //Add thumbnail to a card
//                    card.addCardThumbnail(thumb);
                        //Only for test, change some icons
//                                if ((i % 6 == 0)) {
//
//                                } else if ((i % 6 == 1)) {
//                                    card.resourceIdThumbnail = R.drawable.ic_launcher;
//                                } else if ((i % 6 == 2)) {
//                                    card.resourceIdThumbnail = R.drawable.ic_launcher;
//                                } else if ((i % 6 == 3)) {
//                                    card.resourceIdThumbnail = R.drawable.ic_launcher;
//                                } else if ((i % 6 == 4)) {
//                                    card.resourceIdThumbnail = R.drawable.ic_launcher;
//
                        card.init();
                        cards.add(card);


//
                    }
//                    if (res.length() == 0) {
//                        Toast.makeText(getBaseContext(), "No products currently in this category,\n Explore other categories", Toast.LENGTH_LONG).show();
//                    }
                    CardGridArrayAdapter mCardArrayAdapter = new CardGridArrayAdapter(getBaseContext(), cards);

                    CardGridView listView = (CardGridView) findViewById(R.id.carddemo_grid_base1);
                    if (listView != null) {
                        listView.setAdapter(mCardArrayAdapter);
                    }


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                String creds = String.format("%s:%s", MyShortcuts.getDefaults("email", getBaseContext()), MyShortcuts.getDefaults("password", getBaseContext()));
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


    private void getPrescription() {
        Log.d("URL is", MyShortcuts.baseURL()+"prescription?patientId=" + getIntent().getStringExtra("ID"));
        String tag_string_req = "req_Categories";
        StringRequest strReq = new StringRequest(Request.Method.GET, MyShortcuts.baseURL()+"prescription?patientId=" + getIntent().getStringExtra("ID"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response from server is", response.toString());


                String success = null;
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray data = jObj.getJSONArray("data");


//                    Log.e("result: ", prescriptions.toString());
//                    ArrayList<Card> cards = new ArrayList<Card>();
//
//                    // looping through All res
                    ArrayList<ItemDetails> results = new ArrayList<ItemDetails>();
//                    ArrayList<Card> cards = new ArrayList<Card>();
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);
                        Log.e("Each data" + i, c.toString());
                        JSONArray prescription = c.getJSONArray("prescriptionItems");
                        Log.e("Prescription object", prescription.toString());
                        for (int j = 0; j < prescription.length(); j++) {
                            ItemDetails items = new ItemDetails();
                            JSONObject d = prescription.getJSONObject(j);
                            JSONObject inn = null;
                            String name=null;
                            if (d.getString("type").equals("product")&&!(d.get("product").equals(null))) {
                                inn = d.getJSONObject("product");
                               name = inn.getString("name");
                                String strength = d.getString("note");
                                String dosageForm = d.getString("dosageForm");
                                String frequencyQuantity = d.getString("frequencyQuantity");
                                String frequencyPerDay = d.getString("frequencyPerDay");

                                items.setItemDescription(strength);
                                items.setID(dosageForm);
                                items.setQuantity(frequencyQuantity);
                                items.setTotal(frequencyPerDay);
                                String duration = d.getString("duration");
                                String unit = d.getString("unit");
                                items.setEmail(duration + " " + unit);
                                results.add(items);
                                Log.e("weeks", duration + " " + unit);
                                Log.e("Each INN" + d + i, inn.toString());
                            } else if(d.getString("type").equals("inn")&&!(d.get("inn").equals(null))){
                                inn = d.getJSONObject("inn");

                                name = inn.getString("name");
                                String strength = d.getString("note");
                                String dosageForm = d.getString("dosageForm");
                                String frequencyQuantity = d.getString("frequencyQuantity");
                                String frequencyPerDay = d.getString("frequencyPerDay");

                                items.setItemDescription(strength);
                                items.setID(dosageForm);
                                items.setQuantity(frequencyQuantity);
                                items.setTotal(frequencyPerDay);
                                String duration = d.getString("duration");
                                String unit = d.getString("unit");
                                items.setEmail(duration + " " + unit);
                                results.add(items);
                                Log.e("weeks", duration + " " + unit);
                                Log.e("Each INN" + d + i, inn.toString());
                            }else{
                                name="";

                            }





                            GplayGridCard card = new GplayGridCard(getBaseContext());

                            card.headerTitle = name + " ";
                            if (c.getString("dispensed").equals("true")) {
                                card.secondaryTitle = "dispensed";
                                Log.e("Dispensed", "dispensed");
                            } else {
                                card.secondaryTitle = "undispensed";
                            }
//                    card.id = c.getString("price");
                            card.setId(c.getString("id"));
//                    card.url = path;
                            card.setTitle(name);
                            card.init();
                            cards.add(card);
                        }

                        // Storing each json item in variable

//                        String name = c.getString("firstname");
//                    String description = c.getString("description");
                        //                                children1 = c.getJSONArray("children");
//                        Log.e("CategoryFragment", name);


                    }
                    mCardArrayAdapter = new CardGridArrayAdapter(getBaseContext(), cards);

                    CardGridView listView = (CardGridView) findViewById(R.id.carddemo_grid_base1);
                    if (listView != null) {
                        listView.setAdapter(mCardArrayAdapter);
                    }


//                    initCard(results, "Patient");
                    if (jObj.getJSONArray("data").equals(false)) {
                        Toast.makeText(getBaseContext(), "No prescription done for this patient", Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "No prescription done for this patient", Toast.LENGTH_LONG).show();
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
                String creds = String.format("%s:%s", MyShortcuts.getDefaults("email", getBaseContext()), MyShortcuts.getDefaults("password", getBaseContext()));
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

    private void Delete(String ids) {
        String tag_string_req = "req_Categories";
        StringRequest strReq = new StringRequest(Request.Method.DELETE, MyShortcuts.baseURL()+"prescription?ids=" + ids, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response from server is", response.toString());


                String success = null;
                try {
                    JSONObject jObj = new JSONObject(response);
//                            res = jObj.getJSONArray("All");
                    //successfully gotten matatu data
//                        String regno = jObj.getString("regno");

                    String res = jObj.getString("status");
                    if (res.equals("DELETED")) {
                        Log.d("DELETE", "successfully deleted the patient");
                        Toast.makeText(getBaseContext(), "successfully deleted the prescription", Toast.LENGTH_LONG).show();
                        mCardArrayAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("DELETE", "unsuccessfully deleted the patient");
                        //It is an example.

                        Toast.makeText(getBaseContext(), "Could not delete the patient, prescription is dispensed or prescription can't be deleted at the moment, try again later", Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                String creds = String.format("%s:%s", MyShortcuts.getDefaults("email", getBaseContext()), MyShortcuts.getDefaults("password", getBaseContext()));

                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                headers.put("Authorization", auth);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
//                Log.e("category id", getIntent().getStringExtra("category_id"));
//                params.put("ids", ID);


                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        Log.e("request is", strReq.toString());
    }
}
