package com.example.demo.service;

import com.example.demo.dto.StepDto;
import org.springframework.stereotype.Service;

@Service
public interface DemoService {
    StepDto getExchangeRates();
    void insertTable(String exchangeRatesInfo);

}

