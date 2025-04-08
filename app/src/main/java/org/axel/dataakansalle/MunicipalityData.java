package org.axel.dataakansalle;

import java.io.Serializable;

public class MunicipalityData implements Serializable {

    private int year;
    private int population;
    private String name;
    private double employmentRate;

    public MunicipalityData(int year, int population, String name, int employmentRate){
        this.year = year;
        this.population = population;
        this.name = name;
        this.employmentRate = employmentRate;
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

    public String getMunicipality() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
