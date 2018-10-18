package com.oneshoppoint.yates.yates;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;


import com.oneshoppoint.yates.yates.Interface.ServerCallback;
import com.oneshoppoint.yates.yates.controllers.GetData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardGridView;

public class getFeedback extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Feedback");
        getFeedBack();
    }

    private void getFeedBack() {
        GetData getData = new GetData(getBaseContext());
        getData.online_data("feedback", null, null, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("String result", result);

                try {
                    ArrayList<Card> cards = new ArrayList<Card>();

                    JSONObject res = new JSONObject(result);
                    JSONArray data = res.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject item = data.getJSONObject(i);
                        GplayGridCard feedback = new GplayGridCard(getBaseContext());
                        feedback.name = item.getString("name");
                        feedback.email = item.getString("email");
                        feedback.phone = item.getString("phone");
                        feedback.subject = item.getString("subject");
                        feedback.feedback = item.getString("feedback");
                        feedback.area = item.getString("area");
                        feedback.setId(item.getString("id"));
                        feedback.init();
                        cards.add(feedback);

                    }
                    CardGridArrayAdapter mCardArrayAdapter = new CardGridArrayAdapter(getBaseContext(), cards);

                    CardGridView listView = (CardGridView) findViewById(R.id.carddemo_grid_base1);
                    if (listView != null) {
                        listView.setAdapter(mCardArrayAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(JSONObject response) {
                Log.e("JSON result", response.toString());
            }
        });
    }

    public class GplayGridCard extends Card {

        protected String mTitle;
        protected TextView mSecondaryTitle;
        protected RatingBar mRatingBar;
        protected int resourceIdThumbnail = -1;
        protected int count;
        protected String url;

        protected String headerTitle;
        protected String secondaryTitle;

        protected String name;
        protected String id;
        protected String email;
        protected String phone;
        protected String subject;
        protected String area;
        protected String feedback;
        protected String feedback_date;

        public GplayGridCard(Context context) {
            super(context, R.layout.inner_content_detail_feedback);
        }


        public GplayGridCard(Context context, int innerLayout) {
            super(context, innerLayout);
        }

        private void init() {

            CardHeader header = new CardHeader(getContext());
            header.setButtonOverflowVisible(true);
            header.setTitle(headerTitle);
            header.setPopupMenu(R.menu.popupmain, new CardHeader.OnClickCardHeaderPopupMenuListener() {
                @Override
                public void onMenuItemClick(BaseCard card, MenuItem item) {
//                    Toast.makeText(getContext(), "Item " + item.getTitle(), Toast.LENGTH_SHORT).show();
                    String selected = card.getId();
                    Log.e("card id is", card.getId());

                   /* final TextView subtitle = (TextView) findViewById(R.id.carddemo_gplay_main_inner_subtitle);
                    if (item.getTitle().equals("Info")) {
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        intent.putExtra("id", selected);
                        startActivity(intent);
                    }*/
                }
            });

            addCardHeader(header);

            OnCardClickListener clickListener = new OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    //Do something
                }
            };

            addPartialOnClickListener(Card.CLICK_LISTENER_CONTENT_VIEW, clickListener);
            setOnClickListener(new OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
//                    Do something
                    String selected = card.getId();
                }
            });


        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {

           /* TextView title = (TextView) view.findViewById(R.id.name);
            title.setText(name);*/

            final TextView phone_no = (TextView) view.findViewById(R.id.phone);
            phone_no.setText(phone);
            final TextView address = (TextView) view.findViewById(R.id.email);
            address.setText(email);
            final TextView subject_title = (TextView) view.findViewById(R.id.subject);
            subject_title.setText(subject);
            final TextView feedback_ = (TextView) view.findViewById(R.id.feedback);
            feedback_.setText(feedback);
            final TextView area_ = (TextView) view.findViewById(R.id.area);
            area_.setText(area);


//            subtitle.setTextIsSelectable(true);
            address.setClickable(true);
            address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(address.getText());

                }
            });

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

}
