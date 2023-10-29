package com.project_group_5.currencyUi;

import com.project_group_5.currency.CurrencyMono;
import com.project_group_5.currency.CurrencyNBU;
import com.project_group_5.currency.CurrencyPrivate;

public class ShowCurr {
    public String convertPrivate(double rate, int n, CurrencyPrivate currencyPrivate){
        String currText = "Курс ПриватБанку ${currency} до UAH = ${rate}";

        double scale = Math.pow(10, n);
        double roundedResult = Math.floor(rate * scale) / scale;

        return currText
                .replace("${currency}", currencyPrivate.name())
                .replace("${rate}",String.valueOf(roundedResult));
    }
    public String convertMono(double rate, int n, CurrencyMono currencyMono){
        String currText = "Курс Монобанка ${currency} до UAH = ${rate}";

        double scale = Math.pow(10, n);
        double roundedResult = Math.floor(rate * scale) / scale;

        return currText
                .replace("${currency}",currencyMono.name())
                .replace("${rate}",String.valueOf(roundedResult));
    }
    public String convertNBU(double rate, int n, CurrencyNBU currencyNBU){
        String currText = "Курс НБУ ${currency} до UAH = ${rate}";

        double scale = Math.pow(10, n);
        double roundedResult = Math.floor(rate * scale) / scale;

        return currText
                .replace("${currency}",currencyNBU.name())
                .replace("${rate}",String.valueOf(roundedResult));
    }
}
