package org.yiouli.challenge.leetcode;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;
import org.yiouli.testutil.RandomGenerator;

public class MedianOfTwoSortedArrayTest {

	static float findMedianLinear(int[] a, int[] b, boolean print) {
		int l = a.length, m = b.length;
		int[] combined = new int[l+m];
		int i=0, ai=0, bi=0;
		while(ai != l || bi != m) {
			int ah = ai==l?Integer.MAX_VALUE:a[ai];
			int bh = bi==m?Integer.MAX_VALUE:b[bi];
			combined[i] = ah < bh ? a[ai++] : b[bi++];
			i++;
		}
		if(print) {
			System.out.println(Arrays.toString(a));
			System.out.println(Arrays.toString(b));
			System.out.println(Arrays.toString(combined));
		}
		if((l+m)%2==0)
			return ((float)combined[(l+m)/2-1]+combined[(l+m)/2])/2;
		else
			return combined[(l+m)/2];
	}
	
	@Test
	public void testFindMedianIntArrayIntArray() {
		int tc = 10000, fc = 0;
		int minLen = 1, maxLen = 100, minVal = -1000, maxVal = 1000;
		for(int i=0;i<tc;i++) {
			int[] a = RandomGenerator.getRandomArray(minLen, maxLen, minVal, maxVal);
			Arrays.sort(a);
			int[] b = RandomGenerator.getRandomArray(minLen, maxLen, minVal, maxVal);
			Arrays.sort(b);
			float expected = findMedianLinear(a,b,false);
			float res = MedianOfTwoSortedArray.findMedianDivideArray(a, b);
			if(res != expected) {
				fc++;
				System.out.println("Expected "+expected+" get "+res);
			}
		}
		assertEquals(fc+" out of "+tc+" are wrong.", 0, fc);
	}

}
