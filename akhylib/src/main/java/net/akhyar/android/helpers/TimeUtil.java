package net.akhyar.android.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {

	public static String formatMySqlTime(String dateString, String format) {
		try {
			Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK)
					.parse(dateString);

			return new SimpleDateFormat(format, Locale.UK).format(date);
		} catch (Exception e) {
			return new SimpleDateFormat(format, Locale.UK).format(new Date());
		}
	}
}
