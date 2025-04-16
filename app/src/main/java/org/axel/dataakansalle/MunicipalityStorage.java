package org.axel.dataakansalle;

import java.util.ArrayList;

public class MunicipalityStorage {
    private static MunicipalityStorage municipalityStorage = null;
    private static ArrayList<Municipality> municipalities = new ArrayList<>();

   public static MunicipalityStorage getInstance(){
       if (municipalityStorage == null){
           municipalityStorage = new MunicipalityStorage();
       }
       return municipalityStorage;
   }

    public static ArrayList<Municipality> getMunicipalities() {
        return municipalities;
    }

    public void addMunicipality(Municipality municipality) {
        for (Municipality m : municipalities) {
            if (m.getMunicipality().equalsIgnoreCase(municipality.getMunicipality())) {
                return;
            }
        }
        municipalities.add(municipality);
    }
}
