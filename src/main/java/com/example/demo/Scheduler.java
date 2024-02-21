package com.example.demo;

import com.example.demo.dto.StepDto;
import com.example.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    @Autowired
    private DemoService demoService;

    @EventListener(ApplicationReadyEvent.class)
    @Scheduled(cron = "0 0 18 * * *")
    public void getForeignExchangeInfo() {
        StepDto stepInfo=new StepDto();
        stepInfo.setSuccess(true);

        if(stepInfo.getSuccess()){
            stepInfo=demoService.getExchangeRates();
        }
        if(stepInfo.getSuccess()){
            demoService.insertTable(stepInfo.getData().toString());
        }

    }
}

//        提供一批次每日 18:00 呼叫 API，取得外匯成交資料，
//        並將每日的美元/台幣欄位(USD/NTD)資料與日期(yyyy-MM-dd HH:mm:ss) insert 至 table/collection，
//        並針對批次功能寫 Unit test。
//        API URL：https://openapi.taifex.com.tw/v1/DailyForeignExchangeRates
//        API Method：GET