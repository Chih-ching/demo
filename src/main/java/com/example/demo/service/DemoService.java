package com.example.demo.service;

import com.example.demo.dto.StepDto;
import org.springframework.stereotype.Service;

@Service
public interface DemoService {
    StepDto getExchangeRates();
    StepDto insertTable(String exchangeRatesInfo);

    StepDto dateChk(String start, String end);
    StepDto queryForeignExchangeRates(String currency,String start,String end);
}

