package com.oneshoppoint.yates.yates;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.oneshoppoint.yates.yates.Interface.ServerCallback;
import com.oneshoppoint.yates.yates.Interface.VolleyCallback;
import com.oneshoppoint.yates.yates.Model.Post;
import com.oneshoppoint.yates.yates.controllers.GetData;
import com.oneshoppoint.yates.yates.controllers.PostData;
import com.thefinestartist.Base;
import com.thefinestartist.finestwebview.FinestWebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.oneshoppoint.yates.yates.utils.Utils.applyFontForToolbarTitle;
import static com.oneshoppoint.yates.yates.utils.Utils.baseURL;
import static com.oneshoppoint.yates.yates.utils.Utils.showToast;

public class PrescriptionWebViewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected Typeface mTfLight;
    ArrayList<String> items = new ArrayList<>();
    private EditText first_name, last_name, phone, email, medic_id;
    private String county = "";
    private int PICK_IMAGE_REQUEST = 1;
    private static Bitmap bitmap;
    static boolean imagetrue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_web_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        applyFontForToolbarTitle(this);
        mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");

        webView();

        first_name = (EditText) findViewById(R.id.first_name);
        first_name.setTypeface(mTfLight);

        last_name = (EditText) findViewById(R.id.last_name);
        last_name.setTypeface(mTfLight);

        phone = (EditText) findViewById(R.id.phone_number);
        phone.setTypeface(mTfLight);

        email = (EditText) findViewById(R.id.email);
        email.setTypeface(mTfLight);

        medic_id = (EditText) findViewById(R.id.medic_id);
        medic_id.setTypeface(mTfLight);

        Button buttonChoose = (Button) findViewById(R.id.btn_choose);
        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        Button submit = (Button) findViewById(R.id.btn_submit);
        submit.setTypeface(mTfLight);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });


        String[] new_route_items = new String[]{"No", "Yes"};

        GetData getData = new GetData(this);
        getData.online_data("counties", null, null, new ServerCallback() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray data = jsonObject.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {
                        items.add(data.getJSONObject(i).getString("name"));
                    }

                    Spinner dropdown = (Spinner) findViewById(R.id.choose_county);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, items);
                    dropdown.setAdapter(adapter);

                    dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            county = parent.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(JSONObject response) {

            }
        });


//        new FinestWebView.Builder(this).show("http://yatehealths.com/prescription.html");


       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

    private void uploadData() {

        if (!imagetrue) {
            showToast("Please pick an image first", getBaseContext());
            return;
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("firstname", first_name.getText().toString());
        params.put("lastname", last_name.getText().toString());
        params.put("email", email.getText().toString());
        params.put("phonenumber", phone.getText().toString());
        params.put("county", county);
        params.put("medic_id", medic_id.getText().toString());
        params.put("prescription_image", getStringImage(bitmap));

        PostData post = new PostData(getBaseContext());
        post.post("apipost", params, null, null, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("string result", result);
            }

            @Override
            public void onSuccess(JSONObject response) {
                Log.e("json result", response.toString());

            }
        });

    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                imagetrue = true;

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Log.e("image chosen", bitmap.toString());
                TextView tv = (TextView) findViewById(R.id.imageChosen);
                tv.setVisibility(View.VISIBLE);
                //Setting the Bitmap to ImageView
//                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String encodedImage = null;
        try {
            bmp.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        } catch (Exception e) {
            Log.e("Bitmap", e.toString());
        }
        return encodedImage;
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
            Intent intent = new Intent(getBaseContext(), PrescriptionWebViewActivity.class);
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
           /* Intent intent = new Intent(getBaseContext(), .class);
            startActivity(intent);*/

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
