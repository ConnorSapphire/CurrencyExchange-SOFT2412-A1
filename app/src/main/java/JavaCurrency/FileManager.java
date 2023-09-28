package JavaCurrency;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class FileManager {

    public FileManager() {
    }

    //Return an ArrayList of currencies by name
    public ArrayList<String> getCurrencyList() {
        ArrayList<String> currencyList = new ArrayList<String>();
        try {
            File file = new File("src/main/java/JavaCurrency/CurrencyList.txt");
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                currencyList.add(scan.nextLine());
            }
            scan.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("CurrencyList file not found!");
        }
        return currencyList;
    }

    //Return a scanner object for a certain currency file
    public Scanner getScanner(String symbol) {
        Scanner scan = null;
        try {
            File file = new File("src/main/java/JavaCurrency/"+symbol+".txt");
            scan = new Scanner(file);
        } catch (FileNotFoundException fnfe) {
            System.out.println("Currency not found in files.");
        }
        return scan;
    }

    //Return a HashMap of the latest exchange rate data for two specified currencies
    // UNUSED???
    public HashMap<String, String> getLatestData(String from, String to) {
        from = from.trim().toUpperCase();
        to = to.trim().toUpperCase();
        HashMap<String, String> latestData = new HashMap<String, String>();
        Scanner scan = getScanner(from);
        if (scan != null) {
            while (scan.hasNextLine()) {
                if (scan.nextLine().trim().equals(to)) {
                    for (int i = 0; i < 3; i ++) {
                        String line = scan.nextLine();
                        if (line.split(" ").length > 1) {
                            latestData.put(line.split(" ")[0], line.split(" ")[1]);
                        }
                    }
                }
            }
        }
        return latestData;
    }

    //Initialise Currency objects from file list and return as a HashMap of currency name and object
    public HashMap<String, Currency> initialiseCurrencies() {
        HashMap<String, Currency> currencies = new HashMap<String, Currency>();
        for (String cur : getCurrencyList()) {
            Currency current = new Currency(cur);
            currencies.put(cur, current);
        }
        return currencies;
    }

    //Fill in exchange rate information
    public void getRatesFromFile(HashMap<String, Currency> currencies) {
        HashMap<String,ArrayList<Double>> currencyRateHistory = new HashMap<String,ArrayList<Double>>();
        for (String currency : getCurrencyList()) {
            Scanner scan = getScanner(currency);
            if (scan != null) {
                boolean newCurrency = true;
                String toCurrency = "";
                while (scan.hasNextLine()) {
                    String line = scan.nextLine();
                    if (newCurrency) {
                        toCurrency = line;
                        currencyRateHistory.put(toCurrency,new ArrayList<Double>());
                        newCurrency = false;
                    } else if (line.equals("")) {
                        if(currencyRateHistory.containsKey(toCurrency) && currencies.containsKey(toCurrency)){
                            for (Double d : currencyRateHistory.get(toCurrency)) {
                                currencies.get(currency).addExchangeRate(currencies.get(toCurrency), d);
                            }
                            newCurrency = true;
                            continue;
                        }
                    } else {
                        if (line.contains("rate")) {
                            if (line.split(" ").length > 1) {
                                String valString = line.split(" ")[1];
                                try {
                                    Double value = Double.parseDouble(valString);
                                    currencyRateHistory.get(toCurrency).add(0,value);
                                } catch(NumberFormatException NFE) {
                                    NFE.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //Update file with new exchange rate data
    public void addExchangeRate(String from, String to, String date, String exchangeRate, String change) {
        Scanner scan = getScanner(from);
        ArrayList<String> lines = new ArrayList<String>();
        boolean foundTo = false;
        if (scan == null) {
            try {
                FileWriter fWriter = new FileWriter("src/main/java/JavaCurrency/"+from+".txt");
                fWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            scan = getScanner(from);
        }
        if (scan != null) {
            while (scan.hasNextLine()) {
                lines.add(scan.nextLine());
            }
            scan.close();
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).trim().equals(to.trim().toUpperCase())) {
                    foundTo = true;
                    lines.add(i+1,"date "+date);
                    lines.add(i+2,"rate "+exchangeRate);
                    lines.add(i+3,"change "+change);
                }
            }
            if (!foundTo) {
                lines.add("");
                lines.add(to.trim().toUpperCase());
                lines.add("date "+date);
                lines.add("rate "+exchangeRate);
                lines.add("change "+change);
            }
            try {
                FileWriter fWriter = new FileWriter("src/main/java/JavaCurrency/"+from+".txt");
                for (String line: lines) {
                    fWriter.write(line+"\n");
                }
                fWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Add a new currency to files, will have no data to begin
    public void addCurrency(String currency) {
        currency = currency.toUpperCase();
        Scanner scan = getScanner("CurrencyList");
        ArrayList<String> lines = new ArrayList<String>();
        for (String s : getCurrencyList()) {
            Scanner curScan = getScanner(s);
            ArrayList<String> curLines = new ArrayList<String>();
            if (curScan != null) {
                while (curScan.hasNextLine()) {
                    curLines.add(curScan.nextLine());
                }
                curLines.add("");
                curLines.add(currency);
                try {
                    FileWriter fWriter = new FileWriter("src/main/java/JavaCurrency/"+s+".txt");
                    for (String line: curLines) {
                        fWriter.write(line+"\n");
                    }
                    fWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            FileWriter fWriter = new FileWriter("src/main/java/JavaCurrency/"+currency+".txt");
            for (String s : getCurrencyList()) {
                fWriter.write(s+"\n\n");
            }
            fWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (scan != null) {
            while (scan.hasNextLine()) {
                lines.add(scan.nextLine());
            }
            lines.add(currency);
            try {
                FileWriter fWriter = new FileWriter("src/main/java/JavaCurrency/CurrencyList.txt");
                for (String line: lines) {
                    fWriter.write(line+"\n");
                }
                fWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //update the file with the most popular table
    public void updateTableFile(ArrayList<Currency> table){
        try {
            FileWriter fWriter = new FileWriter("src/main/java/JavaCurrency/table.txt");
            for (Currency c : table) {
                fWriter.write(c.getSymbol() +"\n");
            }
            fWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Retrive from table
    public ArrayList<String> getTableFile(){
        ArrayList<String> symbols = new ArrayList<>();
        String sym = "";
        Scanner scan = getScanner("table");
        if(scan != null){
            while(scan.hasNextLine()){
                sym = scan.nextLine();
                symbols.add(sym);
            }
        }
        return symbols;
    } 

    // Generate history
    public HashMap<Currency[], CurrencyHistory> getHistory(HashMap<String, Currency> currencies){
        HashMap<Currency[], CurrencyHistory> history = new HashMap<>();
        Scanner scan = null;
        CurrencyHistory his = null;
        Calendar date = null;
        Double value = null;
        boolean newData = true;
        for(String s : currencies.keySet()){
            scan = getScanner(s);
            if(scan != null){
                while(scan.hasNextLine()){
                    String[] str = scan.nextLine().split(" ");
                    if(!newData){
                        if(str[0].equals("date")){
                            date = CurrencyHistory.convertDate(str[1]);
                        }else if(str[0].equals("rate")){
                            value = Double.parseDouble(str[1]);
                            his.updateHistory(date, value);
                        }else if(str[0].equals("")){
                            newData = true;
                        }
                    }else{
                        for(String string : currencies.keySet()){
                            if(string.equals(str[0])){
                                Currency[] cur = new Currency[]{currencies.get(s), currencies.get(string)};
                                history.put(cur, new CurrencyHistory(currencies.get(s), currencies.get(string)));
                                his = history.get(cur);
                                newData = false;
                                break;
                            }
                        }
                    }                    
                }
            }
            scan = null;
            newData = true;
        }
        return history;
    }

    // Get change arrows from file
    public String getArrows(Currency from, Currency to){
        
        Scanner scan = getScanner(from.getSymbol());
        String answer = "";
        String line = "";
        boolean find = false;
        if (scan != null) {
            while(scan.hasNextLine()){
                line = scan.nextLine();
                String[] str = line.split(" ");
                if(line.equals(to.getSymbol())){
                    find = true;
                }
                if(find){
                    if(line == ""){
                        break;
                    }else if(str[0].equals("change")){
                        if(str.length >= 2){
                            answer = str[1];
                        }
                        
                        for(int i = 2; i < str.length; i++){
                            answer = answer + " " + str[i];
                        }
                        break;
                    }
                }
            }
        }
        return answer;
    }

    public HashMap<Currency, String> allChangeRates(HashMap<String, Currency> currencies, Currency c) {
        HashMap<Currency, String> hash = new HashMap<>();
        for(String str : currencies.keySet()){
            if(!str.equals(c.getSymbol())){
                Currency to = currencies.get(str);
                hash.put(to, this.getArrows(c, to));
            }
        }
        return hash;
    }
}
