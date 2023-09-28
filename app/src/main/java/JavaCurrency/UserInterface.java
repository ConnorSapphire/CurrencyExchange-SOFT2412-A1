package JavaCurrency;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.UIManager.*;

public class UserInterface {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    private static boolean admin = false;
    private static boolean quit = false;
    public static HashMap<String, Currency> currencies = new HashMap<String, Currency>();
    public static NormalUser user = new NormalUser("User", "password");
    private static FileManager fm = new FileManager();
    public static RecentTable table = new RecentTable();
    public static HashMap<Currency[], CurrencyHistory> history = new HashMap<>();

    public static void getCommand(String line) {
        line = line.toLowerCase();
        String command = line;
        boolean arguments = false;
        if (line.split(" ").length > 1) {
            command = line.split(" ")[0];
            arguments = true;
        } else {
            command = line.trim();
        }
        if (command.equals("admin")) {
            if (admin) {
                System.out.println(ANSI_RED + "You are already in Admin mode." + ANSI_RESET);
            } else if (!arguments) {
                System.out.println(ANSI_RED + "No password provided. Still in User mode." + ANSI_RESET);
            } else {
                admin(line);
            }
        } else if (command.equals("convert")) {
            if (!arguments) {
                System.out.println(ANSI_RED + "No arguments provided. Type 'help' to see how to properly use this command." + ANSI_RESET);
            } else {
                convert(line);
            }
        } else if (command.equals("help")) {
            help();
        } else if (command.equals("table")) {
            table();
        } else if (command.equals("history")) {
            if (!arguments) {
                System.out.println(ANSI_RED + "No arguments provided. Type 'help' to see how to properly use this command." + ANSI_RESET);
            } else {
                history(line);
            }
        } else if (command.equals("user")) {
            if (!admin) {
                System.out.println(ANSI_RED + "You are already in User mode." + ANSI_RESET);
            } else {
                user();
            }
        } else if (command.equals("add")) {
            if (!admin) {
                System.out.println(ANSI_RED + "You are in " + ANSI_YELLOW + "User mode" + ANSI_RED + " and do not currently have access to this command." + ANSI_RESET);
            } else if (!arguments) {
                System.out.println(ANSI_RED + "No arguments provided. Type 'help' to see how to properly use this command." + ANSI_RESET);
            } else {
                add(line);
            }
        } else if (command.equals("update")) {
            if (!admin) {
                System.out.println(ANSI_RED + "You are in " + ANSI_YELLOW + "User mode" + ANSI_RED + " and do not currently have access to this command." + ANSI_RESET);
            } else if (!arguments) {
                System.out.println(ANSI_RED + "No arguments provided. Type 'help' to see how to properly use this command." + ANSI_RESET);
            } else {
                update(line);
            }
        } else if (command.equals("currencies")) {
            currencies();
        } else if (command.equals("quit")) {
            quit();
        } else {
            System.out.println(ANSI_RED + "Unrecognised command." + ANSI_RESET);
        }
    }

    public static void admin(String line) {
        String password = line.split(" ")[1];
        if (password.equals("password")) {
            admin = true;
            help();
        } else {
            System.out.println(ANSI_RED + "Password provided was incorrect. Still in User mode." + ANSI_RESET);
        }
    }

    public static void convert(String line) {
        String fromCurrency, toCurrency, value;
        if (line.split(" ").length < 4) {
            System.out.println(ANSI_RED + "Not enough arguments provided.Type 'help' to see how to properly use this command." + ANSI_RESET);
        } else {
            fromCurrency = line.split(" ")[1].toUpperCase();
            toCurrency = line.split(" ")[2].toUpperCase();
            value = line.split(" ")[3];
            if (!currencies.containsKey(fromCurrency)) {
                System.out.println(String.format("%sUnrecognised Currency %s%s%s. Please type 'currencies' for a list of all available Currencies.%s", ANSI_RED,ANSI_RESET,fromCurrency,ANSI_RED,ANSI_RESET));
            } else if (!currencies.containsKey(toCurrency)) {
                System.out.println(String.format("%sUnrecognised Currency %s%s%s. Please type 'currencies' for a list of all available Currencies.%s", ANSI_RED,ANSI_RESET,toCurrency,ANSI_RED,ANSI_RESET));
            } else {
                try {
                    Double valueNumber = Double.parseDouble(value);
                    if (valueNumber <= 0) {
                        System.out.println(String.format("%sValue cannot be negative or zero.%s",ANSI_RED,ANSI_RESET));
                    } else {
                        Double convertedValue = user.convertMoney(currencies.get(fromCurrency), currencies.get(toCurrency), valueNumber);
                        if (convertedValue!=null) {
                            System.out.println(String.format("%s%s %s = %.2f %s%s",ANSI_GREEN,value,fromCurrency,convertedValue,toCurrency,ANSI_RESET));
                        }
                    }
                } catch (NumberFormatException nfe) {
                    System.out.println(String.format("%sValue must be a number.%s",ANSI_RED,ANSI_RESET));
                }
            }
        }
    }

    public static void help() {
        if (admin) {
            System.out.println("You are currently in " + ANSI_PURPLE + "Admin mode" + ANSI_RESET + ".");
            System.out.println("You have access to the following commands:");
            System.out.println("> user");
            System.out.println("\tSwitch to User mode.\n");
            System.out.println("> convert <From Currency (ISO-4217)> <To Currency (ISO-4217)> <Value>");
            System.out.println("\tConvert the Value from the Currency given by its ISO-4217 code into another Currency given by its ISO-4217 code.\n");
            System.out.println("> table");
            System.out.println("\tDisplay a table of the four most popular Currencies and their Exchange Rates.\n");
            System.out.println("> history <Currency Name (ISO-4217)> <Currency Name (ISO-4217)> <Start Date (DD/MM/YYYY)> <End Date (DD/MM/YYYY)>");
            System.out.println("\tDisplay the relative Exchange Rate history of the Currencies given by their ISO-4217 code between the Start Date and End Date given.\n");
            System.out.println("> add <Currency Name (ISO-4217)>");
            System.out.println("\tAdd the Currency given by its ISO-4217 code to the list of Currencies.\n");
            System.out.println("> update <From Currency (ISO-4217)> <To Currency (ISO-4217)> <Date (DD/MM/YYYY)> <Exchange Rate>");
            System.out.println("\tUpdate the Exchange Rate between the two Currencies given by their ISO-4217 codes and include the current date.\n");
            System.out.println("> currencies");
            System.out.println("\tDisplay a list of all current Currencies by their ISO-4217 codes.\n");
            System.out.println("> help");
            System.out.println("\tDisplay a list of all currently available commands with a brief description of each.\n");
            System.out.println("> quit");
            System.out.println("\tQuit the Currency Converter program.");
        } else {
            System.out.println("You are currently in " + ANSI_YELLOW + "User mode" + ANSI_RESET + ".");
            System.out.println("You have access to the following commands:");
            System.out.println("> admin <Password>");
            System.out.println("\tSwitch to Admin mode.\n");
            System.out.println("> convert <From Currency (ISO-4217)> <To Currency (ISO-4217)> <Value>");
            System.out.println("\tConvert the Value from the Currency given by its ISO-4217 code into another Currency given by its ISO-4217 code.\n");
            System.out.println("> table");
            System.out.println("\tDisplay a table of the four most popular Currencies and their Exchange Rates.\n");
            System.out.println("> history <Currency Name (ISO-4217)> <Currency Name (ISO-4217)> <Start Date (DD/MM/YYYY)> <End Date (DD/MM/YYYY)>");
            System.out.println("\tDisplay the relative Exchange Rate history of two Currencies given by their ISO-4217 code between the Start Date and End Date given.\n");
            System.out.println("> currencies");
            System.out.println("\tDisplay a list of all current Currencies by their ISO-4217 codes.\n");
            System.out.println("> help");
            System.out.println("\tDisplay a list of all currently available commands with a brief description of each.\n");
            System.out.println("> quit");
            System.out.println("\tQuit the Currency Converter program.");
        }
    }

    public static void table() {
        try {// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (info.getName().equals("Nimbus")) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                JTableRowHeader display = new JTableRowHeader(user.popularTable(table));
            }
        });
    }

    public static void history(String line) {
        String currency1, currency2, fromDate, toDate;
        if (line.split(" ").length < 5) {
            System.out.println(ANSI_RED + "Not enough arguments provided.Type 'help' to see how to properly use this command." + ANSI_RESET);
        } else {
            currency1 = line.split(" ")[1].toUpperCase();
            currency2 = line.split(" ")[2].toUpperCase();
            fromDate = line.split(" ")[3];
            toDate = line.split(" ")[4];
            if (!currencies.containsKey(currency1)) {
                System.out.println(String.format("%sUnrecognised Currency %s%s%s. Please type 'currencies' for a list of all available Currencies.%s", ANSI_RED,ANSI_RESET,currency1,ANSI_RED,ANSI_RESET));
            } else if (!currencies.containsKey(currency2)) {
                System.out.println(String.format("%sUnrecognised Currency %s%s%s. Please type 'currencies' for a list of all available Currencies.%s", ANSI_RED,ANSI_RESET,currency2,ANSI_RED,ANSI_RESET));
            } else if (fromDate.split("/").length < 3) {
                System.out.println(String.format("%sIncorrect format for date %s. Please provide the date in the format DD/MM/YYYY.%s",ANSI_RED,fromDate,ANSI_RESET));
            } else if (toDate.split("/").length < 3) {
                System.out.println(String.format("%sIncorrect format for date %s. Please provide the date in the format DD/MM/YYYY.%s",ANSI_RED,toDate,ANSI_RESET));
            } else if (fromDate.split("/")[0].length() != 2 || fromDate.split("/")[1].length() != 2 || fromDate.split("/")[2].length() != 4) {
                System.out.println(String.format("%sIncorrect format for date %s. Please provide the date in the format DD/MM/YYYY.%s",ANSI_RED,fromDate,ANSI_RESET));
            } else if (toDate.split("/")[0].length() != 2 || toDate.split("/")[1].length() != 2 || toDate.split("/")[2].length() != 4) {
                System.out.println(String.format("%sIncorrect format for date %s. Please provide the date in the format DD/MM/YYYY.%s",ANSI_RED,toDate,ANSI_RESET));
            } else {
                try {
                    int fromDateDay = Integer.parseInt(fromDate.split("/")[0]);
                    int fromDateMonth = Integer.parseInt(fromDate.split("/")[1]);
                    int fromDateYear = Integer.parseInt(fromDate.split("/")[2]);
                    int toDateDay = Integer.parseInt(toDate.split("/")[0]);
                    int toDateMonth = Integer.parseInt(toDate.split("/")[1]);
                    int toDateYear = Integer.parseInt(toDate.split("/")[2]);
                    if (fromDateDay <= 0 || fromDateDay > 31 || toDateDay <= 0 || toDateDay > 31) {
                        System.out.println(String.format("%sDay cannot be less than 0 or greater than 31.%s", ANSI_RED, ANSI_RESET));
                    } else if (fromDateMonth <= 0 || fromDateMonth > 12 || toDateMonth <= 0 || toDateMonth > 12) {
                        System.out.println(String.format("%sMonth cannot be less than 0 or greater than 12.%s", ANSI_RED, ANSI_RESET));
                    } else {
                        user.historyRates(currencies.get(currency1), currencies.get(currency2), CurrencyHistory.convertDate(fromDate), CurrencyHistory.convertDate(toDate), history);
                    }
                } catch (NumberFormatException nfe) {
                    System.out.println(String.format("%sPlease only use numbers and '/' for the date. Use the format DD/MM/YYYY.%s",ANSI_RED,ANSI_RESET));
                }
            } 
        }
    }

    public static void user() {
        admin = false;
        help();
    }

    public static void add(String line) {
        String currency;
        if (line.split(" ").length < 2) {
            System.out.println(ANSI_RED + "Not enough arguments provided.Type 'help' to see how to properly use this command." + ANSI_RESET);
        } else {
            currency = line.split(" ")[1].toUpperCase();
            if (currencies.containsKey(currency)) {
                System.out.println(String.format("%sCurrency %s%s%s already exists. Please type 'currencies' for a list of all available Currencies. You can update a Currency using the 'update' command.%s", ANSI_RED,ANSI_RESET,currency,ANSI_RED,ANSI_RESET));
            } else {
                fm.addCurrency(currency);
                currencies  = fm.initialiseCurrencies();
                table.updateTable(currencies.get(currency));
                System.out.println(ANSI_GREEN+"Successfully added new currency!"+ANSI_RESET);
            }
        }
    }

    public static void update(String line) {
        String fromCurrency, toCurrency, date, exchangeRate;
        if (line.split(" ").length < 5) {
            System.out.println(ANSI_RED + "Not enough arguments provided.Type 'help' to see how to properly use this command." + ANSI_RESET);
        } else {
            fromCurrency = line.split(" ")[1].toUpperCase();
            toCurrency = line.split(" ")[2].toUpperCase();
            date = line.split(" ")[3];
            exchangeRate = line.split(" ")[4];
            if (!currencies.containsKey(toCurrency)) {
                System.out.println(String.format("%sUnrecognised Currency %s%s%s. Please type 'currencies' for a list of all available Currencies. You can add a new Currency using the 'add' command.%s", ANSI_RED,ANSI_RESET,toCurrency,ANSI_RED,ANSI_RESET));
            } else if (!currencies.containsKey(fromCurrency)) {
                System.out.println(String.format("%sUnrecognised Currency %s%s%s. Please type 'currencies' for a list of all available Currencies. You can add a new Currency using the 'add' command.%s", ANSI_RED,ANSI_RESET,fromCurrency,ANSI_RED,ANSI_RESET));
            } else if (date.split("/").length < 3) {
                System.out.println(String.format("%sIncorrect format for date %s. Please provide the date in the format DD/MM/YYYY.%s",ANSI_RED,date,ANSI_RESET));
            } else if (date.split("/")[0].length() != 2 || date.split("/")[1].length() != 2 || date.split("/")[2].length() != 4) {
                System.out.println(String.format("%sIncorrect format for date %s. Please provide the date in the format DD/MM/YYYY.%s",ANSI_RED,date,ANSI_RESET));
            } else {
                boolean correctDate = true;
                try {
                    int dateDay = Integer.parseInt(date.split("/")[0]);
                    int dateMonth = Integer.parseInt(date.split("/")[1]);
                    int dateYear = Integer.parseInt(date.split("/")[2]);
                    if (dateDay <= 0 || dateDay > 31) {
                        correctDate = false;
                        System.out.println(String.format("%sDay cannot be less than 0 or greater than 31.%s", ANSI_RED, ANSI_RESET));
                    } else if (dateMonth <= 0 || dateMonth > 12) {
                        correctDate = false;
                        System.out.println(String.format("%sMonth cannot be less than 0 or greater than 12.%s", ANSI_RED, ANSI_RESET));
                    }
                } catch (NumberFormatException nfe) {
                    System.out.println(String.format("%sPlease only use numbers and '/' for the date. Use the format DD/MM/YYYY.%s",ANSI_RED,ANSI_RESET));
                    correctDate = false;
                }
                if (correctDate) {
                    try {
                        Double exchangeRateNumber = Double.parseDouble(exchangeRate);
                        if (exchangeRateNumber <= 0) {
                            System.out.println(String.format("%sExchange Rate cannot be negative or zero.%s",ANSI_RED,ANSI_RESET));
                        } else {
                            table.updateTable(currencies.get(fromCurrency));
                            table.updateTable(currencies.get(toCurrency));
                            currencies.get(fromCurrency).addExchangeRate(currencies.get(toCurrency), exchangeRateNumber);
                            fm.addExchangeRate(fromCurrency, toCurrency, date, exchangeRate, currencies.get(fromCurrency).getChangeValue(currencies.get(toCurrency)));
                            System.out.println(ANSI_GREEN+"Successfully updated Exchange Rate from "+fromCurrency+" to "+toCurrency+"!"+ANSI_RESET);

                            currencies.get(toCurrency).addExchangeRate(currencies.get(fromCurrency), 1/exchangeRateNumber);
                            fm.addExchangeRate(toCurrency, fromCurrency, date, String.format("%f", 1/exchangeRateNumber), currencies.get(toCurrency).getChangeValue(currencies.get(fromCurrency)));
                            System.out.println(ANSI_GREEN+"Successfully updated Exchange Rate from "+toCurrency+" to "+fromCurrency+"!"+ANSI_RESET);
                        }
                    } catch (NumberFormatException nfe) {
                        System.out.println(String.format("%sExchange Rate must be a number.%s",ANSI_RED,ANSI_RESET));
                    }
                }
            }
        }
    }

    public static void currencies() {
        System.out.println("Below are the Currencies that are currently available:");
        for (String s : currencies.keySet()) {
            System.out.println(String.format("> %s",s));
        }
    }

    public static void quit() {
        quit = true;
        System.out.println(ANSI_RED + "You have quit the " + ANSI_YELLOW + "Currency Converter" + ANSI_RED + ". Goodbye!" + ANSI_RESET);
    }
    
    public static boolean getAdmin() {
        return admin;
    }

    public static boolean getQuit() {
        return quit;
    }


    public static void main(String[] args) {

        currencies = fm.initialiseCurrencies();
        for(Currency c : currencies.values()){
            c.setChange(fm.allChangeRates(currencies, c));
        }
        fm.getRatesFromFile(currencies);
        ArrayList<String> str = fm.getTableFile();
        history = fm.getHistory(currencies);
        for(String s : str){
            if(currencies.containsKey(s)){
                table.updateTable(currencies.get(s));
            }
        }
        for(Currency c : currencies.values()){
            if(table.table.size() < 4){
                table.updateTable(c);
            }else{
                break;
            }
        }
        System.out.println(ANSI_YELLOW + "Welcome to the Currency Converter!" + ANSI_RESET);
        help();
        Scanner in = new Scanner(System.in);
        System.out.print(ANSI_CYAN + "\nEnter a command: " + ANSI_RESET);
        while (in.hasNextLine() && !quit) {
            getCommand(in.nextLine());
            if (quit) {
                break;
            }
            System.out.print(ANSI_CYAN + "\nEnter a command: " + ANSI_RESET);
        }
        in.close();
    } 
}
    