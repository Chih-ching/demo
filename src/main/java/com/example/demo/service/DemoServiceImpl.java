package com.example.demo.service;

import com.example.demo.dao.CollectionDAO;
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
import java.util.ArrayList;
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
    public void insertTable(String exchangeRatesInfo) {
        StepDto stepDto = new StepDto();
        Boolean success = false;
        try {
            ArrayList<Map<String, Object>> infoArr = new Gson().fromJson(exchangeRatesInfo, ArrayList.class);
            ArrayList<CollectionPo> CollectionArr = new ArrayList<>();
            for (Map<String, Object> info : infoArr) {
                CollectionPo po = new CollectionPo();
                Date date = new java.sql.Date(new SimpleDateFormat("yyyyMMdd").parse(info.get("Date").toString()).getTime());
                po.setDate(date);
                po.setUsd(info.get("USD/NTD").toString());
                CollectionArr.add(po);
            }
            collectionDAO.saveAll(CollectionArr);
        } catch (Exception e) {
            System.out.println("getJSONObject error");
        }
    }

}
