package com.project_group_5.currency;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class MonoBankCurrencyService implements CurrencyServiceMono {
    @Override
    public double getRateMono(CurrencyMono currencyMono) {
        String urlMono = "https://api.monobank.ua/bank/currency";

        //Get JSON
        String json;
        try {
            json = Jsoup
                    .connect(urlMono)
                    .ignoreContentType(true)
                    .get()
                    .body()
                    .text();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Can't connect to Mono");
        }

        //Convert json => Java Object MONO
        Type typeToken = TypeToken
                .getParameterized(List.class, CurrencyItemMono.class)
                .getType();
        List<CurrencyItemMono> currencyItemsMono = new Gson().fromJson(json, typeToken);

        return currencyItemsMono.stream()
                .filter(it -> it.getCurrencyCodeA() == currencyMono.currencyCode)
                .filter(it -> it.getCurrencyCodeB() == CurrencyMono.UAH.currencyCode)
                .map(CurrencyItemMono::getRateBuy)
                .findFirst()
                .orElseThrow();

    }
}
