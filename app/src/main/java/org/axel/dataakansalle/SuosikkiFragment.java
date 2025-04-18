package org.axel.dataakansalle;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SuosikkiFragment extends Fragment {
    private TextView municipalityName;
    private ImageView weatherIcon;
    private TextView txtTemperature;
    private TextView txtWeatherData, windSpeed;


    private static final String ARG_MUNICIPALITY = "municipality";


    public static SuosikkiFragment getInstance(String municipality) {
        SuosikkiFragment fragment = new SuosikkiFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MUNICIPALITY, municipality);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private String municipality;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_suosikki, container, false);
        municipalityName = view.findViewById(R.id.locationName);
        weatherIcon = view.findViewById(R.id.weatherIcon);
        txtTemperature = view.findViewById(R.id.temperature);
        txtWeatherData = view.findViewById(R.id.description);
        windSpeed = view.findViewById(R.id.windSpeed);
        if (getArguments() != null) {
            municipality = getArguments().getString(ARG_MUNICIPALITY);
            fetchWeatherData(municipality);
        }
        return view;
    }

    private void fetchWeatherData(String municipality) {
        // jos haluu nii tääl paremmat iconit https://www.iconarchive.com
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            WeatherDataRetriever retriever = new WeatherDataRetriever();
            WeatherData data = retriever.getWeatherData(municipality);

            if (data != null && getActivity() != null) {
                requireActivity().runOnUiThread(() -> {
                    municipalityName.setText(data.getName());
                    txtTemperature.setText(data.convertToCelsius(data.getTemperature()) + " °C");
                    txtWeatherData.setText(data.getDescription());
                    windSpeed.setText(data.getWindSpeed() + " m/s");
                    String weatherMain = data.getMain().toLowerCase();

                    if (weatherMain.equals("clear")) {
                        weatherIcon.setImageResource(R.drawable.ic_sunny);
                    } else if (weatherMain.equals("rain")) {
                        weatherIcon.setImageResource(R.drawable.ic_rainy);
                    } else if (weatherMain.equals("clouds")) {
                        weatherIcon.setImageResource(R.drawable.ic_cloudy);
                    } else if (weatherMain.equals("snow")) {
                        weatherIcon.setImageResource(R.drawable.ic_snow);
                    } else {
                        weatherIcon.setImageResource(R.drawable.ic_weather_placeholder);
                    }

                });
            }
        });
    }
}