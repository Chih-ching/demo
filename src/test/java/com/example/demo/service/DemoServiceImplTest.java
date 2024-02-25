package com.example.demo.service;

import com.example.demo.dto.StepDto;
import com.example.demo.po.CollectionPo;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
class DemoServiceImplTest {

    @Autowired
    private DemoService demoService;

    @Test
    void getExchangeRatesTest(){
        StepDto actualStepDto = demoService.getExchangeRates();

        assertEquals(true, actualStepDto.getSuccess());
        assertTrue(!actualStepDto.getData().toString().isEmpty());
    }

    @Test
    void parseExchangeRatesInfoTest() {
        String str="[{\"Date\":\"20240102\",\"USD/NTD\":\"30.866\"},{\"Date\":\"20240103\",\"USD/NTD\":\"31.01\"}]";
        StepDto actualStepDto = demoService.parseExchangeRatesInfo(str);

        ArrayList<CollectionPo> data=(ArrayList<CollectionPo>)actualStepDto.getData();
        Integer dataCount=data.size();

        String countTarget = "Date";
        Integer count=(str.length()-str.replace(countTarget,"").length())/countTarget.length();

        assertEquals(true, actualStepDto.getSuccess());
        assertEquals(count,dataCount);
    }

    @Test
    void dateChk() {
        String startDate="2024/01/01";
        String endDate="2024/01/05";
        StepDto actualStepDto = demoService.dateChk(startDate,endDate);

        assertEquals(true, actualStepDto.getSuccess());
    }

}