package JavaCurrency;
import java.util.HashMap;

public class Currency {
    
    private String symbol;
    private HashMap<Currency, Double> rate;
    private HashMap<Currency, String> change;

    public Currency(String symbol) {
        this.symbol = symbol;
        this.rate = new HashMap<>();
        this.change = new HashMap<>();
    }

    public void setChange(HashMap<Currency, String> c){
        change = c;
    }

    // Add the exchange rate to the hashmap
    // If it has the previous rate, define it gets greater or smaller
    public void addExchangeRate(Currency c, Double value){
        if(rate.containsKey(c)){
            Double previous = rate.get(c);
            if(previous > value){
                if(change.containsKey(c)){
                    change.replace(c, "↓ (D)");
                    c.change.replace(this, "↑ (I)");
                }else{
                    change.put(c, "↓ (D)");
                    c.change.put(this, "↑ (I)");
                }
            }else if(previous < value){
                if(change.containsKey(c)){
                    change.replace(c, "↑ (I)");
                    c.change.replace(this, "↓ (D)");
                } else{
                    change.put(c, "↑ (I)");
                    c.change.put(this, "↓ (D)");
                }
            } else{
                if (change.containsKey(c)){
                    change.replace(c, "-");
                    c.change.replace(this, "-");
                } else{
                    change.put(c, "-");
                    c.change.put(this, "-");
                }
            }
            rate.replace(c, value);
            c.rate.replace(this, 1/value);
        } else{
            rate.put(c, value);
            c.rate.put(this, 1/value);
        }
    }

    // Getter methods 
    public String getSymbol(){
        return symbol;
    }

    public HashMap<Currency, Double> getRate() {
        return rate;
    }

    public HashMap<Currency, String> getChange() {
        return change;
    }
    
    public double getExchangeValue(Currency c){
        if(rate.containsKey(c)){
            return rate.get(c);
        }
        return -1;
    }
    
    public String getChangeValue(Currency c){
        if(change.containsKey(c)){
            return change.get(c);
        }
        return "";
    }

}
