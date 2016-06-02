package com.oneshoppoint.yates.yates;

/**
 * Created by stephineosoro on 19/05/16.
 */

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.oneshoppoint.yates.yates.fragment.RecyclerViewFragment;


public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = HomePage.class.getSimpleName();


    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rategroup);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_fa_home);

        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // handle the menu item
                        return true;
                    }
                }
        );

        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setTitle("Yates");

        initViewPagerAndTabs();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initViewPagerAndTabs() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager1);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new RecyclerViewFragment(), "Test");
//        pagerAdapter.addFragment(new ExploreFragment(), "Explore your Sacco");
//        pagerAdapter.addFragment(new PostCommentFragment(), "Comments");
//        pagerAdapter.addFragment(new ViewHighestFragment(), "Winners");
        try {
            viewPager.setAdapter(pagerAdapter);
        } catch (NullPointerException e) {
            Log.e("NUllPOINTER", e.toString());
        }


        TabLayout tabLayout = (TabLayout) findViewById(R.id.movieTabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
//
//         if (id == R.id.nav_slideshow) {
//            Intent intent = new Intent(this, NewHomeTimer.class);
//            startActivity(intent);
////            finish();
//
//        } else if (id == R.id.nav_manage) {
//            Intent intent = new Intent(this, HomePage.class);
//            startActivity(intent);
//
//
//        } else if (id == R.id.log_out) {
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

