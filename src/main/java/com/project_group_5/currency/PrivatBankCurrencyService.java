package com.project_group_5.currency;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class PrivatBankCurrencyService implements CurrencyServicePrivate {

    @Override
    public double getRatePrivate(CurrencyPrivate currencyPrivate) {
        String urlPrivate = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";

        //Get JSON
        String json;
        try {
            json = Jsoup
                    .connect(urlPrivate)
                    .ignoreContentType(true)
                    .get()
                    .body()
                    .text();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Can't connect to Privat API");
        }

        //Convert json => Java Object Priavat
        Type typeToken = TypeToken
                .getParameterized(List.class, CurrencyItemPrivate.class)
                .getType();
        List<CurrencyItemPrivate> currencyItems = new Gson().fromJson(json, typeToken);

        return currencyItems.stream()
                .filter(it -> it.getCcy() == currencyPrivate)
                .filter(it -> it.getBase_ccy() == CurrencyPrivate.UAH)
                .map(CurrencyItemPrivate::getBuy)
                .findFirst()
                .orElseThrow();
    }
}
