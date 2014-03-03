package helpers;

import java.util.Calendar;

public class ExampleTrials {
    
    public static void main(String args[]) {
    
        getSystemTime();
    }
    
    public static void getSystemTime() {
    
        long millis = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        System.out.println(cal.getTime().getHours());

        
    }
}
