package com.oneshoppoint.yates.yates.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.oneshoppoint.yates.yates.Interface.ServerCallback;
import com.oneshoppoint.yates.yates.R;
import com.oneshoppoint.yates.yates.controllers.PostData;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by stephineosoro on 07/06/16.
 */
public class Utils {
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;

    //    Setting up base URL
    public static String baseURL() {
        return "http://api.oneshoppoint.com/";
//        return "http://41.89.64.90:8000/api/";
//        return "http://41.204.186.47:8000/twiga/";

    }

    public static void set(String username, String password, Context context) {
        setDefaults("username", username, context);
        setDefaults("password", password, context);
    }

    //JWT Authentication headers
    public static Map<String, String> jwtAuthHeaders(String token) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        String auth = "jwt " + token;
        headers.put("Authorization", auth);
        return headers;
    }

    //    Basic Aunthentication headers
    public static HashMap<String, String> AunthenticationHeaders(Context context) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        String creds = String.format("%s:%s", "web@oneshoppoint.com", "spr0iPpQAiwS8u");
        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
        headers.put("Authorization", auth);
        return headers;
    }

    public static HashMap<String, String> AunthenticationHeadersAdmin(Context context) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        String creds = String.format("%s:%s", getDefaults("username", context), getDefaults("password", context));
        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
        headers.put("Authorization", auth);
        return headers;
    }

    //  Storing temporary values to shared preferences

    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    //    Getting item from shared preference
    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }


    //    Showing toast
    public static void showToast(String text, Context context) {

        /*Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();*/

        Toast ToastMessage = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        View toastView = ToastMessage.getView();
        toastView.setBackgroundResource(R.drawable.toast_background_color);
        ToastMessage.show();
    }

    //    checking if there's internet connection before proceeding
    public static boolean hasInternetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }

    //    Checking if item is in shared preference
    public static Boolean checkDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.contains(key);
    }

    //    Deleting all items in shared preference
    public static void Delete(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }

    public static void applyFontForToolbarTitle(Activity context) {
        Toolbar toolbar = (Toolbar) context.findViewById(R.id.toolbar);
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                Typeface titleFont = Typeface.
                        createFromAsset(context.getAssets(), "OpenSans-Light.ttf");
                if (tv.getText().equals(toolbar.getTitle())) {
                    tv.setTypeface(titleFont);
                    break;
                }
            }
        }
    }

    public static Typeface font(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "OpenSans-Light.ttf");
    }

    public static void showProceed(String msg, final Context ctx, final Class<?> cls, final Class<?> cls2) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ctx);
        builder1.setMessage(msg);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(ctx, cls);
                        ctx.startActivity(intent);
//                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(ctx, cls2);
                        ctx.startActivity(intent);
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    public static void showDialog(String msg, final Context ctx, final Class<?> cls, final Class<?> cls2) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ctx);
        builder1.setMessage(msg);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(ctx, cls);
                        ctx.startActivity(intent);
//                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(ctx, cls2);
                        ctx.startActivity(intent);
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }


    public static boolean checkLocationPermission(final Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static void showPermissionDialog(Activity context) {
        ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
    }

    public static void sentEmail(Context mContext, String[] addresses, String subject, String body) {

        try {
            Intent sendIntentGmail = new Intent(Intent.ACTION_VIEW);
            sendIntentGmail.setType("plain/text");
            sendIntentGmail.setData(Uri.parse(TextUtils.join(",", addresses)));
            sendIntentGmail.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
            sendIntentGmail.putExtra(Intent.EXTRA_EMAIL, addresses);
            if (subject != null) sendIntentGmail.putExtra(Intent.EXTRA_SUBJECT, subject);
            if (body != null) sendIntentGmail.putExtra(Intent.EXTRA_TEXT, body);
            mContext.startActivity(sendIntentGmail);
        } catch (Exception e) {
            //When Gmail App is not installed or disable
            Intent sendIntentIfGmailFail = new Intent(Intent.ACTION_SEND);
            sendIntentIfGmailFail.setType("*/*");
            sendIntentIfGmailFail.putExtra(Intent.EXTRA_EMAIL, addresses);
            if (subject != null) sendIntentIfGmailFail.putExtra(Intent.EXTRA_SUBJECT, subject);
            if (body != null) sendIntentIfGmailFail.putExtra(Intent.EXTRA_TEXT, body);
            if (sendIntentIfGmailFail.resolveActivity(mContext.getPackageManager()) != null) {
                mContext.startActivity(sendIntentIfGmailFail);
            }
        }
    }

    public static void setClipboard(Context context, String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    /**
     * Returns MAC address of the given interface name.
     *
     * @param interfaceName eth0, wlan0 or NULL=use first interface
     * @return mac address or empty string
     */
    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null) return "";
                StringBuilder buf = new StringBuilder();
                for (int idx = 0; idx < mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));
                if (buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
        /*try {
            // this is so Linux hack
            return loadFileAsString("/sys/class/net/" +interfaceName + "/address").toUpperCase().trim();
        } catch (IOException ex) {
            return null;
        }*/
    }

    /**
     * Get IP address from first non-localhost interface
     *
     * @param useIPv4 true=return ipv4, false=return ipv6
     * @return address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }

    public static void getToken(Context ctx, ServerCallback callback) {
        Map<String, String> credentials = new HashMap<String, String>();

       /* credentials.put("username", Utils.getDefaults("username", ctx));
        credentials.put("password", Utils.getDefaults("password", ctx));*/
//       TODO Remove below. Only for testing purposes
        credentials.put("username", "test");
        credentials.put("password", "Medic2018");
        PostData postData = new PostData(ctx);
        postData.post("auth/jwt/create/", credentials, null, null, callback);
    }

}
