package org.yiouli.challenge.leetcode;

public class AddBinary {

	public static String addBinary(String a, String b) {
		if(a.length() < b.length())
			return addBinary(b, a);
		StringBuffer sb = new StringBuffer();
		int n = a.length(), m = b.length();
		int carry = 0;
		for(int i=n-1;i>=0;i--) {
			int digit = '0';
			if(i-n+m>=0)
				digit = b.charAt(i-n+m);
			int val = a.charAt(i)+digit-2*'0'+carry;
			sb.insert(0, val%2);
			carry = val/2;
		}
		if(carry > 0)
			sb.insert(0, carry);
		return sb.toString();
	}
}
