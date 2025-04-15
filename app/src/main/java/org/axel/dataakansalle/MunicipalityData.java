package org.axel.dataakansalle;

import java.io.Serializable;

public class MunicipalityData implements Serializable {

    private int year;
    private int population;

    public MunicipalityData(int year, int population ){
        this.year = year;
        this.population = population;

    }

    public int getYear() {
        return year;
    }

    public int getPopulation() {
        return population;
    }

    public void setYear(int year) {
        this.year = year;
    }
    public void setPopulation(int population){
        this.population = population;
    }


}
