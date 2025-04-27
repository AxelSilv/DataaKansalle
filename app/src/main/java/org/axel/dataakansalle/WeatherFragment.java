package org.axel.dataakansalle;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class WeatherFragment extends Fragment {
    private TextView municipalityName;
    private ImageView weatherIcon;
    private TextView txtTemperature;
    private TextView txtWeatherData, windSpeed;
    private TextView employmentRate;
    private TextView jobSelfSufficiency;


    private static final String ARG_MUNICIPALITY = "municipality";


    public static WeatherFragment getInstance(String municipality) {
        WeatherFragment fragment = new WeatherFragment();
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
        employmentRate = view.findViewById(R.id.employmentRate);
        jobSelfSufficiency = view.findViewById(R.id.jobSelfSufficiency);
        if (getArguments() != null) {
            municipality = getArguments().getString(ARG_MUNICIPALITY);
            fetchWeatherData(municipality);
            fetchEmploymentData();
            fetchJobData();
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
                    municipalityName.setText(municipality);
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
    private void fetchEmploymentData() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            EmploymentRetriever retriever = new EmploymentRetriever();
            var data = retriever.getData(requireContext(), municipality);
            if (data != null && getActivity() != null) {
                var latest = data.get(data.size() - 1);
                String text = String.format("Työllisyysaste (%d): %.1f %%", latest.getYear(), latest.getEmployment());
                requireActivity().runOnUiThread(() -> {
                    employmentRate.setText(text);
                });
            }
        });
    }

    private void fetchJobData() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            JobDataRetriever retriever = new JobDataRetriever();
            var data = retriever.getData(requireContext(), municipality);
            if (data != null && getActivity() != null) {
                var latest = data.get(data.size() - 1);
                String text = String.format("Työpaikkaomavaraisuus (%d): %.1f %%", latest.getYear(), latest.getEmploymentSufficiency());
                requireActivity().runOnUiThread(() -> {
                    jobSelfSufficiency.setText(text);
                });
            }
        });
    }
}