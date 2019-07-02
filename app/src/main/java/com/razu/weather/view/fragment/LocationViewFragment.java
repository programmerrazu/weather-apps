package com.razu.weather.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.razu.weather.R;
import com.razu.weather.model.Weather;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LocationViewFragment extends Fragment {

    private Context context;
    private static final String TAG = "LocationViewFragment";
    private static final float MAP_ZOOM = 14.0f;
    private SupportMapFragment mapFragment;
    private GoogleMap mapLocation;
    private Weather weather;

    @Bind(R.id.tv_city)
    TextView tvCity;

    @Bind(R.id.tv_weather)
    TextView tvWeather;

    @Bind(R.id.tv_hum)
    TextView tvHum;

    @Bind(R.id.tv_wind)
    TextView tvWind;

    @Bind(R.id.tv_max_temp)
    TextView tvMaxTemp;

    @Bind(R.id.tv_min_temp)
    TextView tvMinTemp;

    @Bind(R.id.tv_temp)
    TextView tvTemp;

    @Bind(R.id.tv_temp_unit)
    TextView tvTempUnit;

    public LocationViewFragment() {

    }

    public static LocationViewFragment getInstance() {
        return new LocationViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_view, container, false);
        this.context = container.getContext();
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            weather = (Weather) bundle.getSerializable("weather");

            tvCity.setText(weather.getName());
            tvWeather.setText("Clear Sky");
            tvHum.setText("Humidity : " + weather.getMain().getHumidity());
            tvWind.setText("Wind Speed : " + weather.getWind().getSpeed());
            tvMaxTemp.setText("Max Temp : " + weather.getMain().getTempMax());
            tvMinTemp.setText("Min Temp : " + weather.getMain().getTempMin());
            tvTemp.setText(String.valueOf(weather.getMain().getTemp()));
            tvTempUnit.setText("C");
        }

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_location);
        mapFragment.getMapAsync(googleMap -> {
            mapLocation = googleMap;

            UiSettings settings = mapLocation.getUiSettings();
            settings.setAllGesturesEnabled(false);
            settings.setMapToolbarEnabled(false);
            settings.setZoomControlsEnabled(false);
            // mapLocation.setMapStyle(MapStyleOptions.loadRawResourceStyle(context.getApplicationContext(), R.raw.map_style_gray_scale));

            LatLng latLng = new LatLng(weather.getCoord().getLat(), weather.getCoord().getLon());
            mapLocation.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            mapLocation.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, MAP_ZOOM));
        });
    }
}