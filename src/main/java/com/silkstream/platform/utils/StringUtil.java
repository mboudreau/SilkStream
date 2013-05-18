package com.silkstream.platform.utils;

public class StringUtil {
	static public Boolean isNotNullOrEmpty(String value) {
		return value != null && value.length() != 0;
	}
}
