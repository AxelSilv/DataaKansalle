package org.axel.dataakansalle;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChartFragment extends Fragment {

    private BarChart barChart;
    private TextView txtmunicipalityName;
    private TextView municipalityTitle;
    private String municipalityName;

    public static ChartFragment newInstance(String municipalityName) {
        ChartFragment fragment = new ChartFragment();
        Bundle args = new Bundle();
        args.putString("valittuKunta", municipalityName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            municipalityName = getArguments().getString("valittuKunta");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        barChart = view.findViewById(R.id.populationChart);
        txtmunicipalityName = view.findViewById(R.id.txtPopulationInfo);
        municipalityTitle = view.findViewById(R.id.municipalityTitle);
        municipalityTitle.setText(municipalityName + " väestönkehitys:");


        if (municipalityName != null && !municipalityName.isEmpty()) {
            fetchPopulationData(municipalityName);
        } else {
            txtmunicipalityName.setText("Kunnan nimeä ei annettu.");
        }
    }

    private void fetchPopulationData(String location) {
        MunicipalityDataRetriever retriever = new MunicipalityDataRetriever();
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            ArrayList<MunicipalityData> dataList = retriever.getData(requireContext(), location);

            requireActivity().runOnUiThread(() -> {
                if (dataList != null && !dataList.isEmpty()) {
                    ArrayList<BarEntry> entries = new ArrayList<>();
                    ArrayList<String> yearLabels = new ArrayList<>();

                    StringBuilder sb = new StringBuilder();
                    int index = 0;
                    for (MunicipalityData data : dataList) {
                        int year = data.getYear();
                        int population = data.getPopulation();

                        if (year >= 2020 && year <= 2023) {
                            entries.add(new BarEntry(index, population));
                            yearLabels.add(String.valueOf(year));
                            sb.append("Vuosi: ").append(year)
                                    .append("\n")
                                    .append("Väestönmuutos: ").append(data.getPopulationChange()).append(" henkilöä")
                                    .append("\n\n");
                            index++;
                        }
                    }

                    txtmunicipalityName.setText(sb.toString());

                    BarDataSet dataSet = new BarDataSet(entries, "Väkiluku");
                    dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                    dataSet.setValueTextColor(Color.BLACK);
                    dataSet.setValueTextSize(12f);

                    BarData barData = new BarData(dataSet);
                    barChart.setData(barData);

                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(yearLabels));
                    xAxis.setGranularity(1f);
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setTextColor(Color.BLACK);
                    xAxis.setTextSize(12f);

                    barChart.getAxisLeft().setTextColor(Color.BLACK);
                    barChart.getAxisRight().setEnabled(false);
                    barChart.getDescription().setEnabled(false);

                    barChart.animateY(1500);
                    barChart.invalidate();
                } else {
                    txtmunicipalityName.setText("Ei löytynyt dataa kunnalle: " + location);
                }
            });
        });
    }

}