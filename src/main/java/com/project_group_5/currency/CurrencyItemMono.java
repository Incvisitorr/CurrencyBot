package com.project_group_5.currency;
import lombok.Data;

@Data
public class CurrencyItemMono {
    private int currencyCodeA;
    private int currencyCodeB;
    private float rateBuy;
    private float rateSell;
}
