package org.axel.dataakansalle;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class EmploymentRetriever {
    public ArrayList<EmploymentData> getData(Context context, String municipality){
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode areas = null;

        if (areas == null){
            try {
                areas= objectMapper.readTree(new URL("https://pxdata.stat.fi:443/PxWeb/api/v1/fi/StatFin/tyokay/statfin_tyokay_pxt_115x.px"));
                if(areas == null){
                    Log.e("LUT", "Failed to retrieve any data!!");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            ArrayList<String> keys = new ArrayList<>();
            ArrayList<String> values = new ArrayList<>();

            for (JsonNode node : areas.get("variables").get(0).get("values")) {
                values.add(node.asText());
            }
            for (JsonNode node : areas.get("variables").get(0).get("valueTexts")) {
                keys.add(node.asText());
            }


            HashMap<String, String> municipalityCodes = new HashMap<>();
            for(int i = 0; i < keys.size(); i++) {
                municipalityCodes.put(keys.get(i), values.get(i));
            }
            String code = null;
            code = municipalityCodes.get(municipality);

            try {
                URL url = new URL("https://pxdata.stat.fi:443/PxWeb/api/v1/fi/StatFin/tyokay/statfin_tyokay_pxt_115x.px");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);

                JsonNode jsonNode = objectMapper.readTree(context.getResources().openRawResource(R.raw.query2));
                ((ObjectNode) jsonNode.get("query").get(0).get("selection")).putArray("values").add(code);

                byte [] input = objectMapper.writeValueAsBytes(jsonNode);
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(input, 0, input.length);

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                StringBuilder response = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }
                JsonNode employmentData = objectMapper.readTree(response.toString());
                ArrayList<String> years = new ArrayList<>();
                ArrayList<String> employment = new ArrayList<>();

                for (JsonNode node : employmentData.get("dimension").get("Vuosi").get("category").get("label")) {
                    years.add(node.asText());
                }

                for (JsonNode node : employmentData.get("value")) {
                    employment.add(node.asText());
                }
                ArrayList<EmploymentData> employmentDataList = new ArrayList<>();

                for (int i = 0; i < years.size(); i++) {
                    employmentDataList.add(new EmploymentData(Integer.valueOf(years.get(i)), Float.valueOf(employment.get(i))));
                }

                return employmentDataList;

            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        return null;
    }
}
