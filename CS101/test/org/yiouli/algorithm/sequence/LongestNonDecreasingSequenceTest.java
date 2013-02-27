package org.yiouli.algorithm.sequence;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;
import org.yiouli.algorithm.sequence.LongestNonDecreasingSequence;

/**
 * 
 */

/**
 * @author leoSU
 *
 */
public class LongestNonDecreasingSequenceTest {

	Random r = new Random();
	
	protected boolean arrayEquals(int[] a, int[] b) {
		boolean ret = true;
		if(a == null || b== null || a.length != b.length)
			ret = false;
		else
			for(int i=0;i<a.length;i++)
				if(a[i] != b[i])
					ret = false;
		if(!ret)
			System.out.println(Arrays.toString(a)+" is not equal to "+Arrays.toString(b));
		return ret;
	}
	
	protected int[] LNDSQuandratic(int[] s) {
		int maxlen = 0;
		int end = 0;
		int n = s.length;
		int[] lens = new int[s.length];
		int[] trace = new int[s.length];
		for(int i=0;i<n;i++) {
			//max length of LNDS in s[0]...s[i-1]
			int len = 0;
			for(int j=0;j<i;j++) {
				if(s[j] <= s[i] && lens[j] > len) {
					len = lens[j];
					trace[i] = j;
				}
				else if(s[j] <= s[i] && lens[j] == len && s[trace[i]] > s[j])
					trace[i] = j;
			}
			lens[i] = len + 1;
			if(lens[i] > maxlen || (lens[i] == maxlen && s[i] < s[end])) {
				end = i;
				maxlen = lens[i];
			}
		}
		int[] ret = new int[maxlen];
		int idx = end;
		for(int i=maxlen;i>0;i--) {
			ret[i-1] = s[idx];
			idx = trace[idx];
		}
		return ret;
	}
	
	@Test
	public void testLNDSResult() {
		int testCount = 10000;
		int arrSize = 200;
		int minVal = 0, maxVal = 100;
		for(int i=0;i<testCount;i++) {
			int length = r.nextInt(arrSize)+1;
			int[] arr = generateRandomArray(length, minVal, maxVal);
			int[] res = LongestNonDecreasingSequence.LNDSResult(arr);
			int[] expected = LNDSQuandratic(arr);
			if(!arrayEquals(expected, res))
				System.out.println("from "+ Arrays.toString(arr));
			assertEquals(expected.length, res.length);
		}
	}
	
	@Test
	public void testLongestNonDecreasingSequence() {
		int testCount = 10000;
		int arrSize = 200;
		int minVal = 0, maxVal = 100;
		for(int i=0;i<testCount;i++) {
			int length = r.nextInt(arrSize)+1;
			int[] arr = generateRandomArray(length, minVal, maxVal);
			int res = LongestNonDecreasingSequence.lnds(arr);
			assertEquals(LNDSQuandratic(arr).length, res);
		}
	}

	/*
	 * Generate array of certain length, with each of its element's value is
	 * randomly set in the range of [lb,rb)
	 */
	protected int[] generateRandomArray(int length, int lb, int rb) {
		int[] ret = new int[length];
		for(int i=0;i<length;i++)
			ret[i] = r.nextInt(rb-lb)+lb;
		return ret;
	}
	
	//return i in [start-1,end] that arr[i]<=value<arr[i+1]
	protected int linearIntervalSearch(int[] arr, int start, int end, int value) {
		if(start == arr.length-1)
			return start;
		if(value < arr[start])
			return start-1;
		int ret = start;
		for(;ret<end;ret++) {
			if(value >= arr[ret] && value < arr[ret+1])
				break;
		}
		return ret;
	}
	
	@Test
	public void testBinaryIntervalSearch() {
		int testCount = 10000;
		int minVal = 10, maxVal = 1000;
		for(int i=0;i<testCount;i++) {
			int length = r.nextInt(1000)+1;
			int start = r.nextInt(length);
			int end = r.nextInt(length-start)+start;
			int value = r.nextInt();
			int[] arr = generateRandomArray(length, minVal, maxVal);
			Arrays.sort(arr);
			try {
				int res = LongestNonDecreasingSequence.binaryIntervalSearch(arr, start, end, value);
				assertEquals("arr length: "+arr.length, linearIntervalSearch(arr, start, end, value), res);
			}
			catch(IllegalArgumentException ex) {
				if(start != end)
					throw ex;
			}
		}
	}

}
