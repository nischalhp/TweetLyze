package helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utilities {
	public static Date getCurrentDate() throws ParseException {

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date currentDate = cal.getTime();
		currentDate = formatter.parse(formatter.format(currentDate));
		return currentDate;
	}

	public static int getSystemTime() {

		long millis = System.currentTimeMillis();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		return (cal.getTime().getHours());

	}
}
