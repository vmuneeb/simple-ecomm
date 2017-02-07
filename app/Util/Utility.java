package Util;

/**
 * Created by muneeb on 27/01/17.
 */
public class Utility {
    public static String getFormattedPrice(double price) {
        double temp = price;
        String formattedPrice = "";
        if(price < 100) {
            return Double.toString(price);
        }
        return Double.toString(price);
    }

}
