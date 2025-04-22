package org.axel.dataakansalle;

import android.content.Context;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class JobDataRetriever {

    public ArrayList<JobData> getData(Context context, String municipality){

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode areas = objectMapper.readTree(new URL("https://pxdata.stat.fi:443/PxWeb/api/v1/fi/StatFin/tyokay/statfin_tyokay_pxt_125s.px"));

            ArrayList<String> keys = new ArrayList<>();
            ArrayList<String> values = new ArrayList<>();

            for (JsonNode node : areas.get("variables").get(0).get("values")) {
                values.add(node.asText());
            }
            for (JsonNode node : areas.get("variables").get(0).get("valueTexts")) {
                keys.add(node.asText());
            }

            HashMap<String, String> municipalityCodes = new HashMap<>();
            for (int i = 0; i < keys.size(); i++) {
                municipalityCodes.put(keys.get(i), values.get(i));
            }

            String code = municipalityCodes.get(municipality);
            if (code == null) return null;
            URL url = new URL("https://pxdata.stat.fi:443/PxWeb/api/v1/fi/StatFin/tyokay/statfin_tyokay_pxt_125s.px");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            JsonNode jsonInputString = objectMapper.readTree(context.getResources().openRawResource(R.raw.query3));
            ((ObjectNode) jsonInputString.get("query").get(0).get("selection")).putArray("values").add(code);
            byte[] input = objectMapper.writeValueAsBytes(jsonInputString);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(input, 0, input.length);

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                response.append(line.trim());
            }

            JsonNode sufficiencyData = objectMapper.readTree(response.toString());


            ArrayList<String> years = new ArrayList<>();
            ArrayList<Integer> sufficiencyRate = new ArrayList<>();

            for (JsonNode node : sufficiencyData.get("dimension").get("Vuosi").get("category").get("label")) {
                years.add(node.asText());
            }

            JsonNode valuesNode = sufficiencyData.get("value");
            for (int i = 0; i < valuesNode.size(); i++) {
                sufficiencyRate.add(valuesNode.get(i).asInt());
            }
            ArrayList<JobData> sufficiencyDataList = new ArrayList<>();
            for (int i = 0; i < years.size(); i++) {
                sufficiencyDataList.add(new JobData(Integer.valueOf(years.get(i)), Float.valueOf(sufficiencyRate.get(i))));
            }
            return sufficiencyDataList;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
