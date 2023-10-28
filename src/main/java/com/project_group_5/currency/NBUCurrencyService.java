package com.project_group_5.currency;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class NBUCurrencyService implements CurrencyServiceNBU {
    @Override
    public double getRateNBU(CurrencyNBU currencyNBU) {
        String urlNBU = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json";

        //Get JSON
        String json;
        try {
            json = Jsoup
                    .connect(urlNBU)
                    .ignoreContentType(true)
                    .get()
                    .body()
                    .text();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Can't connect to Mono");
        }

        //Convert json => Java Object ТИГ
        Type typeToken = TypeToken
                .getParameterized(List.class, CurrencyItemNBU.class)
                .getType();
        List<CurrencyItemNBU> currencyItemsNBU = new Gson().fromJson(json, typeToken);

        return currencyItemsNBU.stream()
                .filter(it -> it.getCc() == currencyNBU)
                .map(CurrencyItemNBU::getRate)
                .findFirst()
                .orElseThrow();

    }
}

