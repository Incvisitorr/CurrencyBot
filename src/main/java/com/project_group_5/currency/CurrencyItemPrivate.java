package com.project_group_5.currency;
import lombok.Data;

@Data
    public class CurrencyItemPrivate {
        private CurrencyPrivate ccy;
        private CurrencyPrivate base_ccy;
        private float buy;
        private float sale;
    }


