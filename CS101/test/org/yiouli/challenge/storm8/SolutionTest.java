package org.yiouli.challenge.storm8;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.yiouli.testutil.RandomGenerator;

public class SolutionTest {

	Solution sol = new Solution();
	int max = 2000;
	int maxVal = 200;
	
	protected int probe(int[][] tc, int i, int j, int total) {
		if(i == tc.length-1 && j == tc[0].length-1)
			return total + tc[i][j];
		total = total + tc[i][j];
		int down = -1, left = -1;
		if(i != tc.length-1)
			down = probe(tc, i+1, j, total);
		if(j != tc[0].length-1)
			left = probe(tc, i, j+1, total);
		return Math.max(down, left);
	}
	
	protected int brutalForce(int[][] tc) {
		if(tc.length == 0 || tc[0].length == 0)
			return 0;
		return probe(tc, 0, 0, 0);
	}
	
	@Test
	public void testZeros() {
		int[][] tc = new int[0][10];
		int[][] tc1 = new int[10][0];
		assertTrue(sol.rice_chessboard(tc) == 0);
		assertTrue(sol.rice_chessboard(tc1) == 0);
	}
	
	@Test
	public void testMax() {
		int[][] tc = new int[max][max];
		for(int i=0;i<max;i++)
			for(int j=0;j<max;j++)
				tc[i][j] = maxVal;
		assertEquals(maxVal * (2*max-1), sol.rice_chessboard(tc));
	}
	
	@Test
	public void testSingle() {
		int[][] tc = new int[1][1];
		int value = 10;
		tc[0][0] = value;
		assertEquals(value, sol.rice_chessboard(tc));
	}
	
	@Test
	public void testRandom() {
		int count = 1000;
		Random r= new Random();
		int fc = 0;
		for(int i=0;i<count;i++) {
			int m = r.nextInt(15+1), n = r.nextInt(15+1);
			int[][] tc = new int[n][];
			for(int j=0;j<n;j++)
				tc[j] = RandomGenerator.getRandomArray(m, m, 0, maxVal);
			int res = sol.rice_chessboard(tc);
			int expected = brutalForce(tc);
			if(res != expected)
				fc++;
		}
		assertTrue(fc == 0);
	}
}
