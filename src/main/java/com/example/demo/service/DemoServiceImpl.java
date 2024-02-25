package com.example.demo.service;

import com.example.demo.dao.CollectionDAO;
import com.example.demo.dto.CurrencyInfo;
import com.example.demo.dto.StepDto;
import com.example.demo.po.CollectionPo;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DemoServiceImpl implements DemoService {

    @Value("${spring.dailyForeignExchangeRates.url}")
    private String url;

    @Autowired
    private CollectionDAO collectionDAO;

    @Override
    public StepDto getExchangeRates() {
        StepDto stepDto = new StepDto();
        Boolean success = false;
        String result = null;
        try {
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLSocketFactory(new SSLConnectionSocketFactory(SSLContexts.custom()
                                    .loadTrustMaterial(null, new TrustAllStrategy())
                                    .build()
                            )
                    ).build();
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("accept", "application/json");
            httpGet.setHeader("If-Modified-Since", "Mon, 26 Jul 1997 05:00:00 GMT");
            httpGet.setHeader("Cache-Control", "no-cache");
            httpGet.setHeader("Pragma", "no-cache");

            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                result = EntityUtils.toString(resEntity);
                success = true;
            }
        } catch (Exception e) {
            System.out.println("getExchangeRates error");
        }
        stepDto.setSuccess(success);
        stepDto.setData(result);
        return stepDto;
    }

    @Override
    public StepDto parseExchangeRatesInfo(String exchangeRatesInfo) {
        StepDto stepDto = new StepDto();
        Boolean success = false;
        ArrayList<CollectionPo> CollectionArr = null;
        try {
            ArrayList<Map<String, Object>> infoArr = new Gson().fromJson(exchangeRatesInfo, ArrayList.class);
            CollectionArr = new ArrayList<>();
            for (Map<String, Object> info : infoArr) {
                CollectionPo po = new CollectionPo();
                Date date = new java.sql.Date(new SimpleDateFormat("yyyyMMdd").parse(info.get("Date").toString()).getTime());
                po.setDate(date);
                po.setUsd(info.get("USD/NTD").toString());
                CollectionArr.add(po);
            }
            // collectionDAO.saveAll(CollectionArr);
            success = true;
        } catch (Exception e) {
            System.out.println("getJSONObject error");
            System.out.println(e);
        }
        stepDto.setSuccess(success);
        stepDto.setData(CollectionArr);
        return stepDto;
    }

    @Override
    public void insertTable(ArrayList<CollectionPo> CollectionArr) {
        StepDto stepDto = new StepDto();
        Boolean success = false;
        try {
            collectionDAO.saveAll(CollectionArr);
            success = true;
        } catch (Exception e) {
            System.out.println("getJSONObject error");
            System.out.println(e);
        }
        stepDto.setSuccess(success);
        stepDto.setData(null);
    }

    @Override
    public StepDto dateChk(String start, String end) {
        StepDto stepDto = new StepDto();
        Boolean success = false;
        Boolean res = false;
        try {
            //日期範圍最小值
            LocalDate minDate = LocalDate.now().minusYears(1L);
            minDate = minDate.minusDays(1L);
            //日期範圍最大值
            LocalDate maxDate = LocalDate.now();
            //input日期開始日
            LocalDate inputStartDate = LocalDate.parse(start, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            //input日期結束日
            LocalDate inputEndDate = LocalDate.parse(end, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            //確認input日期是否有在日期範圍內
            if (minDate.isBefore(inputStartDate) && maxDate.isAfter(inputEndDate) && inputStartDate.isBefore(inputEndDate)) {
                res = true;
            }
            success = true;
        } catch (Exception e) {
            System.out.println("dateChk error");
            System.out.println(e);
        }
        stepDto.setSuccess(success);
        stepDto.setData(res);
        return stepDto;
    }

    @Override
    public StepDto queryForeignExchangeRates(String start, String end, String currency) {
        StepDto stepDto = new StepDto();
        Boolean success = false;
        try {
            List<Map<String, Object>> infoList = collectionDAO.dateRangeQuery(start, end);
            ArrayList<CurrencyInfo> currencyList = new ArrayList<>();
            for (Map<String, Object> info : infoList) {
                CurrencyInfo currencyInfo = new CurrencyInfo();
                currencyInfo.setDate(info.get("date").toString());
                currencyInfo.setUsd(info.get(currency) == null ? "" : info.get(currency).toString());
                currencyList.add(currencyInfo);
            }
            stepDto.setData(currencyList);
            success = true;
        } catch (Exception e) {
            System.out.println("queryForeignExchangeRates error");
            System.out.println(e);
        }
        stepDto.setSuccess(success);
        return stepDto;
    }

}
