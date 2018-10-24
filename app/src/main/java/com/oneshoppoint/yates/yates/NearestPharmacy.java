package com.oneshoppoint.yates.yates;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardGridView;

import static com.oneshoppoint.yates.yates.utils.Utils.applyFontForToolbarTitle;
import static com.oneshoppoint.yates.yates.utils.Utils.hasInternetConnected;
import static com.oneshoppoint.yates.yates.utils.Utils.showToast;

public class NearestPharmacy extends AppCompatActivity implements OnLocationUpdatedListener, NavigationView.OnNavigationItemSelectedListener {

    private static final int LOCATION_PERMISSION_ID = 1001;
    protected static LocationGooglePlayServicesProvider provider;
    private static Location lastLocation = null;
    protected Typeface mTfLight;
    private static final int REQUEST_FINE_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_pharmacy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Nearest pharmacy");

        applyFontForToolbarTitle(this);
        mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (lastLocation == null)
            showToast("Please turn on your location for best results", getBaseContext());


        if (hasInternetConnected(getApplicationContext())) {
            startLocation();
        } else {
            showToast("Please turn on your internet connection to get nearby Chemists", getBaseContext());
        }

        if (!checkPermission(getBaseContext())) {
            showToast("Please accept location permission to be able to get nearest pharmacies", getBaseContext());
            ActivityCompat.requestPermissions(NearestPharmacy.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_ID);

            return;
        }

       /* SmartLocation.with(this).location()
                .oneFix().start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                showToast(location.toString() + "gotten location", getBaseContext());

                Log.e("location", location.toString());
//                TODO getting nearby pharmacies once user's location has been determined
//                getPharmacies();

            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void getPharmacies(final Location location) {
        GetData getData = new GetData(getBaseContext());
        getData.online_data("gpsinfo", null, null, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("String result", result);

                JSONObject res = null;
                try {
                    res = new JSONObject(result);
                    JSONObject gps = res.getJSONObject("gps");
                    JSONArray data = gps.getJSONArray("outlet_info");
                    ArrayList<Location> locations = new ArrayList<>();

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject item = data.getJSONObject(i);
                        Location location = new Location("");
                        location.setLatitude(Double.parseDouble(item.getString("latitude")));
                        location.setLongitude(Double.parseDouble(item.getString("longitude")));
                        locations.add(location);
                    }

                    nearestPharm(data, location, locations);

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

    private void nearestPharm(JSONArray data, Location userLocation, ArrayList<Location> locations) {
        Location closest = null;
        float smallestDistance = Float.MAX_VALUE;
        for (Location l : locations) {
            float dist = l.distanceTo(userLocation);
            if (dist < smallestDistance) {
                closest = l;
                smallestDistance = dist;
            }
        }

        try {
            ArrayList<Card> cards = new ArrayList<Card>();

            for (int i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                if ((closest.getLatitude() + "").equals(item.getString("latitude")) && (closest.getLongitude() + "").equals(item.getString("longitude"))) {
                    GplayGridCard pharmacy = new GplayGridCard(getBaseContext());
                    pharmacy.name = item.getString("name");
                    pharmacy.email = item.getString("email");
                    pharmacy.phone = item.getString("phonenumber");
                    pharmacy.zone = item.getString("zone");
                    pharmacy.paybill = item.getString("paybill");
                    pharmacy.distance = smallestDistance / 1000 + " Km";
                    pharmacy.setId(item.getString("outlet_id"));
                    pharmacy.init();
                    cards.add(pharmacy);
                }

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

    protected void startLocation() {

        provider = new LocationGooglePlayServicesProvider();
        provider.setCheckLocationSettings(true);

        SmartLocation smartLocation = new SmartLocation.Builder(this).logging(true).build();

        smartLocation.location(provider).start(this);


    }


    //    Function for stopping location updates from network provider
    private void stopLocation() {
        SmartLocation.with(this).location().stop();

        SmartLocation.with(this).activity().stop();
//        SmartLocation.with(this).geofencing().stop();

    }


    public static boolean checkPermission(final Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onLocationUpdated(Location location) {
        lastLocation = location;
        Log.e("location freq", location.toString());

//      TODO getting nearby pharmacies once user's location has been determined
        getPharmacies(location);
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
        protected String zone;
        protected String paybill;
        protected String distance;
        protected String feedback_date;

        public GplayGridCard(Context context) {
            super(context, R.layout.inner_content_detail_pharmacy);
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

            TextView title = (TextView) view.findViewById(R.id.name);
            title.setText(name);
            final TextView phone_no = (TextView) view.findViewById(R.id.phone);
            phone_no.setText(phone);
            final TextView address = (TextView) view.findViewById(R.id.email);
            address.setText(email);
            final TextView zone_title = (TextView) view.findViewById(R.id.zone);
            zone_title.setText(zone);
            final TextView distance_ = (TextView) view.findViewById(R.id.distance);
            distance_.setText(distance);
            final TextView paybill_ = (TextView) view.findViewById(R.id.paybill);
            paybill_.setText("paybill: " + paybill);


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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.get_prescription) {
            // Handle the camera action
            Intent intent = new Intent(getBaseContext(), GetWebPrescription.class);
            startActivity(intent);
        } else if (id == R.id.upload_prescription) {
            Intent intent = new Intent(getBaseContext(), PrescriptionWebViewActivity.class);
            startActivity(intent);

        } else if (id == R.id.feedback) {
            Intent intent = new Intent(getBaseContext(), getFeedback.class);
            startActivity(intent);

        } else if (id == R.id.post_feedback) {
            Intent intent = new Intent(getBaseContext(), postFeedback.class);
            startActivity(intent);

        } else if (id == R.id.nearest_pharmacies) {
            Intent intent = new Intent(getBaseContext(), NearestPharmacy.class);
            startActivity(intent);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // granted
                    startLocation();
                } else {
                    // not granted
                    showToast("You cannot get geo coordinates quickly without accepting this permission!", getBaseContext());
                    ActivityCompat.requestPermissions(NearestPharmacy.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_ID);
                }
                return;
            }

        }

    }

}
