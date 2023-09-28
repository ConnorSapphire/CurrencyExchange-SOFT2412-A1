package JavaCurrency;

import java.util.*;

public class RecentTable {
    public ArrayList<Currency> table;

    public RecentTable(){
        table = new ArrayList<>();
    }

    public ArrayList<Currency> getTable(){
        return table;
    }

    public void updateTable(Currency c){
        if(table.size() >= 4){
            table.remove(0);
        }
        boolean inTable = false;
        for(Currency currency : table){
            if(c == currency){
                inTable = true;
            }
        }
        if(!inTable){
            table.add(c);
        }
        
    }
}
