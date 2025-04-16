package org.axel.dataakansalle;

import java.io.Serializable;

public class Municipality implements Serializable {
    private String municipality;
    public Municipality(String municipality){
        this.municipality = municipality;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }
}
