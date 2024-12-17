package com.r4l.waystone_organiser.reference.functions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexHandler {

	public static boolean preg_match(String str, String regex) {
		
		Pattern MY_PATTERN = Pattern.compile(regex);
		Matcher m = MY_PATTERN.matcher(str);
		
		return m.find();
		
	}
	
	public static String preg_replace(String str, String regex, String replacement) {
		return str.replaceAll(regex, replacement);
	}

}