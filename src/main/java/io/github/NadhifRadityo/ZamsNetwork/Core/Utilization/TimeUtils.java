package io.github.NadhifRadityo.ZamsNetwork.Core.Utilization;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimeUtils {
	public static String getTime(String format) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(timestamp);
	}
	
	public static String getTime() {
		return getTime("[HH:mm:ss]");
	}
}
