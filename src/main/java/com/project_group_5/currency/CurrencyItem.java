package com.project_group_5.currency;
import lombok.Data;

@Data
    public class CurrencyItem {

        private Currency ccy;
        private Currency base_ccy;
        private float buy;
        private float sale;
    }

