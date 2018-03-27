package com.ywzlp.webchat.msg.util;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

public abstract class ChineseUtil {
	
	private static final List<String> SORT_INDEX = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
	
	public static String getFullSpell(String chinese) {
		
		if (chinese == null) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		char[] arr = chinese.toCharArray();
		
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		
		for (char c : arr) {
			
			if (c > 128) {
				try {
					sb.append(PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat)[0]);
				} catch (Exception e) {
					sb.append("#");
				}
			} else {
				sb.append(c);
			}
			
		}
		
		return sb.toString();
	}
	
	public static String getSortIndex(String str) {
		
		if (str == null) {
			return null;
		}
		
		String first = getFirstAndToUpcase(str);
		
		if (SORT_INDEX.contains(first)) {
			return first;
		}
		
		return "#";
		
	}
	
	public static String getFirstAndToUpcase(String str) {
		return String.valueOf(str.charAt(0)).toUpperCase();
	}
	
}
