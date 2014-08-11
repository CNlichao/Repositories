package com.global;

public class StringUtil {
	public static boolean isEmpty(String myString) {
		if (myString == null || myString.isEmpty()) {
			return true;
		}
		return false;
	}

}
