package org.free.commons.components.utils;

import java.util.UUID;

public class UUIDUtils {
	
	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static void main(String[] args) {
		String s1 = UUID.randomUUID().toString();
		String s2 = uuid();
		System.out.println(s1 + "," + s1.length());
		System.out.println(s2 + "," + s2.length());
	}
}
