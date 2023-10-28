package com.project_group_5.currency;

public enum CurrencyMono {
    USD(840),
    EUR(978),
    UAH(980);

    public final int currencyCode;

    CurrencyMono(int currencyCode) {
        this.currencyCode = currencyCode;
    }
}
