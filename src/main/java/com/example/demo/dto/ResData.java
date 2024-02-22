package com.example.demo.dto;

import java.util.ArrayList;

public class ResData {

    private ErrInfo error;
    private ArrayList<CurrencyInfo> currency;

    public ErrInfo getErrInfo() {
        return error;
    }

    public void setErrInfo(ErrInfo errInfo) {
        this.error = errInfo;
    }

    public ArrayList<CurrencyInfo> getCurrencyArr() {
        return currency;
    }

    public void setCurrencyArr(ArrayList<CurrencyInfo> currencyArr) {
        currency = currencyArr;
    }
}
