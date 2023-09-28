package JavaCurrency;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class FileManagerTests {
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setStreams() {
        System.setOut(new PrintStream(out, true));
    }

    @AfterEach
    public void restoreInitialStreams() {
        System.setOut(originalOut);
    }

    @Test
    void testLatestDateCorrect() {
        FileManager fm = new FileManager();
        UserInterface.currencies = new HashMap<String,Currency>();
        UserInterface.currencies.put("TESTAUD", new Currency("TESTAUD"));
        UserInterface.currencies.put("TESTUSD", new Currency("TESTUSD"));
        UserInterface.update("update TESTAUD TESTUSD 01/01/0001 10");
        HashMap<String, String> actual = fm.getLatestData("TESTAUD", "TESTUSD");
        HashMap<String, String> expected = new HashMap<String, String>();
        expected.put("date", "01/01/0001");
        expected.put("rate", "10");
        assertEquals(expected.get("date"), actual.get("date"));;
    }

    @Test
    void testLatestRateCorrect() {
        FileManager fm = new FileManager();
        UserInterface.currencies = new HashMap<String,Currency>();
        UserInterface.currencies.put("TESTAUD", new Currency("TESTAUD"));
        UserInterface.currencies.put("TESTUSD", new Currency("TESTUSD"));
        UserInterface.update("update TESTAUD TESTUSD 01/01/0001 10");
        HashMap<String, String> actual = fm.getLatestData("TESTAUD", "TESTUSD");
        HashMap<String, String> expected = new HashMap<String, String>();
        expected.put("date", "01/01/0001");
        expected.put("rate", "10");
        assertEquals(expected.get("rate"), actual.get("rate"));;
    }

    @Test
    void testAddExchangeRateFileDoesntExist() {
        FileManager fm = new FileManager();
        fm.addExchangeRate("DOESNTEXIST", "TESTAUD", "01/01/0000", "1", "-");
        File file = new File("src/main/java/JavaCurrency/DOESNTEXIST.txt");
        boolean fileCreated = file.delete();
        assertTrue(fileCreated);
    }

    @Test
    void testAddExchangeRateToDoesntExist() {
        FileManager fm = new FileManager();
        fm.addExchangeRate("TESTAUD", "DOESNTEXIST", "01/01/0000", "1", "-");
        Scanner scan = fm.getScanner("TESTAUD");
        boolean foundTo = false;
        ArrayList<String> lines = new ArrayList<String>();
        if (scan != null) {
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (line.contains("DOESNTEXIST")) {
                    foundTo = true;
                    for (int i = 0; i < 3; i++) {
                        if (scan.hasNextLine()) {
                            scan.nextLine();
                        }
                    }
                } else {
                    lines.add(line);
                }
            }
        }
        try {
            FileWriter fw = new FileWriter("src/main/java/JavaCurrency/TESTAUD.txt");
            for (String line : lines) {
                fw.write(line + "\n");
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(foundTo);
    }

    @Test
    void testGetTableFile() {
        FileManager fm = new FileManager();
        Scanner scan = fm.getScanner("table");
        ArrayList<String> lines = new ArrayList<String>();
        if (scan != null) {
            while (scan.hasNextLine()) {
                lines.add(scan.nextLine());
            }
            scan.close();
        }
        ArrayList<Currency> table = new ArrayList<Currency>();
        table.add(new Currency("TESTAUD"));
        fm.updateTableFile(table);
        ArrayList<String> expected = new ArrayList<String>();
        expected.add("TESTAUD");
        ArrayList<String> actual = fm.getTableFile();
        table = new ArrayList<Currency>();
        for (String line: lines) {
            table.add(new Currency(line));
        }
        fm.updateTableFile(table);
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    void testUpdateTableFile() {
        FileManager fm = new FileManager();
        Scanner scan = fm.getScanner("table");
        ArrayList<String> lines = new ArrayList<String>();
        if (scan != null) {
            while (scan.hasNextLine()) {
                lines.add(scan.nextLine());
            }
            scan.close();
        }
        ArrayList<Currency> table = new ArrayList<Currency>();
        table.add(new Currency("TESTAUD"));
        fm.updateTableFile(table);
        scan = fm.getScanner("table");
        boolean tableFileUpdated = false;
        if (scan != null) {
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (line.equals(table.get(0).getSymbol())) {
                    tableFileUpdated = true;
                }
            }
            scan.close();
        }
        table = new ArrayList<Currency>();
        for (String line: lines) {
            table.add(new Currency(line));
        }
        fm.updateTableFile(table);
        assertTrue(tableFileUpdated);
    }
}
