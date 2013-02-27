package org.yiouli.testutil;

import java.util.Random;

public class RandomGenerator {

	protected static Random r = new Random();
	
	public static String getRandomString(int minLen, int maxLen, char charBegin, char charEnd) {
		if(minLen < 1 || maxLen<minLen || charEnd<charBegin)
			throw new IllegalArgumentException();
		StringBuffer sb = new StringBuffer();
		int len = r.nextInt(maxLen-minLen+1) + minLen;
		for(int i=0;i<len;i++) {
			char c = (char)(charBegin + r.nextInt(charEnd-charBegin+1));
			sb.append(c);
		}
		return sb.toString();
	}
	
	public static String getRandomString(int minLen, int maxLen) {
		return getRandomString(minLen, maxLen, 'a', 'z');
	}
	
	public static String getRandomString(int len, char charBegin, char charEnd) {
		return getRandomString(len, len, charBegin, charEnd);
	}
	
	//a-z
	public static String getRandomString(int len) {
		return getRandomString(len, 'a', 'z');
	}
	
	public static String getRandomString() {
		return getRandomString(r.nextInt());
	}
	
	public static int[] getRandomArray(int minLen, int maxLen, int minVal, int maxVal) {
		int len = r.nextInt(maxLen - minLen + 1) + minLen;
		int[] ret = new int[len];
		for(int i=0;i<len;i++) {
			ret[i] = r.nextInt(maxVal - minVal + 1) + minVal;
		}
		return ret;
	}
}
