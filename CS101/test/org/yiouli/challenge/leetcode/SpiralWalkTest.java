package org.yiouli.challenge.leetcode;

import java.util.LinkedList;

import org.junit.Test;
import org.yiouli.challenge.leetcode.SpiralWalk;

public class SpiralWalkTest {
	
	@Test
	public void testWalk() {
		/*
		 *  1  2  3  4  5
		 *  6  7  8  9 10
		 * 11 12 13 14 15
		 * 16 17 18 19 20
		 */
		int n = 4, m = 5;
		int[][] grid = new int[n][m];
		for(int i=0;i<m*n;i++)
			grid[i/5][i%5] = i+1;
		LinkedList<Integer> res = SpiralWalk.spiralWalk(grid);
		for(Integer val : res)
			System.out.print(val+" ");
	}

}
