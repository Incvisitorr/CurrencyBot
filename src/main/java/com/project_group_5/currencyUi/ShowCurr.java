package com.project_group_5.currencyUi;

import com.project_group_5.currency.CurrencyMono;
import com.project_group_5.currency.CurrencyNBU;
import com.project_group_5.currency.CurrencyPrivate;

public class ShowCurr {
    int n; //Количество знаков после запятой
    public String convertPrivate(double rate, CurrencyPrivate currencyPrivate){
        String currText="Курс ПриватБанка ${currency} к  UAH = ${rate}";
        //Количество знаков после запятой
        n=4;
        double scale = Math.pow(10, n);
        double roundedResult = Math.floor(rate * scale) / scale;

        return currText
                .replace("${currency}", currencyPrivate.name())
                .replace("${rate}",roundedResult+"");
    }
    public String convertMono(double rate, CurrencyMono currencyMono){
        String currText="Курс Монобанка ${currency} к  UAH = ${rate}";
        //Количество знаков после запятой
        n=4;
        double scale = Math.pow(10, n);
        double roundedResult = Math.floor(rate * scale) / scale;

        return currText
                .replace("${currency}",currencyMono.name())
                .replace("${rate}",roundedResult+"");
    }
    public String convertNBU(double rate, CurrencyNBU currencyNBU){
        String currText="Курс НБУ ${currency} к  UAH = ${rate}";
        //Количество знаков после запятой
        n=4;
        double scale = Math.pow(10, n);
        double roundedResult = Math.floor(rate * scale) / scale;

        return currText
                .replace("${currency}",currencyNBU.name())
                .replace("${rate}",roundedResult+"");
    }

}
