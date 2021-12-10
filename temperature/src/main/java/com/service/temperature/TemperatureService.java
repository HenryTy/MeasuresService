package com.service.temperature;

import com.service.temperature.types.DataRequest;
import com.service.temperature.types.Fault;
import com.service.temperature.types.TemperatureResponse;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TemperatureService {

    public TemperatureResponse getTemperatures(DataRequest dataRequest) throws Fault{
        XMLGregorianCalendar from = dataRequest.getFrom();
        XMLGregorianCalendar to = dataRequest.getTo();
        if(from.compare(to) > 0) {
            Fault fault = new Fault();
            fault.setCode(182);
            fault.setText("Invalid date range");
            throw fault;
        }
        List<XMLGregorianCalendar> dates = new ArrayList<>();
        List<Double> temperatures = new ArrayList<>();
        XMLGregorianCalendar currentDate = (XMLGregorianCalendar) from.clone();
        Duration minuteDuration = null;
        try {
            minuteDuration = DatatypeFactory.newInstance().newDuration("PT1M");
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        Random random = new Random();
        double minTemp = 16.0;
        double maxTemp = 24.0;
        while(currentDate.compare(to) <= 0) {
            dates.add(currentDate);
            double temp = minTemp + (maxTemp - minTemp) * random.nextDouble();
            temperatures.add(temp);
            currentDate = (XMLGregorianCalendar) currentDate.clone();
            currentDate.add(minuteDuration);
        }
        TemperatureResponse temperatureResponse = new TemperatureResponse();
        temperatureResponse.setDates(dates);
        temperatureResponse.setTemperatures(temperatures);
        return temperatureResponse;
    }
}
