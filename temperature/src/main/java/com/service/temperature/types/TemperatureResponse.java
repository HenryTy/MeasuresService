package com.service.temperature.types;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;

public class TemperatureResponse {

    protected List<XMLGregorianCalendar> dates;
    protected List<Double> temperatures;

    public List<XMLGregorianCalendar> getDates() {
        return dates;
    }

    public void setDates(List<XMLGregorianCalendar> dates) {
        this.dates = dates;
    }

    public List<Double> getTemperatures() {
        return temperatures;
    }

    public void setTemperatures(List<Double> temperatures) {
        this.temperatures = temperatures;
    }
}
