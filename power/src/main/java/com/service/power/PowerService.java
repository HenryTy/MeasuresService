package com.service.power;

import com.service.power.model.PowerRequest;
import com.service.power.model.PowerResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
public class PowerService {

    public PowerResponse getPowers(@RequestBody PowerRequest powerRequest){
        LocalDateTime from = powerRequest.getFrom();
        LocalDateTime to = powerRequest.getTo();
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
