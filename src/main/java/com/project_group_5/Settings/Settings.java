package com.project_group_5.Settings;

import com.project_group_5.currency.*;
import com.project_group_5.currencyUi.ShowCurr;

import java.io.*;
import java.util.*;


public class Settings {
    private static final Map<String, String> defoultMap = new HashMap<>();

    public static Map<String, String> getDefoultMap() {
        defoultMap.put("Bank", "privat");
        defoultMap.put("Currency", "USD");
        defoultMap.put("Number_of_signs", Integer.toString(2));
        return defoultMap;
    }

    public void setSettings(long chatId, String key, String value) throws IOException {

        String filePath = new File("CurrencyBot").getAbsolutePath()
                .replace("CurrencyBot" + File.separator + "CurrencyBot", "CurrencyBot");
        File f1 = new File(filePath);
        List<String> str = List.of(Objects.requireNonNull(f1.list((dir, name) -> name.contains(String.valueOf(chatId)))));

        Map<String, String> settings = new HashMap<>();

        if (!str.isEmpty()) {
            File file = new File("chatId" + chatId + "Settings");
            FileReader reader = new FileReader(file);
            Scanner scanner = new Scanner(reader);
            String[] row = scanner.nextLine()
                    .replace("{", "").replace("}", "").split(", ");
            for (String s : row) {
                String[] keyValue = s.split("=");
                settings.put(keyValue[0], keyValue[1]);
                settings.replace(key, value);
                file.delete();
            }
        } else
            settings.putAll(getDefoultMap());
        settings.replace(key, value);

        BufferedWriter writer = new BufferedWriter(new FileWriter("chatId" + chatId + "Settings"));
        writer.write(settings.toString());
        writer.close();
        }

//        перевіряємо чи встановлювались вже налаштування у цьому chatId
//        public boolean checkForDefoultSettins(long chatId) {
//            boolean checked = false;
//            String filePath = new File("CurrencyBot").getAbsolutePath()
//                    .replace("CurrencyBot" + File.separator + "CurrencyBot", "CurrencyBot");
//            File f1 = new File(filePath);
//            String[] str = f1.list((dir, name) -> name.contains(String.valueOf(chatId)));
//
//            if (str == null) {
//            checked = true;
//            }
//            return checked;
//        }

//        перевіряємо чи у встановлених налаштуваннях є така ж валюта і додавання нової
//        public  boolean settingsForCurrency(long chatId, String value) throws IOException {
//            boolean checkingPointer = false;
//
//            Map<String, String> settings = new HashMap<>();
//
//            File file = new File("chatId" + chatId + "Settings");
//            FileReader reader = new FileReader(file);
//            Scanner scanner = new Scanner(reader);
//            String[] row = scanner.nextLine()
//                    .replace("{", "").replace("}", "").split(", ");
//                for (String s : row) {
//                    String[] keyValue = s.split("=");
//                    settings.put(keyValue[0], keyValue[1]);
//                }
//
//                if (settings.get("Currency").equals(value) || settings.get("Currency1").equals(value)){
//                    checkingPointer = true;
//            }else settings.put("Currency1", value);
//            file.delete();
//
//            BufferedWriter writer = new BufferedWriter(new FileWriter("chatId" + chatId + "Settings"));
//            writer.write(settings.toString());
//            writer.close();
//
//                return checkingPointer;
//        }
//
//        public boolean isSettedTwoCurrencies(long chatId) throws FileNotFoundException {
//            boolean checked = false;
//            Map<String, String> settings = new HashMap<>();
//
//            File file = new File("chatId" + chatId + "Settings");
//            FileReader reader = new FileReader(file);
//            Scanner scanner = new Scanner(reader);
//            String[] row = scanner.nextLine()
//                    .replace("{", "").replace("}", "").split(", ");
//            for (String s : row) {
//                String[] keyValue = s.split("=");
//                settings.put(keyValue[0], keyValue[1]);
//            }
//            if (settings.containsKey("Currency1")){
//                checked = true;
//            }
//            return checked;
//        }

        //надаємо інформацію якщо встановлено дві валюти
//        public String usingTwoCurenciesImplementation(long chatId) throws FileNotFoundException {
//            ShowCurr showCurr = new ShowCurr();
//            Map<String, String> settings = new HashMap<>();
//            String result = "";
//
//            File file = new File("chatId" + chatId + "Settings");
//            FileReader reader = new FileReader(file);
//            Scanner scanner = new Scanner(reader);
//            String[] row = scanner.nextLine()
//                    .replace("{", "").replace("}", "").split(", ");
//            for (String s : row) {
//                String[] keyValue = s.split("=");
//                settings.put(keyValue[0], keyValue[1]);
//
//                implementSettings(chatId);
//
//                if (settings.get("Bank").equals("privat")) {
//                    PrivatBankCurrencyService privatBankCurrencyService = new PrivatBankCurrencyService();
//                    double rate = privatBankCurrencyService
//                            .getRatePrivate(CurrencyPrivate.valueOf(settings.get("Currency1")));
//                    result = showCurr.convertPrivate(rate, Integer.parseInt(settings.get("Number_of_signs")),
//                            CurrencyPrivate.valueOf(settings.get("Currency")));
//
//                }
//                if (settings.get("Bank").equals("mono")) {
//                    MonoBankCurrencyService monoBankCurrencyService = new MonoBankCurrencyService();
//                    double rate = monoBankCurrencyService
//                            .getRateMono(currencyMonoHandler(settings.get("Currency1")));
//                    result = showCurr.convertMono(rate, Integer.parseInt(settings.get("Number_of_signs")),
//                            currencyMonoHandler(settings.get("Currency")));
//
//                }
//                if (settings.get("Bank").equals("nbu")) {
//                    NBUCurrencyService nbuCurrencyService = new NBUCurrencyService();
//                    double rate = nbuCurrencyService
//                            .getRateNBU(CurrencyNBU.valueOf(settings.get("Currency1")));
//                    result = showCurr.convertNBU(rate, Integer.parseInt(settings.get("Number_of_signs")),
//                            CurrencyNBU.valueOf(settings.get("Currency")));
//                }
//            }
//            return result;
//        }

    public String implementSettings(long chatId) throws FileNotFoundException {
        ShowCurr showCurr = new ShowCurr();
        Map<String, String> settings = new HashMap<>();
        String result = "";
        double rate = 0.0;
        String filePath = new File("CurrencyBot").getAbsolutePath()
                .replace("CurrencyBot" + File.separator + "CurrencyBot", "CurrencyBot");
        File f1 = new File(filePath);
        List<String> str = List.of(Objects.requireNonNull(f1.list((dir, name) -> name.contains(String.valueOf(chatId)))));

        if (!str.isEmpty()) {
            File file = new File("chatId" + chatId + "Settings");
            FileReader reader = new FileReader(file);
            Scanner scanner = new Scanner(reader);
            String[] row = scanner.nextLine()
                    .replace("{", "").replace("}", "") .split(", ");
            for (String s : row) {
                String[] keyValue = s.split("=");
                settings.put(keyValue[0], keyValue[1]);
            }
        }else settings.putAll(getDefoultMap());

        if (settings.get("Bank").equals("privat")){
            PrivatBankCurrencyService privatBankCurrencyService = new PrivatBankCurrencyService();
            rate = privatBankCurrencyService.getRatePrivate(CurrencyPrivate.valueOf(settings.get("Currency")));
            result = showCurr.convertPrivate(rate, Integer.parseInt(settings.get("Number_of_signs")),
                    CurrencyPrivate.valueOf(settings.get("Currency")));

        } if (settings.get("Bank").equals("mono")){
            MonoBankCurrencyService monoBankCurrencyService = new MonoBankCurrencyService();
            rate = monoBankCurrencyService.getRateMono(currencyMonoHandler(settings.get("Currency")));
            result = showCurr.convertMono(rate, Integer.parseInt(settings.get("Number_of_signs")),
                    currencyMonoHandler(settings.get("Currency")));

        }if (settings.get("Bank").equals("nbu")){
            NBUCurrencyService nbuCurrencyService = new NBUCurrencyService();
            rate = nbuCurrencyService.getRateNBU(CurrencyNBU.valueOf(settings.get("Currency")));
            result = showCurr.convertNBU(rate, Integer.parseInt(settings.get("Number_of_signs")),
                    CurrencyNBU.valueOf(settings.get("Currency")));
        }
        return result;
        }

        public CurrencyMono currencyMonoHandler(String currency){
        CurrencyMono currencyMono = null;
        if (currency.equals("USD")){
            currencyMono = CurrencyMono.USD;
        }if (currency.equals("EUR")){
            currencyMono = CurrencyMono.EUR;
            }
        return currencyMono;
        }
    }

