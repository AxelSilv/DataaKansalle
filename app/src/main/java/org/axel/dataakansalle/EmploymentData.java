package org.axel.dataakansalle;

import java.io.Serializable;

public class EmploymentData implements Serializable {

    private int year;
    private double employment;

    public EmploymentData(int year, double employment){
        this.employment = employment;
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getEmployment() {
        return employment;
    }

    public void setEmployment(double employment) {
        this.employment = employment;
    }
}
