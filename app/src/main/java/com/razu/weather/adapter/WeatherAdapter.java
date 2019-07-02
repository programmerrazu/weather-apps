package com.razu.weather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.razu.weather.R;
import com.razu.weather.listener.OnItemClickListener;
import com.razu.weather.model.Weather;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherHolder> {

    private static final String TAG = "WeatherAdapter";
    private Context context;
    private List<Weather> weatherList;
    private OnItemClickListener listener;

    public WeatherAdapter(Context context, List<Weather> weatherList, OnItemClickListener listener) {
        this.context = context;
        this.weatherList = weatherList;
        this.listener = listener;
    }

    @Override
    public WeatherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_list_row, parent, false);
        return new WeatherHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WeatherHolder holder, int position) {
        Weather weather = weatherList.get(position);

        holder.tvCityName.setText(weather.getName());
        holder.tvWeathers.setText(weather.getWeathersList().get(0).getMain());
        holder.tvTemp.setText(String.valueOf(weather.getMain().getTemp()));
        holder.tvTempUnit.setText("C");
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public void removeItem() {
        if (!weatherList.isEmpty()) {
            weatherList.clear();
            notifyDataSetChanged();
        }
    }

    class WeatherHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvCityName, tvWeathers, tvTemp, tvTempUnit;

        WeatherHolder(View view) {
            super(view);

            tvCityName = (TextView) view.findViewById(R.id.tv_city_name);
            tvWeathers = (TextView) view.findViewById(R.id.tv_weathers);
            tvTemp = (TextView) view.findViewById(R.id.tv_temp);
            tvTempUnit = (TextView) view.findViewById(R.id.tv_temp_unit);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v, getAdapterPosition());
        }
    }
}