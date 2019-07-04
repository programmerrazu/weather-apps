package com.razu.weather.view.activity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.razu.weather.Apps;
import com.razu.weather.R;
import com.razu.weather.factory.WeatherViewModelFactory;
import com.razu.weather.interfaces.FragmentChanger;
import com.razu.weather.notification.AlarmNotificationReceiver;
import com.razu.weather.notification.NotificationService;
import com.razu.weather.repository.WeatherRepository;
import com.razu.weather.service.LocationService;
import com.razu.weather.view.fragment.CitiesTempFragment;
import com.razu.weather.view.fragment.LocationViewFragment;
import com.razu.weather.viewModel.WeatherViewModel;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.app.PendingIntent.FLAG_ONE_SHOT;

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

        startNotificationService();
        registerReceiver(broadcastReceiver, new IntentFilter("send_notification"));

        //notificationStarter(true, true);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getCurrentLocationToNotification();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private void attachRootFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction()
                .replace(R.id.main_frame_layout, CitiesTempFragment.getInstance())
                .addToBackStack(BACK_STACK_ROOT_TAG)
                .commit();
    }

    private void startNotificationService() {
        Intent i = new Intent(this, NotificationService.class);
        startService(i);
    }

    private void getCurrentLocationToNotification() {

        WeatherViewModel weatherViewModel = ViewModelProviders.of(this, new WeatherViewModelFactory(getApplication(),
                new WeatherRepository(getApplication()))).get(WeatherViewModel.class);

        Location location = new LocationService(this).getLocation();

        if (location != null) {
            weatherViewModel.getCurrentWeatherResponseData(location.getLatitude(), location.getLongitude()).observe(this, weather -> {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

                Intent myIntent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(
                        this,
                        0,
                        myIntent,
                        FLAG_ONE_SHOT);

                builder.setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.ic_n_icon)
                        .setContentTitle("Weather App")
                        .setContentIntent(pendingIntent)
                        .setContentText("Current Temperature " + weather.getMain().getTemp())
                        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                        .setContentInfo("Weather");

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1, builder.build());
            });
        }
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