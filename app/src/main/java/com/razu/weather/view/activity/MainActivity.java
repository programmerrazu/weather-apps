package com.razu.weather.view.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.razu.weather.Apps;
import com.razu.weather.R;
import com.razu.weather.interfaces.FragmentChanger;
import com.razu.weather.notification.AlarmNotificationReceiver;
import com.razu.weather.view.fragment.CitiesTempFragment;
import com.razu.weather.view.fragment.LocationViewFragment;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements FragmentChanger {

    private static final String BACK_STACK_ROOT_TAG = "root_fragment";
    private boolean doubleBackPressed = false;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.tv_toolbar)
    TextView tvToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachRootFragment();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        tvToolbar.setText("Weather App");

        notificationStarter(true,true);
    }

    private void attachRootFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction()
                .replace(R.id.main_frame_layout, CitiesTempFragment.getInstance())
                .addToBackStack(BACK_STACK_ROOT_TAG)
                .commit();
    }

    private void notificationStarter(boolean isNotification, boolean isRepeat) {

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 30);

        myIntent = new Intent(MainActivity.this, AlarmNotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);

        if (!isRepeat)
            manager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 3000, pendingIntent);
        else
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    @Override
    public void onChangeFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        Fragment fm = getSupportFragmentManager().findFragmentById(R.id.main_frame_layout);
        if (fm instanceof CitiesTempFragment) {
            if (doubleBackPressed) {
                finish();
            } else {
                doubleBackPressed = true;
                Apps.snackBarMsg(findViewById(android.R.id.content), getString(R.string.back_press), 0);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackPressed = false;
                    }
                }, 3000);
            }
        } else if (fm instanceof LocationViewFragment) {
            attachRootFragment();
        }
    }
}