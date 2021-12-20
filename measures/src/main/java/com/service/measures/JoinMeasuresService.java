package com.service.measures;

import com.service.measures.power.PowerResponse;
import com.service.measures.temperature.TemperatureResponse;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Service
public class JoinMeasuresService {

    private HashMap<String, AllMeasures> idToMeasures;

    @PostConstruct
    void init() {
        idToMeasures = new HashMap<>();
    }

    public static class AllMeasures {
        TemperatureResponse temperatureResponse;
        PowerResponse powerResponse;
        public boolean isReady() {
            return temperatureResponse != null && powerResponse != null;
        }
    }

    public synchronized boolean addTemperatureResponse(String measuresId, TemperatureResponse temperatureResponse) {
        AllMeasures allMeasures = getMeasures(measuresId);
        allMeasures.temperatureResponse = temperatureResponse;
        return allMeasures.isReady();
    }

    public synchronized boolean addPowerResponse(String measuresId, PowerResponse powerResponse) {
        AllMeasures allMeasures = getMeasures(measuresId);
        allMeasures.powerResponse = powerResponse;
        return allMeasures.isReady();
    }

    public synchronized AllMeasures getMeasures(String measuresId) {
        AllMeasures allMeasures = idToMeasures.get(measuresId);
        if (allMeasures==null) {
            allMeasures = new AllMeasures();
            idToMeasures.put(measuresId, allMeasures);
        }
        return allMeasures;
    }
}
