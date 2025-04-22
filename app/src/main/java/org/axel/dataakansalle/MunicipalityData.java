package org.axel.dataakansalle;

import java.io.Serializable;

public class MunicipalityData implements Serializable {

    private int year;
    private int population;
    private int populationChange;

    public MunicipalityData(int year, int population, int populationChange){
        this.year = year;
        this.population = population;
        this.populationChange = populationChange;

    }

    public int getYear() {
        return year;
    }

    public int getPopulation() {
        return population;
    }
    public int getPopulationChange() {
        return populationChange;
    }

    public void setYear(int year) {
        this.year = year;
    }
    public void setPopulation(int population){
        this.population = population;
    }
    public void setPopulationChange(int populationChange) {
        this.populationChange = populationChange;
    }


}
