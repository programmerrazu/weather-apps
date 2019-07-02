package com.razu.weather;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

public class Apps extends Application {

    private static Apps instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Apps getAppsContext() {
        if (instance == null)
            instance = new Apps();
        return instance;
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getAppsContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isLocationEnabled() {
        Context context = getAppsContext().getApplicationContext();
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void redirect(Context context, final Class<? extends Activity> activityToOpen) {
        Intent intent = new Intent(context, activityToOpen);
        ((Activity) context).startActivity(intent);
        ((Activity) context).finish();
        ((Activity) context).overridePendingTransition(0, 0);
    }

    public static void dataLoaderStart(ProgressDialog pDialog, String msg, Boolean cancelable) {
        pDialog.setIndeterminate(true);
        pDialog.setMessage(msg);
        pDialog.setCancelable(cancelable);
        pDialog.show();
    }

    public static void dataLoaderStop(ProgressDialog pDialog) {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    public static void snackBarMsg(View view, String msg, int length) {
        Snackbar snackbar = Snackbar.make(view, msg, length);
        snackbar.setAction(getAppsContext().getString(R.string.dismiss), v -> snackbar.dismiss()).setActionTextColor(Color.WHITE);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(getAppsContext().getResources().getColor(R.color.appsColor));
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }
}