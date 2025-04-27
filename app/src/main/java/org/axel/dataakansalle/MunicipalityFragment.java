package org.axel.dataakansalle;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MunicipalityFragment extends Fragment {

    private EditText editMunicipality1, editMunicipality2;
    private TextView txtResult1, txtResult2;
    private Button buttonCompare;

    private static final String ARG_MUNICIPALITY = "municipality";
    private String municipality;

    public MunicipalityFragment() {}

    public static MunicipalityFragment newInstance(String municipality) {
        MunicipalityFragment fragment = new MunicipalityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MUNICIPALITY, municipality);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            municipality = getArguments().getString(ARG_MUNICIPALITY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_municipality, container, false);
        editMunicipality1 = view.findViewById(R.id.editMunicipality1);
        editMunicipality2 = view.findViewById(R.id.editMunicipality2);
        buttonCompare = view.findViewById(R.id.buttonCompare);
        txtResult1 = view.findViewById(R.id.txtResult1);
        txtResult2 = view.findViewById(R.id.txtResult2);

        buttonCompare.setOnClickListener(v -> {
            String m1 = editMunicipality1.getText().toString().trim();
            String m2 = editMunicipality2.getText().toString().trim();

            if (!m1.isEmpty() && !m2.isEmpty()) {
                fetchComparisonData(m1, m2);
            } else {
                txtResult1.setText("Syötä molemmat kunnat.");
                txtResult2.setText("");
            }
        });
        return view;
    }

    private void fetchComparisonData(String m1, String m2) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            MunicipalityDataRetriever municipalityDataRetriever = new MunicipalityDataRetriever();
            WeatherDataRetriever weatherDataRetriever = new WeatherDataRetriever();

            List<MunicipalityData> data1 = municipalityDataRetriever.getData(requireContext(), m1);
            List<MunicipalityData> data2 = municipalityDataRetriever.getData(requireContext(), m2);

            WeatherData weather1 = weatherDataRetriever.getWeatherData(m1);
            WeatherData weather2 = weatherDataRetriever.getWeatherData(m2);

            requireActivity().runOnUiThread(() -> {
                if (data1 != null && !data1.isEmpty()) {
                    txtResult1.setText(formatMunicipalityInfo(data1, weather1, m1));
                } else {
                    txtResult1.setText(m1 + ": Ei löytynyt väestödataa.");
                }

                if (data2 != null && !data2.isEmpty()) {
                    txtResult2.setText(formatMunicipalityInfo(data2, weather2, m2));
                } else {
                    txtResult2.setText(m2 + ": Ei löytynyt väestödataa.");
                }
            });
        });
    }

    private String formatMunicipalityInfo(List<MunicipalityData> popData, WeatherData weatherData, String name) {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(":\n");

        boolean foundData = false;

        for (MunicipalityData d : popData) {
            if (d.getYear() == 2023) {
                sb.append("Väkiluku: ").append(d.getPopulation()).append("\n");
                sb.append("Väestönmuutos: ").append(d.getPopulationChange()).append("\n");
                foundData = true;
            }
        }

        if (weatherData != null) {
            sb.append("Säätila: ").append(weatherData.getMain()).append("\n");
            sb.append("Lämpötila: ").append(weatherData.getTemperature()).append(" °C\n");
            sb.append("Tuulen nopeus: ").append(weatherData.getWindSpeed()).append(" m/s\n");
            foundData = true;
        } else {
            sb.append("Säätietoja ei saatavilla.\n");
        }

        if (!foundData) {
            sb.append("Ei saatavilla olevaa dataa vuodelle 2023.");
        }

        return sb.toString();
    }
}
