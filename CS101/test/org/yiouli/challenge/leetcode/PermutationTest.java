package org.yiouli.challenge.leetcode;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.yiouli.challenge.leetcode.Permutation;

public class PermutationTest {

	static void printList(List<int[]> l) {
		for(int[] a : l) {
			System.out.print(Arrays.toString(a)+", ");
		}
		System.out.println();
	}
	
	@Test
	public void testPermute() {
		int[] num = new int[]{1,3,2};
		List<int[]> res = Permutation.permute(num, false);
		printList(res);
	}
}
