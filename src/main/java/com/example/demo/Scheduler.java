package com.example.demo;

import com.example.demo.dto.StepDto;
import com.example.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Profile("scheduling")
public class Scheduler {

    @Autowired
    private DemoService demoService;

    @EventListener(ApplicationReadyEvent.class)
    @Scheduled(cron = "0 0 18 * * *")
    public void getForeignExchangeInfo() {
        StepDto stepInfo=new StepDto();
        stepInfo.setSuccess(true);
        //讀取指定網址，並取得內容
        if(stepInfo.getSuccess()){
            stepInfo=demoService.getExchangeRates();
        }
        //內容整理後新增進資料庫
        if(stepInfo.getSuccess()){
            stepInfo=demoService.parseExchangeRatesInfo(stepInfo.getData().toString());
        }
        //內容整理後新增進資料庫
        if(stepInfo.getSuccess()){
            demoService.insertTable((ArrayList)stepInfo.getData());
        }
    }

}