package org.yiouli.challenge.leetcode;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.yiouli.challenge.leetcode.Subset;

public class CombinationTest {

	@Test
	public void testGetSubsetsOfDistinctArray() {
		int[] a = new int[]{1,3,2};
		LinkedList<int[]> res = Subset.getSubsetsOfDistinctArrayRecursive(a);
		System.out.println(Subset.getContentString(res));
		res = Subset.getSubsetsOfDistinctArrayBit(a);
		System.out.println(Subset.getContentString(res));
		res = Subset.getSubsetsDistinctArrayIterate(a);
		System.out.println(Subset.getContentString(res));
		res = Subset.getSubsetsDistinctArrayBFS(a);
		System.out.println(Subset.getContentString(res));
		assertNotNull(res);
	}

	@Test
	public void testGetSubsetsDuplicates() {
		int[] a = new int[]{3,1,2,2};
		List<int[]> res = Subset.getSubsetsDuplicateBFS(a);
		System.out.println(Subset.getContentString(res));
		res = Subset.getSubsetsDuplicateIterate(a);
		System.out.println(Subset.getContentString(res));
		assertNotNull(res);
	}
}
