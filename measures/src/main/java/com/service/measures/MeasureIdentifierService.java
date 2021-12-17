package com.service.measures;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class MeasureIdentifierService {

    public String getMeasureIdentifier() {
        return UUID.randomUUID().toString();
    }

}