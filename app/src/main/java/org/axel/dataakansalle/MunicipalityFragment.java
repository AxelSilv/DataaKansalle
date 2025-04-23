package org.axel.dataakansalle;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MunicipalityFragment extends Fragment {

    private TextView municipalityName;
    private TextView employmentRate;
    private TextView jobSelfSufficiency;

    private static final String ARG_MUNICIPALITY = "municipality";
    private String municipality;

    public static MunicipalityFragment newInstance(String municipality) {
        MunicipalityFragment fragment = new MunicipalityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MUNICIPALITY, municipality);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_municipality, container, false);

        municipalityName = view.findViewById(R.id.municipalityName);
        employmentRate = view.findViewById(R.id.employmentRate);
        jobSelfSufficiency = view.findViewById(R.id.jobSelfSufficiency);

        if (getArguments() != null) {
            municipality = getArguments().getString(ARG_MUNICIPALITY);
            municipalityName.setText(municipality);
            fetchEmploymentData();
            fetchJobData();
        }
        return view;
    }
    private void fetchJobData(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            JobDataRetriever retriever = new JobDataRetriever();
            List<JobData> data = retriever.getData(requireContext(), municipality);
            if(data != null && getActivity() != null){
               JobData latest = data.get(data.size() -1);
               String text = String.format("Työpaikkaomavaraisuus (%d): %.1f %%", latest.getYear(), latest.getEmploymentSufficiency());
                requireActivity().runOnUiThread(() -> {
                    jobSelfSufficiency.setText(text);
                });
            }
        });
    }
    private void fetchEmploymentData() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            EmploymentRetriever retriever = new EmploymentRetriever();
            List<EmploymentData> data = retriever.getData(requireContext(), municipality);

            if (data != null && getActivity() != null) {
                EmploymentData latest = data.get(data.size() - 1);
                String text = String.format("Työllisyysaste (%d): %.1f %%",
                        latest.getYear(), latest.getEmployment());

                requireActivity().runOnUiThread(() -> {
                    employmentRate.setText(text);
                });
            }
        });
    }
}