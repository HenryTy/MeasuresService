package com.service.power;

import com.service.power.model.PowerRequest;
import com.service.power.model.PowerResponse;
import com.service.power.model.RequestException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
public class PowerService {

    private int queriesLimit = 100;

    @PostMapping("/service/power")
    public PowerResponse getPowers(@RequestBody PowerRequest powerRequest){
        if(queriesLimit == 0) {
            throw new RequestException("Your query limit has been exceeded");
        }
        if(powerRequest.getRoomNr() > 10 || powerRequest.getRoomNr() < 1) {
            throw new RequestException("Room Number must be value from 1 to 10");
        }
        LocalDateTime from = powerRequest.getFrom();
        LocalDateTime to = powerRequest.getTo();
        List<LocalDateTime> dates = new ArrayList<>();
        List<Double> powers = new ArrayList<>();
        LocalDateTime currentDate = from;
        Random random = new Random();
        double minPower = 800;
        double maxPower = 1500;
        while(currentDate.compareTo(to) <= 0) {
            dates.add(currentDate);
            double temp = minPower + (maxPower - minPower) * random.nextDouble();
            powers.add(temp);
            currentDate = currentDate.plusMinutes(1);
        }
        PowerResponse powerResponse = new PowerResponse();
        powerResponse.setDates(dates);
        powerResponse.setPowers(powers);
        queriesLimit--;
        return powerResponse;
    }

    @GetMapping("/service/limit")
    public Integer getQueriesLimit() {
        return queriesLimit;
    }

    @PostMapping("/service/cancel")
    public String cancelQuery() {
        queriesLimit++;
        return "Query cancelled";
    }
}
