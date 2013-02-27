package org.yiouli.algorithm.sequence;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.Random;

import org.junit.Test;
import org.yiouli.testutil.RandomGenerator;

public class LongestCommonSequenceTest {

	protected boolean isSubsequence(String sub, String a) {
		if(sub == null || a == null)
			throw new IllegalArgumentException();
		if(sub.length() == 0)
			return true;
		int si = 0;
		for(int i=0;i<a.length();i++) {
			if(a.charAt(i) == sub.charAt(si)) {
				si++;
				if(si == sub.length())
					return true;
			}
		}
		return false;
	}
	
	protected String getSubsequence(String a) {
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		int prev = 0;
		while(prev < a.length()) {
			prev = r.nextInt(a.length()-prev) + prev + 1;
			sb.append(a.charAt(prev-1));
		}
		return sb.toString();
	}

	//n should be less than 63
	protected LinkedList<String> getAllSubsequences(String a) {
		LinkedList<String> ret = new LinkedList<String>();
		int n = a.length();
		for(long i=0;i<1<<n;i++) {
			StringBuffer sb = new StringBuffer();
			for(int j=0;j<n;j++) {
				if((1<<j&i)!=0)
					sb.append(a.charAt(j));
			}
			ret.add(sb.toString());
		}
		return ret;
	}
	
	protected String naiveLcsResult(String a, String b) {
		if(a == null || b == null)
			throw new IllegalArgumentException();
		String shorter = a, longer = b;
		if(b.length() < a.length()) {
			shorter = b;
			longer = a;
		}
		LinkedList<String> sss = getAllSubsequences(shorter);
		String ret = "";
		for(String s : sss)
			if(s.length() > ret.length() && isSubsequence(s, longer))
				ret = s;
		return ret;
	}
	
	@Test
	public void testIsSubsequence() {
		int tc = 1000;
		int minLen = 10, maxLen = 1000;
		int failCount = 0;
		for(int i=0;i<tc;i++) {
			String a = RandomGenerator.getRandomString(minLen, maxLen, 'a', 'z');
			for(int j=0;j<tc;j++) {
				String ss = getSubsequence(a);
				if(!isSubsequence(ss,a)) {
					System.out.println(ss+ " should be subsequence of "+a);
					failCount++;
				}
			}
		}
		System.out.println(failCount + " out of "+tc*tc +" are wrong");
		assertTrue(failCount == 0);
	}
	
	@Test
	public void testGetAllSubsequences() {
		int tc = 500;
		int minlen = 1, maxlen = 15;
		int fc = 0;
		for(int i=0;i<tc;i++) {
			String s = RandomGenerator.getRandomString(minlen, maxlen);
			LinkedList<String> sss = getAllSubsequences(s);
			boolean pass = true;
			System.out.println(s+" :");
			if(sss.size() != 1 << s.length()) {
				pass = false;
			}
			for(String ss : sss)
				if(!isSubsequence(ss, s)) {
					pass = false;
					System.out.print(ss);
				}
			System.out.println();
			if(!pass)
				fc++;
		}
		assertTrue(fc == 0);
	}
	
	@Test
	public void testNaiveLcs() {
		int tc = 100;
		int minLen = 1, maxLen = 15;
		int fc = 0;
		for(int i=0;i<tc;i++) {
			String a = RandomGenerator.getRandomString(minLen, maxLen, 'a', 'c');
			String b = RandomGenerator.getRandomString(minLen, maxLen, 'a', 'c');
			String ss = naiveLcsResult(a, b);
			System.out.print(a+", "+b+" : "+ss);
			if(!isSubsequence(ss, a) || !isSubsequence(ss, b)) {
				System.out.print(" WRONG!!!");
				fc++;
			}
			System.out.println();
		}
		assertTrue(fc == 0);
	}
	
	@Test
	public void testLcs() {
			
	}

}
