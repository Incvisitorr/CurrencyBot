package com.project_group_5.Settings;

import com.project_group_5.currency.*;
import com.project_group_5.currencyUi.ShowCurr;

import java.io.*;
import java.util.*;

import static com.project_group_5.Settings.TwoCurrencySettings.*;


public class Settings {
    private static final Map<String, String> defoultMap = new HashMap<>();
    private static final Map<String, String> settingsMap = new HashMap<>();

    public static Map<String, String> getDefoultMap() {
        defoultMap.put("Bank", "privat");
        defoultMap.put("Currency", "USD");
        defoultMap.put("Number_of_signs", Integer.toString(2));
        return defoultMap;
    }

    public static Map<String, String> makeMap(long chatId) throws FileNotFoundException {
        File file = new File("chatId" + chatId + "Settings");
        FileReader reader = new FileReader(file);
        Scanner scanner = new Scanner(reader);
        String[] row = scanner.nextLine()
                .replace("{", "").replace("}", "").split(", ");
        for (String s : row) {
            String[] keyValue = s.split("=");
            settingsMap.put(keyValue[0], keyValue[1]);
        }
        return settingsMap;
    }

    public static boolean isSettingsFile(long chatId) {
        boolean checked = false;
        String filePath = new File("CurrencyBot").getAbsolutePath()
                .replace("CurrencyBot" + File.separator + "CurrencyBot", "CurrencyBot");
        File f1 = new File(filePath);
        List<String> list = List.of(Objects.requireNonNull(f1.list((dir, name) -> name.contains(String.valueOf(chatId)))));
        if (!list.isEmpty()) {
            return true;
        }
        return checked;
    }

    public void setSettings(long chatId, String key, String value) throws IOException {
        Map<String, String> settings;
        if (isSettingsFile(chatId)) {
            settings = makeMap(chatId);
            settings.replace(key, value);
            new File("chatId" + chatId + "Settings").delete();
        } else
            settings = new HashMap<>(getDefoultMap());
        settings.replace(key, value);

        BufferedWriter writer = new BufferedWriter(new FileWriter("chatId" + chatId + "Settings"));
        writer.write(settings.toString());
        writer.close();
    }

    public static String implementSettings(long chatId) throws IOException {
        String result;
        if (!isSettingsFile(chatId) || !isSettedTwoCurrencies(chatId)) {
            renameCurrency(chatId);
            result = implementOneCurrencySettings(chatId);
        } else {
            makeDifferentCurrencies(chatId);
            result = showTwoCurrenciesResult(chatId);
        }
        return result;
    }

    public static void makeDifferentCurrencies(long chatId) throws IOException {
        Map<String, String> map = makeMap(chatId);
        if (map.get("Currency").equals(map.get("Currency1"))){
            if (map.get("Currency").equals("USD")){
                map.replace("Currency1", "USD", "EUR");
            }else map.replace("Currency1", "EUR", "USD");
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter("chatId" + chatId + "Settings"));
        writer.write(map.toString());
        writer.close();
    }

    public static void renameCurrency(long chatId) throws FileNotFoundException {
        Map<String, String> settings = makeMap(chatId);
        if (settings.containsKey("Currency1")) {
            String value = settings.get("Currency1");
            settings.put("Currency", value);
        }
    }

    public static String implementOneCurrencySettings(long chatId) throws FileNotFoundException {
        ShowCurr showCurr = new ShowCurr();
        Map<String, String> settings;
        String result = "";
        double rate;
        if (isSettingsFile(chatId)) {
            settings = makeMap(chatId);
        } else settings = new HashMap<>(getDefoultMap());

        if (settings.get("Bank").equals("privat")) {
            PrivatBankCurrencyService privatBankCurrencyService = new PrivatBankCurrencyService();
            rate = privatBankCurrencyService.getRatePrivate(CurrencyPrivate.valueOf(settings.get("Currency")));
            result = showCurr.convertPrivate(rate, Integer.parseInt(settings.get("Number_of_signs")),
                    CurrencyPrivate.valueOf(settings.get("Currency")));

        }
        if (settings.get("Bank").equals("mono")) {
            MonoBankCurrencyService monoBankCurrencyService = new MonoBankCurrencyService();
            rate = monoBankCurrencyService.getRateMono(currencyMonoHandler(settings.get("Currency")));
            result = showCurr.convertMono(rate, Integer.parseInt(settings.get("Number_of_signs")),
                    currencyMonoHandler(settings.get("Currency")));

        }
        if (settings.get("Bank").equals("nbu")) {
            NBUCurrencyService nbuCurrencyService = new NBUCurrencyService();
            rate = nbuCurrencyService.getRateNBU(CurrencyNBU.valueOf(settings.get("Currency")));
            result = showCurr.convertNBU(rate, Integer.parseInt(settings.get("Number_of_signs")),
                    CurrencyNBU.valueOf(settings.get("Currency")));
        }
        return result;
    }

    public static CurrencyMono currencyMonoHandler(String currency) {
        CurrencyMono currencyMono = null;
        if (currency.equals("USD")) {
            currencyMono = CurrencyMono.USD;
        }
        if (currency.equals("EUR")) {
            currencyMono = CurrencyMono.EUR;
        }
        return currencyMono;
    }
}

