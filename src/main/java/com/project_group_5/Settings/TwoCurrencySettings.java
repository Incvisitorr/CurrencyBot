package com.project_group_5.Settings;

import com.project_group_5.currency.*;
import com.project_group_5.currencyUi.ShowCurr;

import java.io.*;
import java.util.*;

import static com.project_group_5.Settings.Settings.isSettingsFile;
import static com.project_group_5.Settings.Settings.makeMap;
import static com.project_group_5.Settings.TwoCurrencySettings.removeOneCurrency;

public class TwoCurrencySettings {

    //        перевіряємо чи у встановлених налаштуваннях є така ж валюта і додаємо ще одну
    public static void setSecondCurrency(long chatId, String value) throws IOException {
        Map<String, String> settings = makeMap(chatId);
        if (settings.containsKey("Currency")){
                settings.put("Currency1", value);
            }else settings.put("Currency", value);
        new File("chatId" + chatId + "Settings").delete();
            BufferedWriter writer = new BufferedWriter(new FileWriter("chatId" + chatId + "Settings"));
            writer.write(settings.toString());
            writer.close();
    }

    public static boolean isSettedTwoCurrencies(long chatId) throws FileNotFoundException {
        boolean checked = false;
        Map<String, String> settings = makeMap(chatId);
        if (settings.containsKey("Currency") && settings.containsKey("Currency1")){
            checked = true;
        }
        return checked;
    }

    public static boolean isSettedCurrency(long chatId, String currency) throws FileNotFoundException {
        Map<String, String> map = makeMap(chatId);
        boolean checked = false;
        if (map.containsKey("Currency")){
            if (map.get("Currency").equals(currency)){
                checked = true;
            }
        }else if (map.get("Currency1").equals(currency)){
            checked = true;
        }
        return checked;
    }

    public String test(long chatId) throws IOException {
        Settings settings = new Settings();
        String text = "";
        if (!isSettingsFile(chatId)) {
            settings.setSettings(chatId, "Currency", "USD");
            text = "Added USD";
        }else
        if (isSettingsFile(chatId) && !isSettedTwoCurrencies(chatId)) {
            if (isSettedCurrency(chatId, "USD")){
                text = "USD is been setted before";
            }else{
                setSecondCurrency(chatId, "USD");
                text = "Second currency added: USD";}
        }else
        if (isSettedTwoCurrencies(chatId)) {
            removeOneCurrency(chatId, "USD");
            text = "One currency left: EUR";
        }
//            if (!isSettingsFile(chatId)) {
//                settings.setSettings(chatId, "Currency", "EUR");
//                text = "Euro setted";
//            } else if (isSettingsFile(chatId) && !isSettedTwoCurrencies(chatId)) {
//                if (isSettedCurrency(chatId, "EUR")) {
//                    text = "EUR already been setted";
//                } else {
//                    setSecondCurrency(chatId, "EUR");
//                    text = "Second currency edded: EUR";
//                }
//            } else if (isSettedTwoCurrencies(chatId)) {
//                removeOneCurrency(chatId, "EUR");
//                text = "One currency left: USD";
//            }
            return text;
        }

    // забираємо одну з валют за вибором користувача
    public static void removeOneCurrency(long chatId, String value) throws IOException {
        Map<String, String> settings = makeMap(chatId);
        if (settings.get("Currency").equals(value)) {
            settings.remove("Currency", value);
        }else
        if (settings.get("Currency1").equals(value)) {
            settings.remove("Currency1", value);
        }
        new File("chatId" + chatId + "Settings").delete();
        BufferedWriter writer = new BufferedWriter(new FileWriter("chatId" + chatId + "Settings"));
        writer.write(settings.toString());
        writer.close();
    }

    //надаємо інформацію якщо встановлено дві валюти
    public static String showTwoCurrenciesResult(long chatId) throws FileNotFoundException {
        ShowCurr showCurr = new ShowCurr();
        String firstCurrencyResult = Settings.implementOneCurrencySettings(chatId);
        Map<String, String> settingsMap = makeMap(chatId);
        String result = "";

            if (settingsMap.get("Bank").equals("privat")) {
                PrivatBankCurrencyService privatBankCurrencyService = new PrivatBankCurrencyService();
                double rate = privatBankCurrencyService
                        .getRatePrivate(CurrencyPrivate.valueOf(settingsMap.get("Currency1")));
                result = firstCurrencyResult + System.lineSeparator() +
                        showCurr.convertPrivate(rate, Integer.parseInt(settingsMap.get("Number_of_signs")),
                        CurrencyPrivate.valueOf(settingsMap.get("Currency1")));

            }
            if (settingsMap.get("Bank").equals("mono")) {
                MonoBankCurrencyService monoBankCurrencyService = new MonoBankCurrencyService();
                double rate = monoBankCurrencyService
                        .getRateMono(Settings.currencyMonoHandler(settingsMap.get("Currency1")));
                result = firstCurrencyResult + System.lineSeparator() +
                        showCurr.convertMono(rate, Integer.parseInt(settingsMap.get("Number_of_signs")),
                        Settings.currencyMonoHandler(settingsMap.get("Currency1")));

            }
            if (settingsMap.get("Bank").equals("nbu")) {
                NBUCurrencyService nbuCurrencyService = new NBUCurrencyService();
                double rate = nbuCurrencyService
                        .getRateNBU(CurrencyNBU.valueOf(settingsMap.get("Currency1")));
                result = firstCurrencyResult + System.lineSeparator() +
                        showCurr.convertNBU(rate, Integer.parseInt(settingsMap.get("Number_of_signs")),
                        CurrencyNBU.valueOf(settingsMap.get("Currency1")));
            }
        return result;
        }
    }
