package JavaCurrency;

import java.util.*;

public class NormalUser {
    public String name;
    public String password;

    public NormalUser(String n, String pwd){
        name = n;
        password = pwd;
    }

    public String[][] popularTable(RecentTable t){
        ArrayList<Currency> table = t.getTable();
        if(table.size() != 4){
            return null;
        }
        String[][] display = {{"From/To", table.get(0).getSymbol(), table.get(1).getSymbol(), table.get(2).getSymbol(), table.get(3).getSymbol()}, {table.get(0).getSymbol(), "-", " ", " ", " "}, {table.get(1).getSymbol(), " ", "-", " ", " "}, {table.get(2).getSymbol(), " ", " ", "-", " "}, {table.get(3).getSymbol(), " ", " ", " ", "-"}};
        Currency c = null;
        for(int i = 0; i < table.size(); i++){
            c = table.get(i);
            for(int j = 0; j < table.size(); j++){
                Currency other = table.get(j);
                if(other == c){
                    continue;
                }
                if(c.getExchangeValue(other) == -1){
                    continue;
                }
                String change = c.getChangeValue(other);
                if(c.getChangeValue(other).equals("-") || c.getChangeValue(other).equals("")){
                    change = "";
                }else{
                    change = " " + change;
                }
                display[i + 1][j + 1] = String.format("%.2f", Math.round(c.getExchangeValue(other)*100.0)/100.0) + change;
            }
        }
        return display;
    }

    public ArrayList<Double> historyRates(Currency from, Currency to, Calendar start, Calendar end, HashMap<Currency[], CurrencyHistory> history){
        int find = 0;
        HashMap<Calendar, Double> details = null;
        for(Currency[] c : history.keySet()){
            if(c[0] == from && c[1] == to){
                find = 1;
                details = history.get(c).history;
                break;
            }
        }
        if(find == 0){
            System.out.println("History not found!");
            return null;
        }
        ArrayList<Double> arr = new ArrayList<>();
        for(Calendar c : details.keySet()){
            if((c.compareTo(start) >= 0) && (c.compareTo(end) <= 0)){
                arr.add(details.get(c));
            }
        }
        String rate = "";
        for(int i = 0; i < arr.size(); i++){
            rate = rate + " " + Double.toString(arr.get(i));
        }
        if(arr.size() == 0){
            System.out.println("No history found!");
            return null;
        }else{
            System.out.println("Convert rates: " + rate);
            System.out.println("Average: " + Double.toString(mean(arr)));
            System.out.println("Median: " + Double.toString(median(arr)));
            System.out.println("Maximum: " + Double.toString(max(arr)));
            System.out.println("Minimum: " + Double.toString(min(arr)));
            System.out.println("Standard Deviation: " + Double.toString(sd(arr)));
        }
        ArrayList<Double> value = new ArrayList<>();
        arr.sort(null);
        for(int i = 0; i < arr.size(); i++){
            value.add(arr.get(i));
        }
        value.add(mean(arr));
        value.add(median(arr));
        value.add(max(arr));
        value.add(min(arr));
        value.add(sd(arr));
        return value;
    }

    public static double median(ArrayList<Double> values) {

        if(values.size() == 0){
            return 0.0;
        }else if(values.size() == 1){
            return values.get(0);
        }else{
            values.sort(null);
            int middle = values.size() / 2;
            if (values.size() % 2 == 1) {
                return values.get(middle);
            } else {
                return Math.round(((values.get(middle - 1) + values.get(middle)) / 2.0)*100.0)/100.0;
            }
        }

    }

    public static double mean(ArrayList<Double> values) {
        int len = values.size();
        double sum = 0;
        for(int i = 0; i < len; i++){
            sum = sum + values.get(i);
        }
        double num = (double) len;
        return Math.round(sum*100.0/num)/100.0;
    }

    public static double max(ArrayList<Double> values) {
        int len = values.size();
        double max = Double.MIN_VALUE;
        for(int i = 0; i < len; i++){
            if(values.get(i) > max){
                max  = values.get(i);
            }
        }
        return max;
    }

    public static double min(ArrayList<Double> values) {
        int len = values.size();
        double min = Double.MAX_VALUE;
        for(int i = 0; i < len; i++){
            if(values.get(i) < min){
                min  = values.get(i);
            }
        }
        return min;
    }

    public static double sd(ArrayList<Double> values) {

        double m = mean(values);
        double sum = 0;
        int len = values.size();
        for(int i = 0; i < len; i++){
            sum += Math.pow(values.get(i) - m, 2);
        }
        double s = (double) len;
        return Math.round(Math.sqrt(sum/s)*100.0)/100.0;

    }

    public Double convertMoney(Currency from, Currency to, Double amount){
        Double d = from.getExchangeValue(to) * amount;
        if(d < 0){
            System.out.println("The change rate is not specified!");
            return null;
        }
        return d;
    }
}
