package io.github.NadhifRadityo.ZamsNetwork.Core.Utilization;

import java.lang.reflect.Field;

public class Utility {
	public static String getInstance(Object data) {
		try {
			StringBuffer sb = new StringBuffer();
			Class<?> objClass = data.getClass();
			
			Field[] fields = objClass.getFields();
			for(Field field : fields) {
				String name = field.getName();
				Object value = field.get(data);
				
				sb.append(name + ": " + value.toString() + "\n");
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
