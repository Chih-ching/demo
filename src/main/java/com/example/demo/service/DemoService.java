package com.example.demo.service;

import com.example.demo.dto.StepDto;
import com.example.demo.po.CollectionPo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public interface DemoService {
    StepDto getExchangeRates();
    StepDto parseExchangeRatesInfo(String toString);
    void insertTable(ArrayList<CollectionPo> collectionPoArr);

    StepDto dateChk(String start, String end);
    StepDto queryForeignExchangeRates(String currency,String start,String end);

}

