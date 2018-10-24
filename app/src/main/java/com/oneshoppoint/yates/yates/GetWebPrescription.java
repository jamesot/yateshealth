package com.oneshoppoint.yates.yates;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.thefinestartist.finestwebview.FinestWebView;

import static com.oneshoppoint.yates.yates.utils.Utils.applyFontForToolbarTitle;

public class GetWebPrescription extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    protected Typeface mTfLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_web_prescription);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Get prescription");

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

        webView();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    private void webView() {
        new FinestWebView.Builder(this).theme(R.style.FinestWebViewTheme)
                .titleDefault("Get Prescription | Yates Health")
                .showUrl(false)
                .statusBarColorRes(R.color.yatesgreen)
                .toolbarColorRes(R.color.yatesgreen)
                .titleColorRes(R.color.finestWhite)
                .urlColorRes(R.color.yatesyellow)
                .iconDefaultColorRes(R.color.finestWhite)
                .progressBarColorRes(R.color.finestWhite)
                .stringResCopiedToClipboard(R.string.copied_to_clipboard)
                .stringResCopiedToClipboard(R.string.copied_to_clipboard)
                .stringResCopiedToClipboard(R.string.copied_to_clipboard)
                .showSwipeRefreshLayout(true)
                .swipeRefreshColorRes(R.color.yatesgreen)
                .menuSelector(R.drawable.selector_light_theme)
                .menuTextGravity(Gravity.CENTER)
                .menuTextPaddingRightRes(R.dimen.defaultMenuTextPaddingLeft)
                .dividerHeight(0)
                .gradientDivider(false)
                .setCustomAnimations(R.anim.slide_up, R.anim.hold, R.anim.hold, R.anim.slide_down)
                .show("http://yatehealths.com/prescription.html");
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
}
