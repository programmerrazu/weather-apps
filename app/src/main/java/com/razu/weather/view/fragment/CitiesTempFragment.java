package com.razu.weather.view.fragment;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.razu.weather.Apps;
import com.razu.weather.R;
import com.razu.weather.adapter.WeatherAdapter;
import com.razu.weather.factory.WeatherViewModelFactory;
import com.razu.weather.interfaces.FragmentChanger;
import com.razu.weather.listener.OnItemClickListener;
import com.razu.weather.model.Weather;
import com.razu.weather.repository.WeatherRepository;
import com.razu.weather.viewModel.WeatherViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CitiesTempFragment extends Fragment {

    private static final String TAG = "CitiesTempFragment";
    private Context context;
    private WeatherViewModel weatherViewModel;
    private List<Weather> weatherList;
    private WeatherAdapter weatherAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressDialog progressDialog;
    private boolean dataLoaderStatus = true;
    private FragmentChanger changer;

    @Bind(R.id.rl_weather_container)
    RelativeLayout rlWeatherContainer;

    @Bind(R.id.rl_weather_no_net_msg)
    RelativeLayout rlWeatherNoNetMsg;

    @Bind(R.id.srl_weather)
    SwipeRefreshLayout srlWeather;

    @Bind(R.id.recycler_view_weather)
    RecyclerView rvWeather;

    public CitiesTempFragment() {

    }

    public static CitiesTempFragment getInstance() {
        return new CitiesTempFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cities_temp, container, false);
        context = container.getContext();
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.changer = (FragmentChanger) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(null);
        toolbar.setNavigationOnClickListener(null);

        weatherViewModel = ViewModelProviders.of(getActivity(), new WeatherViewModelFactory(getActivity().getApplication(),
                new WeatherRepository(getActivity().getApplication()))).get(WeatherViewModel.class);

        progressDialog = new ProgressDialog(context);
        Apps.dataLoaderStart(progressDialog, getString(R.string.waiting_msg), true);
        srlWeather.setColorSchemeResources(R.color.pink, R.color.indigo, R.color.lime);

        layoutManager = new LinearLayoutManager(Apps.getAppsContext());
        rvWeather.setLayoutManager(layoutManager);
        rvWeather.setHasFixedSize(true);
        rvWeather.setNestedScrollingEnabled(false);
        weatherList = new ArrayList<>();
        weatherAdapter = new WeatherAdapter(context, weatherList, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Weather weather = weatherList.get(position);
                Fragment fragment = LocationViewFragment.getInstance();
                Bundle bundle = new Bundle();
                bundle.putSerializable("weather", weather);
                fragment.setArguments(bundle);
                changer.onChangeFragment(fragment);
            }
        });
        rvWeather.setAdapter(weatherAdapter);
        rvWeather.setItemAnimator(new DefaultItemAnimator());

        getWeatherData();
        srlWeather.setOnRefreshListener(this::getWeatherData);
    }

    @OnClick(R.id.tv_weather_retry)
    void retry() {
        dataLoaderStatus = false;
        Apps.dataLoaderStart(progressDialog, getString(R.string.waiting_msg), true);
        getWeatherData();
    }

    private void getWeatherData() {
        if (Apps.isNetworkAvailable()) {

            if (rlWeatherContainer.getVisibility() != View.VISIBLE) {
                rlWeatherContainer.setVisibility(View.VISIBLE);
            }
            if (rlWeatherNoNetMsg.getVisibility() == View.VISIBLE) {
                rlWeatherNoNetMsg.setVisibility(View.GONE);
            }

            weatherViewModel.getWeatherResponseData().observe(getActivity(), weather -> {

                weatherAdapter.removeItem();
                Weather w;
                assert weather != null;

                for (Weather ww : weather) {
                    w = new Weather();

                    w.setId(ww.getId());
                    w.setName(ww.getName());
                    w.setDt(ww.getDt());
                    w.setCoord(ww.getCoord());
                    w.setMain(ww.getMain());
                    w.setWind(ww.getWind());
                    w.setSys(ww.getSys());
                    w.setClouds(ww.getClouds());

                    w.setWeathersList(ww.getWeathersList());

                    weatherList.add(w);
                }
                weatherAdapter.notifyDataSetChanged();
                if (srlWeather.isRefreshing()) {
                    srlWeather.setRefreshing(false);
                }
                Apps.dataLoaderStop(progressDialog);
            });
        } else {
            if (!dataLoaderStatus) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dataLoaderStatus = true;
                        Apps.dataLoaderStop(progressDialog);
                    }
                }, 1000);
            } else {
                Apps.dataLoaderStop(progressDialog);
            }
            if (srlWeather.isRefreshing()) {
                srlWeather.setRefreshing(false);
            }
            if (rlWeatherContainer.getVisibility() == View.VISIBLE) {
                rlWeatherContainer.setVisibility(View.GONE);
            }
            if (rlWeatherNoNetMsg.getVisibility() != View.VISIBLE) {
                rlWeatherNoNetMsg.setVisibility(View.VISIBLE);
            }
        }
    }
}