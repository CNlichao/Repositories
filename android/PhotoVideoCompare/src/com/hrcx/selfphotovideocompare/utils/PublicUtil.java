package com.hrcx.selfphotovideocompare.utils;

import java.util.Calendar;

public class PublicUtil {

	public static int getSfzhDrawColor(String validation, String checkYear) {
		Calendar cal = Calendar.getInstance();
		Integer yearInteger = Integer.valueOf(cal.get(Calendar.YEAR));
		String year = yearInteger.toString();
		if (year.equals(checkYear)) {
			if(validation.equals("0") || validation.equals("1") || validation.equals("10") || validation.equals("20")) {
				return Constant.GREEN;
			} else if (validation.equals("12") || validation.equals("22")) {
				return Constant.BLUE;
			} else if (validation.equals("11") || validation.equals("21")) {
				return Constant.RED;
			}
		} else {
			return Constant.RED;
		}
		
		return -1;
	}
}
