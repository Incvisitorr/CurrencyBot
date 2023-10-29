package com.project_group_5;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project_group_5.Settings.Settings;
import com.project_group_5.currency.*;
import com.project_group_5.currencyUi.ShowCurr;
import com.project_group_5.telegram.TelegramBotService;
import org.jsoup.Jsoup;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class TelegramBotApp {
    public static void main(String[] args) {
        new TelegramBotService();
    }
}