package com.ywzlp.webchat.msg.util;

import java.util.UUID;

public abstract class TokenGenerator {
	
	private static final char[] hexCode = {'1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

	public static String generateToken() {
		return toHexString(UUID.randomUUID().toString().getBytes());
	}

	public static String toHexString(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (byte b : bytes) {
			sb.append(hexCode[(b >> 4) & 0xF]);
			sb.append(hexCode[(b & 0xF)]);
		}
		return sb.toString();
	}
	
}
