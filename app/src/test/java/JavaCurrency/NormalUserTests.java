package JavaCurrency;

import java.util.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class NormalUserTests {
    @Test
    void testMeanRegular(){
        ArrayList<Double> values = new ArrayList<>();
        values.add(1.0);
        values.add(2.0);
        values.add(3.0);
        values.add(4.0);
        values.add(5.0);
        values.add(6.0);
        assertEquals(3.5, NormalUser.mean(values));
    }

    @Test
    void testMeanEdge(){
        ArrayList<Double> values = new ArrayList<>();
        values.add(1.0);
        assertEquals(1.0, NormalUser.mean(values));
    }

    @Test
    void testMedianEven(){
        ArrayList<Double> values = new ArrayList<>();
        values.add(1.0);
        values.add(2.0);
        values.add(3.0);
        values.add(4.0);
        values.add(5.0);
        values.add(6.0);
        assertEquals(3.5, NormalUser.median(values));
    }

    @Test
    void testMedianOdds(){
        ArrayList<Double> values = new ArrayList<>();
        values.add(1.0);
        values.add(2.0);
        values.add(3.0);
        values.add(4.0);
        values.add(5.0);
        assertEquals(3.0, NormalUser.median(values));
    }

    @Test
    void testMedianZero(){
        ArrayList<Double> values = new ArrayList<>();
        assertEquals(0.0, NormalUser.median(values));
    }

    @Test
    void testMedianOne(){
        ArrayList<Double> values = new ArrayList<>();
        values.add(1.0);
        assertEquals(1.0, NormalUser.median(values));
    }

    @Test
    void testMaxRegular(){
        ArrayList<Double> values = new ArrayList<>();
        values.add(1.0);
        values.add(2.0);
        values.add(4.0);
        values.add(3.0);
        values.add(5.0);
        values.add(6.0);
        assertEquals(6.0, NormalUser.max(values));
    }

    @Test
    void testMaxOne(){
        ArrayList<Double> values = new ArrayList<>();
        values.add(1.0);
        assertEquals(1.0, NormalUser.max(values));
    }

    @Test
    void testMinRegular(){
        ArrayList<Double> values = new ArrayList<>();
        values.add(1.0);
        values.add(2.0);
        values.add(3.0);
        values.add(4.0);
        values.add(5.0);
        values.add(6.0);
        assertEquals(1.0, NormalUser.min(values));
    }

    @Test
    void testMinOne(){
        ArrayList<Double> values = new ArrayList<>();
        values.add(1.0);
        assertEquals(1.0, NormalUser.min(values));
    }

    @Test
    void testSdRegular(){
        ArrayList<Double> values = new ArrayList<>();
        values.add(1.0);
        values.add(2.0);
        values.add(3.0);
        values.add(4.0);
        values.add(5.0);
        values.add(6.0);
        assertEquals(1.71, Math.round(NormalUser.sd(values)*100.0)/100.0);
    }

    @Test
    void testSdOne(){
        ArrayList<Double> values = new ArrayList<>();
        values.add(1.0);
        assertEquals(0.0, NormalUser.sd(values));
    }

    @Test
    void testconvertMoneyRegular(){
        NormalUser user = new NormalUser("n", "pwd");
        Currency from = new Currency("aud");
        Currency to = new Currency("cny");
        Double amount = 10.0;
        from.addExchangeRate(to, 4.69);
        assertEquals(46.90, Math.round(user.convertMoney(from, to, amount)*100.0)/100.0);
    }

    @Test
    void testconvertMoneyNoRate(){
        NormalUser user = new NormalUser("n", "pwd");
        Currency from = new Currency("aud");
        Currency to = new Currency("cny");
        Double amount = 10.0;
        assertEquals(null, user.convertMoney(from, to, amount));
    }

    @Test
    void testHistoryRatesRegular(){
        NormalUser user = new NormalUser("n", "pwd");
        Currency from = new Currency("aud");
        Currency to = new Currency("cny");
        Currency otherc = new Currency("symbol");
        Calendar start = CurrencyHistory.convertDate("10/09/2022");
        Calendar end = CurrencyHistory.convertDate("20/09/2022");
        HashMap<Currency[], CurrencyHistory> history = new HashMap<>();
        CurrencyHistory his = new CurrencyHistory(from, to);
        CurrencyHistory other = new CurrencyHistory(to, from);
        his.updateHistory(start, 4.7);
        his.updateHistory(end, 4.5);
        his.updateHistory(CurrencyHistory.convertDate("20/08/2022"), 4.0);
        his.updateHistory(CurrencyHistory.convertDate("20/08/2025"), 4.0);
        history.put(new Currency[]{from, to}, his);
        history.put(new Currency[]{from, otherc}, other);
        history.put(new Currency[]{otherc, to}, other);
        ArrayList<Double> amount = new ArrayList<>();
        amount.add(4.5);
        amount.add(4.7);
        amount.add(4.6);
        amount.add(4.6);
        amount.add(4.7);
        amount.add(4.5);
        amount.add(0.1);
        assertEquals(amount, user.historyRates(from, to, start, end, history));
    }

    @Test
    void testHistoryRatesOne(){
        NormalUser user = new NormalUser("n", "pwd");
        Currency from = new Currency("aud");
        Currency to = new Currency("cny");
        Calendar start = CurrencyHistory.convertDate("10/09/2022");
        Calendar end = CurrencyHistory.convertDate("20/09/2022");
        HashMap<Currency[], CurrencyHistory> history = new HashMap<>();
        CurrencyHistory his = new CurrencyHistory(from, to);
        his.updateHistory(start, 4.7);
        history.put(new Currency[]{from, to}, his);
        ArrayList<Double> amount = new ArrayList<>();
        amount.add(4.7);
        amount.add(4.7);
        amount.add(4.7);
        amount.add(4.7);
        amount.add(4.7);
        amount.add(0.0);
        assertEquals(amount, user.historyRates(from, to, start, end, history));
    }

    @Test
    void testHistoryRatesNoTime(){
        NormalUser user = new NormalUser("n", "pwd");
        Currency from = new Currency("aud");
        Currency to = new Currency("cny");
        Calendar start = CurrencyHistory.convertDate("10/09/2022");
        Calendar end = CurrencyHistory.convertDate("20/09/2022");
        HashMap<Currency[], CurrencyHistory> history = new HashMap<>();
        CurrencyHistory his = new CurrencyHistory(from, to);
        his.updateHistory(CurrencyHistory.convertDate("10/09/2012"), 4.7);
        history.put(new Currency[]{from, to}, his);
        assertEquals(null, user.historyRates(from, to, start, end, history));
    }

    @Test
    void testHistoryRatesNoCurrencyHistory(){
        NormalUser user = new NormalUser("n", "pwd");
        Currency from = new Currency("aud");
        Currency to = new Currency("cny");
        Calendar start = CurrencyHistory.convertDate("10/09/2022");
        Calendar end = CurrencyHistory.convertDate("20/09/2022");
        HashMap<Currency[], CurrencyHistory> history = new HashMap<>();
        CurrencyHistory his = new CurrencyHistory(to, from);
        his.updateHistory(start, 4.7);
        history.put(new Currency[]{to, from}, his);
        assertEquals(null, user.historyRates(from, to, start, end, history));
    }

    @Test
    void testPopularTableSimple(){
        NormalUser user = new NormalUser("n", "pwd");
        Currency a = new Currency("aud");
        Currency b = new Currency("cny");
        Currency c = new Currency("usd");
        Currency d = new Currency("inr");
        RecentTable table = new RecentTable();
        table.updateTable(a);
        table.updateTable(b);
        table.updateTable(c);
        table.updateTable(d);
        String[][] display = {{"From/To", a.getSymbol(), b.getSymbol(), c.getSymbol(), d.getSymbol()}, {a.getSymbol(), "-", " ", " ", " "}, {b.getSymbol(), " ", "-", " ", " "}, {c.getSymbol(), " ", " ", "-", " "}, {d.getSymbol(), " ", " ", " ", "-"}};
        assertArrayEquals(display, user.popularTable(table));
    }

    @Test
    void testPopularTableRegular(){
        NormalUser user = new NormalUser("n", "pwd");
        Currency a = new Currency("aud");
        Currency b = new Currency("cny");
        Currency c = new Currency("usd");
        Currency d = new Currency("inr");
        a.addExchangeRate(b, 4.5);
        a.addExchangeRate(c, 0.9);
        RecentTable table = new RecentTable();
        table.updateTable(a);
        table.updateTable(b);
        table.updateTable(c);
        table.updateTable(d);
        String[][] display = {{"From/To", a.getSymbol(), b.getSymbol(), c.getSymbol(), d.getSymbol()}, {a.getSymbol(), "-", String.format("%.2f", Math.round(a.getExchangeValue(b)*100.0)/100.0), String.format("%.2f", Math.round(a.getExchangeValue(c)*100.0)/100.0), " "}, {b.getSymbol(), String.format("%.2f", Math.round(b.getExchangeValue(a)*100.0)/100.0), "-", " ", " "}, {c.getSymbol(), String.format("%.2f", Math.round(c.getExchangeValue(a)*100.0)/100.0), " ", "-", " "}, {d.getSymbol(), " ", " ", " ", "-"}};
        assertArrayEquals(display, user.popularTable(table));
    }

    @Test
    void testPopularTableNoEnoughCurrencies(){
        NormalUser user = new NormalUser("n", "pwd");
        Currency a = new Currency("aud");
        Currency b = new Currency("cny");
        Currency c = new Currency("usd");
        a.addExchangeRate(b, 4.5);
        a.addExchangeRate(c, 0.9);
        RecentTable table = new RecentTable();
        table.updateTable(a);
        table.updateTable(b);
        table.updateTable(c);
        assertArrayEquals(null, user.popularTable(table));
    }

    @Test
    void testPopularTableChange(){
        NormalUser user = new NormalUser("n", "pwd");
        Currency a = new Currency("aud");
        Currency b = new Currency("cny");
        Currency c = new Currency("usd");
        Currency d = new Currency("inr");
        a.addExchangeRate(b, 4.5);
        a.addExchangeRate(c, 0.9);
        a.addExchangeRate(b, 4.5);
        a.addExchangeRate(c, 1.1);
        RecentTable table = new RecentTable();
        table.updateTable(a);
        table.updateTable(b);
        table.updateTable(c);
        table.updateTable(d);
        String[][] display = {{"From/To", a.getSymbol(), b.getSymbol(), c.getSymbol(), d.getSymbol()}, {a.getSymbol(), "-", String.format("%.2f", Math.round(a.getExchangeValue(b)*100.0)/100.0), String.format("%.2f", Math.round(a.getExchangeValue(c)*100.0)/100.0) + " " + a.getChangeValue(c), " "}, {b.getSymbol(), String.format("%.2f", Math.round(b.getExchangeValue(a)*100.0)/100.0), "-", " ", " "}, {c.getSymbol(), String.format("%.2f", Math.round(c.getExchangeValue(a)*100.0)/100.0) + " " + c.getChangeValue(a), " ", "-", " "}, {d.getSymbol(), " ", " ", " ", "-"}};
        assertArrayEquals(display, user.popularTable(table));
    }
}
