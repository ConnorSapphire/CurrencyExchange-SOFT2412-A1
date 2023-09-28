package JavaCurrency;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class UserInterfaceTest {
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setStreams() {
        System.setOut(new PrintStream(out, true));
    }

    @BeforeEach
    public void restoreDefault() {
        if (UserInterface.getAdmin()) {
            UserInterface.user();
        }
        UserInterface.currencies = new HashMap<String, Currency>();
        UserInterface.currencies.put("TESTAUD", new Currency("TESTAUD"));
        UserInterface.currencies.put("TESTUSD", new Currency("TESTUSD"));
    }

    @AfterEach
    public void restoreInitialStreams() {
        System.setOut(originalOut);
    }

    @AfterEach
    public void restoreFiles() {
        FileManager fm = new FileManager();
        // try {
        //     Scanner scan = new Scanner("src/main/java/JavaCurrency/CurrencyList.txt");
        //     ArrayList<String> lines = new ArrayList<String>();
        //     while (scan.hasNextLine()) {
        //         String line = scan.nextLine();
        //         if (!line.trim().equals("TEST")) {
        //             lines.add(line);
        //         }
        //     }
        //     scan.close();
        //     FileWriter fw = new FileWriter("src/main/java/JavaCurrency/CurrencyList.txt");
        //     for (String line : lines) {
        //         fw.write(line + "\n");
        //     }
        //     fw.close();
        // } catch (Exception e) {
        //     System.out.println("Could not do.");
        // }
        HashMap<String, Currency> currencies = fm.initialiseCurrencies();
        for (String s : currencies.keySet()) {
            Scanner currencyScan = fm.getScanner(s);
            if (currencyScan != null) {
                try {
                    Scanner scan = fm.getScanner(s);
                    ArrayList<String> lines = new ArrayList<String>();
                    while (scan.hasNextLine()) {
                        String line = scan.nextLine();
                        if (!line.trim().equals("TEST")) {
                            lines.add(line);
                        }
                    }
                    scan.close();
                    FileWriter fw = new FileWriter("src/main/java/JavaCurrency/"+s+".txt");
                    for (String line : lines) {
                        fw.write(line + "\n");
                    }
                    fw.close();
                } catch (Exception e) {
                    System.out.println("Could not do.");
                }
            }
        }
    }

    @Test
    void testQuit() {
        UserInterface ui = new UserInterface();
        UserInterface.getCommand("quit");
        assertTrue(UserInterface.getQuit());
    }

    @Test
    void testAdminNoPassword() {
        UserInterface.getCommand("admin");
        assertTrue(out.toString().contains(UserInterface.ANSI_RED + "No password provided. Still in User mode." + UserInterface.ANSI_RESET));
    }

    @Test
    void testAdminWrongPassword() {
        UserInterface.getCommand("admin wrongPassword");
        assertTrue(out.toString().contains(UserInterface.ANSI_RED + "Password provided was incorrect. Still in User mode." + UserInterface.ANSI_RESET));
    }

    @Test
    void testAdminCorrectPassword() {
        UserInterface.getCommand("admin password");
        assertTrue(UserInterface.getAdmin());
    }

    @Test
    void testAdminAlreadyAdmin() {
        UserInterface.admin("admin password");
        UserInterface.getCommand("admin password");
        assertTrue(out.toString().contains(UserInterface.ANSI_RED + "You are already in Admin mode." + UserInterface.ANSI_RESET));
    }

    @Test
    void testAdmintoUser() {
        UserInterface.admin("admin password");
        UserInterface.getCommand("user");
        assertFalse(UserInterface.getAdmin());
    }

    @Test
    void testUserAlreadyUser() {
        UserInterface.getCommand("user");
        assertTrue(out.toString().contains(UserInterface.ANSI_RED + "You are already in User mode." + UserInterface.ANSI_RESET));
    }

    @Test
    void testUserHelp() {
        UserInterface.getCommand("help");
        assertTrue(out.toString().contains("You are currently in " + UserInterface.ANSI_YELLOW + "User mode" + UserInterface.ANSI_RESET + "."));
    }

    @Test
    void testAdminHelp() {
        UserInterface.admin("admin password");
        UserInterface.getCommand("help");
        assertTrue(out.toString().contains("You are currently in " + UserInterface.ANSI_PURPLE + "Admin mode" + UserInterface.ANSI_RESET + "."));
    }

    @Test
    void testMainStarts() {
        UserInterface.main(null);
        assertTrue(out.toString().contains(UserInterface.ANSI_YELLOW + "Welcome to the Currency Converter!" + UserInterface.ANSI_RESET));
    }

    @Test
    void testUnrecognisedCommand() {
        UserInterface.getCommand("jibberish");
        assertTrue(out.toString().contains(UserInterface.ANSI_RED + "Unrecognised command." + UserInterface.ANSI_RESET));
    }

    @Test
    void testNotAdminAdd() {
        UserInterface.getCommand("add");
        assertTrue(out.toString().contains(UserInterface.ANSI_RED + "You are in " + UserInterface.ANSI_YELLOW + "User mode" + UserInterface.ANSI_RED + " and do not currently have access to this command." + UserInterface.ANSI_RESET));
    }

    @Test
    void testNotAdminUpdate() {
        UserInterface.getCommand("update");
        assertTrue(out.toString().contains(UserInterface.ANSI_RED + "You are in " + UserInterface.ANSI_YELLOW + "User mode" + UserInterface.ANSI_RED + " and do not currently have access to this command." + UserInterface.ANSI_RESET));
    }

    @Test
    void testConvertNoArgs() {
        UserInterface.getCommand("convert");
        assertTrue(out.toString().contains(UserInterface.ANSI_RED + "No arguments provided. Type 'help' to see how to properly use this command." + UserInterface.ANSI_RESET));
    }

    @Test
    void testHistoryNoArgs() {
        UserInterface.getCommand("history");
        assertTrue(out.toString().contains(UserInterface.ANSI_RED + "No arguments provided. Type 'help' to see how to properly use this command." + UserInterface.ANSI_RESET));
    }

    @Test
    void testAddNoArgs() {
        UserInterface.getCommand("admin password");
        UserInterface.getCommand("add");
        assertTrue(out.toString().contains(UserInterface.ANSI_RED + "No arguments provided. Type 'help' to see how to properly use this command." + UserInterface.ANSI_RESET));
    }

    @Test
    void testConvertNotEnoughArgs() {
        UserInterface.getCommand("convert TESTAUD 1");
        assertTrue(out.toString().contains(UserInterface.ANSI_RED + "Not enough arguments provided.Type 'help' to see how to properly use this command." + UserInterface.ANSI_RESET));
    }

    @Test
    void testConvertFromCurrencyError() {
        UserInterface.getCommand("convert DOESNTEXIST TESTAUD 1.0");
        assertTrue(out.toString().contains("Unrecognised Currency"));
    
    }
    
    @Test
    void testConvertToCurrencyError() {
        UserInterface.getCommand("convert TESTAUD DOESNTEXIST 1.0");
        assertTrue(out.toString().contains("Unrecognised Currency"));
    }

    @Test
    void testConvertValueEqualsZero() {
        UserInterface.getCommand("convert TESTUSD TESTUSD 0.0");
        assertTrue(out.toString().contains("Value cannot be negative or zero."));    
    }

    @Test
    void testConvertValueNotNumber() {
        UserInterface.getCommand("convert TESTUSD TESTUSD h");
        assertTrue(out.toString().contains("Value must be a number."));    
    }

    @Test
    void testConvertSuccess() {
        UserInterface.admin("admin password");
        UserInterface.update("update TESTAUD TESTUSD 01/01/0001 1.2");
        UserInterface.getCommand("convert TESTAUD TESTUSD 10");
        assertTrue(out.toString().contains("="));    
    }

    @Test
    void testHistoryNotEnoughArgs() {
        UserInterface.getCommand("history TESTAUD TESTUSD");
        assertTrue(out.toString().contains(UserInterface.ANSI_RED + "Not enough arguments provided.Type 'help' to see how to properly use this command." + UserInterface.ANSI_RESET));
    }

    @Test
    void testHistoryFromCurrencyError() {
        UserInterface.getCommand("history DOESNTEXIST TESTAUD 10 10");
        assertTrue(out.toString().contains("Unrecognised Currency"));
    
    }
    
    @Test
    void testHistoryToCurrencyError() {
        UserInterface.getCommand("history TESTAUD DOESNTEXIST 10 10");
        assertTrue(out.toString().contains("Unrecognised Currency"));
    }

    @Test
    void testHistoryIncorrectFromDateFormat() {
        UserInterface.getCommand("history TESTAUD TESTAUD 2009/2022 1.0");
        assertTrue(out.toString().contains("Incorrect format for date"));
    }

    @Test
    void testHistoryIncorrectToDateFormat() {
        UserInterface.getCommand("history TESTAUD TESTAUD 09/09/2022 2009/2022");
        assertTrue(out.toString().contains("Incorrect format for date"));
    }

    @Test
    void testHistoryIncorrectFromDateFormatIndividualLength() {
        UserInterface.getCommand("history TESTUSD TESTUSD 09/122/2022 09/09/2022");
        assertTrue(out.toString().contains("Incorrect format for date"));    
    }

    @Test
    void testHistoryIncorrectDateFormatIndividualLength() {
        UserInterface.getCommand("history TESTUSD TESTUSD 09/12/2022 09/099/2022");
        assertTrue(out.toString().contains("Incorrect format for date"));    
    }

    @Test
    void testHistoryDateContainsLetters() {
        UserInterface.getCommand("history TESTUSD TESTUSD 09/ll/2022 09/ll/2022");
        assertTrue(out.toString().contains("Please only use numbers and '/' for the date."));    
    }

    @Test
    void testHistoryDateDayOutOfRange() {
        UserInterface.getCommand("history TESTUSD TESTUSD 32/12/2022 09/11/2022");
        assertTrue(out.toString().contains("Day cannot be less than 0 or greater than 31."));    
    }

    @Test
    void testHistoryDateMonthOutOfRange() {
        UserInterface.getCommand("history TESTUSD TESTUSD 31/13/2022 21/13/2022");
        assertTrue(out.toString().contains("Month cannot be less than 0 or greater than 12."));    
    }

    @Test
    void testHistorySuccess() {
        UserInterface.getCommand("history TESTAUD TESTUSD 01/01/0000 01/01/4000");
        assertTrue(out.toString().contains("History not found!"));    
    }

    @Test
    void testAddNotEnoughArgs() {
        UserInterface.getCommand("admin password");
        UserInterface.add("add");
        assertTrue(out.toString().contains(UserInterface.ANSI_RED + "Not enough arguments provided.Type 'help' to see how to properly use this command." + UserInterface.ANSI_RESET));
    }
    
    @Test
    void testAddAlreadyExists() {
        UserInterface.getCommand("admin password");
        UserInterface.getCommand("add TESTAUD");
        assertTrue(out.toString().contains("already exists."));
    }
    
    @Test
    void testAddSuccess() {
        UserInterface.getCommand("admin password");
        UserInterface.getCommand("add test");
        assertTrue(out.toString().contains("Successfully added new currency!"));
    }

    @Test
    void testUpdateNoArgs() {
        UserInterface.getCommand("admin password");
        UserInterface.getCommand("update");
        assertTrue(out.toString().contains(UserInterface.ANSI_RED + "No arguments provided. Type 'help' to see how to properly use this command." + UserInterface.ANSI_RESET));
    }

    @Test
    void testUpdateNotEnoughArgs() {
        UserInterface.getCommand("admin password");
        UserInterface.getCommand("update not enough");
        assertTrue(out.toString().contains(UserInterface.ANSI_RED + "Not enough arguments provided.Type 'help' to see how to properly use this command." + UserInterface.ANSI_RESET));
    }

    @Test
    void testUpdateFromCurrencyError() {
        UserInterface.getCommand("admin password");
        UserInterface.getCommand("update DOESNTEXIST TESTAUD 20/09/2022 1.0");
        assertTrue(out.toString().contains("Unrecognised Currency"));
    
    }
    
    @Test
    void testUpdateToCurrencyError() {
        UserInterface.getCommand("admin password");
        UserInterface.getCommand("update TESTAUD DOESNTEXIST 20/09/2022 1.0");
        assertTrue(out.toString().contains("Unrecognised Currency"));
    }

    @Test
    void testUpdateIncorrectDateFormat() {
        UserInterface.getCommand("admin password");
        UserInterface.getCommand("update TESTAUD TESTAUD 2009/2022 1.0");
        assertTrue(out.toString().contains("Incorrect format for date"));
    }

    @Test
    void testUpdateIncorrectDateFormatIndividualLength() {
        UserInterface.getCommand("admin password");
        UserInterface.getCommand("update TESTUSD TESTUSD 09/122/2022 1.0");
        assertTrue(out.toString().contains("Incorrect format for date"));    
    }

    @Test
    void testUpdateDateContainsLetters() {
        UserInterface.getCommand("admin password");
        UserInterface.getCommand("update TESTUSD TESTUSD 09/ll/2022 1.0");
        assertTrue(out.toString().contains("Please only use numbers and '/' for the date."));    
    }

    @Test
    void testUpdateDateDayOutOfRange() {
        UserInterface.getCommand("admin password");
        UserInterface.getCommand("update TESTUSD TESTUSD 32/12/2022 1.0");
        assertTrue(out.toString().contains("Day cannot be less than 0 or greater than 31."));    
    }

    @Test
    void testUpdateDateMonthOutOfRange() {
        UserInterface.getCommand("admin password");
        UserInterface.getCommand("update TESTUSD TESTUSD 31/13/2022 1.0");
        assertTrue(out.toString().contains("Month cannot be less than 0 or greater than 12."));    
    }

    @Test
    void testUpdateRateEqualsZero() {
        UserInterface.getCommand("admin password");
        UserInterface.getCommand("update TESTUSD TESTUSD 31/12/2022 0.0");
        assertTrue(out.toString().contains("Exchange Rate cannot be negative or zero."));    
    }

    @Test
    void testUpdateRateNotNumber() {
        UserInterface.getCommand("admin password");
        UserInterface.getCommand("update TESTUSD TESTUSD 31/12/2022 l");
        assertTrue(out.toString().contains("Exchange Rate must be a number."));    
    }

    @Test
    void testUpdateSuccess() {
        UserInterface.getCommand("admin password");
        UserInterface.getCommand("update TESTAUD TESTUSD 01/01/0000 0.1");
        assertTrue(out.toString().contains("Successfully updated Exchange Rate"));    
    }

    @Test
    void testCurrenciesPrints() {
        UserInterface.getCommand("currencies");
        assertTrue(out.toString().contains("Below are the Currencies that are currently available:"));
    }

}
