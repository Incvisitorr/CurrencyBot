package com.project_group_5.currencyUi;

import com.project_group_5.currency.Currency;

public class ShowCurr {
    int n; //Количество знаков после запятой
    public String convert(double rate, Currency currency){
        String currText="Курс ${currency} к  UAH = ${rate}";
        //Количество знаков после запятой
        n=4;
        double scale = Math.pow(10, n);
        double roundedResult = Math.floor(rate * scale) / scale;

        return currText
                .replace("${currency}",currency.name())
                .replace("${rate}",roundedResult+"");
    }
}
