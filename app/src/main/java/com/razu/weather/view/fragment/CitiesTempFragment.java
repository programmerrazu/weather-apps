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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class CitiesTempFragment extends Fragment {

    private static final String TAG = "CitiesTempFragment";
    private Context context;
    private WeatherViewModel weatherViewModel;
    private List<Weather> weatherList;
    private RecyclerView rvWeather;
    private WeatherAdapter weatherAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout srlWeather;
    private RelativeLayout rlWeatherContainer, rlWeatherNoNetMsg;
    private boolean dataLoaderStatus = true;
    private FragmentChanger changer;

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
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_cities_temp, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.changer = (FragmentChanger) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        weatherViewModel = ViewModelProviders.of(getActivity(), new WeatherViewModelFactory(getActivity().getApplication(),
                new WeatherRepository(getActivity().getApplication()))).get(WeatherViewModel.class);

        progressDialog = new ProgressDialog(context);
        Apps.dataLoaderStart(progressDialog, getString(R.string.waiting_msg), true);

        rlWeatherContainer = (RelativeLayout) view.findViewById(R.id.rl_weather_container);
        rlWeatherNoNetMsg = (RelativeLayout) view.findViewById(R.id.rl_weather_no_net_msg);
        srlWeather = (SwipeRefreshLayout) view.findViewById(R.id.srl_weather);
        srlWeather.setColorSchemeResources(R.color.pink, R.color.indigo, R.color.lime);
        TextView tvEventRetry = (TextView) view.findViewById(R.id.tv_weather_retry);

        rvWeather = (RecyclerView) view.findViewById(R.id.recycler_view_weather);
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
        tvEventRetry.setOnClickListener(v -> {
            dataLoaderStatus = false;
            Apps.dataLoaderStart(progressDialog, getString(R.string.waiting_msg), true);
            getWeatherData();
        });
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