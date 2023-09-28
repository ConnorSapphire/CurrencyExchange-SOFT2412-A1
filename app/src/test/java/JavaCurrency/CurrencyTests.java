package JavaCurrency;
 
import java.util.Calendar;
import java.util.GregorianCalendar;
 
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
 
public class CurrencyTests {
    @Test 
    void testgetCurrencySymbol(){
        String symbol = "AUD";
        Currency aud = new Currency(symbol);
        String actualSymbol = aud.getSymbol();
        assertEquals(symbol, actualSymbol);
    }
    
    @Test 
    void testAddRateEdge(){
        Currency testCurrency = new Currency("TST");
        Currency otherCurrency = new Currency("OTH");
        Double otherValue = 0.96;
        testCurrency.addExchangeRate(otherCurrency, otherValue);
 
        assertTrue(testCurrency.getRate().containsKey(otherCurrency));
        assertTrue(testCurrency.getRate().get(otherCurrency) == 0.96);   
    }
 
    @Test 
    void testDateStringConversion(){
        String today = "17/09/2022";
        Calendar testDay = new GregorianCalendar(2022, 8, 17);
        String test = CurrencyHistory.calendarToString(testDay);
        assertEquals(today, test);
    }
 
    @Test
    void testStringDateConversion(){
        String date = "07/06/2022";
        Calendar testDate = new GregorianCalendar(2022, 5, 7);
        Calendar convDate = CurrencyHistory.convertDate(date);
 
        assertEquals(testDate, convDate);
    }
 
    @Test
    void testGetChangeUp(){
        Currency testCurrency = new Currency("TST");
        Currency otherCurrency = new Currency("OTH");
        Double oldValue = 0.95;
        Double newValue = 1.05;
        testCurrency.addExchangeRate(otherCurrency, oldValue);
        testCurrency.addExchangeRate(otherCurrency, newValue);
        assertEquals(testCurrency.getChange().get(otherCurrency), "↑ (I)");
    }
 
    @Test
    void testGetChangeDown(){
        Currency testCurrency = new Currency("TST");
        Currency otherCurrency = new Currency("OTH");
        Double oldValue = 1.05;
        Double newValue = 0.95;
        testCurrency.addExchangeRate(otherCurrency, oldValue);
        testCurrency.addExchangeRate(otherCurrency, newValue);
        assertEquals(testCurrency.getChange().get(otherCurrency), "↓ (D)");
    }
 
    @Test
    void testSingleDigitDayDateStringConversion(){
        String today = "02/09/2022";
        Calendar testDay = new GregorianCalendar(2022, 8, 2);
        String test = CurrencyHistory.calendarToString(testDay);
        assertEquals(today, test);
    }
 
    @Test
    void testDoubleDigitMonthStringConversion(){
        String today = "02/11/2022";
        Calendar testDay = new GregorianCalendar(2022, 10, 2);
        String test = CurrencyHistory.calendarToString(testDay);
        assertEquals(today, test);
    }
 
    @Test 
    void testGetExchangeValue(){
        Currency testCurrency = new Currency("TST");
        Currency otherCurrency = new Currency("OTH");
        Double otherValue = 0.96;
        testCurrency.addExchangeRate(otherCurrency, otherValue);
 
        double exchangeValue = testCurrency.getExchangeValue(otherCurrency);
        assertEquals(0.96, exchangeValue);
    }
 
    @Test 
    void testGetChangeNoChange(){
        Currency testCurrency = new Currency("TST");
        Currency otherCurrency = new Currency("OTH");
        Double otherValue = 0.96;
        testCurrency.addExchangeRate(otherCurrency, otherValue);
 
        Double newValue = 0.96; 
        testCurrency.addExchangeRate(otherCurrency, newValue);
 
        String changeValue = testCurrency.getChangeValue(otherCurrency);
        assertEquals("-", changeValue);
    }
 
    @Test
    void keyNotInRateEdge(){
        Currency newCurrency = new Currency("NCR");
        Currency missingCurrency = new Currency("MSC");
 
        Double existsActual = newCurrency.getExchangeValue(missingCurrency);
        Double existsExpected = -1.0;
 
        assertEquals(existsExpected, existsActual);
    }
 
    @Test
    void keyNotInChangeEdge(){
        Currency newCurrency = new Currency("NCR");
        Currency missingCurrency = new Currency("MSC");
 
        String existsActual = newCurrency.getChangeValue(missingCurrency);
        String existsExpected = "";
 
        assertEquals(existsExpected, existsActual);
    }
 
    @Test 
    void testUpdateHistory(){
        Currency firstCurrency = new Currency("ONE");
        Currency secondCurrency = new Currency("TWO");
        CurrencyHistory curHist = new CurrencyHistory(firstCurrency, secondCurrency);
 
        Calendar date = CurrencyHistory.convertDate("25/12/2025");
        Double rate = 12.54;
 
        curHist.updateHistory(date, rate);
        assertTrue(curHist.history.containsKey(date));
        assertTrue(curHist.history.get(date) == 12.54);
    }
 
    @Test
    void addSameRate(){
        Currency one = new Currency("ONE");
        Currency two = new Currency("TWO");

        Double twoInitialValue = 0.96;
        one.addExchangeRate(two, twoInitialValue);

        Double twoChangeNew = 0.97;
        one.addExchangeRate(two, twoChangeNew);

        Double twoNoChangeNew = 0.97;
        one.addExchangeRate(two, twoNoChangeNew);

        Double expectedRate = 0.97;
        String expectedChange = "-";

        assertEquals(expectedRate, one.getExchangeValue(two));
        assertEquals(expectedChange, one.getChangeValue(two));
    }


}
