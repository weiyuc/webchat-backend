package com.ywzlp.webchat.msg.util;

import java.util.Comparator;

public class IndexComparator implements Comparator<String> {

	@Override
	public int compare(String o1, String o2) {
		if ("#".equals(o1)) {
			return 1;
		}
		return o1.compareTo(o2);
	}

}
