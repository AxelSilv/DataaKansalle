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
    public MunicipalityFragment() {}

    private static final String ARG_MUNICIPALITY = "municipality";
    private String municipality;


    public static MunicipalityFragment newInstance(String municipality) {
        MunicipalityFragment fragment = new MunicipalityFragment();
        Bundle args = new Bundle();
        args.putString("municipality", municipality);
        fragment.setArguments(args);
        return fragment;
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
            String capital1 = capitalizeFirstLetter(m1);
            String capital2 = capitalizeFirstLetter(m2);
            MunicipalityDataRetriever municipalityDataRetriever = new MunicipalityDataRetriever();
            EmploymentRetriever employmentRetriever = new EmploymentRetriever();
            JobDataRetriever jobDataRetriever = new JobDataRetriever();

            List<MunicipalityData> data1 = municipalityDataRetriever.getData(requireContext(), capital1);
            List<MunicipalityData> data2 = municipalityDataRetriever.getData(requireContext(), capital2);

            List<EmploymentData> emp1 = employmentRetriever.getData(requireContext(), capital1);
            List<EmploymentData> emp2 = employmentRetriever.getData(requireContext(), capital2);

            List<JobData> job1 = jobDataRetriever.getData(requireContext(), capital1);
            List<JobData> job2 = jobDataRetriever.getData(requireContext(), capital2);

            requireActivity().runOnUiThread(() -> {
                txtResult1.setText(formatMunicipalityInfo(data1, emp1, job1, m1));
                txtResult2.setText(formatMunicipalityInfo(data2, emp2, job2, m2));
            });
        });
    }
    private String formatMunicipalityInfo(List<MunicipalityData> popData, List<EmploymentData> empData, List<JobData> jobData, String name) {
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

        for (EmploymentData e : empData) {
            if (e.getYear() == 2023) {
                sb.append("Työllisyysaste: ").append(String.format("%.2f", e.getEmployment())).append(" %\n");
                foundData = true;
            }
        }

        for (JobData j : jobData) {
            if (j.getYear() == 2023) {
                sb.append("Työpaikkaomavaraisuus: ").append(j.getEmploymentSufficiency()).append(" %\n");
                foundData = true;
            }
        }

        if (!foundData) {
            sb.append("Ei saatavilla olevaa dataa vuodelle 2023.");
        }

        return sb.toString();
    }
    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) return "";
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}