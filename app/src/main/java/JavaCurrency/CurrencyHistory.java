package JavaCurrency;

import java.util.*;
import java.util.Calendar;

public class CurrencyHistory {
    public Currency from;
    public Currency to;
    public HashMap<Calendar, Double> history;

    public CurrencyHistory(Currency from, Currency to){
        this.from = from;
        this.to = to;
        history = new HashMap<>();
    }

    public void updateHistory(Calendar date, Double d){
        history.put(date, d);
    }
    
    public static Calendar convertDate(String date) {
        String[] splitDate = date.split("/");
        Calendar convertedDate = new GregorianCalendar(Integer.parseInt(splitDate[2]), Integer.parseInt(splitDate[1])-1, Integer.parseInt(splitDate[0]));

        return convertedDate;
    }
    
    public static String calendarToString(Calendar date){
        String year = Integer.toString(date.get(Calendar.YEAR));

        int monthInitial = date.get(Calendar.MONTH) + 1;
        String month = Integer.toString(monthInitial);
        if (monthInitial < 10){
            month = "0" + month;
        }

        int dayInitial = date.get(Calendar.DATE);
        String day = Integer.toString(dayInitial);
        if (dayInitial < 10){
            day = "0" + day;
        }

        String finalDate = day + "/" + month + "/" + year;
        return finalDate;
    }

}
