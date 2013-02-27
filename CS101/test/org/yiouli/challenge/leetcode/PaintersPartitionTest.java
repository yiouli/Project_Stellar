package org.yiouli.challenge.leetcode;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.Random;

import org.junit.Test;
import org.yiouli.challenge.leetcode.PaintersPartition;
import org.yiouli.testutil.RandomGenerator;

public class PaintersPartitionTest {

	Random r = new Random();
	
	protected String getPartitionString(int[] a, LinkedList<Integer> p) {
		StringBuffer sb = new StringBuffer();
		int i=0;
		while(!p.isEmpty()) {
			int pi = p.removeFirst();
			while(i < pi) {
				sb.append(a[i] + " ");
				i++;
			}
			sb.append("| ");
		}
		for(;i<a.length;i++)
			sb.append(a[i] + " ");
		return sb.toString();
	}
	
	@Test
	public void testMinMaxSumPartition() {
		int[] a = new int[]{100,200,300,400,500,600,700,800,900};
		int k = 3;
		LinkedList<Integer> p = new LinkedList<Integer>();
		long expected = PaintersPartition.minMaxSumPartitionRecursive(a, k, p);
		System.out.println(getPartitionString(a, p));
		p.clear();
		long res = PaintersPartition.minMaxSumPartitionDP(a, k, p);
		System.out.println(getPartitionString(a, p));
		assertEquals(expected, res);
		long bsres = PaintersPartition.minMaxSumPartitionBS(a, k);
		assertEquals(res, bsres);
	}
	
	@Test
	public void testMinMaxSumPartitionLarge() {
		int tc = 1000;
		int minLen = 1, maxLen = 100, minVal = 0, maxVal = 1000;
		int maxk = 105;
		LinkedList<Integer> p = new LinkedList<Integer>();
		int fc = 0;
		for(int i=0;i<tc;i++) {
			int[] a = RandomGenerator.getRandomArray(minLen, maxLen, minVal, maxVal);
			int k =r.nextInt(maxk)+1;
			p.clear();
			long expected = PaintersPartition.minMaxSumPartitionDP(a, k, p);
			long res = PaintersPartition.minMaxSumPartitionBS(a, k);
			if(res != expected) {
				System.out.println(getPartitionString(a, p));
				System.out.println(k+" partitions: expected "+expected+" get "+res);
				fc++;
			}
		}
		assertTrue(fc +" out of "+tc+" are wrong.", fc==0);
	}
}
