package org.axel.dataakansalle;

import java.io.Serializable;

public class JobData implements Serializable {
    private int year;
    private float employmentSufficiency;

    public JobData(int year, float employmentSufficiency) {
        this.year = year;
        this.employmentSufficiency = employmentSufficiency;

    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public float getEmploymentSufficiency() {
        return employmentSufficiency;
    }

    public void setEmploymentSufficiency(float employmentSufficiency) {
        this.employmentSufficiency = employmentSufficiency;
    }
}
