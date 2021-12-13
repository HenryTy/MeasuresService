package com.service.power.model;

import java.time.LocalDateTime;
import java.util.List;

public class PowerResponse {

    private List<LocalDateTime> dates;
    private List<Double> powers;

    public List<LocalDateTime> getDates() {
        return dates;
    }

    public void setDates(List<LocalDateTime> dates) {
        this.dates = dates;
    }

    public List<Double> getPowers() {
        return powers;
    }

    public void setPowers(List<Double> powers) {
        this.powers = powers;
    }
}
